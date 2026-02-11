package org.nmascrie.swingy.views;

import javax.swing.JPanel;

/**
 * Base class for all menu panels
 * Each menu should extend this class and implement the required methods
 */
public abstract class BaseMenu extends JPanel {
    
    /**
     * Get the unique identifier for this menu
     * Used by CardLayout to switch between menus
     */
    public abstract String getMenuId();
    
    /**
     * Handle keyboard input for this menu
     * @param keyText The text representation of the key pressed
     */
    public abstract void handleKeyPress(String keyText);
    
    /**
     * Called when this menu becomes active
     * Use this to reset state, request focus, etc.
     */
    public void onMenuActivated() {
        requestFocusInWindow();
    }
    
    /**
     * Called when this menu is deactivated (switching away)
     * Use this to clean up resources if needed
     */
    public void onMenuDeactivated() {}
    
    /**
     * Check if keyboard input is enabled for this menu
     * @return true if keyboard input should be processed, false otherwise
     */
    public abstract boolean isKeyboardEnabled();
}