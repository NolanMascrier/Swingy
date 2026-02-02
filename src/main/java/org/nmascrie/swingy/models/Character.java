package org.nmascrie.swingy.models;

import org.nmascrie.swingy.enums.HeroClass;
import org.nmascrie.swingy.enums.ItemType;

/**
 * A character is a specific creature that has a class and items.
 * It is meant to be the player character.
 */
public class Character extends Creature {
    private HeroClass classe = HeroClass.SCOUT;
    private Item helmet = null; //GIVES HEALTH
    private Item armor = null; //GIVES DEFENSE
    private Item weapon = null; //GIVES ATTACK
    private Item boots = null; //GIVES DODGE
    private Item belt = null; //GIVES SPEED
    private Item ammos = null; //GIVES CRIT

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

    @Override
    public String toString() {
        return this.name + ", " + this.classe.name +
            " level " + this.level + "(" + this.getExperience() + "/" + this.calculateNeededExperience() + ")" +
            "\nHIT POINTS: " + this.getCurrent_hp() + "/" + this.getHitpoints() +
            "\nPOWER: " + this.getAttack() +"\nDEFENSE: " + this.getDefense() +
            "\nSPEED: " + this.getSpeed() +"\nDODGE CHANCE: " + this.getDodge() * 100 + "%" +
            "\nCRIT CHANCE: " + this.getCrit() * 100 +"%" +
            "\nWEAPON: " + this.weapon + "\nAMMOS: " + this.ammos + "\nHELM: " + this.helmet +
            "\nARMOR: " + this.armor + "\nBELT: " + this.belt + "\nBOOTS: " + this.boots;
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