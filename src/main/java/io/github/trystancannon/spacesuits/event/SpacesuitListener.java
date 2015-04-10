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
package io.github.trystancannon.spacesuits.event;

import io.github.trystancannon.spacesuits.core.SpacesuitsPlugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EntityEquipment;

/**
 * A <code>SpacesuitListener</code> is a <code>Runnable</code> which checks every
 * tick as to whether or not the player is wearing their armor in a world which
 * requires it.
 * 
 * In the case that the <code>Player</code> does not have their armor on, they will receive
 * 1/2 heart of damage every 10 seconds.
 * 
 * However, a <code>Player</code> may bypass this damage with the permission node:
 * spacesuits.bypass.
 * 
 * @author Trystan Cannon
 */
public final class SpacesuitListener implements Listener, Runnable {
    
    /**
     * The number of server ticks between each time the player will receive
     * damage for not wearing their space suit.
     */
    public static final long DAMAGE_INTERVAL = 200L;
    
    /**
     * The <code>Player</code> for which this <code>SpacesuitListener</code> is
     * listening.
     */
    private final Player astronaut;
    
    /**
     * The plugin instance with which this listener will schedule its checks.
     */
    private final SpacesuitsPlugin plugin;
    
    /**
     * The number of server ticks for which this listener's <code>astronaut</code>
     * has not been wearing their armor (space suit).
     */
    private long ticksWithoutSuit = 0;
    
    /**
     * The task ID given to the listener when it schedules itself using the
     * server's current <code>BukkitScheduler</code> object. This is used
     * to cancel the listener's activities if necessary.
     * 
     * -1 if the listener has not been scheduled.
     */
    private int taskId = -1;
    
    public SpacesuitListener(Player player, SpacesuitsPlugin plugin) {
        this.astronaut = player;
        this.plugin = plugin;
    }
    
    /**
     * @return The current task ID of the listener's scheduled checking task (<code>run</code>).
     */
    public int getTaskId() {
        return taskId;
    }
    
    /**
     * Cancels the listener's currently scheduled task. Does nothing if there
     * is no currently scheduled task.
     */
    public void stop() {
        if (taskId != -1) {
            plugin.getServer().getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }
    
    /**
     * Runs every tick to check if the <code>astronaut</code> is wearing their
     * armor (space suit) in world in which it is required. If they are not, the
     * player will receive 1/2 heart of damage every <code>DAMAGE_INTERVAL</code> server ticks.
     * 
     * However, the player will bypass this damage if they have the permission node:
     * spacesuits.bypass.
     */
    @Override
    public void run() {
        // Player can be damaged for not wearing their suit and is a world in which they can be damaged for it:
        if (!astronaut.hasPermission("spacesuits.bypass") && plugin.isWorldSpaceWorld(astronaut.getWorld())) {
            EntityEquipment playerEquipment = astronaut.getEquipment();
            
            // Player isn't wearing a component of their suit:
            if (playerEquipment.getHelmet() == null || playerEquipment.getChestplate() == null || playerEquipment.getLeggings() == null || playerEquipment.getBoots() == null) {
                ticksWithoutSuit++;
                    
                // Player has been without the suit long enough to sustain damage:
                if (ticksWithoutSuit % DAMAGE_INTERVAL == 0) {
                    // Damage the player.
                    SpacesuitsPlugin.sendLabeledMessage(astronaut, ChatColor.ITALIC.toString() + ChatColor.RED + "You are without your space suit! Be careful!");
                    astronaut.damage(0.5);
                }
            // Player is wearing their suit:
            } else {
                ticksWithoutSuit = 0;
            }
        }
        
        // Reschedule the check.
        taskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 1L);
    }
    
}
