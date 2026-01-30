package coffee.khyonieheart.caffeine.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import coffee.khyonieheart.caffeine.Caffeine;

public class PlayerLoadDataListener implements Listener
{
	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(
		PlayerJoinEvent event
	) {
		Caffeine.loadPlayerData(event.getPlayer());
	}
}
