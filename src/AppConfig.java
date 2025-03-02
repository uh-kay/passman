import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;

public class AppConfig extends JFrame {
    private final Properties properties = new Properties();

    {
        try {
            FileInputStream input = new FileInputStream(".config.properties");
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Could not load config: " + e.getMessage());
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public final Color PRIMARY_COLOR = new Color(0, 102, 255);
    public final Color SECONDARY_COLOR = new Color(0, 51, 204);
    public final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    public final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);

    public void styleButton(JButton button, AppConfig appConfig) {
        button.setBackground(appConfig.PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(appConfig.BUTTON_FONT);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(appConfig.SECONDARY_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
    }
}
