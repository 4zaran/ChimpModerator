package com.chimp;

import com.chimp.services.ContextService;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 * The main class of the application.
 * <ul>
 * Contains:
 * <li>{@link #main(String[]) main()} method to start application,
 * <li>{@link #onReady(ReadyEvent) onReady()} to delegate all behaviour when JDA successfully finished loading
 * <li>{@link #onMessageReceived(MessageReceivedEvent) onMessageReceived()} to delegate all behaviour when the message is received,
 * </ul>
 */
public class ApplicationMain extends ListenerAdapter {
    /**
     * The main method of the whole application.
     * It uses the {@link ContextService} class to set everything up.
     * @param args  Command line arguments. Currently, application does not use any arguments.
     * @see ContextService
     */
    public static void main(String[] args) {
        ContextService.create();
        ContextService.getManager().setupConsole();
        ContextService.getLogger().logInfo(ContextService.getInterpreter().getCommandCount());
        ContextService.getAppService().connect();
    }

    /**
     * Launched when JDA finished loading.
     * When this happens, {@link com.chimp.window.WindowManager#setupWindow() setupWindow()} is called to populate
     * all ComboBoxes with available Guilds and Channels.
     * @param event Currently, this parameter is not used.
     */
    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        ContextService.getLogger().logInfo("Connected!");
        ContextService.getManager().setupWindow();
    }

    /**
     * Passes the received message to two classes:
     * <ul>
     * <li>{@link com.chimp.services.Logger Logger} to log those messages in main window
     * <li>{@link com.chimp.services.MessageInterpreter MessageInterpreter} to check if received message is a command
     * or if it's violating any rules
     * </ul>
     * @param event Event of received message, it is necessary for all methods to work.
     */
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(!event.isFromType(ChannelType.PRIVATE)){
            ContextService.getInterpreter().handleMessage(event);
            ContextService.getLogger().logMessage(event);
        }
    }
}