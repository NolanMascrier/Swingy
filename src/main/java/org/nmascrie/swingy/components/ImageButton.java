package org.nmascrie.swingy.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * Custom button component with image, title, and description
 */
public class ImageButton extends JPanel {
    private static final Color SELECTED_COLOR = new Color(100, 149, 237);
    private static final Color HOVER_COLOR = new Color(230, 230, 230);
    private static final Color DEFAULT_COLOR = Color.WHITE;
    
    private JLabel imageLabel;
    private JLabel titleLabel;
    private JLabel descriptionLabel;
    private boolean selected = false;
    private final Runnable onClickCallback;

    private final int width;
    private final int height;
    
    public ImageButton(String title, String description, ImageIcon icon, Runnable onClick) {
        this.onClickCallback = onClick;
        this.width = 221;
        this.height = 568;
        initializeUI(title, description, icon);
    }

    public ImageButton(String title, String description, ImageIcon icon, Runnable onClick, int width, int height) {
        this.onClickCallback = onClick;
        this.width = width;
        this.height = height;
        initializeUI(title, description, icon);
    }
    
    private void initializeUI(String title, String description, ImageIcon icon) {
        setLayout(new BorderLayout(5, 5));
        setBackground(DEFAULT_COLOR);
        setBorder(createDefaultBorder());
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(180, 250));
        
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setIcon(createPlaceholderIcon(this.width, this.height, title));
        }
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(DEFAULT_COLOR);
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        descriptionLabel = new JLabel("<html>" + description + "</html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionLabel.setForeground(Color.GRAY);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(descriptionLabel);
        
        add(imageLabel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.SOUTH);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClickCallback != null) {
                    onClickCallback.run();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    setBackground(HOVER_COLOR);
                    updateComponentColors(HOVER_COLOR);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected) {
                    setBackground(DEFAULT_COLOR);
                    updateComponentColors(DEFAULT_COLOR);
                }
            }
        });
    }
    
    private ImageIcon createPlaceholderIcon(int width, int height, String text) {
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
            width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(200, 200, 255),
            width, height, new Color(150, 150, 255)
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, width, height, 20, 20);
        
        // Draw text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.drawString(text, (width - textWidth) / 2, (height + textHeight) / 2);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            setBackground(SELECTED_COLOR);
            updateComponentColors(SELECTED_COLOR);
            setBorder(createSelectedBorder());
            titleLabel.setForeground(Color.WHITE);
            descriptionLabel.setForeground(new Color(220, 220, 220));
        } else {
            setBackground(DEFAULT_COLOR);
            updateComponentColors(DEFAULT_COLOR);
            setBorder(createDefaultBorder());
            titleLabel.setForeground(Color.BLACK);
            descriptionLabel.setForeground(Color.GRAY);
        }
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    private void updateComponentColors(Color bgColor) {
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                comp.setBackground(bgColor);
            }
        }
    }
    
    private Border createDefaultBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }
    
    private Border createSelectedBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SELECTED_COLOR.darker(), 3),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }
}