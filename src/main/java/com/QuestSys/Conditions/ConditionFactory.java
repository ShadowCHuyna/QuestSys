package com.QuestSys.Conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.configuration.ConfigurationSection;

public class ConditionFactory {
	private static ConditionFactory instance;

	public static ConditionFactory PickMe() {
		if (ConditionFactory.instance == null)
			ConditionFactory.instance = new ConditionFactory();
		return ConditionFactory.instance;
	}

	private final Map<String, Function<Map<?, ?>, Condition>> creators = Map.ofEntries(
		Map.entry("walk", Walk::create),
		Map.entry("jump", Jump::create),
		Map.entry("kill", Kill::create),
		Map.entry("block", Block::create),
		Map.entry("damage", Damage::create),
		Map.entry("craft", Craft::create)
	);

	public ArrayList<Condition> CreateCondition(ConfigurationSection section) {
		ArrayList<Condition> out = new ArrayList<>();

		List<Map<?, ?>> conditions = section.getMapList("conditions");

		for (Map<?, ?> cond : conditions) {
			for (Map.Entry<?, ?> entry : cond.entrySet()) {
				String type = entry.getKey().toString();
				Map<?, ?> values = (Map<?, ?>) entry.getValue();

				Function<Map<?, ?>, Condition> creator = creators.get(type.toLowerCase());
				if (creator != null) {
					out.add(creator.apply(values));
				}
			}
		}

		return out;
	}

	public ArrayList<Condition> CreateConditionFromData(List<Map<String, Object>> savedConditions) {
		ArrayList<Condition> out = new ArrayList<>();
		
		for (Map<String, Object> conditionData : savedConditions) {
			String type = conditionData.keySet().toArray(new String[0])[0];
			Map<?, ?> values = (Map<?, ?>) conditionData.get(type);
			
			Function<Map<?, ?>, Condition> creator = creators.get(type.toLowerCase());
			if (creator != null) {
				Condition condition = creator.apply(values);
				condition.SetData((Map<String, Object>) values);
				out.add(condition);
			}
		}
		
		return out;
	}
}
