package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Check the smite status of a player", usage = "/<command>", aliases = "sstat")
public class Command_smitestatus extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
    if (args.length == 0 || args.length < 1)
    {
       playerMsg(ChatColor.RED + "Invalid amount of command arguments.");
    }
        Player player = getPlayer(args[0]);
        TFM_PlayerData playerData = TFM_PlayerData.getPlayerData(player);
        if (playerData.isSmited())
        {
            playerMsg(ChatColor.RED + player.getName() + "Yes");
        }
        else
        {
            playerMsg("No");
        }
        

        return true;
    }
}