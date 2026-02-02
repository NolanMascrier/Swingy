package org.nmascrie.swingy.enums;

/**
 * Describes the result of an attack.
 */
public final class AttackResult {
    private final AttackOutcome outcome;
    private final long damage;

    private AttackResult(AttackOutcome outcome, long damage) {
        this.outcome = outcome;
        this.damage = damage;
    }

    public static AttackResult hit(long damage) {
        return new AttackResult(AttackOutcome.HIT, damage);
    }

    public static AttackResult crit(long damage) {
        return new AttackResult(AttackOutcome.CRIT, damage);
    }

    public static AttackResult dodge() {
        return new AttackResult(AttackOutcome.DODGE, 0);
    }

    public static AttackResult kill(long damage) {
        return new AttackResult(AttackOutcome.KILL, damage);
    }

    public static AttackResult block() {
        return new AttackResult(AttackOutcome.BLOCK, 0);
    }

    public AttackOutcome getOutcome() {
        return outcome;
    }

    public long getDamage() {
        return damage;
    }
}