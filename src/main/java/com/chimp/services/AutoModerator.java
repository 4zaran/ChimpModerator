package com.chimp.services;

import com.chimp.moderation.BadUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to do all the moderation work:
 * checking for violations, keeping track of violators, delegating punishments.
 */
public class AutoModerator {
    /** Holds information about moderation module being enabled. */
    private static boolean enabled;

    /** Holds amount of kicks executed before kicking the user.
     * Note that setting this to 0 will skip warn execution. */
    private static int warnAmount;

    /** Holds amount of kicks executed before ban.
     * Note that setting this to 0 will skip kicks and ban will be executed. */
    private static int kickAmount;

    /** List of all violators. User is added to this with the first violation. */
    private static List<BadUser> badUsers;

    /** List of all censored expressions.
     * It means that if any of those strings are found in message it is considered as violation. */
    private static List<String> censoredExpressions;

    public AutoModerator() {
        enabled = true;
        warnAmount = kickAmount = 3;
        badUsers = new ArrayList<>();
        censoredExpressions = new ArrayList<>();
    }


    // -- PUBLIC METHODS --

    /**
     * Adds a user to list of violators
     * @param user to be added
     * @return added user
     */
    public BadUser addBadUser(User user){
        BadUser badUser = new BadUser(user);
        badUsers.add(badUser);
        return badUser;
    }

    /**
     * Checks for violation and deals with it
     * @param event of receiving a message
     */
    public void checkViolation(@Nonnull MessageReceivedEvent event){
        if (containsCensoredExpression(event)) {
            BadUser violator = getBadUser(event);
            if (violator == null) violator = addBadUser(event.getAuthor());
            violator.hasViolated(event.getTextChannel());
        }
    }

    /**
     * Checks if a message contains any of censored expressions
     * @param event event of receiving a message
     * @return true if contains any censored expression
     */
    private boolean containsCensoredExpression(@NotNull MessageReceivedEvent event) {
        for (String expression : censoredExpressions) {
            if(event.getMessage().getContentRaw().toLowerCase().contains(expression))
                return true;
        }
        return false;
    }


    // PUBLIC STATIC METHODS

    /**
     * Add specified expression to forbidden ones
     * @param expression to censor
     * @return false if expression is already censored
     */
    public static boolean censor(String expression){
        if(!censoredExpressions.contains(expression)) {
            censoredExpressions.add(expression);
            return true;
        }
        else return false;
    }

    /**
     * Remove specified expression from forbidden ones
     * @param expression string to remove
     * @return true if operation was successful
     */
    public static boolean removeCensored(String expression){
        if(censoredExpressions.contains(expression)) {
            censoredExpressions.remove(expression);
            return true;
        }
        return false;
    }

    /**
     * Builds a string of all censored expressions to display
     * @return all censored expression in one string
     */
    public static String censored(){
        StringBuilder expressions = new StringBuilder();
        for (String censoredExpression: censoredExpressions) {
            expressions.append(censoredExpression).append(", ");
        }
        if(expressions.length() > 0) {
            int i = expressions.lastIndexOf(", ");
            expressions.deleteCharAt(i);
            expressions.deleteCharAt(i);
        }
        return expressions.toString();
    }


    // GETTERS

    /**
     * Searches the bad user list for author of the message
     * @param event received message
     * @return BadUser instance for author of the message or null if not found
     */
    private BadUser getBadUser(@NotNull MessageReceivedEvent event) {
        for (BadUser badUser : badUsers) {
            if (badUser.getUser() == event.getAuthor()) {
                return badUser;
            }
        }
        return null;
    }

    public static int getWarnAmount() { return warnAmount; }

    public static int getKickAmount() { return kickAmount; }

    public static boolean isEnabled() { return enabled; }


    // SETTERS

    public static void setEnabled(boolean enabled) { AutoModerator.enabled = enabled; }

    public static void setWarnAmount(int warnAmount) { AutoModerator.warnAmount = warnAmount; }

    public static void setKickAmount(int kickAmount) { AutoModerator.kickAmount = kickAmount; }
}
