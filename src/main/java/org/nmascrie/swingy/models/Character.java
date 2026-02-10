package org.nmascrie.swingy.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.nmascrie.swingy.enums.HeroClass;
import org.nmascrie.swingy.enums.ItemType;

/**
 * A character is a specific creature that has a class and items.
 * It is meant to be the player character.
 */
public class Character extends Creature implements Serializable {
    private HeroClass classe = HeroClass.SCOUT;
    private Item helmet = null; //GIVES HEALTH
    private Item armor = null; //GIVES DEFENSE
    private Item weapon = null; //GIVES ATTACK
    private Item boots = null; //GIVES DODGE
    private Item belt = null; //GIVES SPEED
    private Item ammos = null; //GIVES CRIT
    public int kills = 0;
    public int explored = 0;

    public Character(String c_name, HeroClass hc) {
        super(c_name, 1, hc.atk, hc.def, hc.hp, hc.speed);
        this.classe = hc;
        this.crit = hc.crit;
        this.dodge = hc.dodge;
    }

    /**
     * Clamps the current life value to avoid overflowing.
     */
    private void clampLife() {
        this.current_hp = Math.min(this.current_hp, this.getHitpoints());
    }

    /**
     * Equips the given item. If the slot was taken, empties it and returns the 
     * removed item. Can return null !
     * 
     * @param it Item to equip.
     * @return Currently equiped item, or null.
     */
    public Item equip(Item it) {
        Item buffer = null;
        
        if (null == it)
            return null;
        switch (it.getType()) {
            case HELM -> {
                if (null != this.helmet)
                    buffer = this.helmet;
                this.helmet = it;
            }
            case ARMOR -> {
                if (null != this.armor)
                    buffer = this.armor;
                this.armor = it;
            }
            case WEAPON -> {
                if (null != this.weapon)
                    buffer = this.weapon;
                this.weapon = it;
            }
            case BOOTS -> {
                if (null != this.boots)
                    buffer = this.boots;
                this.boots = it;
            }
            case BELT -> {
                if (null != this.belt)
                    buffer = this.belt;
                this.belt = it;
            }
            case AMMOS -> {
                if (null != this.ammos)
                    buffer = this.ammos;
                this.ammos = it;
            }
            default -> {
                return null;
            }
        }
        this.clampLife();
        return buffer;
    }

    /**
     * Removes an item from the given slot.
     * 
     * @param type Reference of the slot.
     * @return Currently equiped item, or null.
     */
    public Item unequip(ItemType type) {
        Item buffer = null;

        switch (type) {
            case HELM -> {
                if (null != this.helmet) {
                    buffer = this.helmet;
                    this.helmet = null;
                }
            }
            case ARMOR -> {
                if (null != this.armor) {
                    buffer = this.armor;
                    this.armor = null;
                }
            }
            case WEAPON -> {
                if (null != this.boots) {
                    buffer = this.boots;
                    this.boots = null;
                }
            }
            case BOOTS -> {
                if (null != this.boots) {
                    buffer = this.boots;
                    this.boots = null;
                }
            }
            case BELT -> {
                if (null != this.belt) {
                    buffer = this.belt;
                    this.belt = null;
                }
            }
            case AMMOS -> {
                if (null != this.ammos) {
                    buffer = this.ammos;
                    this.ammos = null;
                }
            }
        }
        this.clampLife();
        return buffer;
    }

    public Item getEquipped(ItemType type) {
        switch (type) {
            case HELM -> {
                return this.helmet;
            }
            case ARMOR -> {
                return this.armor;
            }
            case WEAPON -> {
                return this.weapon;
            }
            case BOOTS -> {
                return this.boots;
            }
            case BELT -> {
                return this.belt;
            }
            case AMMOS -> {
                return this.ammos;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.classe.name +
            " level " + this.level + "(" + this.getExperience() + "/" + this.calculateNeededExperience() + ")" +
            "\nHIT POINTS: " + this.getCurrent_hp() + "/" + this.getHitpoints() +
            "\nPOWER: " + this.getAttack() +"\nDEFENSE: " + this.getDefense() +
            "\nSPEED: " + this.getSpeed() +"\nDODGE CHANCE: " + this.getDodge() * 100 + "%" +
            "\nCRIT CHANCE: " + this.getCrit() * 100 +"%" +
            "\nWEAPON: " + ((null != this.weapon) ? this.weapon : "Knuckle Sandwich") + 
            "\nAMMOS: " + ((null != this.ammos) ? this.ammos : "Powerful fingers") + 
            "\nHELM: " + ((null != this.helmet) ? this.helmet : "Most glorious beard") +
            "\nARMOR: " + ((null != this.armor) ? this.armor : "Glory laid bare") + 
            "\nBELT: " + ((null != this.belt) ? this.belt : "Beard pouch") + 
            "\nBOOTS: " + ((null != this.boots) ? this.boots : "Powerful dwarven toes");
    }

    /**
     * Exports the character as a save file.
     */
    public void exports() {
        File path = new File("saves/" + this.getName() + ".dwarf");
        try (ObjectOutputStream write = new ObjectOutputStream (new FileOutputStream(path))) {
            write.writeObject(this);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static Character imports(String char_name) {
        File path = new File("saves/" + char_name);
        Object data = null;

        try(ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path)))
        {
            data = inFile.readObject();
            return (Character)data;
        } catch(Exception e)
        {
            System.err.println(e);
        }
        return (Character)data;
    }

    /**
     * Let the character speak.
     */
    public String speak(String msg) {
        return this.getName() + " says: " + msg;
    }

    /**
     * Says a little something on mission start.
     */
    public String startMissionQip() {
        String[] qips = {
            "Oh, there's a pebble in me boot",
            "We're all gonna die!",
            "I've got a bad feeling about this op.",
            "Feel like we're digging our own grave!",
            "I hate this damn planet!",
            "Only danger and darkness awaits.",
            "Damn, I forgot me lucky charm.",
            "This is gonna be a walk in the park!",
            "Here we go again!",
            "Here we goooo!",
            "Time to collect me paycheck.",
            "That's it, I'm retiring after that one."
        };

        return this.speak(qips[(int)System.currentTimeMillis() % qips.length]);
    }

    /**
     * Says a little something on action.
     */
    public String actionQip() {
        String[] qips = {
            "That's it lads, Rock and Stone!",
            "Rock and Stone!",
            "STONE !",
            "Did I hear a Rock and Stone?",
            "I think we actually have a chance to make it out alive!",
            "This is a friggin' hellhole! ",
            "I'm having the best time in my life right now!",
            "Next time, let's go somewhere nice!",
            "Oh yeah!",
            "Watch me now!"
        };

        return this.speak(qips[(int)System.currentTimeMillis() % qips.length]);
    }

    /**
     * Says a little something on action.
     */
    public String digQip() {
        String[] qips = {
            "Diggy Diggity!",
            "Digging up!",
            "Booyah!",
            "Eat it!",
            "Whack-a-Mole!",
            "Splat!",
            "Taste the pickaxe!",
            "Ow! Me back!",
            "Oh yeah!",
            "Watch me now!"
        };

        return this.speak(qips[(int)System.currentTimeMillis() % qips.length]);
    }

    public HeroClass getClasse() {
        return classe;
    }

    @Override
    public float getCrit() {
        if (null != this.ammos)
            return crit + this.ammos.getValue();
        return crit;
    }

    @Override
    public float getDodge() {
        if (null != this.boots)
            return dodge + this.boots.getValue();
        return dodge;
    }

    @Override
    public long getHitpoints() {
        if (null != this.helmet)
            return Math.round(hitpoints + this.helmet.getValue());
        return hitpoints;
    }

    @Override
    public long getAttack() {
        if (null != this.weapon)
            return Math.round(attack + this.weapon.getValue());
        return attack;
    }

    @Override
    public long getDefense() {
        if (null != this.armor)
            return Math.round(defense + this.armor.getValue());
        return defense;
    }

    @Override
    public long getSpeed() {
        if (null != this.belt)
            return Math.round(speed + this.belt.getValue());
        return speed;
    }
}