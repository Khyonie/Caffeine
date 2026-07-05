package coffee.khyonieheart.caffeine.pylon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import coffee.khyonieheart.anenome.Nullable;

public class WaypointManager
{
	private static List<Waypoint> registeredWaypoints = new ArrayList<>();

	@Nullable
	public static Waypoint getWaypointAtLocation(Block block)
	{
		for (Waypoint w : registeredWaypoints)
		{
			if (w.isBlockPartOf(block))
			{
				return w;
			}
		}

		return null;
	}

	@Nullable
	public static Waypoint getWaypointByName(String name)
	{
		for (Waypoint w : registeredWaypoints)
		{
			if (w.getName().equals(name))
			{
				return w;
			}
		}

		return null;
	}
}
