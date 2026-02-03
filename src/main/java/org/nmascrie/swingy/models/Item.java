package org.nmascrie.swingy.models;

import java.io.Serializable;

import org.nmascrie.swingy.enums.ItemType;
import org.nmascrie.swingy.enums.Rarity;

/**
 * Class for items.
 */
public class Item implements Serializable {
    private long level;
    private float value;
    private Rarity rarity = Rarity.COMMON;
    private ItemType type = ItemType.RUBBISH;
    private String name;

    /**
     * Defines a basic item. 
     * 
     * @param i_name Name of the item.
     * @param i_lvl Level of the item.
     * @param i_value Power value of the item (ie how much attack power and defense it gives.)
     */
    public Item(String i_name, long i_lvl, float i_value) {
        this.level = i_lvl;
        this.name = i_name;
        this.value = i_value * this.rarity.multiplier;
    }

    /**
     * Defines a basic item and its rarity. Rarity automatically scales the value.
     * 
     * @param i_name Name of the item.
     * @param i_lvl Level of the item.
     * @param i_value Power value of the item (ie how much attack power and defense it gives.)
     * @param i_type Type of the item.
     * @param i_rarity Rarity of the item.
     */
    public Item(String i_name, long i_lvl, float i_value, ItemType i_type, Rarity i_rarity) {
        this.level = i_lvl;
        this.name = i_name;
        this.rarity = i_rarity;
        this.value = i_value * this.rarity.multiplier;
        this.type = i_type;
    }

    /**
     * @return Level of the item.
     */
    public long getLevel() {
        return level;
    }

    /**
     * @return Power value of the item.
     */
    public float getValue() {
        return value;
    }

    /**
     * @return Rarity of the item.
     */
    public Rarity getRarity() {
        return rarity;
    }

    /**
     * @return Type of the item.
     */
    public ItemType getType() {
        return type;
    }

    /**
     * @return Name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the level of an item.
     * 
     * @param level New level of the item.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Sets the power value of an item.
     * 
     * @param value New power value of the item.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Sets the rarity of an item.
     * 
     * @param rarity New rarity of the item.
     */
    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    /**
     * Sets the type of an item.
     * 
     * @param type New type of the item.
     */
    public void setType(ItemType type) {
        this.type = type;
    }

    /**
     * Sets the name of an item.
     * 
     * @param name New name of the item.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + " (lvl " + this.level + " " + this.rarity.desc + " " + this.type.desc + ")";
    }
}