package com.chimp.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class CommandExit implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, String[] parameters) {
        event.getMessage().delete().queue();
        event.getJDA().shutdownNow();
        System.exit(0);
    }

    @Override
    public @NotNull String getDescription() {
        return "Closes application";
    }
}
