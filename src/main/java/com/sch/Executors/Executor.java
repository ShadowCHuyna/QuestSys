package com.sch.Executors;

import org.bukkit.entity.Player;

public class Executor {
	final private Player player;

	public Executor(Player player) {
		this.player = player;
	}

	public final Player GetPlayer(){
		return player;
	}
}
