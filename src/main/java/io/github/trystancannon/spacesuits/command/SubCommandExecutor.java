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

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Abstract implementation of commands which are executed from a base
 * <code>CommandExecutor</code>. The sub-command is aware of its name
 * and base executor.
 * 
 * @author Trystan Cannon
 */
public abstract class SubCommandExecutor {
    
    /**
     * Name of this sub-command. This is the word that users type in to
     * execute this command.
     */
    private final String name;
    
    /**
     * The base <code>CommandExecutor</code> from which this sub-command should
     * be called.
     */
    private final BaseCommandExecutor baseExecutor;
    
    public SubCommandExecutor(String name, BaseCommandExecutor baseExecutor) {
        this.name = name;
        this.baseExecutor = baseExecutor;
    }
    
    /**
     * @return The name of the command which invokes this <code>SubCommandExecutor</code>.
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return The base <code>CommandExecutor</code> for this <code>SubCommandExecutor</code>.
     */
    public BaseCommandExecutor getBaseExecutor() {
        return baseExecutor;
    }
    
    /**
     * Executes this sub-command. No <code>Command</code> parameter is needed because
     * this method should ONLY be called when one is sure the <code>CommandSender</code>
     * intends it be executed.
     * 
     * @param sender
     * @param args
     * 
     * @return <code>true</code> if the command executed properly.
     */
    public abstract boolean execute(CommandSender sender, String[] args);
    
}
