package com.QuestSys.Conditions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.QuestSys.Events.EventTypes;
import com.QuestSys.Events.Subscribe;
import com.QuestSys.Executors.Executor;

public class Swim extends Condition {
	private Biome biome;

	public Swim(double targetCount, Biome biome) {
		super("swim", EventTypes.PlayerMoveEvent, targetCount);
		this.biome = biome;
	}


	public static Condition create(Map<?, ?> values) {
		Biome biome = null;
		if (values.get("biome") instanceof String bn) {
			try {
				biome = Biome.valueOf(bn.toUpperCase());
			} catch (IllegalArgumentException ignored) {
			}
		}
		double targetCount = (int) ParseTargetCount(values);
		return new Swim(targetCount, biome);
	}

	@Override
	protected void onEvent(Event e) {
		PlayerMoveEvent event = (PlayerMoveEvent)e;

		if (!event.getPlayer().isSwimming()) return;

		if (biome != null) {
			Biome playerBiome = event.getPlayer().getLocation().getBlock().getBiome();
			if (playerBiome != biome) return;
		}

		count+=event.getTo().clone().subtract(event.getFrom()).length();
		checkCondition();
	}

	@Override
	public Map<String, Object> GetData() {
		Map<String, Object> data = new LinkedHashMap<>();

		if (biome != null) {
			data.put("biome", biome.name());
		}
		data.put("target_count", this.GetTargetCount());
		data.put("count", this.count);

		Map<String, Object> wrapper = new LinkedHashMap<>();
		wrapper.put("swim", data);
		return wrapper;
	}

	@Override
	public void SetData(Map<String, Object> data) {
		if (data.containsKey("biome")) {
			try {
				biome = Biome.valueOf(((String) data.get("biome")).toUpperCase());
			} catch (IllegalArgumentException ignored) {
			}
		}
		if (data.containsKey("target_count")) {
			targetCount = ((Number) data.get("target_count")).doubleValue();
		}
		if (data.containsKey("count")) {
			count = ((Number) data.get("count")).doubleValue();
		}
	}

	@Override
	public String toString() {
		String str = "swim:\n"
				+ "    count: " + this.count
				+ "\n    target_count: " + this.GetTargetCount();

		if (biome != null)
			str += "\n    biome: " + biome.name();
		return str;
	}
}
