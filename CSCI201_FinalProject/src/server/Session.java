package server;

import java.util.Vector;

public class Session {

	private Vector<User> userList;
	private User dj;
	private String name;
	private String genre;
	private boolean privacy;
	private String password;
	private int likes;
	private int dislikes;

	public Session(User user, String name, String genre, boolean privacy,
			String password) {
		userList = new Vector<User>();
		this.dj = user;
		userList.addElement(dj);
		this.name = name;
		this.genre = genre;
		this.privacy = privacy;
		this.password = password;
		likes = 0;
		dislikes = 0;
	}

	public void joinSession(User user) {
		userList.addElement(user);
	}

	public void leaveSession(User user) {
		userList.remove(user);
	}

	public void like() {
		likes++;
	}

	public void dislike() {
		dislikes++;
	}

	public User getDJ() {
		return dj;
	}

	public String getName() {
		return name;
	}

	public String getGenre() {
		return genre;
	}

	public boolean isPrivate() {
		return privacy;
	}

	public String getPassword() {
		return password;
	}

	public int getRoomSize() {
		return userList.size();
	}

	public Vector<User> getUserList() {
		return userList;
	}

	public int getLikes() {
		return likes;
	}

	public int getDislikes() {
		return dislikes;
	}
}