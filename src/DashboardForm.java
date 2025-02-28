import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DashboardForm extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JTextField addUsernameField;
    private JTextField addTittleField;
    private JPasswordField addPasswordField;

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
        JPanel addPanel = createaddPanel();

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

    private JPanel createaddPanel() {
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridBagLayout());
        addPanel.setBackground(Color.WHITE);
        addPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Config.PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titLabel = new JLabel("ADD");
        addTittleField = new JTextField(20);
        addUsernameField = new JTextField(20);
        addPasswordField = new JPasswordField(20);
        JButton addButton = new JButton("add");
        styleButton(addButton);
        
        titLabel.setFont(Config.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addPanel.add (titLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        addPanel.add(new JLabel("Tittle: "), gbc);
        gbc.gridx = 1;
        addPanel.add(addTittleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addPanel.add(new JLabel("Username: "), gbc);
        gbc.gridx = 1;
        addPanel.add(addUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addPanel.add(new JLabel("Password: "), gbc);
        gbc.gridx = 2;
        addPanel.add(addPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        addPanel.add(addButton, gbc);

        return addPanel;
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