package com.chimp.services;

import com.chimp.window.WindowMain;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Logger {
    WindowMain window;
    public Logger(WindowMain window){
        this.window = window;
    }

    public void logMessage(@Nonnull MessageReceivedEvent event){
        String messageContent = event.getMessage().getContentRaw();
        window.printText(Objects.requireNonNull(event.getMember()).getEffectiveName() + ": " + messageContent);
    }

    public void logInfo(String info){
        window.printText("INFO: " + info);
    }
}
