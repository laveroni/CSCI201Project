package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client implements Runnable {

	private PrintWriter pw;
	private BufferedReader br;
	private Socket s1, s2, s3;

	private int sessionIndex;
	private boolean sessionClosed;
	private String song;
	private boolean readyToPlay;
	private String message;
	private boolean updateSession;
	private String currentSong;

	private boolean loginUserResponse;
	private String createNewUserResponse;
	private boolean isGuestResponse;
	private String getUsernameResponse;
	private int getScoreResponse;
	private String createNewSessionResponse;
	private String getDJResponse;
	private String getNameResponse;
	private String getGenreResponse;
	private boolean isPrivateResponse;
	private String getPasswordResponse;
	private int getRoomSizeResponse;
	private String getUserListResponse;
	private int getLikesResponse;
	private int getDislikesResponse;
	private String getSongListResponse;
	private boolean isDJResponse;
	private String getLeaderboardResponse;
	private String getAllSessionsResponse;

	public Client() throws ConnectionException {
		sessionIndex = -1;
		sessionClosed = false;
		song = "song.mp3";
		readyToPlay = false;
		message = "";
		updateSession = false;
		currentSong = "";

		loginUserResponse = false;
		createNewUserResponse = "";
		isGuestResponse = false;
		getUsernameResponse = "";
		getScoreResponse = 0;
		createNewSessionResponse = "";
		getDJResponse = "";
		getNameResponse = "";
		getGenreResponse = "";
		isPrivateResponse = false;
		getPasswordResponse = "";
		getRoomSizeResponse = 0;
		getUserListResponse = "";
		getLikesResponse = 0;
		getDislikesResponse = 0;
		getSongListResponse = "";
		isDJResponse = false;
		getLeaderboardResponse = "";
		getAllSessionsResponse = "";

		try {
			s1 = new Socket("localhost", 6789);
			br = new BufferedReader(new InputStreamReader(s1.getInputStream()));
			pw = new PrintWriter(s1.getOutputStream());
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
			throw new ConnectionException();
		}
	}

	public void openUploadSongSocket() {
		try {
			s3 = new Socket("localhost", 5690);
		} catch (UnknownHostException uhe) {
			System.out.println("UHE: " + uhe.getMessage());
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
		}
	}

	public Socket getUploadSongSocket() {
		return s3;
	}

	public void resetSessionClosed() {
		sessionClosed = false;
	}

	public boolean isSessionClosed() {
		return sessionClosed;
	}

	public String getMessage() {
		return message;
	}

	public void resetMessage() {
		message = "";
	}

	public String getSong() {
		return song;
	}

	public void setReadyToPlay() {
		readyToPlay = true;
	}

	public void setNotReadyToPlay() {
		readyToPlay = false;
	}

	public boolean isReadyToPlay() {
		return readyToPlay;
	}

	public void setUpdateSessionTrue() {
		updateSession = true;
	}

	public void setUpdateSessionFalse() {
		updateSession = false;
	}

	public boolean updateSession() {
		return updateSession;
	}

	public String getCurrentSong() {
		return currentSong;
	}

	/*
	 * SERVER COMMANDS
	 */

	public void sendCommand(String command) {
		pw.println(command);
		pw.flush();
	}

	public synchronized boolean loginUser(String username, String password) {
		sendCommand("1 " + username + " " + password);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return loginUserResponse;
	}

	public void logoutUser() {
		sendCommand("2");
	}

	public synchronized String createNewUser(String email, String username,
			String password) {
		sendCommand("3 " + email + " " + username + " " + password);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return createNewUserResponse;
	}

	public void setAsGuest() {
		sendCommand("4");
	}

	public void setAsNotGuest() {
		sendCommand("5");
	}

	public synchronized boolean isGuest() {
		sendCommand("6");

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return isGuestResponse;
	}

	public synchronized String getUsername() {
		sendCommand("7");

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getUsernameResponse;
	}

	public synchronized int getUserScore() {
		sendCommand("8");

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getScoreResponse;
	}

	public synchronized void createNewSession(String name, String genre,
			boolean privacy, String password) {
		sendCommand("A " + name + " " + genre + " " + privacy + " " + password);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		sessionIndex = Integer.parseInt(createNewSessionResponse);
	}

	public void joinSession(int index) {
		sessionIndex = index;
		sendCommand("B " + sessionIndex);
	}

	public void leaveSession() {
		sendCommand("C " + sessionIndex);
	}

	public synchronized String getDJ() {
		sendCommand("D " + sessionIndex);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getDJResponse;
	}

	public synchronized String getName() {
		sendCommand("E " + sessionIndex);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getNameResponse;
	}

	public synchronized String getGenre() {
		sendCommand("F " + sessionIndex);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getGenreResponse;
	}

	public synchronized boolean isPrivate(int index) {
		sendCommand("G " + index);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return isPrivateResponse;
	}

	public synchronized String getPassword() {
		sendCommand("H " + sessionIndex);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getPasswordResponse;
	}

	public synchronized int getRoomSize() {
		sendCommand("I " + sessionIndex);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getRoomSizeResponse;
	}

	public synchronized String getUserList() {
		sendCommand("J " + sessionIndex);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getUserListResponse;
	}

	public void like() {
		sendCommand("K " + sessionIndex);
	}

	public void dislike() {
		sendCommand("L " + sessionIndex);
	}

	public synchronized int getLikes() {
		sendCommand("M " + sessionIndex);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getLikesResponse;
	}

	public synchronized int getDislikes() {
		sendCommand("N " + sessionIndex);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getDislikesResponse;
	}

	public synchronized String getSongList() {
		sendCommand("O");

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getSongListResponse;
	}

	public synchronized boolean isDJ() {
		sendCommand("P " + sessionIndex);

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return isDJResponse;
	}

	public void closeSession() {
		sendCommand("Q " + sessionIndex);
	}

	public synchronized String getLeaderboard() {
		sendCommand("R");

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getLeaderboardResponse;
	}

	public synchronized String getAllSessions() {
		sendCommand("S");

		try {
			wait();
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		}

		return getAllSessionsResponse;
	}

	public void uploadSong() {
		sendCommand("Y " + sessionIndex);
	}

	public void playSong(String name) {
		sendCommand("X " + sessionIndex + " " + name);
	}

	public void sendMessage(String message) {
		sendCommand("Z " + sessionIndex + " " + message);
	}

	/*
	 * SERVER RESPONSES
	 */
	@Override
	public void run() {
		try {
			while (true) {

				String line = br.readLine();

				if (line == null) {
					continue;
				}

				// DEBUG
				// System.out.println("Client received response from server: "
				// + line);

				String[] strArray = line.split("\\s+");

				if (strArray[0].equals("1")) {
					loginUserResponse = Boolean.valueOf(line.substring(2,
							line.length()));
				} else if (strArray[0].equals("3")) {
					createNewUserResponse = line.substring(2, line.length());
					System.out.println("createnewuserresponse"
							+ createNewUserResponse);
				} else if (strArray[0].equals("6")) {
					isGuestResponse = Boolean.valueOf(line.substring(2,
							line.length()));
				} else if (strArray[0].equals("7")) {
					getUsernameResponse = line.substring(2, line.length());
				} else if (strArray[0].equals("8")) {
					getScoreResponse = Integer.parseInt(line.substring(2,
							line.length()));
				} else if (strArray[0].equals("A")) {
					createNewSessionResponse = line.substring(2, line.length());
				} else if (strArray[0].equals("D")) {
					getDJResponse = line.substring(2, line.length());
				} else if (strArray[0].equals("E")) {
					getNameResponse = line.substring(2, line.length());
				} else if (strArray[0].equals("F")) {
					getGenreResponse = line.substring(2, line.length());
				} else if (strArray[0].equals("G")) {
					isPrivateResponse = Boolean.valueOf(line.substring(2,
							line.length()));
				} else if (strArray[0].equals("H")) {
					getPasswordResponse = line.substring(2, line.length());
				} else if (strArray[0].equals("I")) {
					getRoomSizeResponse = Integer.parseInt(line.substring(2,
							line.length()));
				} else if (strArray[0].equals("J")) {
					getUserListResponse = line.substring(2, line.length());
				} else if (strArray[0].equals("M")) {
					getLikesResponse = Integer.parseInt(line.substring(2,
							line.length()));
				} else if (strArray[0].equals("N")) {
					getDislikesResponse = Integer.parseInt(line.substring(2,
							line.length()));
				} else if (strArray[0].equals("O")) {
					getSongListResponse = line.substring(2, line.length());
				} else if (strArray[0].equals("P")) {
					isDJResponse = Boolean.valueOf(line.substring(2,
							line.length()));
				} else if (strArray[0].equals("Q")) {
					sessionClosed = true;
				} else if (strArray[0].equals("R")) {
					getLeaderboardResponse = line.substring(2, line.length());
				} else if (strArray[0].equals("S")) {
					getAllSessionsResponse = line.substring(2, line.length());
				} else if (strArray[0].equals("W")) {
					setUpdateSessionTrue();
				} else if (strArray[0].equals("X")) {
					currentSong = line.substring(2, line.length());
					receiveSong();
				} else if (strArray[0].equals("Z")) {
					message = line.substring(2, line.length());
				}

				synchronized (this) {
					notify();
				}
			}
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
			JOptionPane errorMessage = new JOptionPane();
			JOptionPane
					.showMessageDialog(errorMessage,
							"Connection could not be made with the server. Please try again.");
			System.exit(0);
		}
	}

	public void receiveSong() {
		try {
			s2 = new Socket("localhost", 4242);
			int bytesRead = 0;
			byte[] b = new byte[1];
			BufferedInputStream bis = new BufferedInputStream(
					s2.getInputStream());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FileOutputStream fos = new FileOutputStream(song);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bytesRead = bis.read(b, 0, b.length);
			do {
				baos.write(b);
				bytesRead = bis.read(b);
			} while (bytesRead != -1);
			bos.write(baos.toByteArray());
			bos.flush();
			bos.close();
			setReadyToPlay();
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
		}
	}

}

@SuppressWarnings("serial")
class ConnectionException extends Exception {
}
