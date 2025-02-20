import javax.swing.*;

public class Form {
    public static void main(String args[]) {
        JFrame frame = new JFrame();

        frame.setTitle("Login Form");
        frame.setSize(320, 300);

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setBounds(125, 20, 80, 30);
        frame.add(loginLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 60, 80, 30);
        frame.add(usernameLabel);
        
        JTextField usernameField = new JTextField();
        usernameField.setBounds(120, 60, 150, 30);
        frame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 100, 80, 30);
        frame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(120, 100, 150, 30);
        frame.add(passwordField);

        JCheckBox rememberMe = new JCheckBox("Remember me");
        rememberMe.setBounds(120, 130, 150, 30);
        frame.add(rememberMe);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(180, 200, 80, 30);
        frame.add(loginBtn);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(80, 200, 80, 30);
        frame.add(cancelButton);

        frame.setLayout(null);
        frame.setVisible(true);
    }
}
