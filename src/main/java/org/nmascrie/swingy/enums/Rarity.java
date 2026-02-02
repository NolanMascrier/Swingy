package org.nmascrie.swingy.enums;

/**
 * Rarity for items and enemies.
 */
public enum Rarity {
    COMMON(1f, "Stock", 0.815f),
    RARE(1.1f, "Experienced", 0.11f),
    LEGENDARY(1.3f, "Masterpiece", 0.05f),
    ULTIMATE(1.7f, "Dwarvenforged", 0.02f),
    SUPREME(2.2f, "Omega", 0.005f);

    public final float multiplier;
    public final String desc;
    public final float weight;

    private Rarity(float multiplier, String desc, float weight) {
        this.multiplier = multiplier;
        this.desc = desc;
        this.weight = weight;
    }
}