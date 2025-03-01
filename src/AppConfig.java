import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class AppConfig {
    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream input = new FileInputStream(".config.properties");
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Could not load config: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static final Color PRIMARY_COLOR = new Color(0, 102, 255);
    public static final Color SECONDARY_COLOR = new Color(0, 51, 204);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);

    public static void styleButton(JButton button) {
        button.setBackground(AppConfig.PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(AppConfig.BUTTON_FONT);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppConfig.SECONDARY_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
    }
}
