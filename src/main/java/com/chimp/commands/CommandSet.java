package com.chimp.commands;

import java.util.HashMap;

public abstract class CommandSet {
    private static HashMap<String, Command> commands;

    public static HashMap<String, Command> getCommands(){
        commands = new HashMap<>();

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

        return commands;
    }
}
