package com.QuestSys.Quest;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.QuestSys.QuestSys;
import com.QuestSys.Conditions.Condition;
import com.QuestSys.DataBase.DBController;
import com.QuestSys.Executors.Executor;

import net.kyori.adventure.text.Component;

public class Quest {
	private ArrayList<Condition> conditions = new ArrayList<Condition>();
	private ArrayList<Executor> executors = new ArrayList<Executor>();
	private UUID uuid;

	private String name;
	private String id;
	private String description;
	private int exp;
	private String scoreboard;
	private long liveTime = -1;

	private String[] onCompleteCmds;
	private String[] onFailCmds;
	private String[] onCompleteOnceCmds;
	private String[] onFailOnceCmds;

	private boolean isEnd = false;
	private boolean isFail = false;
	private long startTime = -1;

	private long while_in_progress_interval = -1;
	private String[] while_in_progress_cmds = new String[0];

	BukkitRunnable while_in_progress_runnable;
	// @TODO что то делать
	public void Delete(){
		isEnd = true;
		while_in_progress_runnable.cancel();
		RemoveExecutors(executors);
	}

	public Quest(
				// ArrayList<Executor> executors, 
				ArrayList<Condition> conditions,
				String name, 
				String id, 
				String description, 
				int exp, 
				// String scoreboard, 
				long liveTime,
				String[] onCompleteCmds,
				String[] onFailCmds,
				String[] onCompleteOnceCmds,
				String[] onFailOnceCmds,

				long while_in_progress_interval,
				String[] while_in_progress_cmds
			){
		// this.uuid = questManager.Put(this);
		
		this.name = name;
		this.id = id;
		this.description = description;
		this.exp = exp;
		// this.scoreboard = scoreboard;
		this.liveTime = liveTime;

		this.onCompleteCmds = onCompleteCmds;
		this.onFailCmds = onFailCmds;
		this.onCompleteOnceCmds = onCompleteOnceCmds;
		this.onFailOnceCmds = onFailOnceCmds;

		this.while_in_progress_interval = while_in_progress_interval;
		this.while_in_progress_cmds = while_in_progress_cmds;
		
		AddExecutors(new ArrayList<Executor>());
		AddCondition(conditions);

		// startTime = java.time.Instant.now().getEpochSecond();
		if(while_in_progress_interval>0)
			while_in_progress_runnable = new BukkitRunnable() {
				@Override
				public void run() {
					if(isEnd) this.cancel();
					for (Executor executor : executors){
						Player player = executor.GetPlayer(); 
						if(player == null) continue;
						for (String cmd : onFailCmds) 
							try {Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", player.getName()).replace("{PLAYER}", player.getName()));	
							} catch (Exception e) {
								e.printStackTrace();
							}
					}
				}
			};
			while_in_progress_runnable.runTaskTimer(QuestSys.PickMe(), 1L, while_in_progress_interval);
	}

	public void SetIsEnd(boolean state){isEnd=state;}

	public UUID GetUUID(){return uuid;}
	public void SetUUID(UUID uuid){this.uuid=uuid;}
	public void SetNewUUID(){this.uuid=UUID.randomUUID();}

	public String GetName(){return name;}
	public String GetId(){return id;}
	public String GetDescription(){return description;}
	public double GetExp(){return exp;}
	public String GetScoreboard(){return scoreboard;}
	
	public long GetLiveTime(){return liveTime;}
	public long GetStartTime(){return startTime;}
	public void SetStartTime(long startTime){this.startTime=startTime;}
	public void SetStartTimeNOW(){this.startTime=java.time.Instant.now().getEpochSecond();}

	public boolean IsFail(){return isFail;}
	public boolean IsEnd(){return isEnd;}

	public Executor[] GetExecutors(){return executors.toArray(new Executor[0]);}
	public Condition[] GetConditions(){return conditions.toArray(new Condition[0]);}

	public void SetScoreboard(String scoreboard){this.scoreboard = scoreboard;}

	public void AddCondition(ArrayList<Condition> conditions){
		for (Condition condition : conditions)
			condition.SetQuest(this);
		this.conditions.addAll(conditions);
	}

	public void AddExecutors(ArrayList<Executor> executors){
		for (Condition condition : conditions)
			condition.AddExecutors(executors);
		this.executors.addAll(executors);
	}
	
	public void RemoveExecutors(ArrayList<Executor> executors){
		for (Condition condition : conditions)
			condition.RemoveExecutors(executors);
		this.executors.removeAll(executors);
	}

	public void onChangeStateCondition(){
		if(isFail || isEnd) return;
		boolean flag = true;
		
		for (Condition condition : conditions) {
			if(!condition.GetState()){
				flag = false;
				break;
			}
		}

		if(flag) onComplete();
	}

	public void onFail(){
		if(isFail || isEnd) return;
		isFail = true;
		isEnd = true;
		for (Executor executor : executors){
			Player player = executor.GetPlayer(); 
			if(player == null) continue;
			for (String cmd : onFailCmds) 
				try {Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", player.getName()).replace("{PLAYER}", player.getName()));	
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		for (String cmd : onFailOnceCmds) 
			try {Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{UUID}", uuid.toString()).replace("{uuid}", uuid.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	private void addExp(){
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();

		Objective objective = board.getObjective("questsys");
		if(objective==null)
			objective = board.registerNewObjective("questsys", Criteria.DUMMY, Component.text("questsys") );

		Score score = objective.getScore(scoreboard);
		int current = score.getScore() | 0;
		score.setScore(current+exp);
	}


	public void onComplete(){
		isEnd = true;
		addExp();
		for (Executor executor : executors){
			Player player = executor.GetPlayer(); 
			if(player == null) continue;
			for (String cmd : onCompleteCmds)
				try {Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", player.getName()).replace("{PLAYER}", player.getName()));	
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		for (String cmd : onCompleteOnceCmds)
			try {Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{UUID}", uuid.toString()).replace("{uuid}", uuid.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public boolean CheckLiveTime(){
		long time = java.time.Instant.now().getEpochSecond();
		if(startTime >= 0 && liveTime >= 0 && time - startTime >= liveTime){
			onFail();
			return false;
		}
		return true;
	}

	public boolean HasExecutor(Executor executor){
		return executors.contains(executor);
	}

	@Override
	public String toString(){
		String str = uuid.toString()+"\n" +	
				"    name: " + name + "\n" +
				"    id: " + id + "\n" + 
				"    is_end: " + isEnd + "\n" +
				"    start_time: " + startTime + "\n" +
				"    conditions:\n";
		for (Condition condition : conditions)
			str += "        - " + condition.toString().replace("\n", "\n        ") + "\n";
		str += "    executors:\n";
		for (Executor executor : executors)
			str += "        - " + executor.toString() + "\n";
		return str;
	}
}
