package com.sch.Quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.sch.QuestSys;
import com.sch.DataBase.DBController;
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

	private final DBController dbController = DBController.PickMe();

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
		if(!quest.IsEnd() && !quest.IsFail()) liveQuests.add(quest);
		dbController.SaveQuests();
		return uuid;
	}

	public void Remove(UUID uuid){
		Quest quest = Get(uuid);
		quest.Delete();
		quests.remove(uuid);
		liveQuests.remove(quest);
		dbController.SaveQuests();
	}

	public Quest[] GetAllQuests(){return quests.values().toArray(new Quest[0]);}

	private QuestManager() {
		new BukkitRunnable() {
			@Override
			public void run() {
				HashSet<Quest> toRemove = new HashSet<>();
				for (Quest quest : liveQuests)
					if(!quest.CheckLiveTime()) toRemove.add(quest); //liveQuests.remove(quest);
				for (Quest quest : toRemove)
					liveQuests.remove(quest);
				
			}
		}.runTaskTimer(QuestSys.PickMe(), 0L, 20L);

		new BukkitRunnable() {
			@Override
			public void run() {
				final DBController dbController = DBController.PickMe();
				dbController.SaveQuests();
			}
		}.runTaskTimer(QuestSys.PickMe(), 1L, 20L*60);
	}
}
