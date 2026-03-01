package com.QuestSys.Conditions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.QuestSys.Events.EventTypes;

enum ActionTypes {
	placement,
	destroy,
	click,
	l_click,
	r_click
}

public class Block extends Condition {
	private ActionTypes actionType;
	private Material block;
	private static final Random RANDOM = new Random();

	public Block(ActionTypes actionType, Material block, double targetCount, EventTypes type) {
		super("block", type, targetCount);
		this.actionType = actionType;
		this.block = block;
	}

	public static Condition create(Map<?, ?> values) {
		Material material = null;
		if (values.get("block") instanceof String bn) {
			material = Material.getMaterial(bn.toUpperCase());
		}
		ActionTypes actionType = ActionTypes.valueOf(((String) values.get("action")).toLowerCase());
		
		EventTypes eventType;
		if (actionType == ActionTypes.destroy) eventType = EventTypes.BlockBreakEvent;
		else if (actionType == ActionTypes.placement) eventType = EventTypes.BlockPlaceEvent;
		else eventType = EventTypes.PlayerInteractEvent;
		
		double targetCount = parseTargetCount(values);
		return new Block(actionType, material, targetCount, eventType);
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
	protected void onEvent(Event event) {
		if(event instanceof BlockPlaceEvent e) place(e);
		else if (event instanceof BlockBreakEvent e) destroy(e);
		else if (event instanceof PlayerInteractEvent e) click(e);
	}

	private void click(PlayerInteractEvent event){
		if(actionType == ActionTypes.destroy || actionType == ActionTypes.placement ) return;
		double start_c = count;
		if(block == null){
			if (actionType == ActionTypes.click) {
				count++;
			}else if(actionType == ActionTypes.l_click && event.getAction() == Action.LEFT_CLICK_BLOCK){
					count++;
			}else if(actionType == ActionTypes.r_click && event.getAction() == Action.RIGHT_CLICK_BLOCK){
					count++;
			}
		}else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == block){
			if (actionType == ActionTypes.click) {
				count++;
			}else if(actionType == ActionTypes.l_click && event.getAction() == Action.LEFT_CLICK_BLOCK){
					count++;
			}else if(actionType == ActionTypes.r_click &&  event.getAction() == Action.RIGHT_CLICK_BLOCK){
					count++;
			}
		}
		if(start_c!=count)checkCondition();
	}

	private void destroy(BlockBreakEvent event){
		if(actionType != ActionTypes.destroy) return;
		if(block!= null && event.getBlock().getType()!=block) return;
		count++;
		checkCondition();
	}

	private void place(BlockPlaceEvent event){
		if(actionType != ActionTypes.placement) return;
		if(block!= null && event.getBlock().getType()!=block) return;
		count++;
		checkCondition();
	}

	@Override
	public Map<String, Object> GetData() {
		Map<String, Object> data = new LinkedHashMap<>();
		
		if(block!=null) data.put("block", block.name());
		data.put("action", actionType.name());
		data.put("target_count", this.GetTargetCount());
		data.put("count", this.count);

		Map<String, Object> wrapper = new LinkedHashMap<>();
    	wrapper.put("block", data);
		return wrapper;
	}

	@Override
	public void SetData(Map<String, Object> data) {
		if(data.containsKey("block")) block = Material.getMaterial((String)data.get("block"));
		if(data.containsKey("action")) actionType = ActionTypes.valueOf((String)data.get("action"));
		if(data.containsKey("target_count")) targetCount = ((Number)data.get("target_count")).doubleValue();
		if(data.containsKey("count")) count = ((Number)data.get("count")).doubleValue();
	}
	
	@Override
	public String toString(){
		String str = "block:\n"+ 
				"    count: "+this.count+ 
				"\n    target_count: " + this.GetTargetCount()+
				"\n    action: "+actionType.name();

		if(block!=null)
			str+="\n    block: "+block.name();	
		return str;
	}
}
