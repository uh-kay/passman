import javax.swing.*;
import java.awt.*;

public class AuthenticationManager extends JFrame {
    public static JPanel cardPanel;
    public static CardLayout cardLayout;

    // Card identifiers
    public static final String LOGIN_PANEL = "LOGIN_PANEL";
    public static final String REGISTER_PANEL = "REGISTER_PANEL";

    public AuthenticationManager() {
        initializeFrame();
        setupCardLayout();
    }

    public void initializeFrame() {
        setTitle("Login System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    public void setupCardLayout() {
        // Create card layout and panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create navigation panel
        JPanel navPanel = createNavigationPanel();

        // Create content panels
        JPanel loginPanel = new LoginForm().createLoginPanel(this);

        RegisterForm registerForm = new RegisterForm();
        JPanel registrationPanel = registerForm.createRegistrationPanel();

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
        navPanel.setBackground(AppConfig.PRIMARY_COLOR);
        navPanel.setPreferredSize(new Dimension(getWidth(), 40));

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        AppConfig.styleButton(loginBtn);
        AppConfig.styleButton(registerBtn);

        loginBtn.addActionListener(_ -> cardLayout.show(cardPanel, LOGIN_PANEL));
        registerBtn.addActionListener(_ -> cardLayout.show(cardPanel, REGISTER_PANEL));

        navPanel.add(loginBtn);
        navPanel.add(registerBtn);

        return navPanel;
    }

    public void openDashboard() {
        new DashboardForm().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AuthenticationManager().setVisible(true);
        });
    }
}