package pims;

import javax.swing.*;

public class Dashboard {
    private JFrame frame;

    public Dashboard(String username, String role) {
        frame = new JFrame("HealthFirst Pharmacy - Dashboard");

        JPanel panel = new JPanel();

        JLabel welcomeLabel = new JLabel(
                "Welcome, " + username + " (" + role + ")"
        );

        panel.add(welcomeLabel);

        // Admin features
        if (role.equals("Admin")) {
            JButton usersButton = new JButton("Manage Users");
            JButton suppliersButton = new JButton("Manage Suppliers");
            JButton medicinesButton = new JButton("Manage Medicines");
            JButton reportsButton = new JButton("Reports");

            panel.add(usersButton);
            panel.add(suppliersButton);
            panel.add(medicinesButton);
            panel.add(reportsButton);
        }

        // Cashier features
        else if (role.equals("Cashier")) {
            JButton salesButton = new JButton("Process Sale");
            JButton medicinesButton = new JButton("View Medicines");

            panel.add(salesButton);
            panel.add(medicinesButton);
        }

        // Logout button for both roles
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            frame.dispose();

            JFrame loginFrame = new JFrame("HealthFirst Pharmacy");
            loginFrame.setContentPane(new LoginForm().getjPanel());
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(500, 400);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });
        panel.add(logoutButton);
        frame.add(panel);

        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}