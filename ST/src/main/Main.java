package main;

import main.frontend.ConsoleUI;

public final class Main {

    private final ConsoleUI consoleUI;
    // create immutable object to reduce bug occurrence
    private Main() { this.consoleUI = new ConsoleUI(); }

    public static void main(final String[] args) {
        new Main().consoleUI.start();
    }
}