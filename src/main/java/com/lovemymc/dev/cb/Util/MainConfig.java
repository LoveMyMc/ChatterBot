package com.lovemymc.dev.cb.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MainConfig {

	private Plugin plugin;
	private File file;
	private Configuration config;
	
	private String resource_name = "config.yml";
	private String config_file_name = "config.yml";

	public MainConfig(Plugin plugin) {

		this.plugin = plugin;

		// The file you want it saved to.
		this.file = new File(getDataFolder(), config_file_name);

		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
				// Get the file from resources and save it.
				try (InputStream is = getResourceAsStream(resource_name);
						OutputStream os = new FileOutputStream(file)) {
						ByteStreams.copy(is, os);
				}
			} catch (IOException e) {
				throw new RuntimeException("Unable to create config file", e);
			}
		}
		
	}

	public Configuration getConfig() {
		try {
			return (this.config = ConfigurationProvider.getProvider(
					YamlConfiguration.class).load(this.file));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(
					config, new File(getDataFolder(), config_file_name));
		} catch (IOException e) {
			e.printStackTrace();
			plugin.getLogger().severe("Couldn't save config file!");
		}
	}

	private File getDataFolder() {
		return plugin.getDataFolder();
	}

	private InputStream getResourceAsStream(String file) {
		return plugin.getResourceAsStream(file);
	}

	public void reload() {
		try {
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
