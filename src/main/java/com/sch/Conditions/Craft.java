package com.sch.Conditions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;

import com.sch.Events.EventTypes;

public class Craft extends Condition {
	private Material item;

	public Craft(Material item, double targetCount) {
		super("craft", EventTypes.CraftItemEvent, targetCount);
		this.item = item;
	}

	@Override
	protected void onEvent(Event e) {
		CraftItemEvent event = (CraftItemEvent)e;
		if(item != null && event.getRecipe() != null && item != event.getRecipe().getResult().getType()) return;
		count++;
		checkCondition();
	}

	@Override
	public Map<String, Object> GetData() {
		Map<String, Object> data = new LinkedHashMap<>();
		
		if(item!=null) data.put("item", item.name());
		data.put("target_count", this.GetTargetCount());
		data.put("count", this.count);

		Map<String, Object> wrapper = new LinkedHashMap<>();
    	wrapper.put("craft", data);
		return wrapper;
	}

	@Override
	public void SetData(Map<String, Object> data) {
		if(data.containsKey("item")) item = Material.getMaterial((String)data.get("item"));
		targetCount = (double)data.get("target_count");
		count = (double)data.get("count");
	}

	@Override
	public String toString(){
		String str = "craft:\n"+ 
				"    count: "+this.count+ 
				"\n    target_count: " + this.GetTargetCount();

		if(item!=null)
			str+="\n    item: "+item.name();
		
		return str;
	}
	
}
