package com.QuestSys.Conditions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.QuestSys.Events.EventTypes;

enum DamageDirections {
	p2e,
	e2p
}

public class Damage extends Condition {
	EntityType entity;
	DamageDirections direction;
	private static final Random RANDOM = new Random();

	public Damage(EntityType entity, DamageDirections direction, double targetCount) {
		super("damage", EventTypes.EntityDamageByEntityEvent, targetCount);
		this.entity = entity;
		this.direction = direction;
	}

	public static Condition create(Map<?, ?> values) {
		EntityType entityType = null;
		if (values.get("entity") instanceof String en) {
			entityType = EntityType.valueOf(en);
		}
		DamageDirections direction = DamageDirections.valueOf((String) values.get("direction"));
		double targetCount = parseTargetCount(values);
		return new Damage(entityType, direction, targetCount);
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
		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)e;
		Entity ent = null;
		Player player = null;
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
		if(event.getDamager() instanceof Player && direction == DamageDirections.p2e){
			if(entity!=null && event.getEntity().getType()!=entity)return;
			count+=event.getDamage();
		}else if(event.getEntity() instanceof Player && direction == DamageDirections.e2p){
			if(entity!=null && event.getDamager().getType()!=entity)return;
			count+=event.getDamage();	
		}else return;
		
		checkCondition();
	}

	@Override
	public Map<String, Object> GetData() {
		Map<String, Object> data = new LinkedHashMap<>();
		
		if(entity!=null) data.put("entity", entity.getName());
		data.put("target_count", this.GetTargetCount());
		data.put("count", this.count);
		data.put("direction", this.direction.name());

		Map<String, Object> wrapper = new LinkedHashMap<>();
    	wrapper.put("damage", data);
		return wrapper;
	}

	@Override
	public void SetData(Map<String, Object> data) {
		if(data.containsKey("entity")) entity = EntityType.fromName((String)data.get("entity"));
		if(data.containsKey("direction")) direction = DamageDirections.valueOf((String)data.get("direction"));
		if(data.containsKey("target_count")) targetCount = ((Number)data.get("target_count")).doubleValue();
		if(data.containsKey("count")) count = ((Number)data.get("count")).doubleValue();
	}
	
	@Override
	public String toString(){
		String str = "damage:\n"+ 
				"    count: "+this.count+ 
				"\n    target_count: " + this.GetTargetCount()+
				"\n    direction: "+direction.name();

		if(entity!=null)
			str+="\n    entity: "+entity.name();
		
		return str;
	}
}
