package com.chimp.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.TreeMap;

public class CommandRole implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {

        /*
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MESSAGE_WRITE);
        permissions.add(Permission.MESSAGE_READ);
        event.getGuild().createRole().setName("Nowa rola").setPermissions(permissions).complete();
         */
    }

    @Override
    public String getDescription() {
        return "Used to manage roles";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/role", "...");
        return commandsWithDescriptions;
    }
}
