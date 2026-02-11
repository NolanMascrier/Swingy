package org.nmascrie.swingy.generators;

import java.util.concurrent.ThreadLocalRandom;

import org.nmascrie.swingy.enums.EnemyType;
import org.nmascrie.swingy.enums.MonsterPower;
import org.nmascrie.swingy.models.Creature;

public class EnemyGenerator {
    private static EnemyGenerator INSTANCE = null;

    private EnemyGenerator() {}

    public static EnemyGenerator getInstance() {
        if (null == INSTANCE)
            INSTANCE = new EnemyGenerator();
        return INSTANCE;
    }

    /**
     * Generates a random rarity for an enemy.
     */
    public MonsterPower generateRarity(float multiplier) {
        float rnd = ThreadLocalRandom.current().nextFloat();
        float cumulative = 0f;

        for (MonsterPower r : MonsterPower.values()) {
            cumulative += r.weight;
            if (rnd < cumulative) {
                return r;
            }
        }
        return MonsterPower.TRASH;
    }

    /**
     * Returns a random enemy for the given power.
     */
    public EnemyType randomEnemyOfPower(MonsterPower power) {
        float rnd = ThreadLocalRandom.current().nextFloat();
        float cumulative = 0f;

        for (EnemyType e : EnemyType.values()) {
            if (e.power == power) {
                cumulative += e.weight;
                if (rnd < cumulative) {
                    return e;
                }
            }
        }
        return EnemyType.GRUNT;
    }

    /**
     * Generates a creature of the given level.
     */
    public Creature generateCreature(long level) {
        MonsterPower power = EnemyGenerator.getInstance().generateRarity(level / 6);
        EnemyType type = EnemyGenerator.getInstance().randomEnemyOfPower(power);
        String name = power.desc + " " + type.name;
        long atk = Math.round(type.atk * (1 + (level - 1) / 7) * (Math.random() / 5 + 0.95));
        long def = Math.round(type.def * (1 + (level - 1) / 7) * (Math.random() / 5 + 0.95));
        long hp = Math.round(type.hp * (1 + (level - 1) / 3) * (Math.random() / 4 + 0.95));
        long speed = Math.round(type.speed * (1 + (level - 1) / 7) * (Math.random() / 5 + 0.95));
        float crit = (float)Math.min(type.crit + Math.pow(type.crit, 10 / (level - 0.99)), 0.9);
        float dodge = (float)Math.min(type.dodge + (float)Math.pow(type.dodge, 10 / (level - 0.99)), 0.9);

        return new Creature(name, level, atk, def, hp, speed, power.dropMultiplier, power.dropChance, type.desc, crit, dodge, type.name);
    }
}