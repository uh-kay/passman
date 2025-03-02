import javax.swing.*;
import java.awt.*;

public class DashboardForm extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // Card identifiers
    private static final String VIEW_PANEL = "VIEW_PANEL";
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

        JPanel navPanel = createNavPanel();

        JPanel addPanel = new AddForm().createAddPanel();
        JPanel editPanel = new EditForm().createEditPanel();

        cardPanel.add(addPanel, ADD_PANEL);
        cardPanel.add(editPanel, EDIT_PANEL);


        add(navPanel, BorderLayout.SOUTH);
        add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, VIEW_PANEL);
    }

    private JPanel createNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 3, 10, 10));
        navPanel.setBackground(appConfig.PRIMARY_COLOR);
        navPanel.setPreferredSize(new Dimension(getWidth(), 40));

        JButton viewButton = new JButton("View");
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");

        appConfig.styleButton(viewButton, appConfig);
        appConfig.styleButton(addButton, appConfig);
        appConfig.styleButton(editButton, appConfig);

        viewButton.addActionListener(_ -> cardLayout.show(cardPanel, VIEW_PANEL));
        addButton.addActionListener(_ -> cardLayout.show(cardPanel, ADD_PANEL));
        editButton.addActionListener(_ -> cardLayout.show(cardPanel, EDIT_PANEL));

        navPanel.add(viewButton);
        navPanel.add(addButton);
        navPanel.add(editButton);

        return navPanel;
    }

    public static void main(String[] args) {
        new DashboardForm().setVisible(true);
    }
}