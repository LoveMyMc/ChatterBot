package com.lovemymc.dev.cb;

import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.lovemymc.dev.cb.Commands.CBAssign;
import com.lovemymc.dev.cb.Commands.CBCommand;
import com.lovemymc.dev.cb.Listeners.AsyncChat;
import com.lovemymc.dev.cb.Util.MainConfig;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.util.HashMap;

public class ChatterBot extends Plugin {
    public ChatterBotFactory factory;
    public static com.google.code.chatterbotapi.ChatterBot bot1;

    public static HashMap<String, ChatterBotSession> sessions = new HashMap<String, ChatterBotSession>();

    public static String chatterBotName;

    public static ChatterBot instance;

    public ChatterBot() {
        instance = this;
    }

    private MainConfig config;

    @Override
    public void onEnable() {

        try {
            getDataFolder().mkdirs();
            config = new MainConfig(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        factory = new ChatterBotFactory();
        try {
            bot1 = factory.create(ChatterBotType.valueOf(getConfig().getString("ChatterBot-Type")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        chatterBotName = ChatColor.translateAlternateColorCodes('&', getString("ChatterBot-Name"));

        //getCommand("cb").setExecutor(new CBCommand(this));
        //getCommand("chatterbot").setExecutor(new CBCommand(this));
        //getCommand("cbassign").setExecutor(new CBAssign());
        //getCommand("chatterbotassign").setExecutor(new CBAssign());
        getProxy().getPluginManager().registerCommand(this, new CBCommand(this));
        getProxy().getPluginManager().registerCommand(this, new CBAssign());

        getProxy().getPluginManager().registerListener(this, new AsyncChat(this));
    }

	//************************************************************************************//

	public MainConfig getConfiguration() {
		if (this.config == null)	this.config = new MainConfig(this);
		return this.config;
	}

	public Configuration getConfig() {
		return getConfiguration().getConfig();
	}

	public void saveConfig() {
		getConfiguration().saveConfig();
	}
	
	public String getString(String path){
		return ChatColor.translateAlternateColorCodes('&', getConfiguration().getConfig().getString(path));
	}
	
	//************************************************************************************//

}
