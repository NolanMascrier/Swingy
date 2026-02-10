package org.nmascrie.swingy.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.nmascrie.swingy.components.ImageButton;
import org.nmascrie.swingy.controllers.GUIController;
import org.nmascrie.swingy.models.Character;

/**
 * SelectLevel Menu
 * Choose to start a new game or save the current character
 */
public class LevelMenu extends BaseMenu {
    private final Consumer<String> onMenuSwitch;
    private JTextArea heroDisplayArea;
    private ImageButton startGameButton;
    private ImageButton saveCharacterButton;
    
    public LevelMenu(Consumer<String> onMenuSwitch) {
        this.onMenuSwitch = onMenuSwitch;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 245, 245));
        
        // Title
        JLabel titleLabel = new JLabel("THE SPACE RIG", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0,0,0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(new Color(245, 245, 245));
        
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonsPanel.setBackground(new Color(245, 245, 245));
        
        ImageIcon start = new ImageIcon(getClass().getResource("/images/mission.png"));
        startGameButton = new ImageButton(
            "Drop Pod",
            "Explore a Cavern.",
            start,
            this::handleStartGame
        );
        buttonsPanel.add(startGameButton);
        
        ImageIcon save = new ImageIcon(getClass().getResource("/images/save.png"));
        saveCharacterButton = new ImageButton(
            "Abyss Bar",
            "Save your progress.",
            save,
            this::handleSaveCharacter
        );
        buttonsPanel.add(saveCharacterButton);
        
        centerPanel.add(buttonsPanel);
        
        // Right panel - hero display
        JPanel heroPanel = new JPanel(new BorderLayout());
        heroPanel.setBackground(Color.WHITE);
        heroPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel heroTitle = new JLabel("Hero Information", SwingConstants.CENTER);
        heroTitle.setFont(new Font("Arial", Font.BOLD, 18));
        heroTitle.setForeground(new Color(0,0,0));
        heroTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        heroDisplayArea = new JTextArea();
        heroDisplayArea.setEditable(false);
        heroDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        heroDisplayArea.setBackground(Color.WHITE);
        heroDisplayArea.setLineWrap(true);
        heroDisplayArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(heroDisplayArea);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        heroPanel.add(heroTitle, BorderLayout.NORTH);
        heroPanel.add(scrollPane, BorderLayout.CENTER);
        
        centerPanel.add(heroPanel);
        
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 245, 245));
        
        JButton backButton = new JButton("Return to Characters Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(250, 40));
        backButton.addActionListener(e -> {
            if (onMenuSwitch != null) {
                onMenuSwitch.accept("LoadChar");
            }
        });
        
        bottomPanel.add(backButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Update the hero display with current hero information
     */
    private void updateHeroDisplay() {
        Character hero = GUIController.getInstance().getHero();
        
        if (hero != null) {
            // Simply use the hero's toString() method
            heroDisplayArea.setText(hero.toString());
            heroDisplayArea.setForeground(new Color(0,0,0));
        } else {
            heroDisplayArea.setText("No hero loaded.\n\nPlease create or load a character first.");
        }
    }
    
    /**
     * Handle Start Game button click
     */
    private void handleStartGame() {
        Character hero = GUIController.getInstance().getHero();
        
        if (hero == null) {
            JOptionPane.showMessageDialog(
                this,
                "No hero loaded!\n\nPlease create or load a character first.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        GUIController.getInstance().generateMap();
        if (onMenuSwitch != null) {
            onMenuSwitch.accept("Level");
        }
    }
    
    /**
     * Handle Save Character button click
     */
    private void handleSaveCharacter() {
        Character hero = GUIController.getInstance().getHero();
        
        if (hero == null) {
            JOptionPane.showMessageDialog(
                this,
                "No hero to save!\n\nPlease create or load a character first.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        try {
            hero.exports();
            
            System.out.println("Character saved: " + hero.getName());
            
            JOptionPane.showMessageDialog(
                this,
                "Character \"" + hero.getName() + "\" saved successfully!",
                "Save Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            System.err.println("Error saving character: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(
                this,
                "Failed to save character!\n\nError: " + e.getMessage(),
                "Save Failed",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    @Override
    public String getMenuId() {
        return "SelectLevel";
    }
    
    @Override
    public void handleKeyPress(String keyText) {}
    
    @Override
    public boolean isKeyboardEnabled() {
        return false;
    }
    
    @Override
    public void onMenuActivated() {
        super.onMenuActivated();
        updateHeroDisplay();
    }
    
    @Override
    public void onMenuDeactivated() {
        super.onMenuDeactivated();
    }
}