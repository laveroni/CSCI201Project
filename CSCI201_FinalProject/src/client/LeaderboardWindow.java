package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LeaderboardWindow extends JFrame {

	private Client c;
	private ImageIcon djIcon;
	private JLabel djLabel, topDJLabel, rankOne, rankTwo, rankThree, rankFour,
			rankFive, name1, name2, name3, name4, name5, score1, score2,
			score3, score4, score5, yourScoreLabel;
	private JPanel djPanel, headerPanel, rankPanel, yourScorePanel,
			buttonPanel, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12,
			p13, p14, p15;
	private JButton leaveRoomButton;
	private JLabel[] names, scores;

	public LeaderboardWindow(Client c) {
		this.c = c;
		initializeComponents();
		createGUI();
		setEventHandlers();
	}

	public void initializeComponents() {
		djIcon = new ImageIcon("images/DJ_Picture.png");
		djLabel = new JLabel(djIcon);
		topDJLabel = new JLabel("Top DJs");
		topDJLabel.setFont(topDJLabel.getFont().deriveFont(42f));
		rankOne = new JLabel("1. ");
		rankOne.setFont(rankOne.getFont().deriveFont(30f));
		rankTwo = new JLabel("2. ");
		rankTwo.setFont(rankTwo.getFont().deriveFont(30f));
		rankThree = new JLabel("3. ");
		rankThree.setFont(rankThree.getFont().deriveFont(30f));
		rankFour = new JLabel("4. ");
		rankFour.setFont(rankFour.getFont().deriveFont(30f));
		rankFive = new JLabel("5. ");
		rankFive.setFont(rankFive.getFont().deriveFont(30f));
		name1 = new JLabel();
		name1.setFont(name1.getFont().deriveFont(30f));
		name2 = new JLabel();
		name2.setFont(name2.getFont().deriveFont(30f));
		name3 = new JLabel();
		name3.setFont(name3.getFont().deriveFont(30f));
		name4 = new JLabel();
		name4.setFont(name4.getFont().deriveFont(30f));
		name5 = new JLabel();
		name5.setFont(name5.getFont().deriveFont(30f));
		names = new JLabel[5];
		names[0] = name1;
		names[1] = name2;
		names[2] = name3;
		names[3] = name4;
		names[4] = name5;
		score1 = new JLabel();
		score1.setFont(score1.getFont().deriveFont(30f));
		score2 = new JLabel();
		score2.setFont(score2.getFont().deriveFont(30f));
		score3 = new JLabel();
		score3.setFont(score3.getFont().deriveFont(30f));
		score4 = new JLabel();
		score4.setFont(score4.getFont().deriveFont(30f));
		score5 = new JLabel();
		score5.setFont(score5.getFont().deriveFont(30f));
		scores = new JLabel[5];
		scores[0] = score1;
		scores[1] = score2;
		scores[2] = score3;
		scores[3] = score4;
		scores[4] = score5;
		yourScoreLabel = new JLabel();
		yourScoreLabel.setFont(yourScoreLabel.getFont().deriveFont(30f));
		yourScoreLabel.setText("Your Score: " + c.getUserScore());

		leaveRoomButton = new JButton("Leave Window");
		leaveRoomButton.setFont(leaveRoomButton.getFont().deriveFont(30f));
		leaveRoomButton.setBackground(Color.ORANGE);

		djPanel = new JPanel();
		djPanel.setBackground(Color.RED);
		headerPanel = new JPanel();
		headerPanel.setBackground(Color.RED);
		rankPanel = new JPanel();
		rankPanel.setBackground(Color.RED);
		yourScorePanel = new JPanel();
		yourScorePanel.setBackground(Color.RED);
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.RED);

		p1 = new JPanel();
		p1.setBackground(Color.RED);
		p2 = new JPanel();
		p2.setBackground(Color.RED);
		p3 = new JPanel();
		p3.setBackground(Color.RED);
		p4 = new JPanel();
		p4.setBackground(Color.RED);
		p5 = new JPanel();
		p5.setBackground(Color.RED);
		p6 = new JPanel();
		p6.setBackground(Color.RED);
		p7 = new JPanel();
		p7.setBackground(Color.RED);
		p8 = new JPanel();
		p8.setBackground(Color.RED);
		p9 = new JPanel();
		p9.setBackground(Color.RED);
		p10 = new JPanel();
		p10.setBackground(Color.RED);
		p11 = new JPanel();
		p11.setBackground(Color.RED);
		p12 = new JPanel();
		p12.setBackground(Color.RED);
		p13 = new JPanel();
		p13.setBackground(Color.RED);
		p14 = new JPanel();
		p14.setBackground(Color.RED);
		p15 = new JPanel();
		p15.setBackground(Color.RED);

	}

	public void createGUI() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		djPanel.add(djLabel);
		headerPanel.add(topDJLabel);
		rankPanel.setLayout(new GridLayout(5, 3));
		p1.add(rankOne);
		rankPanel.add(p1);
		p2.add(name1);
		rankPanel.add(p2);
		p3.add(score1);
		rankPanel.add(p3);
		p4.add(rankTwo);
		rankPanel.add(p4);
		p5.add(name2);
		rankPanel.add(p5);
		p6.add(score2);
		rankPanel.add(p6);
		p7.add(rankThree);
		rankPanel.add(p7);
		p8.add(name3);
		rankPanel.add(p8);
		p9.add(score3);
		rankPanel.add(p9);
		p10.add(rankFour);
		rankPanel.add(p10);
		p11.add(name4);
		rankPanel.add(p11);
		p12.add(score4);
		rankPanel.add(p12);
		p13.add(rankFive);
		rankPanel.add(p13);
		p14.add(name5);
		rankPanel.add(p14);
		p15.add(score5);
		rankPanel.add(p15);
		yourScorePanel.add(yourScoreLabel);
		buttonPanel.add(leaveRoomButton);

		add(djPanel);
		add(headerPanel);
		add(rankPanel);
		add(yourScorePanel);
		add(buttonPanel);

		getLeaderboardInfo();
	}

	public void setEventHandlers() {
		leaveRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new HomeWindow(c).setVisible(true);
				closeWindow();
			}
		});
	}

	public void closeWindow() {
		setVisible(false);
		dispose();
	}

	public void getLeaderboardInfo() {
		Vector<String> leaderboardNames = new Vector<String>();
		Vector<String> leaderboardScores = new Vector<String>();

		String[] dataArray = c.getLeaderboard().split("\\?");

		for (int i = 0; i < dataArray.length; i++) {
			String[] temp = dataArray[i].split("\\*");
			leaderboardNames.addElement(temp[0]);
			leaderboardScores.addElement(temp[1]);
		}

		for (int i = 0; i < names.length; i++) {
			names[i].setText(leaderboardNames.elementAt(i));
		}

		for (int i = 0; i < scores.length; i++) {
			scores[i].setText(leaderboardScores.elementAt(i));
		}
	}
}
