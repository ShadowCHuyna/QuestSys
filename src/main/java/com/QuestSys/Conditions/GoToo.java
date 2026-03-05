package com.QuestSys.Conditions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.QuestSys.Events.EventTypes;

public class GoToo extends Condition {
	private Location targetLocation;
	private double acceptableError;
	private String worldName;

	public GoToo(Location targetLocation, double acceptableError) {
		super("go_too", EventTypes.PlayerMoveEvent, 1);
		this.targetLocation = targetLocation;
		this.acceptableError = acceptableError;
		this.worldName = targetLocation.getWorld() != null ? targetLocation.getWorld().getName() : "";
	}

	public static Condition create(Map<?, ?> values) {
		if (!(values.get("position") instanceof Map<?, ?> pos))
			throw new IllegalArgumentException("position is required");


		World world = null;
		if (values.get("world") instanceof String worldName) {
			world = Bukkit.getWorld(worldName);
			if (world == null)
				throw new IllegalArgumentException("World not found: " + worldName);
		}

		double x = getDouble(pos, "x", 0);
		double y = getDouble(pos, "y", 0);
		double z = getDouble(pos, "z", 0);

		Location location = new Location(world, x, y, z);

		double acceptableError = getDouble(values, "acceptable_error", 5);

		return new GoToo(location, acceptableError);
	}

	private static double getDouble(Map<?, ?> map, String key, double defaultValue) {
		Object value = map.get(key);
		if (value instanceof Number)
			return ((Number) value).doubleValue();

		return defaultValue;
	}

	@Override
	protected void onEvent(Event event) {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		Location playerLoc = e.getTo();

		if (playerLoc.getWorld() == null || targetLocation.getWorld() == null) return;

		if (!playerLoc.getWorld().getName().equals(targetLocation.getWorld().getName())) return;

		double distance = playerLoc.distance(targetLocation);
		if (distance <= acceptableError) {
			count = targetCount;
			checkCondition();
		}
	}

	@Override
	public Map<String, Object> GetData() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("world", worldName);

		Map<String, Object> position = new LinkedHashMap<>();
		position.put("x", targetLocation.getX());
		position.put("y", targetLocation.getY());
		position.put("z", targetLocation.getZ());
		data.put("position", position);

		data.put("acceptable_error", acceptableError);
		data.put("count", this.count);

		Map<String, Object> wrapper = new LinkedHashMap<>();
		wrapper.put("go_too", data);
		return wrapper;
	}

	@Override
	public void SetData(Map<String, Object> data) {
		if (data.containsKey("world")) {
			World world = Bukkit.getWorld((String) data.get("world"));
			if (data.get("position") instanceof Map<?, ?> pos) {
				double x = getDouble(pos, "x", 0);
				double y = getDouble(pos, "y", 0);
				double z = getDouble(pos, "z", 0);
				targetLocation = new Location(world, x, y, z);
			}
		}
		if (data.containsKey("acceptable_error"))
			acceptableError = ((Number) data.get("acceptable_error")).doubleValue();

		if (data.containsKey("count"))
			count = ((Number) data.get("count")).doubleValue();
	}

	@Override
	public String toString() {
		String str = "go_too:\n"
				+ "    count: " + this.count
				+ "\n    target_count: " + this.GetTargetCount()
				+ "\n    world: " + worldName
				+ "\n    position:"
				+ "\n        x: " + targetLocation.getX()
				+ "\n        y: " + targetLocation.getY()
				+ "\n        z: " + targetLocation.getZ()
				+ "\n    acceptable_error: " + acceptableError;
		return str;
	}
}
