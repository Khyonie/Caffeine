package coffee.khyonieheart.caffeine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import coffee.khyonieheart.anenome.operation.Results;
import coffee.khyonieheart.caffeine.event.PlayerLoadDataListener;
import coffee.khyonieheart.lilac.LilacDecoder;
import coffee.khyonieheart.lilac.LilacEncoder;
import coffee.khyonieheart.lilac.TomlEncoder;
import coffee.khyonieheart.lilac.TomlVersion;

public class Caffeine extends JavaPlugin
{
	private static Map<UUID, PlayerData> loadedPlayerData = new HashMap<>();
	public static final String DATA_FOLDER_PATH = "./Caffeine/";
	public static final String PLUGIN_VERSION = "0.1.0";

	@Override
	public void onEnable()
	{
		if (!(new File(DATA_FOLDER_PATH).exists()))
		{
			new File(DATA_FOLDER_PATH).mkdir();
		}

		new CaffeineCommand().register();
		Bukkit.getPluginManager().registerEvents(new PlayerLoadDataListener(), this);
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
