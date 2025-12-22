package coffee.khyonieheart.caffeine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import coffee.khyonieheart.anenome.operation.Results;
import coffee.khyonieheart.lilac.LilacDecoder;
import coffee.khyonieheart.lilac.TomlVersion;

public class Caffeine extends JavaPlugin
{
	private static Map<UUID, PlayerData> loadedPlayerData = new HashMap<>();
	
	public static final String DATA_FOLDER_PATH = "./Caffeine/";

	@Override
	public void onEnable()
	{
		
	}

	@Override 
	public void onDisable()
	{

	}

	public static PlayerData loadPlayerData(
		Player player
	) {
		File filepath = new File(DATA_FOLDER_PATH + player.getUniqueId().toString() + ".toml");

		if (filepath.exists())
		{
			PlayerData data = Results.tryFunction(() -> new PlayerData(new LilacDecoder(TomlVersion.V1_1_0).decode(filepath)))
				.ok()
				.unwrapOr(new PlayerData());

			loadedPlayerData.put(player.getUniqueId(), data);

			return data;
		}

		loadedPlayerData.put(player.getUniqueId(), new PlayerData());
		return loadedPlayerData.get(player.getUniqueId());
	}
}
