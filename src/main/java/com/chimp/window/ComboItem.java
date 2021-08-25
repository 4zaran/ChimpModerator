package com.chimp.window;

/**
 * It is used in ComboBoxes to store info about guilds and text channels.
 * It holds two values: {@link #name name of the guild/channel} and {@link #id it's unique id}.
 * Using this class it is possible to display name in the combo boxes and get internal id of the element when necessary.
 */
public class ComboItem {
    private final String name;
    private final String id;

    public ComboItem(String name, String id){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
