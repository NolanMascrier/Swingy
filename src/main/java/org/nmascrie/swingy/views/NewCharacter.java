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
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.nmascrie.swingy.components.ImageButton;
import org.nmascrie.swingy.controllers.GUIController;
import org.nmascrie.swingy.enums.HeroClass;
import org.nmascrie.swingy.models.Character;
import org.nmascrie.swingy.validator.NameValidator;
import org.nmascrie.swingy.validator.NameValidator.ValidationResult;

/**
 * Menu 4: Character selection with name input
 * Demonstrates image buttons, input validation, and state management
 */
public class NewCharacter extends BaseMenu {
    private String selectedCharacter = null;
    private final StringBuilder nameInput = new StringBuilder();
    private int index = -1;
    
    private JLabel instructionLabel;
    private JLabel nameDisplayLabel;
    private JLabel stateDisplayLabel;
    private JButton acceptButton;
    private ImageButton[] optionsButtons;
    private final NameValidator nameValidator;
    private final Consumer<String> onMenuSwitch;
    
    private static final String[] CHARACTER_NAMES = {
        "Scout", "Gunner", "Engineer", "Driller"
    };
    private static final String[] CHARACTER_DESCRIPTIONS = {
        "A swift yet fragile warrior. Can dodge attacks.", "A solid but slow fighter.",
        "A soldier with balanced abilities and high critical chance.", "A powerful and dangerous maniac that forgoes defenses."
    };
    private static final HeroClass[] CHARACTER_CLASSES = {
        HeroClass.SCOUT, HeroClass.GUNNER, HeroClass.ENGINEER, HeroClass.DRILLER
    };
    private static final String[] URLS = {
        "/images/scout_e.png", "/images/gunner_e.png", "/images/engineer_e.png", "/images/driller_p.png"
    };
    
    public NewCharacter(Consumer<String> onMenuSwitch) {
        this.onMenuSwitch = onMenuSwitch;
        this.nameValidator = new NameValidator();
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 245, 245));
        
        instructionLabel = new JLabel("Choose your character and enter your name:", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        instructionLabel.setForeground(Color.BLACK);
        add(instructionLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setBackground(new Color(245, 245, 245));
        
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        buttonsPanel.setBackground(new Color(245, 245, 245));
        
        optionsButtons = new ImageButton[4];
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            ImageIcon icon = new ImageIcon(getClass().getResource(URLS[i]));
            optionsButtons[i] = new ImageButton(
                CHARACTER_NAMES[i],
                CHARACTER_DESCRIPTIONS[i],
                icon,
                () -> selectCharacter(idx)
            );
            buttonsPanel.add(optionsButtons[i]);
        }
        
        centerPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        rightPanel.setPreferredSize(new Dimension(250, 0));
        
        JLabel stateTitle = new JLabel("Dwarven CV", SwingConstants.CENTER);
        stateTitle.setForeground(Color.BLACK);
        stateTitle.setFont(new Font("Arial", Font.BOLD, 18));
        
        stateDisplayLabel = new JLabel("<html><center>No character selected<br><br>Name: (empty)</center></html>");
        stateDisplayLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        stateDisplayLabel.setForeground(Color.BLACK);
        stateDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        stateDisplayLabel.setVerticalAlignment(SwingConstants.TOP);
        
        rightPanel.add(stateTitle, BorderLayout.NORTH);
        rightPanel.add(stateDisplayLabel, BorderLayout.CENTER);
        
        centerPanel.add(rightPanel, BorderLayout.EAST);
        
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(245, 245, 245));
        
        JPanel namePanel = new JPanel(new BorderLayout(10, 5));
        namePanel.setBackground(new Color(245, 245, 245));
        
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        nameDisplayLabel = new JLabel("");
        nameDisplayLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameDisplayLabel.setPreferredSize(new Dimension(400, 30));
        nameDisplayLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        nameDisplayLabel.setOpaque(true);
        nameDisplayLabel.setBackground(Color.WHITE);
        
        namePanel.add(nameLabel, BorderLayout.WEST);
        namePanel.add(nameDisplayLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        acceptButton = new JButton("Accept");
        acceptButton.setFont(new Font("Arial", Font.BOLD, 16));
        acceptButton.setPreferredSize(new Dimension(150, 40));
        acceptButton.setEnabled(false);
        acceptButton.addActionListener(e -> handleAccept());
        
        buttonPanel.add(acceptButton);
        
        bottomPanel.add(namePanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.addActionListener(e -> {
            if (onMenuSwitch != null) {
                onMenuSwitch.accept("MainMenu");
            }
        });

        buttonPanel.add(backButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void selectCharacter(int index) {
        for (ImageButton button : optionsButtons) {
            button.setSelected(false);
        }
        
        optionsButtons[index].setSelected(true);
        this.index = index;
        selectedCharacter = CHARACTER_NAMES[index];
        
        updateStateDisplay();
        updateAcceptButton();
    }
    
    private void updateStateDisplay() {
        String nameText = nameInput.length() > 0 ? nameInput.toString() : "-";
        
        if (this.index == -1)
            stateDisplayLabel.setText(
                "<html><center>" +
                "<b>Name:</b> " + nameText +
                "</center></html>"
            );
        else 
            stateDisplayLabel.setText(
                "<html><center>" +
                "<b>Name:</b> " + nameText + "<br/>Level 1 " + selectedCharacter +
                "<br/>HITPOINTS: " + CHARACTER_CLASSES[index].hp +
                "<br/>POWER: " + CHARACTER_CLASSES[index].atk +
                "<br/>DEFENSE: " + CHARACTER_CLASSES[index].def +
                "<br/>SPEED: " + CHARACTER_CLASSES[index].speed +
                "<br/>DODGE CHANCE: " + CHARACTER_CLASSES[index].dodge * 100 +
                "%<br/>CRIT CHANCE: " + CHARACTER_CLASSES[index].crit * 100 +
                "%</center></html>"
            );
    }
    
    private void updateAcceptButton() {
        boolean canAccept = selectedCharacter != null && nameInput.length() >= 3;
        acceptButton.setEnabled(canAccept);
    }
    
    private void handleAccept() {
        Character hero = new Character(nameInput.toString(), CHARACTER_CLASSES[index]);
        GUIController.getInstance().setHero(hero);
        if (onMenuSwitch != null)
            onMenuSwitch.accept("SelectLevel");
    }
    
    @Override
    public String getMenuId() {
        return "NewChar";
    }
    
    /**
     * Handle key press with proper case preservation
     * Uses KeyEvent.getKeyChar() to get the actual character typed
     */
    @Override
    public void handleKeyPress(String keyText) {
        if (keyText.equals("Back Space") || keyText.equals("Backspace")) {
            if (nameInput.length() > 0) {
                nameInput.deleteCharAt(nameInput.length() - 1);
                nameDisplayLabel.setText(nameInput.toString());
                nameDisplayLabel.setForeground(Color.BLACK);
                updateStateDisplay();
                updateAcceptButton();
            }
        }
    }
    
    /**
     * Handle key typed event with actual character
     * This preserves upper/lower case properly
     */
    public void handleKeyTyped(char keyChar) {
        if (java.lang.Character.isISOControl(keyChar)) {
            return;
        }
        String input = String.valueOf(keyChar);
        ValidationResult result = nameValidator.validate(input);
        
        if (result.isValid()) {
            nameInput.append(keyChar);
            nameDisplayLabel.setText(nameInput.toString());
            nameDisplayLabel.setForeground(Color.BLACK);
            updateStateDisplay();
            updateAcceptButton();
        } else {
            nameDisplayLabel.setForeground(Color.RED);
            Timer timer = new Timer(500, evt -> nameDisplayLabel.setForeground(Color.BLACK));
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    @Override
    public boolean isKeyboardEnabled() {
        return true;
    }
    
    @Override
    public void onMenuActivated() {
        super.onMenuActivated();
    }
    
    @Override
    public void onMenuDeactivated() {
        super.onMenuDeactivated();
    }
}