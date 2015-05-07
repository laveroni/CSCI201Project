package client;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

public class DJProgram {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Client c = null;
				try {
					c = new Client();
				} catch (ConnectionException e) {
					JOptionPane errorMessage = new JOptionPane();
					JOptionPane
							.showMessageDialog(errorMessage,
									"Connection could not be made with the server. Please try again.");
					System.exit(0);
				}
				Thread t = new Thread(c);
				t.start();

				LoginWindow lw = new LoginWindow(c);
				lw.setVisible(true);
			}
		});
	}
}