package coffee.khyonieheart.caffeine;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;

import coffee.khyonieheart.anenome.operation.Option;

public class PlayerData
{
	@Expose private boolean treecapitator = true;
	@Expose private boolean veinminer = true;

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
				
				Object value = Option.of(data.get(field.getName()))
					.map((s) -> ofGeneric(enumType, (String) s))
					.unwrapOr(defaultValue);

				try {
					field.set(playerData, value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					continue;
				}

				continue;
			}
		}

		return playerData;
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
}
