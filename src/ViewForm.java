import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewForm {
    private JTable itemTable;
    private DefaultTableModel tableModel;

    AppConfig appConfig = new AppConfig();

    public JPanel createViewPanel() {
        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new GridBagLayout());
        viewPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        
        // Create column names and data for the table
        String[] columnNames = {"ID", "Title", "Username", "Password", "Domain", "Tag", "Creation Date", "Modified Date"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        itemTable = new JTable(tableModel);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.setPreferredScrollableViewportSize(new Dimension(600, 400));
        itemTable.setFillsViewportHeight(true);
        
        itemTable.getColumnModel().getColumn(3).setCellRenderer((tbl, _, isSelected, _, _, _) -> {
            JLabel label = new JLabel("********");
            if (isSelected) {
                label.setBackground(tbl.getSelectionBackground());
                label.setForeground(tbl.getSelectionForeground());
                label.setOpaque(true);
            }
            return label;
        });

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(itemTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        
        // Add the table to the layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        viewPanel.add(scrollPane, gbc);
        
        // Load data from MySQL
        AppConnection appConnection = new AppConnection();
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
}