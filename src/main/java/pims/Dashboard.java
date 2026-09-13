package pims;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Dashboard {

    private JFrame frame;

    // =========================================================
    // HEALTH-FIRST COLOUR PALETTE
    // =========================================================
    private static final Color PRIMARY_GREEN = new Color(25, 135, 84);
    private static final Color DARK_GREEN = new Color(20, 108, 67);
    private static final Color LIGHT_GREEN = new Color(240, 248, 243);
    private static final Color SOFT_GREEN = new Color(220, 239, 229);

    private static final Color WHITE = Color.WHITE;
    private static final Color DARK_TEXT = new Color(31, 41, 55);
    private static final Color GREY_TEXT = new Color(107, 114, 128);
    private static final Color BORDER = new Color(217, 231, 223);

    public Dashboard(String username, String role) {

        // =========================================================
        // CREATE THE WINDOW
        // =========================================================
        frame = new JFrame("HealthFirst Pharmacy");

        frame.setSize(1000, 650);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        // =========================================================
        // MAIN PANEL
        // =========================================================
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.setBackground(LIGHT_GREEN);

        // =========================================================
        // TOP HEADER
        // =========================================================
        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(WHITE);

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0, BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                16, 25, 16, 25
                        )
                )
        );

        // Application title
        JLabel titleLabel = new JLabel("HealthFirst Pharmacy");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        titleLabel.setForeground(PRIMARY_GREEN);

        // Application subtitle
        JLabel subtitleLabel = new JLabel(
                "Pharmacy Inventory Management System"
        );

        subtitleLabel.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        subtitleLabel.setForeground(GREY_TEXT);

        // Title panel
        JPanel titlePanel = new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBackground(WHITE);

        titlePanel.add(titleLabel);

        titlePanel.add(
                Box.createVerticalStrut(3)
        );

        titlePanel.add(subtitleLabel);

        // Logged-in user
        JLabel userLabel = new JLabel(
                username + "  |  " + role
        );

        userLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        userLabel.setForeground(DARK_TEXT);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                userLabel,
                BorderLayout.EAST
        );

        // =========================================================
        // SIDEBAR
        // =========================================================
        JPanel sidebar = new JPanel();

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        sidebar.setPreferredSize(
                new Dimension(220, 0)
        );

        sidebar.setBackground(WHITE);

        sidebar.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 15, 20, 15
                )
        );

        // =========================================================
        // SIDEBAR LOGO
        // =========================================================
        JLabel logo = new JLabel("HEALTHFIRST");

        logo.setFont(
                new Font("Arial", Font.BOLD, 21)
        );

        logo.setForeground(PRIMARY_GREEN);

        JLabel pharmacyLabel = new JLabel("PHARMACY");

        pharmacyLabel.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );

        pharmacyLabel.setForeground(GREY_TEXT);

        JLabel menuLabel = new JLabel("MENU");

        menuLabel.setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        menuLabel.setForeground(GREY_TEXT);

        sidebar.add(logo);
        sidebar.add(pharmacyLabel);

        sidebar.add(
                Box.createVerticalStrut(30)
        );

        sidebar.add(menuLabel);

        sidebar.add(
                Box.createVerticalStrut(12)
        );

        // =========================================================
        // SIDEBAR BUTTONS
        // =========================================================
        JButton dashboardButton = new JButton("Dashboard");
        JButton usersButton = new JButton("Manage Users");
        JButton suppliersButton = new JButton("Manage Suppliers");
        JButton medicinesButton = new JButton("Manage Medicines");
        JButton salesButton = new JButton("Process Sale");
        JButton reportsButton = new JButton("Reports");
        JButton logoutButton = new JButton("Logout");

        // =========================================================
        // LOGOUT BUTTON
        // =========================================================
        logoutButton.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                frame.dispose();

                LoginForm loginForm = new LoginForm();

                loginForm.showLogin();
            }
        });

        // =========================================================
        // STYLE SIDEBAR BUTTONS
        // =========================================================
        JButton[] menuButtons = {
                dashboardButton,
                usersButton,
                suppliersButton,
                medicinesButton,
                salesButton,
                reportsButton,
                logoutButton
        };

        for (JButton button : menuButtons) {

            button.setFont(
                    new Font("Arial", Font.PLAIN, 14)
            );

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

        dashboardButton.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        // =========================================================
        // SAME SIZE FOR ALL BUTTONS
        // =========================================================
        Dimension buttonSize = new Dimension(
                190,
                38
        );

        for (JButton button : menuButtons) {

            button.setPreferredSize(buttonSize);

            button.setMinimumSize(buttonSize);

            button.setMaximumSize(buttonSize);
        }

        // =========================================================
        // ROLE-BASED MENU
        // =========================================================
        sidebar.add(dashboardButton);

        sidebar.add(
                Box.createVerticalStrut(6)
        );

        // Admin menu
        if (role.equalsIgnoreCase("Admin")) {

            sidebar.add(usersButton);

            sidebar.add(
                    Box.createVerticalStrut(6)
            );

            sidebar.add(suppliersButton);

            sidebar.add(
                    Box.createVerticalStrut(6)
            );

            sidebar.add(medicinesButton);

            sidebar.add(
                    Box.createVerticalStrut(6)
            );

            sidebar.add(salesButton);

            sidebar.add(
                    Box.createVerticalStrut(6)
            );

            sidebar.add(reportsButton);

        } else {

            // Cashier menu
            sidebar.add(medicinesButton);

            sidebar.add(
                    Box.createVerticalStrut(6)
            );

            sidebar.add(salesButton);
        }

        // Push Logout to bottom
        sidebar.add(
                Box.createVerticalGlue()
        );

        sidebar.add(logoutButton);

        // =========================================================
        // MAIN CONTENT AREA
        // =========================================================
        final JPanel contentPanel = new JPanel(
                new BorderLayout()
        );

        contentPanel.setBackground(LIGHT_GREEN);

        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        28, 30, 30, 30
                )
        );

        // =========================================================
        // WELCOME MESSAGE
        // =========================================================
        JLabel welcomeLabel = new JLabel(
                "Welcome back, " + username
        );

        welcomeLabel.setFont(
                new Font("Arial", Font.BOLD, 26)
        );

        welcomeLabel.setForeground(DARK_TEXT);

        JLabel descriptionLabel = new JLabel(
                "Here's your pharmacy overview."
        );

        descriptionLabel.setFont(
                new Font("Arial", Font.PLAIN, 15)
        );

        descriptionLabel.setForeground(GREY_TEXT);

        JPanel welcomePanel = new JPanel();

        welcomePanel.setLayout(
                new BoxLayout(
                        welcomePanel,
                        BoxLayout.Y_AXIS
                )
        );

        welcomePanel.setBackground(LIGHT_GREEN);

        welcomePanel.add(welcomeLabel);

        welcomePanel.add(
                Box.createVerticalStrut(4)
        );

        welcomePanel.add(descriptionLabel);

        contentPanel.add(
                welcomePanel,
                BorderLayout.NORTH
        );

        // =========================================================
// PAGE CONTENT AREA
// =========================================================
        final JPanel pagePanel = new JPanel(
                new BorderLayout()
        );

        pagePanel.setBackground(LIGHT_GREEN);

        pagePanel.add(
                createDashboardContent(),
                BorderLayout.CENTER
        );

        contentPanel.add(
                pagePanel,
                BorderLayout.CENTER
        );
        // =========================================================
        // SIDEBAR BUTTON ACTIONS
        // =========================================================

        // Dashboard
        dashboardButton.addActionListener(e -> {
            showContent(pagePanel, createDashboardContent());
        });

        // Manage Users
        usersButton.addActionListener(e -> {
            showContent(pagePanel, createPlaceholderPanel("USER MANAGEMENT", "Manage pharmacy system users here."));
        });

        // Manage Suppliers
        suppliersButton.addActionListener(e -> {
            showContent(pagePanel, createPlaceholderPanel("SUPPLIER MANAGEMENT", "Manage pharmacy suppliers here."));
        });

        // Manage Medicines
        medicinesButton.addActionListener(e -> {
            showContent(pagePanel, createPlaceholderPanel("MEDICINE MANAGEMENT", "Manage medicines and inventory here."
                    )
            );

        });

        // Process Sale
        salesButton.addActionListener(e -> {
            showContent(pagePanel, createPlaceholderPanel("PROCESS SALE", "Process pharmacy sales here."));
        });

        // Reports
        reportsButton.addActionListener(e -> {
            showContent(pagePanel,createPlaceholderPanel("REPORTS", "View pharmacy reports here."));
        });

        // =========================================================
        // ADD EVERYTHING TO MAIN PANEL
        // =========================================================
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel,BorderLayout.CENTER);

        // =========================================================
        // PUT MAIN PANEL INSIDE WINDOW
        // =========================================================
        frame.add(mainPanel);
        // Show dashboard
        frame.setVisible(true);
    }

    // =========================================================
    // CREATE DASHBOARD CONTENT
    // =========================================================
    private JPanel createDashboardContent() {

        JPanel dashboardContent = new JPanel();

        dashboardContent.setLayout(new BoxLayout(dashboardContent, BoxLayout.Y_AXIS));
        dashboardContent.setBackground(LIGHT_GREEN);

        // =========================================================
        // STATISTICS CARDS
        // =========================================================
        JPanel statsPanel = new JPanel(
                new GridLayout(
                        1,
                        3,
                        16,
                        0
                )
        );

        statsPanel.setBackground(LIGHT_GREEN);

        JPanel medicinesCard = createStatCard(
                "MEDICINES", String.valueOf(getCount("medicines")),
                PRIMARY_GREEN
        );

        JPanel salesCard = createStatCard(
                "TOTAL SALES",
                String.format("R%.2f", getTotalSales()),
                PRIMARY_GREEN
        );

        JPanel suppliersCard = createStatCard(
                "SUPPLIERS",
                String.valueOf(getCount("suppliers")),
                PRIMARY_GREEN
        );

        statsPanel.add(medicinesCard);

        statsPanel.add(salesCard);

        statsPanel.add(suppliersCard);

        // =========================================================
        // ANALYTICS PANEL
        // =========================================================
        JPanel analyticsPanel = new JPanel(
                new BorderLayout()
        );

        analyticsPanel.setBackground(WHITE);

        analyticsPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
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

        // =========================================================
        // ANALYTICS INFORMATION
        // =========================================================
        JPanel analyticsInfo = new JPanel(
                new GridLayout(
                        2,
                        2,
                        20,
                        15
                )
        );

        analyticsInfo.setBackground(WHITE);

        analyticsInfo.setBorder(BorderFactory.createEmptyBorder(
                        20,
                        0,
                        0,
                        0));

        analyticsInfo.add(new JLabel("Inventory Status: " + getCount("medicines") + " Medicines"));
        analyticsInfo.add(new JLabel("Today's Sales: R" + String.format("%.2f", getTotalSales())));
        analyticsInfo.add(new JLabel("Low Stock Items: " + getLowStockCount()));
        analyticsInfo.add(new JLabel("Active Suppliers: " + getCount("suppliers")));

        analyticsPanel.add(analyticsInfo, BorderLayout.CENTER);

        // =========================================================
        // ADD DASHBOARD SECTIONS
        // =========================================================
        dashboardContent.add(statsPanel);
        dashboardContent.add(Box.createVerticalStrut(20));
        dashboardContent.add(analyticsPanel);

        return dashboardContent;
    }

    // =========================================================
    // CREATE STAT CARD
    // =========================================================
    private JPanel createStatCard(
            String title,
            String value,
            Color valueColor
    ) {

        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                15,
                                18,
                                15,
                                18
                        )
                )
        );

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
    // CREATE PLACEHOLDER PAGE
    // =========================================================
    private JPanel createPlaceholderPanel(String title, String description) {

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
// GET COUNT FROM DATABASE
// =========================================================
    private int getCount(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // =========================================================
// GET TOTAL SALES
// =========================================================
    private double getTotalSales() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM sales";
        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // =========================================================
// GET LOW STOCK COUNT
// =========================================================
    private int getLowStockCount() {
        String sql = "SELECT COUNT(*) FROM medicines WHERE quantity <= reorder_level";
        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // =========================================================
    // CHANGE MIDDLE CONTENT
    // =========================================================
    private void showContent(JPanel contentPanel, JPanel newPanel){
        contentPanel.removeAll();
        contentPanel.add(
                newPanel,
                BorderLayout.CENTER
        );
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}