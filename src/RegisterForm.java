import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    AppConfig appConfig = new AppConfig();

    private final String DB_URL = appConfig.get("DB_URL");
    private final String DB_USER = appConfig.get("DB_USER");
    private final String DB_PASSWORD = appConfig.get("DB_PASSWORD");
    private final String DB_DRIVER = appConfig.get("DB_DRIVER");

    private JTextField regUsernameField;
    private JPasswordField regPasswordField;
    private JPasswordField regConfirmPasswordField;

    public JPanel createRegistrationPanel() {
        var registrationPanel = new JPanel();
        registrationPanel.setLayout(new GridBagLayout());
        registrationPanel.setBackground(Color.WHITE);
        
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add title
        var titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(appConfig.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registrationPanel.add(titleLabel, gbc);

        // Create form fields
        regUsernameField = new JTextField(20);
        regPasswordField = new JPasswordField(20);
        regConfirmPasswordField = new JPasswordField(20);
        var registerButton = new JButton("Register");
        var clearButton = new JButton("Clear");
        
        appConfig.styleButton(registerButton, appConfig);
        appConfig.styleButton(clearButton, appConfig);

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
        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
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

    private void registerUser() {
        if (!validateRegistrationInputs()) {
            return;
        }
        
        var username = regUsernameField.getText();
        var password = new String(regPasswordField.getPassword());
        
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
                AuthenticationForm.cardLayout.show(AuthenticationForm.cardPanel, AuthenticationForm.LOGIN_PANEL);
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
        if (regPasswordField.getPassword().length < 4) {
            JOptionPane.showMessageDialog(this, 
                "Password must be at least 4 characters long.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Check if passwords match
        var password = new String(regPasswordField.getPassword());
        var confirmPassword = new String(regConfirmPasswordField.getPassword());
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void clearRegistrationFields() {
        regUsernameField.setText("");
        regPasswordField.setText("");
        regConfirmPasswordField.setText("");
    }

    private boolean isUsernameTaken(String username) throws SQLException, ClassNotFoundException {
        var query = "SELECT username FROM users WHERE username = ?";
        
        try (var connection = createDatabaseConnection();
             var statement = connection.prepareStatement(query)) {
            
            statement.setString(1, username);
            try (var resultSet = statement.executeQuery()) {
                return resultSet.next(); // Returns true if username exists
            }
        }
    }

    private boolean insertNewUser(String username, String password) 
        throws SQLException, ClassNotFoundException {
            var query = "INSERT INTO users (username, password) VALUES (?, ?)";
        
            try (var connection = createDatabaseConnection();
                var statement = connection.prepareStatement(query)) {
                
                statement.setString(1, username);
                statement.setString(2, password);
                
                var rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            }
    }

    private Connection createDatabaseConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private void handleDatabaseError(Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Database Error: " + e.getMessage(), 
            "Connection Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
