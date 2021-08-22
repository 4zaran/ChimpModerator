package com.chimp.commands;

import com.chimp.commands.syntax.Command;
import com.chimp.commands.syntax.CommandWrapper;
import com.chimp.services.ContextService;

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
        ContextService.getAppService().getJda().shutdown();
        System.exit(0);
    }
}
