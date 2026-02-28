package com.sch.Quest;

import org.bukkit.configuration.ConfigurationSection;

import com.sch.QuestSys;
import com.sch.Conditions.Condition;
import com.sch.Conditions.ConditionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class QuestFactory {
	private static QuestFactory instance;

	public static QuestFactory PickMe() {
		if (QuestFactory.instance == null)
			QuestFactory.instance = new QuestFactory();
		return QuestFactory.instance;
	}

	private ConfigurationSection root;
	private final Random random = new Random();
	ConditionFactory conditionFactory = ConditionFactory.PickMe();

	public QuestFactory() {
		reload();
	}

	public void reload() {
		QuestSys.PickMe().reloadConfig();
		this.root = QuestSys.PickMe().getConfig().getConfigurationSection("all");
	}

	public List<String> getAllPaths() {
		List<String> paths = new ArrayList<>();
		collectPaths(root, "", paths);
		return paths;
	}

	private void collectPaths(ConfigurationSection section, String prefix, List<String> paths) {
		for (String key : section.getKeys(false)) {
			Object value = section.get(key);

			// Игнорируем секции с ключом "conditions"
			if (value instanceof ConfigurationSection childSection) {
				String newPrefix = prefix.isEmpty() ? key : prefix + "." + key;

				collectPaths(childSection, newPrefix, paths);
				paths.add(newPrefix);
				if (childSection.contains("conditions")) continue;

			}
		}
	}

	public Quest CreateQuest(String groupOrId) {
		ConfigurationSection section = root;
		String[] path = groupOrId.split("\\.");

		for (String key : path) {
			if (section == null)
				break;
			section = section.getConfigurationSection(key);
		}

		if (section == null)
			return null;

		if (section.contains("conditions")) {
			return buildQuest(section);
		}

		List<ConfigurationSection> allQuests = new ArrayList<>();
		collectQuestsRecursively(section, allQuests);

		if (allQuests.isEmpty())
			return null;

		ConfigurationSection chosen = allQuests.get(random.nextInt(allQuests.size()));
		return buildQuest(chosen);
	}

	private void collectQuestsRecursively(ConfigurationSection section, List<ConfigurationSection> quests) {
		Set<String> keys = section.getKeys(false);
		for (String key : keys) {
			ConfigurationSection child = section.getConfigurationSection(key);
			if (child == null)
				continue;

			if (child.contains("conditions")) {
				quests.add(child);
			} else {
				collectQuestsRecursively(child, quests);
			}
		}
	}

	
	public Quest CreateQuestByID(String id){
		List<ConfigurationSection> allQuests = new ArrayList<>();
		collectQuestsRecursively(root, allQuests);
		if (allQuests.isEmpty()) return null;

		for (ConfigurationSection questSection : allQuests)
			if(questSection.getName().equalsIgnoreCase(id))
				return buildQuest(questSection);

		return null;
	}

	public Quest CreateQuestByID(String id, List<Map<String, Object>> savedConditions){
		List<ConfigurationSection> allQuests = new ArrayList<>();
		collectQuestsRecursively(root, allQuests);
		if (allQuests.isEmpty()) return null;

		for (ConfigurationSection questSection : allQuests)
			if(questSection.getName().equalsIgnoreCase(id))
				return buildQuest(questSection, savedConditions);

		return null;
	}
	
	private Quest buildQuest(ConfigurationSection section) {
		
		// @TODO необязательные
		String name = section.getString("name");
		String description = section.getString("description");

		List<String> on_complete = section.getStringList("on_complete");
		List<String> on_fail = section.getStringList("on_fail");
		List<String> on_complete_once = section.getStringList("on_complete_once");
		List<String> on_fail_once = section.getStringList("on_fail_once");

		
		int exp = section.getInt("exp");
		long live_time = section.getLong("live_time");



		ArrayList<Condition> conditions = conditionFactory.CreateCondition(section);

		return new Quest(conditions,
						name, 
						section.getName(), 
						description, 
						exp, 
						live_time, 
						on_complete.toArray(new String[0]), 
						on_fail.toArray(new String[0]),
						on_complete_once.toArray(new String[0]),
						on_fail_once.toArray(new String[0])
					);
	}

	private Quest buildQuest(ConfigurationSection section, List<Map<String, Object>> savedConditions) {
		String name = section.getString("name");
		String description = section.getString("description");

		List<String> on_complete = section.getStringList("on_complete");
		List<String> on_fail = section.getStringList("on_fail");
		List<String> on_complete_once = section.getStringList("on_complete_once");
		List<String> on_fail_once = section.getStringList("on_fail_once");

		int exp = section.getInt("exp");
		long live_time = section.getLong("live_time");

		ArrayList<Condition> conditions = conditionFactory.CreateConditionFromData(savedConditions);

		return new Quest(conditions,
						name, 
						section.getName(), 
						description, 
						exp, 
						live_time, 
						on_complete.toArray(new String[0]), 
						on_fail.toArray(new String[0]),
						on_complete_once.toArray(new String[0]),
						on_fail_once.toArray(new String[0])
					);
	}
}
