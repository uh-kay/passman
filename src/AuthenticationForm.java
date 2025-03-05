import javax.swing.*;
import java.awt.*;

public class AuthenticationForm extends JFrame {
    public static JPanel cardPanel;
    public static CardLayout cardLayout;

    // Card identifiers
    public static final String LOGIN_PANEL = "LOGIN_PANEL";
    public static final String REGISTER_PANEL = "REGISTER_PANEL";

    AppConfig appConfig = new AppConfig();

    public AuthenticationForm() {
        initializeFrame();
        setupCardLayout();
    }

    public void initializeFrame() {
        setTitle("Passman");
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
        var navPanel = createNavigationPanel();

        // Create content panels
        var loginPanel = new LoginForm().createLoginPanel(this);

        var registerForm = new RegisterForm();
        var registrationPanel = registerForm.createRegistrationPanel();

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
        var navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 2, 10, 10));
        navPanel.setBackground(appConfig.PRIMARY_COLOR);
        navPanel.setPreferredSize(new Dimension(getWidth(), 40));

        var loginBtn = new JButton("Login");
        var registerBtn = new JButton("Register");

        appConfig.styleButton(loginBtn, appConfig);
        appConfig.styleButton(registerBtn, appConfig);

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
            new AuthenticationForm().setVisible(true);
        });
    }
}