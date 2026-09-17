package pims;

import javax.swing.*;
import java.sql.*;

public class LoginForm {

    private JPanel jPanel;
    private JTextField txtUsername;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JPasswordField txtPass;
    private JButton loginBtn;
    private JLabel lblStatus;
    private JLabel lblSubtitle;
    private JLabel lblTitle;

    public LoginForm(){
        loginBtn.addActionListener(e -> login());
    }

    public JPanel getjPanel() {
        return jPanel;
    }

    public void showLogin(){
        JFrame frame = new JFrame("HealthFirst Pharmacy");

        frame.setContentPane(this.jPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPass.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please enter username and password.");
            return;
        }

        String sql = "SELECT role, status FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                // 1. Get the user's role
                String role = result.getString("role");
                // 2. Get the user's account status
                String status = result.getString("status");
                // 3. Check if the account is inactive
                if (status.equals("Inactive")) {
                    // 4. Show the inactive account message
                    lblStatus.setText("This account is inactive.");
                    // 5. Stop the login process
                    return;
                }
                // 6. Show the successful login message
                lblStatus.setText("Login successful!");
                // 7. Open the dashboard
                new Dashboard(username, role);
            } else {
                // 8. Show the invalid login message
                lblStatus.setText("Invalid username or password.");
            }
        } catch (SQLException e) {
            lblStatus.setText("Database error.");
            e.printStackTrace();
        }
    }
}
