package com.sch.Quest;

import org.bukkit.configuration.ConfigurationSection;

import com.sch.QuestSys;
import com.sch.Conditions.Condition;
import com.sch.Conditions.ConditionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuestFactory {
	private static QuestFactory instance;

	public static QuestFactory PickMe() {
		if (QuestFactory.instance == null)
			QuestFactory.instance = new QuestFactory();
		return QuestFactory.instance;
	}

	private final ConfigurationSection root;
	private final Random random = new Random();
	ConditionFactory conditionFactory = ConditionFactory.PickMe();

	public QuestFactory() {
		this.root = QuestSys.PickMe().getConfig().getConfigurationSection("all");
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
	
	private Quest buildQuest(ConfigurationSection section) {
		String name = section.getString("name");
		String description = section.getString("description");
		int exp = section.getInt("exp");
		long live_time = section.getLong("live_time");
		List<String> on_complite = section.getStringList("on_complite");
		List<String> on_fail = section.getStringList("on_fail");


		ArrayList<Condition> conditions = conditionFactory.CreateCondition(section);

		return new Quest(conditions,
						name, 
						section.getName(), 
						description, 
						exp, 
						live_time, 
						on_complite.toArray(new String[0]), 
						on_fail.toArray(new String[0])
					);
	}
}