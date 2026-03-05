package com.QuestSys.Conditions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.QuestSys.Events.EventTypes;

public class Walk extends Condition {
	private Material block;

	public Walk(double targetCount, Material block) {
		super("walk", EventTypes.PlayerMoveEvent, targetCount);
		this.block = block;
	}

	public static Condition create(Map<?, ?> values) {
		Material block = null;
		if (values.get("block") instanceof String bn) {
			block = Material.getMaterial(bn.toUpperCase());
		}
		double targetCount = Utils.ParseTargetCount(values);
		return new Walk(targetCount, block);
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
		String str = "walk:\n"+ 
				"    count: "+this.count+ 
				"\n    target_count: " + this.GetTargetCount();

		if(block!=null)
			str+="\n    block: "+block.name();
		return str;
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
		if(data.containsKey("target_count")) targetCount = ((Number)data.get("target_count")).doubleValue();
		if(data.containsKey("count")) count = ((Number)data.get("count")).doubleValue();
	}
}