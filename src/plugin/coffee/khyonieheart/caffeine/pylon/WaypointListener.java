package coffee.khyonieheart.caffeine.pylon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;

public class WaypointListener implements Listener
{
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		Waypoint waypoint = WaypointManager.getWaypointAtLocation(event.getBlock());
		if (waypoint == null)
		{
			return;
		}
	}

	@EventHandler
	public void onExplosion(BlockExplodeEvent event)
	{
		Waypoint waypoint = WaypointManager.getWaypointAtLocation(event.getBlock());
		if (waypoint != null)
		{
			event.setCancelled(true);
		}
	}
}
