package org.nmascrie.swingy.enums;

public enum MonsterPower {
    TRASH(0.2f, 1f, "Pathetic", 0.25f),
    NORMAL(0.5f, 1.5f, "Glyphid", 0.55f),
    ELITE(0.9f, 2f, "Elite", 0.15f),
    BOSS(1f, 3f, "Alpha", 0.05f);

    public final float dropMultiplier;
    public final float dropChance;
    public final float weight;
    public final String desc;

    private MonsterPower(float dropChance, float dropMultiplier, String desc, float weight) {
        this.dropMultiplier = dropMultiplier;
        this.dropChance = dropChance;
        this.weight = weight;
        this.desc = desc;
    }
}