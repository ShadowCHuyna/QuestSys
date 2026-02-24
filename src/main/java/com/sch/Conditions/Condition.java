package com.sch.Conditions;

import java.util.ArrayList;

import org.bukkit.event.Event;

import com.sch.Events.EventTypes;
import com.sch.Events.Subscribe;
import com.sch.Executors.Executor;
import com.sch.Quest.Quest;

public abstract class Condition {
	protected Subscribe subscribe;
	protected Quest master;
	protected double count = 0; 

	private double targetCount;
	private boolean state = false;

	public Condition(EventTypes eventType, 
					// ArrayList<Executor> executors, 
					double targetCount){
		this.targetCount = targetCount;
		subscribe = new Subscribe(eventType, new ArrayList<Executor>(), (Event e)->{onEvent(e);});
	}

	protected abstract void onEvent(Event event);
	
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
}
