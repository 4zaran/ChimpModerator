package com.chimp.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

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
    public @NotNull HashMap<String, String> getSyntax() {
        HashMap<String, String> commandsWithDescriptions= new HashMap<>();
        commandsWithDescriptions.put("/exit", "Closes application");
        return commandsWithDescriptions;
    }
}
