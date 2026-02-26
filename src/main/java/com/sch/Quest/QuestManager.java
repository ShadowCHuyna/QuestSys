package com.sch.Quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.sch.QuestSys;
import com.sch.Executors.Executor;

public class QuestManager {
	private static QuestManager instance;

	public static QuestManager PickMe() {
		if (QuestManager.instance == null)
			QuestManager.instance = new QuestManager();
		return QuestManager.instance;
	}

	private HashMap<UUID, Quest> quests = new HashMap<UUID, Quest>();
	private HashSet<Quest> liveQuests = new HashSet<Quest>();


	public Quest Get(UUID uuid) {
		return quests.get(uuid);
	}

	public ArrayList<Quest> Get(Executor executor){
		ArrayList<Quest> quests_arr = new ArrayList<>();
		for (Quest quest : quests.values())
			if(quest.HasExecutor(executor)) quests_arr.add(quest);

		return quests_arr;
	}

	public UUID Put(Quest quest) {
		return Put(quest, UUID.randomUUID());
	}

	public UUID Put(Quest quest, UUID uuid) {
		quests.put(uuid, quest);
		liveQuests.add(quest);
		return uuid;
	}

	public void Remove(UUID uuid){
		quests.remove(uuid);
	}

	public Quest[] GetAllQuests(){return quests.values().toArray(new Quest[0]);}

	private QuestManager() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Quest quest : liveQuests)
					if(!quest.CheckLiveTime()) liveQuests.remove(quest);
			}
		}.runTaskTimer(QuestSys.PickMe(), 0L, 20L);
	}
}
