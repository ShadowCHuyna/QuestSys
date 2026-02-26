package com.sch.Conditions;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

import com.sch.Events.EventTypes;
import com.sch.Events.Subscribe;
import com.sch.Executors.Executor;
import com.sch.Quest.Quest;

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

	protected abstract void onEvent(Event event);
	
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

	public abstract Map<String, Object> GetData();
	public abstract void SetData(Map<String, Object> data);
}
