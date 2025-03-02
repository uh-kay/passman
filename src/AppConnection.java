import java.sql.*;
import javax.swing.JOptionPane;

public class AppConnection {
    AppConfig appConfig = new AppConfig();

    private final String DB_URL = appConfig.get("DB_URL");
    private final String DB_USER = appConfig.get("DB_USER");
    private final String DB_PASSWORD = appConfig.get("DB_PASSWORD");
    private final String DB_DRIVER = appConfig.get("DB_DRIVER");

    public boolean authenticate(LoginForm loginForm) {
        String username = loginForm.loginUsernameField.getText();
        String password = new String(loginForm.loginPasswordField.getPassword());

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
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(query);
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

        String title = addForm.addTitleField.getText();
        String username = addForm.addUsernameField.getText();
        String password = new String(addForm.addPasswordField.getPassword());
        String domain = addForm.addDomainField.getText();

        Connection connection = null;
        PreparedStatement passwordStatement = null;
        PreparedStatement domainStatement = null;
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

            String passwordQuery = "INSERT INTO passwords (title, username, password, domain_id) VALUES (?, ?, ?, ?)";
            passwordStatement = connection.prepareStatement(passwordQuery);
            passwordStatement.setString(1, title);
            passwordStatement.setString(2, username);
            passwordStatement.setString(3, password);
            passwordStatement.setInt(4, domainId);

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
}