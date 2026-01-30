package coffee.khyonieheart.caffeine;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import coffee.khyonieheart.tidal.TidalCommand;
import coffee.khyonieheart.tidal.structure.Root;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class CaffeineCommand extends TidalCommand
{
	public CaffeineCommand() 
	{
		super("caffeine", "Base command for Caffeine plugin.", "/caffeine", null, "caf");
	}

	@Root(isRootExecutor = true)
	public void caffeineCommand(
		CommandSender sender
	) {
		Gradient.sendGradient(sender, "#7F7F7F", "#FFFFFF", "[☕] Caffeine v" + Caffeine.PLUGIN_VERSION);
	}

	@Root
	public void treecap(
		Player player,
		boolean enabled
	) {
		PlayerData data = Caffeine.getPlayerData(player);
		data.setTreecapitatorEnabled(enabled);

		ComponentBuilder builder = new ComponentBuilder();
		builder.append("§7Treecapitator has been ");

		BaseComponent[] setting = data.isTreecapitatorEnabled() ? 
			new Gradient("#7FFF7F", "#FFFFFF").createComponents("enabled.") : 
			new Gradient("#FF7F7F", "#FFFFFF").createComponents("disabled.");

		builder.append(setting);
		player.spigot().sendMessage(builder.create());
	}
}
