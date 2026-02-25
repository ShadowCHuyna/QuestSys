package com.sch.Quest;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sch.Conditions.Condition;
import com.sch.Executors.Executor;

public class Quest {
	private ArrayList<Condition> conditions = new ArrayList<Condition>();
	private ArrayList<Executor> executors = new ArrayList<Executor>();
	private UUID uuid;

	private String name;
	private String id;
	private String description;
	private double exp;
	private String scoreboard;
	private long liveTime;

	private String[] onCompliteCmds;
	private String[] onFailCmds;

	private boolean isEnd = false;
	private boolean isFail = false;
	private long startTime;

	private QuestManager questManager = QuestManager.PickMe();

	public Quest(
				// ArrayList<Executor> executors, 
				ArrayList<Condition> conditions,
				String name, 
				String id, 
				String description, 
				double exp, 
				// String scoreboard, 
				long liveTime,
				String[] onCompliteCmds,
				String[] onFailCmds
			){
		this.uuid = questManager.Put(this);
		
		this.name = name;
		this.id = id;
		this.description = description;
		this.exp = exp;
		// this.scoreboard = scoreboard;
		this.liveTime = liveTime;

		this.onCompliteCmds = onCompliteCmds;
		this.onFailCmds = onFailCmds;

		AddExecutors(new ArrayList<Executor>());
		AddCondition(conditions);

		startTime = java.time.Instant.now().getEpochSecond();
	}

	public UUID GetUUID(){return uuid;}
	public String GetName(){return name;}
	public String GetId(){return id;}
	public String GetDescription(){return description;}
	public double GetExp(){return exp;}
	public String GetScoreboard(){return scoreboard;}
	public long GetLiveTime(){return liveTime;}
	public boolean IsFail(){return isFail;}
	public boolean IsEnd(){return isEnd;}

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

		if(flag) onComplite();
	}

	public void onFail(){
		isFail = true;
		isEnd = true;
		for (Executor executor : executors){
			Player player = executor.GetPlayer(); 
			for (String cmd : onFailCmds) 
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", player.getName()));
		}
	}

	// @TODO scoreboard
	public void onComplite(){
		isEnd = true;
		for (Executor executor : executors){
			Player player = executor.GetPlayer(); 
			for (String cmd : onCompliteCmds) 
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", player.getName()));
		}
	}

	// @TODO отписываться во всех Condition
	public boolean CheckLiveTime(){
		long time = java.time.Instant.now().getEpochSecond();
		if(time - startTime >= liveTime){
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
		String str = "Quest{" +
				"\tuuid: " + uuid + "\n" +	
				"\tname: " + name + "\n" +
				"\tid: " + id + "\n" + 
				"\tdescription: " + description + "\n" +
				"\texp: " + exp + "\n" +
				"\tliveTime: " + liveTime + "\n" +
				"\tisFail: " + isFail + "\n" +
				"\tisEnd: " + isEnd + "\n" +
				"\tstartTime: " + startTime + "\n" +
				"\tconditions: [\n";
		for (Condition condition : conditions)
			str += "\t\t" + condition.toString() + "\n";
		str += "\t]\n";
		str += "\texecutors: [\n";
		for (Executor executor : executors)
			str += "\t\t" + executor.toString() + "\n";
		str += "\t]";
		return str;
	}
}
