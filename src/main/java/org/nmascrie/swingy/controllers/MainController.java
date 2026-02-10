package org.nmascrie.swingy.controllers;

import java.util.NoSuchElementException;

public class MainController {
    public static void main(String[] args) {
        try {
            if (args.length < 1 || args[0].equals("console")) {
                System.out.println("Starting in console mode.");
                AsciiController controller = new AsciiController();
                controller.start();
            }
            else {
                GUIController.getInstance().start();
            }
        } catch (NoSuchElementException e) {}
    }
}