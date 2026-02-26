package com.sch.Conditions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
	protected void onEvent(Event event) {
		Entity ent = null;
		Player player = null;
		if (event instanceof EntityDamageByEntityEvent ede && (ede.getDamager() instanceof Player || ede.getEntity() instanceof Player)) {
			if(ede.getDamager() instanceof Player) {
				player = (Player)ede.getDamager();
				ent = ede.getEntity();
			}
			else if(ede.getEntity() instanceof Player) {
				player = (Player)ede.getEntity();
				ent = ede.getDamager();
			}
		}
		if(player==null || ent == null) return;
		if(!ent.isDead()) return;
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
	
}
