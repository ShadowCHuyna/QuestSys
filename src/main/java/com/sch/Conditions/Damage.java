package com.sch.Conditions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.sch.Events.EventTypes;

enum DamageDirections {
	p2e,
	e2p
}

public class Damage extends Condition {
	EntityType entity;
	DamageDirections direction;

	public Damage(EntityType entity, DamageDirections direction, double targetCount) {
		super("damage", EventTypes.EntityDamageByEntityEvent, targetCount);
		this.entity = entity;
		this.direction = direction;
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
		direction = DamageDirections.valueOf((String)data.get("direction"));
		targetCount = (double)data.get("target_count");
		count = (double)data.get("count");
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
