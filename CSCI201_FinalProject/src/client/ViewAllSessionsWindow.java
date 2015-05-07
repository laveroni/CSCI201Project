package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ViewAllSessionsWindow extends JFrame implements Runnable {

	private Client c;
	private Vector<String> passwords;
	private ImageIcon djIcon;
	private JLabel djLabel, currentSessionsLabel;
	private JTable sessionsTable;
	private JButton joinButton, leaveButton;
	private JPanel djPanel, headerPanel, tablePanel, buttonPanel;
	private String[] columnNames = { "Privacy", "Session Name", "Genre",
			"# People" };
	private JScrollPane jsp;

	public ViewAllSessionsWindow(Client c) {
		this.c = c;
		passwords = new Vector<String>();
		initializeComponents();
		createGUI();
		setEventHandlers();
	}

	public void initializeComponents() {
		djIcon = new ImageIcon("images/DJ_Picture.png");
		djLabel = new JLabel(djIcon);
		currentSessionsLabel = new JLabel("All Current Sessions");
		currentSessionsLabel.setFont(currentSessionsLabel.getFont().deriveFont(
				42f));

		DefaultTableModel model = new DefaultTableModel(getSessionData(),
				columnNames);
		sessionsTable = new JTable() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		sessionsTable.setModel(model);
		sessionsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jsp = new JScrollPane(sessionsTable);

		new Thread() {
			public void run() {
				model.fireTableDataChanged();
			}
		};

		joinButton = new JButton("Join Selected Session");
		joinButton.setFont(joinButton.getFont().deriveFont(30f));
		joinButton.setBackground(Color.ORANGE);
		leaveButton = new JButton("Return to Home Window");
		leaveButton.setFont(leaveButton.getFont().deriveFont(30f));
		leaveButton.setBackground(Color.ORANGE);

		djPanel = new JPanel();
		djPanel.setBackground(Color.RED);
		headerPanel = new JPanel();
		headerPanel.setBackground(Color.RED);
		tablePanel = new JPanel();
		tablePanel.setBackground(Color.RED);
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.RED);
	}

	public void createGUI() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		djPanel.add(djLabel);
		headerPanel.add(currentSessionsLabel);
		tablePanel.add(jsp);
		buttonPanel.add(joinButton);
		buttonPanel.add(leaveButton);

		add(djPanel);
		add(headerPanel);
		add(tablePanel);
		add(buttonPanel);
	}

	public void setEventHandlers() {
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int sessionIndex = sessionsTable.getSelectedRow();

				if (sessionIndex == -1) {
					JOptionPane errorMessage = new JOptionPane();
					JOptionPane.showMessageDialog(errorMessage,
							"Please select a session.");
					return;
				}

				if (c.isPrivate(sessionIndex)) {
					JPanel panel = new JPanel();
					JLabel label = new JLabel("Enter the password:");
					JPasswordField pass = new JPasswordField(10);
					panel.add(label);
					panel.add(pass);
					String[] options = new String[] { "OK", "Cancel" };
					int option = JOptionPane.showOptionDialog(null, panel, "",
							JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
							null, options, options[1]);
					if (option == 0) {
						String password = new String(pass.getPassword());
						if (passwords.elementAt(sessionIndex).equals(password)) {
							CurrentSessionWindow csw = new CurrentSessionWindow(
									c, sessionIndex);
							csw.setVisible(true);
							Thread sessionThread = new Thread(csw);
							sessionThread.start();
							closeWindow();
						}
					}
				} else {
					CurrentSessionWindow csw = new CurrentSessionWindow(c,
							sessionIndex);
					csw.setVisible(true);
					Thread sessionThread = new Thread(csw);
					sessionThread.start();
					closeWindow();
				}
			}
		});
		leaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new HomeWindow(c).setVisible(true);
				closeWindow();
			}
		});
	}

	public String[][] getSessionData() {
		String[] sessionArray = c.getAllSessions().split("\\?");
		String[][] result = new String[sessionArray.length][4];

		if (sessionArray[0].equals("")) {
			return result;
		}

		for (int i = 0; i < sessionArray.length; i++) {
			String[] tempArray = sessionArray[i].split("\\*");

			result[i][0] = tempArray[0];
			result[i][1] = tempArray[1];
			result[i][2] = tempArray[2];
			result[i][3] = tempArray[3];

			if (tempArray.length == 5) {
				passwords.addElement(tempArray[4]);
			}
		}

		return result;
	}

	private void closeWindow() {
		setVisible(false);
		dispose();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ie) {

			}

			DefaultTableModel model = new DefaultTableModel(getSessionData(),
					columnNames);
			sessionsTable.setModel(model);

		}
	}
}
