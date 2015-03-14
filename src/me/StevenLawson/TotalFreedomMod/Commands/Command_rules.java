package me.StevenLawson.TotalFreedomMod.Commands;

import static me.StevenLawson.TotalFreedomMod.Commands.TFM_Command.PLAYER_NOT_FOUND;
import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH, blockHostConsole = true)
@CommandParameters(description = "See the rules.", usage = "/<command> <player> <reason>")
public class Command_rules extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {

        Player player = getPlayer(args[0]);

            playerMsg(ChatColor.BOLD + "The Basic Rules of TatsuCraft", ChatColor.RED);
            playerMsg("1) No griefing. This is common sense, people!", ChatColor.GOLD);
            playerMsg("2) Try to avoid massive WorldEdits that would crash the server.", ChatColor.DARK_GREEN);
            playerMsg("3) Try to avoid teleporting to players if they don't give consent.", ChatColor.GOLD);
            playerMsg("4) Respect other players.", ChatColor.DARK_GREEN);
            playerMsg("5) Do not attempt to evade bans.", ChatColor.GOLD);
            playerMsg("6) Do not log in as admins. You won't have admin permissions and this will result in permanent ban.", ChatColor.DARK_GREEN);
            return true;
    }
}