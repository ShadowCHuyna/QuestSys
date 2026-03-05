package com.QuestSys.Conditions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.event.Event;

import com.QuestSys.Events.EventTypes;

import io.papermc.paper.event.player.PlayerDeepSleepEvent;

public class Sleep extends Condition {

	public Sleep(double targetCount) {
		super("sleep", EventTypes.PlayerDeepSleepEvent, targetCount);
	}

	public static Condition create(Map<?, ?> values) {
		double targetCount = (int) ParseTargetCount(values);
		return new Sleep(targetCount);
	}

	@Override
	protected void onEvent(Event e) {
		PlayerDeepSleepEvent event = (PlayerDeepSleepEvent) e;
		count++;
		checkCondition();
	}

	@Override
	public Map<String, Object> GetData() {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("target_count", this.GetTargetCount());
		data.put("count", this.count);

		Map<String, Object> wrapper = new LinkedHashMap<>();
		wrapper.put("sleep", data);
		return wrapper;
	}

	@Override
	public void SetData(Map<String, Object> data) {
		if (data.containsKey("target_count")) {
			targetCount = ((Number) data.get("target_count")).doubleValue();
		}
		if (data.containsKey("count")) {
			count = ((Number) data.get("count")).doubleValue();
		}
	}

	@Override
	public String toString() {
		String str = "sleep:\n"
				+ "    count: " + this.count
				+ "\n    target_count: " + this.GetTargetCount();
		return str;
	}
}
