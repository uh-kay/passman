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

    public JPanel createAddPanel() {
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridBagLayout());
        addPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titLabel = new JLabel("Edit");
        editTitleField = new JTextField(20);
        editUsernameField = new JTextField(20);
        editPasswordField = new JPasswordField(20);
        editDomainField = new JTextField(20);
        JButton editButton = new JButton("Edit");
        JButton cancelButton = new JButton("Cancel");
        appConfig.styleButton(editButton, appConfig);
        
        titLabel.setFont(appConfig.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addPanel.add (titLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        addPanel.add(new JLabel("Title: "), gbc);
        gbc.gridx = 1;
        addPanel.add(editTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addPanel.add(new JLabel("Username: "), gbc);
        gbc.gridx = 1;
        addPanel.add(editUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addPanel.add(new JLabel("Password: "), gbc);
        gbc.gridx = 1;
        addPanel.add(editPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        addPanel.add(new JLabel("Domain: "), gbc);
        gbc.gridx = 1;
        addPanel.add(editDomainField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        addPanel.add(new JLabel("Tags: "), gbc);
        gbc.gridx = 1;
        addPanel.add(editTagsField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        addPanel.add(cancelButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        addPanel.add(editButton, gbc);

        AppConnection appConnection = new AppConnection();

        editButton.addActionListener(_ -> {
            try {
                appConnection.editPassword(this);
            } catch (SQLException | ClassNotFoundException e) {
                appConnection.handleDatabaseError(e);
            }
        });

        return addPanel;
    }
}
