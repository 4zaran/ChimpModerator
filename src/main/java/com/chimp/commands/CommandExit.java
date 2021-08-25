package com.chimp.commands;

import com.chimp.services.ContextService;
import com.chimp.services.syntax.Command;
import com.chimp.services.syntax.CommandWrapper;

public class CommandExit extends Command {

    @Override
    public String getDescription() {
        return "Closes application";
    }

    @Override
    public void execute(CommandWrapper wrapper) throws Exception {
        wrapper.assignOptions();

        if(wrapper.hasUnusedValues())
            reportError("Too many parameters!", wrapper);

        if(wrapper.isMessage())
            wrapper.getEvent().getMessage().delete().complete();
        ContextService.getJdaService().getJda().shutdown();
        System.exit(0);
    }
}
