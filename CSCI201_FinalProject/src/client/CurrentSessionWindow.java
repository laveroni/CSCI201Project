package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

@SuppressWarnings("serial")
public class CurrentSessionWindow extends JFrame implements Runnable {
	private Client c;
	private Player p;
	private BufferedInputStream bis;
	private BufferedOutputStream bos;
	private JLabel djLabel, headerLabel, populationLabel, currentDJLabel,
			currentSongLabel, likesLabel, dislikesLabel;
	private ImageIcon djIcon, thumbsUp, thumbsDown;
	private JButton leaveRoomButton, sendButton, thumbsUpButton,
			thumbsDownButton, playSongButton, uploadSongButton;
	private JTextArea chatArea;
	private JScrollPane jsp;
	private JTextField chatMessageField;
	private JList<String> songList;
	private JPanel mainPanel, leftPanel, rightPanel, djPanel, headerPanel,
			infoPanel, votesPanel, songPanel, buttonPanel, chatPanel, chatBox,
			chatMessages, populationPanel, currentDJPanel, currentSongPanel;
	private boolean canVote;
	private boolean thread;

	public CurrentSessionWindow(Client c, int index) {
		this.c = c;
		p = null;
		canVote = false;
		thread = true;

		if (index != -1) {
			c.joinSession(index);
		}

		initializeComponents();
		createGUI();
		setEventHandlers();
	}

	public void initializeComponents() {
		djIcon = new ImageIcon("images/DJ_Picture.png");
		thumbsUp = new ImageIcon("images/ThumbsUp.png");
		thumbsDown = new ImageIcon("images/ThumbsDown.png");

		djLabel = new JLabel(djIcon);
		headerLabel = new JLabel(c.getName());
		headerLabel.setFont(headerLabel.getFont().deriveFont(42f));
		populationLabel = new JLabel("Population: " + c.getRoomSize());
		populationLabel.setFont(populationLabel.getFont().deriveFont(20f));

		if (c.isDJ()) {
			currentDJLabel = new JLabel("DJ: " + c.getUsername());
		} else {
			currentDJLabel = new JLabel("DJ: " + c.getDJ());
		}
		currentDJLabel.setFont(currentDJLabel.getFont().deriveFont(20f));
		currentSongLabel = new JLabel("Current Song: ");
		currentSongLabel.setFont(currentSongLabel.getFont().deriveFont(20f));
		likesLabel = new JLabel(": " + c.getLikes());
		likesLabel.setFont(likesLabel.getFont().deriveFont(30f));
		dislikesLabel = new JLabel(": " + c.getDislikes());
		dislikesLabel.setFont(dislikesLabel.getFont().deriveFont(30f));
		thumbsUpButton = new JButton(thumbsUp);
		thumbsDownButton = new JButton(thumbsDown);
		leaveRoomButton = new JButton("Leave Room");
		leaveRoomButton.setFont(leaveRoomButton.getFont().deriveFont(20f));
		sendButton = new JButton("Send");
		sendButton.setFont(sendButton.getFont().deriveFont(20f));
		playSongButton = new JButton("Play Selected Song");
		uploadSongButton = new JButton("Upload a song");

		chatArea = new JTextArea();
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret) chatArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		jsp = new JScrollPane(chatArea);
		jsp.setPreferredSize(new Dimension(200, 300));
		chatMessageField = new JTextField(50);

		DefaultListModel<String> data = new DefaultListModel<String>();
		String[] songArray = c.getSongList().split("\\?");
		for (int i = 0; i < songArray.length; i++) {
			data.addElement(songArray[i]);
		}

		songList = new JList<String>(data);
		songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		songList.setLayoutOrientation(JList.VERTICAL);
		songList.setVisibleRowCount(5);

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.RED);
		leftPanel = new JPanel();
		leftPanel.setBackground(Color.RED);
		rightPanel = new JPanel();
		rightPanel.setBackground(Color.RED);
		djPanel = new JPanel();
		djPanel.setBackground(Color.RED);
		headerPanel = new JPanel();
		headerPanel.setBackground(Color.RED);
		infoPanel = new JPanel();
		infoPanel.setBackground(Color.RED);
		votesPanel = new JPanel();
		votesPanel.setBackground(Color.RED);
		songPanel = new JPanel();
		songPanel.setBackground(Color.RED);
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.RED);
		chatPanel = new JPanel();
		chatPanel.setBackground(Color.RED);
		chatBox = new JPanel();
		chatBox.setBackground(Color.RED);
		chatMessages = new JPanel();
		chatMessages.setBackground(Color.RED);
		populationPanel = new JPanel();
		populationPanel.setBackground(Color.RED);
		currentDJPanel = new JPanel();
		currentDJPanel.setBackground(Color.RED);
		currentSongPanel = new JPanel();
		currentSongPanel.setBackground(Color.RED);
	}

	public void createGUI() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		djPanel.add(djLabel);
		headerPanel.add(headerLabel);
		populationPanel.add(populationLabel);
		currentDJPanel.add(currentDJLabel);
		currentSongPanel.add(currentSongLabel);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(currentDJPanel);
		infoPanel.add(currentSongPanel);
		votesPanel.add(thumbsUpButton);
		votesPanel.add(likesLabel);
		votesPanel.add(thumbsDownButton);
		votesPanel.add(dislikesLabel);
		songPanel.add(songList);
		songPanel.add(playSongButton);
		songPanel.add(uploadSongButton);
		songList.setPreferredSize(new Dimension(screenSize.width / 3, 100));
		playSongButton.setPreferredSize(new Dimension(
				screenSize.width / 4 - 23, 20));

		buttonPanel.add(leaveRoomButton);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatBox.add(chatMessageField);
		chatBox.add(sendButton);
		chatPanel.add(jsp);
		chatPanel.add(chatBox);

		leftPanel.setPreferredSize(new Dimension(screenSize.width / 2,
				screenSize.height - 400));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(headerPanel);
		leftPanel.add(populationPanel);
		leftPanel.add(infoPanel);
		if (c.isDJ()) {
			leftPanel.add(songPanel);
		} else {
			leftPanel.add(votesPanel);
		}
		leftPanel.add(buttonPanel);

		rightPanel.setPreferredSize(new Dimension(screenSize.width / 2,
				screenSize.height - 400));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(chatPanel);

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);

		add(djPanel);
		add(mainPanel);
	}

	public void setEventHandlers() {
		thumbsUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (c.isGuest()) {
					JOptionPane errorMessage = new JOptionPane();
					JOptionPane.showMessageDialog(errorMessage,
							"Only registered users can vote.");
				} else {
					if (canVote) {
						c.like();
						canVote = false;
					}
				}
			}
		});

		thumbsDownButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (c.isGuest()) {
					JOptionPane errorMessage = new JOptionPane();
					JOptionPane.showMessageDialog(errorMessage,
							"Only registered users can vote.");
				} else {
					if (canVote) {
						c.dislike();
						canVote = false;
					}
				}
			}
		});

		playSongButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (p != null) {
					if (!p.isComplete()) {
						canVote = true;
						p.close();
					}
				}
				c.playSong(songList.getSelectedValue());
			}
		});

		uploadSongButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"MP3 File", "mp3");
				fc.setFileFilter(filter);

				int returnVal = fc.showOpenDialog(CurrentSessionWindow.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					c.uploadSong();
					File song = fc.getSelectedFile();
					try {
						c.openUploadSongSocket();
						bis = new BufferedInputStream(new FileInputStream(song));
						bos = new BufferedOutputStream(c.getUploadSongSocket()
								.getOutputStream());
						byte[] b = new byte[(int) song.length()];
						bis.read(b, 0, b.length);
						bos.write(b, 0, b.length);
						bos.flush();
						bis.close();
						bos.close();

						if (p != null) {
							p.close();
						}
					} catch (FileNotFoundException fnfe) {
						System.out.println("FNFE: " + fnfe.getMessage());
					} catch (IOException ioe) {
						System.out.println("IOE: " + ioe.getMessage());
					}
				}

			}
		});

		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!chatMessageField.getText().equals("")) {
					c.sendMessage(chatMessageField.getText());
				}
			}
		});

		chatMessageField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_ENTER
						&& !chatMessageField.getText().equals("")) {
					c.sendMessage(chatMessageField.getText());
				}
			}
		});

		leaveRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (p != null) {
					p.close();
				}

				if (c.isDJ()) {
					stopThread();
					c.closeSession();
				} else {
					stopThread();
					c.leaveSession();
				}
			}
		});
	}

	public void playSong() {
		try {
			FileInputStream fis = new FileInputStream(c.getSong());
			p = new Player(fis);
			p.play();
		} catch (FileNotFoundException fnfe) {
			System.out.println("FNFE: " + fnfe.getMessage());
		} catch (JavaLayerException jle) {
			System.out.println("JLE: " + jle.getMessage());
		}
	}

	private void closeWindow() {
		setVisible(false);
		dispose();
	}

	@Override
	public void run() {
		while (thread) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException ie) {
				System.out.println("IE: " + ie.getMessage());
			}

			if (!c.isSessionClosed()) {
				if (!c.getMessage().equals("")) {
					chatArea.append(c.getMessage() + "\n");
					chatMessageField.setText("");
					c.resetMessage();
				}

				if (c.isReadyToPlay()) {
					c.setNotReadyToPlay();
					canVote = true;
					new Thread() {
						public void run() {
							playSong();
						}
					}.start();
				}

				if (p != null) {
					if (p.isComplete()) {
						p.close();
					}
				}
				if (c.updateSession()) {
					likesLabel.setText(": " + c.getLikes());
					dislikesLabel.setText(": " + c.getDislikes());
					currentSongLabel.setText("Current Song: "
							+ c.getCurrentSong());
					populationLabel.setText("Population: " + c.getRoomSize());
				}
				c.setUpdateSessionFalse();
			} else {
				if (p != null) {
					p.close();
				}
				c.resetSessionClosed();
				new HomeWindow(c).setVisible(true);
				closeWindow();
				stopThread();
			}
		}
	}

	public void stopThread() {
		thread = false;
	}
}
