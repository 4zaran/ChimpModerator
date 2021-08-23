package com.chimp.commands.old;

import com.chimp.services.AutoModerator;
import com.chimp.services.syntax.Command;
import com.chimp.services.syntax.CommandWrapper;
import com.chimp.services.syntax.OptionConverter;
import com.chimp.services.syntax.ParameterType;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated
 */
public @Deprecated class CommandUncensor extends Command {
    private List<String> success;
    private List<String> error;

    public CommandUncensor(){
        addOption("expression",
                ParameterType.STRING,
                true,
                "Defines expression to remove from censored ones. " +
                        "Command can acquire multiple expressions at once. " +
                        "Expressions are separated with space by default. " +
                        "To censor expression containing multiple words just use a quotation marks (\")");
    }

    @Override
    public String getDescription() {
        return "Used to remove provided text from censored expressions.";
    }

    @Override
    public void execute(CommandWrapper wrapper) throws Exception {
        wrapper.assignOptions();

        String expression;
        error = new ArrayList<>();
        success = new ArrayList<>();

        OptionConverter getOption = wrapper.getOption("expression");

        while(getOption != null){
            expression = getOption.asString();

            uncensor(expression);

            getOption = wrapper.fetchNextValue("expression");
        }

        StringBuilder sb = new StringBuilder();
        appendSuccessfull(sb);
        appendAlreadyCensored(sb);
        reportInfo(sb.toString(), wrapper);
    }

    private void uncensor(String expression) {
        if (AutoModerator.removeCensored(expression)){
            if(!success.contains(expression))
                success.add(expression);
        }
        else if(!error.contains(expression))
            error.add(expression);
    }


    private void appendSuccessfull(StringBuilder sb) {
        if(success.size() > 0) {
            sb.append("Successfully removed those expressions: \n");
            for (String s : success) {
                sb.append(s).append(", ");
            }
            sb.append('\n');
        }
    }

    private void appendAlreadyCensored(StringBuilder sb) {
        if(error.size() > 0) {
            sb.append("Those expressions are not censored: \n");
            for (String s : error) {
                sb.append(s).append(", ");
            }
        }
    }
}
