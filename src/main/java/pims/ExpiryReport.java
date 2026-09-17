package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ExpiryReport {

    private JFrame frame;

    public ExpiryReport(JFrame frame) {
        this.frame = frame;
    }

    public JPanel createExpiryReportPanel(String title, String description) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Dashboard.LIGHT_GREEN);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Dashboard.LIGHT_GREEN);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Dashboard.DARK_TEXT);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        descriptionLabel.setForeground(Dashboard.GREY_TEXT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(descriptionLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Dashboard.LIGHT_GREEN);

        topPanel.add(headerPanel, BorderLayout.WEST);

        JButton refreshButton = new JButton("Refresh Report");

        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshPanel.setBackground(Dashboard.LIGHT_GREEN);
        refreshPanel.add(refreshButton);

        topPanel.add(refreshPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {
                "ID",
                "Medicine Name",
                "Category",
                "Supplier",
                "Quantity",
                "Price",
                "Expiry Date"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable expiryTable = new JTable(tableModel);

        expiryTable.setRowHeight(30);
        expiryTable.setFont(new Font("Arial", Font.PLAIN, 13));
        expiryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        expiryTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(expiryTable);

        // SELECTED MEDICINE DETAILS
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(2, 4, 10, 10));
        detailsPanel.setBackground(Dashboard.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Selected Medicine Details"));

        JLabel detailsId = new JLabel("ID: ");
        JLabel detailsName = new JLabel("Medicine: ");
        JLabel detailsCategory = new JLabel("Category: ");
        JLabel detailsSupplier = new JLabel("Supplier: ");
        JLabel detailsQuantity = new JLabel("Quantity: ");
        JLabel detailsPrice = new JLabel("Price: ");
        JLabel detailsExpiry = new JLabel("Expiry Date: ");

        detailsPanel.add(detailsId);
        detailsPanel.add(detailsName);
        detailsPanel.add(detailsCategory);
        detailsPanel.add(detailsSupplier);
        detailsPanel.add(detailsQuantity);
        detailsPanel.add(detailsPrice);
        detailsPanel.add(detailsExpiry);

        scrollPane.setBorder(BorderFactory.createLineBorder(Dashboard.BORDER));

        JPanel tableAndDetailsPanel = new JPanel(new BorderLayout(10, 10));
        tableAndDetailsPanel.setBackground(Dashboard.LIGHT_GREEN);

        tableAndDetailsPanel.add(scrollPane, BorderLayout.CENTER);
        tableAndDetailsPanel.add(detailsPanel, BorderLayout.SOUTH);

        panel.add(tableAndDetailsPanel, BorderLayout.CENTER);

        // SELECTED MEDICINE
        expiryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && expiryTable.getSelectedRow() != -1) {
                int selectedRow = expiryTable.getSelectedRow();

                detailsId.setText("ID: " + expiryTable.getValueAt(selectedRow, 0));
                detailsName.setText("Medicine: " + expiryTable.getValueAt(selectedRow, 1));
                detailsCategory.setText("Category: " + expiryTable.getValueAt(selectedRow, 2));
                detailsSupplier.setText("Supplier: " + expiryTable.getValueAt(selectedRow, 3));
                detailsQuantity.setText("Quantity: " + expiryTable.getValueAt(selectedRow, 4));
                detailsPrice.setText("Price: R" + expiryTable.getValueAt(selectedRow, 5));
                detailsExpiry.setText("Expiry Date: " + expiryTable.getValueAt(selectedRow, 6));
            }
        });

        JLabel countLabel = new JLabel("Medicines Expiring: 0");

        countLabel.setFont(new Font("Arial", Font.BOLD, 18));
        countLabel.setForeground(Dashboard.DARK_TEXT);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Dashboard.LIGHT_GREEN);
        bottomPanel.add(countLabel);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        loadExpiryReport(tableModel, countLabel);

        refreshButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            loadExpiryReport(tableModel, countLabel);
        });

        return panel;
    }

    private void loadExpiryReport(DefaultTableModel tableModel, JLabel countLabel) {
        String sql = "SELECT m.medicine_id, m.medicine_name, m.category, s.supplier_name, m.quantity, m.price, m.expiry_date FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id WHERE m.expiry_date IS NOT NULL AND m.expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 1 MONTH) ORDER BY m.expiry_date";

        int count = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("medicine_id"),
                        resultSet.getString("medicine_name"),
                        resultSet.getString("category"),
                        resultSet.getString("supplier_name"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("price"),
                        resultSet.getDate("expiry_date")
                });

                count++;
            }

            countLabel.setText("Medicines Expiring: " + count);

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    frame,
                    "Could not load expiry report.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
