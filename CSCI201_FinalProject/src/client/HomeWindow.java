package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HomeWindow extends JFrame {
	private Client c;
	private JLabel welcomeLabel, djLabel;
	private JButton viewAllSessionsButton, createSessionButton,
			viewLeaderboardButton, logoutButton;
	private JPanel welcomePanel, viewAllSessionsPanel, createSessionPanel,
			viewLeaderboardPanel, logoutPanel, logoPanel;
	private ImageIcon djIcon;

	public HomeWindow(Client c) {
		this.c = c;
		initializeComponents();
		createGUI();
		setEventHandlers();
	}

	public void initializeComponents() {
		djIcon = new ImageIcon("images/DJ_Picture.png");
		djLabel = new JLabel(djIcon);
		welcomeLabel = new JLabel("Welcome, " + c.getUsername());
		welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(42f));
		viewAllSessionsButton = new JButton("View All Sessions");
		viewAllSessionsButton.setFont(viewAllSessionsButton.getFont()
				.deriveFont(30f));
		viewAllSessionsButton.setBackground(Color.ORANGE);
		createSessionButton = new JButton("Create a Session");
		createSessionButton.setFont(createSessionButton.getFont().deriveFont(
				30f));
		createSessionButton.setBackground(Color.ORANGE);
		viewLeaderboardButton = new JButton("View Leaderboard");
		viewLeaderboardButton.setFont(viewLeaderboardButton.getFont()
				.deriveFont(30f));
		viewLeaderboardButton.setBackground(Color.ORANGE);
		logoutButton = new JButton("Log Out");
		logoutButton.setFont(logoutButton.getFont().deriveFont(30f));
		logoutButton.setBackground(Color.ORANGE);
		logoPanel = new JPanel();
		logoPanel.setBackground(Color.RED);
		welcomePanel = new JPanel();
		welcomePanel.setBackground(Color.RED);
		viewAllSessionsPanel = new JPanel();
		viewAllSessionsPanel.setBackground(Color.RED);
		createSessionPanel = new JPanel();
		createSessionPanel.setBackground(Color.RED);
		viewLeaderboardPanel = new JPanel();
		viewLeaderboardPanel.setBackground(Color.RED);
		logoutPanel = new JPanel();
		logoutPanel.setBackground(Color.RED);
	}

	public void createGUI() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		logoPanel.add(djLabel);
		welcomePanel.add(welcomeLabel);
		viewAllSessionsPanel.add(viewAllSessionsButton);
		createSessionPanel.add(createSessionButton);
		viewLeaderboardPanel.add(viewLeaderboardButton);
		logoutPanel.add(logoutButton);

		add(logoPanel);
		add(welcomePanel);
		add(viewAllSessionsPanel);
		add(createSessionPanel);
		add(viewLeaderboardPanel);
		add(logoutPanel);
	}

	public void setEventHandlers() {
		viewAllSessionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ViewAllSessionsWindow vasw = new ViewAllSessionsWindow(c);
				vasw.setVisible(true);
				Thread t = new Thread(vasw);
				t.start();
				closeWindow();
			}
		});

		createSessionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (c.isGuest()) {
					JOptionPane errorMessage = new JOptionPane();
					JOptionPane.showMessageDialog(errorMessage,
							"Only registered users can create new sessions.");
				} else {
					new CreateSessionWindow(c).setVisible(true);
					closeWindow();
				}
			}
		});

		viewLeaderboardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new LeaderboardWindow(c).setVisible(true);
				closeWindow();
			}
		});

		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				c.logoutUser();
				if (c.isGuest()) {
					c.setAsNotGuest();
				}
				new LoginWindow(c).setVisible(true);
				closeWindow();
			}
		});
	}

	private void closeWindow() {
		setVisible(false);
		dispose();
	}
}
