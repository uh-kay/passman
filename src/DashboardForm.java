import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DashboardForm extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    public JTextField addUsernameField;
    public JTextField addTitleField;
    public JPasswordField addPasswordField;

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
        JPanel addPanel = createAddPanel();

        cardPanel.add(addPanel, ADD_PANEL);


        add(navPanel, BorderLayout.SOUTH);
        add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, VIEW_PANEL);
    }

    private JPanel createNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 2, 10, 10));
        navPanel.setBackground(AppConfig.PRIMARY_COLOR);
        navPanel.setPreferredSize(new Dimension(getWidth(), 40));

        JButton viewButton = new JButton("View");
        JButton addButton = new JButton("Add");

        AppConfig.styleButton(viewButton);
        AppConfig.styleButton(addButton);

        viewButton.addActionListener(_ -> cardLayout.show(cardPanel, VIEW_PANEL));
        addButton.addActionListener(_ -> cardLayout.show(cardPanel, ADD_PANEL));

        navPanel.add(viewButton);
        navPanel.add(addButton);

        return navPanel;
    }

    private JPanel createAddPanel() {
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridBagLayout());
        addPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titLabel = new JLabel("Add");
        addTitleField = new JTextField(20);
        addUsernameField = new JTextField(20);
        addPasswordField = new JPasswordField(20);
        JButton addButton = new JButton("Add");
        AppConfig.styleButton(addButton);
        
        titLabel.setFont(AppConfig.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addPanel.add (titLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        addPanel.add(new JLabel("Title: "), gbc);
        gbc.gridx = 1;
        addPanel.add(addTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addPanel.add(new JLabel("Username: "), gbc);
        gbc.gridx = 1;
        addPanel.add(addUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addPanel.add(new JLabel("Password: "), gbc);
        gbc.gridx = 1;
        addPanel.add(addPasswordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        addPanel.add(addButton, gbc);

        AppConnection appConnection = new AppConnection();

        addButton.addActionListener(_ -> {
            try {
                appConnection.insertPassword(this);
            } catch (SQLException | ClassNotFoundException e) {
                appConnection.handleDatabaseError(e);
            }
        });

        return addPanel;
    }

    public static void main(String[] args) {
        new DashboardForm().setVisible(true);
    }
}