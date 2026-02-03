package org.nmascrie.swingy;

import java.util.Scanner;

import org.nmascrie.swingy.enums.HeroClass;
import org.nmascrie.swingy.generators.ItemGenerator;
import org.nmascrie.swingy.models.BattleScene;
import org.nmascrie.swingy.models.Character;
import org.nmascrie.swingy.models.Item;
import org.nmascrie.swingy.models.Map;

/**
 *
 * @author nmascrie
 */
public class Swingy {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Character gimli = new Character("Gimli", HeroClass.GUNNER);
        int survive = 0;
        gimli.equip(ItemGenerator.getInstance().generateItem(5, 1, 1));
        Scanner sc = new Scanner(System.in);
        Map map = new Map(39, gimli, 5);
        String msg = "";
        BattleScene bs = null;
        String choice;

        while (!map.victoryCondition())
        {
            bs = null;
            System.out.print("\033\143");
            System.out.println(map);
            System.out.print("\n" + msg + "\n");
            msg = "";
            System.out.println("\nU - Move up\nD - Move down\nL - Move left\nR - Move Right \nZ - Cancel last move\nK - Dig around\nP - Show details");
            choice = sc.nextLine();
            switch (choice.toLowerCase()) {
                case "u" -> bs = map.up();
                case "d" -> bs = map.down();
                case "l" -> bs = map.left();
                case "r" -> bs = map.right();
                case "z" -> map.getHero().cancel();
                case "k" -> map.dig();
                case "v" -> msg = "For rock and stone !";
                case "p" -> {
                    System.out.print("\033\143");
                    System.out.println(gimli);
                    System.out.println("\n...");
                    choice = sc.nextLine();
                    System.err.println(choice);
                }
                default -> {}
            }
            if (null != bs) {
                System.out.println("A " + bs.getLeft().getName() + " blocks your path.");
                System.out.println("Do you wish to engage ?\nY - Yes\nN - No (50% chance)");
                choice = sc.nextLine();
                if (choice.toLowerCase().equals("n") && Math.random() < 0.5) {
                    map.getHero().cancel();
                    bs = null;
                }
                else 
                    try {
                        while (true) {
                            BattleScene.BattleLog log = bs.nextMove();
                            while (null != log) {
                                System.out.println(log);
                                log = bs.nextMove();
                                Thread.sleep(125);
                            }
                            if (bs.getRight().getCurrent_hp() <= 0) {
                                System.out.println("YOU LOSE");
                                System.exit(0);
                            }
                            if (bs.getLeft().getCurrent_hp() <= 0) {
                                System.out.println("");
                                Item it = ItemGenerator.getInstance().generateItem(bs.getLeft().getLevel(), bs.getLeft().getLootChance(), bs.getLeft().getPower());
                                if (null != it) {
                                    System.out.println("Loot: " + it);
                                    if (null == gimli.getEquipped(it.getType()))
                                        System.out.println(it.fullDesc());
                                    else
                                        System.out.println(gimli.getEquipped(it.getType()).compare(it));
                                    System.out.println("Do you wish to equip it ?\nY - Yes\nN - No (Defaults to No)");
                                    choice = sc.nextLine();
                                        if (choice.toLowerCase().equals("y"))
                                            gimli.equip(it);
                                }
                                long exp = Math.round(bs.getLeft().getLevel() * bs.getLeft().getLootChance() * bs.getLeft().getPower()) * 15;
                                System.out.println(gimli.getName() + " gained " + exp + " Experience.");
                                gimli.grantExperience(exp);
                                map.eliminateMonster();
                                Thread.sleep(2500);
                                break ;
                            }
                        }
                    } catch (InterruptedException e) {}
            }
        }
    }
}
