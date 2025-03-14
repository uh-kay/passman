import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;

public class AddForm {
    public JTextField addUsernameField;
    public JTextField addTitleField;
    public JPasswordField addPasswordField;
    public JTextField addDomainField;
    public JTextField addTagField;

    AppConfig appConfig = new AppConfig();

    public JPanel createAddPanel(int userId) {
        var addPanel = new JPanel();
        addPanel.setLayout(new GridBagLayout());
        addPanel.setBackground(Color.WHITE);
        
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        var titLabel = new JLabel("Add");
        addTitleField = new JTextField(20);
        addUsernameField = new JTextField(20);
        addPasswordField = new JPasswordField(20);
        addDomainField = new JTextField(20);
        addTagField = new JTextField(20);
        var addButton = new JButton("Add");
        var cancelButton = new JButton("Cancel");
        appConfig.styleButton(addButton, appConfig);
        appConfig.styleButton(cancelButton, appConfig);
        
        titLabel.setFont(appConfig.TITLE_FONT);
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

        gbc.gridx = 0;
        gbc.gridy = 4;
        addPanel.add(new JLabel("Domain: "), gbc);
        gbc.gridx = 1;
        addPanel.add(addDomainField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        addPanel.add(new JLabel("Tag: "), gbc);
        gbc.gridx = 1;
        addPanel.add(addTagField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        addPanel.add(cancelButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        addPanel.add(addButton, gbc);

        cancelButton.addActionListener(_ -> DashboardForm.cardLayout.show(DashboardForm.cardPanel, DashboardForm.VIEW_PANEL));

        var appConnection = new AppConnection();

        addButton.addActionListener(_ -> {
            try {
                appConnection.insertPassword(this, userId);
                clearFields();
                DashboardForm.refreshViewPanel();
                DashboardForm.cardLayout.show(DashboardForm.cardPanel, DashboardForm.VIEW_PANEL);
            } catch (SQLException | ClassNotFoundException e) {
                appConnection.handleDatabaseError(e);
            }
        });


        return addPanel;
    }

    private void clearFields() {
        addTitleField.setText("");
        addUsernameField.setText("");
        addPasswordField.setText("");
        addDomainField.setText("");
        addTagField.setText("null");
    }
}
