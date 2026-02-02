package org.nmascrie.swingy.models;

/**
 * Vector to hold 2D positions
 */
class Vec2D {
    public float x;
    public float y;

    public Vec2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Displace the vector by a x;y difference.
     * 
     * @param dx Delta X
     * @param dy Delta Y
     */
    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Adds two vector.
     * 
     * @param vec Second vector to add.
     */
    public void move(Vec2D vec) {
        this.x += vec.x;
        this.y += vec.y;
    }

    /**
     * Calculates the distance between two points.
     * 
     * @param x x of the second point
     * @param y y of the second point
     * @return int - Computed distance
     */
    public float dist(float x, float y) {
        return (float)Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
    }

    /**
     * Calculates the distance between two points.
     * 
     * @param vec Second point
     * @return int - Computed distance
     */
    public float dist(Vec2D vec) {
        return (float)Math.sqrt(Math.pow(this.x - vec.x, 2) + Math.pow(this.y - vec.y, 2));
    }

    @Override
    public String toString() {
        return "(" + this.x + ";" + this.y + ")";
    }
}