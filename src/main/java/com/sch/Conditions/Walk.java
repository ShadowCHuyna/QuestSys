package com.sch.Conditions;

import java.util.ArrayList;

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
		super(EventTypes.PlayerMoveEvent, targetCount);
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
}