package net.minecraftmurder.commands;

import java.util.logging.Level;

import net.minecraftmurder.main.MPlayer;
import net.minecraftmurder.tools.ChatContext;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WarnCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		// warn <player> [level]
		if (args.length != 2) {
			sender.sendMessage(ChatContext.ERROR_ARGUMENTS);
			sender.sendMessage(ChatContext.PREFIX_PLUGIN + "/warn <player> [level]");
			return true;
		}
		int level = 0;
		try {
			level = Integer.parseInt(args[1]);
		} catch (Exception e) {
			sender.sendMessage(ChatContext.PREFIX_WARNING + e.getMessage());
			return true;
		}
		if (level <= 0) {
			sender.sendMessage(ChatContext.PREFIX_WARNING + "Warn level must be higher than 0.");
			return true;
		}
		sender.sendMessage(ChatContext.PREFIX_PLUGIN + "Player warned.");
		MPlayer.addWarnLevel(args[0], level);
		Bukkit.getLogger().log(Level.INFO, sender.getName() + " warned " + args[0] + " with a level " + level + ".");
		return true;
	}

}
