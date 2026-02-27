package com.sch.Conditions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.sch.Events.EventTypes;

public class Kill extends Condition {
	private EntityType entityType;

	public Kill(double targetCount, EntityType entityType) {
		super("kill", EventTypes.EntityDamageByEntityEvent, targetCount);
		this.entityType = entityType;
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
		targetCount = (double)data.get("target_count");
		count = (double)data.get("count");
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
