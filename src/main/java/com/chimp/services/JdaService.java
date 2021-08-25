package com.chimp.services;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

/**
 * This class holds the {@link JDA} object for all other classes and handles it's all operations
 * (creating object, connecting, disconnecting, exception handling).
 */
public class JdaService extends ListenerAdapter {
    private JDA jda;
    private final JDABuilder jdaBuild;

    /**
     * Initiates the JDABuilder
     */
    public JdaService() {
        ContextService.getLogger().logInfo("Connecting...");
        // TODO LOAD TOKEN FROM FILE
        String TOKEN = "ODE5NTUwMTU3MTM3NTEwNDIx.YEoPjw.UZhG6THDOqYdyWuZmGiqDcL3_a0";
        jdaBuild = JDABuilder
                .createDefault(TOKEN)
                .addEventListeners(this);
    }

    /**
     * Creates the JDA object and attempts to connect to discord servers.
     */
    public void connect() {
        try {
            jda = jdaBuild.build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException le) {
            ContextService.getLogger().logError(le.getMessage(), "0");
        }
    }

    /**
     * Ends the connection and closes the application.
     */
    public void disconnect() {
        jda.shutdownNow();
        System.exit(0);
    }

    public JDA getJda() {
        return jda;
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
     * Handles any exceptions that may be thrown during log or execution.
     * @param event Event of received message, it is necessary for all methods to work.
     */
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        try {
            if (!event.isFromType(ChannelType.PRIVATE)) {
                ContextService.getInterpreter().handleMessage(event);
                ContextService.getLogger().logMessage(event);
            }
        } catch (Exception e) {
            ContextService.getLogger().logError("Cause: " + e.getCause() + ", " + e.getLocalizedMessage());
        }

    }

    @Override
    public void onDisconnect(@NotNull DisconnectEvent event) {
        super.onDisconnect(event);
        ContextService.getLogger().logError("Disconnected!");
    }

    @Override
    public void onReconnect(@NotNull ReconnectedEvent event) {
        super.onReconnect(event);
        ContextService.getLogger().logInfo("Reconnected successfully!");
    }

    @Override
    public void onResume(@NotNull ResumedEvent event) {
        super.onResume(event);
        ContextService.getLogger().logInfo("Resumed the connection!");
    }
}
