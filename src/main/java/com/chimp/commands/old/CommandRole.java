package com.chimp.commands.old;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.EnumSet;
import java.util.List;
import java.util.TreeMap;

/**
 * @deprecated not in use
 */
public class CommandRole implements Command {
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        if(parameters.size() == 2){
            List<Role> roles = event.getGuild().getRolesByName(parameters.get(1), true);
            if(roles.size() == 0){
                event.getTextChannel().sendMessage("No roles found!").queue();
            }
            else{
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Found roles:");
                eb.setColor(Color.yellow);
                for(Role role: roles){
                    EnumSet<Permission> rolePermissions = role.getPermissions();
                    eb.addField(role.getName(),rolePermissions.toString(),false);
                }
                event.getTextChannel().sendMessage(eb.build()).queue();
            }
        }
        else if(parameters.size() == 3){
            if(parameters.get(1).equalsIgnoreCase("add")){
                event.getGuild().createRole().setName(parameters.get(2)).setPermissions(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).complete();
            }
        }
    }

    @Override
    public String getDescription() {
        return "Used to manage roles";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/role add (role name)", "Creates new role");
        commandsWithDescriptions.put("/role (role name)", "Displays permissions of roles");
        return commandsWithDescriptions;
    }
}
