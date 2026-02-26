package com.sch.Commands;

import java.util.ArrayList;
import java.util.Arrays;

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
		// /QuestSys add clan.daily.easy {SCOREBOARD} {PLAYER_0} {PLAYER_1} {PLAYER_n}
		// /QuestSys {QWEST_UUID} remove {PLAYER} {PLAYER_n}
		// /QuestSys {QWEST_UUID} append {PLAYER} {PLAYER_n}
		// /QuestSys get {PLAYER}

		if (args.length < 2) return ErrorCmd(sender);

		if(args[0].equalsIgnoreCase("add")){
			return Add(sender, args[1], args[2], Arrays.copyOfRange(args, 3, args.length));
		}else if(args[0].equalsIgnoreCase("get")){
			return Get(sender, args[1]);
		}else if(args[1].equalsIgnoreCase("remove")){
			return Remove(sender, args[0], Arrays.copyOfRange(args, 2, args.length));
		}else if(args[1].equalsIgnoreCase("append")){
			return Append(sender, args[0], Arrays.copyOfRange(args, 2, args.length));
		}

		return ErrorCmd(sender);
	}

	private boolean Remove(CommandSender sender, String uuid, String[] players){
		return false;
	}

	private boolean Append(CommandSender sender, String uuid, String[] players){
		return false;
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
			quest.SetScoreboard(scoreboard);
			
			ArrayList<Executor> executors = new ArrayList<>();
			for (String player : players)
				executors.add(executorManager.Get(player));
	
			quest.AddExecutors(executors);
			quest.SetStartTimeNOW();
			quest.SetNewUUID();
			questManager.Put(quest);

			dbController.SaveQuests();

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
			"/QuestSys add clan.daily.easy {SCOREBOARD} {PLAYER_0} {PLAYER_1} {PLAYER_n}\n" +
			"/QuestSys {QWEST_UUID} remove {PLAYER} {PLAYER_n}\n" +
			"/QuestSys {QWEST_UUID} append {PLAYER} {PLAYER_n}\n" +
			"/QuestSys get {PLAYER}"
		);
		return false;
	}
}
