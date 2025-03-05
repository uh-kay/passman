import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.SQLException;

public class DashboardForm extends JFrame {
    public static JPanel cardPanel;
    public static CardLayout cardLayout;
    private AppConnection appConnection;
    private static ViewForm viewForm;

    // Card identifiers
    public static final String VIEW_PANEL = "VIEW_PANEL";
    private static final String ADD_PANEL = "ADD_PANEL";
    private static final String EDIT_PANEL = "EDIT_PANEL";

    AppConfig appConfig = new AppConfig();
    
    public DashboardForm() {
        initializeFrame();
        setupCardLayout();
    }
    
    private void initializeFrame() {
        setTitle("Passman");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void setupCardLayout() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create a single instance of ViewForm
        viewForm = new ViewForm();
        JPanel viewPanel = viewForm.createViewPanel();
        
        JPanel addPanel = new AddForm().createAddPanel();
        JPanel editPanel = new EditForm().createEditPanel();

        cardPanel.add(viewPanel, VIEW_PANEL);
        cardPanel.add(addPanel, ADD_PANEL);
        cardPanel.add(editPanel, EDIT_PANEL);

        JPanel navPanel = createNavPanel();
        
        add(navPanel, BorderLayout.SOUTH);
        add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, VIEW_PANEL);
    }

    private JPanel createNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 3, 10, 10));
        navPanel.setBackground(appConfig.PRIMARY_COLOR);
        navPanel.setPreferredSize(new Dimension(getWidth(), 40));

        Icon addIcon = new ImageIcon("assets\\add.png");
        Icon deleteIcon = new ImageIcon("assets\\delete.png");
        Icon editIcon = new ImageIcon("assets\\edit.png");
        Icon copyUsernameIcon = new ImageIcon("assets\\username-copy.png");
        Icon copyPasswordIcon = new ImageIcon("assets\\password-copy.png");
        Icon lockIcon = new ImageIcon("assets\\database-lock.png");

        JButton deleteButton = new JButton(deleteIcon);
        JButton addButton = new JButton(addIcon);
        JButton editButton = new JButton(editIcon);
        JButton copyPasswordButton = new JButton(copyPasswordIcon);
        JButton copyUsernameButton = new JButton(copyUsernameIcon);
        JButton lockButton = new JButton(lockIcon);

        appConfig.styleButton(deleteButton, appConfig);
        appConfig.styleButton(addButton, appConfig);
        appConfig.styleButton(editButton, appConfig);
        appConfig.styleButton(copyPasswordButton, appConfig);
        appConfig.styleButton(copyUsernameButton, appConfig);
        appConfig.styleButton(lockButton, appConfig);

        appConnection = new AppConnection();

        deleteButton.addActionListener(_ -> appConnection.deleteSelectedItem(viewForm));
        addButton.addActionListener(_ -> cardLayout.show(cardPanel, ADD_PANEL));
        editButton.addActionListener(_ -> cardLayout.show(cardPanel, EDIT_PANEL));
        lockButton.addActionListener(_ -> {
            try {
                appConnection.performLogout(this);
            } catch (SQLException | ClassNotFoundException e) {
                appConnection.handleDatabaseError(e);
            }
        });

        navPanel.add(addButton);
        navPanel.add(copyUsernameButton);
        navPanel.add(copyPasswordButton);
        navPanel.add(editButton);
        navPanel.add(deleteButton);
        navPanel.add(lockButton);

        return navPanel;
    }

    public static void refreshViewPanel() {
        if (viewForm != null) {
            // Clear existing data
            DefaultTableModel model = viewForm.getTableModel();
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
            
            // Reload data
            AppConnection appConnection = new AppConnection();
            try {
                appConnection.loadDataFromDatabase(model, viewForm);
            } catch (ClassNotFoundException | SQLException e) {
                appConnection.handleDatabaseError(e);
            }
        }
    }

    public static void main(String[] args) {
        new DashboardForm().setVisible(true);
    }
}