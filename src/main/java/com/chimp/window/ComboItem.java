package com.chimp.window;

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
