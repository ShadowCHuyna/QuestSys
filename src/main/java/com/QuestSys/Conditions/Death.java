package com.QuestSys.Conditions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.damage.DamageType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.QuestSys.Events.EventTypes;

public class Death extends Condition {
	private DamageType damageType;

	public Death(double targetCount, DamageType damageType) {
		super("death", EventTypes.PlayerDeathEvent, targetCount);
		this.damageType = damageType;
	}

	public static Condition create(Map<?, ?> values) {
		DamageType damageType = null;
		if (values.get("damage_type") instanceof String dt) {
			damageType = Registry.DAMAGE_TYPE.get(NamespacedKey.minecraft(dt.toLowerCase()));
		}
		double targetCount = (int) ParseTargetCount(values);
		return new Death(targetCount, damageType);
	}

	@Override
	protected void onEvent(Event e) {
		System.err.println("AAAAAAAAAAA");
		PlayerDeathEvent event = (PlayerDeathEvent) e;
		
		if (damageType != null) {
			DamageType eventDamageType = event.getDamageSource().getDamageType();
			if (!damageType.equals(eventDamageType)) {
				return;
			}
		}
		
		count++;
		checkCondition();
	}

	@Override
	public Map<String, Object> GetData() {
		Map<String, Object> data = new LinkedHashMap<>();
		
		if (damageType != null) {
			data.put("damage_type", damageType.getKey().getKey());
		}
		data.put("target_count", this.GetTargetCount());
		data.put("count", this.count);

		Map<String, Object> wrapper = new LinkedHashMap<>();
		wrapper.put("death", data);
		return wrapper;
	}

	@Override
	public void SetData(Map<String, Object> data) {
		if (data.containsKey("damage_type")) {
			damageType = Registry.DAMAGE_TYPE.get(NamespacedKey.minecraft(((String) data.get("damage_type")).toLowerCase()));
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
		String str = "death:\n"
				+ "    count: " + this.count
				+ "\n    target_count: " + this.GetTargetCount();

		if (damageType != null)
			str += "\n    damage_type: " + damageType.getKey().getKey();
		return str;
	}
}
