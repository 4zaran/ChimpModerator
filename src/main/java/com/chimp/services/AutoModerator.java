package com.chimp.services;

import com.chimp.moderation.BadUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AutoModerator {
    private static boolean enabled;
    private static int warnAmount;
    private static int kickAmount;
    private static List<BadUser> badUsers;
    private static List<String> censoredExpressions;

    public AutoModerator() {
        enabled = true;
        warnAmount = kickAmount = 3;
        badUsers = new ArrayList<>();
        censoredExpressions = new ArrayList<>();
    }
    // Setters
    public static int getWarnAmount() { return warnAmount; }
    public static int getKickAmount() { return kickAmount; }
    public static List<BadUser> getBadUsers() { return badUsers; }
    public static List<String> getCensoredExpressions() { return censoredExpressions; }
    public static void setEnabled(boolean enabled) { AutoModerator.enabled = enabled; }

    //Getters
    public static boolean isEnabled() { return enabled; }
    public static void setWarnAmount(int warnAmount) { AutoModerator.warnAmount = warnAmount; }
    public static void setKickAmount(int kickAmount) { AutoModerator.kickAmount = kickAmount; }
    public static void setBadUsers(List<BadUser> badUsers) { AutoModerator.badUsers = badUsers; }
    public static void setCensoredExpressions(List<String> censoredExpressions) { AutoModerator.censoredExpressions = censoredExpressions; }

    @Override
    public String toString() {
        return "AutoModerator{" +
                "warnAmount=" + warnAmount +
                ", kickAmount=" + kickAmount +
                ", badUsers=" + badUsers +
                ", censoredExpressions=" + censoredExpressions +
                '}';
    }

    public static boolean censor(String expression){
        if(!censoredExpressions.contains(expression)) {
            censoredExpressions.add(expression);
            return true;
        }
        else return false;
    }
    public static void uncensor(String expression){
        censoredExpressions.remove(expression);
    }
    public static String censored(){
        StringBuilder expressions = new StringBuilder();
        for (String censoredExpression: censoredExpressions) {
            expressions.append(censoredExpression).append("\n");
        }
        return expressions.toString();
    }
    public void addBadUser(User user){
        badUsers.add(new BadUser(user, warnAmount, kickAmount));
    }

    public void checkViolation(@Nonnull MessageReceivedEvent event){
        for (String censoredExpression : censoredExpressions) {
            if (event.getMessage().getContentRaw().toLowerCase().contains(censoredExpression.toLowerCase())) {
                boolean alreadyViolated = false;
                BadUser violator = null;
                for (BadUser badUser : badUsers) {
                    if (badUser.getUser() == event.getAuthor()) {
                        alreadyViolated = true;
                        violator = badUser;
                        break;
                    }
                }
                if (!alreadyViolated) {
                    violator = new BadUser(event.getAuthor(), warnAmount, kickAmount);
                    badUsers.add(violator);
                }
                violator.hasViolated(event.getTextChannel());
                break;
            }
        }
    }
}
