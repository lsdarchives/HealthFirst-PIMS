package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SalesReport {

    private JFrame frame;

    public SalesReport(JFrame frame) {
        this.frame = frame;
    }

    public JPanel createSalesReportPanel(String title, String description) {
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

        // REPORT SUMMARY
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryPanel.setBackground(Dashboard.LIGHT_GREEN);

        JLabel totalRevenueLabel = new JLabel("Total Revenue: R0.00");
        totalRevenueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalRevenueLabel.setForeground(Dashboard.DARK_TEXT);

        JLabel transactionLabel = new JLabel("Number of Transactions: 0");
        transactionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        transactionLabel.setForeground(Dashboard.DARK_TEXT);

        JLabel itemsSoldLabel = new JLabel("Total Items Sold: 0");
        itemsSoldLabel.setFont(new Font("Arial", Font.BOLD, 16));
        itemsSoldLabel.setForeground(Dashboard.DARK_TEXT);

        summaryPanel.add(totalRevenueLabel);
        summaryPanel.add(transactionLabel);
        summaryPanel.add(itemsSoldLabel);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Dashboard.LIGHT_GREEN);
        contentPanel.add(summaryPanel, BorderLayout.NORTH);

        String[] columns = {
                "Sale ID",
                "Cashier",
                "Sale Date",
                "Medicine",
                "Quantity",
                "Unit Price",
                "Item Total"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable reportTable = new JTable(tableModel);

        reportTable.setRowHeight(30);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 13));
        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        reportTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(reportTable);

        // SELECTED SALE DETAILS
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(2, 4, 10, 10));
        detailsPanel.setBackground(Dashboard.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Selected Sale Details"));

        JLabel detailsSaleId = new JLabel("Sale ID: ");
        JLabel detailsCashier = new JLabel("Cashier: ");
        JLabel detailsDate = new JLabel("Sale Date: ");
        JLabel detailsMedicine = new JLabel("Medicine: ");
        JLabel detailsCategory = new JLabel("Category: ");
        JLabel detailsSupplier = new JLabel("Supplier: ");
        JLabel detailsQuantity = new JLabel("Quantity Sold: ");
        JLabel detailsUnitPrice = new JLabel("Unit Price: ");
        JLabel detailsItemTotal = new JLabel("Item Total: ");

        detailsPanel.add(detailsSaleId);
        detailsPanel.add(detailsCashier);
        detailsPanel.add(detailsDate);
        detailsPanel.add(detailsMedicine);
        detailsPanel.add(detailsQuantity);
        detailsPanel.add(detailsUnitPrice);
        detailsPanel.add(detailsItemTotal);

        scrollPane.setBorder(BorderFactory.createLineBorder(Dashboard.BORDER));

        JPanel tableAndDetailsPanel = new JPanel(new BorderLayout(10, 10));
        tableAndDetailsPanel.setBackground(Dashboard.LIGHT_GREEN);

        tableAndDetailsPanel.add(scrollPane, BorderLayout.CENTER);
        tableAndDetailsPanel.add(detailsPanel, BorderLayout.SOUTH);

        contentPanel.add(tableAndDetailsPanel, BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);

        loadSalesReport(tableModel, totalRevenueLabel, transactionLabel, itemsSoldLabel);

        // SELECTED SALE
        reportTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && reportTable.getSelectedRow() != -1) {
                int selectedRow = reportTable.getSelectedRow();

                detailsSaleId.setText("Sale ID: " + reportTable.getValueAt(selectedRow, 0));
                detailsCashier.setText("Cashier: " + reportTable.getValueAt(selectedRow, 1));
                detailsDate.setText("Sale Date: " + reportTable.getValueAt(selectedRow, 2));
                detailsMedicine.setText("Medicine: " + reportTable.getValueAt(selectedRow, 3));
                detailsQuantity.setText("Quantity Sold: " + reportTable.getValueAt(selectedRow, 4));
                detailsUnitPrice.setText("Unit Price: R" + reportTable.getValueAt(selectedRow, 5));
                detailsItemTotal.setText("Item Total: R" + reportTable.getValueAt(selectedRow, 6));
            }
        });
        // ActionListener
        refreshButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            loadSalesReport(tableModel, totalRevenueLabel, transactionLabel, itemsSoldLabel);
        });

        return panel;
    }

    private void loadSalesReport(DefaultTableModel tableModel, JLabel totalRevenueLabel, JLabel transactionLabel, JLabel itemsSoldLabel){
        String sql = "SELECT s.sale_id, u.username, s.sale_date, m.medicine_name, si.quantity, si.unit_price, (si.quantity * si.unit_price) AS item_total FROM sales s JOIN users u ON s.user_id = u.user_id JOIN sale_items si ON s.sale_id = si.sale_id JOIN medicines m ON si.medicine_id = m.medicine_id ORDER BY s.sale_date DESC";

        double totalSales = 0;
        int totalItemsSold = 0;
        int transactionCount = 0;
        int lastSaleId = -1;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int saleId = resultSet.getInt("sale_id");
                double itemTotal = resultSet.getDouble("item_total");
                int quantity = resultSet.getInt("quantity");

                tableModel.addRow(new Object[]{
                        saleId,
                        resultSet.getString("username"),
                        resultSet.getTimestamp("sale_date"),
                        resultSet.getString("medicine_name"),
                        quantity,
                        resultSet.getDouble("unit_price"),
                        itemTotal
                });
                totalSales += itemTotal;
                totalItemsSold += quantity;

                if (saleId != lastSaleId) {
                    transactionCount++;
                    lastSaleId = saleId;
                }
            }

            totalRevenueLabel.setText(String.format("Total Revenue: R%.2f", totalSales));
            transactionLabel.setText("Number of Transactions: " + transactionCount);
            itemsSoldLabel.setText("Total Items Sold: " + totalItemsSold);
        } catch (SQLException e){
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    frame,
                    "Could not load sales report.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}


