package com.sch;

import org.bukkit.plugin.java.JavaPlugin;

import com.sch.Commands.QuestSysCmdRouter;
import com.sch.Events.EventListener;

public class QuestSys extends JavaPlugin {
	private static QuestSys instance;
	public static QuestSys PickMe() {return QuestSys.instance;}

	void DrawLogo(){
		System.out.println("\n"+
                        "   ▄███████▄  ▄██████▄      ███        ▄████████     ███      ▄██████▄    ▄▄▄▄███▄▄▄▄   \n" + //
                        "  ███    ███ ███    ███ ▀█████████▄   ███    ███ ▀█████████▄ ███    ███ ▄██▀▀▀███▀▀▀██▄ \n" + //
                        "  ███    ███ ███    ███    ▀███▀▀██   ███    ███    ▀███▀▀██ ███    ███ ███   ███   ███ \n" + //
                        "  ███    ███ ███    ███     ███   ▀   ███    ███     ███   ▀ ███    ███ ███   ███   ███ \n" + //
                        "▀█████████▀  ███    ███     ███     ▀███████████     ███     ███    ███ ███   ███   ███ \n" + //
                        "  ███        ███    ███     ███       ███    ███     ███     ███    ███ ███   ███   ███ \n" + //
                        "  ███        ███    ███     ███       ███    ███     ███     ███    ███ ███   ███   ███ \n" + //
                        " ▄████▀       ▀██████▀     ▄████▀     ███    █▀     ▄████▀    ▀██████▀   ▀█   ███   █▀  \n" + //
                        "                                                                                        ");
	}

	@Override
	public void onEnable() {
		super.onEnable();
		QuestSys.instance = this;
		System.out.println("Enable QuestSys!!!");
		saveDefaultConfig();

		getServer().getPluginManager().registerEvents(EventListener.PickMe(), this);

		RegCommands();

		DrawLogo();
	}

	@Override
	public void onDisable() {
		System.out.println("Disable QuestSys!!!");
	}

	private void RegCommands(){
        getCommand("QuestSys").setExecutor(new QuestSysCmdRouter());
    }

}
