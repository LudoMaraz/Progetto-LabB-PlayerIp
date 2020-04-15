import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphicManager {


    public void startGraphic (JFrame win, Container c) {
        JPanel panel = new JPanel();
        c.add(panel);
        JButton login = new JButton("LOGIN");
        JButton registrazione = new JButton("REGISTAZIONE");
        login.setBackground(Color.yellow);
        login.setSize(250, 250);
        registrazione.setBackground(Color.cyan);
        registrazione.setBorderPainted(true);
        registrazione.setSize(250,250);
        panel.add(login);
        panel.add(registrazione);
        win.setSize(250, 2000);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setVisible(true);
    }

    public void loginGraphic (JFrame win, Container c) {
        JPanel loginPanel = new JPanel();
        c.add(loginPanel);
        JLabel usernameLabel = new JLabel();
        usernameLabel.setText("Username");
        JTextField username_text = new JTextField(25);
        loginPanel.add(usernameLabel);
        loginPanel.add(username_text);
        JLabel passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        JTextField passwordText = new JTextField(25);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordText);
        JButton conferma = new JButton("conferma");
        loginPanel.add(conferma);
    }
}
