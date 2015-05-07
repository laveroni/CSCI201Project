package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginWindow extends JFrame {
	private Client c;
	private JLabel djLabel, usernameLabel, passwordLabel;
	private ImageIcon djIcon;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton, createNewUserButton, loginAsGuestButton;
	private JPanel DJPanel, usernamePanel, passwordPanel, buttonPanel,
			loginPanel, newUserPanel, guestPanel;

	public LoginWindow(Client c) {
		this.c = c;
		initializeComponents();
		createGUI();
		setEventHandlers();
	}

	public void initializeComponents() {
		djIcon = new ImageIcon("images/DJ_Picture.png");
		djLabel = new JLabel(djIcon);
		usernameLabel = new JLabel("Username ");
		usernameLabel.setFont(usernameLabel.getFont().deriveFont(30f));
		passwordLabel = new JLabel("Password ");
		passwordLabel.setFont(passwordLabel.getFont().deriveFont(30f));
		usernameField = new JTextField(50);
		passwordField = new JPasswordField(50);

		loginButton = new JButton("Login");
		loginButton.setFont(loginButton.getFont().deriveFont(30f));
		loginButton.setBackground(Color.ORANGE);
		createNewUserButton = new JButton("Create New User");
		createNewUserButton.setFont(createNewUserButton.getFont().deriveFont(
				30f));
		createNewUserButton.setBackground(Color.ORANGE);
		loginAsGuestButton = new JButton("Login As Guest");
		loginAsGuestButton
				.setFont(loginAsGuestButton.getFont().deriveFont(30f));
		loginAsGuestButton.setBackground(Color.ORANGE);

		DJPanel = new JPanel();
		DJPanel.setBackground(Color.RED);
		usernamePanel = new JPanel();
		usernamePanel.setBackground(Color.RED);
		passwordPanel = new JPanel();
		passwordPanel.setBackground(Color.RED);
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.RED);
		loginPanel = new JPanel();
		loginPanel.setBackground(Color.RED);
		newUserPanel = new JPanel();
		newUserPanel.setBackground(Color.RED);
		guestPanel = new JPanel();
		guestPanel.setBackground(Color.RED);
	}

	public void createGUI() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		DJPanel.add(djLabel);
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameField);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);
		loginPanel.add(loginButton);
		newUserPanel.add(createNewUserButton);
		buttonPanel.add(loginPanel);
		buttonPanel.add(newUserPanel);
		guestPanel.add(loginAsGuestButton);

		add(DJPanel);
		add(usernamePanel);
		add(passwordPanel);
		add(buttonPanel);
		add(guestPanel);
	}

	public void setEventHandlers() {
		passwordField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
					@SuppressWarnings("deprecation")
					boolean loginResult = c.loginUser(usernameField.getText(),
							passwordField.getText());

					if (loginResult) {
						HomeWindow hw = new HomeWindow(c);
						hw.setVisible(true);
						closeWindow();
					} else {
						JOptionPane errorMessage = new JOptionPane();
						JOptionPane.showMessageDialog(errorMessage,
								"That username or password is incorrect");
					}
				}
			}
		});

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				@SuppressWarnings("deprecation")
				boolean loginResult = c.loginUser(usernameField.getText(),
						passwordField.getText());

				if (loginResult) {
					HomeWindow hw = new HomeWindow(c);
					hw.setVisible(true);
					closeWindow();
				} else {
					JOptionPane errorMessage = new JOptionPane();
					JOptionPane.showMessageDialog(errorMessage,
							"That username or password is incorrect");
				}
			}
		});

		createNewUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				NewUserWindow nw = new NewUserWindow(c);
				nw.setVisible(true);
			}
		});

		loginAsGuestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				c.setAsGuest();
				HomeWindow hw = new HomeWindow(c);
				hw.setVisible(true);
				closeWindow();
			}
		});
	}

	private void closeWindow() {
		setVisible(false);
		dispose();
	}
}
