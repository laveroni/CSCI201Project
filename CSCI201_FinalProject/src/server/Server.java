package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class Server {

	private String MYSQL_USERNAME = "root";
	private String MYSQL_PASSWORD = "";
	private String MYSQL_DATABASE = "dj_project";
	private Connection conn;
	private Vector<User> userVector;
	private Vector<Session> sessionVector;
	private HashMap<String, String> songDatabase;

	private ServerSocket js;

	public Server(String username, String password, String database) {
		MYSQL_USERNAME = username;
		MYSQL_PASSWORD = password;
		MYSQL_DATABASE = database;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost/" + MYSQL_DATABASE + "?user="
							+ MYSQL_USERNAME + "&password=" + MYSQL_PASSWORD);
		} catch (SQLException sqle) {
			System.out.println("SQLE: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("CNFE: " + cnfe.getMessage());
		}

		userVector = new Vector<User>();
		sessionVector = new Vector<Session>();
		songDatabase = new HashMap<String, String>();

		addSong("FourFiveSeconds - Rihanna, Kanye West, Paul McCartney",
				"song_database/FourFiveSeconds.mp3");
		addSong("See You Again - Wiz Khalifa, Charlie Puth",
				"song_database/SeeYouAgain.mp3");
		addSong("Know Yourself - Drake", "song_database/KnowYourself.mp3");

		ServerSocket ss = null;
		ServerSocket ms = null;
		try {
			System.out.println("Starting Server");
			ss = new ServerSocket(6789);
			ms = new ServerSocket(4242);
			js = new ServerSocket(5690);
			while (true) {
				System.out.println("Waiting for client to connect...");
				Socket s1 = ss.accept();
				System.out.println("Client " + s1.getInetAddress() + ":"
						+ s1.getPort() + " connected");
				User user = new User(conn, s1, ms, this);
				userVector.add(user);
				user.start();
			}
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException ioe) {
					System.out.println("IOE: " + ioe.getMessage());
				}
			}
		}
	}

	public void removeUserThread(User user) {
		userVector.remove(user);
	}

	public void checkRequest(User user, String str) {

		if (str == null) {
			return;
		}

		String[] strArray = str.split("\\s+");

		// DEBUG
		// System.out.println("received " + strArray[0]);

		if (strArray[0].equals("1")) { // *
			String username = strArray[1];
			String password = strArray[2];
			user.sendResponse("1 " + loginUser(user, username, password));
		} else if (strArray[0].equals("2")) {
			removeUserThread(user);
		} else if (strArray[0].equals("3")) { // *
			String email = strArray[1];
			String username = strArray[2];
			String password = strArray[3];
			user.sendResponse("3 "
					+ createNewUser(user, email, username, password));
		} else if (strArray[0].equals("4")) {
			user.setAsGuest();
			user.setUsername("Guest");
		} else if (strArray[0].equals("5")) {
			user.setAsNotGuest();
			user.setUsername("");
		} else if (strArray[0].equals("6")) { // *
			user.sendResponse("6 " + user.isGuest());
		} else if (strArray[0].equals("7")) { // *
			user.sendResponse("7 " + user.getUsername());
		} else if (strArray[0].equals("8")) { // *
			user.sendResponse("8 " + user.getScore());
		} else if (strArray[0].equals("A")) { // *
			String name = strArray[1];
			String genre = strArray[2];
			boolean privacy = Boolean.valueOf(strArray[3]);
			String password = "";
			if (strArray.length == 5) {
				password = strArray[4];
			}
			user.sendResponse("A "
					+ createNewSession(user, name, genre, privacy, password));
		} else if (strArray[0].equals("B")) {
			int index = Integer.parseInt(strArray[1]);
			joinSession(user, index);
		} else if (strArray[0].equals("C")) {
			int index = Integer.parseInt(strArray[1]);
			leaveSession(user, index);
		} else if (strArray[0].equals("D")) {
			int index = Integer.parseInt(strArray[1]);
			user.sendResponse("D " + getDJ(user, index));
		} else if (strArray[0].equals("E")) {
			int index = Integer.parseInt(strArray[1]);
			user.sendResponse("E " + getName(user, index));
		} else if (strArray[0].equals("F")) {
			int index = Integer.parseInt(strArray[1]);
			user.sendResponse("F " + getGenre(user, index));
		} else if (strArray[0].equals("G")) {
			int index = Integer.parseInt(strArray[1]);
			user.sendResponse("G " + isPrivate(user, index));
		} else if (strArray[0].equals("H")) {
			int index = Integer.parseInt(strArray[1]);
			user.sendResponse("H " + getPassword(user, index));
		} else if (strArray[0].equals("I")) {
			int index = Integer.parseInt(strArray[1]);
			user.sendResponse("I " + getRoomSize(user, index));
		} else if (strArray[0].equals("J")) {
			int index = Integer.parseInt(strArray[1]);
			user.sendResponse("J " + getUserList(user, index));
		} else if (strArray[0].equals("K")) {
			int index = Integer.parseInt(strArray[1]);
			like(index);
		} else if (strArray[0].equals("L")) {
			int index = Integer.parseInt(strArray[1]);
			dislike(index);
		} else if (strArray[0].equals("M")) { // *
			int index = Integer.parseInt(strArray[1]);
			user.sendResponse("M " + getLikes(index));
		} else if (strArray[0].equals("N")) { // *
			int index = Integer.parseInt(strArray[1]);
			user.sendResponse("N " + getDislikes(index));
		} else if (strArray[0].equals("O")) { // *
			user.sendResponse("O " + getSongList());
		} else if (strArray[0].equals("P")) { // *
			int index = Integer.parseInt(strArray[1]);
			user.sendResponse("P " + isDJ(user, index));
		} else if (strArray[0].equals("Q")) { // *
			int index = Integer.parseInt(strArray[1]);
			closeSession(index);
		} else if (strArray[0].equals("R")) { // *
			user.sendResponse("R " + getLeaderboard());
		} else if (strArray[0].equals("S")) { // *
			user.sendResponse("S " + getAllSessions());
		} else if (strArray[0].equals("Y")) { // *
			int index = Integer.parseInt(strArray[1]);
			uploadSong(index);
		} else if (strArray[0].equals("X")) { // *
			int index = Integer.parseInt(strArray[1]);
			String name = str.substring(4, str.length());
			playSong(index, name);
		} else if (strArray[0].equals("Z")) {
			int index = Integer.parseInt(strArray[1]);
			String message = str.substring(3, str.length());
			sendMessage(user, index, message);
		}

	}

	public boolean loginUser(User user, String username, String password) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String u = rs.getString("username");
				String p = rs.getString("password");

				if (username.equals(u) && password.equals(p)) {
					user.setUsername(username);
					return true;
				}
			}
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("SQLE: " + sqle.getMessage());
		}

		return false;
	}

	public String createNewUser(User user, String email, String username,
			String password) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String u = rs.getString("username");
				String e = rs.getString("email");

				if (username.equals(u)) {
					return "username exists";
				} else if (email.equalsIgnoreCase(e)) {
					return "email exists";
				}
			}

			ps = conn.prepareStatement("INSERT INTO users"
					+ "(username, email, password) " + "VALUES ('" + username
					+ "', '" + email + "', '" + password + "')");
			ps.executeUpdate();

			rs.close();
		} catch (SQLException sqle) {
			System.out.println("SQLE: " + sqle.getMessage());
			return "database error";
		}

		user.setUsername(username);
		return "createuser success";
	}

	public int createNewSession(User user, String name, String genre,
			boolean privacy, String password) {
		Session st = new Session(user, name, genre, privacy, password);
		sessionVector.addElement(st);
		return sessionVector.indexOf(st);
	}

	public void joinSession(User user, int index) {
		sessionVector.elementAt(index).joinSession(user);
		Vector<User> users = sessionVector.elementAt(index).getUserList();
		for (int i = 0; i < users.size(); i++) {
			users.elementAt(i).sendResponse("W");
		}
	}

	public void leaveSession(User user, int index) {
		sessionVector.elementAt(index).leaveSession(user);
		Vector<User> users = sessionVector.elementAt(index).getUserList();
		for (int i = 0; i < users.size(); i++) {
			users.elementAt(i).sendResponse("W");
		}
	}

	public String getDJ(User user, int index) {
		return sessionVector.elementAt(index).getDJ().getUsername();
	}

	public String getName(User user, int index) {
		return sessionVector.elementAt(index).getName();
	}

	public String getGenre(User user, int index) {
		return sessionVector.elementAt(index).getGenre();
	}

	public boolean isPrivate(User user, int index) {
		return sessionVector.elementAt(index).isPrivate();
	}

	public String getPassword(User user, int index) {
		return sessionVector.elementAt(index).getPassword();
	}

	public int getRoomSize(User user, int index) {
		return sessionVector.elementAt(index).getRoomSize();
	}

	public String getUserList(User user, int index) {
		Vector<User> tempUserList = sessionVector.elementAt(index)
				.getUserList();
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < tempUserList.size(); i++) {
			builder.append(tempUserList.elementAt(i).getUsername());
			builder.append(" ");
		}

		return builder.toString();
	}

	public void like(int index) {
		sessionVector.elementAt(index).like();
		sessionVector.elementAt(index).getDJ().increaseScore();

		Vector<User> users = sessionVector.elementAt(index).getUserList();
		for (int i = 0; i < users.size(); i++) {
			users.elementAt(i).sendResponse("W");
		}
	}

	public void dislike(int index) {
		sessionVector.elementAt(index).dislike();
		sessionVector.elementAt(index).getDJ().decreaseScore();

		Vector<User> users = sessionVector.elementAt(index).getUserList();
		for (int i = 0; i < users.size(); i++) {
			users.elementAt(i).sendResponse("W");
		}
	}

	public int getLikes(int index) {
		return sessionVector.elementAt(index).getLikes();
	}

	public int getDislikes(int index) {
		return sessionVector.elementAt(index).getDislikes();
	}

	public String getSongList() {
		StringBuilder builder = new StringBuilder();
		Set<String> songNames = songDatabase.keySet();
		Iterator<String> it = songNames.iterator();
		while (it.hasNext()) {
			builder.append(it.next());
			builder.append("?");
		}
		return builder.toString();
	}

	public boolean isDJ(User user, int index) {
		return user.getUsername() == sessionVector.elementAt(index).getDJ()
				.getUsername();
	}

	public void closeSession(int index) {
		Vector<User> users = sessionVector.elementAt(index).getUserList();
		for (int i = 0; i < users.size(); i++) {
			users.elementAt(i).sendResponse("Q");
		}
		sessionVector.remove(index);
	}

	public String getLeaderboard() {
		Vector<String> sortedNames = new Vector<String>();
		Vector<Integer> sortedScores = new Vector<Integer>();

		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String username = rs.getString("username");
				int highscore = rs.getInt("highscore");

				sortedNames.addElement(username);
				sortedScores.addElement(highscore);
			}

			quickSort(sortedScores, sortedNames, 0, sortedScores.size() - 1);

			rs.close();
		} catch (SQLException sqle) {
			System.out.println("SQLE: " + sqle.getMessage());
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < sortedNames.size(); i++) {
			builder.append(sortedNames.elementAt(i));
			builder.append("*");
			builder.append(sortedScores.elementAt(i));
			builder.append("?");
		}

		return builder.toString();
	}

	public String getAllSessions() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < sessionVector.size(); i++) {
			builder.append(sessionVector.elementAt(i).isPrivate());
			builder.append("*");
			builder.append(sessionVector.elementAt(i).getName());
			builder.append("*");
			builder.append(sessionVector.elementAt(i).getGenre());
			builder.append("*");
			builder.append(sessionVector.elementAt(i).getRoomSize());
			builder.append("*");
			builder.append(sessionVector.elementAt(i).getPassword());
			builder.append("?");
		}
		return builder.toString();
	}

	public void uploadSong(int index) {
		try {
			Socket s = js.accept();
			String song = "upload.mp3";
			int bytesRead = 0;
			byte[] b = new byte[1];
			BufferedInputStream bis = new BufferedInputStream(
					s.getInputStream());
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

			playSong(index, song);
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
		}

	}

	public void sendMessage(User user, int index, String message) {
		Vector<User> users = sessionVector.elementAt(index).getUserList();
		for (int i = 0; i < users.size(); i++) {
			users.elementAt(i).sendResponse(
					"Z " + user.getUsername() + ":" + message);
		}
	}

	public void playSong(int index, String name) {
		Vector<User> users = sessionVector.elementAt(index).getUserList();
		for (int i = 0; i < users.size(); i++) {
			users.elementAt(i).sendResponse("X " + name);
			users.elementAt(i).sendResponse("W");
			users.elementAt(i).sendSong(name);
		}
	}

	public HashMap<String, String> getSongDatabase() {
		return songDatabase;
	}

	public void addSong(String title, String location) {
		songDatabase.put(title, location);
	}

	private void quickSort(Vector<Integer> v1, Vector<String> v2, int left,
			int right) {
		int index = partition(v1, v2, left, right);
		if (left < index - 1) {
			quickSort(v1, v2, left, index - 1);
		}
		if (index < right) {
			quickSort(v1, v2, index, right);
		}
	}

	private int partition(Vector<Integer> v1, Vector<String> v2, int left,
			int right) {
		int i = left, j = right;
		int tmp;
		String temp;
		int pivot = v1.elementAt((left + right) / 2);

		while (i <= j) {
			while (v1.elementAt(i) > pivot) {
				i++;
			}
			while (v1.elementAt(j) < pivot) {
				j--;
			}
			if (i <= j) {
				tmp = v1.elementAt(i);
				v1.setElementAt(v1.elementAt(j), i);
				v1.setElementAt(tmp, j);
				temp = v2.elementAt(i);
				v2.setElementAt(v2.elementAt(j), i);
				v2.setElementAt(temp, j);
				i++;
				j--;
			}
		}
		return i;
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter database username: ");
		String username = scan.nextLine();
		System.out.println("Enter database password: ");
		String password = scan.nextLine();
		System.out.println("Enter database name: ");
		String database = scan.nextLine();
		new Server(username, password, database);
		scan.close();
	}
}