import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DashboardForm extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // Database configuration constants
    private static final String DB_URL = Config.get("DB_URL");
    private static final String DB_USER = Config.get("DB_USER");
    private static final String DB_PASSWORD = Config.get("DB_PASSWORD");
    private static final String DB_DRIVER = Config.get("DB_DRIVER");

    // Card identifiers
    private static final String VIEW_PANEL = "VIEW_PANEL";
    private static final String ADD_PANEL = "ADD_PANEL";
    
    public DashboardForm() {
        initializeFrame();
        setupCardLayout();
    }
    
    private void initializeFrame() {
        setTitle("Admin DashboardForm");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void setupCardLayout() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel navPanel = createNavPanel();

        add(navPanel, BorderLayout.SOUTH);
        add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, VIEW_PANEL);
    }

    private JPanel createNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 2, 10, 10));
        navPanel.setBackground(Config.PRIMARY_COLOR);
        navPanel.setPreferredSize(new Dimension(getWidth(), 40));

        JButton viewButton = new JButton("View");
        JButton addButton = new JButton("Add");

        styleButton(viewButton);
        styleButton(addButton);

        viewButton.addActionListener(_ -> cardLayout.show(cardPanel, VIEW_PANEL));
        addButton.addActionListener(_ -> cardLayout.show(cardPanel, ADD_PANEL));

        navPanel.add(viewButton);
        navPanel.add(addButton);

        return navPanel;
    }

    private void styleButton(JButton button) {
        button.setBackground(Config.PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(Config.BUTTON_FONT);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Config.SECONDARY_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
    }

    private Connection createDatabaseConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private boolean insertPassword(String title, String username, String password)
        throws SQLException, ClassNotFoundException {
            
            String title = addTitleField.getText();
            String username = addUsernameFIeld.getText();
            String password = new String(addPasswordField.getPassword());

            String query = "INSERT INTO passwords (title, username, password) VALUES (?, ?, ?)";

            try (Connection connection = createDatabaseConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, title);
                statement.setString(2, username);
                statement.setString(3, password);

                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException | ClassNotFoundException e) {
                handleDatabaseError(e);
            }
        }

        private void handleDatabaseError(Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Database Error: " + e.getMessage(), 
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }


    public static void main(String[] args) {
        new DashboardForm().setVisible(true);
    }
}