package coffee.khyonieheart.caffeine.event;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathChestListener implements Listener
{
	private static final int VOID_HEIGHT = -64;

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		List<ItemStack> drops = event.getDrops();
		Location targetLocation = event.getEntity().getLocation();

		try {
			// Void logic
			if (targetLocation.getY() <= VOID_HEIGHT)
			{
				targetLocation.setY(0);
			}

			dumpContents(targetLocation, drops);
			event.getEntity().sendMessage("§6Your items have been placed in a chest at " + targetLocation.getBlockX() + ", " + targetLocation.getBlockY() + ", " + targetLocation.getBlockZ() + ".");
		} catch (Exception e) {
			event.getEntity().sendMessage("§cFailed to create a death chest. As a safety precaution, you have not dropped your inventory. Please send a picture of this message to Khyonie.");
			event.getEntity().sendMessage("§7" + e.getClass().getSimpleName() + ": " + e.getMessage());

			event.setKeepInventory(true);
			event.getDrops().clear();
		}
	}

	private void dumpContents(
		Location location,
		List<ItemStack> drops
	) {
		while (!drops.isEmpty())
		{
			Block currentBlock = location.getBlock();
			while (!currentBlock.isEmpty())
			{
				currentBlock = currentBlock.getRelative(BlockFace.UP);
			}

			currentBlock.setType(Material.CHEST);
			Container containerData = (Container) currentBlock.getState();
			fillChest(containerData.getInventory(), drops);
		}
	}

	private void fillChest(
		Inventory chest,
		List<ItemStack> drops
	) {
		while (chest.firstEmpty() != -1 && !drops.isEmpty())
		{
			chest.addItem(drops.removeLast());
		}
	}
}
