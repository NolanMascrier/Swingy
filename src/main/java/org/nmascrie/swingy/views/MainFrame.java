package org.nmascrie.swingy.views;

import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Main application frame that manages all menus
 * Uses CardLayout to switch between different menu panels
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private final Map<String, BaseMenu> menus;
    private BaseMenu currentMenu;

    public MainFrame() {
        menus = new HashMap<>();
        initializeUI();
        registerMenus();
    }

    private void initializeUI() {
        setTitle("Somewhat profound Stone in Space");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 860);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        add(cardPanel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                handleKeyTyped(e);  // ← ADD THIS
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    /**
     * Register all menus here
     * Add new menus by creating instances and calling addMenu()
     */
    private void registerMenus() {
        addMenu(new MainMenu(this::showMenu));
        addMenu(new NewCharacter(this::showMenu));
        addMenu(new LoadMenu(this::showMenu));
        addMenu(new LevelMenu(this::showMenu));
        addMenu(new MapMenu(this::showMenu));
        addMenu(new BattleMenu(this::showMenu));
        showMenu("MainMenu");
    }

    /**
     * Add a menu to the frame
     * @param menu The menu to add
     */
    private void addMenu(BaseMenu menu) {
        String menuId = menu.getMenuId();
        menus.put(menuId, menu);
        cardPanel.add(menu, menuId);
        System.out.println("DEBUG:: Registered menu -> " + menuId);
    }

    /**
     * Show a specific menu by its ID
     * @param menuId The ID of the menu to show
     */
    public void showMenu(String menuId) {
        BaseMenu menu = menus.get(menuId);
        if (menu == null) {
            System.err.println("DEBUG:: Menu not found -> " + menuId);
            return;
        }

        if (currentMenu != null) {
            currentMenu.onMenuDeactivated();
        }

        currentMenu = menu;
        cardLayout.show(cardPanel, menuId);
        
        currentMenu.onMenuActivated();
        requestFocusInWindow();
        
        System.out.println("DEBUG:: Frame -> " + menuId);
    }

    /**
     * Handle keyboard input and delegate to current menu
     */
    private void handleKeyPress(KeyEvent e) {
        if (currentMenu == null) {
            return;
        }
        String keyText = KeyEvent.getKeyText(e.getKeyCode());
        
        if (currentMenu.isKeyboardEnabled()) {
            currentMenu.handleKeyPress(keyText);
        } else {
            currentMenu.handleKeyPress(keyText);
        }
    }

    /**
     * Get a specific menu instance (useful for updating menu state from outside)
     * @param menuId The ID of the menu
     * @return The menu instance, or null if not found
     */
    public BaseMenu getMenu(String menuId) {
        return menus.get(menuId);
    }

    public void display() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            requestFocusInWindow();
        });
    }

    private void handleKeyTyped(KeyEvent e) {
        if (currentMenu instanceof NewCharacter newCharacter)
            newCharacter.handleKeyTyped(e.getKeyChar());
    }
}