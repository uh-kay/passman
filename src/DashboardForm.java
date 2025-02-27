import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardForm extends JFrame {
    // private JTextArea laporanArea;

    public DashboardForm() {
        setTitle("Admin DashboardForm");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
 
        JPanel panel = new JPanel();
        panel.setLayout(null);
            
        add(panel);
    }
 
    public static void main(String[] args) {
        new DashboardForm().setVisible(true);
    }
}