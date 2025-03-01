import java.sql.*;
import javax.swing.JOptionPane;

public class AppConnection {
    private static final String DB_URL = AppConfig.get("DB_URL");
    private static final String DB_USER = AppConfig.get("DB_USER");
    private static final String DB_PASSWORD = AppConfig.get("DB_PASSWORD");
    private static final String DB_DRIVER = AppConfig.get("DB_DRIVER");

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

    public boolean insertPassword(DashboardForm dashboardForm)
            throws SQLException, ClassNotFoundException {

        String title = dashboardForm.addTitleField.getText();
        String username = dashboardForm.addUsernameField.getText();
        String password = new String(dashboardForm.addPasswordField.getPassword());

        String query = "INSERT INTO passwords (title, username, password) VALUES (?, ?, ?)";

        try (Connection connection = createDatabaseConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, username);
            statement.setString(3, password);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}