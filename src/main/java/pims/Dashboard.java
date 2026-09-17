package pims;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Dashboard {

    // 1. CLASS VARIABLES
    private JFrame frame;

    // 2. HEALTH-FIRST COLOUR PALETTE
    static final Color PRIMARY_GREEN = new Color(25, 135, 84);
    static final Color DARK_GREEN = new Color(20, 108, 67);
    static final Color LIGHT_GREEN = new Color(240, 248, 243);
    static final Color SOFT_GREEN = new Color(220, 239, 229);

    static final Color WHITE = Color.WHITE;
    static final Color DARK_TEXT = new Color(31, 41, 55);
    static final Color GREY_TEXT = new Color(107, 114, 128);
    static final Color BORDER = new Color(217, 231, 223);

    // 3. CONSTRUCTOR / DASHBOARD SETUP
    public Dashboard(String username, String role) {

        // CREATE WINDOW
        frame = new JFrame("HealthFirst Pharmacy");

        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // CREATE MANAGEMENT MODULES
        SupplierManagement supplierManagement = new SupplierManagement(frame);
        MedicineManagement medicineManagement = new MedicineManagement(frame);
        UserManagement userManagement = new UserManagement(frame);
        PointOfSale pointOfSale = new PointOfSale(frame, username);
        SalesReport salesReport = new SalesReport(frame);
        ExpiryReport expiryReport = new ExpiryReport(frame);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_GREEN);

        // PAGE CONTENT AREA
        final JPanel pagePanel = new JPanel(new BorderLayout());
        pagePanel.setBackground(LIGHT_GREEN);
        pagePanel.add(createDashboardContent(), BorderLayout.CENTER);

        // HEADER
        JPanel header = createHeader(username, role);

        // SIDEBAR
        JPanel sidebar = createSidebar(
                role,
                username,
                pagePanel,
                supplierManagement,
                medicineManagement,
                userManagement,
                pointOfSale,
                salesReport,
                expiryReport
        );

        // MAIN CONTENT
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(LIGHT_GREEN);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(28, 30, 30, 30));

        // WELCOME MESSAGE
        JPanel welcomePanel = createWelcomePanel(username);

        contentPanel.add(welcomePanel, BorderLayout.NORTH);

        contentPanel.add(pagePanel, BorderLayout.CENTER);

        // ADD EVERYTHING TO MAIN PANEL
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // SHOW WINDOW
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // 4. HEADER
    private JPanel createHeader(String username, String role) {

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

        return header;
    }

    // 5. SIDEBAR
    private JPanel createSidebar(
            String role, String username,
            JPanel pagePanel, SupplierManagement supplierManagement,
            MedicineManagement medicineManagement,
            UserManagement userManagement, PointOfSale pointOfSale,
            SalesReport salesReport, ExpiryReport expiryReport) {

        JPanel sidebar = new JPanel();

        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(WHITE);
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 15, 20, 15));

        // SIDEBAR LOGO
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

        // SIDEBAR BUTTONS
        JButton dashboardButton = new JButton("Dashboard");
        JButton usersButton = new JButton("Manage Users");
        JButton suppliersButton = new JButton("Manage Suppliers");
        JButton medicinesButton = new JButton("Manage Medicines");
        JButton salesButton = new JButton("Process Sale");
        JButton reportsButton = new JButton("Reports");
        JButton logoutButton = new JButton("Logout");

        // STYLE SIDEBAR BUTTONS
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

            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

            button.setBackground(WHITE);
            button.setForeground(DARK_TEXT);
            button.setOpaque(true);
        }

        // HIGHLIGHT DASHBOARD
        dashboardButton.setBackground(SOFT_GREEN);
        dashboardButton.setForeground(PRIMARY_GREEN);
        dashboardButton.setFont(new Font("Arial", Font.BOLD, 14));

        // BUTTON SIZE
        Dimension buttonSize = new Dimension(190, 38);

        for (JButton button : menuButtons) {

            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
        }

        // ROLE-BASED MENU
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

        // LOGOUT
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutButton);

        // DASHBOARD NAVIGATION
        dashboardButton.addActionListener(e -> {
            showContent(pagePanel, createDashboardContent());
        });

        // USER MANAGEMENT NAVIGATION
        usersButton.addActionListener(e -> {
            showContent(pagePanel, userManagement.createUserPanel("USER MANAGEMENT","Manage pharmacy system users here."));
        });

        // SUPPLIER MANAGEMENT NAVIGATION
        suppliersButton.addActionListener(e -> {
            showContent(pagePanel, supplierManagement.createSupplierPanel("SUPPLIER MANAGEMENT", "Manage pharmacy suppliers here."));
        });

        // MEDICINE MANAGEMENT NAVIGATION
        medicinesButton.addActionListener(e -> {
            showContent(pagePanel, medicineManagement.createMedicinePanel("MEDICINE MANAGEMENT", "Manage medicines and inventory here."));
        });

        // POINT OF SALE NAVIGATION
        salesButton.addActionListener(e -> {
            showContent(pagePanel, pointOfSale.createSalesPanel("PROCESS SALE", "Process pharmacy sales here."));
        });

        // REPORTS NAVIGATION
        reportsButton.addActionListener(e -> {
            showContent(pagePanel, createReportsPanel(pagePanel, salesReport, expiryReport));
        });

        // LOGOUT NAVIGATION
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

        return sidebar;
    }

    // 6. WELCOME PANEL
    private JPanel createWelcomePanel(String username) {

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

        return welcomePanel;
    }

    // 7. DASHBOARD CONTENT
    private JPanel createDashboardContent() {

        JPanel dashboardContent = new JPanel();

        dashboardContent.setLayout(
                new BoxLayout(
                        dashboardContent,
                        BoxLayout.Y_AXIS
                )
        );

        dashboardContent.setBackground(LIGHT_GREEN);

        // STATISTICS CARDS
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 16, 0));

        statsPanel.setBackground(LIGHT_GREEN);

        JPanel medicinesCard = createStatCard(
                "MEDICINES",
                String.valueOf(getCount("medicines")),
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
        JPanel todaySalesCard = createStatCard(
                "TODAY'S SALES",
                String.format("R%.2f", getTodaySales()),
                PRIMARY_GREEN
        );

        statsPanel.add(medicinesCard);
        statsPanel.add(salesCard);
        statsPanel.add(suppliersCard);
        statsPanel.add(todaySalesCard);

        // ANALYTICS PANEL
        JPanel analyticsPanel = new JPanel(
                new BorderLayout()
        );

        analyticsPanel.setBackground(WHITE);
        analyticsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), BorderFactory.createEmptyBorder(
                        20, 20, 20, 20)
                )
        );

        JLabel analyticsTitle = new JLabel("SALES / INVENTORY ANALYTICS");

        analyticsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        analyticsTitle.setForeground(DARK_TEXT);

        analyticsPanel.add(
                analyticsTitle,
                BorderLayout.NORTH
        );

        // ANALYTICS INFORMATION
        JPanel analyticsInfo = new JPanel(new GridLayout(2, 2, 20, 15));

        analyticsInfo.setBackground(WHITE);
        analyticsInfo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        analyticsInfo.add(new JLabel("Inventory Status: " + getCount("medicines") + " Medicines"));
        analyticsInfo.add(new JLabel("Today's Sales: R" + String.format("%.2f", getTodaySales())));
        analyticsInfo.add(new JLabel("Low Stock Items: " + getLowStockCount()));
        analyticsInfo.add(new JLabel("Today's Transactions: " + getTodayTransactionCount()));

        analyticsPanel.add(analyticsInfo, BorderLayout.CENTER);

        // ADD DASHBOARD SECTIONS
        dashboardContent.add(statsPanel);
        dashboardContent.add(Box.createVerticalStrut(20));
        dashboardContent.add(analyticsPanel);
        dashboardContent.add(Box.createVerticalStrut(20));
        dashboardContent.add(createSalesGraphPanel());

        return dashboardContent;
    }

    // 8. CREATE STAT CARD
    private JPanel createStatCard(String title, String value, Color valueColor) {
        JPanel card = new JPanel();

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), BorderFactory.createEmptyBorder(15, 18, 15, 18)));

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

    // 9. REPORTS PAGE
    private JPanel createReportsPanel(JPanel pagePanel, SalesReport salesReport, ExpiryReport expiryReport) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBackground(LIGHT_GREEN);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // REPORT HEADER
        JLabel titleLabel = new JLabel("REPORTS");

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);

        JLabel descriptionLabel = new JLabel("View pharmacy sales and medicine expiry reports.");

        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        descriptionLabel.setForeground(GREY_TEXT);

        JPanel headerPanel = new JPanel();

        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(LIGHT_GREEN);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(descriptionLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

        // REPORT BUTTONS
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));

        buttonPanel.setBackground(LIGHT_GREEN);

        JButton salesReportButton = new JButton("Sales Report");
        JButton expiryReportButton = new JButton("Expiry Report");

        buttonPanel.add(salesReportButton);
        buttonPanel.add(expiryReportButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        // SALES REPORT BUTTON
        salesReportButton.addActionListener(e -> {
            showContent(pagePanel, salesReport.createSalesReportPanel(
                            "SALES REPORT",
                            "View completed pharmacy sales."
            ));
        });

        // EXPIRY REPORT BUTTON
        expiryReportButton.addActionListener(e -> {
            showContent(pagePanel, expiryReport.createExpiryReportPanel(
                            "EXPIRY REPORT",
                            "Medicines expiring within the next one month."
            ));
        });
        return panel;
    }

    // 10. PLACEHOLDER PAGE
    private JPanel createPlaceholderPanel(String title, String description) {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(LIGHT_GREEN);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

    // 11. NAVIGATION METHOD
    private void showContent(JPanel contentPanel, JPanel newPanel) {
        contentPanel.removeAll();

        contentPanel.add(newPanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // 12. DATABASE METHODS
    // GET COUNT FROM DATABASE
    private int getCount(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)
        ){
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // GET TOTAL SALES
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

    // GET TODAY'S SALES
    private double getTodaySales() {

        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM sales WHERE DATE(sale_date) = CURDATE()";

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

    // GET LOW STOCK COUNT
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

    // GET SALES FOR THE LAST 7 DAYS
    private double[] getLast7DaysSales() {

        double[] dailySales = new double[7];

        String sql = "SELECT DATE(sale_date) AS sale_day, COALESCE(SUM(total_amount), 0) AS total_sales " +
                "FROM sales " +
                "WHERE sale_date >= CURDATE() - INTERVAL 6 DAY " +
                "GROUP BY DATE(sale_date) " +
                "ORDER BY sale_day";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {

                Date saleDate = resultSet.getDate("sale_day");
                double totalSales = resultSet.getDouble("total_sales");

                long daysAgo = java.time.temporal.ChronoUnit.DAYS.between(
                        saleDate.toLocalDate(),
                        java.time.LocalDate.now()
                );

                int index = 6 - (int) daysAgo;

                if (index >= 0 && index < 7) {
                    dailySales[index] = totalSales;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dailySales;
    }

    // CREATE 7-DAY SALES GRAPH
    private JPanel createSalesGraphPanel() {

        JPanel graphPanel = new JPanel(new BorderLayout());
        graphPanel.setPreferredSize(new Dimension(0, 300));

        graphPanel.setBackground(Color.WHITE);
        graphPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 230, 225)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel title = new JLabel("SALES TREND - LAST 7 DAYS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(DARK_GREEN);

        graphPanel.add(title, BorderLayout.NORTH);

        double[] sales = getLast7DaysSales();

        JPanel chart = new JPanel() {

            @Override
            protected void paintComponent(Graphics graphics) {

                super.paintComponent(graphics);

                Graphics2D g = (Graphics2D) graphics;

                g.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                int width = getWidth();
                int height = getHeight();

                int leftPadding = 60;
                int rightPadding = 20;
                int topPadding = 25;
                int bottomPadding = 45;

                int graphWidth = width - leftPadding - rightPadding;
                int graphHeight = height - topPadding - bottomPadding;

                double maxSales = 0;

                for (double value : sales) {
                    if (value > maxSales) {
                        maxSales = value;
                    }
                }

                if (maxSales == 0) {
                    maxSales = 100;
                }

                // Draw horizontal grid lines
                g.setColor(new Color(230, 235, 232));

                for (int i = 0; i <= 4; i++) {

                    int y = topPadding + (graphHeight * i / 4);

                    g.drawLine(
                            leftPadding,
                            y,
                            width - rightPadding,
                            y
                    );
                }

                // Draw Y-axis
                g.setColor(Color.GRAY);

                g.drawLine(
                        leftPadding,
                        topPadding,
                        leftPadding,
                        height - bottomPadding
                );

                // Draw X-axis
                g.drawLine(
                        leftPadding,
                        height - bottomPadding,
                        width - rightPadding,
                        height - bottomPadding
                );

                // Draw Y-axis values
                g.setFont(new Font("Segoe UI", Font.PLAIN, 11));

                for (int i = 0; i <= 4; i++) {

                    double value = maxSales - (maxSales * i / 4);

                    int y = topPadding + (graphHeight * i / 4);

                    g.setColor(Color.DARK_GRAY);

                    g.drawString(
                            String.format("R%.0f", value),
                            5,
                            y + 5
                    );
                }

                // Draw sales line
                int previousX = 0;
                int previousY = 0;

                for (int i = 0; i < sales.length; i++) {

                    int x = leftPadding +
                            (graphWidth * i / 6);

                    int y = height - bottomPadding -
                            (int) ((sales[i] / maxSales) * graphHeight);

                    // Draw line between points
                    if (i > 0) {

                        g.setColor(PRIMARY_GREEN);

                        g.setStroke(new BasicStroke(3));

                        g.drawLine(
                                previousX,
                                previousY,
                                x,
                                y
                        );
                    }

                    // Draw point
                    g.setColor(PRIMARY_GREEN);

                    g.fillOval(
                            x - 5,
                            y - 5,
                            10,
                            10
                    );

                    // Draw sales amount
                    g.setColor(Color.DARK_GRAY);

                    g.setFont(new Font("Segoe UI", Font.PLAIN, 10));

                    g.drawString(
                            String.format("R%.0f", sales[i]),
                            x - 18,
                            y - 10
                    );

                    previousX = x;
                    previousY = y;
                }

                // Draw day labels
                for (int i = 0; i < 7; i++) {

                    java.time.LocalDate date =
                            java.time.LocalDate.now().minusDays(6 - i);

                    String day =
                            date.getDayOfWeek().toString().substring(0, 3);

                    int x = leftPadding +
                            (graphWidth * i / 6);

                    g.setColor(Color.DARK_GRAY);

                    g.drawString(
                            day,
                            x - 10,
                            height - 20
                    );
                }
            }
        };

        chart.setBackground(Color.WHITE);
        graphPanel.add(chart, BorderLayout.CENTER);

        return graphPanel;
    }

    // GET TODAY'S TRANSACTION COUNT
    private int getTodayTransactionCount() {

        String sql = "SELECT COUNT(*) FROM sales WHERE DATE(sale_date) = CURDATE()";

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