package com.sch.Conditions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.sch.Events.EventTypes;

public class Walk extends Condition {
	private Material block;
	private static final Random RANDOM = new Random();

	public Walk(double targetCount, Material block) {
		super("walk", EventTypes.PlayerMoveEvent, targetCount);
		this.block = block;
	}

	public static Condition create(Map<?, ?> values) {
		Material block = null;
		if (values.get("block") instanceof String bn) {
			block = Material.getMaterial(bn.toUpperCase());
		}
		double targetCount = parseRange(values.get("range"));
		return new Walk(targetCount, block);
	}

	private static double parseRange(Object rangeObj) {
		if (rangeObj instanceof List<?> range) {
			int min = ((Number) range.get(0)).intValue();
			int max = ((Number) range.get(1)).intValue();
			return RANDOM.nextDouble() * (max - min + 1) + min;
		}
		return 1;
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
		targetCount = (double)data.get("target_count");
		count = (double)data.get("count");
	}
}