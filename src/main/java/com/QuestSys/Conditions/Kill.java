package com.QuestSys.Conditions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.QuestSys.Events.EventTypes;

public class Kill extends Condition {
	private EntityType entityType;
	private static final Random RANDOM = new Random();

	public Kill(double targetCount, EntityType entityType) {
		super("kill", EventTypes.EntityDamageByEntityEvent, targetCount);
		this.entityType = entityType;
	}

	public static Condition create(Map<?, ?> values) {
		EntityType entityType = null;
		if (values.get("entity") instanceof String en) {
			entityType = EntityType.valueOf(en.toLowerCase());
		}
		double targetCount = parseTargetCount(values);
		return new Kill(targetCount, entityType);
	}

	private static double parseTargetCount(Object valuesObj) {
		if (valuesObj instanceof Map<?, ?> values) {
			if (values.containsKey("target_count")) {
				return ((Number) values.get("target_count")).doubleValue();
			}
			if (values.get("range") instanceof List<?> range) {
				int min = ((Number) range.get(0)).intValue();
				int max = ((Number) range.get(1)).intValue();
				return RANDOM.nextDouble() * (max - min + 1) + min;
			}
		}
		return 1;
	}

	@Override
	protected void onEvent(Event e) {
		Entity ent = null;
		Player player = null;
		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)e;
		if (event.getDamager() instanceof Player || event.getEntity() instanceof Player) {
			if(event.getDamager() instanceof Player) {
				player = (Player)event.getDamager();
				ent = event.getEntity();
			}
			else if(event.getEntity() instanceof Player) {
				player = (Player)event.getEntity();
				ent = event.getDamager();
			}
		}
		if(player==null || ent == null) return;
		if(ent instanceof LivingEntity lent) if(lent.getHealth()-event.getDamage() > 0) return;
		if(entityType!=null && !ent.getType().equals(entityType)) return;
		count++;
		checkCondition();
	}

	@Override
	public Map<String, Object> GetData() {
		Map<String, Object> data = new LinkedHashMap<>();
		
		if(entityType!=null) data.put("entity", entityType.getName());
		data.put("target_count", this.GetTargetCount());
		data.put("count", this.count);

		Map<String, Object> wrapper = new LinkedHashMap<>();
    	wrapper.put("kill", data);
		return wrapper;
	}

	@Override
	public void SetData(Map<String, Object> data) {
		if(data.containsKey("entity")) entityType = EntityType.fromName((String)data.get("entity"));
		if(data.containsKey("target_count")) targetCount = ((Number)data.get("target_count")).doubleValue();
		if(data.containsKey("count")) count = ((Number)data.get("count")).doubleValue();
	}
	
	@Override
	public String toString(){
		String str = "kill:\n"+ 
				"    count: "+this.count+ 
				"\n    target_count: " + this.GetTargetCount();

		if(entityType!=null)
			str+="\n    entity: "+entityType.name();
		return str;
	}
}
