package org.nmascrie.swingy.generators;

import java.util.concurrent.ThreadLocalRandom;

import org.nmascrie.swingy.enums.ItemType;
import org.nmascrie.swingy.enums.Rarity;
import org.nmascrie.swingy.models.Item;

/**
 * Generates item.
 */
public class ItemGenerator {
    private static ItemGenerator INSTANCE = null;

    private ItemGenerator() {}

    public static ItemGenerator getInstance() {
        if (null == INSTANCE)
            INSTANCE = new ItemGenerator();
        return INSTANCE;
    }

    /**
     * Returns a random int from 0 to n.
     */
    public int rng(int n) {
        return (int)(n * Math.random());
    }

    /**
     * Generates a random rarity for an item.
     */
    public Rarity generateRarity(float multiplier) {
        float rnd = ThreadLocalRandom.current().nextFloat() / multiplier;
        float cumulative = 0f;

        for (Rarity r : Rarity.values()) {
            cumulative += r.weight;
            if (rnd < cumulative) {
                return r;
            }
        }
        return Rarity.COMMON;
    }

    /**
     * Generates an appropriate name for each gear type.
     */
    public String generateName(ItemType type) {
        String[] weapons = {"CRSPR Flamethrower", "Cryo Cannon", "Subata 120", "Warthog Auto 210", "LOK-1 Smart Rifle", "Lead Storm Minigun", "BRT7 Burst Fire Gun", "M10000 Classic", "DRAK-25 Plasma Carbine"};
        String[] ammos = {"Hollow Point ammunitions", "Explosive ammunitions", "Infector ammunitions", "Lead bullets", "Depleted Uranium bullets"};
        String[] boots = {"Steel boots", "Muddy boots", "Lootbug slippers", "DRG-Issued boots"};
        String[] helm = {"Flare helm", "Glorious beard", "Full-faced mask", "DRG-Issued helm"};
        String[] belt = {"Security cordon", "Iron loops", "Doretta keychain", "DRG-Issued belt"};
        String[] armor = {"MkI Bodysuit", "MkII Plates", "MkIII Tanktop", "DRG-Issued vest"};

        switch (type) {
            case WEAPON -> {
                return weapons[(int)(System.currentTimeMillis() % weapons.length)];
            }
            case AMMOS -> {
                return ammos[(int)(System.currentTimeMillis() % ammos.length)];
            }
            case BOOTS -> {
                return boots[(int)(System.currentTimeMillis() % boots.length)];
            }
            case HELM -> {
                return helm[(int)(System.currentTimeMillis() % helm.length)];
            }
            case BELT -> {
                return belt[(int)(System.currentTimeMillis() % belt.length)];
            }
            case ARMOR -> {
                return armor[(int)(System.currentTimeMillis() % armor.length)];
            }
            default -> {
                return "Something weird";
            }
        }
    }

    /**
     * Generates a random item according to the level and the power of the enemy.
     */
    public Item generateItem(long level, float chance, float multiplier) {
        double rng = Math.random();
        int next = ItemGenerator.getInstance().rng(7);
        Rarity rare;
        ItemType type;
        long r_level;
        float r_value;
        String name;

        if (rng > chance)
            return null;
        rare = ItemGenerator.getInstance().generateRarity(multiplier);
        switch (next) {
            case 1 -> type = ItemType.BOOTS;
            case 2 -> type = ItemType.ARMOR;
            case 3 -> type = ItemType.BELT;
            case 4 -> type = ItemType.AMMOS;
            case 5 -> type = ItemType.HELM;
            default -> type = ItemType.WEAPON;
        }
        name = ItemGenerator.getInstance().generateName(type);
        r_level = level + ItemGenerator.getInstance().rng(5) - 3;
        if (r_level < 1)
            r_level = 1;
        if (type == ItemType.BOOTS || type == ItemType.AMMOS) {
            r_value = r_level * (0.01f + 0.0005f * (ItemGenerator.getInstance().rng(5) - 3));
            if (r_value > 0.85f)
                r_value = 0.85f;
        }
        else {
            r_value = r_level * (4f + ItemGenerator.getInstance().rng(6) - 3);
        }
        return new Item(name, r_level, r_value, type, rare);
    }
}