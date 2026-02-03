package org.nmascrie.swingy.enums;

/**
 * FOR ROCK AND STONE
 */
public enum HeroClass {
    SCOUT(80, 10, 8, 20, 0.05f, 0.12f, "Scout"),
    DRILLER(120, 25, 5, 10, 0.05f, 0.04f, "Driller"),
    GUNNER(150, 15, 20, 8, 0.05f, 0.03f, "Gunner"),
    ENGINEER(100, 12, 12, 12, 0.1f, 0.05f, "Engineer");

    public final int hp;
    public final int atk;
    public final int def;
    public final int speed;
    public final float crit;
    public final float dodge;
    public final String name;

    private HeroClass(int hp, int atk, int def, int speed, float crit, float dodge, String name) {
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.speed = speed;
        this.crit = crit;
        this.dodge = dodge;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}