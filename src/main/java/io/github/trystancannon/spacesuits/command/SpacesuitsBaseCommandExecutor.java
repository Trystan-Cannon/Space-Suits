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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handles the execution of the plugin's base command: /spacesuits.
 * 
 * All sub-commands (/spacesuits [sub-command] [args...]) are processed through
 * this executor.
 * 
 * @author Trystan Cannon
 */
public class SpacesuitsBaseCommandExecutor extends BaseCommandExecutor implements CommandExecutor {
    
    /**
     * The name of this command, invoked when the player calls the command from in-game.
     */
    public static final String COMMAND_NAME = "spacesuits";
    
    /**
     * The list of executors for each sub-command which is executed by this base executor.
     * 
     * For example, this would include anything following /spacesuits:
     *  - /spacesuits reload
     *  - /spacesuits add [world]
     *  - etc.
     */
    private final List<SubCommandExecutor> subCommandExecutors = new ArrayList<>();
    
    /**
     * Initializes the base executor with all of its sub commands, filling
     * the <code>subCommandExecutors</code> list.
     */
    public SpacesuitsBaseCommandExecutor(SpacesuitsPlugin plugin) {
        super(COMMAND_NAME, plugin);
        subCommandExecutors.add(new SetWorldCommandExecutor(SetWorldCommandExecutor.COMMAND_NAME, this));
    }
    
    /**
     * Handles the execution of the base /spacesuits command. Execution of sub
     * commands begins here, using the arguments to the base command as
     * the sub command.
     * 
     * @param sender
     * @param command
     * @param label
     * @param args
     * 
     * @return <code>true</code> if the base command was executed.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Make sure there is a sub command along with the base:
        if (command.getName().equalsIgnoreCase("spacesuits") && args.length > 0) {
            String subCommand = args[0];
            
            // Execute sub-commands if any are found.
            for (SubCommandExecutor subExecutor : subCommandExecutors) {
                if (subExecutor.getName().equalsIgnoreCase(subCommand)) {
                    subExecutor.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
            }
        }
        
        return false;
    }
    
}
