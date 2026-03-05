package com.QuestSys.Conditions;

import java.util.Map;

import org.bukkit.damage.DamageType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.QuestSys.Events.EventTypes;

public class Death extends Condition {
	private DamageType damageType;

	public Death(double targetCount, DamageType damageType) {
		super("death", EventTypes.PlayerDeathEvent, targetCount);
		this.damageType = damageType;
	}

	@Override
	protected void onEvent(Event e) {
		PlayerDeathEvent event = (PlayerDeathEvent)e;
		DamageType damageType = event.getDamageSource().getDamageType();
		throw new UnsupportedOperationException("Unimplemented method 'onEvent'");
	}

	@Override
	public Map<String, Object> GetData() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'GetData'");
	}

	@Override
	public void SetData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'SetData'");
	}
	
}
