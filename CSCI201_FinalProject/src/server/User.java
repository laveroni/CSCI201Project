package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User extends Thread {

	private Connection conn;
	private BufferedOutputStream bos;
	private BufferedInputStream bis;
	private BufferedReader br;
	private PrintWriter pw;
	private Server sv;
	private Socket s1;
	private ServerSocket ms;

	private String username;
	private boolean guest;
	private int score;

	public User(Connection conn, Socket s1, ServerSocket ms, Server sv) {
		this.conn = conn;
		this.s1 = s1;
		this.ms = ms;
		this.sv = sv;

		username = "";
		guest = false;
		score = 0;

		try {
			br = new BufferedReader(new InputStreamReader(s1.getInputStream()));
			pw = new PrintWriter(s1.getOutputStream());
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
		}
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setAsGuest() {
		guest = true;
	}

	public void setAsNotGuest() {
		guest = false;
	}

	public boolean isGuest() {
		return guest;
	}

	public void increaseScore() {
		score++;
		try {
			PreparedStatement ps = conn
					.prepareStatement("UPDATE users SET highscore=highscore+1 WHERE username = '"
							+ username + "'");
			ps.executeUpdate();
		} catch (SQLException sqle) {
			System.out.println("SQLE: " + sqle.getMessage());
		}
	}

	public void decreaseScore() {
		score--;
		try {
			PreparedStatement ps = conn
					.prepareStatement("UPDATE users SET highscore=highscore-1 WHERE username = '"
							+ username + "'");
			ps.executeUpdate();
		} catch (SQLException sqle) {
			System.out.println("SQLE: " + sqle.getMessage());
		}
	}

	public int getScore() {
		return score;
	}

	public void sendResponse(String response) {
		pw.println(response);
		pw.flush();
	}

	public void sendSong(String name) {
		File song = null;
		if (name.equals("upload.mp3")) {
			song = new File(name);
		} else {
			song = new File(sv.getSongDatabase().get(name));
		}
		try {
			Socket s2 = ms.accept();
			bis = new BufferedInputStream(new FileInputStream(song));
			bos = new BufferedOutputStream(s2.getOutputStream());
			byte[] b = new byte[(int) song.length()];
			bis.read(b, 0, b.length);
			bos.write(b, 0, b.length);
			bos.flush();
			bis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				String line = br.readLine();
				sv.checkRequest(this, line);
			}
		} catch (IOException ioe) {
			sv.removeUserThread(this);
			System.out.println(s1.getInetAddress() + ":" + s1.getPort()
					+ " disconnected.");
		}
	}
}