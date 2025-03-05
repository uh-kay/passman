import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;

public class EditForm {
    public JTextField editUsernameField;
    public JTextField editTitleField;
    public JPasswordField editPasswordField;
    public JTextField editDomainField;
    public JTextField editTagsField;

    AppConfig appConfig = new AppConfig();

    public JPanel createEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        editPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titLabel = new JLabel("Edit");
        editTitleField = new JTextField(20);
        editUsernameField = new JTextField(20);
        editPasswordField = new JPasswordField(20);
        editDomainField = new JTextField(20);
        editTagsField = new JTextField(20);
        JButton editButton = new JButton("Edit");
        JButton cancelButton = new JButton("Cancel");
        appConfig.styleButton(editButton, appConfig);
        appConfig.styleButton(cancelButton, appConfig);
        
        titLabel.setFont(appConfig.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        editPanel.add (titLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        editPanel.add(new JLabel("Title: "), gbc);
        gbc.gridx = 1;
        editPanel.add(editTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editPanel.add(new JLabel("Username: "), gbc);
        gbc.gridx = 1;
        editPanel.add(editUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        editPanel.add(new JLabel("Password: "), gbc);
        gbc.gridx = 1;
        editPanel.add(editPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        editPanel.add(new JLabel("Domain: "), gbc);
        gbc.gridx = 1;
        editPanel.add(editDomainField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        editPanel.add(new JLabel("Tag: "), gbc);
        gbc.gridx = 1;
        editPanel.add(editTagsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        editPanel.add(cancelButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        editPanel.add(editButton, gbc);

        cancelButton.addActionListener(_ -> DashboardForm.cardLayout.show(DashboardForm.cardPanel, DashboardForm.VIEW_PANEL));

        AppConnection appConnection = new AppConnection();

        editButton.addActionListener(_ -> {
            try {
                appConnection.editPassword(this);
            } catch (SQLException | ClassNotFoundException e) {
                appConnection.handleDatabaseError(e);
            }
        });

        return editPanel;
    }
}
