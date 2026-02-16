package org.nmascrie.swingy.views;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.nmascrie.swingy.controllers.GUIController;
import org.nmascrie.swingy.models.BattleScene;
import org.nmascrie.swingy.models.Character;
import org.nmascrie.swingy.models.Creature;
import org.nmascrie.swingy.models.Item;

/**
 * BattleMenu - Displays automated combat between player and enemy
 */
public class BattleMenu extends BaseMenu {
    private final Consumer<String> onMenuSwitch;
    private JLabel leftFighterImage;
    private JLabel rightFighterImage;
    private JLabel leftFighterName;
    private JLabel rightFighterName;
    private JLabel leftFighterHP;
    private JLabel rightFighterHP;
    private JTextArea battleLogArea;
    private JProgressBar leftHealthBar;
    private JProgressBar rightHealthBar;
    private JButton continueButton;
    private JLabel statusLabel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    
    private BattleScene battleScene;
    private Timer battleTimer;
    private boolean battleInProgress = false;
    private boolean battleEnded = false;
    
    public BattleMenu(Consumer<String> onMenuSwitch) {
        this.onMenuSwitch = onMenuSwitch;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(30, 30, 40));
        
        JLabel titleLabel = new JLabel("BATTLE!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.RED);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel battlePanel = new JPanel(new GridLayout(1, 3, 15, 0));
        battlePanel.setBackground(new Color(30, 30, 40));
        
        leftPanel = createFighterPanel(true);
        
        JPanel centerPanel = createBattleLogPanel();
        
        rightPanel = createFighterPanel(false);
        
        battlePanel.add(leftPanel);
        battlePanel.add(centerPanel);
        battlePanel.add(rightPanel);
        
        add(battlePanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(30, 30, 40));
        
        statusLabel = new JLabel("Battle in progress...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(Color.LIGHT_GRAY);
        
        continueButton = new JButton("Continue");
        continueButton.setFont(new Font("Arial", Font.BOLD, 16));
        continueButton.setPreferredSize(new Dimension(150, 40));
        continueButton.setVisible(false);
        continueButton.addActionListener(e -> handleContinue());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(30, 30, 40));
        buttonPanel.add(continueButton);
        
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create fighter display panel
     */
    private JPanel createFighterPanel(boolean isLeft) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(50, 50, 60));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isLeft ? new Color(100, 149, 237) : Color.RED, 3),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 250));
        imageLabel.setIcon(createFighterPlaceholder(isLeft));
        
        if (isLeft) {
            leftFighterImage = imageLabel;
        } else {
            rightFighterImage = imageLabel;
        }
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(50, 50, 60));
        
        JLabel nameLabel = new JLabel("Fighter");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if (isLeft) {
            leftFighterName = nameLabel;
        } else {
            rightFighterName = nameLabel;
        }
        
        JLabel hpLabel = new JLabel("HP: 100/100");
        hpLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        hpLabel.setForeground(Color.LIGHT_GRAY);
        hpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if (isLeft) {
            leftFighterHP = hpLabel;
        } else {
            rightFighterHP = hpLabel;
        }
        
        JProgressBar healthBar = new JProgressBar(0, 100);
        healthBar.setValue(100);
        healthBar.setStringPainted(true);
        healthBar.setForeground(isLeft ? new Color(76, 175, 80) : Color.RED);
        healthBar.setBackground(new Color(80, 80, 90));
        healthBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if (isLeft) {
            leftHealthBar = healthBar;
        } else {
            rightHealthBar = healthBar;
        }
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(hpLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(healthBar);
        
        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Loads the picture associated to the creature.
     */
    private ImageIcon loadCreatureImage(Creature creature, boolean isLeft) {
    try {
        String imageID = creature.getImageID();
        String imagePath = "/images/" + imageID + ".png";
        
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        
        Image scaled = icon.getImage().getScaledInstance(180, 220, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
        
    } catch (Exception e) {
        System.err.println("ERROR:: Could not load creature image: " + creature.getImageID());
        return createFighterPlaceholder(isLeft);
    }
}
    
    /**
     * Create battle log panel
     */
    private JPanel createBattleLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel logTitle = new JLabel("Battle Log", SwingConstants.CENTER);
        logTitle.setFont(new Font("Arial", Font.BOLD, 16));
        logTitle.setForeground(Color.WHITE);
        logTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        battleLogArea = new JTextArea();
        battleLogArea.setEditable(false);
        battleLogArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        battleLogArea.setBackground(new Color(25, 25, 35));
        battleLogArea.setForeground(Color.LIGHT_GRAY);
        battleLogArea.setLineWrap(true);
        battleLogArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(battleLogArea);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        panel.add(logTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create placeholder fighter image
     */
    private ImageIcon createFighterPlaceholder(boolean isLeft) {
        int width = 180;
        int height = 220;
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
            width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color bgColor = isLeft ? new Color(100, 149, 237) : new Color(220, 50, 50);
        GradientPaint gradient = new GradientPaint(
            0, 0, bgColor,
            width, height, bgColor.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
        
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(0, 0, width - 1, height - 1);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 80));
        String icon = "FIGHTER";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(icon);
        g2d.drawString(icon, (width - textWidth) / 2, height / 2 + 30);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    /**
     * Initialize battle display
     */
    private void initializeBattle() {
        battleScene = GUIController.getInstance().getBattleScene();
        battleEnded = false;
        
        if (battleScene == null) {
            JOptionPane.showMessageDialog(
                this,
                "No battle data found!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            returnToLevel();
            return;
        }
        Creature leftFighter = battleScene.getRight();  // Player
        Creature rightFighter = battleScene.getLeft();  // Enemy
        ImageIcon leftImage = loadCreatureImage(leftFighter, true);
        leftFighterImage.setIcon(leftImage);
        ImageIcon rightImage = loadCreatureImage(rightFighter, false);
        rightFighterImage.setIcon(rightImage);
        
        leftFighterName.setText(leftFighter.getName());
        updateFighterHP(leftFighter, leftFighterHP, leftHealthBar, true);
        
        rightFighterName.setText(rightFighter.getName());
        updateFighterHP(rightFighter, rightFighterHP, rightHealthBar, false);
        
        battleLogArea.setText("");
        appendBattleLog(leftFighter.getName() + " vs " + rightFighter.getName());
        appendBattleLog("Let the Battle Begin !");
        appendBattleLog("");
        
        continueButton.setVisible(false);
        statusLabel.setText("Battle in progress...");
        startBattle();
    }
    
    /**
     * Update fighter HP display
     */
    private void updateFighterHP(Creature fighter, JLabel hpLabel, JProgressBar healthBar, boolean isLeft) {
        long currentHP = fighter.getCurrent_hp();
        long maxHP = fighter.getHitpoints();
        
        hpLabel.setText("HP: " + currentHP + "/" + maxHP);
        
        int percentage = (int) ((currentHP * 100) / maxHP);
        healthBar.setValue(Math.max(0, percentage));
        
        if (percentage > 60) {
            healthBar.setForeground(isLeft ? new Color(76, 175, 80) : Color.RED);
        } else if (percentage > 30) {
            healthBar.setForeground(Color.ORANGE);
        } else {
            healthBar.setForeground(Color.RED);
        }
    }
    
    /**
     * Append message to battle log
     */
    private void appendBattleLog(String message) {
        battleLogArea.append(message + "\n");
        battleLogArea.setCaretPosition(battleLogArea.getDocument().getLength());
    }
    
    /**
     * Start automated battle
     */
    private void startBattle() {
        battleInProgress = true;
        battleTimer = new Timer(125, e -> {
            if (!battleInProgress) {
                battleTimer.stop();
                return;
            }
            processBattleMove();
        });
        battleTimer.start();
    }
    
    /**
     * Process a single battle move
     */
    private void processBattleMove() {
        BattleScene.BattleLog log = battleScene.nextMove();
        if (log == null) {
            endBattle();
            return;
        }
        appendBattleLog(log.toString());
        
        updateFighterHP(battleScene.getRight(), leftFighterHP, leftHealthBar, true);
        updateFighterHP(battleScene.getLeft(), rightFighterHP, rightHealthBar, false);
        
        if (battleScene.getLeft().getCurrent_hp() <= 0 || 
            battleScene.getRight().getCurrent_hp() <= 0) {
            battleTimer.setDelay(500);
        }
    }
    
    /**
     * End battle and handle results
     */
    private void endBattle() {
        battleInProgress = false;
        battleEnded = true;
        
        if (battleTimer != null) {
            battleTimer.stop();
        }
        
        GUIController.getInstance().handleBattleEnd(
            () -> {
                statusLabel.setText("Defeat! Press Continue to return...");
                statusLabel.setForeground(Color.RED);
                continueButton.setVisible(true);
            },
            this::handleLoot
        );
    }
    
    /**
     * Handle loot after victory
     */
    private void handleLoot(Item item) {
        statusLabel.setText("Victory! Check the log below.");
        statusLabel.setForeground(Color.GREEN);
        appendBattleLog("You gained " + GUIController.getInstance().getLastEXP() + " EXP !");
        
        if (item == null) {
            appendBattleLog("");
            appendBattleLog("Press Continue to return to the map.");
            continueButton.setVisible(true);
            return;
        }
        
        Character hero = GUIController.getInstance().getHero();
        
        String message = "Loot found: " + item.toString() + "\n\n";
        
        Item equipped = hero.getEquipped(item.getType());
        if (equipped == null) {
            message += item.fullDesc();
        } else {
            message += equipped.compare(item);
        }
        
        message += "\n\nDo you wish to equip it?";
        
        int result = JOptionPane.showConfirmDialog(
            this,
            message,
            "Loot Found!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            hero.equip(item);
            appendBattleLog("Equipped: " + item.getName());
        } else {
            appendBattleLog("Discarded: " + item.getName());
        }
        
        appendBattleLog("");
        appendBattleLog("Press Continue to return to the map.");
        continueButton.setVisible(true);
    }
    
    /**
     * Handle continue button click
     */
    private void handleContinue() {
        returnToLevel();
    }
    
    /**
     * Return to level menu
     */
    private void returnToLevel() {
        if (onMenuSwitch != null) {
            onMenuSwitch.accept("Level");
        }
    }
    
    @Override
    public String getMenuId() {
        return "BattleMenu";
    }
    
    @Override
    public void handleKeyPress(String keyText) {
        if (battleEnded && (keyText.equals("Enter") || keyText.equals("Space"))) {
            if (continueButton.isVisible()) {
                handleContinue();
            }
        }
    }
    
    @Override
    public boolean isKeyboardEnabled() {
        return true;
    }
    
    @Override
    public void onMenuActivated() {
        super.onMenuActivated();
        initializeBattle();
    }
    
    @Override
    public void onMenuDeactivated() {
        super.onMenuDeactivated();
        
        if (battleTimer != null && battleTimer.isRunning()) {
            battleTimer.stop();
        }
        
        battleInProgress = false;
        battleEnded = false;
    }
}