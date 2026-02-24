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

	private HashMap<UUID, Executor> executors = new HashMap<UUID, Executor>();

	public Executor Get(UUID uuid) {
		return executors.get(uuid);
	}

	public Executor Get(String nick) {
		Player player = Bukkit.getPlayer(nick);
		return executors.get(player.getUniqueId());
	}

	public Executor Put(UUID uuid, Player player) {
		Executor e = new Executor(player);
		executors.put(uuid, e);
		return e;
	}

	public void Remove(UUID uuid){
		executors.remove(uuid);
	}

	private ExecutorManager() {}
}
