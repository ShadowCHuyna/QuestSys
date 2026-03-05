package com.QuestSys.Conditions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.Event;

import com.QuestSys.Events.EventTypes;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;

public class Jump extends Condition {
	private Material block;

	public Jump(double targetCount, Material block) {
		super("jump", EventTypes.PlayerJumpEvent, targetCount);
		this.block = block;
	}

	public static Condition create(Map<?, ?> values) {
		Material block = null;
		if (values.get("block") instanceof String bn) {
			block = Material.getMaterial(bn.toUpperCase());
		}
		double targetCount = Utils.ParseTargetCount(values);
		return new Jump(targetCount, block);
	}

	@Override
	protected void onEvent(Event event) {
		PlayerJumpEvent e = (PlayerJumpEvent) event;
		Material m = e.getFrom().getBlock().getType();
		if(block != null && m.asBlockType() != block.asBlockType()) return;
		count++;
		checkCondition();
	}

	@Override
	public Map<String, Object> GetData() {
		Map<String, Object> data = new LinkedHashMap<>();
		
		if(block!=null) data.put("block", block.name());
		data.put("target_count", this.GetTargetCount());
		data.put("count", this.count);

		Map<String, Object> wrapper = new LinkedHashMap<>();
    	wrapper.put("jump", data);
		return wrapper;
	}

	@Override
	public void SetData(Map<String, Object> data) {
		if(data.containsKey("block")) block = Material.getMaterial((String)data.get("block"));
		if(data.containsKey("target_count")) targetCount = ((Number)data.get("target_count")).doubleValue();
		if(data.containsKey("count")) count = ((Number)data.get("count")).doubleValue();
	}
	
	@Override
	public String toString(){
		String str = "jump:\n"+ 
				"    count: "+this.count+ 
				"\n    target_count: " + this.GetTargetCount();

		if(block!=null)
			str+="\n    block: "+block.name();
		return str;
	}
}
