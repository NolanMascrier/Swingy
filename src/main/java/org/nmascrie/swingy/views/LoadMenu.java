package org.nmascrie.swingy.views;

import org.nmascrie.swingy.components.ImageButton;
import org.nmascrie.swingy.controllers.GUIController;
import org.nmascrie.swingy.enums.HeroClass;
import org.nmascrie.swingy.models.Character;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Menu for loading saved characters
 * Displays saved characters as scrollable image buttons
 */
public class LoadMenu extends BaseMenu {
    private final Consumer<String> onMenuSwitch;
    private JPanel charactersPanel;
    private JScrollPane scrollPane;
    private JButton backButton;
    private ArrayList<Character> savedCharacters;
    
    private static final Map<HeroClass, String> CLASS_IMAGES = new HashMap<HeroClass, String>() {{
        put(HeroClass.SCOUT, "/images/scout_m.png");
        put(HeroClass.GUNNER, "/images/gunner_m.png");
        put(HeroClass.ENGINEER, "/images/engineer_m.png");
        put(HeroClass.DRILLER, "/images/driller_m.png");
    }};
    
    private static final Map<HeroClass, String> CLASS_DESCRIPTIONS = new HashMap<HeroClass, String>() {{
        put(HeroClass.SCOUT, "Scout");
        put(HeroClass.GUNNER, "Gunner");
        put(HeroClass.ENGINEER, "Engineer");
        put(HeroClass.DRILLER, "Driller");
    }};
    
    public LoadMenu(Consumer<String> onMenuSwitch) {
        this.onMenuSwitch = onMenuSwitch;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 245, 245));
        
        JLabel titleLabel = new JLabel("Select your Dwarf", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        
        JLabel instructionLabel = new JLabel("Select a character to continue:", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        centerPanel.add(instructionLabel, BorderLayout.NORTH);
        
        charactersPanel = new JPanel();
        charactersPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        charactersPanel.setBackground(Color.WHITE);
        
        scrollPane = new JScrollPane(charactersPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(245, 245, 245));
        
        JButton createNewButton = new JButton("Create New Character");
        createNewButton.setFont(new Font("Arial", Font.BOLD, 16));
        createNewButton.setPreferredSize(new Dimension(200, 40));
        createNewButton.addActionListener(e -> {
            if (onMenuSwitch != null) {
                onMenuSwitch.accept("NewChar");
            }
        });
        
        backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.addActionListener(e -> {
            if (onMenuSwitch != null) {
                onMenuSwitch.accept("MainMenu");
            }
        });
        
        bottomPanel.add(createNewButton);
        bottomPanel.add(backButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Load all saved characters and display them as buttons
     */
    private void loadCharacters() {
        charactersPanel.removeAll();
        savedCharacters = GUIController.getInstance().loadAllSaves();
        
        if (savedCharacters == null || savedCharacters.isEmpty()) {
            JLabel noCharactersLabel = new JLabel("No saved characters found. Create a new one!");
            noCharactersLabel.setFont(new Font("Arial", Font.ITALIC, 18));
            noCharactersLabel.setForeground(Color.GRAY);
            charactersPanel.add(noCharactersLabel);
        } else {
            for (Character character : savedCharacters) {
                ImageButton charButton = createCharacterButton(character);
                charactersPanel.add(charButton);
            }
        }
        charactersPanel.revalidate();
        charactersPanel.repaint();
    }
    
    /**
     * Create an ImageButton for a saved character
     */
    private ImageButton createCharacterButton(Character character) {
        String name = character.getName();
        HeroClass heroClass = character.getClasse();
        String imagePath = CLASS_IMAGES.getOrDefault(heroClass, "/images/scout_e.png");
        String description = CLASS_DESCRIPTIONS.getOrDefault(heroClass, "Hero");
        String fullDescription = description + " - Level " + character.getLevel();
        
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getResource(imagePath));
        } catch (Exception e) {
            System.err.println("Could not load image: " + imagePath);
        }
        
        ImageButton button = new ImageButton(
            name,
            fullDescription,
            icon,
            () -> selectCharacter(character), 156, 169
        );
        return button;
    }
    
    /**
     * Handle character selection
     */
    private void selectCharacter(Character character) {
        GUIController.getInstance().setHero(character);
        if (onMenuSwitch != null) {
            onMenuSwitch.accept("SelectLevel");
        }
    }
    
    @Override
    public String getMenuId() {
        return "LoadChar";
    }
    
    @Override
    public void handleKeyPress(String keyText) {
        if (keyText.equals("Right") || keyText.equals("D")) {
            JScrollBar scrollBar = scrollPane.getHorizontalScrollBar();
            scrollBar.setValue(scrollBar.getValue() + 50);
        } else if (keyText.equals("Left") || keyText.equals("A")) {
            JScrollBar scrollBar = scrollPane.getHorizontalScrollBar();
            scrollBar.setValue(scrollBar.getValue() - 50);
        }
    }
    
    @Override
    public boolean isKeyboardEnabled() {
        return true;
    }
    
    @Override
    public void onMenuActivated() {
        super.onMenuActivated();
        loadCharacters();
    }
    
    @Override
    public void onMenuDeactivated() {
        super.onMenuDeactivated();
        if (savedCharacters != null) {
            savedCharacters.clear();
        }
    }
}