package org.nmascrie.swingy.models;

import java.util.ArrayList;
import java.util.HashMap;

import org.nmascrie.swingy.generators.EnemyGenerator;

public class Map {
    private final ArrayList<Cell> mapCells;
    private final HashMap<Pos, Entity> entityLst;
    private final long size;
    private Entity hero;


    public Map(long size, Character hero, long level) {

        this.size = size;
        this.mapCells = new ArrayList<>();
        this.entityLst = new HashMap<>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                this.mapCells.add(new Cell(x, y, true));
            }
        }
        this.generateTunnels(hero);
        this.generateMonsters(level);
    }

    /**
     * Generate random monsters on the map.
     */
    private void generateMonsters(long level) {
        long max = Math.max(size * size / 25, 15);
        long min = Math.max(size * size / 50, 5);
        long count = (int)(Math.random() * (max - min)) + min;

        int placed = 0;
        int attempts = 0;

        while (placed < count && attempts < count * 10) {
            attempts++;

            int x = (int)(Math.random() * size);
            int y = (int)(Math.random() * size);

            if (!isFree(x, y))
                continue;

            Creature monster = EnemyGenerator.getInstance()
                                                .generateCreature(level);

            Entity entity = new Entity(monster, x, y);
            this.entityLst.put(new Pos(x, y), entity);
            placed++;
        }
    }

    /**
     * Generate walkable tunnels inside the map.
     */
    private void generateTunnels(Character hero) {
        long cx = this.size / 2;
        long cy = this.size / 2;
        int dx;
        int dy;
        int tunnels = (int)(this.size / 5 + 1);

        this.get(cx, cy).setWall(false);
        this.hero = new Entity(hero, cx, cy);
        this.carveTunnel(cx, cy);
        for (int i = 1; i < tunnels; i++) {
            dx = (int)(Math.random() * (this.size - 1) + 1);
            dy = (int)(Math.random() * (this.size - 1) + 1);
            if (i % 3 == 0)
                this.carveTunnel(cx, cy);
            else
                this.carveTunnel(dx, dy);
        }
    }

    /**
     * Carves a random single tunnel from the starting point to a border.
     */
    private void carveTunnel(long startX, long startY) {
        long x = startX;
        long y = startY;
        int lastDir = -1;
        int target = pickTargetSide();
        int dir;
        int prefDir;

        while (!isBorder(x, y)) {
            this.get(x, y).setWall(false);
            if (x + 1 < this.size)
                this.get(x + 1, y).setWall(false);
            if (y + 1 < this.size)
                this.get(x, y + 1).setWall(false);
            if (y + 1 < this.size && x + 1 < this.size)
                this.get(x + 1, y + 1).setWall(false);

            prefDir = target;

            if (lastDir != -1 && Math.random() < 0.55f) {
                dir = prefDir;
                if (opposite(lastDir) == dir)
                    dir = lastDir;
            } else {
                do {
                    dir = (int)(Math.random() * 4);
                } while (dir == opposite(lastDir));
            }
            lastDir = dir;
            switch (dir) {
                case 0 -> x++;
                case 1 -> x--;
                case 2 -> y++;
                case 3 -> y--;
            }
            x = Math.max(0, Math.min(size - 1, x));
            y = Math.max(0, Math.min(size - 1, y));
        }
        this.get(x, y).setWall(false);
    }

    /**
     * Returns the opposite of the given direction. Used for cavern generation.
     */
    private int opposite(int dir) {
        return switch (dir) {
            case 0 -> 1;
            case 1 -> 0;
            case 2 -> 3;
            case 3 -> 2;
            default -> -1;
        };
    }

    /**
     * Returns a random direction for the cavern generation.
     */
    private int pickTargetSide() {
        return (int)(Math.random() * 4);
    }

    /**
     * Checks if the given cell is a border.
     */
    private boolean isBorder(long x, long y) {
        return x == 0 || y == 0 || x == this.size - 1 || y == this.size - 1;
    }

    /**
     * Returns the cell at the given position.
     */
    public Cell get(float x, float y) {
        int idx = (int)(y * this.size + x);

        try {
            return this.mapCells.get(idx);  
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns whether or not the player has reached a border.
     */
    public boolean victoryCondition() {
        return this.hero.getPosition().x == 0 || this.hero.getPosition().x == this.size - 1 ||
               this.hero.getPosition().y == 0 || this.hero.getPosition().y == this.size - 1;
    }

    /**
     * Digs a 3x3 square around the hero. ROCK AND STONE.
     */
    public void dig() {
        if (this.hero.getPosition().x - 1 >= 0)
            this.get(this.hero.getPosition().x - 1, this.hero.getPosition().y).setWall(false);
        if (this.hero.getPosition().x + 1 <= this.size)
            this.get(this.hero.getPosition().x + 1, this.hero.getPosition().y).setWall(false);
        if (this.hero.getPosition().y - 1 >= 0) {
            if (this.hero.getPosition().x - 1 >= 0)
                this.get(this.hero.getPosition().x - 1, this.hero.getPosition().y - 1).setWall(false);
            this.get(this.hero.getPosition().x, this.hero.getPosition().y - 1).setWall(false);
            if (this.hero.getPosition().x + 1 <= this.size)
                this.get(this.hero.getPosition().x + 1, this.hero.getPosition().y - 1).setWall(false);
        }
        if (this.hero.getPosition().y + 1 <= this.size) {
            if (this.hero.getPosition().x - 1 >= 0)
                this.get(this.hero.getPosition().x - 1, this.hero.getPosition().y + 1).setWall(false);
            this.get(this.hero.getPosition().x, this.hero.getPosition().y + 1).setWall(false);
            if (this.hero.getPosition().x + 1 <= this.size)
                this.get(this.hero.getPosition().x + 1, this.hero.getPosition().y + 1).setWall(false);
        }
    }

    /**
     * Checks if the given position is free for a monster.
     */
    public boolean isFree(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size)
            return false;

        if (get(x, y).isAWall())
            return false;

        return !this.entityLst.containsKey(new Pos(x, y))
            && !hero.isHere(x, y);
    }

    /**
     * Attempts to move the player character up.
     */
    public BattleScene up() {
        Pos pos = new Pos((int)this.hero.getPosition().x, (int)this.hero.getPosition().y - 1);
    
        if (this.entityLst.containsKey(pos))
        {
            this.hero.up();
            return new BattleScene(this.hero.getCreature(), this.entityLst.get(pos).getCreature());
        }
        if (this.get(this.hero.getPosition().x, this.hero.getPosition().y - 1).isAWall())
            return null;
        this.hero.up();
        return null;
    }

    /**
     * Attempts to move the player character down.
     */
    public BattleScene down() {
        Pos pos = new Pos((int)this.hero.getPosition().x, (int)this.hero.getPosition().y + 1);
    
        if (this.entityLst.containsKey(pos))
        {
            this.hero.down();
            return new BattleScene(this.hero.getCreature(), this.entityLst.get(pos).getCreature());
        }
        if (this.get(this.hero.getPosition().x, this.hero.getPosition().y + 1).isAWall())
            return null;
        this.hero.down();
        return null;
    }

    /**
     * Attempts to move the player character left.
     */
    public BattleScene left() {
        Pos pos = new Pos((int)this.hero.getPosition().x - 1, (int)this.hero.getPosition().y);
    
        if (this.entityLst.containsKey(pos))
        {
            this.hero.left();
            return new BattleScene(this.hero.getCreature(), this.entityLst.get(pos).getCreature());
        }
        if (this.get(this.hero.getPosition().x - 1, this.hero.getPosition().y).isAWall())
            return null;
        this.hero.left();
        return null;
    }

    /**
     * Attempts to move the player character right.
     */
    public BattleScene right() {
        Pos pos = new Pos((int)this.hero.getPosition().x + 1, (int)this.hero.getPosition().y);
    
        if (this.entityLst.containsKey(pos))
        {
            this.hero.right();
            return new BattleScene(this.hero.getCreature(), this.entityLst.get(pos).getCreature());
        }
        if (this.get(this.hero.getPosition().x + 1, this.hero.getPosition().y).isAWall())
            return null;
        this.hero.right();
        return null;
    }

    public void cancel() {
        this.hero.cancel();
    }

    /**
     * Destroys the monster at the player's position.
     */
    public void eliminateMonster() {
        Pos pos = new Pos((int)this.hero.getPosition().x, (int)this.hero.getPosition().y);
        if (this.entityLst.containsKey(pos))
            this.entityLst.remove(pos);
    }

    /**
     * Get the entity at a specific position (if any)
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @return Entity at that position, or null if none
     */
    public Entity getEntityAt(int x, int y) {
        Pos pos = new Pos(x, y);
        return entityLst.get(pos);
    }
    
    /**
     * Get all entities on the map (for rendering)
     * 
     * @return HashMap of all entities
     */
    public HashMap<Pos, Entity> getAllEntities() {
        return entityLst;
    }

    @Override
    public String toString() {
        String dsp = "";
        Cell cell;

        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                cell = this.get(x, y);
                if (null != cell) {
                    if (this.hero.isHere(x, y))
                        dsp += "O";
                    else if (this.entityLst.containsKey(new Pos(x, y)))
                        dsp += this.entityLst.get(new Pos(x, y)).getCreature().getDesc();
                    else if (cell.isAWall())
                        dsp += "X";
                    else 
                        dsp += ".";
                }
            }
            dsp += "\n";
        }
        return dsp;
    }

    public Entity getHero() {
        return hero;
    }
}