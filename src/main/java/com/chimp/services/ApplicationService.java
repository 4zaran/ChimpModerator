package com.chimp.services;

import com.chimp.ApplicationMain;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import javax.security.auth.login.LoginException;

public class ApplicationService {
    private JDA jda;
    private final Logger logger;
    private final JDABuilder jdaBuild;

    public ApplicationService(Logger logger) {
        this.logger = logger;
        String TOKEN = "ODE5NTUwMTU3MTM3NTEwNDIx.YEoPjw.UZhG6THDOqYdyWuZmGiqDcL3_a0";
        jdaBuild = JDABuilder.createDefault(TOKEN)
                .addEventListeners(new ApplicationMain());
    }

    public void connect() {
        try {
            jda = jdaBuild.build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException le) {
            logger.logError(le.getMessage());
        }
    }

    public void disconnect() {
        jda.shutdownNow();
        System.exit(0);
    }

    public JDA getJda() {
        return jda;
    }

    public TextChannel getTextChannel(String name) {
        return jda.getTextChannelsByName(name, true).get(0);
    }
}
