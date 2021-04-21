package com.chimp.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public interface Command {
    void execute(@Nonnull MessageReceivedEvent event, String[] parameters);
    @NotNull String getDescription();
}
