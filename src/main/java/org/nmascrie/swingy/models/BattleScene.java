package org.nmascrie.swingy.models;

import org.nmascrie.swingy.enums.AttackResult;


/**
 * A battle scene pits two creature against one another.
 */
public class BattleScene {
    private final Creature right;
    private final Creature left;
    private int turns = 1;
    private long initiative_l = 1;
    private long initiative_r = 1;

    public BattleScene(Creature a, Creature b) {
        this.right = a;
        this.left = b;
        this.initiative_l = a.getSpeed() * b.getSpeed();
        this.initiative_r = a.getSpeed() * b.getSpeed();
    }

    public Creature getRight() {
        return right;
    }

    public Creature getLeft() {
        return left;
    }

    /**
     * Log object containing the data of the attack and possible messages.
     */
    public class BattleLog {
        public AttackResult res;
        public Creature attacker;
        public Creature attacked;
        public String log = null;

        public BattleLog(AttackResult res, Creature attacker, Creature attacked) {
            this.res = res;
            this.attacker = attacker;
            this.attacked = attacked;
        }

        /**
         * Returns the battle log as a string.
         */
        @Override
        public String toString() {
            String logged = "";

            switch (this.res.getOutcome()) {
                case HIT -> logged += this.attacker.getName() + " attacks " + this.attacked.getName() + " and deals " + this.res.getDamage() + " damage.";
                case CRIT -> logged += this.attacker.getName() + " lands a devastating hit on " + this.attacked.getName() + " and deals " + this.res.getDamage() + " damage!";
                case DODGE -> logged += this.attacker.getName() + " attacks, but " + this.attacked.getName() + " dodged.";
                case BLOCK -> logged += this.attacker.getName() + " attacks, but " + this.attacked.getName() + " blocked the hit and took only 1 damage.";
                case KILL -> logged += this.attacker.getName() + " puts " + this.attacked.getName() + " out of their misery.";
            }
            if (null != this.log)
                logged += "\n" + this.log;
            return logged;
        }
    }
    /**
     * Moves on the battle.
     */
    public BattleLog nextMove() {
        AttackResult res;
        BattleLog log;

        if (this.left.getCurrent_hp() <= 0 || this.right.getCurrent_hp() <= 0)
            return null;
        if (this.initiative_l == this.initiative_r) {
            if (this.left.getSpeed() > this.right.getSpeed()) {
                res = this.left.attack(this.right);
                this.initiative_l -= this.right.getSpeed();
                log = new BattleLog(res, this.left, this.right);
            }
            else {
                res = this.right.attack(this.left);
                this.initiative_r -= this.left.getSpeed();
                log = new BattleLog(res, this.right, this.left);
            }
        }
        else if (this.initiative_l > this.initiative_r) {
            res = this.left.attack(this.right);
            this.initiative_l -= this.right.getSpeed();
            log = new BattleLog(res, this.left, this.right);
        }
        else {
            res = this.right.attack(this.left);
            this.initiative_r -= this.left.getSpeed();
            log = new BattleLog(res, this.right, this.left);
        }
        if (this.initiative_l <= 0 && this.initiative_r <= 0) {
            this.initiative_l = right.getSpeed() * left.getSpeed();
            this.initiative_r = right.getSpeed() * left.getSpeed();
            this.turns += 1;
            log.log = "\nTurn " + this.turns + " has started.";
        }
        return log;
    }
}