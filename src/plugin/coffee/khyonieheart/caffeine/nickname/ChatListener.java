package coffee.khyonieheart.caffeine.nickname;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import coffee.khyonieheart.caffeine.Caffeine;
import coffee.khyonieheart.caffeine.PlayerData;

public class ChatListener implements Listener
{
	private static final Pattern CHAT_FORMATTING_PATTERNS = Pattern.compile("&(?=[a-fA-F0-9])");

	@EventHandler
	public void onChat(
		AsyncPlayerChatEvent event
	) {
		event.setCancelled(true);
		PlayerData data = Caffeine.getPlayerData(event.getPlayer());
		String name = data.getDisplayName(event.getPlayer());

		String message = event.getMessage();

		// Handle formatting
		Matcher matcher = CHAT_FORMATTING_PATTERNS.matcher(message);
		message = matcher.replaceAll("§");

		Bukkit.getConsoleSender().sendMessage(event.getPlayer().getName() + ": " + message);
		for (Player p : Bukkit.getOnlinePlayers())
		{
			p.sendMessage("<" + name + ">: " + message);
		}
	}
}
