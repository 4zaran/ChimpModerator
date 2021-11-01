package com.chimp.commands;

import com.chimp.services.moderation.AutoModerator;
import com.chimp.services.syntax.Command;
import com.chimp.services.syntax.CommandWrapper;
import com.chimp.services.syntax.OptionConverter;
import com.chimp.services.syntax.ParameterType;

import java.util.ArrayList;
import java.util.List;

public class CommandCensored extends Command {

    public CommandCensored(){
        // TODO MAYBE ADD SOMETHING TO TELL IF ALL PARAMETERS MUST BE USED
        addOption("add", ParameterType.KEYWORD, false, "Used to add new censored expressions.")
                .addValue("expression", ParameterType.ANY, true, "Expression to add");
        addOption("remove", ParameterType.KEYWORD, false, "Used to remove expression from censored ones.")
                .addValue("expression", ParameterType.ANY, true, "Expression to remove");
    }

    @Override
    public String getDescription() {
        return "Displays a list of all censored expressions.";
    }

    @Override
    public void execute(CommandWrapper wrapper) throws Exception {
        wrapper.assignOptions();

        // No options/keywords - Display simple list of censored expressions.
        if(wrapper.hasNoOptions()){
            String censored = AutoModerator.getCensored(wrapper.getGuild());
            if(censored.length() > 0)
                reportInfo("Censored expressions: " + censored, wrapper);
            else reportInfo("No censored expressions found.", wrapper);
        }
        else{
            executeAddOption(wrapper);
            executeRemoveOption(wrapper);
        }
    }

    private void executeAddOption(CommandWrapper wrapper){
        if(wrapper.isOptionPresent("add")){
            String expression;
            List<String> error = new ArrayList<>();
            List<String> success = new ArrayList<>();

            OptionConverter getOption = wrapper.getOption("add");

            while (getOption != null) {
                expression = getOption.asString();

                if (AutoModerator.addCensored(expression, wrapper.getGuild())) {
                    if (!success.contains(expression))
                        success.add(expression);
                } else if (!error.contains(expression))
                    error.add(expression);

                getOption = wrapper.fetchNextValue("add");
            }

            StringBuilder sb = new StringBuilder();
            if (success.size() > 0) {
                sb.append("Successfully censored those expressions: \n");
                appendList(sb, success);
            }
            if(error.size() > 0){
                sb.append("Those expressions were already censored: \n");
                appendList(sb, error);
            }
            reportInfo(sb.toString(), wrapper);
            // String s = wrapper.getOption("add").asString();
        }
    }

    private void executeRemoveOption(CommandWrapper wrapper){
        if(wrapper.isOptionPresent("remove")){String expression;
            List<String> success = new ArrayList<>();
            List<String> error = new ArrayList<>();

            OptionConverter getOption = wrapper.getOption("remove");

            while(getOption != null){
                expression = getOption.asString();

                if (AutoModerator.removeCensored(expression, wrapper.getGuild())){
                    if(!success.contains(expression))
                        success.add(expression);
                }
                else if(!error.contains(expression))
                    error.add(expression);

                getOption = wrapper.fetchNextValue("remove");
            }

            StringBuilder sb = new StringBuilder();
            if(success.size() > 0) {
                sb.append("Successfully removed those expressions: \n");
                appendList(sb, success);
            }
            if(error.size() > 0) {
                sb.append("Those expressions are not censored: \n");
                appendList(sb, error);
            }
            reportInfo(sb.toString(), wrapper);
        }
    }

    private void appendList(StringBuilder sb, List<String> list) {
        if (list.size() > 0) {
            for (String s : list) {
                sb.append(s).append(", ");
            }
            int i = sb.lastIndexOf(",");
            sb.deleteCharAt(i).deleteCharAt(i);
            sb.append("\n");
        }
    }
}