package com.sch.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.sch.DataBase.DBController;
import com.sch.Executors.Executor;
import com.sch.Executors.ExecutorManager;
import com.sch.Quest.Quest;
import com.sch.Quest.QuestFactory;
import com.sch.Quest.QuestManager;

public class QuestSysCmdRouter implements CommandExecutor {
	private final QuestFactory questFactory = QuestFactory.PickMe();
	private final ExecutorManager executorManager = ExecutorManager.PickMe();
	private final QuestManager questManager = QuestManager.PickMe();
	private final DBController dbController = DBController.PickMe();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
		if (args.length < 1) return ErrorCmd(sender);

		if(args[0].equalsIgnoreCase("add")){
			if(args.length < 3) return ErrorCmd(sender);
			return Add(sender, args[1], args[2], Arrays.copyOfRange(args, 3, args.length));
		}else if(args[0].equalsIgnoreCase("get")){
			if(args.length < 2) return ErrorCmd(sender);
			return Get(sender, args[1]);
		}else if(args.length >= 2){
			try {
				UUID uuid = UUID.fromString(args[0]);
				if(args[1].equalsIgnoreCase("remove")){
					return Remove(sender, uuid, Arrays.copyOfRange(args, 2, args.length));
				}else if(args[1].equalsIgnoreCase("append")){
					return Append(sender, uuid, Arrays.copyOfRange(args, 2, args.length));
				}else if(args[1].equalsIgnoreCase("delete")){
					return Delete(sender, uuid);
				}
			} catch (IllegalArgumentException e) {
				sender.sendMessage("Invalid UUID: " + e.getMessage());
				return false;
			}
		}

		return ErrorCmd(sender);
	}

	private boolean Remove(CommandSender sender, UUID uuid, String[] players){
		if(players.length==0) return ErrorCmd(sender);

		Quest quest = questManager.Get(uuid);
		if(quest == null){
			sender.sendMessage("Quest not found: " + uuid);
			return false;
		}
		ArrayList<Executor> executors = new ArrayList<>();
		for (String player : players) executors.add(executorManager.Put(player));
		quest.RemoveExecutors(executors);

		dbController.SaveQuests();
		return true;
	}

	private boolean Append(CommandSender sender, UUID uuid, String[] players){
		if(players.length==0) return ErrorCmd(sender);
		
		Quest quest = questManager.Get(uuid);
		if(quest == null){
			sender.sendMessage("Quest not found: " + uuid);
			return false;
		}
		ArrayList<Executor> executors = new ArrayList<>();
		for (String player : players) executors.add(executorManager.Put(player));
		quest.AddExecutors(executors);

		dbController.SaveQuests();
		return true;
	}

	private boolean Delete(CommandSender sender, UUID uuid){
		Quest quest = questManager.Get(uuid);
		if(quest == null){
			sender.sendMessage("Quest not found: " + uuid);
			return false;
		}
		questManager.Remove(uuid);
		return true;
	}

	private boolean Get(CommandSender sender, String player){
		try {
			Executor executor = executorManager.Get(player);
			ArrayList<Quest> quests = questManager.Get(executor);
			for (Quest quest : quests) {
				sender.sendMessage(quest.toString());
			}
			return true;
		} catch (Exception e) {
			sender.sendMessage(e.toString());
			e.printStackTrace();
			return false;
		}
	}

	private boolean Add(CommandSender sender, String group, String scoreboard, String[] players){
		try {
			Quest quest = questFactory.CreateQuest(group);
			if(quest == null){
				sender.sendMessage("Quest not found: " + group);
				return false;
			}
			quest.SetScoreboard(scoreboard);
			
			ArrayList<Executor> executors = new ArrayList<>();
			for (String player : players)
				executors.add(executorManager.Get(player));
	
			quest.AddExecutors(executors);
			quest.SetStartTimeNOW();
			quest.SetNewUUID();
			questManager.Put(quest);

			sender.sendMessage(quest.GetUUID().toString());
		} catch (Exception e) {
			sender.sendMessage(e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean ErrorCmd(CommandSender sender){
		sender.sendMessage(
			"/questsys add clan.daily.easy {SCOREBOARD} {PLAYER_0} {PLAYER_1} {PLAYER_n}\n" +
			"/questsys {QWEST_UUID} remove {PLAYER} {PLAYER_n}\n" +
			"/questsys {QWEST_UUID} append {PLAYER} {PLAYER_n}\n" +
			"/questsys {QWEST_UUID} delete\n" +
			"/questsys get {PLAYER}"
		);
		return false;
	}
}
