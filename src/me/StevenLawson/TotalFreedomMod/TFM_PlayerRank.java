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
    IMPOSTORD("an " + ChatColor.YELLOW + ChatColor.UNDERLINE + "Impostor", ChatColor.YELLOW.toString() + ChatColor.UNDERLINE + "[IMP]"),
    NON_OP("a " + ChatColor.GREEN + "Non-OP", ChatColor.GREEN.toString()),
    OP("an " + ChatColor.RED + "OP", ChatColor.RED + "[OP]"),
    DONATOR("a " + ChatColor.DARK_PURPLE + "Donator", ChatColor.DARK_PURPLE + "[Donator]"),
    DONATORPLUS("a " + ChatColor.LIGHT_PURPLE + "Donator+", ChatColor.LIGHT_PURPLE + "[Donator+]"),
    SUPER("a " + ChatColor.AQUA + "Super Admin", ChatColor.AQUA + "[SA]"),
    SUPERD("a " + ChatColor.AQUA + "Super Admin " + ChatColor.AQUA + "and " + ChatColor.DARK_PURPLE + "Donator", ChatColor.AQUA + "[SA/D]"),
    SUPERDP("a " + ChatColor.AQUA + "Super Admin " + ChatColor.AQUA + "and " + ChatColor.LIGHT_PURPLE + "Donator+", ChatColor.AQUA + "[SA/Donator+]"),    
    TELNET("a " + ChatColor.DARK_GREEN + "Super Telnet Admin", ChatColor.DARK_GREEN + "[STA]"),
    TELNETD("a " + ChatColor.DARK_GREEN + "Super Telnet Admin " + ChatColor.AQUA + "and " + ChatColor.DARK_PURPLE + "Donator", ChatColor.DARK_GREEN + "[STA/Donator]"),
    TELNETDP("a " + ChatColor.DARK_GREEN + "Super Telnet Admin " + ChatColor.AQUA + "and " + ChatColor.LIGHT_PURPLE + "Donator+", ChatColor.DARK_GREEN + "[STA/Donator+]"),
    SENIOR("a " + ChatColor.GOLD + "Senior Admin", ChatColor.GOLD + "[SrA]"),
    SENIORD("a " + ChatColor.GOLD + "Senior Admin " + ChatColor.AQUA + "and " + ChatColor.DARK_PURPLE + "Donator", ChatColor.GOLD + "[SrA/Donator]"),
    SENIORDP("a " + ChatColor.GOLD + "Senior Admin " + ChatColor.AQUA + "and " + ChatColor.LIGHT_PURPLE + "Donator+", ChatColor.GOLD + "[SrA/Donator+]"),
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
        if (TFM_DonatorList.isDonatorImpostor((Player) sender))
        {
            return IMPOSTOR;
        }
        if (DEVELOPERS.contains(sender.getName()))
        {
            return DEVELOPER;
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
            if (TFM_UuidManager.getUniqueId(sender.getName()).toString().equals("4ebd76e2-1fdf-403d-8a72-5d517a6876f8"))
            {
                return PERVERT;
            }
            if (TFM_UuidManager.getUniqueId(sender.getName()).toString().equals("55c32d2c-1b9e-4aa1-9507-4c3078e70890"))
            {
                return PERVERT;
            }
            if (TFM_UuidManager.getUniqueId(sender.getName()).toString().equals("5330c607-19eb-42f5-b762-8764197a0047"))
            {
                return BISH;
            }
            if (entry.isSeniorAdmin() && !TFM_DonatorList.isDonator(sender))
            {
                rank = SENIOR;
            }
            else if (entry.isSeniorAdmin() && TFM_DonatorList.isDonatorPlus(sender))
            {
                rank = SENIORDP;
            }            
            else if (entry.isSeniorAdmin() && TFM_DonatorList.isDonator(sender))
            {
                rank = SENIORD;
            }
            else if (entry.isTelnetAdmin() && !TFM_DonatorList.isDonator(sender))
            {
                rank = TELNET;
            }
            else if (entry.isTelnetAdmin() && TFM_DonatorList.isDonator(sender))
            {
                rank = TELNETDP;
            }            
            else if (entry.isTelnetAdmin() && TFM_DonatorList.isDonator(sender))
            {
                rank = TELNETD;
            }
            else if (TFM_AdminList.isSuperAdmin(sender) && TFM_DonatorList.isDonatorPlus(sender))
            {
                rank = SUPERDP;
            }            
            else if (TFM_AdminList.isSuperAdmin(sender) && TFM_DonatorList.isDonator(sender))
            {
                rank = SUPERD;
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
