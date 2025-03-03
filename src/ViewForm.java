import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewForm {
    private JTable table;
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

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        table.setFillsViewportHeight(true);
        
        table.getColumnModel().getColumn(3).setCellRenderer((tbl, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel("********");
            if (isSelected) {
                label.setBackground(tbl.getSelectionBackground());
                label.setForeground(tbl.getSelectionForeground());
                label.setOpaque(true);
            }
            return label;
        });

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);
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
}