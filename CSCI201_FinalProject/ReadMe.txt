# CS201-Project
	 
Third Party API:
-JavaLayer: allows mp3 play functionality
-MySQL: database functionality
	 
Instructions:
-Import the project
-Change build paths for jl1.0.1.jar and mysql-connector-java-5.1.35-bin.jar (included in project directory)
-A mysql dump file is given in the repository, use data import to create the database
-Run Server and enter the username, password, and name of the database
	-Mine is ("root", "", "password") for simplicity
-Clients may now start DJProgram to establish connections with the server

General notes:
-Reminder: both jl1.0.1.jar and mysql-connector-java-5.1.35-bin.jar need to be added to the project
-Caution: although data is passed through different sockets, the server can still be overloaded
-Every time a new song is played, a vote can be issued
-Sessions only exist for a single instance of the server
-Guests cannot vote or create sessions

	/*
	 * Server Request Commands (* response from server necessary):
	 * 1 loginUser *
	 * 2 logoutUser
	 * 3 createNewUser *
	 * 4 setAsGuest
	 * 5 setAsNotGuest
	 * 6 isGuest *
	 * 7 getUsername *
	 * 8 getScore *
	 * 
	 * A createNewSession *
	 * B joinSession
	 * C leaveSession
	 * D getDJ *
	 * E getName *
	 * F getGenre *
	 * G isPrivate *
	 * H getPassword *
	 * I getRoomSize *
	 * J getUserList *
	 * K like
	 * L dislike
	 * M getLikes *
	 * N getDislikes *
	 * O getSongList *
	 * P isDJ *
	 * Q closeSession
	 * R getLeaderboard *
	 * S getAllSessions *
	 * 
	 * Y uploadSong
	 * X playSong
	 * Z sendMessage
	 */