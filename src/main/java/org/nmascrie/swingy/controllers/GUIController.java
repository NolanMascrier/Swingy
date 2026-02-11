package org.nmascrie.swingy.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.UIManager;

import org.nmascrie.swingy.enums.HeroClass;
import org.nmascrie.swingy.generators.ItemGenerator;
import org.nmascrie.swingy.models.BattleScene;
import org.nmascrie.swingy.models.Character;
import org.nmascrie.swingy.models.Creature;
import org.nmascrie.swingy.models.Item;
import org.nmascrie.swingy.models.Map;
import org.nmascrie.swingy.views.MainFrame;

/**
 * Controller to handle the terminal mode.
 */
public class GUIController {

    private Character hero;
    private Map map;
    private BattleScene bs;
    private final StringBuilder eventLog;
    private static GUIController INSTANCE = null;
    private long totalEXP = 0;
    private long lastEXP = 0;

    private GUIController() {
        this.eventLog = new StringBuilder();
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
        this.lastEXP = 0;
        this.totalEXP = 0;
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
    }

        /**
     * Handle the end of a battle
     * 
     * @param onDefeat Callback if player is defeated
     * @param onVictory Callback if player wins, receives Item or null
     */
    public void handleBattleEnd(Runnable onDefeat, Consumer<Item> onVictory) {
        if (bs == null) {
            return;
        }
        
        Creature player = bs.getRight();
        Creature enemy = bs.getLeft();
        
        if (player.getCurrent_hp() <= 0) {
            appendLog(player.getName() + " has been defeated!");
            if (onDefeat != null) {
                onDefeat.run();
            }
            return;
        }
        
        if (enemy.getCurrent_hp() <= 0) {
            appendLog(enemy.getName() + " has been defeated!");
            Item loot = ItemGenerator.getInstance().generateItem(
                enemy.getLevel(),
                enemy.getLootChance(),
                enemy.getPower()
            );
            
            if (loot != null) {
                appendLog("Loot dropped: " + loot.getName());
            } else {
                appendLog("No loot dropped.");
            }
            
            long exp = Math.round(
                enemy.getLevel() * 
                enemy.getLootChance() * 
                enemy.getPower()
            ) * 125;
            
            appendLog(hero.getName() + " gained " + exp + " experience!");
            this.totalEXP += exp / 10;
            this.lastEXP = exp;
            hero.grantExperience(exp);
            
            if (null != map) {
                map.eliminateMonster();
                appendLog("Enemy eliminated from map.");
            }

            this.hero.kills += 1;
            
            if (onVictory != null) {
                onVictory.accept(loot);
            }
        }
    }

    /**
     * Simplified battle end handler for when no loot handling is needed
     */
    public void handleBattleEnd(Runnable onComplete) {
        handleBattleEnd(
            onComplete,
            loot -> {
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        );
    }

    /**
     * Get the event log as a string
     */
    public String getLog() {
        return eventLog.toString();
    }
    
    /**
     * Append a message to the event log
     */
    public void appendLog(String message) {
        eventLog.append(message).append("\n");
    }
    
    /**
     * Clear the event log
     */
    public void clearLog() {
        eventLog.setLength(0);
    }
    
    /**
     * Set the current battle scene
     */
    public void setBattleScene(BattleScene battle) {
        this.bs = battle;
    }
    
    /**
     * Get the current battle scene
     */
    public BattleScene getBattleScene() {
        return bs;
    }

    public Character getHero() {
        return hero;
    }

    public Map getMap() {
        return map;
    }

    public long getTotalEXP() {
        return totalEXP;
    }

    public long getLastEXP() {
        return lastEXP;
    }
}