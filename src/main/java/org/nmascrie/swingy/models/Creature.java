package org.nmascrie.swingy.models;

import java.io.Serializable;

import org.nmascrie.swingy.enums.AttackResult;

/**
 * Creature class, that holds the data for both players and enemies. 
 */
public class Creature implements Serializable{
    private String imgID = "";
    protected String name;
    protected long level = 1;
    protected long experience = 0;
    protected long hitpoints = 10;
    protected long current_hp = 10;
    protected long attack = 1;
    protected long defense = 1;
    protected long speed = 1;
    protected float crit = 0.05f;
    protected float dodge = 0.05f;
    protected char desc = 'o';
    private float power = 0f;
    private float lootChance = 0f;

    /**
     * Defines a basic creature.
     * 
     * @param c_name Name of the creature.
     */
    public Creature(String c_name) {
        this.name = c_name;
    }

    /**
     * Defines a basic creature.
     * 
     * @param c_name Name of the creature.
     * @param c_level Level of the creature.
     * @param c_atk Attack power of the creature.
     * @param c_def Defense power of the creature.
     * @param c_hp Hitpoints of the creature.
     * @param c_speed Speed of the creature.
     */
    public Creature(String c_name, long c_level, long c_atk, long c_def, long c_hp, long c_speed) {
        this.name = c_name;
        this.level = c_level;
        this.hitpoints = c_hp;
        this.current_hp = c_hp;
        this.defense = c_def;
        this.attack = c_atk;
        this.speed = c_speed;
    }

    /**
     * Defines a basic creature.
     * 
     * @param c_name Name of the creature.
     * @param c_level Level of the creature.
     * @param c_atk Attack power of the creature.
     * @param c_def Defense power of the creature.
     * @param c_hp Hitpoints of the creature.
     * @param c_speed Speed of the creature.
     * @param c_power Monster rarity multiplier.
     * @param c_char Character of ASCII display.
     * @param c_crit Crit chance of the creature.
     * @param c_dodge Dodge chance of the creature.
     */
    public Creature(String c_name, long c_level, long c_atk, long c_def, long c_hp, long c_speed, float c_power,
                    float c_loot, char c_char, float c_crit, float c_dodge) {
        this.name = c_name;
        this.level = c_level;
        this.hitpoints = c_hp;
        this.current_hp = c_hp;
        this.defense = c_def;
        this.attack = c_atk;
        this.speed = c_speed;
        this.power = c_power;
        this.lootChance = c_loot;
        this.desc = c_char;
        this.crit = c_crit;
        this.dodge = c_dodge;
    }

    /**
     * Defines a basic creature.
     * 
     * @param c_name Name of the creature.
     * @param c_level Level of the creature.
     * @param c_atk Attack power of the creature.
     * @param c_def Defense power of the creature.
     * @param c_hp Hitpoints of the creature.
     * @param c_speed Speed of the creature.
     * @param c_power Monster rarity multiplier.
     * @param c_char Character of ASCII display.
     * @param c_crit Crit chance of the creature.
     * @param c_dodge Dodge chance of the creature.
     * @param c_id name of the Image file.
     */
    public Creature(String c_name, long c_level, long c_atk, long c_def, long c_hp, long c_speed, float c_power,
                    float c_loot, char c_char, float c_crit, float c_dodge, String c_id) {
        this.name = c_name;
        this.level = c_level;
        this.hitpoints = c_hp;
        this.current_hp = c_hp;
        this.defense = c_def;
        this.attack = c_atk;
        this.speed = c_speed;
        this.power = c_power;
        this.lootChance = c_loot;
        this.desc = c_char;
        this.crit = c_crit;
        this.dodge = c_dodge;
        this.imgID = c_id;
    }

    /**
     * Returns the needed experience for the next level.
     */
    protected long calculateNeededExperience() {
        return (long)(Math.round(this.level * 1000 + 450 * Math.pow(this.level - 1, 2)));
    }

    /**
     * Grants the creature experience, and gains level when needed.
     * On level up, the creature gains a 10% HP buff and a full heal.
     * 
     * @param exp Granted experience.
     */
    public void grantExperience(long exp) {
        long next = this.calculateNeededExperience();

        this.experience += exp;
        while (this.experience >= next) {
            this.level += 1;
            this.experience -= next;
            next = this.calculateNeededExperience();
            this.hitpoints *= 1.05;
            this.current_hp = this.getHitpoints();
        }
    }

    /**
     * Deals the creature damage, or possibly heals it. Returns whether or not the creature was
     * killed by said damage.
     * 
     * @param dmg Power of the damage.
     * @return `true` if the creature is killed, `false` otherwise.
     */
    public boolean damage(long dmg) {
        if (dmg == 0)
            return false;
        this.current_hp -= dmg;
        if (this.current_hp >= this.getHitpoints())
            this.current_hp = this.getHitpoints();
        return (this.current_hp <= 0);
    }

    /**
     * Attacks the targeted creature.
     * 
     * @param target Creature to attack.
     */
    public AttackResult attack(Creature target) {
        boolean crit_roll = Math.random() <= this.getCrit();
        boolean dodge_roll = Math.random() <= this.getDodge();
        boolean is_kill;
        double rng_variation;
        long dmg;

        if (dodge_roll && !crit_roll)
            return AttackResult.dodge();
        dmg = this.getAttack();
        if (crit_roll)
            dmg *= 2;
        rng_variation = Math.random() / 5 + 0.95;
        dmg = Math.round(dmg * rng_variation);
        if (dmg <= target.getDefense()) {
            is_kill = target.damage(1);
            if (is_kill)
                return AttackResult.kill(1);
            return AttackResult.block();
        }
        dmg -= target.getDefense();
        is_kill = target.damage(dmg);
        if (is_kill)
            return AttackResult.kill(dmg);
        if (crit_roll)
            return AttackResult.crit(dmg);
        return AttackResult.hit(dmg);
    }

    /**
     * Returns how much EXP is the creature worth.
     */
    public long calculateExp() {
        long exp = Math.round(
                this.getLevel() * 
                this.getLootChance() * 
                this.getPower()
            ) * 125;
        
        return Math.max(Math.round(exp * (Math.random() / 10 + 0.95)), 1);
    }

    @Override
    public String toString() {
        return this.name + ", level " + this.level + "(" + this.getExperience() + "/" + this.calculateNeededExperience() + ")" +
            "\nHIT POINTS: " + this.getCurrent_hp() + "/" + this.getHitpoints() +
            "\nPOWER: " + this.getAttack() +"\nDEFENSE: " + this.getDefense() +
            "\nSPEED: " + this.getSpeed() +"\nDODGE CHANCE: " + this.getDodge() * 100 + "%" +
            "\nCRIT CHANCE: " + this.getCrit() * 100 +"%";
    }

    public String getImageID() {
        return this.imgID;
    }

    public float getCrit() {
        return crit;
    }

    public float getDodge() {
        return dodge;
    }

    public String getName() {
        return name;
    }

    public long getLevel() {
        return level;
    }

    public long getExperience() {
        return experience;
    }

    public long getHitpoints() {
        return hitpoints;
    }

    public long getCurrent_hp() {
        return current_hp;
    }

    public long getAttack() {
        return attack;
    }

    public long getDefense() {
        return defense;
    }

    public long getSpeed() {
        return speed;
    }

    public char getDesc() {
        return desc;
    }

    public float getPower() {
        return power;
    }

    public float getLootChance() {
        return lootChance;
    }
}