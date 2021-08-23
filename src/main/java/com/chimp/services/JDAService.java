package com.chimp.services;

import com.chimp.ApplicationMain;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

/**
 * This class holds the {@link JDA} object for all other classes and handles it's all operations
 * (creating object, connecting, disconnecting, exception handling).
 */
public class JDAService {
    private JDA jda;
    private final JDABuilder jdaBuild;

    /**
     * Initiates the JDABuilder
     */
    public JDAService() {
        String TOKEN = "ODE5NTUwMTU3MTM3NTEwNDIx.YEoPjw.UZhG6THDOqYdyWuZmGiqDcL3_a0";
        jdaBuild = JDABuilder
                .createDefault(TOKEN)
                .addEventListeners(new ApplicationMain());
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
}
