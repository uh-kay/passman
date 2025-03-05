import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    JTextField loginUsernameField;
    JPasswordField loginPasswordField;

    AppConfig appConfig = new AppConfig();

    public JPanel createLoginPanel(AuthenticationForm authenticationForm) {
        var loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);

        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add title
        var titleLabel = new JLabel("Login");
        titleLabel.setFont(appConfig.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        // Create form fields
        loginUsernameField = new JTextField(20);
        loginPasswordField = new JPasswordField(20);
        var loginButton = new JButton("Login");
        appConfig.styleButton(loginButton, appConfig);

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
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        loginPanel.add(loginButton, gbc);

        // Add action listener
        var appConnection = new AppConnection();
        loginButton.addActionListener(_ -> {
            if (appConnection.authenticate(this)) {
                authenticationForm.openDashboard();
            }
        });

        return loginPanel;
    }
}