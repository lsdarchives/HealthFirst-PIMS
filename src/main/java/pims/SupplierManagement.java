package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SupplierManagement {
    private JFrame frame;

    public SupplierManagement(JFrame frame) {
        this.frame = frame;
    }

    public JPanel createSupplierPanel(String title, String description){
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

        // =====================================================
        // SEARCH PANEL
        // =====================================================
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
        // SUPPLIER TABLE
        // =====================================================
        String[] columns = {
                "ID",
                "Supplier Name",
                "Contact Person",
                "Phone",
                "Email",
                "Address"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable supplierTable = new JTable(tableModel);

        loadSuppliersTable(tableModel);

        supplierTable.setRowHeight(30);
        supplierTable.setFont(new Font("Arial", Font.PLAIN, 13));
        supplierTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        supplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        supplierTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(supplierTable);

        // SELECTED SUPPLIER DETAILS
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(2, 3, 10, 10));
        detailsPanel.setBackground(Dashboard.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Selected Supplier Details"));

        JLabel detailsId = new JLabel("ID: ");
        JLabel detailsName = new JLabel("Supplier: ");
        JLabel detailsContact = new JLabel("Contact Person: ");
        JLabel detailsPhone = new JLabel("Phone: ");
        JLabel detailsEmail = new JLabel("Email: ");
        JLabel detailsAddress = new JLabel("Address: ");

        detailsPanel.add(detailsId);
        detailsPanel.add(detailsName);
        detailsPanel.add(detailsContact);
        detailsPanel.add(detailsPhone);
        detailsPanel.add(detailsEmail);
        detailsPanel.add(detailsAddress);

        scrollPane.setBorder(BorderFactory.createLineBorder(Dashboard.BORDER));

        // =====================================================
        // SEARCH ACTION
        // =====================================================
        searchButton.addActionListener(e -> {
            searchSuppliers(searchField.getText(), tableModel);
        });

        showAllButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            loadSuppliersTable(tableModel);
        });

        // =====================================================
        // SUPPLIER BUTTONS
        // =====================================================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Dashboard.LIGHT_GREEN);

        JButton addButton = new JButton("Add Supplier");
        JButton editButton = new JButton("Edit Supplier");
        JButton deleteButton = new JButton("Delete Supplier");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> {
            addSupplier(tableModel);
        });

        editButton.addActionListener(e -> {
            editSupplier(supplierTable, tableModel);
        });

        deleteButton.addActionListener(e -> {
            deleteSupplier(supplierTable, tableModel);
        });

        JPanel tableAndDetailsPanel = new JPanel(new BorderLayout(10, 10));
        tableAndDetailsPanel.setBackground(Dashboard.LIGHT_GREEN);

        tableAndDetailsPanel.add(scrollPane, BorderLayout.CENTER);
        tableAndDetailsPanel.add(detailsPanel, BorderLayout.SOUTH);

        panel.add(tableAndDetailsPanel, BorderLayout.CENTER);

        // SELECTED SUPPLIER
        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && supplierTable.getSelectedRow() != -1) {
                int selectedRow = supplierTable.getSelectedRow();

                detailsId.setText("ID: " + supplierTable.getValueAt(selectedRow, 0));
                detailsName.setText("Supplier: " + supplierTable.getValueAt(selectedRow, 1));
                detailsContact.setText("Contact Person: " + supplierTable.getValueAt(selectedRow, 2));
                detailsPhone.setText("Phone: " + supplierTable.getValueAt(selectedRow, 3));
                detailsEmail.setText("Email: " + supplierTable.getValueAt(selectedRow, 4));
                detailsAddress.setText("Address: " + supplierTable.getValueAt(selectedRow, 5));
            }
        });

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    // ---------------------------------------------------------
    // ADD SUPPLIER
    // ---------------------------------------------------------
    private void addSupplier(DefaultTableModel tableModel) {
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("Supplier Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Contact Person:"));
        formPanel.add(contactField);

        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        int result = JOptionPane.showConfirmDialog(
                frame,
                formPanel,
                "Add Supplier",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String sql = "INSERT INTO suppliers (supplier_name, contact_person, phone, email, address) VALUES (?, ?, ?, ?, ?)";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)){

                statement.setString(1, nameField.getText());
                statement.setString(2, contactField.getText());
                statement.setString(3, phoneField.getText());
                statement.setString(4, emailField.getText());
                statement.setString(5, addressField.getText());

                statement.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Supplier added successfully.");

                tableModel.setRowCount(0);
                loadSuppliersTable(tableModel);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Could not add supplier.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ---------------------------------------------------------
    // EDIT SUPPLIER
    // ---------------------------------------------------------
    private void editSupplier(JTable supplierTable, DefaultTableModel tableModel) {
        int selectedRow = supplierTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a supplier to edit.");
            return;
        }

        int supplierId = (int) tableModel.getValueAt(selectedRow, 0);

        JTextField nameField = new JTextField(tableModel.getValueAt(selectedRow, 1).toString());
        JTextField contactField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString());
        JTextField phoneField = new JTextField(tableModel.getValueAt(selectedRow, 3).toString());
        JTextField emailField = new JTextField(tableModel.getValueAt(selectedRow, 4).toString());
        JTextField addressField = new JTextField(tableModel.getValueAt(selectedRow, 5).toString());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("Supplier Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Contact Person:"));
        formPanel.add(contactField);

        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        int result = JOptionPane.showConfirmDialog(
                frame,
                formPanel,
                "Edit Supplier",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String sql = "UPDATE suppliers SET supplier_name = ?, contact_person = ?, phone = ?, email = ?, address = ? WHERE supplier_id = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)){

                statement.setString(1, nameField.getText());
                statement.setString(2, contactField.getText());
                statement.setString(3, phoneField.getText());
                statement.setString(4, emailField.getText());
                statement.setString(5, addressField.getText());
                statement.setInt(6, supplierId);

                statement.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Supplier updated successfully.");

                tableModel.setRowCount(0);
                loadSuppliersTable(tableModel);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Could not update supplier.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ---------------------------------------------------------
    // DELETE SUPPLIER
    // ---------------------------------------------------------
    private void deleteSupplier(JTable supplierTable, DefaultTableModel tableModel) {
        int selectedRow = supplierTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a supplier to delete.");
            return;
        }

        int supplierId = (int) tableModel.getValueAt(selectedRow, 0);
        String supplierName = tableModel.getValueAt(selectedRow, 1).toString();

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete " + supplierName + "?",
                "Delete Supplier",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM suppliers WHERE supplier_id = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)){

                statement.setInt(1, supplierId);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Supplier deleted successfully.");

                tableModel.setRowCount(0);
                loadSuppliersTable(tableModel);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Could not delete supplier.\nThis supplier may still be linked to medicines.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ---------------------------------------------------------
    // LOAD SUPPLIERS TABLE
    // ---------------------------------------------------------
    private void loadSuppliersTable(DefaultTableModel tableModel) {
        String sql = "SELECT supplier_id, supplier_name, contact_person, phone, email, address FROM suppliers ORDER BY supplier_name";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("supplier_name"),
                        resultSet.getString("contact_person"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("address")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not load suppliers from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------------------------
    // SEARCH SUPPLIERS
    // ---------------------------------------------------------
    private void searchSuppliers(String searchText, DefaultTableModel tableModel) {
        String sql = "SELECT supplier_id, supplier_name, contact_person, phone, email, address FROM suppliers WHERE supplier_name LIKE ? OR contact_person LIKE ? OR phone LIKE ? OR email LIKE ? ORDER BY supplier_name";

        tableModel.setRowCount(0);

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, "%" + searchText + "%");
            statement.setString(2, "%" + searchText + "%");
            statement.setString(3, "%" + searchText + "%");
            statement.setString(4, "%" + searchText + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tableModel.addRow(new Object[]{
                            resultSet.getInt("supplier_id"),
                            resultSet.getString("supplier_name"),
                            resultSet.getString("contact_person"),
                            resultSet.getString("phone"),
                            resultSet.getString("email"),
                            resultSet.getString("address")
                    });
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not search suppliers.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
