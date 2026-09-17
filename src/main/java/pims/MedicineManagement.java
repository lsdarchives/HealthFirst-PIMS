package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MedicineManagement {

    private JFrame frame;

    public MedicineManagement(JFrame frame) {
        this.frame = frame;
    }
    public JPanel createMedicinePanel(String title, String description){
        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(Dashboard.LIGHT_GREEN);
        panel.setBorder(BorderFactory.createEmptyBorder(
                10,
                10,
                10,
                10));

        // =====================================================
        // PAGE HEADER
        // =====================================================
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

        // search panel on the header
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Dashboard.LIGHT_GREEN);

        topPanel.add(headerPanel);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Dashboard.LIGHT_GREEN);

        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        topPanel.add(searchPanel);

        panel.add(topPanel, BorderLayout.NORTH);

        // =====================================================
        // MEDICINE TABLE
        // =====================================================
        String[] columns = {
                "ID",
                "Medicine Name",
                "Category",
                "Supplier",
                "Quantity",
                "Price",
                "Expiry Date",
                "Reorder Level"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable medicineTable = new JTable(tableModel);
        loadMedicinesTable(tableModel);

        // Search
        searchButton.addActionListener(e -> {
            searchMeds(searchField.getText(), tableModel);
        });

        // Show all
        showAllButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            loadMedicinesTable(tableModel);
        });

        medicineTable.setRowHeight(30);
        medicineTable.setFont(new Font("Arial", Font.PLAIN, 13));
        medicineTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        medicineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        medicineTable.setFillsViewportHeight(true);

        // Low stock highlighter
        medicineTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                int quantity = Integer.parseInt(table.getValueAt(row, 4).toString());
                int reorderLevel = Integer.parseInt(table.getValueAt(row, 7).toString());

                if (!isSelected && quantity <= reorderLevel) {
                    component.setBackground(new Color(255, 235, 235));
                    component.setForeground(Color.RED);
                } else if (!isSelected) {
                    component.setBackground(Color.WHITE);
                    component.setForeground(Dashboard.DARK_TEXT);
                }

                return component;
            }
        });

        JScrollPane scrollPane = new JScrollPane(medicineTable);

        // SELECTED MEDICINE DETAILS
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(4, 4, 10, 10));
        detailsPanel.setBackground(Dashboard.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Selected Medicine Details"));

        JLabel detailsId = new JLabel("ID: ");
        JLabel detailsName = new JLabel("Medicine: ");
        JLabel detailsCategory = new JLabel("Category: ");
        JLabel detailsSupplier = new JLabel("Supplier: ");
        JLabel detailsQuantity = new JLabel("Quantity: ");
        JLabel detailsPrice = new JLabel("Price: ");
        JLabel detailsExpiry = new JLabel("Expiry Date: ");
        JLabel detailsReorder = new JLabel("Reorder Level: ");

        detailsPanel.add(detailsId);
        detailsPanel.add(detailsName);
        detailsPanel.add(detailsCategory);
        detailsPanel.add(detailsSupplier);
        detailsPanel.add(detailsQuantity);
        detailsPanel.add(detailsPrice);
        detailsPanel.add(detailsExpiry);
        detailsPanel.add(detailsReorder);

        scrollPane.setBorder(BorderFactory.createLineBorder(Dashboard.BORDER));

        // =====================================================
        // MEDICINE BUTTONS
        // =====================================================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Dashboard.LIGHT_GREEN);

        JButton addButton = new JButton("Add Medicine");
        JButton editButton = new JButton("Edit Medicine");
        JButton deleteButton = new JButton("Delete Medicine");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> {
            addMedicine(tableModel);
        });

        editButton.addActionListener(e -> {
            editMedicine(medicineTable, tableModel);
        });

        deleteButton.addActionListener(e -> {
            deleteMedicine(medicineTable, tableModel);
        });

        JPanel tableAndDetailsPanel = new JPanel(new BorderLayout(10, 10));
        tableAndDetailsPanel.setBackground(Dashboard.LIGHT_GREEN);

        tableAndDetailsPanel.add(scrollPane, BorderLayout.CENTER);
        tableAndDetailsPanel.add(detailsPanel, BorderLayout.SOUTH);

        panel.add(tableAndDetailsPanel, BorderLayout.CENTER);

        // // SELECTED ROW
        medicineTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && medicineTable.getSelectedRow() != -1) {
                int selectedRow = medicineTable.getSelectedRow();

                detailsId.setText("ID: " + medicineTable.getValueAt(selectedRow, 0));
                detailsName.setText("Medicine: " + medicineTable.getValueAt(selectedRow, 1));
                detailsCategory.setText("Category: " + medicineTable.getValueAt(selectedRow, 2));
                detailsSupplier.setText("Supplier: " + medicineTable.getValueAt(selectedRow, 3));
                detailsQuantity.setText("Quantity: " + medicineTable.getValueAt(selectedRow, 4));
                detailsPrice.setText("Price: R" + medicineTable.getValueAt(selectedRow, 5));
                detailsExpiry.setText("Expiry Date: " + medicineTable.getValueAt(selectedRow, 6));
                detailsReorder.setText("Reorder Level: " + medicineTable.getValueAt(selectedRow, 7));
            }
        });

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------------------------------------------------
    // ADD MEDICINE
    // ---------------------------------------------------------
    private void addMedicine(DefaultTableModel tableModel) {
        JTextField name = new JTextField();
        JTextField category= new JTextField();
        JTextField quantity = new JTextField();
        JTextField price = new JTextField();
        JTextField expiry = new JTextField();
        JTextField reorder = new JTextField();

        JComboBox<String> supplierDropDown = new JComboBox<>();
        loadSuppliersDropdown(supplierDropDown);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        formPanel.add(new JLabel("Medicine Name:"));
        formPanel.add(name);

        formPanel.add(new JLabel("Category:"));
        formPanel.add(category);

        formPanel.add(new JLabel("Supplier:"));
        formPanel.add(supplierDropDown);

        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantity);

        formPanel.add(new JLabel("Price:"));
        formPanel.add(price);

        formPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        formPanel.add(expiry);

        formPanel.add(new JLabel("Reorder Level:"));
        formPanel.add(reorder);

        int result = JOptionPane.showConfirmDialog(
                frame,
                formPanel,
                "Add Medicine",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String sql = "INSERT INTO medicines (medicine_name, category, supplier_id, quantity, price, expiry_date, reorder_level) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, name.getText());
                statement.setString(2, category.getText());

                String selectedSupplier = (String) supplierDropDown.getSelectedItem();
                int supplierId = Integer.parseInt(selectedSupplier.split(" - ")[0]);
                statement.setInt(3, supplierId);

                statement.setInt(4, Integer.parseInt(quantity.getText()));
                statement.setDouble(5, Double.parseDouble(price.getText()));
                statement.setDate(6, Date.valueOf(expiry.getText()));
                statement.setInt(7, Integer.parseInt(reorder.getText()));

                statement.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Medicine added successfully.");

                tableModel.setRowCount(0);
                loadMedicinesTable(tableModel);

            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Could not add medicine.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ---------------------------------------------------------
    // EDIT MEDICINE
    // ---------------------------------------------------------
    private void editMedicine(JTable medicineTable, DefaultTableModel tableModel) {
        int selectedRow = medicineTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a medicine to edit.");
            return;
        }

        int medicineId = (int) tableModel.getValueAt(selectedRow, 0);
        int currSupplierId = getSupplierForMeds(medicineId);

        JTextField nameField = new JTextField(tableModel.getValueAt(selectedRow, 1).toString());
        JTextField categoryField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString());

        // Dropdown
        JComboBox<String> supplierDropDown = new JComboBox<>();
        loadSuppliersDropdown(supplierDropDown);

        // Select current supplier
        for (int i = 0; i < supplierDropDown.getItemCount(); i++) {
            String supplierItem = supplierDropDown.getItemAt(i);

            if (supplierItem.startsWith(currSupplierId + " - ")) {
                supplierDropDown.setSelectedIndex(i);
                break;
            }
        }

        JTextField quantityField = new JTextField(tableModel.getValueAt(selectedRow, 4).toString());
        JTextField priceField = new JTextField(tableModel.getValueAt(selectedRow, 5).toString());
        JTextField expiryField = new JTextField(tableModel.getValueAt(selectedRow, 6).toString());
        JTextField reorderField = new JTextField(tableModel.getValueAt(selectedRow, 7).toString());

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        formPanel.add(new JLabel("Medicine Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);

        formPanel.add(new JLabel("Supplier:"));
        formPanel.add(supplierDropDown);

        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        formPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        formPanel.add(expiryField);

        formPanel.add(new JLabel("Reorder Level:"));
        formPanel.add(reorderField);

        int result = JOptionPane.showConfirmDialog(
                frame,
                formPanel,
                "Edit Medicine",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String sql = "UPDATE medicines SET medicine_name = ?, category = ?, supplier_id = ?, quantity = ?, price = ?, expiry_date = ?, reorder_level = ? WHERE medicine_id = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, nameField.getText());
                statement.setString(2, categoryField.getText());

                String selectedSupplier = (String) supplierDropDown.getSelectedItem();
                int supplierId = Integer.parseInt(selectedSupplier.split(" - ")[0]);

                statement.setInt(3, supplierId);
                statement.setInt(4, Integer.parseInt(quantityField.getText()));
                statement.setDouble(5, Double.parseDouble(priceField.getText()));
                statement.setDate(6, Date.valueOf(expiryField.getText()));
                statement.setInt(7, Integer.parseInt(reorderField.getText()));
                statement.setInt(8, medicineId);

                statement.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Medicine updated successfully.");

                tableModel.setRowCount(0);
                loadMedicinesTable(tableModel);

            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Could not update medicine.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // ---------------------------------------------------------
    // DELETE MEDICINE
    // ---------------------------------------------------------
    private void deleteMedicine(JTable medicineTable, DefaultTableModel tableModel) {
        int selectedRow = medicineTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a medicine to delete.");
            return;
        }

        int medicineId = (int) tableModel.getValueAt(selectedRow, 0);
        String medicineName = tableModel.getValueAt(selectedRow, 1).toString();

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete " + medicineName + "?",
                "Delete Medicine",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM medicines WHERE medicine_id = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1, medicineId);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Medicine deleted successfully.");

                tableModel.setRowCount(0);
                loadMedicinesTable(tableModel);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Could not delete medicine.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ---------------------------------------------------------
    // LOAD MEDICINES TABLE
    // ---------------------------------------------------------
    private void loadMedicinesTable(DefaultTableModel tableModel) {
        String sql = "SELECT m.medicine_id, m.medicine_name, m.category, s.supplier_name, m.quantity, m.price, m.expiry_date, m.reorder_level FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("medicine_id"),
                        resultSet.getString("medicine_name"),
                        resultSet.getString("category"),
                        resultSet.getString("supplier_name"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("price"),
                        resultSet.getDate("expiry_date"),
                        resultSet.getInt("reorder_level")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not load medicines from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------------------------
    // LOAD SUPPLIERS INTO MEDICINE DROPDOWN
    // ---------------------------------------------------------
    private void loadSuppliersDropdown(JComboBox<String> supplierComboBox) {
        String sql = "SELECT supplier_id, supplier_name FROM suppliers ORDER BY supplier_name ASC";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                int supplierId = resultSet.getInt("supplier_id");
                String supplierName = resultSet.getString("supplier_name");

                supplierComboBox.addItem(supplierId + " - " + supplierName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not load suppliers from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------------------------
    // GET SUPPLIER ID FOR MEDICINE
    // ---------------------------------------------------------
    private int getSupplierForMeds(int medicineId) {
        String sql = "SELECT supplier_id FROM medicines WHERE medicine_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, medicineId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()){
                    return resultSet.getInt("supplier_id");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // ---------------------------------------------------------
    // SEARCH MEDICINES
    // ---------------------------------------------------------
    private void searchMeds(String searchText, DefaultTableModel tableModel) {
        String sql = "SELECT m.medicine_id, m.medicine_name, m.category, s.supplier_name, m.quantity, m.price, m.expiry_date, m.reorder_level FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id WHERE m.medicine_name LIKE ? OR m.category LIKE ?";
        tableModel.setRowCount(0);

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, "%" + searchText + "%");
            statement.setString(2, "%" + searchText + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tableModel.addRow(new Object[]{
                            resultSet.getInt("medicine_id"),
                            resultSet.getString("medicine_name"),
                            resultSet.getString("category"),
                            resultSet.getString("supplier_name"),
                            resultSet.getInt("quantity"),
                            resultSet.getDouble("price"),
                            resultSet.getDate("expiry_date"),
                            resultSet.getInt("reorder_level")
                    });
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not search medicines.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
