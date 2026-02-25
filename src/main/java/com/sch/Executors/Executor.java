package com.sch.Executors;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Executor {
	private Player player;
	private String nick;
	private UUID uuid;

	public UUID GetUUID(){return uuid;}

	public Executor(Player player) {
		this.player = player;
	}

	public Executor(String nick) {
		this.nick = nick;
		uuid = Bukkit.getOfflinePlayer(nick).getUniqueId();
	}

	public final Player GetPlayer(){
		return player;
	}
}
