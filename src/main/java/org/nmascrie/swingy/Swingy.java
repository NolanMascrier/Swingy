package org.nmascrie.swingy;

import org.nmascrie.swingy.enums.HeroClass;
import org.nmascrie.swingy.generators.ItemGenerator;
import org.nmascrie.swingy.models.Character;

/**
 *
 * @author nmascrie
 */
public class Swingy {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Character gimli = new Character("Gimli", HeroClass.GUNNER);
        System.out.println(gimli);
        System.out.println("\n###\n#\n###");
        int survive = 0;
        gimli.equip(ItemGenerator.getInstance().generateItem(5, 1, 1));

        /*while (gimli.getCurrent_hp() > 0) { 
            try {
                Creature steeve = EnemyGenerator.getInstance().generateCreature(6 + (int)(survive / 4) + gimli.getLevel());
                BattleScene scene = new BattleScene(gimli, steeve);
                BattleScene.BattleLog log = scene.nextMove();
                while (null != log) {
                    System.out.println(log);
                    log = scene.nextMove();
                    Thread.sleep(125);
                }
                if (gimli.getCurrent_hp() >= 0) {
                    Item it = ItemGenerator.getInstance().generateItem(steeve.getLevel(), steeve.getLootChance(), steeve.getPower());
                    if (null != it) {
                        System.out.println("Loot: " + it);
                        gimli.equip(it);
                    }
                    long exp = Math.round(steeve.getLevel() * steeve.getLootChance() * steeve.getPower()) * 15;
                    System.out.println(gimli.getName() + " gained " + exp + " Experience.");
                    System.out.println(gimli.getName() + " has murdered " + survive + " bugs.");
                    gimli.grantExperience(exp);
                    System.out.println(gimli);
                    System.out.println("\n###\n#\n###");
                    survive += 1;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
            }
        }*/
        System.out.println(gimli.getName() + " survived against " + survive + " enemies on Hoxxes 3 and died.");
        gimli.exports();
        Character cha = Character.imports("Gimli");
        System.out.println(cha);
    }
}
