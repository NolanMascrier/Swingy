package org.nmascrie.swingy.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.nmascrie.swingy.models.Cell;
import org.nmascrie.swingy.models.Entity;
import org.nmascrie.swingy.models.Map;

/**
 * Custom component for rendering the game map
 * Displays an 11x11 view centered on the player
 */
public class MapPanel extends JPanel {
    private static final int TILE_SIZE = 64;
    private static final int VIEW_SIZE = 11;
    private static final int PANEL_SIZE = TILE_SIZE * VIEW_SIZE;
    
    private Map gameMap;
    private BufferedImage floorTile;
    private BufferedImage wallTile;
    private BufferedImage playerTile;
    private final java.util.Map<String, BufferedImage> entityTiles;
    
    public MapPanel() {
        entityTiles = new HashMap<>();
        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        initializeTiles();
    }
    
    /**
     * Initialize tile images
     */
    private void initializeTiles() {
        floorTile = createTile(new Color(176, 90, 63), "");
        wallTile = createTile(new Color(77, 21, 4), "");
        playerTile = createPlayerTile();
    }
    
    /**
     * Create a simple colored tile
     */
    private BufferedImage createTile(Color color, String text) {
        BufferedImage tile = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = tile.createGraphics();
        FontMetrics fm;
        int textWidth;
    
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fillRect(0, 0, TILE_SIZE, TILE_SIZE);

        if (text != null && !text.isEmpty()) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            fm = g2d.getFontMetrics();
            textWidth = fm.stringWidth(text);
            g2d.drawString(text, (TILE_SIZE - textWidth) / 2, TILE_SIZE / 2 + 5);
        }
        
        g2d.dispose();
        return tile;
    }
    
    /**
     * Create player character tile
     */
    private BufferedImage createPlayerTile() {
        BufferedImage tile = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tile.createGraphics();
        FontMetrics fm;
        String text;
        int textWidth;
        int textHeight;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(100, 149, 237));
        g2d.fillOval(8, 8, TILE_SIZE - 16, TILE_SIZE - 16);
        
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(8, 8, TILE_SIZE - 16, TILE_SIZE - 16);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        fm = g2d.getFontMetrics();
        text = "O";
        textWidth = fm.stringWidth(text);
        textHeight = fm.getHeight();
        g2d.drawString(text, (TILE_SIZE - textWidth) / 2, (TILE_SIZE + textHeight / 2) / 2);
        
        g2d.dispose();
        return tile;
    }
    
    /**
     * Get or create entity tile based on creature description
     */
    private BufferedImage getEntityTile(String desc) {
        FontMetrics fm;
        String text;
        int textWidth;
        int textHeight;
        Color entityColor;
        BufferedImage tile;
        Graphics2D g2d;
    
        if (!entityTiles.containsKey(desc)) {
            entityColor = getEntityColor(desc);
            tile = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
            g2d = tile.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(entityColor);
            g2d.fillRect(12, 12, TILE_SIZE - 24, TILE_SIZE - 24);
            
            g2d.setColor(entityColor.darker());
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(12, 12, TILE_SIZE - 24, TILE_SIZE - 24);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            fm = g2d.getFontMetrics();
            text = desc.length() > 0 ? desc.substring(0, 1) : "?";
            textWidth = fm.stringWidth(text);
            textHeight = fm.getHeight();
            g2d.drawString(text, (TILE_SIZE - textWidth) / 2, (TILE_SIZE + textHeight / 2) / 2);
            
            g2d.dispose();
            entityTiles.put(desc, tile);
        }
        return entityTiles.get(desc);
    }
    
    /**
     * Get color for entity based on description
     */
    private Color getEntityColor(String desc) {
        int hash = desc.hashCode();
        int r = 150 + (Math.abs(hash) % 100);
        int g = 50 + (Math.abs(hash / 2) % 100);
        int b = 50 + (Math.abs(hash / 3) % 100);
        return new Color(r % 256, g % 256, b % 256);
    }
    
    /**
     * Set the map to display
     */
    public void setMap(Map map) {
        this.gameMap = map;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        int mapX;
        int mapY;
        int screenX;
        int screenY;
        int playerX;
        int playerY;
        int viewRadius;
        int cameraX;
        int cameraY;
        long mapSize;
        Cell cell;
        Entity entity;
        Entity hero;
        String desc;
        String msg;
        Graphics2D g2d;
        FontMetrics fm;
        BufferedImage entityTile;

        super.paintComponent(g);
        
        if (gameMap == null) {
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            msg = "No map loaded";
            fm = g.getFontMetrics();
            g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
            return;
        }
        
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        hero = gameMap.getHero();
        if (hero == null) return;

        playerX = (int) hero.getPosition().x;
        playerY = (int) hero.getPosition().y;
        mapSize = gameMap.getSize();
        
        viewRadius = VIEW_SIZE / 2;
        
        cameraX = playerX - viewRadius;
        cameraY = playerY - viewRadius;
        
        cameraX = Math.max(0, cameraX);
        cameraY = Math.max(0, cameraY);
        
        cameraX = Math.min((int)(mapSize - VIEW_SIZE), cameraX);
        cameraY = Math.min((int)(mapSize - VIEW_SIZE), cameraY);
        
        if (mapSize < VIEW_SIZE) {
            cameraX = -(VIEW_SIZE - (int)mapSize) / 2;
            cameraY = -(VIEW_SIZE - (int)mapSize) / 2;
        }
        
        for (int tileY = 0; tileY < VIEW_SIZE; tileY++) {
            for (int tileX = 0; tileX < VIEW_SIZE; tileX++) {
                mapX = cameraX + tileX;
                mapY = cameraY + tileY;
                
                screenX = tileX * TILE_SIZE;
                screenY = tileY * TILE_SIZE;
                
                if (mapX < 0 || mapY < 0 || mapX >= mapSize || mapY >= mapSize) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(screenX, screenY, TILE_SIZE, TILE_SIZE);
                    continue;
                }

                cell = gameMap.get(mapX, mapY);
                
                if (cell == null) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(screenX, screenY, TILE_SIZE, TILE_SIZE);
                } else {
                    if (cell.isAWall()) {
                        g2d.drawImage(wallTile, screenX, screenY, null);
                    } else {
                        g2d.drawImage(floorTile, screenX, screenY, null);
                    }
                }
            }
        }
        
        for (int tileY = 0; tileY < VIEW_SIZE; tileY++) {
            for (int tileX = 0; tileX < VIEW_SIZE; tileX++) {
                mapX = cameraX + tileX;
                mapY = cameraY + tileY;
                
                if (mapX < 0 || mapY < 0 || mapX >= mapSize || mapY >= mapSize)
                    continue;
                
                screenX = tileX * TILE_SIZE;
                screenY = tileY * TILE_SIZE;
                
                if (hero.isHere(mapX, mapY)) {
                    g2d.drawImage(playerTile, screenX, screenY, null);
                } else {
                    entity = gameMap.getEntityAt(mapX, mapY);
                    if (entity != null) {
                        desc = "" + entity.getCreature().getDesc();
                        entityTile = getEntityTile(desc);
                        g2d.drawImage(entityTile, screenX, screenY, null);
                    }
                }
            }
        }
    }
}