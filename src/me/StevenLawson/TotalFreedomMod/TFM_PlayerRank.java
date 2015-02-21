package me.StevenLawson.TotalFreedomMod;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.DEVELOPERS;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum TFM_PlayerRank
{
    DEVELOPER("a " + ChatColor.DARK_PURPLE + "Developer", ChatColor.DARK_PURPLE + "[Dev]"),
    IMPOSTOR("an " + ChatColor.YELLOW + ChatColor.UNDERLINE + "Impostor", ChatColor.YELLOW.toString() + ChatColor.UNDERLINE + "[IMP]"),
    NON_OP("a " + ChatColor.GREEN + "Non-OP", ChatColor.GREEN.toString()),
    OP("an " + ChatColor.RED + "OP", ChatColor.RED + "[OP]"),
    SUPER("a " + ChatColor.GOLD + "Super Admin", ChatColor.GOLD + "[SA]"),
    TELNET("a " + ChatColor.DARK_GREEN + "Super Telnet Admin", ChatColor.DARK_GREEN + "[STA]"),
    SENIOR("a " + ChatColor.LIGHT_PURPLE + "Senior Admin", ChatColor.LIGHT_PURPLE + "[SrA]"),
    EXECUTIVE("a " + ChatColor.DARK_RED + "Executive Senior Admin", ChatColor.DARK_RED + "[Exec]"),
    BRONY("a... uh... " + ChatColor.LIGHT_PURPLE + "Brony?", ChatColor.LIGHT_PURPLE + "[Brony]"),
    OWNER("the " + ChatColor.BLUE + "Owner", ChatColor.BLUE + "[Owner]"),
    PERV("a " + ChatColor.LIGHT_PURPLE + "Pervert", ChatColor.LIGHT_PURPLE + "[Pervert]"),
    NUB(ChatColor.RED + "Nub", ChatColor.LIGHT_PURPLE + "[Pervert]"),
    SADEV("a " + ChatColor.GOLD + "Super Admin" + ChatColor.AQUA + "and " + ChatColor.DARK_PURPLE + "Developer", ChatColor.GOLD + "[SA]"),
    STADEV("a " + ChatColor.DARK_GREEN + "Super Telnet Admin" + ChatColor.AQUA + "and " + ChatColor.DARK_PURPLE + "Developer", ChatColor.DARK_GREEN + "[STA]"), //Not used right now
    CONSOLE("The " + ChatColor.DARK_PURPLE + "Console", ChatColor.DARK_PURPLE + "[Console]");
    private String loginMessage;
    private String prefix;

    private TFM_PlayerRank(String loginMessage, String prefix)
    {
        this.loginMessage = loginMessage;
        this.prefix = prefix;
    }

    public static String getLoginMessage(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return fromSender(sender).getLoginMessage();
        }

        final TFM_Admin entry = TFM_AdminList.getEntry((Player) sender);
        if (entry == null)
        {
            return fromSender(sender).getLoginMessage();
        }

        final String loginMessage = entry.getCustomLoginMessage();

        if (loginMessage == null || loginMessage.isEmpty())
        {
            return fromSender(sender).getLoginMessage();
        }

        return ChatColor.translateAlternateColorCodes('&', loginMessage);
    }

    public static TFM_PlayerRank fromSender(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return CONSOLE;
        }

        if (TFM_AdminList.isAdminImpostor((Player) sender))
        {
            return IMPOSTOR;
        }

        if (DEVELOPERS.contains(sender.getName()))
        {
            return DEVELOPER;
        }

        final TFM_Admin entry = TFM_AdminList.getEntry((Player) sender);

        final TFM_PlayerRank rank;

        if (entry != null && entry.isActivated())
        {
            if (TFM_ConfigEntry.SERVER_OWNERS.getList().contains(sender.getName()))
            {
                return OWNER;
            }
            if (TFM_ConfigEntry.SERVER_EXECS.getList().contains(sender.getName()))
            {
                return EXECUTIVE;
            }
            if (sender.getName().equals("ImALuckyBrony"))
            {
                return BRONY;
            }
            if (sender.getName().equals("neroblackcat"))
            {
                return PERV;
            }
            if (sender.getName().equals("Dirazi"))
            {
                return NUB;
            }
            if (sender.getName().equals("xDestroyer217"))
            {
                return SADEV;
            }
            if (sender.getName().equals("Got_No_Friends"))
            {
                return SADEV;
            }
            
            
            if (entry.isSeniorAdmin())
            {
                rank = SENIOR;
            }
            else if (entry.isTelnetAdmin())
            {
                rank = TELNET;
            }
            else
            {
                rank = SUPER;
            }
        }
        else
        {
            if (sender.isOp())
            {
                rank = OP;
            }
            else
            {
                rank = NON_OP;
            }

        }
        return rank;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getLoginMessage()
    {
        return loginMessage;
    }
}
