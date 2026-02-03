package org.nmascrie.swingy.models;

/**
 * An entity is a class that associates a creature to a position.
 */
public class Entity {
    private final Creature creature;
    private final Vec2D position;

    public Entity(Creature c, float x, float y) {
        this.creature = c;
        this.position = new Vec2D(x, y);
    }

    public Entity(Creature c, Vec2D vec) {
        this.creature = c;
        this.position = vec;
    }

    /**
     * Moves the entity up.
     */
    public void up() {
        this.position.move(0, -1);
    }

    /**
     * Moves the entity down.
     */
    public void down() {
        this.position.move(0, 1);
    }

    /**
     * Moves the entity left.
     */
    public void left() {
        this.position.move(-1, 0);
    }

    /**
     * Moves the entity right.
     */
    public void right() {
        this.position.move(1, 0);
    }

    /**
     * Cancels the entity's last movement.
     */
    public void cancel() {
        this.position.revert();
    }

    /**
     * Returns whether or not the position corresponds to the entity.
     */
    public boolean isHere(float x, float y) {
        return (this.position.x == x && this.position.y == y);
    }

    /**
     * Fights another entity.
     */
    public BattleScene clash(Entity other) {
        return new BattleScene(this.creature, other.creature);
    }

    public Creature getCreature() {
        return creature;
    }

    public Vec2D getPosition() {
        return position;
    }
}