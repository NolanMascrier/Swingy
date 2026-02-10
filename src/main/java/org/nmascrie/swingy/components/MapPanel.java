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
import org.nmascrie.swingy.models.Pos;

/**
 * Custom component for rendering the game map
 * Displays an 11x11 view centered on the player
 */
public class MapPanel extends JPanel {
    private static final int TILE_SIZE = 64;
    private static final int VIEW_SIZE = 11; // 11x11 tiles visible
    private static final int PANEL_SIZE = TILE_SIZE * VIEW_SIZE;
    
    private Map gameMap;
    private BufferedImage floorTile;
    private BufferedImage wallTile;
    private BufferedImage playerTile;
    private final java.util.Map<String, BufferedImage> entityTiles;
    
    public MapPanel() {
        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        
        entityTiles = new HashMap<>();
        initializeTiles();
    }
    
    /**
     * Initialize tile images (placeholder for now)
     */
    private void initializeTiles() {
        floorTile = createTile(new Color(139, 90, 43), "Floor");
        wallTile = createTile(new Color(105, 105, 105), "Wall");
        playerTile = createPlayerTile();
    }
    
    /**
     * Create a simple colored tile with optional text
     */
    private BufferedImage createTile(Color color, String text) {
        BufferedImage tile = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = tile.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fill background
        g2d.setColor(color);
        g2d.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        
        // Add border
        g2d.setColor(color.darker());
        g2d.drawRect(0, 0, TILE_SIZE - 1, TILE_SIZE - 1);
        
        // Add text if provided
        if (text != null && !text.isEmpty()) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw circle for player
        g2d.setColor(new Color(100, 149, 237));
        g2d.fillOval(8, 8, TILE_SIZE - 16, TILE_SIZE - 16);
        
        // Draw border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(8, 8, TILE_SIZE - 16, TILE_SIZE - 16);
        
        // Draw "O" in center
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "O";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.drawString(text, (TILE_SIZE - textWidth) / 2, (TILE_SIZE + textHeight / 2) / 2);
        
        g2d.dispose();
        return tile;
    }
    
    /**
     * Get or create entity tile based on creature description
     */
    private BufferedImage getEntityTile(String desc) {
        if (!entityTiles.containsKey(desc)) {
            // Create a new tile for this entity type
            Color entityColor = getEntityColor(desc);
            BufferedImage tile = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = tile.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw entity shape
            g2d.setColor(entityColor);
            g2d.fillRect(12, 12, TILE_SIZE - 24, TILE_SIZE - 24);
            
            // Draw border
            g2d.setColor(entityColor.darker());
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(12, 12, TILE_SIZE - 24, TILE_SIZE - 24);
            
            // Draw description character
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = g2d.getFontMetrics();
            String text = desc.length() > 0 ? desc.substring(0, 1) : "?";
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
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
        // Hash the description to get consistent colors
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
        super.paintComponent(g);
        
        if (gameMap == null) {
            // No map loaded, show placeholder
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            String msg = "No map loaded";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Get player position
        Entity hero = gameMap.getHero();
        if (hero == null) return;
        
        int playerX = (int) hero.getPosition().x;
        int playerY = (int) hero.getPosition().y;
        
        // Calculate view bounds (11x11 centered on player)
        int viewRadius = VIEW_SIZE / 2; // 5 tiles in each direction
        int startX = playerX - viewRadius;
        int startY = playerY - viewRadius;
        
        // Adjust if near edges (map size from toString can be inferred)
        // For now, we'll just render what we can
        
        // Render base layer (floor and walls)
        for (int tileY = 0; tileY < VIEW_SIZE; tileY++) {
            for (int tileX = 0; tileX < VIEW_SIZE; tileX++) {
                int mapX = startX + tileX;
                int mapY = startY + tileY;
                
                Cell cell = gameMap.get(mapX, mapY);
                if (cell != null) {
                    int screenX = tileX * TILE_SIZE;
                    int screenY = tileY * TILE_SIZE;
                    
                    if (cell.isAWall()) {
                        g2d.drawImage(wallTile, screenX, screenY, null);
                    } else {
                        g2d.drawImage(floorTile, screenX, screenY, null);
                    }
                }
            }
        }
        
        // Render entity layer (monsters and player)
        HashMap<Pos, Entity> entities = gameMap.getAllEntities();

        for (int tileY = 0; tileY < VIEW_SIZE; tileY++) {
            for (int tileX = 0; tileX < VIEW_SIZE; tileX++) {
                int mapX = startX + tileX;
                int mapY = startY + tileY;
                
                int screenX = tileX * TILE_SIZE;
                int screenY = tileY * TILE_SIZE;
                
                // Check if player is here
                if (hero.isHere(mapX, mapY)) {
                    g2d.drawImage(playerTile, screenX, screenY, null);
                } else {
                    // Check if entity is here
                    Entity entity = gameMap.getEntityAt(mapX, mapY);
                    if (entity != null) {
                        String desc = "" + entity.getCreature().getDesc();
                        BufferedImage entityTile = getEntityTile(desc);
                        g2d.drawImage(entityTile, screenX, screenY, null);
                    }
                }
            }
        }
        
        // Draw grid lines for clarity
        g2d.setColor(new Color(0, 0, 0, 30));
        for (int i = 0; i <= VIEW_SIZE; i++) {
            g2d.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, PANEL_SIZE);
            g2d.drawLine(0, i * TILE_SIZE, PANEL_SIZE, i * TILE_SIZE);
        }
    }
}