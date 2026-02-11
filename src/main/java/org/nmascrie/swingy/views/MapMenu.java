package org.nmascrie.swingy.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.nmascrie.swingy.components.MapPanel;
import org.nmascrie.swingy.controllers.GUIController;
import org.nmascrie.swingy.models.BattleScene;
import org.nmascrie.swingy.models.Character;
import org.nmascrie.swingy.validator.InputValidator;
import org.nmascrie.swingy.validator.InputValidator.ValidationResult;

/**
 * Level Menu - Main gameplay screen
 * Displays map, character stats, and event log
 */
public class MapMenu extends BaseMenu {
    private final Consumer<String> onMenuSwitch;
    private MapPanel mapPanel;
    private JTextArea statsArea;
    private JTextArea logArea;
    private final InputValidator validator;
    
    public MapMenu(Consumer<String> onMenuSwitch) {
        this.onMenuSwitch = onMenuSwitch;
        this.validator = new InputValidator();
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 240, 240));
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(240, 240, 240));
        
        JLabel mapTitle = new JLabel("Somewhere on Hoxxes ...", SwingConstants.CENTER);
        mapTitle.setFont(new Font("Arial", Font.BOLD, 18));
        mapTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mapTitle.setForeground(Color.BLACK);
        
        mapPanel = new MapPanel();
        
        leftPanel.add(mapTitle, BorderLayout.NORTH);
        leftPanel.add(mapPanel, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        rightPanel.setBackground(new Color(240, 240, 240));
        rightPanel.setPreferredSize(new Dimension(400, 0));
        
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statsArea.setBackground(Color.WHITE);
        statsArea.setLineWrap(false);
        
        JScrollPane statsScroll = new JScrollPane(statsArea);
        statsScroll.setBorder(null);
        statsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        statsPanel.add(statsScroll, BorderLayout.CENTER);
        
        // Bottom right - Event log
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBackground(Color.WHITE);
        logPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel logTitle = new JLabel("Event Log", SwingConstants.CENTER);
        logTitle.setFont(new Font("Arial", Font.BOLD, 16));
        logTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        logArea.setBackground(new Color(250, 250, 250));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(null);
        logScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Auto-scroll to bottom
        logArea.setCaretPosition(logArea.getDocument().getLength());
        
        logPanel.add(logTitle, BorderLayout.NORTH);
        logPanel.add(logScroll, BorderLayout.CENTER);
        
        rightPanel.add(statsPanel);
        rightPanel.add(logPanel);
        
        // Add panels to main layout
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        
        // Bottom panel - Controls hint
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(240, 240, 240));
        
        JLabel controlsLabel = new JLabel("Controls: WASD to move | K to dig");
        controlsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        controlsLabel.setForeground(Color.DARK_GRAY);
        
        bottomPanel.add(controlsLabel);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Initialize a new map for the current level
     */
    private void initializeMap() {
        Character hero = GUIController.getInstance().getHero();
        
        if (hero == null) {
            JOptionPane.showMessageDialog(
                this,
                "No hero loaded! Returning to character selection.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            if (onMenuSwitch != null) {
                onMenuSwitch.accept("LoadChar");
            }
            return;
        }
        mapPanel.setMap(GUIController.getInstance().getMap());
        GUIController.getInstance().appendLog(GUIController.getInstance().getHero().startMissionQip());
        updateDisplay();
    }
    
    /**
     * Update stats and log displays
     */
    private void updateDisplay() {
        // Update character stats
        Character hero = GUIController.getInstance().getHero();
        if (hero != null) {
            statsArea.setText(hero.toString());
            statsArea.setForeground(Color.BLACK);
        }
        
        // Update event log
        String log = GUIController.getInstance().getLog();
        logArea.setForeground(Color.BLACK);
        if (log != null && !log.isEmpty()) {
            logArea.setText(log);
            logArea.setCaretPosition(logArea.getDocument().getLength());
        }
    }
    
    /**
     * Append a message to the log
     */
    private void appendLog(String message) {
        GUIController.getInstance().appendLog(message);
        updateDisplay();
    }

    private void checkPlayerDeath() {
        Character hero = GUIController.getInstance().getHero();
        if (hero != null && hero.getCurrent_hp() <= 0) {
            appendLog("=== GAME OVER ===");
            appendLog(hero.getName() + " has perished...");
            
            JOptionPane.showMessageDialog(
                this,
                "GAME OVER\n\n" + hero.getName() + " has died.\n\nReturning to character selection.",
                "Game Over",
                JOptionPane.ERROR_MESSAGE
            );
            
            if (onMenuSwitch != null) {
                onMenuSwitch.accept("LoadChar");
            }
        }
    }
    
    /**
     * Handle movement and battle
     */
    private void handleMovement(BattleScene battle) {
        if (battle != null) {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Enemy spotted!\n\nDo you wish to engage?",
                "Enemy Encounter",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                appendLog(GUIController.getInstance().getHero().getName() + " engages " + battle.getLeft().getName());
                GUIController.getInstance().setBattleScene(battle);
                onMenuSwitch.accept("BattleMenu");
            } else {
                appendLog("Attempting to flee...");
                if (Math.random() < 0.5) {
                    appendLog("Successfully escaped!");
                    GUIController.getInstance().getMap().cancel();
                    mapPanel.repaint();
                } else {
                    appendLog("Failed to escape! Enemy blocks your path!");
                    JOptionPane.showMessageDialog(
                        this,
                        "You couldn't escape!\n\nThe enemy caught you!",
                        "Escape Failed",
                        JOptionPane.WARNING_MESSAGE
                    );
                    GUIController.getInstance().setBattleScene(battle);
                    onMenuSwitch.accept("BattleMenu");
                }
            }
        } else {
            mapPanel.repaint();
            updateDisplay();
            if (GUIController.getInstance().getMap().victoryCondition()) {
                JOptionPane.showConfirmDialog(
                    this,
                    "You won!",
                    "Victory!",
                    JOptionPane.OK_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
                );
                if (onMenuSwitch != null) {
                    onMenuSwitch.accept("SelectLevel");
                }
            }
        }
    }
    
    @Override
    public String getMenuId() {
        return "Level";
    }
    
    @Override
    public void handleKeyPress(String keyText) {
        BattleScene battle = null;
        ValidationResult result = validator.validate(keyText);

        if (GUIController.getInstance().getMap() == null)
            return;
        if (GUIController.getInstance().getHero() != null && GUIController.getInstance().getHero().getCurrent_hp() <= 0) {
            checkPlayerDeath();
            return;
        }
        if (result.isValid())
        {
            switch (keyText.toUpperCase()) {
                case "W" -> battle = GUIController.getInstance().getMap().up();
                case "A" -> battle = GUIController.getInstance().getMap().left();
                case "S" -> battle = GUIController.getInstance().getMap().down();
                case "D" -> battle = GUIController.getInstance().getMap().right();
                case "V" -> GUIController.getInstance().appendLog(GUIController.getInstance().getHero().actionQip());
                case "K" -> {
                    GUIController.getInstance().appendLog(GUIController.getInstance().getHero().digQip());
                    GUIController.getInstance().getMap().dig();
                    mapPanel.repaint();
                }
            }
            handleMovement(battle);
        }
    }
    
    @Override
    public boolean isKeyboardEnabled() {
        return true;
    }
    
    @Override
    public void onMenuActivated() {
        super.onMenuActivated();
        initializeMap();
        checkPlayerDeath();
    }
    
    @Override
    public void onMenuDeactivated() {
        super.onMenuDeactivated();
        mapPanel.setMap(null);
    }
}