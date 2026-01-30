package coffee.khyonieheart.caffeine.teleport;

import org.bukkit.entity.Player;

import coffee.khyonieheart.tidal.TidalCommand;
import coffee.khyonieheart.tidal.structure.Root;

public class TeleportCommand extends TidalCommand
{
	public TeleportCommand() 
	{
		super("teleport", "Base command for Caffeine plugin.", "/caffeine", null, "ctp");
	}

	@Root
	public void to(
		Player sender,
		Player target
	) {

	}

	@Root
	public void accept(
		Player sender
	) {

	}

	@Root("accept")
	public void acceptSpecific(
		Player sender,
		Player target
	) {

	}

	@Root
	public void deny(
		Player sender
	) {

	}

	@Root("deny")
	public void denySpecific(
		Player sender,
		Player target
	) {

	}
}
