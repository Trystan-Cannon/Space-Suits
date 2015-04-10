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
import org.bukkit.command.CommandExecutor;

/**
 * A <code>BaseCommandExecutor</code> executes using <code>CommandExecutor</code>'s
 * <code>onCommand</code> method, invoking sub commands which are passed to it
 * via the command's arguments.
 * 
 * @author Trystan Cannon
 */
public abstract class BaseCommandExecutor implements CommandExecutor {
    
    /**
     * The plugin instance with which this command executor is registered.
     */
    private final SpacesuitsPlugin plugin;
    
    /**
     * The name of this command. Used when calling this command from the game.
     */
    private final String name;
    
    public BaseCommandExecutor(String name, SpacesuitsPlugin plugin) {
        this.plugin = plugin;
        this.name = name;
    }
    
    /**
     * @return The plugin with which this command is registered.
     */
    public SpacesuitsPlugin getPlugin() {
        return plugin;
    }
    
}
