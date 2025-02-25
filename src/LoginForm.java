import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginForm() {
        setTitle("Login Form");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        
        add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        add(usernameField);
 
        add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        add(passwordField);
        
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        add(loginButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
    
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan Password harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                authenticate(username, password);
            }
        }
    }

    private void authenticate(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/pos_db";
        String user = "root";
        String pass = "1234";
    
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url, user, pass);

            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setString(1, username);
            statement.setString(2, password);
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("supervisor")) {
                    new DashboardForm().setVisible(true);
                } else if (role.equals("cashier")) {
                    // new KasirForm().setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}