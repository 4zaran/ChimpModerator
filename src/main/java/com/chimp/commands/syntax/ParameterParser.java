package com.chimp.commands.syntax;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used to check what type the parameter is.
 */
public class ParameterParser {
    private static boolean checkPattern(String pattern, String strToCheck){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(strToCheck);
        return m.matches();
    }

    private static boolean checkInteger(String strToCheck){
        return checkPattern("^\\d+$", strToCheck);
    }

    private static boolean checkBoolean(String strToCheck){
        return checkPattern("(^true$)|(^false$)", strToCheck);
    }

    private static boolean checkUser(String strToCheck){
        return checkPattern("^(@[a-zA-Z ]{2,32}+(#[0-9]{4})$)|(^<@!?[0-9]+>$)", strToCheck);   // @USER OR @USER#1234
    }

    private static boolean checkKeyword(String strToCheck, List<String> keywords){
        strToCheck = strToCheck.toLowerCase();
        if(checkPattern("^[a-zA-Z]+$", strToCheck)) //check if it's 1 word
            return keywords.contains(strToCheck);
        return false;
    }

    public static ParameterType matchType(String strToCheck, List<String> keywords){
        if(checkBoolean(strToCheck))
            return ParameterType.BOOLEAN;
        else if(checkKeyword(strToCheck, keywords))
            return ParameterType.KEYWORD;
        else if(checkUser(strToCheck))
            return ParameterType.USER;
        else if(checkInteger(strToCheck))
            return ParameterType.INTEGER;
        return ParameterType.STRING;
    }
}
