package com.QuestSys.Conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

import com.QuestSys.Events.EventTypes;
import com.QuestSys.Events.Subscribe;
import com.QuestSys.Executors.Executor;
import com.QuestSys.Quest.Quest;

public abstract class Condition {
	protected Subscribe subscribe;
	protected Quest master;
	protected double count = 0; 

	protected double targetCount;
	private boolean state = false;

	private String name;

	public Condition(String name, EventTypes eventType,
					// ArrayList<Executor> executors, 
					double targetCount){
		this.name = name;
		this.targetCount = targetCount;
		subscribe = new Subscribe(eventType, new ArrayList<Executor>(), (Event e)->{onEvent(e);});
	}

	protected abstract void onEvent(Event e);
	
	public String GetName(){ return name; }

	public void AddExecutors(ArrayList<Executor> executors){subscribe.AddExecutors(executors);}
	public void RemoveExecutors(ArrayList<Executor> executors){subscribe.RemoveExecutors(executors);}
	public boolean GetState(){return state;}

	public double GetTargetCount(){return targetCount;}
	public double GetCount(){return count;}

	public void SetQuest(Quest master){
		this.master = master;
	}

	protected boolean checkCondition(){
		if (count >= targetCount){
			state = true;
			subscribe.UnSubscribe();
			master.onChangeStateCondition();
		}
		return state;
	}

	protected static final Random RANDOM = new Random();

	protected static double ParseTargetCount(Object valuesObj) {
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

	public abstract Map<String, Object> GetData();
	public abstract void SetData(Map<String, Object> data);
}
