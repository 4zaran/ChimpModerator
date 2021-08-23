package com.chimp.services.syntax;

import com.chimp.services.ContextService;
import net.dv8tion.jda.api.entities.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helps with converting options from regular string to desired type.
 */
public class OptionConverter {
    private final String value;

    public OptionConverter(String value) {
        this.value = value;
    }

    public Integer asInteger(){
        return Integer.valueOf(value);
    }

    public String asString(){
        return value;
    }

    public User asUser(){
        Pattern p = Pattern.compile("^(<@!?)([0-9]+)(>$)");
        Matcher m = p.matcher(value);
        if(m.find())
            return ContextService.getAppService().getJda().getUserById(m.group(2));
        System.out.println(value.substring(1));
        return ContextService.getAppService().getJda().getUserByTag(value.substring(1));
    }

    public Boolean asBool(){
        return Boolean.parseBoolean(value);
    }

    public boolean asBoolFromKeyword(){
        return value != null;
    }
}
