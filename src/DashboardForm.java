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
    private JTextField nameField;
    private JTextField stockField;
    private JTextField priceField;
    private JButton tambahButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewButton;
    private JButton searchButton;
    private JTextField searchField;
    private JTable productsTable;
    private DefaultTableModel tableModel;
    // private JTextArea laporanArea;

    public DashboardForm() {
        setTitle("Admin DashboardForm");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
 
        JPanel panel = new JPanel();
        panel.setLayout(null);
 
        // Label dan Field untuk Input Produk
        JLabel nameLabel = new JLabel("Nama Produk:");
        nameLabel.setBounds(10, 20, 80, 25);
        panel.add(nameLabel);
 
        nameField = new JTextField();
        nameField.setBounds(100, 20, 165, 25);
        panel.add(nameField);
 
        JLabel stockLabel = new JLabel("Stock:");
        stockLabel.setBounds(10, 50, 80, 25);
        panel.add(stockLabel);
 
        stockField = new JTextField();
        stockField.setBounds(100, 50, 165, 25);
        panel.add(stockField);
 
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(10, 80, 80, 25);
        panel.add(priceLabel);
 
        priceField = new JTextField();
        priceField.setBounds(100, 80, 165, 25);
        panel.add(priceField);
        
        // Tombol Tambah, Edit, Delete
        tambahButton = new JButton("Tambah Produk");
        tambahButton.setBounds(10, 120, 120, 25);
        panel.add(tambahButton);
 
        editButton = new JButton("Edit Produk");
        editButton.setBounds(140, 120, 120, 25);
        panel.add(editButton);
 
        deleteButton = new JButton("Hapus Produk");
        deleteButton.setBounds(270, 120, 120, 25);
        panel.add(deleteButton);
    
        // Tombol View Produk
        viewButton = new JButton("Lihat Produk");
        viewButton.setBounds(400, 120, 120, 25);
        panel.add(viewButton);

        // Field dan Tombol Search
        searchField = new JTextField();
        searchField.setBounds(400, 50, 150, 25);
        panel.add(searchField);

        searchButton = new JButton("Cari Produk");
        searchButton.setBounds(400, 80, 150, 25);
        panel.add(searchButton);
 
        // Tabel untuk Menampilkan Produk
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nama Produk");
        tableModel.addColumn("Stock");
        tableModel.addColumn("Price");
        
        productsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setBounds(10, 160, 560, 150);
        panel.add(scrollPane);
 
        // Action Listener untuk Tombol
        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahProduk();
            }
        });
 
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editProduk();
            }
        });
 
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusProduk();
            }
        });
 
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lihatProduk();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cariProduk();
            }
        });
            
        add(panel);
    }
            
    // Method untuk Menambahkan Produk
    private void tambahProduk() {
        String name = nameField.getText();
        double stock = Double.parseDouble(stockField.getText());
        int price = Integer.parseInt(priceField.getText());
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO products (name, stock, price) VALUES (?, ?, ?)");
            stmt.setString(1, name);
            stmt.setDouble(2, stock);
            stmt.setInt(3, price);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produk berhasil ditambahkan!");
            lihatProduk(); // Refresh tabel setelah menambahkan products
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Method untuk Mengedit Produk
    private void editProduk() {
        int selectedRow = productsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Pilih products yang ingin diedit!");
            return;
        }

        int id = (int) productsTable.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        double stock = Double.parseDouble(stockField.getText());
        int price = Integer.parseInt(priceField.getText());

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root","");

            PreparedStatement stmt = conn.prepareStatement("UPDATE products SET name = ?, stock = ?, price = ? WHERE id = ?");
            stmt.setString(1, name);
            stmt.setDouble(2, stock);
            stmt.setInt(3, price);
            stmt.setInt(4, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produk berhasil diupdate!");
            lihatProduk(); // Refresh tabel setelah mengedit products
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method untuk Menghapus Produk
    private void hapusProduk() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Pilih products yang ingin dihapus!");
            return;
        }
 
        int id = (int) productsTable.getValueAt(selectedRow, 0);
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "");
 
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM products WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produk berhasil dihapus!");
            lihatProduk(); // Refresh tabel setelah menghapus products
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
 
    // Method untuk Melihat Semua Produk
    private void lihatProduk() {
        tableModel.setRowCount(0); // Clear tabel sebelum menambahkan data baru
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "");
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double stock = rs.getDouble("stock");
                int price = rs.getInt("price");
                tableModel.addRow(new Object[]{id, name, stock, price});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
 
    // Method untuk Mencari Produk
    private void cariProduk() {
        String keyword = searchField.getText();
        tableModel.setRowCount(0); // Clear tabel sebelum menambahkan data baru
        
        try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "");
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products WHERE name LIKE ?");
        stmt.setString(1, "%" + keyword + "%");
        ResultSet rs = stmt.executeQuery();
        
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double stock = rs.getDouble("stock");
                int price = rs.getInt("price");
                tableModel.addRow(new Object[]{id, name, stock, price});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        new DashboardForm().setVisible(true);
    }
}