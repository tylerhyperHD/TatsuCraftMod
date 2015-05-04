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
    DONATOR("a " + ChatColor.DARK_PURPLE + "Donator", ChatColor.DARK_PURPLE + "[Donator]"),
    DONATORPLUS("a " + ChatColor.LIGHT_PURPLE + "Donator+", ChatColor.LIGHT_PURPLE + "[Donator+]"),
    SUPER("a " + ChatColor.GOLD + "Super Admin", ChatColor.GOLD + "[SA]"),
    SUPERD("a " + ChatColor.GOLD + "Super Admin " + ChatColor.AQUA + "and " + ChatColor.DARK_PURPLE + "Donator", ChatColor.GOLD + "[SA " + ChatColor.DARK_GRAY + "+" + ChatColor.DARK_PURPLE + "Donator]"),
    SUPERDP("a " + ChatColor.GOLD + "Super Admin " + ChatColor.AQUA + "and " + ChatColor.LIGHT_PURPLE + "Donator+", ChatColor.GOLD + "[SA " + ChatColor.DARK_GRAY + "+" + ChatColor.LIGHT_PURPLE + "Donator+]"),    
    TELNET("a " + ChatColor.DARK_GREEN + "Super Telnet Admin", ChatColor.DARK_GREEN + "[STA]"),
    TELNETD("a " + ChatColor.DARK_GREEN + "Super Telnet Admin " + ChatColor.AQUA + "and " + ChatColor.DARK_PURPLE + "Donator", ChatColor.DARK_GREEN + "[STA " + ChatColor.DARK_GRAY + "+" + ChatColor.DARK_PURPLE + "Donator]"),
    TELNETDP("a " + ChatColor.DARK_GREEN + "Super Telnet Admin " + ChatColor.AQUA + "and " + ChatColor.LIGHT_PURPLE + "Donator+", ChatColor.DARK_GREEN + "[STA " + ChatColor.DARK_GRAY + "+" + ChatColor.LIGHT_PURPLE + "Donator+]"),
    SENIOR("a " + ChatColor.LIGHT_PURPLE + "Senior Admin", ChatColor.LIGHT_PURPLE + "[SrA]"),
    SENIORD("a " + ChatColor.LIGHT_PURPLE + "Senior Admin " + ChatColor.AQUA + "and " + ChatColor.DARK_PURPLE + "Donator", ChatColor.LIGHT_PURPLE + "[SrA " + ChatColor.DARK_GRAY + "+" + ChatColor.DARK_PURPLE + "Donator]"),
    SENIORDP("a " + ChatColor.LIGHT_PURPLE + "Senior Admin " + ChatColor.AQUA + "and " + ChatColor.LIGHT_PURPLE + "Donator+", ChatColor.LIGHT_PURPLE + "[SrA " + ChatColor.DARK_GRAY + "+" + ChatColor.LIGHT_PURPLE + "Donator+]"),
    EXECUTIVE("an " + ChatColor.DARK_RED + "Executive Senior Admin", ChatColor.DARK_RED + "[Exec]"),
    PERVERT("a " + ChatColor.LIGHT_PURPLE + "Pervert", ChatColor.LIGHT_PURPLE + "[Pervert]"),
    HO("a " + ChatColor.LIGHT_PURPLE + "Ho", ChatColor.LIGHT_PURPLE + "[Ho]"),
    BOI("the " + ChatColor.GOLD + "Dinner Warrior", ChatColor.GOLD + "[Boi]"),    
    OWNER("the " + ChatColor.BLUE + "Owner", ChatColor.BLUE + "[Owner]"),
    BISH("a " + ChatColor.GOLD + "Pimp", ChatColor.GOLD + "[Pimp]"),    
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
        // Handle console
        if (!(sender instanceof Player))
        {
            return fromSender(sender).getLoginMessage();
        }

        // Handle admins
        final TFM_Admin entry = TFM_AdminList.getEntry((Player) sender);
        if (entry == null)
        {
            // Player is not an admin
            return fromSender(sender).getLoginMessage();
        }

        // Custom login message
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
        if (TFM_DonatorList.isDonatorPlus(sender))
        {
            return DONATORPLUS;
        }       
        else if (TFM_DonatorList.isDonator(sender))
        {
            return DONATOR;
        }


        final TFM_Admin entry = TFM_AdminList.getEntry((Player) sender);
        final TFM_Donator dEntry = TFM_DonatorList.getEntry((Player) sender);

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
            if (sender.getName().equals("DeziThePenguin"))
            {
                return PERVERT;
            }
            if (sender.getName().equals("neroblackcat"))
            {
                return PERVERT;
            }
            if (sender.getName().equals("OliverDatGuy"))
            {
                return BISH;
            }
            if (entry.isSeniorAdmin() && !TFM_DonatorList.isDonator(sender))
            {
                rank = SENIOR;
            }
            else if (entry.isSeniorAdmin() && TFM_DonatorList.isDonator(sender))
            {
                rank = SENIORD;
            }
            else if (entry.isSeniorAdmin() && TFM_DonatorList.isDonatorPlus(sender))
            {
                rank = SENIORDP;
            }
            else if (entry.isTelnetAdmin() && !TFM_DonatorList.isDonator(sender))
            {
                rank = TELNET;
            }
            else if (entry.isTelnetAdmin() && TFM_DonatorList.isDonator(sender))
            {
                rank = TELNETD;
            }
            else if (entry.isTelnetAdmin() && TFM_DonatorList.isDonator(sender))
            {
                rank = TELNETDP;
            }
            else if (TFM_AdminList.isSuperAdmin(sender) && TFM_DonatorList.isDonator(sender))
            {
                rank = SUPERD;
            }
            else if (TFM_AdminList.isSuperAdmin(sender) && TFM_DonatorList.isDonatorPlus(sender))
            {
                rank = SUPERDP;
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
