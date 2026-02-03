package org.nmascrie.swingy.controllers;

public class MainController {
    public static void main(String[] args) {
        if (args.length < 2 || args[1].equals("console")) {
            System.out.println("Starting in console mode.");
            AsciiController controller = new AsciiController();
            controller.start();
        }
        else {
            GUIController controller = new GUIController();
            controller.start();
        }
    }
}