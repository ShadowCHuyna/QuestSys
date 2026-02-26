package com.sch.Executors;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ExecutorManager {
	private static ExecutorManager instance;

	public static ExecutorManager PickMe() {
		if (ExecutorManager.instance == null)
			ExecutorManager.instance = new ExecutorManager();
		return ExecutorManager.instance;
	}

	private HashMap<String, Executor> executors = new HashMap<String, Executor>();

	public Executor Get(String nick) {
		Executor executor = executors.get(nick);
		if (executor == null) executor = Put(nick);
		return executor;
	}

	public Executor Put(String nick) {
		Executor e = new Executor(nick);
		executors.put(nick, e);
		return e;
	}

	public void Remove(String nick){
		executors.remove(nick);
	}

	private ExecutorManager() {}
}
