package org.nmascrie.swingy.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import org.nmascrie.swingy.enums.HeroClass;
import org.nmascrie.swingy.generators.ItemGenerator;
import org.nmascrie.swingy.models.BattleScene;
import org.nmascrie.swingy.models.Character;
import org.nmascrie.swingy.models.Item;
import org.nmascrie.swingy.models.Map;

/**
 * Controller to handle the terminal mode.
 */
class AsciiController {
    private final Scanner scan;
    private final ArrayList<Character> charList;

    public AsciiController() {
        this.scan = new Scanner(System.in);
        this.charList = new ArrayList<>();
    }

    /**
     * Await the next input.
     */
    private String awaitInput() {
        String scanned;
        try {
            scanned = this.scan.nextLine();
            @SuppressWarnings("unused")
            char a = scanned.charAt(0);
        } catch (StringIndexOutOfBoundsException e) {
            return " ";
        }
        return scanned;
    }

    /**
     * Waits for time miliseconds.
     */
    private void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
    }

    /**
     * Loads a saved character.
     */
    private Character loadSave(String name) {
        return Character.imports(name);
    }

    /**
     * Saves the character.
     */
    private void save(Character c) {
        c.exports();
    }

    /**
     * Loads all characters in the /saves/ folder, and store them in the list.
     */
    private void loadAllSaves() {
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
                    buffer = this.loadSave(name);
                    if (null != buffer)
                        this.charList.add(buffer);
                }
            }
        }
    }

    /**
     * Clears the terminal display.
     */
    private void clearTerminal() {
        System.out.print("\033\143");
    }

    /**
     * Creates a new character and saves it.
     */
    private Character createNewCharacter() {
        String name;
        String choice;
        HeroClass classe = null;

        System.out.println("Please enter your name.");
        name = this.awaitInput();
        System.out.println("Please choose a class.");
        System.out.println("1 - Gunner: A solid but slow fighter.");
        System.out.println("2 - Scout:  A swift yet fragile warrior. Can dodge attacks.");
        System.out.println("3 - Engineer: A soldier with balanced abilities and high critical chance.");
        System.out.println("4 - Driller: A powerful and dangerous maniac that forgoes defenses.");
        while (true) { 
            choice = this.awaitInput();
            switch (choice.toLowerCase().charAt(0)) {
                case '1' -> classe = HeroClass.GUNNER;
                case '2' -> classe = HeroClass.SCOUT;
                case '3' -> classe = HeroClass.ENGINEER;
                case '4' -> classe = HeroClass.DRILLER;
            }
            if (null != classe)
                break ;
        }
        System.out.println("Hoxxes 3 welcomes you, " + name + " ...");
        return new Character(name, classe);
    }

    /**
     * Displays the list of loaded characters, and allows the player to choose one.
     */
    private Character chooseACharacter() {
        int idx = 0;
        int choice;

        while (true) { 
            this.clearTerminal();
            System.out.println("Loaded characters:");
            for (Character c : this.charList) {
                System.err.println(idx + ": " + c.getName() + ", level " + c.getLevel() + " " + c.getClasse());
                idx += 1;
            }
            System.out.println("\nPlease choose a character.");
            try {
                choice = Integer.parseInt(this.awaitInput());
            } catch (NumberFormatException e) {
                choice = -1;
            }
            if (choice >= 0 && choice < this.charList.size())
                return this.charList.get(choice);
        }
    }

    /**
     * Displays the character stats.
     */
    private void admireDwarf(Character c) {
        @SuppressWarnings("unused")
        String useless;

        this.clearTerminal();
        System.out.println(c);
        System.out.println("Bugs killed: " + c.kills);
        System.out.println("Caverns explored: " + c.explored);
        useless = this.awaitInput();
    }

    /**
     * Displays the map and the input menu.
     */
    private void displayMapAndMenu(Map map, String msg) {
        System.out.print("\033\143");
        System.out.println(map);
        System.out.println("\n" + msg + "\n");
        System.out.println("\nU - Move up\nD - Move down\nL - Move left\nR - Move Right \nZ - Cancel last move\nK - Dig around\nP - Show details");
    }

    /**
     * Simulates a fight. Returns whether or not the character was victorious.
     */
    private long fight(BattleScene scene, Map map, Character c) {
        String choice;
        long exp;

        this.clearTerminal();
        while (true) {
            BattleScene.BattleLog log = scene.nextMove();
            while (null != log) {
                System.out.println(log);
                log = scene.nextMove();
                this.wait(125);
            }
            if (scene.getRight().getCurrent_hp() <= 0)
                return -1;
            if (scene.getLeft().getCurrent_hp() <= 0) {
                System.out.println("");
                Item it = ItemGenerator.getInstance().generateItem(scene.getLeft().getLevel(), scene.getLeft().getLootChance(), scene.getLeft().getPower());
                if (null != it) {
                    System.out.println("Loot: " + it);
                    if (null == c.getEquipped(it.getType()))
                        System.out.println(it.fullDesc());
                    else
                        System.out.println(c.getEquipped(it.getType()).compare(it));
                    System.out.println("Do you wish to equip it ?\nY - Yes\nN - No (Defaults to No)");
                    choice = this.awaitInput();
                    if (choice.toLowerCase().equals("y"))
                        c.equip(it);
                }
                exp = scene.getLeft().calculateExp();
                System.out.println(c.getName() + " gained " + exp + " Experience.");
                c.grantExperience(exp);
                map.eliminateMonster();
                this.wait(2500);
                return exp;
            }
        }
    }

    /**
     * Starts a level.
     */
    private void startLevel(Character c) {
        long size = (c.getLevel() - 1) * 5 + 10 -(c.getLevel() % 2);
        Map map = new Map(size, c, c.getLevel() + 1);
        String msg = "";
        String action;
        BattleScene scene = null;
        long defeated = 0;
        long bonus = 0;

        while (!map.victoryCondition()) { 
            this.displayMapAndMenu(map, msg);
            action = this.awaitInput();
            switch (action.toLowerCase().charAt(0)) {
                case 'u' -> scene = map.up();
                case 'd' -> scene = map.down();
                case 'l' -> scene = map.left();
                case 'r' -> scene = map.right();
                case 'z' -> map.cancel();
                case 'k' -> map.dig();
                case 'v' -> msg = "For rock and stone !";
                case 'p' -> this.admireDwarf(c);
            }
            if (null != scene) {
                System.out.println("A " + scene.getLeft().getName() + " blocks your path.");
                System.out.println("Do you wish to engage ?\nY - Yes\nN - No (50% chance)");
                action = this.awaitInput();
                if (action.toLowerCase().equals("n") && Math.random() < 0.5) {
                    map.getHero().cancel();
                    scene = null;
                }
                else {
                    defeated = this.fight(scene, map, c);
                    bonus += Math.round(defeated / 10);
                    c.kills += 1;
                    scene = null;
                }
                if (defeated == -1) {
                    System.out.println("You have been defeated.");
                    this.wait(2500);
                    break ;
                }
            }
        }
        this.displayMapAndMenu(map, msg);
        System.out.println("You have escaped this cavern !");
        System.out.println("EXP Bonus: " + bonus);
        c.grantExperience(bonus);
        c.explored += 1;
        this.wait(2500);
    }

    /**
     * Displays the main menu, giving choice between saving, quitting, starting a level or looking
     * at the character sheet.
     * 
     * @param c Player character.
     */
    private void mainMenu(Character c) {
        String choice;

        while (true) {
            this.clearTerminal();
            System.out.println("N - Starts a level (Difficulty: " + c.getLevel() + ")");
            System.out.println("S - Saves the character.");
            System.out.println("A - Admire your dwarf.");
            System.out.println("R - Rest your dwarf. Removes half of your EXP.");
            System.out.println("Q - Quits the game.");
            choice = this.awaitInput();
            switch (choice.toLowerCase().charAt(0)) {
                case 'n' -> this.startLevel(c);
                case 's' -> this.save(c);
                case 'a' -> this.admireDwarf(c);
                case 'q' -> choice = null;
                case 'r' -> c.rest();
            }
            if (null == choice)
                break ;
        }
    }

    /**
     * Starts the game.
     */
    public void start() {
        Character choice = null;
        String input;

        this.loadAllSaves();
        if (this.charList.size() < 1)
            choice = this.createNewCharacter();
        else {
            this.clearTerminal();
            System.out.println("N - Start a new character\nL - Load a character");
            while (true) { 
                input = this.awaitInput();
                switch (input.toLowerCase().charAt(0)) {
                    case 'n' -> choice = this.createNewCharacter();
                    case 'l' -> choice = this.chooseACharacter();
                }
                if (null != choice)
                    break ;
            }
        }
        System.out.println(choice);
        if (null == choice)
            return ;
        choice.exports();
        this.wait(1250);
        this.mainMenu(choice);
    }
}