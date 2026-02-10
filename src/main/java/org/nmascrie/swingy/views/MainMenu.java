package org.nmascrie.swingy.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.nmascrie.swingy.validator.ClickValidator;

/**
 * Menu 1: Click-only interface
 * Demonstrates a menu where keyboard input is disabled
 */
public class MainMenu extends BaseMenu {
    private JLabel statusLabel;
    private final ClickValidator clickValidator;
    private final Consumer<String> onMenuSwitch;

    public MainMenu(Consumer<String> onMenuSwitch) {
        this.onMenuSwitch = onMenuSwitch;
        this.clickValidator = new ClickValidator();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("Welcome to the Space Rig", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBackground(new Color(240, 240, 240));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        String[] boxLabels = {"Create a new Dwarf", "Load an existing Dwarf"};
        String[] images = {"/images/egg.png", "/images/load.png"};
        Color[] boxColors = {
            new Color(148, 148, 148),
            new Color(148, 148, 148)
        };

        for (int i = 0; i < boxLabels.length; i++) {
            JPanel box = createClickableBox(boxLabels[i], boxColors[i], images[i]);
            centerPanel.add(box);
        }

        add(centerPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Welcome to Hoxxes 3", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel createClickableBox(String text, Color color, String url) {
        JPanel box = new JPanel(new GridBagLayout());
        box.setBackground(color);
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        box.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ImageIcon icon = new ImageIcon(getClass().getResource(url));
        Image img = icon.getImage();
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 32, 256, 256, null, null);
        icon = new ImageIcon(bi);
        JLabel label = new JLabel(text, icon, SwingConstants.LEFT);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        box.add(label);

        box.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(text);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                box.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                box.setBackground(color);
            }
        });

        return box;
    }

    private void handleClick(String option) {
        statusLabel.setForeground(Color.BLACK);
        ClickValidator.ValidationResult result = clickValidator.validateOption(option);
        System.out.println("Click validation: " + result.getMessage());
        if (option.equals("Create a new Dwarf")) {
            if (onMenuSwitch != null) {
                onMenuSwitch.accept("NewChar");
            }
        }
        if (option.equals("Load an existing Dwarf")) {
            if (onMenuSwitch != null) {
                onMenuSwitch.accept("LoadChar");
            }
        }
    }

    @Override
    public String getMenuId() {
        return "MainMenu";
    }

    @Override
    public void handleKeyPress(String keyText) {
        System.out.println("Nice try; Only clicks in here.");
        statusLabel.setForeground(Color.RED);
        
        Timer timer = new Timer(2000, evt -> statusLabel.setForeground(Color.BLACK));
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public boolean isKeyboardEnabled() {
        return false;
    }

    @Override
    public void onMenuActivated() {
        super.onMenuActivated();
        statusLabel.setText("Click on any option above");
        statusLabel.setForeground(Color.BLACK);
    }
}