package coffee.khyonieheart.caffeine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import coffee.khyonieheart.anenome.operation.Results;
import coffee.khyonieheart.caffeine.event.PlayerLoadDataListener;
import coffee.khyonieheart.caffeine.teleport.TeleportCommand;
import coffee.khyonieheart.caffeine.teleport.TeleportListener;
import coffee.khyonieheart.caffeine.util.JarUtils;
import coffee.khyonieheart.lilac.LilacDecoder;
import coffee.khyonieheart.lilac.LilacEncoder;
import coffee.khyonieheart.lilac.TomlConfiguration;
import coffee.khyonieheart.lilac.TomlDecoder;
import coffee.khyonieheart.lilac.TomlEncoder;
import coffee.khyonieheart.lilac.TomlVersion;
import coffee.khyonieheart.tidal.TidalPlugin;

public class Caffeine extends JavaPlugin
{
	private static Map<UUID, PlayerData> loadedPlayerData = new HashMap<>();
	private static TomlConfiguration pluginConfiguration;
	public static final String DATA_FOLDER_PATH = "./Caffeine/";
	public static final String PLUGIN_CONFIG_PATH = DATA_FOLDER_PATH + "caffeine.toml";
	public static final String PLUGIN_VERSION = "0.1.0";

	private static Caffeine pluginInstance;

	@Override
	public void onEnable()
	{
		if (!(new File(DATA_FOLDER_PATH).exists()))
		{
			new File(DATA_FOLDER_PATH).mkdir();
		}

		try {
			loadPluginConfig();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		TidalPlugin.initDefaultCommands();
		new CaffeineCommand().register();
		new TeleportCommand().register();
		Bukkit.getPluginManager().registerEvents(new PlayerLoadDataListener(), this);
		Bukkit.getPluginManager().registerEvents(new TeleportListener(), this);

		pluginInstance = this;
	}

	@Override 
	public void onDisable()
	{
		TomlEncoder encoder = new LilacEncoder();
		loadedPlayerData.forEach((uuid, data) -> {
			File file = new File(DATA_FOLDER_PATH + uuid.toString() + ".toml");
			String toml = encoder.encode(data.toMap());
			
			try {
				Files.writeString(file.toPath(), toml);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static Caffeine getInstance()
	{
		return pluginInstance;
	}

	public static TomlConfiguration getCaffeineConfig()
	{
		return pluginConfiguration;
	}

	private void loadPluginConfig()
		throws IOException
	{
		File configFile = new File(PLUGIN_CONFIG_PATH);

		if (!configFile.exists())
		{
			// Copy from default
			JarFile pluginJar = JarUtils.getPluginJar("Caffeine");
			JarUtils.extractFromJar(pluginJar, "default-config.toml", configFile);
		}

		TomlDecoder decoder = new LilacDecoder(TomlVersion.V1_1_0);

		Map<String, Object> data = decoder.decode(configFile); // TODO This is unsafe
		pluginConfiguration = new TomlConfiguration(data);
	}

	public static PlayerData getPlayerData(
		Player player
	) {
		return loadedPlayerData.get(player.getUniqueId());
	}

	public static PlayerData loadPlayerData(
		Player player
	) {
		File filepath = new File(DATA_FOLDER_PATH + player.getUniqueId().toString() + ".toml");

		if (filepath.exists())
		{
			PlayerData data = Results.tryFunction(() -> PlayerData.of(new LilacDecoder(TomlVersion.V1_1_0).decode(filepath)))
				.ok()
				.unwrapOr(new PlayerData());

			loadedPlayerData.put(player.getUniqueId(), data);

			return data;
		}

		loadedPlayerData.put(player.getUniqueId(), new PlayerData());
		return loadedPlayerData.get(player.getUniqueId());
	}
}
