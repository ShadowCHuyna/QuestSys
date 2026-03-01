package com.QuestSys;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.QuestSys.Commands.QuestSysCmdRouter;
import com.QuestSys.Commands.QuestSysTabRouter;
import com.QuestSys.DataBase.DBController;
import com.QuestSys.Events.EventListener;

final class ConsoleBanner {

    private static final String PREFIX = "[QuestSys] ";
         

    public static void print() {
        String[] logo = {
			" ██████  ",
			"██    ██ ",
			"██    ██ ",
			"██ ▄▄ ██ ",
			" ██████  ",
			"    ▀▀   "
        };
        
        String[][] data = {
                {"name",     "QuestSys"},
                {"version",       QuestSys.PickMe().getDescription().getVersion()},
                {"author",        "sch"},
                {"association",  "POTATOM"},
                {"repo",  "https://github.com/ShadowCHuyna/QuestSys"},
                {"POTATOM", "potatom.ru"}
        };

        int logoWidth = maxWidth(logo);
        int keyWidth  = maxWidth(data, 0);
        int valWidth  = maxWidth(data, 1);

        String top = "┌" + repeat("─", logoWidth + 2) + "┬"
                + repeat("─", keyWidth + 2) + "┬"
                + repeat("─", valWidth + 2) + "┐";

        String bottom = "└" + repeat("─", logoWidth + 2) + "┴"
                + repeat("─", keyWidth + 2) + "┴"
                + repeat("─", valWidth + 2) + "┘";

        log(top);

        int rows = Math.max(logo.length, data.length);
        for (int i = 0; i < rows; i++) {

            String l = i < logo.length ? logo[i] : "";
            String k = i < data.length ? data[i][0] : "";
            String v = i < data.length ? data[i][1] : "";

            String line = String.format(
                    "│ %-" + logoWidth + "s │ %-" + keyWidth + "s │ %-" + valWidth + "s │",
                    l, k, v
            );

            log(line);
        }

        log(bottom);
    }

    private static void log(String s) {
        Bukkit.getLogger().info(PREFIX + s);
    }

    private static int maxWidth(String[] arr) {
        int max = 0;
        for (String s : arr) max = Math.max(max, s.length());
        return max;
    }

    private static int maxWidth(String[][] arr, int col) {
        int max = 0;
        for (String[] row : arr) max = Math.max(max, row[col].length());
        return max;
    }

    private static String repeat(String s, int count) {
        return s.repeat(Math.max(0, count));
    }
}

public class QuestSys extends JavaPlugin {
	private static QuestSys instance;
	public static QuestSys PickMe() {return QuestSys.instance;}

	@Override
	public void onEnable() {
		super.onEnable();
		QuestSys.instance = this;
		System.out.println("Enable QuestSys!!!");
		saveDefaultConfig();

		getServer().getPluginManager().registerEvents(EventListener.PickMe(), this);

		DBController.PickMe().init(this.getDataFolder());
		DBController.PickMe().LoadQuests();

		RegCommands();

		ConsoleBanner.print();
	}

	@Override
	public void onDisable() {
		DBController.PickMe().SaveQuests();
		System.out.println("Disable QuestSys!!!");
	}

	private void RegCommands(){
        getCommand("questsys").setExecutor(new QuestSysCmdRouter());
		getCommand("questsys").setTabCompleter(new QuestSysTabRouter());
    }

}
