package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.util.Arrays;

@SuppressWarnings("serial")
public class NewUserWindow extends JFrame {
	private Client c;
	private JLabel windowHeader, username, email, password, confirmPassword,
			djLabel;
	private JTextField usernameField, emailField;
	private JPasswordField passwordField, confirmPasswordField;
	private JButton createUserButton, cancelButton;
	private JPanel headerPanel, usernamePanel, emailPanel, passwordPanel,
			confirmPasswordPanel, buttonPanel, logoPanel;
	private ImageIcon djIcon;

	public NewUserWindow(Client c) {
		this.c = c;
		initializeComponents();
		createGUI();
		setEventHandlers();
	}

	public void initializeComponents() {
		windowHeader = new JLabel("Enter your user info:");
		windowHeader.setFont(windowHeader.getFont().deriveFont(42f));
		djLabel = new JLabel();
		djIcon = new ImageIcon("images/DJ_Picture.png");
		djLabel.setIcon(djIcon);
		username = new JLabel("Username: ");
		username.setFont(username.getFont().deriveFont(30f));
		email = new JLabel("Email: ");
		email.setFont(email.getFont().deriveFont(30f));
		password = new JLabel("Password: ");
		password.setFont(password.getFont().deriveFont(30f));
		confirmPassword = new JLabel("Confirm Password");
		confirmPassword.setFont(confirmPassword.getFont().deriveFont(30f));
		usernameField = new JTextField(50);
		emailField = new JTextField(50);
		passwordField = new JPasswordField(50);

		confirmPasswordField = new JPasswordField(50);
		createUserButton = new JButton("Create User");
		createUserButton.setBackground(Color.ORANGE);
		createUserButton.setFont(createUserButton.getFont().deriveFont(30f));
		cancelButton = new JButton("Cancel");
		cancelButton.setBackground(Color.ORANGE);
		cancelButton.setFont(cancelButton.getFont().deriveFont(30f));
		logoPanel = new JPanel();
		logoPanel.setBackground(Color.RED);
		headerPanel = new JPanel();
		headerPanel.setBackground(Color.RED);
		usernamePanel = new JPanel();
		usernamePanel.setBackground(Color.RED);
		emailPanel = new JPanel();
		emailPanel.setBackground(Color.RED);
		passwordPanel = new JPanel();
		passwordPanel.setBackground(Color.RED);
		confirmPasswordPanel = new JPanel();
		confirmPasswordPanel.setBackground(Color.RED);
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.RED);

	}

	public void createGUI() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		logoPanel.add(djLabel);
		headerPanel.add(windowHeader);
		usernamePanel.add(username);
		usernamePanel.add(usernameField);
		emailPanel.add(email);
		emailPanel.add(emailField);
		passwordPanel.add(password);
		passwordPanel.add(passwordField);
		confirmPasswordPanel.add(confirmPassword);
		confirmPasswordPanel.add(confirmPasswordField);
		buttonPanel.add(createUserButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(cancelButton);

		add(logoPanel);
		add(headerPanel);
		add(usernamePanel);
		add(emailPanel);
		add(passwordPanel);
		add(confirmPasswordPanel);
		add(buttonPanel);
	}

	public void setEventHandlers() {
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new LoginWindow(c).setVisible(true);
				closeWindow();
			}
		});

		createUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				createUser();
			}
		});
	}

	public void createUser() {
		if (usernameField.getText().equals("")
				|| emailField.getText().equals("")
				|| passwordField.getPassword().equals("")
				|| confirmPasswordField.getPassword().equals("")) {
			JOptionPane errorMessage = new JOptionPane();
			JOptionPane
					.showMessageDialog(errorMessage,
							"One or more fields were not entered. Please fill out all text fields.");
		} else if (Arrays.equals(passwordField.getPassword(),
				confirmPasswordField.getPassword()) == false) {
			JOptionPane errorMessage = new JOptionPane();
			JOptionPane.showMessageDialog(errorMessage,
					"The two passwords did not match.");
		} else {
			@SuppressWarnings("deprecation")
			String newUserResult = c.createNewUser(emailField.getText(),
					usernameField.getText(), passwordField.getText());

			if (newUserResult.equals("username exists")) {
				JOptionPane errorMessage = new JOptionPane();
				JOptionPane.showMessageDialog(errorMessage,
						"The username has already been used by another user.");
				return;
			} else if (newUserResult.equals("email exists")) {
				JOptionPane errorMessage = new JOptionPane();
				JOptionPane.showMessageDialog(errorMessage,
						"The email has already been used by another user.");
				return;
			} else if (newUserResult.equals("database error")) {
				JOptionPane errorMessage = new JOptionPane();
				JOptionPane.showMessageDialog(errorMessage,
						"The database is down. Please try again later.");
				return;
			}

			new HomeWindow(c).setVisible(true);
			closeWindow();
		}
	}

	private void closeWindow() {
		setVisible(false);
		dispose();
	}
}
