package com.sch.Quest;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.sch.Conditions.Condition;
import com.sch.Executors.Executor;

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
	String[] onCompleteOnceCmds;
	String[] onFailOnceCmds;

	private boolean isEnd = false;
	private boolean isFail = false;
	private long startTime = -1;

	public void Delete(){
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
				String[] onFailOnceCmds
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
		
		AddExecutors(new ArrayList<Executor>());
		AddCondition(conditions);

		// startTime = java.time.Instant.now().getEpochSecond();
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
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", player.getName()));
		}
		for (String cmd : onFailOnceCmds) 
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{UUID}", uuid.toString()));
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
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", player.getName()));
		}
		for (String cmd : onCompleteOnceCmds) 
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{UUID}", uuid.toString()));
		
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
		for (Executor e : executors)
			if(e == executor) return true;
		return false;
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
