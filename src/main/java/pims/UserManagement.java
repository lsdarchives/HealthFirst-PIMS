package pims;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserManagement {

    private JFrame frame;

    public UserManagement(JFrame frame) {
        this.frame = frame;
    }

    private void loadUsersTable(DefaultTableModel tableModel) {
        String sql = "SELECT user_id, username, role FROM users ORDER BY username ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("role")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    frame,
                    "Could not load users from the database.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void searchUsers(String searchText, DefaultTableModel tableModel) {
        String sql = "SELECT user_id, username, role FROM users WHERE username LIKE ? OR role LIKE ? ORDER BY username";

        tableModel.setRowCount(0);

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String searchValue = "%" + searchText + "%";

            preparedStatement.setString(1, searchValue);
            preparedStatement.setString(2, searchValue);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("role")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    frame,
                    "Could not search users.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void addUser(DefaultTableModel tableModel) {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Admin", "Cashier"});

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);

        int result = JOptionPane.showConfirmDialog(
                frame,
                panel,
                "Add User",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = roleComboBox.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Username and password are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);

            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(
                    frame,
                    "User added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            tableModel.setRowCount(0);
            loadUsersTable(tableModel);

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    frame,
                    "Could not add user.\nThe username may already exist.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void editUser(JTable userTable, DefaultTableModel tableModel) {
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please select a user to edit.",
                    "No User Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int userId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String currentUsername = tableModel.getValueAt(selectedRow, 1).toString();
        String currentRole = tableModel.getValueAt(selectedRow, 2).toString();

        JTextField usernameField = new JTextField(currentUsername);
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Admin", "Cashier"});
        roleComboBox.setSelectedItem(currentRole);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("New Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);

        int result = JOptionPane.showConfirmDialog(
                frame,
                panel,
                "Edit User",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = roleComboBox.getSelectedItem().toString();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Username is required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (password.isEmpty()) {
                String sql = "UPDATE users SET username = ?, role = ? WHERE user_id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, role);
                    preparedStatement.setInt(3, userId);

                    preparedStatement.executeUpdate();
                }

            } else {
                String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE user_id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    preparedStatement.setString(3, role);
                    preparedStatement.setInt(4, userId);

                    preparedStatement.executeUpdate();
                }
            }

            JOptionPane.showMessageDialog(
                    frame,
                    "User updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            tableModel.setRowCount(0);
            loadUsersTable(tableModel);

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    frame,
                    "Could not update user.\nThe username may already exist.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void deleteUser(JTable userTable, DefaultTableModel tableModel) {
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please select a user to delete.",
                    "No User Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int userId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String username = tableModel.getValueAt(selectedRow, 1).toString();

        int confirmation = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete user: " + username + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(
                    frame,
                    "User deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            tableModel.setRowCount(0);
            loadUsersTable(tableModel);

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    frame,
                    "Could not delete user.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public JPanel createUserPanel(String title, String description) {
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

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Dashboard.LIGHT_GREEN);

        topPanel.add(headerPanel);
        topPanel.add(searchPanel);

        panel.add(topPanel, BorderLayout.NORTH);

        // =====================================================
        // USER TABLE
        // =====================================================
        String[] columns = {
                "ID",
                "Username",
                "Role"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable userTable = new JTable(tableModel);

        loadUsersTable(tableModel);

        userTable.setRowHeight(30);
        userTable.setFont(new Font("Arial", Font.PLAIN, 13));
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(userTable);

        // SELECTED USER DETAILS
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(1, 3, 10, 10));
        detailsPanel.setBackground(Dashboard.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Selected User Details"));

        JLabel detailsId = new JLabel("ID: ");
        JLabel detailsUsername = new JLabel("Username: ");
        JLabel detailsRole = new JLabel("Role: ");

        detailsPanel.add(detailsId);
        detailsPanel.add(detailsUsername);
        detailsPanel.add(detailsRole);

        scrollPane.setBorder(BorderFactory.createLineBorder(Dashboard.BORDER));

        // =====================================================
        // SEARCH ACTION
        // =====================================================
        searchButton.addActionListener(e -> {
            searchUsers(searchField.getText(), tableModel);
        });

        showAllButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            loadUsersTable(tableModel);
        });

        // =====================================================
        // USER BUTTONS
        // =====================================================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Dashboard.LIGHT_GREEN);

        JButton addButton = new JButton("Add User");
        JButton editButton = new JButton("Edit User");
        JButton deleteButton = new JButton("Delete User");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> {
            addUser(tableModel);
        });

        editButton.addActionListener(e -> {
            editUser(userTable, tableModel);
        });

        deleteButton.addActionListener(e -> {
            deleteUser(userTable, tableModel);
        });

        JPanel tableAndDetailsPanel = new JPanel(new BorderLayout(10, 10));
        tableAndDetailsPanel.setBackground(Dashboard.LIGHT_GREEN);

        tableAndDetailsPanel.add(scrollPane, BorderLayout.CENTER);
        tableAndDetailsPanel.add(detailsPanel, BorderLayout.SOUTH);

        panel.add(tableAndDetailsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // SELECTED USER
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userTable.getSelectedRow() != -1) {
                int selectedRow = userTable.getSelectedRow();

                detailsId.setText("ID: " + userTable.getValueAt(selectedRow, 0));
                detailsUsername.setText("Username: " + userTable.getValueAt(selectedRow, 1));
                detailsRole.setText("Role: " + userTable.getValueAt(selectedRow, 2));
            }
        });

        return panel;
    }
}
