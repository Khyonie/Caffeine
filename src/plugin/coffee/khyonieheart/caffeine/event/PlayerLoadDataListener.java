package coffee.khyonieheart.caffeine.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import coffee.khyonieheart.caffeine.Caffeine;
import coffee.khyonieheart.caffeine.PlayerData;

public class PlayerLoadDataListener implements Listener
{
	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(
		PlayerJoinEvent event
	) {
		PlayerData data = Caffeine.loadPlayerData(event.getPlayer());
		data.updateSchema(event.getPlayer());

		// Set player's tab list name
		if (!data.getNickname().equals("none"))
		{
			event.getPlayer().setPlayerListName(data.getNickname());
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onLeave(
		PlayerQuitEvent event
	) {
		Caffeine.storePlayerData(event.getPlayer());
	}
}
