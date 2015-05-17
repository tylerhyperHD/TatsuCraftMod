package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Util;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommandPermissions(level = AdminLevel.ALL, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "I hate doing stuff I'm dared to do... ~_~", usage = "/<command> [playername] [playername]")
public class Command_ship extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 2)
        {
            playerMsg(ChatColor.RED + "Invalid player.");            
            return true;
            
        }
      Player shipperProbablyMiddie = (Player)sender;
      Player p1 = getPlayer(args[0]);
      Player p2 = getPlayer(args[1]);

      if ((p1 == null) || (!p1.isOnline()) || (p2 == null) || (!p2.isOnline())) {
        playerMsg(ChatColor.RED + "Invalid player.");
        return true;
      }
      
      if (p1 == p2)
      {
        sender.sendMessage(ChatColor.RED + "For some reason unknown, you just tried to ship yourself. Seriously? I don't believe it! I" + ChatColor.ITALIC + " really don't" + ChatColor.RESET + ChatColor.RED + "! Why you did, we'll never know, but one thing we do know is that you love yourself so much and that you're pretty stupid. Here, have a cookie.");
            final ItemStack heldItem = new ItemStack(Material.COOKIE);
            final ItemMeta heldItemMeta = heldItem.getItemMeta();
            heldItemMeta.setDisplayName((new StringBuilder()).append(ChatColor.WHITE).append("Idiot of ").append(ChatColor.DARK_GRAY).append("the week award").toString());
            heldItem.setItemMeta(heldItemMeta);
            final int firstEmpty = shipperProbablyMiddie.getInventory().firstEmpty();
            shipperProbablyMiddie.getInventory().setItem(firstEmpty, heldItem);
        return true;
      }
      
      if ((p1.getName().equals("ImALuckyGuy")) && (p2.getName().equals("neroblackcat")))
      {
        sender.sendMessage(ChatColor.RED + "In your dreams. Have a cookie for trying though.");
        final ItemStack heldItem = new ItemStack(Material.COOKIE);
        final ItemMeta heldItemMeta = heldItem.getItemMeta();
        heldItemMeta.setDisplayName((new StringBuilder()).append(ChatColor.RED).append("Cookie for trying.").append(ChatColor.LIGHT_PURPLE).append(" <3").toString());
        heldItem.setItemMeta(heldItemMeta);
        final int firstEmpty = shipperProbablyMiddie.getInventory().firstEmpty();
        shipperProbablyMiddie.getInventory().setItem(firstEmpty, heldItem);
        return true;
      }
      if ((p1.getName().equals("neroblackcat")) && (p2.getName().equals("ImALuckyGuy")))
      {
        sender.sendMessage(ChatColor.RED + "In your dreams. Have a cookie for trying though.");
        final ItemStack heldItem = new ItemStack(Material.COOKIE);
        final ItemMeta heldItemMeta = heldItem.getItemMeta();
        heldItemMeta.setDisplayName((new StringBuilder()).append(ChatColor.RED).append("Cookie for trying.").append(ChatColor.LIGHT_PURPLE).append(" <3").toString());
        heldItem.setItemMeta(heldItemMeta);
        final int firstEmpty = shipperProbablyMiddie.getInventory().firstEmpty();
        shipperProbablyMiddie.getInventory().setItem(firstEmpty, heldItem);
        return true;
      }
      else
      TFM_Util.bcastMsg(ChatColor.GREEN + "" + shipperProbablyMiddie.getName() + " ships " + p1.getName() + " x " + p2.getName() + "." + ChatColor.LIGHT_PURPLE + " <3");
          
      return true;
    }
    
}