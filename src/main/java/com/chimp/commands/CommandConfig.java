package com.chimp.commands;

import com.chimp.services.AutoModerator;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.TreeMap;

public class CommandConfig implements Command {
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        boolean finished = false;
        if (parameters.size() > 1) {
            String parameterKeyWord = parameters.get(1).toLowerCase();
            switch (parameterKeyWord) {
                case "warn":
                    if (parameters.size() == 2) {
                        event.getTextChannel().sendMessage("Warnings amount: " + AutoModerator.getWarnAmount()).queue();
                        finished = true;
                    } else if (parameters.size() == 3 && isNumeric(parameters.get(2))) {
                        int amount = Integer.parseInt(parameters.get(2));
                        event.getTextChannel().sendMessage("Setting warnings amount to " + amount).queue();
                        AutoModerator.setWarnAmount(amount);
                        finished = true;
                    }
                    break;
                case "kick":
                    if (parameters.size() == 2) {
                        event.getTextChannel().sendMessage("Kicks amount: " + AutoModerator.getKickAmount()).queue();
                        finished = true;
                    } else if (parameters.size() == 3 && isNumeric(parameters.get(2))) {
                        int amount = Integer.parseInt(parameters.get(2));
                        event.getTextChannel().sendMessage("Setting kicks amount to " + amount).queue();
                        AutoModerator.setKickAmount(amount);
                        finished = true;
                    }
                    break;
                case "automoderator":
                    if (parameters.size() == 2) {
                        if (AutoModerator.isEnabled())
                            event.getTextChannel().sendMessage("AutoModerator is enabled").queue();
                        else
                            event.getTextChannel().sendMessage("AutoModerator is disabled").queue();
                        finished = true;
                    } else if (parameters.size() == 3) {
                        if (parameters.get(2).equalsIgnoreCase("enable")) {
                            event.getTextChannel().sendMessage("Enabling AutoModerator module...").queue();
                            AutoModerator.setEnabled(true);
                            finished = true;
                        } else if (parameters.get(2).equalsIgnoreCase("disable")) {
                            event.getTextChannel().sendMessage("Disabling AutoModerator module...").queue();
                            AutoModerator.setEnabled(false);
                            finished = true;
                        }
                    }
                    break;
            }
        }
        if (!finished) {
            event.getTextChannel().sendMessage("Invalid syntax!").queue();
        }
    }

    @Override
    public String getDescription() {
        return "Used to config bot";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/config warn", "Displays amount of warnings performed before next punishment");
        commandsWithDescriptions.put("/config warn (count)", "Sets the warnings performed before next punishment");
        commandsWithDescriptions.put("/config kick", "Displays amount of kicks performed before banning user");
        commandsWithDescriptions.put("/config kick (count)", "Sets the kicks performed before banning user");
        commandsWithDescriptions.put("/config automoderator", "Displays information about status");
        commandsWithDescriptions.put("/config automoderator (enable / disable)", "Sets the kicks performed before banning user");
        return commandsWithDescriptions;
    }

    private static boolean isNumeric(String s) {
        if (s == null) {
            return false;
        }
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
