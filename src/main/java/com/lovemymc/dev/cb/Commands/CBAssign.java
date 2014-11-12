package com.lovemymc.dev.cb.Commands;

import com.google.code.chatterbotapi.ChatterBotSession;
import com.lovemymc.dev.cb.ChatterBot;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;

public class CBAssign extends Command {
	
    public CBAssign() {
		super("chatterbotassign", null, "cbassign");
	}

	public static HashMap<String, ChatterBotSession> assignie = new HashMap<String, ChatterBotSession>();

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {

        if (sender.hasPermission("chatterbot.admin")) {
            if (args.length == 0) {
                sender.sendMessage(ChatterBot.chatterBotName + ChatColor.GRAY + " Help:");
                sender.sendMessage(ChatColor.AQUA + "/cbassign <player> " + ChatColor.GRAY + "- Make ChatterBot reply to everything a player says");
            } else if (args.length == 1) {
                if (ProxyServer.getInstance().getPlayer(args[0]) != null) {
                    ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
                    if (assignie.containsKey(p.getName())) {
                        assignie.remove(p.getName());
                        sender.sendMessage(ChatterBot.chatterBotName + " " + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " has been un-assigned");
                    } else {
                        assignie.put(p.getName(), ChatterBot.bot1.createSession());
                        sender.sendMessage(ChatterBot.chatterBotName + " " + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " has been assigned");
                    }
                } else {
                    sender.sendMessage(ChatterBot.chatterBotName + ChatColor.RED + "Player not found");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have permission for this command");
        }
        return;
	}
}
