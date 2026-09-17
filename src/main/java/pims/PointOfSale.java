package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PointOfSale {

    private JFrame frame;
    private String username;

    public PointOfSale(JFrame frame, String username) {
        this.frame = frame;
        this.username = username;
    }

    public JPanel createSalesPanel(String title, String description) {
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

        JPanel productPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productPanel.setBackground(Dashboard.LIGHT_GREEN);

        JLabel medicineLabel = new JLabel("Medicine:");
        JComboBox<String> medicineComboBox = new JComboBox<>();
        JTextField quantityField = new JTextField(5);
        JButton addButton = new JButton("Add to Cart");

        productPanel.add(medicineLabel);
        productPanel.add(medicineComboBox);
        productPanel.add(new JLabel("Quantity:"));
        productPanel.add(quantityField);
        productPanel.add(addButton);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Dashboard.LIGHT_GREEN);

        topPanel.add(headerPanel);
        topPanel.add(productPanel);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {
                "Medicine ID",
                "Medicine",
                "Quantity",
                "Unit Price",
                "Subtotal"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable cartTable = new JTable(tableModel);

        cartTable.setRowHeight(30);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 13));
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Dashboard.BORDER));

        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Dashboard.LIGHT_GREEN);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Dashboard.LIGHT_GREEN);

        JButton removeButton = new JButton("Remove Item");
        JButton clearButton = new JButton("Clear Cart");
        JButton checkoutButton = new JButton("Checkout");

        actionPanel.add(removeButton);
        actionPanel.add(clearButton);
        actionPanel.add(checkoutButton);

        JLabel totalLabel = new JLabel("Total: R0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(Dashboard.DARK_TEXT);

        bottomPanel.add(actionPanel, BorderLayout.WEST);
        bottomPanel.add(totalLabel, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        loadMedicineDropdown(medicineComboBox);

        addButton.addActionListener(e -> {
            addToCart(medicineComboBox, quantityField, tableModel, totalLabel);
        });

        removeButton.addActionListener(e -> {
            removeFromCart(cartTable, tableModel, totalLabel);
        });

        clearButton.addActionListener(e -> {
            clearCart(tableModel, totalLabel);
        });

        checkoutButton.addActionListener(e -> {
            checkout(tableModel, totalLabel);
        });

        return panel;
    }

    private void loadMedicineDropdown(JComboBox<String> medicineComboBox) {
        medicineComboBox.removeAllItems();

        String sql = "SELECT medicine_id, medicine_name, quantity, price FROM medicines WHERE quantity > 0 ORDER BY medicine_name ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                medicineComboBox.addItem(
                        resultSet.getInt("medicine_id") + " - " +
                                resultSet.getString("medicine_name") +
                                " - R" +
                                resultSet.getDouble("price") +
                                " (" +
                                resultSet.getInt("quantity") +
                                " in stock)"
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    frame,
                    "Could not load medicines.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addToCart(JComboBox<String> medicineComboBox, JTextField quantityField, DefaultTableModel tableModel, JLabel totalLabel) {
        if (medicineComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please select a medicine.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int quantity;

        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please enter a valid quantity.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Quantity must be greater than zero.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String selectedMedicine = medicineComboBox.getSelectedItem().toString();
        int medicineId = Integer.parseInt(selectedMedicine.split(" - ")[0]);

        String sql = "SELECT medicine_name, quantity, price FROM medicines WHERE medicine_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, medicineId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String medicineName = resultSet.getString("medicine_name");
                int stock = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                int existingRow = findMedicineInCart(tableModel, medicineId);

                int cartQuantity = 0;

                if (existingRow != -1) {
                    cartQuantity = Integer.parseInt(tableModel.getValueAt(existingRow, 2).toString());
                }

                if (cartQuantity + quantity > stock) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Not enough stock available.\nAvailable stock: " + stock,
                            "Insufficient Stock",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (existingRow != -1) {
                    int newQuantity = cartQuantity + quantity;
                    double subtotal = newQuantity * price;

                    tableModel.setValueAt(newQuantity, existingRow, 2);
                    tableModel.setValueAt(price, existingRow, 3);
                    tableModel.setValueAt(subtotal, existingRow, 4);

                } else {
                    double subtotal = quantity * price;

                    tableModel.addRow(new Object[]{
                            medicineId,
                            medicineName,
                            quantity,
                            price,
                            subtotal
                    });
                }

                quantityField.setText("");
                updateTotal(tableModel, totalLabel);
            }

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    frame,
                    "Could not add medicine to cart.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private int findMedicineInCart(DefaultTableModel tableModel, int medicineId) {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            int currentMedicineId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

            if (currentMedicineId == medicineId) {
                return row;
            }
        }

        return -1;
    }

    private void removeFromCart(JTable cartTable, DefaultTableModel tableModel, JLabel totalLabel) {
        int selectedRow = cartTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please select an item to remove.",
                    "No Item Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        tableModel.removeRow(selectedRow);
        updateTotal(tableModel, totalLabel);
    }

    private void clearCart(DefaultTableModel tableModel, JLabel totalLabel) {
        tableModel.setRowCount(0);
        updateTotal(tableModel, totalLabel);
    }

    private double calculateTotal(DefaultTableModel tableModel) {
        double total = 0;

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            total += Double.parseDouble(tableModel.getValueAt(row, 4).toString());
        }

        return total;
    }

    private void updateTotal(DefaultTableModel tableModel, JLabel totalLabel) {
        double total = calculateTotal(tableModel);
        totalLabel.setText(String.format("Total: R%.2f", total));
    }

    private void checkout(DefaultTableModel tableModel, JLabel totalLabel) {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    frame,
                    "The cart is empty.",
                    "Checkout",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double total = calculateTotal(tableModel);

        int confirmation = JOptionPane.showConfirmDialog(
                frame,
                String.format("Complete sale for R%.2f?", total),
                "Confirm Checkout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        String userSql = "SELECT user_id FROM users WHERE username = ?";
        String saleSql = "INSERT INTO sales (user_id, total_amount) VALUES (?, ?)";
        String itemSql = "INSERT INTO sale_items (sale_id, medicine_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String stockSql = "UPDATE medicines SET quantity = quantity - ? WHERE medicine_id = ? AND quantity >= ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            int userId;

            try (PreparedStatement userStatement = connection.prepareStatement(userSql)) {
                userStatement.setString(1, username);

                ResultSet resultSet = userStatement.executeQuery();

                if (!resultSet.next()) {
                    connection.rollback();

                    JOptionPane.showMessageDialog(
                            frame,
                            "Could not identify the logged-in user.",
                            "Checkout Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                userId = resultSet.getInt("user_id");
            }

            int saleId;

            try (PreparedStatement saleStatement = connection.prepareStatement(saleSql, Statement.RETURN_GENERATED_KEYS)) {
                saleStatement.setInt(1, userId);
                saleStatement.setDouble(2, total);
                saleStatement.executeUpdate();

                ResultSet generatedKeys = saleStatement.getGeneratedKeys();

                if (!generatedKeys.next()) {
                    connection.rollback();

                    JOptionPane.showMessageDialog(
                            frame,
                            "Could not create the sale.",
                            "Checkout Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                saleId = generatedKeys.getInt(1);
            }

            for (int row = 0; row < tableModel.getRowCount(); row++) {
                int medicineId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                int quantity = Integer.parseInt(tableModel.getValueAt(row, 2).toString());
                double unitPrice = Double.parseDouble(tableModel.getValueAt(row, 3).toString());

                try (PreparedStatement stockStatement = connection.prepareStatement(stockSql)) {
                    stockStatement.setInt(1, quantity);
                    stockStatement.setInt(2, medicineId);
                    stockStatement.setInt(3, quantity);

                    int updatedRows = stockStatement.executeUpdate();

                    if (updatedRows == 0) {
                        connection.rollback();

                        JOptionPane.showMessageDialog(
                                frame,
                                "There is not enough stock for one of the medicines.",
                                "Checkout Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                }

                try (PreparedStatement itemStatement = connection.prepareStatement(itemSql)) {
                    itemStatement.setInt(1, saleId);
                    itemStatement.setInt(2, medicineId);
                    itemStatement.setInt(3, quantity);
                    itemStatement.setDouble(4, unitPrice);

                    itemStatement.executeUpdate();
                }
            }

            connection.commit();

            showBillWindow(tableModel, saleId, total);

            tableModel.setRowCount(0);
            totalLabel.setText("Total: R0.00");

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    frame,
                    "Could not complete the sale.",
                    "Checkout Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void showBillWindow(DefaultTableModel tableModel, int saleId, double total) {

        JFrame billFrame = new JFrame("HealthFirst Pharmacy - Bill");
        billFrame.setSize(500, 500);
        billFrame.setLocationRelativeTo(frame);
        billFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        billArea.setEditable(false);
        billArea.setMargin(new Insets(15, 15, 15, 15));

        StringBuilder bill = new StringBuilder();

        bill.append("========================================\n");
        bill.append("         HEALTHFIRST PHARMACY\n");
        bill.append("              SALES BILL\n");
        bill.append("========================================\n\n");

        bill.append("Sale ID: ").append(saleId).append("\n");
        bill.append("Cashier: ").append(username).append("\n");
        bill.append("----------------------------------------\n");

        bill.append(String.format("%-18s %-5s %-10s\n", "Medicine", "Qty", "Subtotal"));
        bill.append("----------------------------------------\n");

        for (int row = 0; row < tableModel.getRowCount(); row++) {

            String medicineName = tableModel.getValueAt(row, 1).toString();
            int quantity = Integer.parseInt(tableModel.getValueAt(row, 2).toString());
            double subtotal = Double.parseDouble(tableModel.getValueAt(row, 4).toString());

            bill.append(String.format("%-18s %-5d R%-9.2f\n", medicineName, quantity, subtotal));
        }

        bill.append("----------------------------------------\n");
        bill.append(String.format("TOTAL:                         R%.2f\n", total));
        bill.append("========================================\n");
        bill.append("       Thank you for your purchase!\n");
        bill.append("========================================\n");

        billArea.setText(bill.toString());

        JScrollPane scrollPane = new JScrollPane(billArea);

        JButton closeButton = new JButton("Close");

        closeButton.addActionListener(e -> billFrame.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        billFrame.setLayout(new BorderLayout());
        billFrame.add(scrollPane, BorderLayout.CENTER);
        billFrame.add(buttonPanel, BorderLayout.SOUTH);

        billFrame.setVisible(true);
    }
}