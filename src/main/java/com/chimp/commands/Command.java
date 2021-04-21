package com.chimp.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

public interface Command {
    void execute(@Nonnull MessageReceivedEvent event);
    String getDescription();
}
