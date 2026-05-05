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

	@Root
	public void to(
		Player sender,
		Player target
	) {
		new TeleportRequest(sender, target, TeleportDirection.SENDER_TO_TARGET);
	}

	@Root
	public void here(
		Player sender,
		Player target
	) {
		new TeleportRequest(sender, target, TeleportDirection.TARGET_TO_SENDER);
	}

	@Root
	public void accept(
		Player sender
	) {
		TeleportRequest.getMostRecentRequest(sender).execute();
	}

	@Root("accept")
	public void acceptSpecific(
		Player sender,
		Player target
	) {
		TeleportRequest.getRequest(sender, target).execute();
	}

	@Root
	public void deny(
		Player sender
	) {
		TeleportRequest.getMostRecentRequest(sender).cancel();
	}

	@Root("deny")
	public void denySpecific(
		Player sender,
		Player target
	) {
		TeleportRequest.getRequest(sender, target).cancel();
	}

}
