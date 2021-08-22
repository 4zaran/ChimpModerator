package com.chimp.commands.old;

import com.chimp.commands.syntax.Command;
import com.chimp.commands.syntax.CommandWrapper;
import com.chimp.commands.syntax.OptionConverter;
import com.chimp.commands.syntax.ParameterType;
import com.chimp.services.AutoModerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated
 */
public @Deprecated class CommandCensor extends Command {
    private List<String> successfullyCensored;
    private List<String> alreadyCensored;

    public CommandCensor() {
        addOption("expression",
                ParameterType.ANY,
                true,
                "Defines expression to be censored. Command can acquire multiple expressions at once." +
                        "Expressions are separated with space by default. " +
                        "To censor expression containing multiple words just use a quotation marks (\")");
    }

    @Override
    public String getDescription() {
        return "Used to add expressions to a list of censored ones";
    }

    @Override
    public void execute(CommandWrapper wrapper) throws Exception {
        wrapper.assignOptions();

        String expression;
        alreadyCensored = new ArrayList<>();
        successfullyCensored = new ArrayList<>();

        OptionConverter getOption = wrapper.getOption("expression");

        while (getOption != null) {
            expression = getOption.asString();

            censor(expression);

            getOption = wrapper.fetchNextValue("expression");
        }

        StringBuilder sb = new StringBuilder();
        appendSuccessfull(sb);
        appendAlreadyCensored(sb);
        reportInfo(sb.toString(), wrapper);
    }

    private void censor(String expression) {
        if (AutoModerator.censor(expression)) {
            if (!successfullyCensored.contains(expression))
                successfullyCensored.add(expression);
        } else if (!alreadyCensored.contains(expression))
            alreadyCensored.add(expression);
    }

    private void appendAlreadyCensored(StringBuilder sb) {
        if (alreadyCensored.size() > 0) {
            sb.append("Those expressions were already censored: \n");
            for (String s : alreadyCensored) {
                sb.append(s).append(", ");
            }
        }
    }

    private void appendSuccessfull(StringBuilder sb) {
        if (successfullyCensored.size() > 0) {
            sb.append("Successfully censored those expressions: \n");
            for (String s : successfullyCensored) {
                sb.append(s).append(", ");
            }
            sb.append('\n');
        }
    }
}
