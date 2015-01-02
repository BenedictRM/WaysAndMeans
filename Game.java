import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import com.mysql.jdbc.Statement;//To connect to the database

//***To create a new game call INSERT INTO game (CREATED) values (null), add a function called 'create game' to let users create new games

public class Game {	 
	
	//Class variables: Keep out of this class to keep it like a state machine	
			
	//Connect to sql JAR file
	public static void connection()
	{
		try{
			//Accessing driver from the JAR file
			Class.forName("com.mysql.jdbc.Driver");
		}catch (ClassNotFoundException o){
			o.printStackTrace();
		}
	}
	
	//This function connects to the mysql database game, once on server we can change passwords more easily from here
	public static Connection dbConnection()
	{
		Connection connect = null;
		
		try{
			//zeroDateTimeBehavior=convertToNull converts 0:00:00etc. time-stamps to null preventing the system from crashing
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/game?zeroDateTimeBehavior=convertToNull", "Your UN","Your Password");
		}catch(SQLException o){
			o.printStackTrace();
		}
		
		return connect;
	}
	
	//Create the new user entry into player table in game database
	public static void createNew (String pk, String username, String password)
	{
		//Declare variables
		Connection connect = null;		
		PreparedStatement statement = null;
		
		connection();
		//Creating a variable for the connection called "connect"
		
		try{
			
			//connect to the database
			connect = dbConnection();
			
			statement = connect.prepareStatement("INSERT INTO player(APP_USERS_PK, USERNAME, PASSWORD, REPUTATION_POINTS) VALUES (?,?,?,?);");
			statement.setString(1, pk);
			statement.setString(2, username);
			statement.setString(3, password);
			statement.setInt(4, 50);
			statement.executeUpdate();
								
			//Disconnect from the database
			connect.close();
			statement.close();
		}
		
		catch(SQLException o){
			o.printStackTrace();
		}
	}
		
	//Create a login function to retrieve user data
	public static int login (String username, String password)
	{
		//Declare variables
		String dbUsername = null;
		String dbPassword = null;
		int dbPrimarykey = 0;//May need to make this an int
		Statement statement;
		ResultSet resultSet;
		Connection connect = null;
		
		//Connect to jar file
		connection();
		
		try
		{			
			 connect = dbConnection();
		     statement = (Statement) connect.createStatement();
		     resultSet = statement.executeQuery("select * from player;");
		     
			 while (resultSet.next()) 
			 {      
				    //retrieve data				    				    
			        dbUsername = resultSet.getString("USERNAME");
			        dbPassword = resultSet.getString("PASSWORD");
			        
			        //User found!
			        if ((dbUsername.equals(username)) && (dbPassword.equals(password)))
			        {
			        	
			        	dbPrimarykey = resultSet.getInt("APP_USERS_PK");			     
			        	break;
			        }
			 }
			 
			//Disconnect from the database
			connect.close(); 
			statement.close();
			resultSet.close(); 			
		     
			 return dbPrimarykey;
		}
	
		catch(SQLException o)
		{
			o.printStackTrace();
			return 0;
		}
	}
	
	//Delete user from player table and game database all together
	public static void deleteUser (int pk)
	{
		//Create variables
		Connection connect = null;
		PreparedStatement statement = null;
		
		connection();
				
		try{
			//Creating a variable for the connection called "connect"
			//connect to the database
			connect = dbConnection();
		    statement = connect.prepareStatement("DELETE FROM player WHERE APP_USERS_PK = " + pk);
			statement.executeUpdate();
			//Disconnect from the database
			connect.close();
			statement.close();			
		}
		
		catch(SQLException o){
			o.printStackTrace();
		}
		System.out.println("Your profile has now been deleted from the database");
	}
				
	//This function is used to add a user to a game, sets their initial values across several tables
	//Prereq: No games can be called 'game 0', this will cause failures--games can be any positive integer, just increment as necessary
	public static void addToGame (int pk, int playerGame)
	{
		Connection connect = null;
		PreparedStatement statement = null; 
		ResultSet resultSet;//For retrieving database data
		int game = playerGame;//users game that they wish to join
		int dbpk = pk;//user's pk
		int PK_P2G = 0;//This will set the FK player role for this user
		
		//Connect to jar file
		connection();
												
		try
		{
			 connect = dbConnection();
			 			 			 
			 //Go ahead and add them to the candidate list now, that function will sort who is worthy of president there
			 statement = connect.prepareStatement("INSERT INTO candidates (FK_PLAYER, FK_GAME) VALUES (?,?);");	
			 statement.setInt(1, dbpk);	
			 statement.setInt(2, game);	
			 statement.executeUpdate();
			 
			 //Set player_to_game to reflect this new game for this player
			 statement = connect.prepareStatement("INSERT INTO player_to_game (PLAYER_ID, GAME_ID, WHEN_JOINED) VALUES (?,?,?);");
			 statement.setInt(1, dbpk);
			 statement.setInt(2, game);
			 statement.setString(3, null);//Send a null value to trigger an update to joined timestamp			      					  	        					 	        					
			 statement.executeUpdate();	
			 
			 //Retrieve the player_to_game PK for this user to add to the player_role and voting_history tables
			 resultSet = statement.executeQuery("SELECT P2G.PK FROM player_to_game P2G WHERE (P2G.PLAYER_ID = " + dbpk + ");");
			 resultSet.next();//ADvance the resultSet cursor
			 PK_P2G = resultSet.getInt("P2G.PK");   		
			 
			 //Set voting_history for this player for this game
			 statement = connect.prepareStatement("INSERT INTO voting_history (FK_P2G) VALUES (?);");	
			 statement.setInt(1, PK_P2G);	
			 statement.executeUpdate();
			 
			 //Set default player_role for this player for this game
			 statement = connect.prepareStatement("INSERT INTO player_role (FK_P2G) VALUES (?);");	
			 statement.setInt(1, PK_P2G);		
			 statement.executeUpdate();
			 
			 //Disconnect from the database
			 connect.close();
			 statement.close();
			 resultSet.close();
		}
			catch(SQLException o)
			{				
				o.printStackTrace();
			}
		
		return;
	}
	
	//This function starts a game with at least 10 players in it
	//User Story: the player who created this game can start but no-one else
	public static void startGame(int playerGame) 
	{
		Connection connect = null;
		PreparedStatement statement = null;
		//Connect to jar file
		connection();		
				
		try
		{
			connect = dbConnection();//Connect to db
			
			//Start the game that this player is currently in
			statement = connect.prepareStatement("UPDATE game G SET G.STARTED = (?) WHERE G.PK_GAME_NUMBER = " + playerGame + ";");
			statement.setString(1, null);//Send a null value to trigger an update (effectively starts the game)
			statement.executeUpdate();						
			
			//Disconnect from the database
		    connect.close();
		    statement.close();		
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}			
	}
							
	//This function will be called to add a user's response to running for president to the database
	public static void candidateAdd (String ans, int pk)
	{
		PreparedStatement stmnt = null; 
		Connection connect = null;
		
		//Connect to jar file
		connection();
		try
		{
			 connect = dbConnection();
			 while (!(ans.equals("Yea")) || !((ans.equals("Nay"))))
			 {
				 //Add user's response to the Yea column
				 if (ans.equals("Yea"))    			  
				 {		        					       					 
					 stmnt = connect.prepareStatement("UPDATE candidates C SET C.YEA = (?) WHERE C.FK_PLAYER = " + pk + ";");
					 stmnt.setInt(1, 1);	        					  	        					 	        					
					 stmnt.executeUpdate();
					  
					 break;
				 }
				     //Add user's response to the Nay column
				     else if (ans.equals("Nay"))
				     {
					     stmnt = connect.prepareStatement("UPDATE candidates C SET C.NAY = (?) WHERE C.FK_PLAYER = " + pk + ";");
					     stmnt.setInt(1, 1);
					     stmnt.executeUpdate();				  
					     break;
				     }				  			  
			 }
			 //Disconnect from the database
			 connect.close();
			 stmnt.close();			
		}
		
		catch(SQLException o)
		{
			o.printStackTrace();
		}
	}
	
	//Sets up election table, if it has already been set up then it skips setup
	public static boolean electionSetup(int playerGame)
	{
		int candidateCounter = 0;//Users who've accepted nomination
		int userCounter = 0;//To count user in this game in total	
		int FK_CANDIDATE = 0;//This will set the FK for election table referencing candidates PK
		int FK_GAME = 0;
		String dbUsername = null;//Used to store each user name from candidates list
		Connection connect = null;
		Statement statement;
		PreparedStatement stmnt = null;
		ResultSet resultSet;
				
		//Connect to jar file
		connection(); 
		
		try{
			   connect = dbConnection();
		       statement = (Statement) connect.createStatement();
		     
		       //Count the number of candidates in this game
		       resultSet = statement.executeQuery("SELECT COUNT(C.YEA) FROM candidates C, game G WHERE (C.FK_GAME = G.PK_GAME_NUMBER) AND (YEA = 1) AND (G.PK_GAME_NUMBER = " + playerGame +");");
		       resultSet.next();
		       candidateCounter = resultSet.getInt(1);
		     		    		       
		       //if we have all of our candidates then populate the election table and exit
		       if(candidateCounter >= 10)
		       {
		    	   //Check if the election table has already been populated
			       resultSet = statement.executeQuery("SELECT COUNT(E.CANDIDATE) FROM election E, game G WHERE (E.FK_GAME = G.PK_GAME_NUMBER) AND (G.PK_GAME_NUMBER = " + playerGame +");");
			       resultSet.next();
			       userCounter = resultSet.getInt(1);
		    	   
			       //Double check if this election needs an update i.e. players dropping out or whatever
			       if (userCounter < candidateCounter)
			       {
			    	    System.out.println("Entering line 458\n");
			    	    //Delete the old election and setup a new one, users may have dropped out or this is a new game
			    	    if (userCounter > 0)
			    	    {
			    	    	resultSet = statement.executeQuery("SELECT * FROM election E, game G WHERE (E.FK_GAME = G.PK_GAME_NUMBER) AND (G.PK_GAME_NUMBER = " + playerGame +");");
			    	    	
			    	    	while(resultSet.next())
			    	    	{
			    	    		stmnt = connect.prepareStatement("DELETE FROM election USING game, election WHERE (election.FK_GAME = game.PK_GAME_NUMBER);");
			    	    		stmnt.executeUpdate();
			    	    	}
			    	    }
			    	   
			    	    System.out.println("Setting up election line 471"); 
			    	    resultSet = statement.executeQuery("SELECT P.USERNAME, P.REPUTATION_POINTS, C.PK, C.FK_GAME " +
				     		"FROM player P, candidates C, player_to_game P2G, game G WHERE (P.APP_USERS_PK = C.FK_PLAYER) AND (C.YEA = 1) " +
				     		"AND (C.FK_GAME = G.PK_GAME_NUMBER) AND (P.APP_USERS_PK = P2G.PLAYER_ID) AND (P2G.GAME_ID = G.PK_GAME_NUMBER) AND (G.PK_GAME_NUMBER = " + playerGame +")" +
				     		" ORDER BY P.REPUTATION_POINTS, P.APP_USERS_PK;");
			    	    System.out.println("Setting up election line 647"); 			    	   	    	   
						while (resultSet.next()) 
						{      					    						   										
							//retrieve data				    				    						    						      						
							dbUsername = resultSet.getString("P.USERNAME");  				
						    FK_CANDIDATE = resultSet.getInt("C.PK");
						    FK_GAME = resultSet.getInt("C.FK_GAME");
						    stmnt = connect.prepareStatement("INSERT INTO election(CANDIDATE, FK_CANDIDATES, FK_GAME) VALUES (?,?,?)");
						    stmnt.setString(1, dbUsername);							
							stmnt.setInt(2, FK_CANDIDATE);
							stmnt.setInt(3, FK_GAME);
							stmnt.executeUpdate();
						}						
			       }			   	   			    

			       //The election table has been filled
			       return true;
		 	   }
		       
		       //Disconnect from the database
		       connect.close();
		       statement.close();			   	
			   resultSet.close();			   
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		//The election table is empty, no vote yet
		return false;
	}
	
	//This function sets player's vote for president, players votes are kept in voting_history table
	public static void elect (String vote, int pk, int playerGame)
	{
		PreparedStatement statement = null;
		Connection connect = null;
		
		//Connect to jar file
		connection(); 
		
		try
		{			
			//Connect to the DB
			connect = dbConnection();
			//NOTE that to send a string for comparison you must encapsulate the string in quotes like: '" + value + "'
			statement = connect.prepareStatement("UPDATE election E, game G SET E.YEA = E.YEA + (?) WHERE (E.CANDIDATE = '" + vote + "') AND (E.FK_GAME = G.PK_GAME_NUMBER) AND (G.PK_GAME_NUMBER = " + playerGame +");");
			statement.setInt(1, 1);
			statement.executeUpdate();
			
			//Update voting history table
			statement = connect.prepareStatement("UPDATE voting_history V, player_to_game P2G SET V.ELECTION = 1 WHERE (V.FK_P2G = P2G.PK) AND (P2G.PLAYER_ID = " + pk + ") AND (P2G.GAME_ID = " + playerGame + ");");		
			statement.executeUpdate();
			
			//Disconnect from the database
			connect.close();
			statement.close();		
		}
		
		catch(SQLException o)
		{
			o.printStackTrace();
		}
	}
	
	//This function returns the number of players who are in the game the user is currently in
	public static int inGameCount(int playerGame)
	{			
		Connection connect = null;
		Statement statement;
		ResultSet resultSet;
		int playersInGame;	
		
		playersInGame = 0;
		//Connect to jar file
		connection();
		
		try{	
			connect = dbConnection();

			statement = (Statement) connect.createStatement();		     
	        //Count the number of candidates in this game
	        resultSet = statement.executeQuery("SELECT GAME_ID FROM player_to_game WHERE GAME_ID = " + playerGame + ";");
	        
	        while (resultSet.next())
	        {
	        	playersInGame++;
	        }
	        
			//Disconnect from the database
		    connect.close();
		    statement.close();
		    
		    System.out.println("Players in this game is: " + playersInGame + "\n");
		    
		    return playersInGame;		
	    }
		
		//Catch any errors	
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		
		return playersInGame;
	}
	
	//This function is used to check if a logged in player is in a game, if not ask them to join a game
	public static boolean inGameCheck (int pk)
	{
		boolean inGame = false;
		Connection connect = null;
		Statement statement;
		ResultSet resultSet;
		
		//Connect to jar file
		connection();
			
		try
		{
			 connect = dbConnection();
		     statement = (Statement) connect.createStatement();
		     
		     //Return the player_to_game table with associated players
		     resultSet = statement.executeQuery("SELECT P.APP_USERS_PK, G.PLAYER_ID, G.GAME_ID FROM player P, player_to_game G " +
		     		                               "WHERE P.APP_USERS_PK = G.PLAYER_ID;");
		     
		     //retrieve games for this player	
		     while (resultSet.next()) 
			 {      				    		       			        			    	 
		    	    //User found database
			        if (pk == resultSet.getInt("APP_USERS_PK")) 
			        {			        			    				    
			        	//This player is already in a valid game
			        	if (resultSet.getInt("GAME_ID") > 0)
	        			{
			        		//Disconnect from the database
		       		        connect.close();
		       		        statement.close();				 				
		       			    resultSet.close(); 
		       			    //User in a game
		       			    inGame = true;
		       			    
			        		return inGame;
	        			}
				        	//This player is not in a game
			        		else
				        	{
			        			//Disconnect from the database
			       		        connect.close();
			       		        statement.close();				 				
			       			    resultSet.close();
			        			//User not found or user not in game
			       			    return inGame;			        		
				        	}			        	
			        }
			 }
		     
		     //Disconnect from the database and close statements (might be possible to hack the statements if they stay open)
		     connect.close();
		     statement.close();				 				
			 resultSet.close();		     
		}
		
		catch(SQLException o)
		{				
			o.printStackTrace();
		}
		//Player not found, can't be in game (this will be the default return)
		return inGame;
	}
	
	//Check to see if the game this player is actively in has started
	//***May want to pass in an argument later that will let all games that have started be displayed, not just the one this user is in?
	public static boolean gameStartedCheck(int playerGame)
	{
		Date startTime = null;
		Connection connect = null;
		Statement statement;
		ResultSet resultSet;
		//Connect to jar file
		connection();
		try
		{
			connect = dbConnection();//Connect to db
			statement = (Statement) connect.createStatement();//Create the statement		
			resultSet = statement.executeQuery("SELECT G.STARTED FROM game G WHERE G.PK_GAME_NUMBER = " + playerGame + ";");
			
			//Advance the resultSet, if true retrieve the date, otherwise it will be false on program start returning empty set
			//  This if statement should block that from happening
			if (resultSet.next() == true)
			{
				startTime = resultSet.getDate(1);
			}
			
			//This game has not started
			if (startTime == null)
			{
				//Disconnect from the database
				connect.close();
			    statement.close();				 				
				resultSet.close();
				
				return false;
			}
			    //This game has started
				else
				{
					//Disconnect from the database
					connect.close();
				    statement.close();				 				
					resultSet.close();
					
					return true;
				}						
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		//Default return false -- most likely game can still be joined		
		return false;
	}
	
	//while candidate list <= 10 and user rp >= to top candidates and user has not already replied nay to running return true else false
	public static boolean candidateCheck (int pk, int playerGame)
	{
		//Sort the player list, if user is in top ten for rp's and has not replied y or n extend offer
		int RP = 0;
		int candidateCounter = 0;//Users who've accepted nomination
		int answeredCounter = 0;//Users who've accepted or declined nomination
		int userCounter = 0;//To count user in this game in total
		boolean answered = false;
		Connection connect = null;
		Statement statement;
		ResultSet resultSet;
						
		//Connect to jar file
		connection();
		
		try
		{
			 connect = dbConnection();
		     statement = (Statement) connect.createStatement();
		     
		     //Count the number of candidates in this game
		     //resultSet = statement.executeQuery("SELECT COUNT(YEA) FROM candidates WHERE YEA = 1;");
		     resultSet = statement.executeQuery("SELECT COUNT(C.YEA) FROM candidates C, " 
		    		                             + "game G WHERE (C.YEA = 1) AND (C.FK_GAME = G.PK_GAME_NUMBER) AND (G.PK_GAME_NUMBER = " + playerGame +");");
		     		    
		     resultSet.next();
		     candidateCounter = resultSet.getInt(1);
		     
		     //Count the number of users in this game who have answered yea or nay
		     //resultSet = statement.executeQuery("SELECT COUNT(PK), FROM candidates WHERE YEA = 1 OR NAY = 1;");
		     resultSet = statement.executeQuery("SELECT COUNT(C.PK) FROM candidates C, " +
                      "game G WHERE (C.FK_GAME = G.PK_GAME_NUMBER) AND (C.YEA = 1 OR NAY = 1) AND (G.PK_GAME_NUMBER = " + playerGame +");");

		     resultSet.next();
		     answeredCounter = resultSet.getInt(1);
		     
		     //Count the number of users in this game's player database
		     //resultSet = statement.executeQuery("SELECT COUNT(p.USERNAME) FROM player p;");
		     //Count the number of users in this games player database
		     resultSet = statement.executeQuery("SELECT COUNT(P.USERNAME) FROM player P, player_to_game P2G, game G WHERE" +
		     		                             " (P.APP_USERS_PK = P2G.PLAYER_ID) AND (P2G.GAME_ID = G.PK_GAME_NUMBER) AND (G.PK_GAME_NUMBER = " + playerGame +");");
		     resultSet.next();
		     userCounter = resultSet.getInt(1);
		     
		     System.out.println("This game has " + candidateCounter + " viable candidates for president");
		     System.out.println("This game has " + answeredCounter + " candidates who've accepted or declined nomination");
		     System.out.println("This game has " + userCounter + " users in game");
		    
		     //***If candidate pool already 10 viable candidates (***need to add ->) or if all users have answered then skip all this***
		     if (candidateCounter < 10)
		     {
		    	 System.out.println("ENTERING line 497\n");
		    	 //Return the top ten users by rep points and FIFO join time from the same game
			     resultSet = statement.executeQuery("SELECT P.APP_USERS_PK, P.REPUTATION_POINTS, C.YEA, C.NAY " +
			     		"FROM player P, candidates C, game G WHERE (P.APP_USERS_PK = C.FK_PLAYER) AND (C.YEA = 0 AND C.NAY = 0) AND (C.FK_GAME = G.PK_GAME_NUMBER) AND (G.PK_GAME_NUMBER = " + playerGame +") "  +
			     		"ORDER BY P.REPUTATION_POINTS, P.APP_USERS_PK LIMIT 10;");
			     
			     //retrieve rp's FOR THIS USER	
			     while (resultSet.next()) 
				 {      				    		       			        			    	 
			    	    //User found in top ten!
				        if (pk == resultSet.getInt("APP_USERS_PK")) 
				        {			        			    				    
				        	//This user was found in the top ten
				        	RP = resultSet.getInt("REPUTATION_POINTS");						       
				        	break;
				        }
				 }
			     
			     //Now check to see if this user has already entered the candidates table with a yea or nay, if not then invite them to do so
			     //If RP is still 0 then this user wasn't found in the top ten qualified candidates and this code will be skipped
			     if (RP > 0)
			     {	    	 
			    	  resultSet = statement.executeQuery("select * from candidates C, game G WHERE (C.FK_GAME = G.PK_GAME_NUMBER) AND (G.PK_GAME_NUMBER = " + playerGame + ");");			          
			    	  //Check to see if this user has already decided to accept or decline nomination
			          while (resultSet.next())
			          {			        	  			        	 
			        	  if (pk == resultSet.getInt("FK_PLAYER"))
			        	  {
			        		  if (resultSet.getInt("Yea") == 1 || resultSet.getInt("Nay") == 1)
			        		  {
			        			  //This user has already decided whether to run or not
			        			  answered = true;
			        			  break;
			        		  }
			              }
			          }
			          //This user qualifies to run and has not replied to their offer to run  
	        	      if (answered == false)
	        		  {        			  	        	    	  	        	    	  
	        	    	  return true;
	        		  }		        	  		          		        	  
			     }
			     		    		     
				 //Disconnect from the database
			     connect.close();
			     statement.close();				 				
				 resultSet.close();
				 
				 //Either we have all the candidates, or this user has responded already, or this user doesn't qualify to run
				 return false;
			}
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		//Default return, don't risk adding candidates in case an error happens that sends the code here
		return false;
	}
	
	//This function is used to check a new user's userName against the database to make sure their name is unique
	public static boolean userNameCheck(String i)
	{
		Connection connect = null;
		Statement statement;
		ResultSet resultSet;
		
		//Connect to jar file
		connection();
		
		try{
			connect = dbConnection();
	        statement = (Statement) connect.createStatement();
	     
	        //Count the number of candidates in this game
	        resultSet = statement.executeQuery("SELECT USERNAME FROM player;");
	        
	        //Check the database user names against this user name
	        while (resultSet.next())
	        {
	        	if(resultSet.getString(1).equals(i))
	        	{
	        		//Disconnect from the database
	    		    connect.close();
	    		    statement.close();	
	        		return true;
	        	}
	        }
	        
	        //Disconnect from the database
		    connect.close();
		    statement.close();
		    return false;			
		}
		
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		return false;
	}
	
	//This function determines if a player has already cast a vote for this election
	//returns true if player has already cast their vote, false if not
	public static boolean electionVoteCheck(int pk, int playerGame)
	{
		boolean voted = false;
		Connection connect = null;
		Statement statement;
		ResultSet resultSet;
		
		//Connect to jar file
		connection();
		
		try
		{
			 connect = dbConnection();
		     statement = (Statement) connect.createStatement();
		     //Pull this player from voting_history
		     resultSet = statement.executeQuery("SELECT ELECTION FROM voting_history V, player_to_game P2G " 
                     + "WHERE (V.FK_P2G = P2G.PK) AND (P2G.PLAYER_ID = " + pk + ") AND (P2G.GAME_ID = " + playerGame + ");");

		     //Pull resultSet data
		     resultSet.next();

		     if (resultSet.getInt("ELECTION") == 1)
		     {
		    	 //This player has voted in this election
		    	 voted = true;
		     }
		     //Disconnect from the database
		     connect.close();
		     statement.close();
		     resultSet.close();
		}
		
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		
		//Default is false
		return voted;
	}
	
	//This function determines if an election has been completed (***Currently just checks if everyone has voted, later add time constraint)
	//Returns True if election has been completed, False if election is ongoing
	public static boolean electionFinishedCheck(int playerGame)
	{
	    boolean finished = true;
	    Connection connect = null;
	    Statement statement;
		ResultSet resultSet;
		
		//Connect to jar file
		connection();
		
		try
		{
			 connect = dbConnection();
		     statement = (Statement) connect.createStatement();
			 //Do a sort on voting_history for this game, check the election column--if any values == 0 then election not completed
		     resultSet = statement.executeQuery("SELECT ELECTION FROM voting_history V, player_to_game P2G " 
                     + "WHERE (V.FK_P2G = P2G.PK) AND (P2G.GAME_ID = " + playerGame + ");");

		     //Cycle through resultSet data
		     while(resultSet.next())
		     {
			     if (resultSet.getInt("ELECTION") == 0)
			     {
			    	 //There are still players who need to vote in this election
			    	 finished = false;
			    	 
			    	 //Disconnect from the database
				     connect.close();
				     statement.close();
				     resultSet.close();
			    	 
			    	 return finished;
			     }
		     }
		     
		     //Disconnect from the database
		     connect.close();
		     statement.close();
		     resultSet.close();
		}
		
		catch(SQLException o)
		{
			o.printStackTrace();
		}
	    
	    
		//Default is True 
		return finished;
	}
	
	//This function returns a Vector of available games for a player to join 
	//Prereq: The games added to this vector cannot have already started
	public static Vector <Integer> getAvailableGames()
	{
		//Since the there is in an unknown number of games at program start, use a Vector
		Vector<Integer> games = new Vector<Integer>();//use games.size() to get final size of Vector	
		Connection connect = null;
		Statement statement;
		ResultSet resultSet;
		
		//Connect to jar file
		connection();
		
		try
		{
			 connect = dbConnection();
		     statement = (Statement) connect.createStatement();
		     
		     //Return all games from game list that have not started
		     resultSet = statement.executeQuery("SELECT G.PK_GAME_NUMBER from game G WHERE G.STARTED = 0;");
		     
		     //retrieve game ID's
		     while (resultSet.next()) 
			 {      				    		       			        			    	 
		    	 //Add available games to vector
		    	 games.addElement(new Integer(resultSet.getInt("PK_GAME_NUMBER")));    
			 }
		     
		     //Disconnect from the database and close statements (might be possible to hack the statements if they stay open)
		     connect.close();
		     statement.close();				 				
			 resultSet.close();		     
		}
		
			catch(SQLException o)
			{				
				o.printStackTrace();
			}
		//Return the vector of available games
		return games;
	}
	
	//Return all games this player is in
	//Returns a Vector data type
	public static Vector <Integer> getPlayerGames(int pk)
	{
		//Since the player is in an unknown number of games at program start, use a Vector
		Vector<Integer> games = new Vector<Integer>();//use games.size() to get final size of Vector	
		Connection connect = null;
		Statement statement;
		ResultSet resultSet;
		
		//Connect to jar file
		connection();
			
		try
		{
			 connect = dbConnection();
		     statement = (Statement) connect.createStatement();
		     
		     //Return player_to_game_list
		     resultSet = statement.executeQuery("SELECT P.APP_USERS_PK, P2G.PLAYER_ID, P2G.GAME_ID FROM player P, player_to_game P2G " +
		     		                               "WHERE P.APP_USERS_PK = P2G.PLAYER_ID;");
		     
		     //retrieve game ID's
		     while (resultSet.next()) 
			 {      				    		       			        			    	 
		    	    //User found database
			        if (pk == resultSet.getInt("APP_USERS_PK")) 
			        {			        			    				    
			        	//This player is already in a valid game
			        	if (resultSet.getInt("GAME_ID") > 0)//0 is default non-game value
	        			{
			        		//Set this gameID to the vector 
			        		games.addElement(new Integer(resultSet.getInt("GAME_ID")));
			        		//step the vector
	        			}			        				        	
			        }
			 }
		     
		     //Disconnect from the database and close statements (might be possible to hack the statements if they stay open)
		     connect.close();
		     statement.close();				 				
			 resultSet.close();		     
		}
		
			catch(SQLException o)
			{				
				o.printStackTrace();
			}
				
		return games;		
	}
	
	//Retrieve candidates reputation points list for this game's election for president
	//returns an array of size 10 with all RPs 
	public static int[] getCandidatesRP(int playerGame)
	{
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		int[] candidateRP = new int[10];
		int j;
		
		try{	
			connect = dbConnection();

			//collect the candidates
			statement = connect.prepareStatement("select P.REPUTATION_POINTS " + 
			                                  "FROM player P, candidates C, player_to_game P2G, game G WHERE (C.FK_PLAYER = P.APP_USERS_PK)" +
					                          " AND (P2G.PLAYER_ID = P.APP_USERS_PK) AND (P2G.GAME_ID = G.PK_GAME_NUMBER) AND (G.PK_GAME_NUMBER = " + playerGame +");");
			//Creating a variable to execute query
			result = statement.executeQuery();
			//populate the class member candidateList
			for (j = 0; j <= 9; j++)
			{
				result.next();//retrieve database data				
				//convert that data into the arrays
				candidateRP[j] = result.getInt(1);
			}
			//Disconnect from the database
			statement.close();
			connect.close();
			result.close();
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		
		return candidateRP;
	}
			
	//Retrieve the candidates list for this games election for president
	//Returns an array of size 10
	public static String[] getCandidates(int playerGame)
	{
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		String[] candidateList = new String[10];//Holds candidate list for each game
		//Connect to the database
		connection();				
		int j;
		
		try{	
			connect = dbConnection();

			//collect the candidates
			statement = connect.prepareStatement("select E.CANDIDATE " + 
			                                  "FROM election E, player P, candidates C, player_to_game P2G, game G " + 
					                          "WHERE (E.FK_CANDIDATES = C.PK) AND (C.FK_PLAYER = P.APP_USERS_PK) " +
					                          "AND (P2G.PLAYER_ID = P.APP_USERS_PK) AND (P2G.GAME_ID = G.PK_GAME_NUMBER) AND (G.PK_GAME_NUMBER = " + playerGame +");");							

			//Creating a variable to execute query
			result = statement.executeQuery();
			
			//populate the class member candidateList
			for (j = 0; j <= 9; j++)
			{
				result.next();//retrieve database data				
				//convert that data into the arrays
				candidateList[j] = result.getString(1);
			}

			//Disconnect from the database
			statement.close();
			connect.close();
			result.close();
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		
		return candidateList;
	}
	
	//Retrieve the election results for this specific game
	//Returns an array of size 10
	public static String[] getElectionResults(int playerGame)
	{
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		String[] electionResults = new String[10];
		int j;
		int votes = 0;
		//Connect to the jar file
		connection();		
						
		try{				
			//Connect to the database
			connect = dbConnection();
			
			//collect the candidates
			statement = connect.prepareStatement("select E.CANDIDATE, E.YEA from election E, game G WHERE (E.FK_GAME = G.PK_GAME_NUMBER) AND (G.PK_GAME_NUMBER = " + playerGame +") ORDER BY E.YEA DESC;");
			result = statement.executeQuery();
			
			//populate the class member candidateList
			for (j = 0; j <= 9; j ++)
			{
				result.next();//retrieve database data
				//Must concatenate into a String
				votes = result.getInt(2);
				//convert that data into the arrays
				electionResults[j] = result.getString(1) + " with " + Integer.toString(votes) + " votes";
			}
			//Disconnect from the database
			statement.close();
			connect.close();
			result.close();
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		
		return electionResults;
	}
	
	//This function sets all player roles for this game (President or Senator)
	//Call this function after an election has been completed
	//***Maybe just call this function inside an election completed boolean function in this class?
	public static void setPlayerRoles(int playerGame)
	{
		//Function variables
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		int P2G_PK;
		//Connect to the jar file
		connection();
		
		try{				
			//Connect to the database
			connect = dbConnection();
						
			//First set president for this game
			statement = connect.prepareStatement("SELECT P2G.PK, E.YEA, E.CANDIDATE from election E, player_to_game P2G, candidates C, player P WHERE " + 
					                               "(E.FK_CANDIDATES = C.PK) AND (C.FK_PLAYER= P.APP_USERS_PK) AND (P2G.PLAYER_ID = P.APP_USERS_PK) AND (E.FK_GAME = " + playerGame +") " +
					                                  "AND (C.FK_GAME = " + playerGame + ") AND (P2G.GAME_ID = " + playerGame + ") ORDER BY E.YEA DESC LIMIT 1;");
			result = statement.executeQuery();//Capture result set
			result.next();//Bring up data into cursor
			P2G_PK = result.getInt("P2G.PK");//Store the primary key for later
            //Set this player to be president
			statement = connect.prepareStatement("UPDATE player_role PR SET PR.PRESIDENT = 1 WHERE PR.FK_P2G = " + result.getInt("P2G.PK") + ";");
			statement.executeUpdate();
			
			//Next set all other players in this game to be senators, exclude the current president
			statement = connect.prepareStatement("SELECT P2G.PK from player_to_game P2G WHERE (P2G.GAME_ID = " + playerGame + ") AND (P2G.PK != " + P2G_PK  + ");");
			result = statement.executeQuery();//Capture result set
			//Set all senators
			while (result.next()) 
			{
				statement = connect.prepareStatement("UPDATE player_role PR SET PR.SENATOR = 1 WHERE PR.FK_P2G = " + result.getInt("P2G.PK") + ";");
				statement.executeUpdate();
			}
						
			//Disconnect from the database
			statement.close();
			connect.close();
			result.close();
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
	}
}	

