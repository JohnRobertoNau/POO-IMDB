

package org.example;

import org.example.IMDB;
import org.example.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class LoginGUI {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorMessage;
    // current user in the application
    static User myUser;

    public LoginGUI() {
        frame = new JFrame("Authentication");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("src/main/resources/images/IMAGE2.jpg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        panel.setLayout(null);
        frame.setContentPane(panel);

        // To have centred credentials
        int fieldWidth = 165;
        int fieldHeight = 25;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (screenSize.getWidth() / 2 - fieldWidth / 2);
        int y = (int) (screenSize.getHeight() / 2 - 3 * fieldHeight / 2);

        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setBounds(x - 90, y, 80, 25);
        frame.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(x, y, fieldWidth, fieldHeight);
        frame.add(emailField);

        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setBounds(x - 90, y + 35, fieldWidth, fieldHeight);
        frame.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(x, y + 35, fieldWidth, fieldHeight);
        frame.add(passwordField);

        loginButton = new JButton("Login: ");
        loginButton.setBounds(x, y + 70, 120, 25);
        frame.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myUser = performLogin();
            }
        });

        errorMessage = new JLabel();
        errorMessage.setBounds(x, y + 105, fieldWidth, fieldHeight);
        frame.add(errorMessage);

        frame.setVisible(true);
    }
    private User performLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (IMDB.getInstance().verifyPassword(email, password) == false) {
            setErrorMessage("Email or password wrong");
            return null;
        } else {
            setErrorMessage("");
            // The login was made, jump to main page
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MainPage(frame);
                }
            });
            return IMDB.getInstance().findUserByEmail(email);
        }
    }
    private void setErrorMessage(String message) {
        errorMessage.setText(message);
    }
}
