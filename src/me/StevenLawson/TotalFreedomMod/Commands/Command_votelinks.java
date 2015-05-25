package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

@CommandPermissions(level = AdminLevel.OP, source = SourceType.BOTH)
@CommandParameters(description = "Shows information on how to vote.", usage = "/<command>", aliases = "vote,votelink")
public class Command_votelinks extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {

        playerMsg(TFM_Util.randomChatColor() + "You can vote for the server at any of the following links:");
        playerMsg(TFM_Util.randomChatColor() + "Minecraft-MP: http://minecraft-mp.com/server/76892/vote/");
        playerMsg(TFM_Util.randomChatColor() + "minecraftservers.org: http://minecraftservers.org/vote/236391");
        playerMsg(TFM_Util.randomChatColor() + "Planet Minecraft: http://www.planetminecraft.com/server/tatsucraft/");

        return true;
    }
}
