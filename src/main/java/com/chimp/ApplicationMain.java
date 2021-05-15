package com.chimp;

import com.chimp.services.ApplicationService;
import com.chimp.services.Logger;
import com.chimp.services.MessageInterpreter;
import com.chimp.window.WindowMain;
import com.chimp.window.WindowManager;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ApplicationMain extends ListenerAdapter {
    private static Logger logger;
    private static MessageInterpreter interpreter;
    private static WindowMain window;

    public static void main(String[] args) {
        window = new WindowMain();
        logger = new Logger(window);

        interpreter = new MessageInterpreter();
        logger.logInfo(interpreter.getCommandCount());

        ApplicationService jda = new ApplicationService(logger);
        jda.connect();

        WindowManager manager = new WindowManager(jda, window);
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        logger.logInfo("Connected!");
        window.sendButton.setEnabled(true);
        window.messageTextField.setEnabled(true);
        window.messageTextField.setText("");
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        interpreter.handleMessage(event);
        logger.logMessage(event);
    }
}