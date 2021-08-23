package com.chimp.commands;

import com.chimp.services.syntax.*;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class CommandHelp extends Command {

    public CommandHelp(){
        addOption("command",
                ParameterType.STRING,
                false,
                "Specifies the command to display help about");
        addOption("detailed",
                ParameterType.KEYWORD, false, "If given, all options will be described.");
    }

    @Override
    public String getDescription() {
        return "Displays help";
    }

    @Override
    public void execute(CommandWrapper wrapper) throws Exception {
        wrapper.assignOptions();
        TreeMap<String, Command> commands = CommandSet.getCommands();
        StringBuilder sb = new StringBuilder();

        String specificCommand = wrapper.getOption("command").asString();
        String detailed = wrapper.getOption("detailed").asString();
        boolean details = detailed != null && detailed.equals("");

        // Just a general help
        if(specificCommand == null){
            sb.append("**Available commands:**\n");
            sb.append("Note: use `/help [command]` for help with desired command," +
                    " you can also add `detailed` to see whole syntax.\n\n");

            for (HashMap.Entry<String, Command> entry : commands.entrySet()) {
                sb.append("`").append(entry.getKey()).append("` - ").append(entry.getValue().getDescription()).append("\n");
            }
        }
        else{   // Help for a specified command
            if (commands.containsKey(specificCommand)) {
                Command c = commands.get(specificCommand);
                sb.append("**Help for `").append(specificCommand).append("`:**\n").append(c.getDescription());
                sb.append("\nUse with `detailed` to see more (`!help [command] detailed`)\n");

                List<Option> options = c.getOptions();
                if(options.size() > 0)
                    sb.append("**Options:**");
                for (Option option : options) {
                    sb.append("\n\n`").append(option.getName()).append("` - ").append(option.getDescription());
                    sb.append("\n**Option type: **`").append(option.getType()).append("`, ");
                    sb.append("**Required?** `").append(option.isRequired()).append("`");
                    if(option.hasValue() && details) {
                        Option value = option.getValue();
                        sb.append("**Value:**\n");
                        sb.append("> `").append(value.getName()).append("` - ");
                        sb.append(value.getDescription()).append("\n");
                        sb.append("> **Value type: **`").append(value.getType()).append("`, ");
                        sb.append("**Required?** `").append(value.isRequired()).append("`");
                    }
                }
                // TODO CHAR LIMIT
            } else {
                reportError("No such command!", wrapper);
            }
        }
        reportInfo(sb.toString(), wrapper);
    }
}
