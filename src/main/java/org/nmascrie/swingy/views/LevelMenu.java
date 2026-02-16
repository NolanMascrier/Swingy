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
    private ImageButton restCharacterButton;
    
    public LevelMenu(Consumer<String> onMenuSwitch) {
        this.onMenuSwitch = onMenuSwitch;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 245, 245));
        
        JLabel titleLabel = new JLabel("DRG Space Rig 17", SwingConstants.CENTER);
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

        ImageIcon rest = new ImageIcon(getClass().getResource("/images/medbay.png"));
        restCharacterButton = new ImageButton(
            "Med Bay",
            "Rest and get rusty.",
            rest,
            this::handleHealthCare
        );
        buttonsPanel.add(restCharacterButton);
        
        centerPanel.add(buttonsPanel);
        
        JPanel heroPanel = new JPanel(new BorderLayout());
        heroPanel.setBackground(Color.WHITE);
        heroPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel heroTitle = new JLabel("DRG Performance Report", SwingConstants.CENTER);
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
            heroDisplayArea.setText(hero.toString() + "\nBugs squashed: " + hero.kills + "\nCaves explored: " + hero.explored);
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
        hero.exports();
        System.out.println("DEBUG:: Character saved: " + hero.getName());
        
        JOptionPane.showMessageDialog(
            this,
            "Character \"" + hero.getName() + "\" saved successfully!",
            "Save Successful",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void handleHealthCare() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure ? You will lose half of the needed experience for the next level.",
            "Med Bay",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            GUIController.getInstance().getHero().rest();
            updateHeroDisplay();
            JOptionPane.showMessageDialog(
                this,
                "You're feeling healthy !",
                "ALL GOOD",
                JOptionPane.OK_OPTION
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