package org.nmascrie.swingy.models;

public class Cell {
    private final Vec2D position;
    private boolean isWall = false;

    public Cell(float x, float y) {
        this.position = new Vec2D(x, y);
    }

    public Cell(float x, float y, boolean wall) {
        this.position = new Vec2D(x, y);
        this.isWall = wall;
    }

    public Vec2D getPosition() {
        return position;
    }

    public boolean isAWall() {
        return isWall;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }
}