package com.QuestSys.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

import com.QuestSys.Quest.Quest;
import com.QuestSys.Quest.QuestFactory;
import com.QuestSys.Quest.QuestManager;

public class QuestSysTabRouter implements TabCompleter{
	private final QuestManager questManager = QuestManager.PickMe();
	private final QuestFactory questFactory = QuestFactory.PickMe();

	private List<String> getOnlinePlayers() {
		return Bukkit.getOnlinePlayers()
				.stream()
				.map(Player::getName)
				.collect(Collectors.toList());
	}

	private List<String> getQuestUUIDs() {
		Quest[] quests = questManager.GetAllQuests();
		List<String> uuids = new ArrayList<>();
		for (Quest quest : quests) uuids.add(quest.GetUUID().toString());
		return uuids;
	}

	private List<String> getAvailableGroups() {
		return questFactory.getAllPaths();
	}

	private List<String> getScoreboards() {
		return Bukkit.getScoreboardManager()
        .getMainScoreboard()
        .getObjectives()
        .stream()
        .map(Objective::getName)
        .toList();
	}

	private List<String> filter(List<String> list, String current) {
		return list.stream()
				.filter(s -> s.toLowerCase().startsWith(current.toLowerCase()))
				.sorted()
				.collect(Collectors.toList());
	}

	@Override
	public @NotNull List<String> onTabComplete(@NotNull CommandSender sender,
			@NotNull Command command,
			@NotNull String alias,
			@NotNull String[] args) {

		List<String> result = new ArrayList<>();

		if (!command.getName().equalsIgnoreCase("questsys"))
			return result;

		// /questsys <...>
		if (args.length == 1) {
			result.add("add");
			result.add("get");
			result.add("reload");

			// UUID квестов
			result.addAll(getQuestUUIDs());

			return filter(result, args[0]);
		}

		// /questsys add ...
		if (args[0].equalsIgnoreCase("add")) {

			if (args.length == 2) {
				// /questsys add <group>
				result.addAll(getAvailableGroups()); // clan.daily.easy и т.п.
				// System.err.println(getAvailableGroups());
				return filter(result, args[1]);
			}

			if (args.length == 3) {
				// /questsys add <group> <scoreboard>
				result.addAll(getScoreboards());
				return filter(result, args[2]);
			}

			if (args.length >= 4) {
				// /questsys add ... <player...>
				return filter(getOnlinePlayers(), args[args.length - 1]);
			}
		}

		// /questsys get <player>
		if (args[0].equalsIgnoreCase("get")) {
			if (args.length == 2) {
				return filter(getOnlinePlayers(), args[1]);
			}
		}

		// /questsys <uuid> ...
		if (args.length >= 2) {

			String uuid = args[0];

			if (args.length == 2) {
				result.add("remove");
				result.add("append");
				result.add("delete");
				return filter(result, args[1]);
			}

			if (args[1].equalsIgnoreCase("remove") ||
					args[1].equalsIgnoreCase("append")) {

				if (args.length >= 3) {
					return filter(getOnlinePlayers(), args[args.length - 1]);
				}
			}
		}

		return result;
	}
}
