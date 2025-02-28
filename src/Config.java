import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream input = new FileInputStream("config.properties");
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
}
