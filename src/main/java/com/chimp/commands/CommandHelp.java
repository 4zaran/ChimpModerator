package com.chimp.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class CommandHelp implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        boolean finished = false;
        TreeMap<String, Command> commands = CommandSet.getCommands();
        StringBuilder descriptions = new StringBuilder();
        StringBuilder syntax = new StringBuilder();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.red);
        if (parameters.size() == 1) {
            eb.setTitle("Available commands", null);
            eb.setDescription("Note: use `/help [command]` for additional info");

            for (HashMap.Entry<String, Command> entry : commands.entrySet()) {
                eb.addField("`" + entry.getKey() + "`", entry.getValue().getDescription(), false);
            }
        } else {
            String command = "/" + parameters.get(1).toLowerCase();
            if (commands.containsKey(command)) {
                eb.setTitle("Syntax for command `" + command + "`", null);
                eb.setDescription("Arguments in `[ ]` are optional, arguments in `( )` are necessary");
                Command command1 = commands.get(command);
                TreeMap<String, String> allCommands = command1.getSyntax();
                for (HashMap.Entry<String, String> commandDescriptions : allCommands.entrySet()) {
                    eb.addField("`" + commandDescriptions.getKey() + "`", commandDescriptions.getValue(), false);
                }
            } else {
                event.getChannel().sendMessage("No such command!").queue();
                finished = true;
            }
        }
        if (!finished) {
            event.getChannel().sendMessage(eb.build()).queue();
        }

    }

    @Override
    public String getDescription() {
        return "Displays help";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/help", "Displays help");
        commandsWithDescriptions.put("/help [command]", "Displays syntax for specified command");
        return commandsWithDescriptions;
    }

    public EmbedBuilder embedTest(){
        // Create the EmbedBuilder instance
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Title", null);
        eb.setColor(Color.red);
        eb.setDescription("Text");
        eb.addField("Title of field", "test of field", false);
        return eb;
    }
}
