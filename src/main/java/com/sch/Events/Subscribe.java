package com.sch.Events;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.bukkit.event.Event;

import com.sch.Executors.Executor;

public class Subscribe {
	private ArrayList<Executor> executors = new ArrayList<Executor>(); 
	Consumer<Event> handler;
	EventTypes eventType;

	private EventListener eventListener = EventListener.PickMe();

	public Subscribe(EventTypes eventType, ArrayList<Executor> executors, Consumer<Event> handler){
		this.eventType = eventType;
		this.handler = handler;
		AddExecutors(executors);
	}

	public void AddExecutors(ArrayList<Executor> executors){
		for (Executor e : executors)
			eventListener.AddHandler(eventType, e, handler);

		this.executors.addAll(executors);
	}

	public void RemoveExecutors(ArrayList<Executor> executors){
		for (Executor e : executors)
			eventListener.RemoveHandler(eventType, e, handler);

		this.executors.removeAll(executors);
	}

	public void UnSubscribe(){
		RemoveExecutors(executors);
	}
}
