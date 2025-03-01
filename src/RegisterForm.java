import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    private static final String DB_URL = AppConfig.get("DB_URL");
    private static final String DB_USER = AppConfig.get("DB_USER");
    private static final String DB_PASSWORD = AppConfig.get("DB_PASSWORD");
    private static final String DB_DRIVER = AppConfig.get("DB_DRIVER");

    private JTextField regUsernameField;
    private JPasswordField regPasswordField;
    private JPasswordField regConfirmPasswordField;

    public JPanel createRegistrationPanel() {
        JPanel registrationPanel = new JPanel();
        registrationPanel.setLayout(new GridBagLayout());
        registrationPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(AppConfig.TITLE_FONT);
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
        
        AppConfig.styleButton(registerButton);
        AppConfig.styleButton(clearButton);

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
                AuthenticationManager.cardLayout.show(AuthenticationManager.cardPanel, AuthenticationManager.LOGIN_PANEL);
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

    private void clearRegistrationFields() {
        regUsernameField.setText("");
        regPasswordField.setText("");
        regConfirmPasswordField.setText("");
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
