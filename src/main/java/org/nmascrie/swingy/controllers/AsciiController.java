package org.nmascrie.swingy.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import org.nmascrie.swingy.models.Character;

/**
 * Controller to handle the terminal mode.
 */
class AsciiController {
    private final Scanner scan;
    private ArrayList<Character> charList;

    public AsciiController() {
        this.scan = new Scanner(System.in);
        this.charList = new ArrayList<>();
    }

    /**
     * Await the next input.
     */
    public String awaitInput() {
        return this.scan.nextLine();
    }

    /**
     * Waits for time miliseconds.
     */
    public void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
    }

    /**
     * Loads a saved character.
     */
    public Character loadSave(String name) {
        return Character.imports(name);
    }

    public void loadAllSaves() {
        File folder = new File("saves/");
        File[] listOfFiles = folder.listFiles();
        String name;

        if(listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    System.out.println("File " + listOfFile.getName());
                }
            }
        }
    }

    /**
     * Starts the game.
     */
    public void start() {

    }
}