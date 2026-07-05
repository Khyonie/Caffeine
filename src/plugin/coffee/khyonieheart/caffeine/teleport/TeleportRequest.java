package coffee.khyonieheart.caffeine.teleport;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import coffee.khyonieheart.caffeine.Caffeine;
import coffee.khyonieheart.caffeine.util.Gradient;

public class TeleportRequest
{
	private Player sender, target;
	private TeleportDirection direction;
	private BukkitTask timer;

	/** Map to track active requests */
	static Map<Player, Deque<TeleportRequest>> requests = new HashMap<>(); 

	/** Map to track the origin of a request */
	static Map<TeleportRequest, Player> requestReverse = new HashMap<>();

	TeleportRequest(
		Player sender,
		Player target,
		TeleportDirection direction
	) {
		this.sender = sender;
		this.target = target;
		this.direction = direction;

		storeRequest();
		switch (direction)
		{
			case TeleportDirection.SENDER_TO_TARGET -> {
				Gradient.sendGradient(sender, "#7F7F7F", "#FFFFFF", "Request sent.");
				Gradient.sendGradient(target, "#7F7F7F", "#FFFFFF", sender.getName() + " has requested to teleport to you.");
				Gradient.sendGradient(target, "#7F7F7F", "#FFFFFF", "\"/teleport accept\" to accept the request, or \"/teleport deny\" to dismiss it.");
			}
			case TeleportDirection.TARGET_TO_SENDER -> {
				Gradient.sendGradient(sender, "#7F7F7F", "#FFFFFF", "Request sent.");
				Gradient.sendGradient(target, "#7F7F7F", "#FFFFFF", sender.getName() + " has requested you to teleport to them.");
				Gradient.sendGradient(target, "#7F7F7F", "#FFFFFF", "\"/teleport accept\" to accept the request, or \"/teleport deny\" to dismiss it.");
			}
		}

		Bukkit.getScheduler().runTaskLater(Caffeine.getInstance(), () -> removeRequest(), Caffeine.getCaffeineConfig().getInteger("teleport.timeout"));
	}

	public void cancel()
	{
		this.timer.cancel();
		removeRequest();

		String senderTpaOverview = switch (this.direction) {
			case TeleportDirection.SENDER_TO_TARGET -> "you → " + target.getName();
			case TeleportDirection.TARGET_TO_SENDER -> target.getName() + " → you";
		};

		String targetTpaOverview = switch (this.direction) {
			case TeleportDirection.SENDER_TO_TARGET -> sender.getName() + " → you";
			case TeleportDirection.TARGET_TO_SENDER -> "you → " + sender.getName();
		};

		Gradient.sendGradient(sender, "#7F7F7F", "#FFFFFF", "Teleport request (" + senderTpaOverview + ") has been cancelled.");
		Gradient.sendGradient(target, "#7F7F7F", "#FFFFFF", "Teleport request (" + targetTpaOverview + ") has been cancelled.");
	}

	private void storeRequest()
	{
		if (!requests.containsKey(target))
		{
			requests.put(target, new ArrayDeque<>());
		}

		requests.get(target).push(this);
		requestReverse.put(this, this.sender);
	}

	private void removeRequest()
	{
		requestReverse.remove(this);
		requests.get(this.target).remove(this);

		// Remove empty deques
		if (requests.get(this.target).isEmpty())
		{
			requests.remove(this.target);
		}
	}

	public void execute()
	{
		int timeToTeleport = Caffeine.getCaffeineConfig().getInteger("teleport.time-to-teleport");
		this.removeRequest();
		switch (this.direction)
		{
			case TeleportDirection.SENDER_TO_TARGET -> {
				if (timeToTeleport != 0)
				{
					Gradient.sendGradient(sender, "#7F7F7F", "#FFFFFF", "Teleport request accepted. Commencing in " + timeToTeleport + " seconds...");
					Bukkit.getScheduler().runTaskLater(Caffeine.getInstance(), () -> {
						sender.teleport(target);
					}, timeToTeleport * 20l);
					return;
				}
				Gradient.sendGradient(sender, "#7F7F7F", "#FFFFFF", "Commencing teleport...");
				sender.teleport(target);
			}
			case TeleportDirection.TARGET_TO_SENDER -> {
				if (timeToTeleport != 0)
				{
					Gradient.sendGradient(sender, "#7F7F7F", "#FFFFFF", "Teleport request accepted. Commencing in " + timeToTeleport + " seconds...");
					Gradient.sendGradient(target, "#7F7F7F", "#FFFFFF", "Teleport request accepted. Commencing in " + timeToTeleport + " seconds...");
					Bukkit.getScheduler().runTaskLater(Caffeine.getInstance(), () -> {
						target.teleport(sender);
					}, timeToTeleport * 20l);
					return;
				}
				Gradient.sendGradient(sender, "#7F7F7F", "#FFFFFF", "Commencing teleport...");
				target.teleport(sender);
			}
		}
	}

	static TeleportRequest getMostRecentRequest(
		Player player
	) {
		if (!requests.containsKey(player))
		{
			return null;
		}

		return requests.get(player).peek();
	}

	static TeleportRequest getRequest(
		Player target,
		Player sender
	) {
		if (!requests.containsKey(target))
		{
			return null;
		}

		for (TeleportRequest req : requests.get(target))
		{
			if (requestReverse.get(req).equals(sender))
			{
				return req;
			}
		}

		return null;
	}

	static enum TeleportDirection
	{
		SENDER_TO_TARGET,
		TARGET_TO_SENDER
	}
}
