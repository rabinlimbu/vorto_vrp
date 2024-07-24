package com.limburabin.model;

public enum LoadState {
    NOT_STARTED("NOT_STARTED"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETE("COMPLETE");

    private final String name;

    LoadState(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
