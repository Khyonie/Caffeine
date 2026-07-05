package coffee.khyonieheart.caffeine.event;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import coffee.khyonieheart.caffeine.Caffeine;
import coffee.khyonieheart.caffeine.util.ConnectedBlocks;

public class TreecapitatorListener implements Listener
{
	private static final Set<Material> CHAINABLE_MATERIALS = Set.of(
		// Woods
		Material.OAK_LOG,
		Material.SPRUCE_LOG,
		Material.ACACIA_LOG,
		Material.BIRCH_LOG,
		Material.DARK_OAK_LOG,
		Material.JUNGLE_LOG,
		Material.MANGROVE_LOG,
		Material.CRIMSON_STEM,
		Material.WARPED_STEM,
		Material.CHERRY_LOG,
		Material.PALE_OAK_LOG,

		Material.OAK_WOOD,
		Material.SPRUCE_WOOD,
		Material.BIRCH_WOOD,
		Material.JUNGLE_WOOD,
		Material.ACACIA_WOOD,
		Material.DARK_OAK_WOOD,
		Material.MANGROVE_WOOD,
		Material.CRIMSON_HYPHAE,
		Material.WARPED_HYPHAE,
		Material.CHERRY_WOOD,
		Material.PALE_OAK_WOOD,

		// Ores
		Material.COAL_ORE,
		Material.DEEPSLATE_COAL_ORE,
		Material.IRON_ORE,
		Material.DEEPSLATE_IRON_ORE,
		Material.COPPER_ORE,
		Material.DEEPSLATE_COPPER_ORE,
		Material.GOLD_ORE,
		Material.DEEPSLATE_GOLD_ORE,
		Material.REDSTONE_ORE,
		Material.DEEPSLATE_REDSTONE_ORE,
		Material.EMERALD_ORE,
		Material.DEEPSLATE_EMERALD_ORE,
		Material.LAPIS_ORE,
		Material.DEEPSLATE_LAPIS_ORE,
		Material.DIAMOND_ORE,
		Material.DEEPSLATE_DIAMOND_ORE,
		Material.NETHER_GOLD_ORE,
		Material.NETHER_QUARTZ_ORE
	);

	private static final Set<Material> LEAF_TYPES = Set.of(
		Material.OAK_LEAVES,
		Material.SPRUCE_LEAVES,
		Material.BIRCH_LEAVES,
		Material.JUNGLE_LEAVES,
		Material.ACACIA_LEAVES,
		Material.DARK_OAK_LEAVES,
		Material.MANGROVE_LEAVES,
		Material.AZALEA_LEAVES,
		Material.FLOWERING_AZALEA_LEAVES,
		Material.PALE_OAK_LEAVES,

		Material.NETHER_WART_BLOCK,
		Material.SHROOMLIGHT,
		Material.WARPED_WART_BLOCK
	);

	private static final Set<Material> AXE_TYPES = Set.of(
		Material.WOODEN_AXE,
		Material.STONE_AXE,
		Material.COPPER_AXE,
		Material.IRON_AXE,
		Material.GOLDEN_AXE,
		Material.DIAMOND_AXE,
		Material.NETHERITE_AXE
	);

	private static final Set<Material> PICKAXE_TYPES = Set.of(
		Material.WOODEN_PICKAXE,
		Material.STONE_PICKAXE,
		Material.COPPER_PICKAXE,
		Material.IRON_PICKAXE,
		Material.GOLDEN_PICKAXE,
		Material.DIAMOND_PICKAXE,
		Material.NETHERITE_PICKAXE
	);

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (event.getBlock() == null)
		{
			return;
		}

		if (!CHAINABLE_MATERIALS.contains(event.getBlock().getType()))
		{
			return;
		}

		ItemStack held = event.getPlayer().getInventory().getItemInMainHand();
		if (held == null)
		{
			return;
		}

		final Material blockMaterial = event.getBlock().getType();

		if (AXE_TYPES.contains(held.getType()) && Caffeine.getPlayerData(event.getPlayer()).isTreecapitatorEnabled())
		{
			ConnectedBlocks.getConnectedBlocks(event.getBlock(), (block) -> {
				return blockMaterial.equals(block.getType());
			}, (block) -> {
				return LEAF_TYPES.contains(block.getType());
			}).primaryForEach((block) -> {
				// Don't break if the damage would otherwise break the held item
				if (((Damageable) held.getItemMeta()).getDamage() < held.getType().getMaxDurability())
				{
					block.breakNaturally(held);

					Damageable meta = (Damageable) held.getItemMeta();
					meta.setDamage(meta.getDamage() + 1);
					held.setItemMeta(meta);
				}
			}).secondaryForEach((block) -> {
				block.breakNaturally();
			});

			return;
		}

		if (PICKAXE_TYPES.contains(held.getType()) && Caffeine.getPlayerData(event.getPlayer()).isVeinminerEnabled())
		{
			if (held.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH))
			{
				return;
			}

			ConnectedBlocks.getConnectedBlocks(event.getBlock(), (block) -> {
				return blockMaterial.equals(block.getType());
			}, 
				null
			).primaryForEach((block) -> {
				if (((Damageable) held.getItemMeta()).getDamage() < held.getType().getMaxDurability())
				{
					block.breakNaturally(held);

					event.getPlayer().giveExp(event.getExpToDrop());

					Damageable meta = (Damageable) held.getItemMeta();
					meta.setDamage(meta.getDamage() + 1);
					held.setItemMeta(meta);
				}
			});
		}
	}
}
