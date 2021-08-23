package com.chimp.services.moderation;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

// TODO UPDATE ALL JAVADOCS
/**
 * Is used to do all the moderation work:
 * checking for violations, keeping track of violators, delegating punishments.
 */
public class AutoModerator {
    /** List of all guilds */
    private static List<GuildRestrictions> guilds;

    public AutoModerator() {
        guilds = new ArrayList<>();
    }


    // -- PUBLIC METHODS --

    /**
     * Checks for violation and deals with it
     * @param event of receiving a message
     */
    public void checkViolation(@Nonnull MessageReceivedEvent event){
        if (containsCensoredExpression(event)) {
            BadUser violator = getBadUser(event);
            violator.hasViolated(event.getTextChannel());
        }
    }

    /**
     * Add specified expression to forbidden ones for specified guild
     * @param expression to censor
     * @param guild that will have specified expression censored
     * @return false if expression is already censored
     */
    public static boolean addCensored(String expression, Guild guild){
        GuildRestrictions restrictions = getGuildRestrictions(guild);
        List<String> censoredExpressions = restrictions.getCensoredExpressions();

        if(!censoredExpressions.contains(expression)) {
            censoredExpressions.add(expression);
            return true;
        }
        else return false;
    }

    /**
     * Remove specified expression from forbidden ones for specified guild
     * @param expression string to remove
     * @param guild that will have specified expression removed
     * @return true if operation was successful
     */
    public static boolean removeCensored(String expression, Guild guild){
        GuildRestrictions restrictions = getGuildRestrictions(guild);
        List<String> censoredExpressions = restrictions.getCensoredExpressions();

        if(censoredExpressions.contains(expression)) {
            censoredExpressions.remove(expression);
            return true;
        }
        return false;
    }

    /**
     * Builds a string of all censored expressions for specified guild
     * @return all censored expression in one string
     */
    public static String getCensored(Guild guild){
        StringBuilder expressions = new StringBuilder();
        GuildRestrictions restrictions = getGuildRestrictions(guild);
        List<String> censoredExpressions = restrictions.getCensoredExpressions();

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

    // PRIVATE METHODS

    /**
     * Adds new restriction's object for specified guild
     * @param guild to create restrictions for
     * @return created object
     */
    private static GuildRestrictions addGuildRestrictions(Guild guild){
        GuildRestrictions restrictions = new GuildRestrictions(guild);
        guilds.add(restrictions);
        return restrictions;
    }

    /**
     * Checks if a message contains any of censored expressions.
     * @param event event of receiving a message
     * @return true if contains any censored expression
     */
    private boolean containsCensoredExpression(@NotNull MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        GuildRestrictions restrictions = getGuildRestrictions(guild);
        for (String expression : restrictions.getCensoredExpressions()) {
            if(event.getMessage().getContentRaw().toLowerCase().contains(expression))
                return true;
        }
        return false;
    }

    // GETTERS

    /**
     * Returns the information about moderation module ({@code AutoModerator}) being enabled for specified guild.
     * @param guild to check if it has enabled moderator
     * @return true if moderator is enabled, false if not
     */
    public static boolean isEnabled(Guild guild) {
        GuildRestrictions restrictions = getGuildRestrictions(guild);
        return restrictions.isEnabled();
    }

    /**
     * Returns warn amount for specified guild.
     * It calls the {@code getWarnAmount()} in corresponding GuildRestrictions object.
     * @param guild  to get the value for
     * @return warn amount
     */
    public static int getWarnAmount(Guild guild) {
        GuildRestrictions restrictions = getGuildRestrictions(guild);
        return restrictions.getWarnAmount();
    }

    /**
     * Returns kick amount for specified guild.
     * It calls the {@code getKickAmount()} in corresponding GuildRestrictions object.
     * @param guild  to get the value for
     * @return kick amount for specified guild
     */
    public static int getKickAmount(Guild guild) {
        GuildRestrictions restrictions = getGuildRestrictions(guild);
        return restrictions.getKickAmount();
    }

    /**
     * Searches the bad user list for author of the message.
     * @param event received message
     * @return BadUser instance for author of the message or null if not found
     */
    private BadUser getBadUser(@NotNull MessageReceivedEvent event) {
        GuildRestrictions restrictions = getGuildRestrictions(event.getGuild());
        for (BadUser badUser : restrictions.getBadUsers())
            if (badUser.getMember() == event.getMember()) {
                return badUser;
            }
        return restrictions.addBadUser(event.getMember());
    }

    /**
     * Returns the GuildRestriction object for specified guild.
     * @param guild to get the restrictions for
     * @return GuildRestriction object for guild
     */
    private static GuildRestrictions getGuildRestrictions(Guild guild){
        for (GuildRestrictions g : guilds) {
            if(g.getGuild().equals(guild))
                return g;
        }
        return addGuildRestrictions(guild);
    }

    // SETTERS

    /**
     * Sets the moderator's module enabled state for specified guild
     * @param enabled true to enable, false to disable
     * @param guild to set the value for
     */
    public static void setEnabled(boolean enabled, Guild guild) {
        GuildRestrictions restrictions = getGuildRestrictions(guild);
        restrictions.setEnabled(enabled);
    }

    /**
     * Sets the moderator's warn amount for specified guild
     * @param warnAmount amount of warns
     * @param guild to set the value for
     */
    public static void setWarnAmount(int warnAmount, Guild guild) {
        GuildRestrictions restrictions = getGuildRestrictions(guild);
        restrictions.setWarnAmount(warnAmount);
    }

    /**
     * Sets the moderator's kick amount for specified guild
     * @param kickAmount amount of kicks
     * @param guild to set the value for
     */
    public static void setKickAmount(int kickAmount, Guild guild) {
        GuildRestrictions restrictions = getGuildRestrictions(guild);
        restrictions.setKickAmount(kickAmount);
    }
}
