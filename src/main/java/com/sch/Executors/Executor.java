package com.sch.Executors;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Executor {
	private Player player;
	private String nick;
	
	public Executor(String nick) {
		this.nick = nick;
	}

	public void SetPlayer(Player player){this.player=player;}
	public final Player GetPlayer(){return player;}

	public final String GetNick(){return nick;}

	public String toString(){
		return nick;
	}
}
