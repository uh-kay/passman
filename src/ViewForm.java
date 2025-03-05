import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewForm {
    private JTable itemTable;
    private DefaultTableModel tableModel;

    AppConfig appConfig = new AppConfig();

    public JPanel createViewPanel() {
        var viewPanel = new JPanel();
        viewPanel.setLayout(new GridBagLayout());
        viewPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        
        String[] columnNames = {"ID", "Title", "Username", "Password", "Domain", "Tag"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        itemTable = new JTable(tableModel);
        AppConfig.styleTable(itemTable, appConfig);

        var scrollPane = new JScrollPane(itemTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        viewPanel.add(scrollPane, gbc);
        
        var appConnection = new AppConnection();
        try {
            appConnection.loadDataFromDatabase(tableModel, this);
        } catch (ClassNotFoundException | SQLException e) {
            appConnection.handleDatabaseError(e);
        }
        
        return viewPanel;
    }

    public JTable getItemTable() {
        return itemTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void refreshData() {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        
        AppConnection appConnection = new AppConnection();
        try {
            appConnection.loadDataFromDatabase(tableModel, this);
        } catch (ClassNotFoundException | SQLException e) {
            appConnection.handleDatabaseError(e);
        }
    }

    public void copyPasswordToClipboard() {
        int selectedRow = itemTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                null, 
                "Please select a row to copy password", 
                "No Row Selected", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String password = (String) tableModel.getValueAt(selectedRow, 3);
        
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(password);
        clipboard.setContents(stringSelection, null);
    }

    public void copyUsernameToClipboard() {
        int selectedRow = itemTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                null, 
                "Please select a row to copy username", 
                "No Row Selected", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String username = (String) tableModel.getValueAt(selectedRow, 2);
        
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(username);
        clipboard.setContents(stringSelection, null);
    }
}