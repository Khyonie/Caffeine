package coffee.khyonieheart.caffeine.teleport;

import org.bukkit.entity.Player;

import coffee.khyonieheart.caffeine.teleport.TeleportRequest.TeleportDirection;
import coffee.khyonieheart.tidal.TidalCommand;
import coffee.khyonieheart.tidal.structure.Root;

public class TeleportCommand extends TidalCommand
{
	public TeleportCommand() 
	{
		super("teleport", "Fancy /tpa command.", "/teleport [ to | here | accept | deny ]", null, "ctp");
	}

	@Root(description = "Send a teleport request to the target player (you → them)")
	public void to(
		Player sender,
		Player target
	) {
		new TeleportRequest(sender, target, TeleportDirection.SENDER_TO_TARGET);
	}

	@Root(description = "Send a teleport request to the target player (them → you)")
	public void here(
		Player sender,
		Player target
	) {
		new TeleportRequest(sender, target, TeleportDirection.TARGET_TO_SENDER);
	}

	@Root(description = "Accept the most recent teleport request")
	public void accept(
		Player sender
	) {
		TeleportRequest.getMostRecentRequest(sender).execute();
	}

	@Root(value = "accept", description = "Accept a teleport request from a specific player")
	public void acceptSpecific(
		Player sender,
		Player target
	) {
		TeleportRequest.getRequest(sender, target).execute();
	}

	@Root(description = "Deny the most recent teleport request")
	public void deny(
		Player sender
	) {
		TeleportRequest.getMostRecentRequest(sender).cancel();
	}

	@Root(value = "deny", description = "Deny a teleport request from a specific player")
	public void denySpecific(
		Player sender,
		Player target
	) {
		TeleportRequest.getRequest(sender, target).cancel();
	}

}
