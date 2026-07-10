package coffee.khyonieheart.caffeine;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.annotations.Expose;

public class PlayerData
{
	@SuppressWarnings("unused") // Schema tracker
	private static final long SCHEMA_VERSION = 1;
	public static final String BLANK_NICKNAME = "none";

	@Expose 
	private boolean 
		treecapitator = true, 
		veinminer = true;

	@Expose 
	private String 
		nickname = BLANK_NICKNAME, 
		username = "none";

	@Expose 
	private long schemaVersion = -1;

	public PlayerData() {}

	/**
	 * Stores a snapshot of the current player data in a map for serialization.
	 */
	public Map<String, Object> toMap()
	{
		Map<String, Object> data = new HashMap<>();

		for (Field field : this.getClass().getDeclaredFields())
		{
			if (!field.isAnnotationPresent(Expose.class))
			{
				continue;
			}

			field.setAccessible(true);

			try {
				// Store enums as a string
				if (field.getType().isEnum())
				{
					Enum<?> enumValue = (Enum<?>) field.get(this);
					data.put(field.getName(), enumValue.name());
					continue;
				}

				// Anything else is stored as-is
				data.put(field.getName(), field.get(this));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * Applies loaded player data.
	 */
	@SuppressWarnings("unchecked")
	public static PlayerData of(
		Map<String, Object> data
	) {
		PlayerData playerData = new PlayerData();

		// Load fields
		for (Field field : PlayerData.class.getDeclaredFields())
		{
			if (!field.isAnnotationPresent(Expose.class))
			{
				continue;
			}

			field.setAccessible(true);
			if (field.getType().isEnum())
			{
				Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) field.getType();
				Object defaultValue;
				try {
					defaultValue = field.get(playerData);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					continue;
				}
				
				String enumVariant = (String) data.get(field.getName());
				Object value = enumVariant == null ? defaultValue
					: ofGeneric(enumType, enumVariant);

				try {
					field.set(playerData, value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					continue;
				}

				continue;
			}

			try {
				if (field.getType().isPrimitive() && data.get(field.getName()) == null)
				{
					continue;
				}
				field.set(playerData, data.get(field.getName()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				continue;
			}
		}

		return playerData;
	}

	public void updateSchema(
		Player player
	) {
		long currentSchema = this.schemaVersion;
		// Schema 1: Store usernames
		if (this.schemaVersion == -1)
		{
			this.schemaVersion = 1;
			this.username = player.getName();
			this.nickname = BLANK_NICKNAME;
		}

		if (currentSchema != this.schemaVersion)
		{
			Bukkit.getConsoleSender().sendMessage("§aUpdated " + player.getUniqueId().toString() + "'s data (schema " + currentSchema + " → " + this.schemaVersion + ")");
			player.sendMessage("§aYour player data has been updated! If issues occur, please contact @khyonie on discord.");
			player.sendMessage("§7Data schema: " + currentSchema + " → " + this.schemaVersion);
		}
	}

	@SuppressWarnings("unchecked")
	private static <E extends Enum<E>> E ofGeneric(
		Class<?> type,
		String string
	) {
		Class<E> enumType = (Class<E>) type;
		return Enum.valueOf(enumType, string);
	}

	// Fields
	//-------------------------------------------------------------------------------- 

	public boolean isTreecapitatorEnabled()
	{
		return this.treecapitator;
	}

	public void setTreecapitatorEnabled(
		boolean setting
	) {
		this.treecapitator = setting;
	}

	public boolean isVeinminerEnabled()
	{
		return this.veinminer;
	}

	public void setVeinminerEnabled(
		boolean setting
	) {
		this.veinminer = setting;
	}

	public String getNickname()
	{
		return this.nickname;
	}

	public boolean hasNickname()
	{
		return this.nickname != null && !this.nickname.isBlank() && !this.nickname.equals(BLANK_NICKNAME);
	}

	public String getDisplayName(
		Player player
	) {
		return this.hasNickname() ? this.nickname : player.getName();
	}

	public void setNickname(
		String nickname
	) {
		this.nickname = nickname;
	}
}
