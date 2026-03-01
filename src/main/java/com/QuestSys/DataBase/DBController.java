package com.QuestSys.DataBase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.QuestSys.QuestSys;
import com.QuestSys.Conditions.Condition;
import com.QuestSys.Executors.Executor;
import com.QuestSys.Executors.ExecutorManager;
import com.QuestSys.Quest.Quest;
import com.QuestSys.Quest.QuestFactory;
import com.QuestSys.Quest.QuestManager;

public class DBController {
	private FileConfiguration db;
	private QuestManager questManager;
	private QuestFactory questFactory;
	private ExecutorManager executorManager;
	private File dbFile;
	private File dataFolder;

	private static DBController instance;

	public static DBController PickMe() {
		if (DBController.instance == null)
			DBController.instance = new DBController();
		return DBController.instance;
	}

	private DBController() {}

	public void init(File dataFolder){
		questManager = QuestManager.PickMe();
		questFactory = QuestFactory.PickMe();
		executorManager = ExecutorManager.PickMe();

		this.dataFolder = dataFolder;
		dbFile = new File(dataFolder, "quests.yml");
		if (!dbFile.exists())
			try {dbFile.createNewFile();} catch (IOException e) {
				e.printStackTrace();
			}
		db = YamlConfiguration.loadConfiguration(dbFile);
	}

	public void SaveQuests() {
		dbFile.delete();
		init(dataFolder);

		Quest[] quests = questManager.GetAllQuests();
		for (Quest quest : quests) {
			String path = quest.GetUUID().toString() + ".";
			db.set(path + "id", quest.GetId());
			db.set(path + "start_time", quest.GetStartTime());
			db.set(path + "is_end", quest.IsEnd());
			ArrayList<String> executorNicks = new ArrayList<>();
			for (Executor executor : quest.GetExecutors())
				executorNicks.add(executor.GetNick());
			db.set(path + "executors", executorNicks);
			List<Map<String, Object>> conditions = new ArrayList<>();
			for (Condition condition : quest.GetConditions())
				conditions.add(condition.GetData());
			db.set(path + "conditions", conditions);
		}

		try {db.save(dbFile);} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void LoadQuests() {
		Set<String> keys = db.getKeys(false);
		for (String key : keys) {
			String id = db.getString(key + ".id");
			List<Map<String, Object>> savedConditions = (List<Map<String, Object>>) db.get(key + ".conditions");
			
			Quest quest = questFactory.CreateQuestByID(id, savedConditions);
			if (quest == null) continue;

			quest.SetStartTime(db.getLong(key + ".start_time"));
			UUID uuid = UUID.fromString(key);
			quest.SetUUID(uuid);
			quest.SetIsEnd(db.getBoolean(key + ".is_end"));

			ArrayList<Executor> executors = new ArrayList<>();
			List<String> executorNicks = db.getStringList(key + ".executors");
			for (String nick : executorNicks) {
				executorManager.Put(nick);
				executors.add(executorManager.Get(nick));
			}

			quest.AddExecutors(executors);

			questManager.Put(quest, uuid);

		}
	}
}
