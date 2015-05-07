package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CreateSessionWindow extends JFrame {
	private Client c;
	private JLabel createSessionLabel, sessionNameLabel, genresLabel,
			sessionPrivacyLabel, passwordLabel, djLabel;
	private ImageIcon djIcon;
	private JTextField sessionField, passwordField;
	private JComboBox<String> genreBox;
	private JRadioButton publicButton, privateButton;
	private ButtonGroup privacyGroup;
	private JButton createSessionButton, cancelButton;
	private JPanel logoPanel, headerPanel, sessionPanel, genrePanel,
			privacyPanel, passwordPanel, buttonPanel;

	CreateSessionWindow(Client c) {
		this.c = c;
		initializeComponents();
		createGUI();
		setEventHandlers();
	}

	public void initializeComponents() {
		djIcon = new ImageIcon("images/DJ_Picture.png");
		djLabel = new JLabel(djIcon);
		createSessionLabel = new JLabel("Create a New Session");
		createSessionLabel
				.setFont(createSessionLabel.getFont().deriveFont(42f));
		sessionNameLabel = new JLabel("Session Name ");
		sessionNameLabel.setFont(sessionNameLabel.getFont().deriveFont(30f));
		genresLabel = new JLabel("Genres");
		genresLabel.setFont(genresLabel.getFont().deriveFont(30f));
		sessionPrivacyLabel = new JLabel("Session Privacy ");
		sessionPrivacyLabel.setFont(sessionPrivacyLabel.getFont().deriveFont(
				30f));
		passwordLabel = new JLabel("Password");
		passwordLabel.setFont(passwordLabel.getFont().deriveFont(30f));
		sessionField = new JTextField(50);
		passwordField = new JTextField(50);
		String genres[] = { "Pop", "Rock", "Rap", "Country", "Hip/Hop", "RnB" };
		genreBox = new JComboBox<String>(genres);
		publicButton = new JRadioButton("Public");
		publicButton.setSelected(true);
		privateButton = new JRadioButton("Private");
		privacyGroup = new ButtonGroup();
		createSessionButton = new JButton("Create Session");
		createSessionButton.setFont(createSessionButton.getFont().deriveFont(
				30f));
		createSessionButton.setBackground(Color.ORANGE);
		cancelButton = new JButton("Cancel");
		cancelButton.setBackground(Color.ORANGE);
		cancelButton.setFont(cancelButton.getFont().deriveFont(30f));
		logoPanel = new JPanel();
		logoPanel.setBackground(Color.RED);
		headerPanel = new JPanel();
		headerPanel.setBackground(Color.RED);
		sessionPanel = new JPanel();
		sessionPanel.setBackground(Color.RED);
		genrePanel = new JPanel();
		genrePanel.setBackground(Color.RED);
		privacyPanel = new JPanel();
		privacyPanel.setBackground(Color.RED);
		passwordPanel = new JPanel();
		passwordPanel.setBackground(Color.RED);
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.RED);
	}

	public void createGUI() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		logoPanel.add(djLabel);
		headerPanel.add(createSessionLabel);
		sessionPanel.add(sessionNameLabel);
		sessionPanel.add(sessionField);
		genrePanel.add(genresLabel);
		genrePanel.add(genreBox);
		privacyPanel.add(sessionPrivacyLabel);
		privacyGroup.add(privateButton);
		privacyGroup.add(publicButton);
		privacyPanel.add(privateButton);
		privacyPanel.add(publicButton);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);
		buttonPanel.add(createSessionButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(cancelButton);

		add(logoPanel);
		add(headerPanel);
		add(sessionPanel);
		add(genrePanel);
		add(privacyPanel);
		add(passwordPanel);
		add(buttonPanel);
	}

	public void setEventHandlers() {
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new HomeWindow(c).setVisible(true);
				closeWindow();
			}
		});

		createSessionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (sessionField.getText().equals("")) {
					JOptionPane errorMessage = new JOptionPane();
					JOptionPane.showMessageDialog(errorMessage,
							"Please enter the name of your session.");
				} else if (privateButton.isSelected()
						&& passwordField.getText().equals("")) {
					JOptionPane errorMessage = new JOptionPane();
					JOptionPane
							.showMessageDialog(errorMessage,
									"You must enter a password in order to create a private session");
				} else {
					String password = "";
					if (privateButton.isSelected()) {
						password = passwordField.getText();
					}

					c.createNewSession(sessionField.getText(),
							(String) genreBox.getSelectedItem(),
							privateButton.isSelected(), password);

					CurrentSessionWindow csw = new CurrentSessionWindow(c, -1);
					csw.setVisible(true);
					Thread sessionThread = new Thread(csw);
					sessionThread.start();
					closeWindow();
				}

			}
		});
	}

	private void closeWindow() {
		setVisible(false);
		dispose();
	}

}
