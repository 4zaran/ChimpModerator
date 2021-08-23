package com.chimp.services;

import com.chimp.window.WindowMain;
import com.chimp.window.WindowManager;

/**
 * Class containing static objects of the most important classes.
 * It is used to easily get those classes in any place.
 */
public class ContextService {
    private static String prefix;
    private static Logger logger;
    private static WindowMain window;
    private static WindowManager manager;
    private static JDAService appService;
    private static MessageInterpreter interpreter;

    public static void create(){
        prefix = "!";
        window = new WindowMain();
        logger = new Logger();
        appService = new JDAService();
        manager = new WindowManager();
        interpreter = new MessageInterpreter();
    }

    // GETTERS
    public static Logger getLogger() {
        return logger;
    }

    public static WindowMain getWindow() {
        return window;
    }

    public static WindowManager getManager() {
        return manager;
    }

    public static JDAService getAppService() {
        return appService;
    }

    public static MessageInterpreter getInterpreter() {
        return interpreter;
    }

    public static String getPrefix() {
        return prefix;
    }
}
