package org.nmascrie.swingy.controllers;

/**
 * Controller to handle the terminal mode.
 */
class GUIController {

    public GUIController() {
        
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
     * Starts the game.
     */
    public void start() {

    }
}