package me.StevenLawson.TotalFreedomMod;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import me.StevenLawson.TotalFreedomMod.Config.TFM_Config;
import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import me.StevenLawson.TotalFreedomMod.Config.TFM_MainConfig;
import me.StevenLawson.TotalFreedomMod.World.TFM_DonatorWorld;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class TFM_DonatorList
{
    private static final Map<UUID, TFM_Donator> donatorList;
    private static final Set<UUID> donatorUUIDs;
    private static final Set<UUID> donatorPlusUUIDs;
    private static final Set<String> superIps;
    private static int cleanThreshold = 24 * 7; // 1 Week in hours

    static
    {
        donatorList = new HashMap<UUID, TFM_Donator>();
        donatorUUIDs = new HashSet<UUID>();
        donatorPlusUUIDs = new HashSet<UUID>();
        superIps = new HashSet<String>();
    }

    private TFM_DonatorList()
    {
        throw new AssertionError();
    }

    public static Set<UUID> getdonatorUUIDs()
    {
        return Collections.unmodifiableSet(donatorUUIDs);
    }

    public static Set<UUID> getdonatorPlusUUIDs()
    {
        return Collections.unmodifiableSet(donatorPlusUUIDs);
    }

    public static Set<String> getDonatorIps()
    {
        return Collections.unmodifiableSet(superIps);
    }

    public static Set<String> getDonatorNames()
    {
        final Set<String> names = new HashSet<String>();

        for (TFM_Donator donator : donatorList.values())
        {
            if (!donator.isActivated())
            {
                continue;
            }

            names.add(donator.getLastLoginName());
        }

        return Collections.unmodifiableSet(names);
    }

    public static Set<String> getLowerDonatorNames()
    {
        final Set<String> names = new HashSet<String>();

        for (TFM_Donator donator : donatorList.values())
        {
            if (!donator.isActivated())
            {
                continue;
            }

            names.add(donator.getLastLoginName().toLowerCase());
        }

        return Collections.unmodifiableSet(names);
    }

    public static Set<TFM_Donator> getAllDonators()
    {
        return Sets.newHashSet(donatorList.values());
    }

    public static void setUuid(TFM_Donator donator, UUID oldUuid, UUID newUuid)
    {
        if (!donatorList.containsKey(oldUuid))
        {
            TFM_Log.warning("Could not set new UUID for donator " + donator.getLastLoginName() + ", donator is not loaded!");
            return;
        }

        if (oldUuid.equals(newUuid))
        {
            TFM_Log.warning("could not set new UUID for donator " + donator.getLastLoginName() + ", UUIDs match.");
            return;
        }

        final TFM_Donator newDonator = new TFM_Donator(
                newUuid,
                donator.getLastLoginName(),
                donator.getLastLogin(),
                donator.getCustomLoginMessage(),
                donator.isDonatorPlus(),
                donator.isActivated());

        newDonator.addIps(donator.getIps());

        donatorList.remove(oldUuid);
        donatorList.put(newUuid, newDonator);

        final TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, TotalFreedomMod.DONATOR_FILENAME, true);

        config.load();
        config.set("donators." + oldUuid.toString(), null);
        config.save();

        save(newDonator);
    }

    public static void load()
    {
        donatorList.clear();

        final TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, TotalFreedomMod.DONATOR_FILENAME, true);
        config.load();

        cleanThreshold = config.getInt("clean_threshold_hours", cleanThreshold);

        // Parse old donators
        if (config.isConfigurationSection("donators"))
        {
            parseOldConfig(config);
        }

        if (!config.isConfigurationSection("donators"))
        {
            TFM_Log.warning("Missing donators section in donator.yml.");
            return;
        }

        final ConfigurationSection section = config.getConfigurationSection("donators");

        for (String uuidString : section.getKeys(false))
        {
            if (!TFM_UuidManager.isUniqueId(uuidString))
            {
                TFM_Log.warning("Invalid Unique ID: " + uuidString + " in donator.yml, ignoring");
                continue;
            }

            final UUID uuid = UUID.fromString(uuidString);

            final TFM_Donator donator = new TFM_Donator(uuid, section.getConfigurationSection(uuidString));
            donatorList.put(uuid, donator);
        }

        updateIndexLists();

        TFM_Log.info("Loaded " + donatorList.size() + " donators (" + donatorUUIDs.size() + " active) and " + superIps.size() + " IPs.");
    }

    public static void updateIndexLists()
    {
        donatorUUIDs.clear();
        donatorPlusUUIDs.clear();
        superIps.clear();

        for (TFM_Donator donator : donatorList.values())
        {
            if (!donator.isActivated())
            {
                continue;
            }

            final UUID uuid = donator.getUniqueId();

            donatorUUIDs.add(uuid);

            for (String ip : donator.getIps())
            {
                superIps.add(ip);
            }


            if (donator.isDonatorPlus())
            {
                donatorPlusUUIDs.add(uuid);

            }
        }

        TFM_DonatorWorld.getInstance().wipeAccessCache();
    }

    private static void parseOldConfig(TFM_Config config)
    {
        TFM_Log.info("Old donator configuration found, parsing...");

        final ConfigurationSection section = config.getConfigurationSection("donators");

        int counter = 0;
        int errors = 0;

        for (String donator : config.getConfigurationSection("donators").getKeys(false))
        {
            final UUID uuid = TFM_UuidManager.getUniqueId(donator);

            if (uuid == null)
            {
                errors++;
                TFM_Log.warning("Could not convert donator " + donator + ", UUID could not be found!");
                continue;
            }

            config.set("donators." + uuid + ".last_login_name", uuid);
            config.set("donators." + uuid + ".is_activated", section.getBoolean(donator + ".is_activated"));
            config.set("donators." + uuid + ".is_donator_plus", section.getBoolean(donator + ".is_donator_plus"));
            config.set("donators." + uuid + ".last_login", section.getString(donator + ".last_login"));
            config.set("donators." + uuid + ".custom_login_message", section.getString(donator + ".custom_login_message"));
            config.set("donators." + uuid + ".ips", section.getStringList(donator + ".ips"));

            counter++;
        }

        config.set("donators", null);
        config.save();

        TFM_Log.info("Done! " + counter + " donators parsed, " + errors + " errors");
    }

    public static void saveAll()
    {
        final TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, TotalFreedomMod.DONATOR_FILENAME, true);
        config.load();

        config.set("clean_threshold_hours", cleanThreshold);

        final Iterator<Entry<UUID, TFM_Donator>> it = donatorList.entrySet().iterator();
        while (it.hasNext())
        {
            final Entry<UUID, TFM_Donator> pair = it.next();

            final UUID uuid = pair.getKey();
            final TFM_Donator donator = pair.getValue();

            config.set("donators." + uuid + ".last_login_name", donator.getLastLoginName());
            config.set("donators." + uuid + ".is_activated", donator.isActivated());
            config.set("donators." + uuid + ".is_donator_plus", donator.isDonatorPlus());
            config.set("donators." + uuid + ".last_login", TFM_Util.dateToString(donator.getLastLogin()));
            config.set("donators." + uuid + ".custom_login_message", donator.getCustomLoginMessage());
            config.set("donators." + uuid + ".ips", TFM_Util.removeDuplicates(donator.getIps()));
        }

        config.save();
    }

    public static void save(TFM_Donator donator)
    {
        if (!donatorList.containsValue(donator))
        {
            TFM_Log.warning("Could not save donator " + donator.getLastLoginName() + ", donator is not loaded!");
            return;
        }

        final TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, TotalFreedomMod.DONATOR_FILENAME, true);
        config.load();

        final UUID uuid = donator.getUniqueId();

        config.set("donators." + uuid + ".last_login_name", donator.getLastLoginName());
        config.set("donators." + uuid + ".is_activated", donator.isActivated());
        config.set("donators." + uuid + ".is_donator_plus", donator.isDonatorPlus());
        config.set("donators." + uuid + ".last_login", TFM_Util.dateToString(donator.getLastLogin()));
        config.set("donators." + uuid + ".custom_login_message", donator.getCustomLoginMessage());
        config.set("donators." + uuid + ".ips", TFM_Util.removeDuplicates(donator.getIps()));

        config.save();
    }

    public static TFM_Donator getEntry(Player player)
    {
        return getEntry(TFM_UuidManager.getUniqueId(player));
    }

    public static TFM_Donator getEntry(UUID uuid)
    {
        return donatorList.get(uuid);
    }

    @Deprecated
    public static TFM_Donator getEntry(String name)
    {
        for (UUID uuid : donatorList.keySet())
        {
            if (donatorList.get(uuid).getLastLoginName().equalsIgnoreCase(name))
            {
                return donatorList.get(uuid);
            }
        }
        return null;
    }

    public static TFM_Donator getEntryByIp(String ip)
    {
        return getEntryByIp(ip, false);
    }

    public static TFM_Donator getEntryByIp(String needleIp, boolean fuzzy)
    {
        Iterator<Entry<UUID, TFM_Donator>> it = donatorList.entrySet().iterator();
        while (it.hasNext())
        {
            final Entry<UUID, TFM_Donator> pair = it.next();
            final TFM_Donator donator = pair.getValue();

            if (fuzzy)
            {
                for (String haystackIp : donator.getIps())
                {
                    if (TFM_Util.fuzzyIpMatch(needleIp, haystackIp, 3))
                    {
                        return donator;
                    }
                }
            }
            else
            {
                if (donator.getIps().contains(needleIp))
                {
                    return donator;
                }
            }
        }
        return null;
    }

    public static void updateLastLogin(Player player)
    {
        final TFM_Donator donator = getEntry(player);
        if (donator == null)
        {
            return;
        }
        donator.setLastLogin(new Date());
        donator.setLastLoginName(player.getName());
        saveAll();
    }

    public static boolean isDonatorPlus(CommandSender sender)
    {
        return isDonatorPlus(sender, false);
    }

    public static boolean isDonatorPlus(CommandSender sender, boolean verifydonator)
    {
        if (verifydonator)
        {
            if (!isDonator(sender))
            {
                return false;
            }
        }

        final TFM_Donator entry = getEntry((Player) sender);
        if (entry != null)
        {
            return entry.isDonatorPlus();
        }

        return false;
    }

    public static boolean isDonator(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return true;
        }

        final Player player = (Player) sender;

        if (superIps.contains(TFM_Util.getIp(player)))
        {
            return true;
        }

        if (Bukkit.getOnlineMode() && donatorUUIDs.contains(TFM_UuidManager.getUniqueId(player)))
        {
            return true;
        }

        return false;
    }

    public static boolean isIdentityMatched(Player player)
    {
        if (!isDonator(player))
        {
            return false;
        }

        if (Bukkit.getOnlineMode())
        {
            return true;
        }

        final TFM_Donator entry = getEntry(player);
        if (entry == null)
        {
            return false;
        }

        return entry.getUniqueId().equals(TFM_UuidManager.getUniqueId(player));
    }

    @Deprecated
    public static boolean checkPartialdonatorIp(String ip, String name)
    {
        ip = ip.trim();

        if (superIps.contains(ip))
        {
            return true;
        }

        try
        {
            String matchIp = null;
            for (String testIp : superIps)
            {
                if (TFM_Util.fuzzyIpMatch(ip, testIp, 3))
                {
                    matchIp = testIp;
                    break;
                }
            }

            if (matchIp != null)
            {
                final TFM_Donator entry = getEntryByIp(matchIp);

                if (entry == null)
                {
                    return true;
                }

                if (entry.getLastLoginName().equalsIgnoreCase(name))
                {
                    if (!entry.getIps().contains(ip))
                    {
                        entry.addIp(ip);
                    }
                    saveAll();
                }
                return true;

            }
        }
        catch (Exception ex)
        {
            TFM_Log.severe(ex);
        }

        return false;
    }

    public static boolean isdonatorImpostor(Player player)
    {
        if (donatorUUIDs.contains(TFM_UuidManager.getUniqueId(player)))
        {
            return !isDonator(player);
        }

        return false;
    }

    public static void adddonator(OfflinePlayer player)
    {
        final UUID uuid = TFM_UuidManager.getUniqueId(player);
        final String ip = TFM_Util.getIp(player);
        final boolean canSuperIp = !TFM_MainConfig.getList(TFM_ConfigEntry.NOADMIN_IPS).contains(ip);

        if (donatorList.containsKey(uuid))
        {
            final TFM_Donator donator = donatorList.get(uuid);
            donator.setActivated(true);

            if (player.isOnline())
            {
                donator.setLastLogin(new Date());

                if (ip != null && canSuperIp)
                {
                    donator.addIp(ip);
                }
            }

            saveAll();
            updateIndexLists();
            return;
        }

        if (ip == null)
        {
            TFM_Log.severe("Could not add donator: " + TFM_Util.formatPlayer(player));
            TFM_Log.severe("Could not retrieve IP!");
            return;
        }

        if (!canSuperIp)
        {
            TFM_Log.warning("Could not add donator: " + TFM_Util.formatPlayer(player));
            TFM_Log.warning("IP " + ip + " may not be supered.");
            return;
        }

        final TFM_Donator donator = new TFM_Donator(
                uuid,
                player.getName(),
                new Date(),
                "",
                false,
                true);
        donator.addIp(ip);

        donatorList.put(uuid, donator);

        saveAll();
        updateIndexLists();
    }

    public static void removedonator(OfflinePlayer player)
    {
        final UUID uuid = TFM_UuidManager.getUniqueId(player);

        if (!donatorList.containsKey(uuid))
        {
            TFM_Log.warning("Could not remove donator: " + TFM_Util.formatPlayer(player));
            TFM_Log.warning("Player is not an donator!");
            return;
        }

        final TFM_Donator donator = donatorList.get(uuid);
        donator.setActivated(false);

        saveAll();
        updateIndexLists();
    }

    public static void cleandonatorList(boolean verbose)
    {
        Iterator<Entry<UUID, TFM_Donator>> it = donatorList.entrySet().iterator();
        while (it.hasNext())
        {
            final Entry<UUID, TFM_Donator> pair = it.next();
            final TFM_Donator donator = pair.getValue();

            if (!donator.isActivated() || donator.isDonatorPlus())
            {
                continue;
            }

            final Date lastLogin = donator.getLastLogin();
            final long lastLoginHours = TimeUnit.HOURS.convert(new Date().getTime() - lastLogin.getTime(), TimeUnit.MILLISECONDS);

            if (lastLoginHours > cleanThreshold)
            {
                if (verbose)
                {
                    TFM_Util.adminAction("TotalFreedomMod", "Deactivating donator " + donator.getLastLoginName() + ", inactive for " + lastLoginHours + " hours.", true);
                }

                donator.setActivated(false);
                TFM_TwitterHandler.delTwitter(donator.getLastLoginName());
            }
        }

        saveAll();
        updateIndexLists();
    }
}
