package com.QuestSys.Conditions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import com.QuestSys.Events.EventTypes;

public class Craft extends Condition {
	private Material item;

	public Craft(Material item, double targetCount) {
		super("craft", EventTypes.CraftItemEvent, targetCount);
		this.item = item;
	}

	public static Condition create(Map<?, ?> values) {
		Material material = null;
		if (values.get("item") instanceof String in) {
			material = Material.getMaterial(in);
		}
		double targetCount = (int)ParseTargetCount(values);
		return new Craft(material, targetCount);
	}

	@Override
	protected void onEvent(Event e) {
		CraftItemEvent event = (CraftItemEvent)e;
		if(item != null && event.getRecipe() != null && item != event.getRecipe().getResult().getType()) return;
		
		ItemStack result = event.getRecipe().getResult();
		int maxPerCraft = result.getAmount();

		// Считаем, сколько раз хватит ингредиентов
		int times = Integer.MAX_VALUE;
		for (ItemStack ingredient : event.getInventory().getMatrix()) {
			if (ingredient == null || ingredient.getType().isAir()) continue;
			times = Math.min(times, ingredient.getAmount());
		}

		if (times == Integer.MAX_VALUE) times = 1;

		count+=maxPerCraft * times;
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
		if(data.containsKey("target_count")) targetCount = ((Number)data.get("target_count")).doubleValue();
		if(data.containsKey("count")) count = ((Number)data.get("count")).doubleValue();
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
