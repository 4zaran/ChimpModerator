package com.chimp.commands.syntax;

/**
 * Determines a single command's option.
 * It can be a command's option as well as option's value.
 */
public class Option {
    /** This name will be a key in map holding all values of the command. */
    private final String name;

    /** The type of option. It determines what type of parameter will this option prefer. */
    private final ParameterType type;

    /** When an option is required, and it hasn't been found during execution exception will be raised. */
    private final boolean required;

    /** When an option is a keyword, it can have a value.
     * In other words, keyword can specify what the option relate to
     * and the value can be used to change the value of related thing.*/
    private Option value;

    /** Short description of the option. Used in the help command */
    private final String description;

    // -- CONSTRUCTORS --

    public Option(String name, ParameterType type, boolean required, String description) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.description = description;
        this.value = null;
    }


    // -- SETTERS --

    /**
     * Sets the value.
     * @param valueName name of the value
     * @param type for which the command will search
     * @param required necessary to execute command
     * @param description short description of this value
     */
    public void addValue(String valueName,
                         ParameterType type,
                         boolean required,
                         String description){

        valueName = valueName.toLowerCase();
        Option value = new Option(valueName, type, required, description);
        if(this.value == null){
            this.value = value;
        }
    }

    // -- GETTERS --

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public ParameterType getType(){
        return type;
    }

    public boolean hasValue(){
        return value != null;
    }

    public Option getValue(){
        return value;
    }
}
