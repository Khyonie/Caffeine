package coffee.khyonieheart.caffeine.nickname;

import org.bukkit.entity.Player;

import coffee.khyonieheart.caffeine.Caffeine;
import coffee.khyonieheart.caffeine.PlayerData;
import coffee.khyonieheart.tidal.TidalCommand;
import coffee.khyonieheart.tidal.structure.Root;

public class NicknameCommand extends TidalCommand
{
	public NicknameCommand() 
	{
		super("nickname", "Sets your nickname when chatting.", "/nickname <name>", null, "nick");
	}

	@Root(isLocalExecutor = true)
	public void setNickname(
		Player sender,
		String nickname
	) {
		if (nickname.length() > 24)
		{
			sender.sendMessage("§cNickname is too long. Nicknames must be 4-24 characters");
			return;
		}

		if (nickname.length() < 4)
		{
			sender.sendMessage("§cNickname is too short. Nicknames must be 4-24 characters.");
			return;
		}

		// TODO Perform sanitization
		
		PlayerData data = Caffeine.getPlayerData(sender);
		data.setNickname(nickname);
		if (!data.getNickname().equals("none"))
		{
			sender.setPlayerListName(data.getNickname());
		}

		sender.sendMessage("§aNickname updated!");
	}
}
