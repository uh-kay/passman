import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class AppConnection {
    AppConfig appConfig = new AppConfig();

    private final String DB_URL = appConfig.get("DB_URL");
    private final String DB_USER = appConfig.get("DB_USER");
    private final String DB_PASSWORD = appConfig.get("DB_PASSWORD");
    private final String DB_DRIVER = appConfig.get("DB_DRIVER");

    public boolean authenticate(LoginForm loginForm) {
        var username = loginForm.loginUsernameField.getText();
        var password = new String(loginForm.loginPasswordField.getPassword());

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(loginForm.createLoginPanel(null),
                    "Username and password cannot be empty",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try (Connection connection = createDatabaseConnection();
                PreparedStatement statement = prepareAuthenticationStatement(connection, username, password);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            handleDatabaseError(e);
            return false;
        }
    }

    private Connection createDatabaseConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private PreparedStatement prepareAuthenticationStatement(Connection connection, String username, String password)
            throws SQLException {
        var query = "SELECT * FROM users WHERE username = ? AND password = ?";
        var statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        return statement;
    }

    public void handleDatabaseError(Exception e) {
        JOptionPane.showMessageDialog(null,
                "Database Error: " + e.getMessage(),
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    public boolean insertPassword(AddForm addForm)
        throws SQLException, ClassNotFoundException {

        var title = addForm.addTitleField.getText();
        var username = addForm.addUsernameField.getText();
        var password = new String(addForm.addPasswordField.getPassword());
        var domain = addForm.addDomainField.getText();
        var tag = addForm.addTagField.getText();

        Connection connection = null;
        PreparedStatement passwordStatement = null;
        PreparedStatement domainStatement = null;
        PreparedStatement tagStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = createDatabaseConnection();
            connection.setAutoCommit(false);

            int domainId;
            String domainQuery = "SELECT id FROM domains WHERE domain = ?";
            domainStatement = connection.prepareStatement(domainQuery);
            domainStatement.setString(1, domain);
            ResultSet domainResult = domainStatement.executeQuery();

            if (domainResult.next()) {
                domainId = domainResult.getInt("id");
            } else {
                domainStatement.close();
                domainStatement = connection.prepareStatement(
                    "INSERT INTO domains (domain) VALUES (?)", 
                    Statement.RETURN_GENERATED_KEYS
                );
                domainStatement.setString(1, domain);
                domainStatement.executeUpdate();

                generatedKeys = domainStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    domainId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to get generated domain ID");
                }
            }

            int tagId;
            String tagQuery = "SELECT id FROM tags WHERE name = ?";
            tagStatement = connection.prepareStatement(tagQuery);
            tagStatement.setString(1, tag);
            ResultSet tagResult = tagStatement.executeQuery();

            if (tagResult.next()) {
                tagId = tagResult.getInt("id");
            } else {
                tagStatement.close();
                tagStatement = connection.prepareStatement(
                    "INSERT INTO tags (name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                tagStatement.setString(1, tag);
                tagStatement.executeUpdate();

                generatedKeys = tagStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    tagId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to get generated tag ID");
                }
            }

            String passwordQuery = "INSERT INTO passwords (title, username, password, domain_id, tag_id) VALUES (?, ?, ?, ?, ?)";
            passwordStatement = connection.prepareStatement(passwordQuery);
            passwordStatement.setString(1, title);
            passwordStatement.setString(2, username);
            passwordStatement.setString(3, password);
            passwordStatement.setInt(4, domainId);
            passwordStatement.setInt(5, tagId);

            int rowsAffected = passwordStatement.executeUpdate();

            connection.commit();

            return rowsAffected > 0;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw e;
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) { /* ignored */ }
            if (domainStatement != null) try { domainStatement.close(); } catch (SQLException e) { /* ignored */ }
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) { /* ignored */ }
            if (tagStatement != null) try { domainStatement.close(); } catch (SQLException e) { /* ignored */ }
            if (passwordStatement != null) try { passwordStatement.close(); } catch (SQLException e) { /* ignored */ }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);  // Reset auto-commit
                    connection.close();
                } catch (SQLException e) { /* ignored */ }
            }
        }
    }

    public boolean editPassword(EditForm editForm)
        throws SQLException, ClassNotFoundException {

        var title = editForm.editTitleField.getText();
        var username = editForm.editUsernameField.getText();
        var password = new String(editForm.editPasswordField.getPassword());
        var domain = editForm.editDomainField.getText();
        var tag = editForm.editTagsField.getText();

        Connection connection = null;
        PreparedStatement passwordStatement = null;
        PreparedStatement domainStatement = null;
        PreparedStatement tagStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = createDatabaseConnection();
            connection.setAutoCommit(false);

            int domainId;
            var domainQuery = "SELECT id FROM domains WHERE domain = ?";
            domainStatement = connection.prepareStatement(domainQuery);
            domainStatement.setString(1, domain);
            var domainResult = domainStatement.executeQuery();

            if (domainResult.next()) {
                domainId = domainResult.getInt("id");
            } else {
                domainStatement.close();
                domainStatement = connection.prepareStatement(
                    "INSERT INTO domains (domain) VALUES (?)", 
                    Statement.RETURN_GENERATED_KEYS
                );
                domainStatement.setString(1, domain);
                domainStatement.executeUpdate();

                generatedKeys = domainStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    domainId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to get generated domain ID");
                }
            }

            int tagId;
            String tagQuery = "SELECT id FROM tags WHERE name = ?";
            tagStatement = connection.prepareStatement(tagQuery);
            tagStatement.setString(1, tag);
            ResultSet tagResult = tagStatement.executeQuery();

            if (tagResult.next()) {
                tagId = tagResult.getInt("id");
            } else {
                tagStatement.close();
                tagStatement = connection.prepareStatement(
                    "INSERT INTO tags (name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                tagStatement.setString(1, tag);
                tagStatement.executeUpdate();

                generatedKeys = tagStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    tagId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to get generated tag ID");
                }
            }

            String updateQuery = "UPDATE passwords SET title = ?, username = ?, password = ?, domain_id = ? , tag_id = ? , WHERE id = ?";
            passwordStatement = connection.prepareStatement(updateQuery);
            passwordStatement.setString(1, title);
            passwordStatement.setString(2, username);
            passwordStatement.setString(3, password);
            passwordStatement.setInt(4, domainId);
            passwordStatement.setInt(5, tagId);

            int rowsAffected = passwordStatement.executeUpdate();

            connection.commit();

            return rowsAffected > 0;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw e;
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) { /* ignored */ }
            if (domainStatement != null) try { domainStatement.close(); } catch (SQLException e) { /* ignored */ }
            if (passwordStatement != null) try { passwordStatement.close(); } catch (SQLException e) { /* ignored */ }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);  // Reset auto-commit
                    connection.close();
                } catch (SQLException e) { /* ignored */ }
            }
        }
    }

    public void loadDataFromDatabase(DefaultTableModel model, ViewForm viewForm)
        throws SQLException, ClassNotFoundException {
        Connection connection = null;

        try {
            connection = createDatabaseConnection();
            connection.setAutoCommit(false);
        
            // Create SQL query with JOINs to get domain and tag names
            var sql = "SELECT p.id, p.title, p.username, p.password, " +
                            "d.domain as domain_name, t.name as tag_name, " +
                            "p.creationDate, p.modiifedDate " +
                            "FROM passwords p " +
                            "LEFT JOIN domains d ON p.domain_id = d.id " +
                            "LEFT JOIN tags t ON p.tag_id = t.id " +
                            "ORDER BY p.id";
            
            var stmt = connection.createStatement();
            var rs = stmt.executeQuery(sql);
                
            // Clear existing data
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
                
            // Add data from result set to table model
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("domain_name"),
                    rs.getString("tag_name"),
                    rs.getTimestamp("creationDate"),
                    rs.getTimestamp("modiifedDate")
                };

                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(viewForm.createViewPanel(),
                "Database error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void deleteSelectedItem(ViewForm viewForm) {
        if (viewForm == null) {
            System.err.println("Error: ViewForm is null");
            return;
        }

        var table = viewForm.getItemTable();

        if (table == null) {
            System.err.println("Error: Table reference is null in deleteSelectedItem");
            JOptionPane.showMessageDialog(null, 
                "Cannot delete: Table reference is invalid",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, 
                "Please select an item to delete",
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get the ID of the selected item
        var itemId = (int) table.getValueAt(selectedRow, 0);
        var itemName = (String) table.getValueAt(selectedRow, 1);
        
        // Confirm deletion
        var confirmation = JOptionPane.showConfirmDialog(null,
            "Are you sure you want to delete " + itemName + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            // Delete from database
            try {
                if (deleteItemFromDatabase(itemId)) {
                    // Remove from table model if delete was successful
                    var model = (DefaultTableModel) table.getModel();
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null, 
                        "Item deleted successfully",
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException e) {
                handleDatabaseError(e);
            }
        }
    }
    
    private boolean deleteItemFromDatabase(int itemId)
        throws SQLException, ClassNotFoundException {
        Connection connection = createDatabaseConnection();
        
        var sql = "DELETE FROM passwords WHERE id = ?";
        
        try {
            var statement = connection.prepareStatement(sql);
            
            statement.setInt(1, itemId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Failed to delete item: " + e.getMessage(),
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void performLogout(DashboardForm dashboardForm)
        throws SQLException, ClassNotFoundException {
        try {
            var connection = createDatabaseConnection();
            // Step 1: Lock the database connection
            if (connection != null && !connection.isClosed()) {
                // Rollback any open transactions
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
                
                // Close the database connection
                connection.close();
            }

            dashboardForm.dispose();

            SwingUtilities.invokeLater(() -> {
                AuthenticationForm authenticationForm = new AuthenticationForm();
                authenticationForm.setVisible(true);
            });

        } catch (SQLException | ClassNotFoundException e) {
            handleDatabaseError(e);
        }
    }
}