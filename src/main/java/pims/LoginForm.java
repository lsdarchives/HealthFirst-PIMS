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

        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String role = result.getString("role");
                lblStatus.setText("Login successful!");
                new Dashboard(username, role);
            } else {
                lblStatus.setText("Invalid username or password.");
            }

        } catch (SQLException e) {
            lblStatus.setText("Database error.");
            e.printStackTrace();
        }
    }
}
