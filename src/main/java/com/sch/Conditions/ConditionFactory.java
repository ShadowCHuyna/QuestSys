package com.sch.Conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class ConditionFactory {
	private static ConditionFactory instance;
	public static ConditionFactory PickMe() {
		if (ConditionFactory.instance == null)
			ConditionFactory.instance = new ConditionFactory();
		return ConditionFactory.instance;
	}

	private final Random random = new Random();

	public ArrayList<Condition> CreateCondition(ConfigurationSection section){
		ArrayList<Condition> out = new ArrayList<>();

		List<Map<?, ?>> conditions = section.getMapList("conditions");

		for (Map<?, ?> cond : conditions) {
			for (Map.Entry<?, ?> entry : cond.entrySet()) {
				String type = entry.getKey().toString();
				Map<?, ?> values = (Map<?, ?>) entry.getValue();
				List<Integer> range = (List<Integer>) values.get("range");
				double targetCount = random.nextDouble(range.get(1) - range.get(0) + 1) + range.get(0);

				if(type.equalsIgnoreCase("walk")){
					String blockName = (String)values.get("block");
					Material material = null;
					if (blockName!=null)
						material = Material.getMaterial(blockName);

					Walk walk = new Walk(targetCount, material);
					out.add(walk);
				}
			}
		}

		return out;
	}
}
