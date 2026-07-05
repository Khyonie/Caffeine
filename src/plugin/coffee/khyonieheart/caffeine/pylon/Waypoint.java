package coffee.khyonieheart.caffeine.pylon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.google.gson.annotations.Expose;

public class Waypoint
{
	@Expose 
	private int x, y, z;
	@Expose 
	private String world, ownerUUID, ownerName, pylonName;
	@Expose 
	private List<String> addedPlayers = new ArrayList<>();

	private Set<Block> incorporatedBlocks = new HashSet<>();

	/** @deprecated Deserialization target */
	@Deprecated 
	Waypoint() {
	};

	public Waypoint(Block seed, Player owner) 
	{
		this.x = seed.getX();
		this.y = seed.getY();
		this.z = seed.getZ();
		this.world = seed.getWorld().getName();
	}

	void addBlocks(Block seed)
	{
		// Bell
		Block current = seed;
		incorporatedBlocks.add(seed);

		// Amethyst stem
		current = current.getRelative(BlockFace.DOWN);
		incorporatedBlocks.add(current);
		current = current.getRelative(BlockFace.DOWN);
		incorporatedBlocks.add(current);

		// Cross of endstone bricks
		incorporatedBlocks.add(current.getRelative(BlockFace.NORTH));
		incorporatedBlocks.add(current.getRelative(BlockFace.EAST));
		incorporatedBlocks.add(current.getRelative(BlockFace.SOUTH));
		incorporatedBlocks.add(current.getRelative(BlockFace.WEST));
	}

	public boolean isBlockPartOf(Block block)
	{
		return this.incorporatedBlocks.contains(block);
	}

	public String getName()
	{
		return this.pylonName;
	}
}
