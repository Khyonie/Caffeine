package coffee.khyonieheart.caffeine.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import coffee.khyonieheart.anenome.NotNull;
import coffee.khyonieheart.anenome.Nullable;

public class ConnectedBlocks
{
	public static ConnectedBlocksResult getConnectedBlocks(
		@NotNull Block seed,
		@NotNull Predicate<Block> filter,
		@Nullable Predicate<Block> closedFilter
	) {
		Deque<Block> open = new ArrayDeque<>();
		List<Block> closed = new ArrayList<>();
		List<Block> secondaryClosed = new ArrayList<>();

		open.add(seed);

		Block target;
		Block relativeTarget;
		Location targetLocation;
		final World world = seed.getWorld();
		while (!open.isEmpty())
		{
			target = open.removeLast();
			closed.add(target);
			targetLocation = target.getLocation().clone();

			for (int y = -1; y < 2; y++)
			{
				for (int x = -1; x < 2; x++)
				{
					for (int z = -1; z < 2; z++)
					{
						relativeTarget = world.getBlockAt(targetLocation.clone().add(x, y, z));

						if (target.equals(relativeTarget))
						{
							continue;
						}

						if (relativeTarget == null)
						{
							continue;
						}

						if (open.contains(relativeTarget) || closed.contains(relativeTarget))
						{
							continue;
						}

						if (filter.test(relativeTarget))
						{
							open.push(relativeTarget);
							continue;
						}

						if (closedFilter == null)
						{
							continue;
						}

						if (closedFilter.test(relativeTarget))
						{
							secondaryClosed.add(relativeTarget);
						}
					}
				}
			}
		}

		return new ConnectedBlocksResult(closed, secondaryClosed);
	}

	public static class ConnectedBlocksResult
	{
		private final List<Block> primaryClosed;
		private final List<Block> secondaryClosed;

		public ConnectedBlocksResult(List<Block> primaryClosed, List<Block> secondaryClosed)
		{
			this.primaryClosed = primaryClosed;
			this.secondaryClosed = secondaryClosed;
		}

		public ConnectedBlocksResult primaryForEach(Consumer<Block> action)
		{
			primaryClosed.forEach((b) -> action.accept(b));

			return this;
		}

		public ConnectedBlocksResult secondaryForEach(Consumer<Block> action)
		{
			secondaryClosed.forEach((b) -> action.accept(b));

			return this;
		}
	}
}
