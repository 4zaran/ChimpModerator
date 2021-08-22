package com.chimp.commands;

import com.chimp.commands.syntax.Command;
import com.chimp.commands.syntax.CommandWrapper;
import com.chimp.commands.syntax.ParameterType;
import com.chimp.services.AutoModerator;
import com.chimp.services.ContextService;

import java.util.Iterator;
import java.util.Map;

public class CommandConfig extends Command {

    public CommandConfig(){
        addOption("warn",
                ParameterType.KEYWORD,
                false,
                "This option defines the number of warnings before the next punishment (kick) for the member.")
                .addValue("warn_count",
                        ParameterType.INTEGER,
                        false,
                        "This option is used to change the number of warnings. " +
                                "Note that setting this option to 0 will mean that " +
                                "warning execution will be skipped and next punishment will be considered.");

        addOption("kick",
                ParameterType.KEYWORD,
                false,
                "This option defines the number of kicks before banning the member from the guild.").
                addValue("kick_count",
                    ParameterType.INTEGER,
                    false,
                    "This option is used to change the number of kicks" +
                            "Note that setting this option to 0 will mean that " +
                            "kick execution will be skipped and the violator will be banned to compensate the violation.");

        addOption("AutoModerator",
                ParameterType.KEYWORD,
                false,
                "This option displays the state of AutoModerator module.")
                .addValue("state",
                    ParameterType.BOOLEAN,
                    false,
                    "This option is used to enable or disable the AutoModerator module." +
                            "It will accept only the \"true\"/\"false\" values.");
    }

    @Override
    public String getDescription() {
        return "Used to change settings of application. "; // +
//                "Commands are using keywords to define a desired setting. " +
//                "Using keyword alone will display a currently set value for matching setting. " +
//                "Using keyword with value will change this setting to specified value. " +
//                "Note that it is possible to change multiple settings with one command execution";
    }

    @Override
    public void execute(CommandWrapper wrapper) throws Exception {
        wrapper.assignOptions();

        if(wrapper.hasUnusedValues())
            reportError("Too many parameters!", wrapper);
        if(wrapper.hasNoOptions())
            reportError("No options provided! Use " + ContextService.getPrefix() + "help to see available options.", wrapper);

        Iterator<Map.Entry<String, String>> it = wrapper.getOptions().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String> pair = it.next();
            String value = pair.getValue();
            switch (pair.getKey()){
                case "automoderator":
                    if(!value.equals("")){
                        AutoModerator.setEnabled(Boolean.parseBoolean(value));
                    }
                    if(AutoModerator.isEnabled())
                        reportInfo("AutoModerator is now enabled!", wrapper);
                    else reportInfo("AutoModerator is now disabled!", wrapper);
                    break;
                case "warn":
                    if(!value.equals(""))
                        AutoModerator.setWarnAmount(Integer.parseInt(value));
                    reportInfo("Warn count is now " + AutoModerator.getWarnAmount() + ".", wrapper);
                    break;
                case "kick":
                    if(!value.equals(""))
                        AutoModerator.setKickAmount(Integer.parseInt(value));
                    reportInfo("Kick count is now " + AutoModerator.getKickAmount() + ".", wrapper);
                    break;
                default:
                    reportError("Unknown keyword!", wrapper);
                    break;
            }
            it.remove();
        }
    }
}
