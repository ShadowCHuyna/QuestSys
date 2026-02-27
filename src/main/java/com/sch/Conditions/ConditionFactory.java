package com.sch.Conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import com.sch.Events.EventTypes;

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

				// @TODO хуйня
				if(type.equalsIgnoreCase("walk")){
					Material material = null;
					if (values.get("block") instanceof String bn) material = Material.getMaterial(bn.toUpperCase());

					Walk c = new Walk(targetCount, material);
					out.add(c);
				}else if(type.equalsIgnoreCase("jump")){
					Material material = null;
					if (values.get("block") instanceof String bn) material = Material.getMaterial(bn.toUpperCase());
					Jump c = new Jump(targetCount, material);
					out.add(c);
				}else if(type.equalsIgnoreCase("kill")){
					EntityType entityType = null;
					if (values.get("entity") instanceof String en) entityType = EntityType.valueOf(en.toLowerCase());
					Kill c = new Kill(targetCount, entityType);
					out.add(c);
				}else if(type.equalsIgnoreCase("block")){
					Material material = null;
					ActionTypes actionType = ActionTypes.valueOf(((String)values.get("action")).toLowerCase());
					if (values.get("block") instanceof String bn) material = Material.getMaterial(bn.toUpperCase());
					EventTypes et = null;
					if(actionType == ActionTypes.destroy) et = EventTypes.BlockBreakEvent;
					else if(actionType == ActionTypes.placement) et = EventTypes.BlockPlaceEvent;
					else et = EventTypes.PlayerInteractEvent;
					
					Block c = new Block(actionType, material, targetCount, et);
					out.add(c);
				}else if(type.equalsIgnoreCase("damage")){
					EntityType entityType = null;
					if (values.get("entity") instanceof String en) entityType = EntityType.valueOf(en);
					DamageDirections dd = DamageDirections.valueOf((String)values.get("direction"));
					Damage c = new Damage(entityType, dd, targetCount);
					out.add(c);
				}else if(type.equalsIgnoreCase("craft")){
					Material material = null;
					if (values.get("item") instanceof String in) material = Material.getMaterial(in);
					Craft c = new Craft(material, targetCount);
					out.add(c);
				}
			}
		}

		return out;
	}
}
