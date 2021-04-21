package com.chimp.commands;

import java.util.HashMap;

public abstract class CommandSet {
    private static HashMap<String, Command> commands;

    public static HashMap<String, Command> getCommands(){
        commands = new HashMap<>();

        commands.put("/kick", new CommandKick());
        commands.put("/ban", new CommandBan());

        return commands;
    }
}
