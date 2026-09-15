package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Dashboard {
    // =========================================================
    // 1. CLASS VARIABLES
    // =========================================================
    private JFrame frame;

    // =========================================================
    // 2. HEALTH-FIRST COLOUR PALETTE
    // =========================================================
    private static final Color PRIMARY_GREEN = new Color(25, 135, 84);
    private static final Color DARK_GREEN = new Color(20, 108, 67);
    private static final Color LIGHT_GREEN = new Color(240, 248, 243);
    private static final Color SOFT_GREEN = new Color(220, 239, 229);

    private static final Color WHITE = Color.WHITE;
    private static final Color DARK_TEXT = new Color(31, 41, 55);
    private static final Color GREY_TEXT = new Color(107, 114, 128);
    private static final Color BORDER = new Color(217, 231, 223);
    // =========================================================
    // 3. CONSTRUCTOR / DASHBOARD SETUP
    // =========================================================
    public Dashboard(String username, String role) {
        // =====================================================
        // CREATE THE WINDOW
        // =====================================================
        frame = new JFrame("HealthFirst Pharmacy");
        frame.setSize(1000, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // =====================================================
        // MAIN PANEL
        // =====================================================
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_GREEN);

        // =====================================================
        // TOP HEADER
        // =====================================================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),BorderFactory.createEmptyBorder(16, 25, 16, 25)));

        // Application title
        JLabel titleLabel = new JLabel("HealthFirst Pharmacy");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(PRIMARY_GREEN);

        // Application subtitle
        JLabel subtitleLabel = new JLabel("Pharmacy Inventory Management System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(GREY_TEXT);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(WHITE);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subtitleLabel);

        // Logged-in user
        JLabel userLabel = new JLabel(username + "  |  " + role);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setForeground(DARK_TEXT);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(userLabel, BorderLayout.EAST);

        // =====================================================
        // SIDEBAR
        // =====================================================
        JPanel sidebar = new JPanel();

        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(WHITE);
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 15, 20, 15));

        // =====================================================
        // SIDEBAR LOGO
        // =====================================================
        JLabel logo = new JLabel("HEALTHFIRST");
        logo.setFont(new Font("Arial", Font.BOLD, 21));
        logo.setForeground(PRIMARY_GREEN);

        JLabel pharmacyLabel = new JLabel("PHARMACY");
        pharmacyLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        pharmacyLabel.setForeground(GREY_TEXT);

        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 12));
        menuLabel.setForeground(GREY_TEXT);

        sidebar.add(logo);
        sidebar.add(pharmacyLabel);
        sidebar.add(Box.createVerticalStrut(30));
        sidebar.add(menuLabel);
        sidebar.add(Box.createVerticalStrut(12));

        // =====================================================
        // SIDEBAR BUTTONS
        // =====================================================
        JButton dashboardButton = new JButton("Dashboard");
        JButton usersButton = new JButton("Manage Users");
        JButton suppliersButton = new JButton("Manage Suppliers");
        JButton medicinesButton = new JButton("Manage Medicines");
        JButton salesButton = new JButton("Process Sale");
        JButton reportsButton = new JButton("Reports");
        JButton logoutButton = new JButton("Logout");

        // =====================================================
        // LOGOUT BUTTON
        // =====================================================
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                frame.dispose();
                LoginForm loginForm = new LoginForm();
                loginForm.showLogin();
            }
        });

        // =====================================================
        // STYLE SIDEBAR BUTTONS
        // =====================================================
        JButton[] menuButtons = {
                dashboardButton,
                usersButton,
                suppliersButton,
                medicinesButton,
                salesButton,
                reportsButton,
                logoutButton
        };

        for (JButton button : menuButtons) {button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            button.setBackground(WHITE);
            button.setForeground(DARK_TEXT);
            button.setOpaque(true);
        }


        // Highlight Dashboard
        dashboardButton.setBackground(SOFT_GREEN);
        dashboardButton.setForeground(PRIMARY_GREEN);
        dashboardButton.setFont(new Font("Arial", Font.BOLD, 14));

        // =====================================================
        // SAME SIZE FOR ALL BUTTONS
        // =====================================================
        Dimension buttonSize = new Dimension(190, 38);

        for (JButton button : menuButtons) {
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
        }

        // =====================================================
        // ROLE-BASED MENU
        // =====================================================
        sidebar.add(dashboardButton);
        sidebar.add(Box.createVerticalStrut(6));

        // Admin menu
        if (role.equalsIgnoreCase("Admin")) {
            sidebar.add(usersButton);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(suppliersButton);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(medicinesButton);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(salesButton);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(reportsButton);
        } else {
            // Cashier menu
            sidebar.add(medicinesButton);
            sidebar.add(Box.createVerticalStrut(6));
            sidebar.add(salesButton);
        }

        // Push Logout to bottom
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutButton);

        // =====================================================
        // MAIN CONTENT AREA
        // =====================================================
        final JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(LIGHT_GREEN);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(28, 30, 30, 30));

        // =====================================================
        // WELCOME MESSAGE
        // =====================================================
        JLabel welcomeLabel = new JLabel("Welcome back, " + username);

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setForeground(DARK_TEXT);
        JLabel descriptionLabel = new JLabel("Here's your pharmacy overview.");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        descriptionLabel.setForeground(GREY_TEXT);

        JPanel welcomePanel = new JPanel();

        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(LIGHT_GREEN);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(4));
        welcomePanel.add(descriptionLabel);

        contentPanel.add(welcomePanel, BorderLayout.NORTH);

        // =====================================================
        // PAGE CONTENT AREA
        // =====================================================
        final JPanel pagePanel = new JPanel(new BorderLayout());

        pagePanel.setBackground(LIGHT_GREEN);
        pagePanel.add(createDashboardContent(), BorderLayout.CENTER);
        contentPanel.add(pagePanel, BorderLayout.CENTER);

        // =====================================================
        // SIDEBAR BUTTON ACTIONS / NAVIGATION
        // =====================================================
        // Dashboard
        dashboardButton.addActionListener(e -> {
            showContent(pagePanel, createDashboardContent());
        });


        // Manage Users
        usersButton.addActionListener(e -> {
            showContent(pagePanel, createPlaceholderPanel("USER MANAGEMENT","Manage pharmacy system users here."));
        });

        // Manage Suppliers
        suppliersButton.addActionListener(e -> {
            showContent(pagePanel, createPlaceholderPanel("SUPPLIER MANAGEMENT", "Manage pharmacy suppliers here."));
        });

        // Manage Medicines
        medicinesButton.addActionListener(e -> {
            showContent(pagePanel, createMedicinePanel("MEDICINE MANAGEMENT", "Manage medicines and inventory here."));
        });


        // Process Sale
        salesButton.addActionListener(e -> {
            showContent(pagePanel, createPlaceholderPanel("PROCESS SALE", "Process pharmacy sales here."));
        });

        // Reports
        reportsButton.addActionListener(e -> {
            showContent(pagePanel, createPlaceholderPanel("REPORTS", "View pharmacy reports here."));
        });

        // =====================================================
        // ADD EVERYTHING TO MAIN PANEL
        // =====================================================
        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        mainPanel.add(
                sidebar,
                BorderLayout.WEST
        );

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // PUT MAIN PANEL INSIDE WINDOW
        // =====================================================

        frame.add(mainPanel);

        // Show dashboard
        frame.setVisible(true);
    }

    // =========================================================
    // 4. DASHBOARD CONTENT METHODS
    // =========================================================
    // ---------------------------------------------------------
    // CREATE DASHBOARD CONTENT
    // ---------------------------------------------------------
    private JPanel createDashboardContent() {

        JPanel dashboardContent = new JPanel();

        dashboardContent.setLayout(new BoxLayout(dashboardContent, BoxLayout.Y_AXIS));
        dashboardContent.setBackground(LIGHT_GREEN);

        // =====================================================
        // STATISTICS CARDS
        // =====================================================
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 16, 0));

        statsPanel.setBackground(LIGHT_GREEN);

        JPanel medicinesCard = createStatCard("MEDICINES", String.valueOf(getCount("medicines")), PRIMARY_GREEN);
        JPanel salesCard = createStatCard("TOTAL SALES", String.format("R%.2f", getTotalSales()), PRIMARY_GREEN);
        JPanel suppliersCard = createStatCard("SUPPLIERS", String.valueOf(getCount("suppliers")), PRIMARY_GREEN);

        statsPanel.add(medicinesCard);
        statsPanel.add(salesCard);
        statsPanel.add(suppliersCard);

        // =====================================================
        // ANALYTICS PANEL
        // =====================================================
        JPanel analyticsPanel = new JPanel(new BorderLayout());

        analyticsPanel.setBackground(WHITE);
        analyticsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), BorderFactory.createEmptyBorder(
                20, 20, 20, 20)
                )
        );


        JLabel analyticsTitle = new JLabel(
                "SALES / INVENTORY ANALYTICS"
        );

        analyticsTitle.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        analyticsTitle.setForeground(DARK_TEXT);


        analyticsPanel.add(
                analyticsTitle,
                BorderLayout.NORTH
        );


        // =====================================================
        // ANALYTICS INFORMATION
        // =====================================================

        JPanel analyticsInfo = new JPanel(
                new GridLayout(
                        2,
                        2,
                        20,
                        15
                )
        );

        analyticsInfo.setBackground(WHITE);

        analyticsInfo.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        0,
                        0,
                        0
                )
        );


        analyticsInfo.add(
                new JLabel(
                        "Inventory Status: "
                                + getCount("medicines")
                                + " Medicines"
                )
        );


        analyticsInfo.add(
                new JLabel(
                        "Today's Sales: R"
                                + String.format(
                                "%.2f",
                                getTotalSales()
                        )
                )
        );


        analyticsInfo.add(
                new JLabel(
                        "Low Stock Items: "
                                + getLowStockCount()
                )
        );


        analyticsInfo.add(
                new JLabel(
                        "Active Suppliers: "
                                + getCount("suppliers")
                )
        );


        analyticsPanel.add(
                analyticsInfo,
                BorderLayout.CENTER
        );


        // =====================================================
        // ADD DASHBOARD SECTIONS
        // =====================================================

        dashboardContent.add(statsPanel);

        dashboardContent.add(
                Box.createVerticalStrut(20)
        );

        dashboardContent.add(analyticsPanel);


        return dashboardContent;
    }

    // ---------------------------------------------------------
    // CREATE STAT CARD
    // ---------------------------------------------------------
    private JPanel createStatCard(String title, String value, Color valueColor){
        JPanel card = new JPanel();

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), BorderFactory.createEmptyBorder(
                                15,
                                18,
                                15,
                                18)));

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(GREY_TEXT);

        JLabel valueLabel = new JLabel(value);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(valueColor);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);

        return card;
    }

    // =========================================================
    // 5. MEDICINE MANAGEMENT METHODS
    // =========================================================
    // ---------------------------------------------------------
    // CREATE MEDICINE MANAGEMENT PANEL
    // ---------------------------------------------------------
    private JPanel createMedicinePanel(String title, String description){
        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(LIGHT_GREEN);
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
        headerPanel.setBackground(LIGHT_GREEN);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        descriptionLabel.setForeground(GREY_TEXT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(descriptionLabel);

        // search panel on the header
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(LIGHT_GREEN);

        topPanel.add(headerPanel);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(LIGHT_GREEN);

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
                "Quantity",
                "Price",
                "Expiry Date",
                "Reorder Level"
        };


        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable medicineTable = new JTable(tableModel);
        loadMedicinesTable(tableModel);

        //action listener
        searchButton.addActionListener(e -> {
            searchMeds(searchField.getText(), tableModel);
        });

        showAllButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            loadMedicinesTable(tableModel);
        });

        medicineTable.setRowHeight(30);
        medicineTable.setFont(new Font("Arial", Font.PLAIN, 13));
        medicineTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        medicineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        medicineTable.setFillsViewportHeight(true);

        // low stock highlighter
        medicineTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                int quantity = Integer.parseInt(table.getValueAt(row, 3).toString());
                int reorderLevel = Integer.parseInt(table.getValueAt(row, 6).toString());

                if (!isSelected && quantity <= reorderLevel) {
                    component.setBackground(new Color(255, 235, 235));
                    component.setForeground(Color.RED);
                } else if (!isSelected) {
                    component.setBackground(Color.WHITE);
                    component.setForeground(DARK_TEXT);
                }

                return component;
            }
        });

        JScrollPane scrollPane = new JScrollPane(medicineTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));

        // =====================================================
        // ADD TABLE TO PANEL
        // =====================================================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(LIGHT_GREEN);

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

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ADD
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

    // EDIT
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
        //Dropdown
        JComboBox<String> supplierDropDown = new JComboBox<>();
        loadSuppliersDropdown(supplierDropDown);
        // for loop to ....
        for (int i = 0; i < supplierDropDown.getItemCount(); i++) {
            String supplierItem = supplierDropDown.getItemAt(i);

            if (supplierItem.startsWith(currSupplierId + " - ")) {
                supplierDropDown.setSelectedIndex(i);
                break;
            }
        }
        JTextField quantityField = new JTextField(tableModel.getValueAt(selectedRow, 3).toString());
        JTextField priceField = new JTextField(tableModel.getValueAt(selectedRow, 4).toString());
        JTextField expiryField = new JTextField(tableModel.getValueAt(selectedRow, 5).toString());
        JTextField reorderField = new JTextField(tableModel.getValueAt(selectedRow, 6).toString());

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

    // DELETE
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

    // Load Table to Medicines page
    private void loadMedicinesTable(DefaultTableModel tableModel) {
        String sql = "SELECT medicine_id, medicine_name, category, quantity, price, expiry_date, reorder_level FROM medicines";

        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("medicine_id"),
                        resultSet.getString("medicine_name"),
                        resultSet.getString("category"),
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

    //Supplier Dropdown
    private void loadSuppliersDropdown(JComboBox<String> supplierComboBox) {
        String sql = "SELECT supplier_id, supplier_name FROM suppliers ORDER BY supplier_name";

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

    //Getting supplierid for dropdown
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

    //search for meds
    private void searchMeds(String searchText, DefaultTableModel tableModel) {
        String sql = "SELECT medicine_id, medicine_name, category, quantity, price, expiry_date, reorder_level FROM medicines WHERE medicine_name LIKE ? OR category LIKE ?";

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

    // =========================================================
    // 6. OTHER PAGE METHODS
    // =========================================================
    // ---------------------------------------------------------
    // CREATE PLACEHOLDER PAGE
    // ---------------------------------------------------------
    private JPanel createPlaceholderPanel(String title,String description){
        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(LIGHT_GREEN);
        panel.setBorder(BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10));

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        descriptionLabel.setForeground(GREY_TEXT);

        JPanel textPanel = new JPanel();

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(LIGHT_GREEN);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(descriptionLabel);

        panel.add(textPanel, BorderLayout.NORTH);
        return panel;
    }

    // =========================================================
    // 7. NAVIGATION METHODS
    // =========================================================
    // ---------------------------------------------------------
    // CHANGE MIDDLE CONTENT
    // ---------------------------------------------------------
    private void showContent(JPanel contentPanel, JPanel newPanel){
        contentPanel.removeAll();
        contentPanel.add(newPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================================================
    // 8. DATABASE METHODS
    // =========================================================
    // ---------------------------------------------------------
    // GET COUNT FROM DATABASE
    // ---------------------------------------------------------
    private int getCount(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try (Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ---------------------------------------------------------
    // GET TOTAL SALES
    // ---------------------------------------------------------
    private double getTotalSales() {

        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM sales";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // --------------------------------------------------------
    // GET LOW STOCK COUNT
    // ---------------------------------------------------------
    private int getLowStockCount() {
        String sql = "SELECT COUNT(*) FROM medicines " + "WHERE quantity <= reorder_level";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}