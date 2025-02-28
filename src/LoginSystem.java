import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginSystem extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    // Login panel components
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    
    // Registration panel components
    private JTextField regUsernameField;
    private JPasswordField regPasswordField;
    private JPasswordField regConfirmPasswordField;

    // Database configuration constants
    private static final String DB_URL = Config.get("DB_URL");
    private static final String DB_USER = Config.get("DB_USER");
    private static final String DB_PASSWORD = Config.get("DB_PASSWORD");
    private static final String DB_DRIVER = Config.get("DB_DRIVER");

    // Card identifiers
    private static final String LOGIN_PANEL = "LOGIN_PANEL";
    private static final String REGISTER_PANEL = "REGISTER_PANEL";
    
    public LoginSystem() {
        initializeFrame();
        setupCardLayout();
    }
    
    private void initializeFrame() {
        setTitle("Login System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }
    
    private void setupCardLayout() {
        // Create card layout and panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Create navigation panel
        JPanel navPanel = createNavigationPanel();
        
        // Create content panels
        JPanel loginPanel = createLoginPanel();
        JPanel registrationPanel = createRegistrationPanel();
        
        // Add panels to card layout
        cardPanel.add(loginPanel, LOGIN_PANEL);
        cardPanel.add(registrationPanel, REGISTER_PANEL);
        
        // Add components to frame
        add(navPanel, BorderLayout.SOUTH);
        add(cardPanel, BorderLayout.CENTER);
        
        // Show login panel by default
        cardLayout.show(cardPanel, LOGIN_PANEL);
    }
    
    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 2, 10, 10));
        navPanel.setBackground(Config.PRIMARY_COLOR);
        navPanel.setPreferredSize(new Dimension(getWidth(), 40));

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        styleButton(loginBtn);
        styleButton(registerBtn);
        
        loginBtn.addActionListener(_ -> cardLayout.show(cardPanel, LOGIN_PANEL));
        registerBtn.addActionListener(_ -> cardLayout.show(cardPanel, REGISTER_PANEL));

        navPanel.add(loginBtn);
        navPanel.add(registerBtn);
        
        return navPanel;
    }
    
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add title
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(Config.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        // Create form fields
        loginUsernameField = new JTextField(20);
        loginPasswordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);

        // Add username field
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(new JLabel("Username: "), gbc);
        gbc.gridx = 1;
        loginPanel.add(loginUsernameField, gbc);

        // Add password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(new JLabel("Password: "), gbc);
        gbc.gridx = 1;
        loginPanel.add(loginPasswordField, gbc);

        // Add login button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        loginPanel.add(loginButton, gbc);

        // Add action listener
        loginButton.addActionListener(_ -> authenticate());
        
        return loginPanel;
    }
    
    private JPanel createRegistrationPanel() {
        JPanel registrationPanel = new JPanel();
        registrationPanel.setLayout(new GridBagLayout());
        registrationPanel.setBackground(Color.WHITE);
        registrationPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Config.PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(Config.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registrationPanel.add(titleLabel, gbc);

        // Create form fields
        regUsernameField = new JTextField(20);
        regPasswordField = new JPasswordField(20);
        regConfirmPasswordField = new JPasswordField(20);
        JButton registerButton = new JButton("Register");
        JButton clearButton = new JButton("Clear");
        
        styleButton(registerButton);
        styleButton(clearButton);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        registrationPanel.add(new JLabel("Username: "), gbc);
        gbc.gridx = 1;
        registrationPanel.add(regUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        registrationPanel.add(new JLabel("Password: "), gbc);
        gbc.gridx = 1;
        registrationPanel.add(regPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        registrationPanel.add(new JLabel("Confirm Password: "), gbc);
        gbc.gridx = 1;
        registrationPanel.add(regConfirmPasswordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        registrationPanel.add(buttonPanel, gbc);

        // Add action listeners
        registerButton.addActionListener(_ -> registerUser());
        clearButton.addActionListener(_ -> clearRegistrationFields());
        
        return registrationPanel;
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
    
    private void authenticate() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());
        
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username and password cannot be empty", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (Connection connection = createDatabaseConnection();
             PreparedStatement statement = prepareAuthenticationStatement(connection, username, password);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                openDashboard();
            } else {
                showLoginError();
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            handleDatabaseError(e);
        }
    }
    
    private Connection createDatabaseConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    private PreparedStatement prepareAuthenticationStatement(Connection connection, String username, String password) 
            throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        return statement;
    }
    
    private void openDashboard() {
        new DashboardForm().setVisible(true);
        this.dispose();
    }
    
    private void showLoginError() {
        JOptionPane.showMessageDialog(this, 
            "Wrong username or password!", 
            "Authentication Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void registerUser() {
        if (!validateRegistrationInputs()) {
            return;
        }
        
        String username = regUsernameField.getText();
        String password = new String(regPasswordField.getPassword());
        
        try {
            if (isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(this, 
                    "Username already exists. Please choose a different one.", 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (insertNewUser(username, password)) {
                JOptionPane.showMessageDialog(this, 
                    "Registration successful! Please login with your new account.", 
                    "Registration Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                clearRegistrationFields();
                cardLayout.show(cardPanel, LOGIN_PANEL);
            }
        } catch (SQLException | ClassNotFoundException e) {
            handleDatabaseError(e);
        }
    }
    
    private boolean validateRegistrationInputs() {
        // Check for empty fields
        if (regUsernameField.getText().trim().isEmpty() || 
            regPasswordField.getPassword().length == 0 ||
            regConfirmPasswordField.getPassword().length == 0) {
            
            JOptionPane.showMessageDialog(this, 
                "All fields are required.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Check password length
        if (regPasswordField.getPassword().length < 6) {
            JOptionPane.showMessageDialog(this, 
                "Password must be at least 6 characters long.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Check if passwords match
        String password = new String(regPasswordField.getPassword());
        String confirmPassword = new String(regConfirmPasswordField.getPassword());
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private boolean isUsernameTaken(String username) throws SQLException, ClassNotFoundException {
        String query = "SELECT username FROM users WHERE username = ?";
        
        try (Connection connection = createDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Returns true if username exists
            }
        }
    }
    
    private boolean insertNewUser(String username, String password) 
            throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        
        try (Connection connection = createDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, username);
            statement.setString(2, password);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    private void clearRegistrationFields() {
        regUsernameField.setText("");
        regPasswordField.setText("");
        regConfirmPasswordField.setText("");
    }
    
    private void handleDatabaseError(Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Database Error: " + e.getMessage(), 
            "Connection Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginSystem().setVisible(true);
        });
    }
}