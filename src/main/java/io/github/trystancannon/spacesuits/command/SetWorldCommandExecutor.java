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
package io.github.trystancannon.spacesuits.command;

import io.github.trystancannon.spacesuits.core.SpacesuitsPlugin;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the execution of the command /spacesuits setworld, which sets
 * the current world that the player is in to a space world, meaning that players
 * can be damaged for not wearing their suits.
 * 
 * @author Trystan Cannon
 */
public class SetWorldCommandExecutor extends SubCommandExecutor {

    /**
     * The name for this sub-command, used to execute the command by typing it
     * as /spacesuits [name] [args...].
     */
    public static final String COMMAND_NAME = "setworld";
    
    public SetWorldCommandExecutor(String name, BaseCommandExecutor baseExecutor) {
        super(name, baseExecutor);
    }

    /**
     * Sets the world of the sender to a space world if it currently isn't, or,
     * if it is a space world, sets it to being a normal world.
     * 
     * If a world is a space world, players can be damaged for not wearing a
     * full suit of armor (space suit).
     * 
     * This method requires that the sender must be a <code>Player</code>.
     * 
     * @param sender
     * @param args
     * 
     * @return <code>true</code> if the command executes properly.
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            SpacesuitsPlugin.sendLabeledError(sender, "You must be a player to use " + COMMAND_NAME + ".");
            return false;
        }
        
        World playerWorld = ((Player) sender).getWorld();
        String successMessage = getBaseExecutor().getPlugin().toggleSpaceWorld(playerWorld) ? playerWorld.getName() + " is now a space world." : playerWorld.getName() + " is no longer a space world.";
        
        SpacesuitsPlugin.sendLabeledSucces(sender, successMessage);
        return true;
    }
    
}
