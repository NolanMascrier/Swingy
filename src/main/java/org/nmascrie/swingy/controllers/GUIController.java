package org.nmascrie.swingy.controllers;

import java.io.File;
import java.util.ArrayList;

import javax.swing.UIManager;

import org.nmascrie.swingy.enums.HeroClass;
import org.nmascrie.swingy.models.BattleScene;
import org.nmascrie.swingy.models.Character;
import org.nmascrie.swingy.models.Map;
import org.nmascrie.swingy.views.MainFrame;

/**
 * Controller to handle the terminal mode.
 */
public class GUIController {

    private Character hero;
    private Map map;
    private BattleScene bs;
    private String log = "";
    private static GUIController INSTANCE = null;

    private GUIController() {
        
    }

    public static GUIController getInstance() {
        if (null == INSTANCE)
            INSTANCE = new GUIController();
        return INSTANCE;
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
     * Generates the hero.
     */
    public void generateHero(String name, HeroClass classe) {
        this.hero = new Character(name, classe);
        this.hero.exports();
    }

    public void setHero(Character c) {
        this.hero = c;
        c.exports();
        System.out.println("Set character " + c.getName() + ", lvl " + c.getLevel() + " " + c.getClasse());
    }

    /**
     * Loads all characters in the /saves/ folder, and store them in the list.
     */
    public ArrayList loadAllSaves() {
        ArrayList<Character> charList = new ArrayList<>();
        File folder = new File("saves/");
        File[] listOfFiles = folder.listFiles();
        String name;
        Character buffer;

        if(listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    name = listOfFile.getName();
                    if (name.equals(".gitaccess"))
                        continue ;
                    buffer = Character.imports(name);
                    if (null != buffer)
                        charList.add(buffer);
                }
            }
        }
        return charList;
    }

    /**
     * Creates a new map.
     */
    public void generateMap() {
        long size = (this.hero.getLevel() - 1) * 5 + 10 -(this.hero.getLevel() % 2);
        this.map = new Map(size, this.hero, this.hero.getLevel());
    }

    /**
     * Starts the game.
     */
    public void start() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If setting look and feel fails, just use default
            e.printStackTrace();
        }

        // Create and display the GUI
        MainFrame frame = new MainFrame();
        frame.display();
        
        System.out.println("Application started. Press W, A, S, or D keys to see validation in action.");
    }

    public Character getHero() {
        return hero;
    }

    public Map getMap() {
        return map;
    }

    public BattleScene getBs() {
        return bs;
    }

    public String getLog() {
        return log;
    }
}