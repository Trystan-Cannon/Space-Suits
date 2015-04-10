/*
 * The MIT License
 *
 * Copyright 2015 Trystan Cannon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.trystancannon.spacesuits.core;

import io.github.trystancannon.spacesuits.command.SpacesuitsBaseCommandExecutor;
import io.github.trystancannon.spacesuits.event.SpacesuitListener;
import io.github.trystancannon.spacesuits.file.Utils;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Implementation of the <code>JavaPlugin</code> for the Space Suits plugin.
 * 
 * @author Trystan Cannon
 */
public class SpacesuitsPlugin extends JavaPlugin implements Listener {
    
    /**
     * Contains all of the <code>SpacesuitListener</code>s for the plugin.
     * 
     * The key for the listeners are the UUID of the player for which they
     * listen.
     * 
     * Each listener is a runnable which repeats itself every tick, checking if its
     * <code>Player</code> is without their armor in a world that requires them
     * to do so.
     * 
     * If the player is without their armor, they receive 1/2 heart of damage for
     * every ten seconds they are without it.
     * 
     * However, players may bypass this damage if they have the permission node:
     * spacesuits.bypass.
     */
    private static final HashMap<UUID, SpacesuitListener> suitListeners = new HashMap<>();
    
    /**
     * Contains all of the <code>World</code>s in which players are required to
     * wear their space suits. In these worlds, if the player has not been wearing
     * their suit for long enough, they will be damaged.
     */
    private static final HashMap<World, Boolean> spaceWorlds = new HashMap<>();
    
    /**
     * Loads the configuration file and creates all of the <code>SpacesuitListener</code>s
     * for currently online players.
     * 
     * Registers the plugin for events.
     */
    @Override
    public void onEnable() {
        loadSpaceWorldConfig();
        getLogger().info("Loaded " + spaceWorlds.size() + " space worlds.");
        
        // Create the plugin's data folder if it doesn't already exist:
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        
        // Create listeners for all currently online players.
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            suitListeners.put(onlinePlayer.getUniqueId(), new SpacesuitListener(onlinePlayer, this));
            suitListeners.get(onlinePlayer.getUniqueId()).run();
        }
        
        // Register for events.
        getServer().getPluginManager().registerEvents(this, this);
        
        // Register commands with their executors.
        getCommand("spacesuits").setExecutor(new SpacesuitsBaseCommandExecutor(this));
    }
    
    /**
     * Saves the configuration file and empties the <code>suitListeners</code>
     * map, preventing players from being damaged after the plugin has been
     * disabled.
     */
    @Override
    public void onDisable() {
        // TODO: Save configuration file.
        
        // Remove all listners from the map.
        for (UUID listenerId : suitListeners.keySet()) {
            // Stop all of the listeners before removing them from the map.
            suitListeners.get(listenerId).stop();
            suitListeners.remove(listenerId);
        }
    }
    
    /**
     * Creates <code>SpacesuitListener</code> for each player upon joining
     * the server.
     * 
     * @param playerJoin 
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoin) {
        Player playerJoined = playerJoin.getPlayer();
        
        // Create and start the listener.
        suitListeners.put(playerJoined.getUniqueId(), new SpacesuitListener(playerJoined, this));
        suitListeners.get(playerJoined.getUniqueId()).run();
    }
    
    /**
     * Removes the <code>SpacesuitListener</code> for the player quitting.
     * 
     * @param playerQuit 
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuit) {
        UUID playerQuittingId = playerQuit.getPlayer().getUniqueId();
        
        // Stop the listener's activities before removing it from the map.
        suitListeners.get(playerQuittingId).stop();
        suitListeners.remove(playerQuittingId);
        
        getServer().broadcastMessage(playerQuit.getPlayer().getDisplayName() + " unregistered for space suit stuff.");
    }
    
    /**
     * Checks if the given world is registered as a space world, meaning that
     * players without armor can be damaged for not wearing any.
     * 
     * @param world World to check.
     * @return <code>true</code> if the given <code>World</code> is a valid space world.
     */
    public boolean isWorldSpaceWorld(World world) {
        return spaceWorlds.get(world) != null;
    }
    
    /**
     * Sets the given world to be a space world. If the world is already a space world,
     * the world is removed from the space worlds list.
     * 
     * @param world
     * 
     * @return <code>true</code> if the world is set to a space world; <code>false</code> if it is removed from the list.
     */
    public boolean toggleSpaceWorld(World world) {
        // World is already a space world:
        if (spaceWorlds.get(world) != null) {
            spaceWorlds.remove(world);
        // World is not currently a space world:
        } else {
            spaceWorlds.put(world, true);
        }

        updateSpaceWorldConfig();
        return spaceWorlds.get(world) != null;
    }
    
    /**
     * Sends the given message to the given receiver with a label that designates
     * the message as coming from the Spacuits plugin. The prefix looks like:
     * [Spacesuits] with dark gray brackets and white text.
     * 
     * @param receiver
     * @param message 
     */
    public static void sendLabeledMessage(CommandSender receiver, String message) {
        receiver.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.WHITE + "Spacesuits" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + message);
    }
    
    /**
     * Sends the given error message to the given receiver with a label that designates
     * the message as coming from the Spacesuits plugin. The prefix is that which
     * comes from <code>sendLabeledMessage</code> with the message in dark red.
     * 
     * @param receiver
     * @param errorMessage 
     */
    public static void sendLabeledError(CommandSender receiver, String errorMessage) {
        sendLabeledMessage(receiver, ChatColor.DARK_RED + errorMessage);
    }
    
    /**
     * Sends the given success message to the given receiver with a label that designates
     * the message as coming from the Spacesuits plugin. The prefix is that which
     * comes from <code>sendLabeledMessage</code> with the message in green.
     * 
     * @param receiver
     * @param successMessage 
     */
    public static void sendLabeledSucces(CommandSender receiver, String successMessage) {
        sendLabeledMessage(receiver, ChatColor.GREEN + successMessage);
    }
    
    /**
     * Updates the configuration file with all current space world <code>UUID</code>s.
     * 
     * @return <code>true</code> if the file was updated without failure.
     */
    private boolean updateSpaceWorldConfig() {
        List<String> worldIds = new ArrayList<>();
        
        for (World spaceWorld : spaceWorlds.keySet()) {
            worldIds.add(spaceWorld.getUID().toString());
        }
        
        return Utils.writeFile(getDataFolder() + "/config.txt", worldIds);
    }
    
    /**
     * Reads the configuration file, creating all current entries in the
     * <code>spaceWorlds</code> map.
     * 
     * @return <code>true</code> if the configuration file was loaded without failure.
     */
    private boolean loadSpaceWorldConfig() {
        List<String> lines = Utils.getFileContents(getDataFolder() + "/config.txt");
        
        // Failed to read the config:
        if (lines == null) {
            return false;
        }
        
        for (String line : lines) {
            World world = getServer().getWorld(UUID.fromString(line));
            
            if (world != null) {
                spaceWorlds.put(world, true);
            }
        }
        
        return true;
    }
    
}
