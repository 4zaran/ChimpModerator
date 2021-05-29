package com.chimp.services;

import com.chimp.commands.*;

import java.util.TreeMap;

public abstract class CommandSet {
    private static TreeMap<String, Command> commands;

    public static TreeMap<String, Command> getCommands(){
        commands = new TreeMap<>();

        commands.put("/ban", new CommandBan());
        commands.put("/exit", new CommandExit());
        commands.put("/help", new CommandHelp());
        commands.put("/kick", new CommandKick());
        commands.put("/purge", new CommandPurge());
        commands.put("/censor", new CommandCensor());
        commands.put("/uncensor", new CommandUncensor());
        commands.put("/censored", new CommandCensored());
        commands.put("/config", new CommandConfig());
        commands.put("/role", new CommandRole());
        commands.put("/roles", new CommandRoles());

        return commands;
    }
}
