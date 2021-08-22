package com.chimp.services;

import com.chimp.commands.*;
import com.chimp.commands.syntax.Command;

import java.util.TreeMap;

/**
 * Holds commands.
 * All commands that should be available must be added here.
 */
public abstract class CommandSet {

    /**
     *
     * @return map containing all commands with their names as keys
     */
    public static TreeMap<String, Command> getCommands(){
        TreeMap<String, Command> commands = new TreeMap<>();

        commands.put("ban", new CommandBan());
        commands.put("censored", new CommandCensored());
        commands.put("config", new CommandConfig());
        commands.put("exit", new CommandExit());
        commands.put("help", new CommandHelp());
        commands.put("kick", new CommandKick());
        commands.put("purge", new CommandPurge());

        return commands;
    }
}
