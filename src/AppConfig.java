import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

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

    public static void styleTable(JTable itemTable, AppConfig appConfig) {
        // Increase row height
        itemTable.setRowHeight(30);
        
        // Style table header
        JTableHeader header = itemTable.getTableHeader();
        header.setBackground(appConfig.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        // Universal cell renderer for all columns
        DefaultTableCellRenderer universalRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, 
                boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                // Remove cell borders
                ((JComponent)c).setBorder(BorderFactory.createEmptyBorder());
                
                // Set colors for selected and unselected rows
                if (isSelected) {
                    // Highlight color for selected row
                    c.setBackground(new Color(100, 149, 237, 200)); // Cornflower blue with transparency
                    c.setForeground(Color.BLACK);
                } else {
                    // Alternating row colors for unselected rows
                    if (row % 2 == 0) {
                        c.setBackground(new Color(173, 216, 230, 100)); // Light blue with transparency
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                }
                
                return c;
            }
        };

        // Apply the universal renderer to all columns
        TableColumnModel columnModel = itemTable.getColumnModel();
        for (int i = 0; i < itemTable.getColumnCount(); i++) {
            // Specifically skip the password column at index 3
            if (i != 3) {
                columnModel.getColumn(i).setCellRenderer(universalRenderer);
            }
        }
        
        // Special renderer for password column at index 3
        columnModel.getColumn(3).setCellRenderer((_, _, isSelected, _, row, _) -> {
            JLabel label = new JLabel("********");
            label.setHorizontalAlignment(JLabel.CENTER);
            
            if (isSelected) {
                label.setBackground(new Color(100, 149, 237, 200)); // Cornflower blue with transparency
                label.setForeground(Color.BLACK);
                label.setOpaque(true);
            } else {
                // Maintain alternating row colors
                if (row % 2 == 0) {
                    label.setBackground(new Color(173, 216, 230, 100));
                } else {
                    label.setBackground(Color.WHITE);
                }
                label.setOpaque(true);
            }
            
            return label;
        });
        
        // Remove table grid lines
        itemTable.setShowGrid(false);
        itemTable.setIntercellSpacing(new Dimension(0, 0));
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.setPreferredScrollableViewportSize(new Dimension(600, 400));
        itemTable.setFillsViewportHeight(true);
        // itemTable.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    }
}
