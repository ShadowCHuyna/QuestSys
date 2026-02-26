package com.sch.Conditions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.sch.Events.EventTypes;

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
	
	public Block(ActionTypes actionType, Material block, double targetCount, EventTypes type) {
		super("block", type, targetCount);
		this.actionType = actionType;
		this.block = block;
	}

	@Override
	protected void onEvent(Event event) {
		if(event instanceof BlockPlaceEvent e) place(e);
		else if (event instanceof BlockDestroyEvent e) destroy(e);
		else if (event instanceof PlayerInteractEvent e) click(e);
	}

	private void click(PlayerInteractEvent event){
		if(actionType == ActionTypes.destroy || actionType == ActionTypes.placement ) return;
		double start_c = count;
		if(block == null){
			if (actionType == ActionTypes.click) {
				count++;
			}else if(actionType == ActionTypes.l_click && 
				(event.getAction() == Action.LEFT_CLICK_AIR 
				|| event.getAction() == Action.LEFT_CLICK_BLOCK)){
					count++;
			}else if(actionType == ActionTypes.r_click && 
				(event.getAction() == Action.RIGHT_CLICK_AIR 
				|| event.getAction() == Action.RIGHT_CLICK_BLOCK)){
					count++;
			}
		}else if(event.getMaterial() == block){
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

	private void destroy(BlockDestroyEvent event){
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
		actionType = ActionTypes.valueOf((String)data.get("action"));
		targetCount = (double)data.get("target_count");
		count = (double)data.get("count");
	}
	
}
