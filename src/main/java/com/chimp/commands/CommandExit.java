package com.chimp.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.TreeMap;

public class CommandExit implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        event.getMessage().delete().complete();
        event.getJDA().shutdown();
        System.exit(0);
    }

    @Override
    public @NotNull String getDescription() {
        return "Closes application";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/exit", "Closes application");
        return commandsWithDescriptions;
    }
}
