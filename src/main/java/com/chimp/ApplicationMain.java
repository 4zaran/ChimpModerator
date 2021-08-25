package com.chimp;

import com.chimp.services.ContextService;

/**
 * The main class of the application.
 * <ul>
 * Contains:
 * <li>{@link #main(String[]) main()} method to start application,
 * </ul>
 */
public class ApplicationMain {
    /**
     * The main method of the whole application.
     * Executes the init() method.
     * @param args  Command line arguments. Currently, application does not use any arguments.
     */
    public static void main(String[] args) {
        init();
    }

    /**
     * It uses the {@link ContextService} class to set everything up.
     * @see ContextService
     */
    private static void init() {
        try {
            ContextService.create();
            ContextService.getManager().setupConsole();
            ContextService.getLogger().logInfo(ContextService.getInterpreter().getCommandCount());
            ContextService.getJdaService().connect();
        }
        catch (Exception e){
            ContextService.getLogger().logError("Cause: " + e.getCause() + ", " + e.getLocalizedMessage());
        }
    }
}