package com.sch.Conditions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.sch.Events.EventTypes;

import org.bukkit.Material;

public class Walk extends Condition {
	private Material block;

	public Walk(
		// ArrayList<Executor> executors, 
		double targetCount, Material block) {
		// super(EventTypes.PlayerMoveEvent, executors, targetCount);
		super("walk", EventTypes.PlayerMoveEvent, targetCount);
		this.block = block;
	}

	@Override
	protected void onEvent(Event event) {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		double dist = e.getTo().clone().subtract(e.getFrom()).length();
		Material m = e.getTo().clone().subtract(0, 1, 0).getBlock().getType();
		if(block != null && m.asBlockType() != block.asBlockType()) return;
		count+=dist;
		checkCondition();
	}

	@Override
	public String toString(){
		return "Walk distance: "+this.count+ " target: " + this.GetTargetCount();
	}

	@Override
	public Map<String, Object> GetData() {
		Map<String, Object> data = new LinkedHashMap<>();
		
		if(block!=null) data.put("block", block.name());
		data.put("target_count", this.GetTargetCount());
		data.put("count", this.count);

		Map<String, Object> wrapper = new LinkedHashMap<>();
    	wrapper.put("walk", data);
		return wrapper;
	}

	@Override
	public void SetData(Map<String, Object> data) {
		if(data.containsKey("block")) block = Material.getMaterial((String)data.get("block"));
		targetCount = (double)data.get("target_count");
		count = (double)data.get("count");
	}
}