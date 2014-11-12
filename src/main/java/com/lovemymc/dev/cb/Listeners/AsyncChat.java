package com.lovemymc.dev.cb.Listeners;

import com.google.code.chatterbotapi.ChatterBotSession;
import com.lovemymc.dev.cb.ChatterBot;
import com.lovemymc.dev.cb.Commands.CBAssign;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;

public class AsyncChat implements Listener {
	private ChatterBot plugin;
	private HashMap<String, Long> triggered = new HashMap<String, Long>();
	private HashMap<String, ChatterBotSession> sessions = new HashMap<String, ChatterBotSession>();

	public AsyncChat(ChatterBot p) {
		plugin = p;
	}

	@EventHandler
	public void onChat(final ChatEvent e) {
		if (!(e.getSender() instanceof ProxiedPlayer))
			return;
		ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
		if (!e.isCancelled()) {
			if (CBAssign.assignie.containsKey(sender.getName())) {
				final ChatterBotSession cbSession = CBAssign.assignie
						.get(sender.getName());

				plugin.getProxy().getScheduler()
						.runAsync(ChatterBot.instance, new Runnable() {
							@SuppressWarnings("deprecation")
							@Override
							public void run() {
								final StringBuilder sb = new StringBuilder();
								try {
									sb.append(cbSession.think(e.getMessage()));
								} catch (Exception e) {
									e.printStackTrace();
								}
								plugin.getProxy()
										.broadcast(
												ChatColor
														.translateAlternateColorCodes(
																'&',
																plugin.getConfig()
																		.getString(
																				"ChatterBot-Format")
																		.replace(
																				"%name%",
																				ChatterBot.chatterBotName)
																		.replace(
																				"%message%",
																				sb.toString())));
							}
						});
			} else {
				for (String word : ChatterBot.instance.getConfig()
						.getStringList("Trigger-Word-List")) {
					if (!ChatterBot.instance.getConfig().getBoolean("No-Trigger-Words")
							&& e.getMessage().toLowerCase()
									.contains(word.toLowerCase())) {
						if (triggered.containsKey(sender.getName())) {
							triggered
									.put(sender.getName(),
											System.currentTimeMillis()
													+ (ChatterBot.instance
															.getConfig()
															.getInt("Trigger-Word-Timeout") * 1000));

							final ChatterBotSession cbSession = sessions
									.get(sender.getName());

							plugin.getProxy()
									.getScheduler()
									.runAsync(ChatterBot.instance,
											new Runnable() {
												@SuppressWarnings("deprecation")
												@Override
												public void run() {
													final StringBuilder sb = new StringBuilder();
													try {
														sb.append(cbSession.think(e
																.getMessage()));
													} catch (Exception e) {
														e.printStackTrace();
													}
													plugin.getProxy()
															.broadcast(
																	ChatColor
																			.translateAlternateColorCodes(
																					'&',
																					plugin.getConfig()
																							.getString(
																									"ChatterBot-Format")
																							.replace(
																									"%name%",
																									ChatterBot.chatterBotName)
																							.replace(
																									"%message%",
																									sb.toString())));
												}
											});
						} else {
							final ChatterBotSession cbSession = ChatterBot.bot1
									.createSession();

							triggered
									.put(sender.getName(),
											System.currentTimeMillis()
													+ (ChatterBot.instance
															.getConfig()
															.getInt("Trigger-Word-Timeout") * 1000));
							sessions.put(sender.getName(), cbSession);

							plugin.getProxy()
									.getScheduler()
									.runAsync(ChatterBot.instance,
											new Runnable() {
												@SuppressWarnings("deprecation")
												@Override
												public void run() {
													final StringBuilder sb = new StringBuilder();
													try {
														sb.append(cbSession.think(e
																.getMessage()));
													} catch (Exception e) {
														e.printStackTrace();
													}
													plugin.getProxy()
															.broadcast(
																	ChatColor
																			.translateAlternateColorCodes(
																					'&',
																					plugin.getConfig()
																							.getString(
																									"ChatterBot-Format")
																							.replace(
																									"%name%",
																									ChatterBot.chatterBotName)
																							.replace(
																									"%message%",
																									sb.toString())));
												}
											});
						}

						return;
					}
				}

				if (triggered.containsKey(sender.getName())) {
					if (!(System.currentTimeMillis() >= triggered.get(sender
							.getName()))) {

						final ChatterBotSession cbSession = sessions.get(sender
								.getName());

						plugin.getProxy().getScheduler()
								.runAsync(ChatterBot.instance, new Runnable() {
									@SuppressWarnings("deprecation")
									@Override
									public void run() {
										final StringBuilder sb = new StringBuilder();
										try {
											sb.append(cbSession.think(e
													.getMessage()));
										} catch (Exception e) {
											e.printStackTrace();
										}
										plugin.getProxy()
												.broadcast(
														ChatColor
																.translateAlternateColorCodes(
																		'&',
																		plugin.getConfig()
																				.getString(
																						"ChatterBot-Format")
																				.replace(
																						"%name%",
																						ChatterBot.chatterBotName)
																				.replace(
																						"%message%",
																						sb.toString())));
									}
								});
					}
				}
			}
		}
	}

}
