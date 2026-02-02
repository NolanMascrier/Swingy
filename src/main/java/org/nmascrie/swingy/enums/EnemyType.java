package org.nmascrie.swingy.enums;

/**
 * Enemy template
 */
public enum EnemyType {
    SWARMER(MonsterPower.TRASH, "swarmer", 20, 4, 2, 18, 0.01f, 0.1f, 's', 0.6f),
    SPAWN(MonsterPower.TRASH, "spawn", 15, 2, 1, 38, 0.01f, 0.15f, 'y', 0.4f),
    GRUNT(MonsterPower.NORMAL, "grunt", 50, 10, 10, 10, 0.05f, 0.05f, 'G', 0.5f),
    SLASHER(MonsterPower.NORMAL, "slasher", 40, 13, 5, 10, 0.1f, 0.02f, 'L', 0.25f),
    GUARD(MonsterPower.NORMAL, "guard", 80, 10, 13, 8, 0.05f, 0.01f, 'T', 0.25f),
    STALKER(MonsterPower.ELITE, "stalker", 30, 15, 2, 20, 0.1f, 0.25f, 'i', 0.33f),
    PRAETORIAN(MonsterPower.ELITE, "praetorian", 100, 12, 20, 5, 0.01f, 0.01f, 'P', 0.67f),
    OPPRESSOR(MonsterPower.BOSS, "oppressor", 250, 15, 20, 6, 0.01f, 0.01f, 'O', 1f);

    public final MonsterPower power;
    public final String name;
    public final int hp;
    public final int atk;
    public final int def;
    public final int speed;
    public final float crit;
    public final float dodge;
    public final char desc;
    public final float weight;

    private EnemyType(MonsterPower mp, String name, int hp, int atk, int def, int speed, float crit, float dodge, char desc, float weight) {
        this.power = mp;
        this.name = name;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.speed = speed;
        this.crit = crit;
        this.dodge = dodge;
        this.desc = desc;
        this.weight = weight;
    }
}