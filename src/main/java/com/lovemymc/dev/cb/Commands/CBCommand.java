package com.lovemymc.dev.cb.Commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import com.google.code.chatterbotapi.ChatterBotSession;
import com.lovemymc.dev.cb.ChatterBot;


public class CBCommand extends Command{
    private ChatterBot plugin;

    public CBCommand(ChatterBot p) {
    	super("chatterbox", null, "cb");
        plugin = p;
    }

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {
            final ProxiedPlayer p = (ProxiedPlayer) sender;
            if (p.hasPermission("chatterbot.use")) {
                if (args.length == 0) {
                    p.sendMessage(ChatterBot.chatterBotName + ChatColor.WHITE + " Correct usage: " + ChatColor.YELLOW + "/cb <message>");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        sb.append(" ").append(args[i]);
                    }
                    final String input = sb.toString().trim();
                    if (plugin.getConfig().getBoolean("Loud-Mode")) {
                        p.chat(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Loud-Format").replace("%bot%", ChatterBot.chatterBotName).replace("%message%", input)));
                    } else {
                        p.sendMessage(ChatColor.AQUA + "[" + p.getName() + "]" + ChatColor.WHITE + ": " + input);
                    }
                    p.sendMessage(ChatColor.AQUA + "Thinking...");
                    if (ChatterBot.sessions.containsKey(p.getName())) {
                        final ChatterBotSession cbSession = ChatterBot.sessions.get(p.getName());

                        plugin.getProxy().getScheduler().runAsync(ChatterBot.instance, new Runnable() {
                            @Override
                            public void run() {
                                final StringBuilder sb = new StringBuilder();
                                try {
                                    sb.append(cbSession.think(input));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                        if (plugin.getConfig().getBoolean("Loud-Mode")) {
                                            plugin.getProxy().broadcast(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ChatterBot-Format").replace("%name%", ChatterBot.chatterBotName).replace("%message%", sb.toString())));
                                        } else {
                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ChatterBot-Format").replace("%name%", ChatterBot.chatterBotName).replace("%message%", sb.toString())));
                                        }
                            }
                        });
                    } else {
                        final ChatterBotSession cbSession = ChatterBot.bot1.createSession();
                        ChatterBot.sessions.put(p.getName(), cbSession);

                        plugin.getProxy().getScheduler().runAsync(ChatterBot.instance, new Runnable() {
                            @Override
                            public void run() {
                                final StringBuilder sb = new StringBuilder();
                                try {
                                    sb.append(cbSession.think(input));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                        if (plugin.getConfig().getBoolean("Loud-Mode")) {
                                            plugin.getProxy().broadcast(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ChatterBot-Format").replace("%name%", ChatterBot.chatterBotName).replace("%message%", sb.toString())));
                                        } else {
                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ChatterBot-Format").replace("%name%", ChatterBot.chatterBotName).replace("%message%", sb.toString())));
                                        }
                            }
                        });
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "You don't have permission for this command.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
        }

        return;
		
	}
}
