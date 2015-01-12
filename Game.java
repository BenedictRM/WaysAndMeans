import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;//Used for the external jar driver connection
import java.sql.ResultSet;
import java.sql.PreparedStatement;//To connect to the database
import java.sql.SQLException;
import java.util.Vector;

/**
*API Documentation (in progress)

*Function requirements for this class:
*Statement vs PreparedStatement -- in general go with Prepared Statement (strongly preferred) since it helps prevent SQL injection attacks
*Class variables: Keep out of this class to keep it like a state machine
*Parameters in this class should be wrapper datatype objects (Long, Integer, etc.) instead of primitive datatypes (long, int, etc.). Due to datatypes in sql being able to be null and primitive types in Java cannot be null
*Close DB connections in reverse order as acquired to avoid unexpected errors, this is how to close connections properly
*it's a good practice to set the database to use the Unicode character encoding UTF-8 by default in the mysql DB
*Also a good idea to create a new user 'java' to represent java code accesses, and give it a complex password
*Keep methods non-static to allow for copies of the DAO to be created by the DAOFactory i.e. one copy per thread/GUI out there
*External jar and DB mysql connections only occur through the DAOFactory
*@author Russ Mehring
*/
//DO THIS NEXT
//THEN add an error handling DAO 

//**********Consider making all updates to DB a transaction rather than automatically updating, look at http://tutorials.jenkov.com/jdbc/transaction.html
//**********Follow Ballus' DAO tutorial to round this thing out, then in the GUI (for now) create instances of the DAO to test, then add connection pool
//**********Potentially then move the DAO and DB to a server and see if you can connect directly with the GUI, then move on to implementing server class
public class Game implements GameDAO{	 
	/*					
	//Connect to sql JAR file, private to ensure only DAO has access to this data
	private static void connection()
	{
		try{
			//Accessing driver from the JAR file
			Class.forName("com.mysql.jdbc.Driver");
		}catch (ClassNotFoundException o){
			o.printStackTrace();
		}
	}
	
	//This function connects to the mysql database game, once on server we can change passwords more easily from here
	//Private to ensure only DAO has access to this data
	private static Connection dbConnection()
	{
		Connection connect = null;
		
		try{
			//zeroDateTimeBehavior=convertToNull converts 0:00:00etc. time-stamps to null preventing the system from crashing
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/game?zeroDateTimeBehavior=convertToNull", "root","Scruffy#1");
		}catch(SQLException o){
			o.printStackTrace();
		}
		
		return connect;
	}*/
	
	// Vars ---------------------------------------------------------------------------------------
    private DAOFactory daoFactory;//An instance of DAOFactory

    // Constructors -------------------------------------------------------------------------------
    /**
     * Construct a Game DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Game DAO for.
     */
    Game(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
	
	//Create the new user entry into player table in game database
	public void createNew (String pk, String username, String password)
	{
		//Declare variables
		Connection connect = null;		
		PreparedStatement statement = null;
		
		//connect to jar file
		//connection();
				
		try{
			
			//connect to the database
			//connect = dbConnection();
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			
			statement = connect.prepareStatement("INSERT INTO player(_PK_PLAYER, USERNAME, PASSWORD, REPUTATION_POINTS) VALUES (?,?,?,?);");
			statement.setString(1, pk);
			statement.setString(2, username);
			statement.setString(3, password);
			statement.setInt(4, 50);
			statement.executeUpdate();
								
			//Disconnect from the database
			statement.close();
			connect.close();		
		}
		
		catch(SQLException o){
			o.printStackTrace();
		}
	}
		
	//Create a login function to retrieve user data
	public Integer login (String username, String password)
	{
		//Declare variables
		String dbUsername = null;
		String dbPassword = null;
		Integer dbPrimarykey = 0;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection connect = null;
		
		//Connect to jar file
		//connection();
		
		try
		{			
			 //Connect to database
			 //connect = dbConnection();
			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			 			 			 
			 statement = connect.prepareStatement("select * from player;");
			 resultSet = statement.executeQuery();
			 
			 while (resultSet.next()) 
			 {      
				    //retrieve data				    				    
			        dbUsername = resultSet.getString("USERNAME");
			        dbPassword = resultSet.getString("PASSWORD");
			        
			        //User found!
			        if ((dbUsername.equals(username)) && (dbPassword.equals(password)))
			        {
			        	
			        	dbPrimarykey = resultSet.getInt("_PK_PLAYER");			     
			        	break;
			        }
			 }
			 
			//Disconnect from the database			 
			statement.close();
			resultSet.close(); 
			connect.close();
		     
			 return dbPrimarykey;
		}
	
		catch(SQLException o)
		{
			o.printStackTrace();
			return 0;
		}
	}
	
	//Delete user from player table and game database all together
	public void deleteUser (Integer pk)
	{
		//Create variables
		Connection connect = null;
		PreparedStatement statement = null;
		
		//connection();
				
		try{
			//connect to the database
			//connect = dbConnection();
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			
		    statement = connect.prepareStatement("DELETE FROM player WHERE _PK_PLAYER = " + pk);
			statement.executeUpdate();
			//Disconnect from the database			
			statement.close();	
			connect.close();
		}
		
		catch(SQLException o){
			o.printStackTrace();
		}
		System.out.println("Your profile has now been deleted from the database");
	}
	
	//This function creates a new game in the database table 'game' that users can now join and play
	//***Suggestion--let user input the 'title' of their game (include a string size check!!!) that lets them identify their game more easily
	//**Maybe also add a 'created by' field to let them see that they themselves are the 'owner' of that game
	public void createGame()
	{	
		//Declare variables
		Connection connect = null;		
		PreparedStatement statement = null;
		
		//connect to jar file
		//connection();
				
		try{
			
			//connect to the database
			//connect = dbConnection();
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			
			//Create a new game
			statement = connect.prepareStatement("INSERT INTO game (CREATED) values (null);");
			statement.executeUpdate();
								
			//Disconnect from the database			
			statement.close();
			connect.close();
		}
		
		catch(SQLException o){
			o.printStackTrace();
		}		
	}
	
	//This function is used to add a user to a game, sets their initial values across several tables
	//Prereq: No games can be called 'game 0', this will cause failures--games can be any positive integer, just increment as necessary
	public void addToGame (Integer pk, Integer playerGame)
	{
		Integer game = playerGame;//users game that they wish to join
		Integer dbpk = pk;//user's pk
		Integer PK_P2G = 0;//This will set the FK player role for this user
		Connection connect = null;
		PreparedStatement statement = null; 
		ResultSet resultSet = null;//For retrieving database data
				
		//Connect to jar file
		//connection();
												
		try
		{
			 //connect = dbConnection();
			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			 			 			 
			 //Go ahead and add them to the candidate list now, that function will sort who is worthy of president there
			 statement = connect.prepareStatement("INSERT INTO candidates (_FK_PLAYER, _FK_GAME) VALUES (?,?);");	
			 statement.setInt(1, dbpk);	
			 statement.setInt(2, game);	
			 statement.executeUpdate();
			 
			 //Set player_to_game to reflect this new game for this player
			 statement = connect.prepareStatement("INSERT INTO player_to_game (_FK_PLAYER, _FK_GAME, WHEN_JOINED) VALUES (?,?,?);");
			 statement.setInt(1, dbpk);
			 statement.setInt(2, game);
			 statement.setString(3, null);//Send a null value to trigger an update to joined timestamp			      					  	        					 	        					
			 statement.executeUpdate();	
			 
			 //Retrieve the player_to_game PK for this user to add to the player_role and voting_history tables
			 resultSet = statement.executeQuery("SELECT P2G._PK_P2G FROM player_to_game P2G WHERE (P2G._FK_PLAYER = " + dbpk + ");");
			 resultSet.next();//ADvance the resultSet cursor
			 PK_P2G = resultSet.getInt("P2G._PK_P2G");   		
			 
			 //Set voting_history for this player for this game
			 statement = connect.prepareStatement("INSERT INTO voting_history (_FK_P2G) VALUES (?);");	
			 statement.setInt(1, PK_P2G);	
			 statement.executeUpdate();
			 
			 //Set default player_role for this player for this game
			 statement = connect.prepareStatement("INSERT INTO player_role (_FK_P2G) VALUES (?);");	
			 statement.setInt(1, PK_P2G);		
			 statement.executeUpdate();
			 
			 //Disconnect from the database			
			 statement.close();
			 resultSet.close();
			 connect.close();
		}
			catch(SQLException o)
			{				
				o.printStackTrace();
			}
		
		return;
	}
	
	//This function starts a game with at least 10 players in it
	//User Story: the player who created this game can start but no-one else
	public void startGame(Integer playerGame) 
	{
		Connection connect = null;
		PreparedStatement statement = null;
		//Connect to jar file
		//connection();		
				
		try
		{
			//connect = dbConnection();//Connect to db
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			
			//Start the game that this player is currently in
			statement = connect.prepareStatement("UPDATE game G SET G.STARTED = (?) WHERE G._PK_GAME = " + playerGame + ";");
			statement.setString(1, null);//Send a null value to trigger an update (effectively starts the game)
			statement.executeUpdate();						
			
			//Disconnect from the database		    
		    statement.close();	
		    connect.close();
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}			
	}
							
	//This function will be called to add a user's response to running for president to the database
	public void candidateAdd (String ans, Integer pk)
	{
		PreparedStatement stmnt = null; 
		Connection connect = null;
		
		//Connect to jar file
		//connection();
		
		try
		{
			 //connect = dbConnection();
			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			 
			 while (!(ans.equals("Yea")) || !((ans.equals("Nay"))))
			 {
				 //Add user's response to the Yea column
				 if (ans.equals("Yea"))    			  
				 {		        					       					 
					 stmnt = connect.prepareStatement("UPDATE candidates C SET C.YEA = (?) WHERE C._FK_PLAYER = " + pk + ";");
					 stmnt.setInt(1, 1);	        					  	        					 	        					
					 stmnt.executeUpdate();
					  
					 break;
				 }
				     //Add user's response to the Nay column
				     else if (ans.equals("Nay"))
				     {
					     stmnt = connect.prepareStatement("UPDATE candidates C SET C.NAY = (?) WHERE C._FK_PLAYER = " + pk + ";");
					     stmnt.setInt(1, 1);
					     stmnt.executeUpdate();				  
					     break;
				     }				  			  
			 }
			 //Disconnect from the database			 
			 stmnt.close();	
			 connect.close();
		}
		
		catch(SQLException o)
		{
			o.printStackTrace();
		}
	}
	
	//Sets up election table, if it has already been set up then it skips setup
	public Boolean electionSetup(Integer playerGame)
	{
		Integer candidateCounter = 0;//Users who've accepted nomination
		Integer userCounter = 0;//To count user in this game in total	
		Integer FK_CANDIDATE = 0;//This will set the FK for election table referencing candidates PK
		Integer _FK_GAME = 0;
		String dbUsername = null;//Used to store each user name from candidates list
		Connection connect = null;
		PreparedStatement statement = null;
		PreparedStatement stmnt = null;
		ResultSet resultSet = null;
				
		//Connect to jar file
		//connection(); 
		
		try{
			   //connect = dbConnection();	
			 
			   //connect to the database and jar file through the DAO Factory
			   connect = daoFactory.getConnection();
			
			   //Count the number of candidates who have answered yea in this game
			   statement = connect.prepareStatement("SELECT COUNT(C.YEA) FROM candidates C, game G WHERE (C._FK_GAME = G._PK_GAME) AND (YEA = 1) AND (G._PK_GAME = " + playerGame +");");
			   resultSet = statement.executeQuery();
			   //Step through collected data
		       resultSet.next();
		       candidateCounter = resultSet.getInt(1);
		     		    		       
		       //if we have all of our candidates then populate the election table and exit
		       if(candidateCounter >= 10)
		       {
		    	   //Check if the election table has already been populated
			       resultSet = statement.executeQuery("SELECT COUNT(E.CANDIDATE) FROM election E, game G WHERE (E._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");
			       resultSet.next();
			       userCounter = resultSet.getInt(1);
		    	   
			       //Double check if this election needs an update i.e. players dropping out or whatever
			       if (userCounter < candidateCounter)
			       {
			    	    System.out.println("Entering line 458\n");
			    	    //Delete the old election and setup a new one, users may have dropped out or this is a new game
			    	    if (userCounter > 0)
			    	    {
			    	    	resultSet = statement.executeQuery("SELECT * FROM election E, game G WHERE (E._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");
			    	    	
			    	    	while(resultSet.next())
			    	    	{
			    	    		stmnt = connect.prepareStatement("DELETE FROM election USING game, election WHERE (election._FK_GAME = game._PK_GAME);");
			    	    		stmnt.executeUpdate();
			    	    	}
			    	    }
			    	   
			    	    System.out.println("Setting up election line 471"); 
			    	    resultSet = statement.executeQuery("SELECT P.USERNAME, P.REPUTATION_POINTS, C._PK_CANDIDATES, C._FK_GAME " +
				     		"FROM player P, candidates C, player_to_game P2G, game G WHERE (P._PK_PLAYER = C._FK_PLAYER) AND (C.YEA = 1) " +
				     		"AND (C._FK_GAME = G._PK_GAME) AND (P._PK_PLAYER = P2G._FK_PLAYER) AND (P2G._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +")" +
				     		" ORDER BY P.REPUTATION_POINTS, P._PK_PLAYER;");
			    	    System.out.println("Setting up election line 647"); 			    	   	    	   
						while (resultSet.next()) 
						{      					    						   										
							//retrieve data				    				    						    						      						
							dbUsername = resultSet.getString("P.USERNAME");  				
						    FK_CANDIDATE = resultSet.getInt("C._PK_CANDIDATES");
						    _FK_GAME = resultSet.getInt("C._FK_GAME");
						    stmnt = connect.prepareStatement("INSERT INTO election(CANDIDATE, _FK_CANDIDATES, _FK_GAME) VALUES (?,?,?)");
						    stmnt.setString(1, dbUsername);							
							stmnt.setInt(2, FK_CANDIDATE);
							stmnt.setInt(3, _FK_GAME);
							stmnt.executeUpdate();
						}		
						
						//Disconnect stmnt from database
						stmnt.close();
			       }			   	   			    

			       //The election table has been filled
			       return true;
		 	   }
		       
		       //Disconnect from the database		       
		       statement.close();		       
			   resultSet.close();	
			   connect.close();
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		//The election table is empty, no vote yet
		return false;
	}
	
	//This function sets player's vote for president, players votes are kept in voting_history table
	public void elect (String vote, Integer pk, Integer playerGame)
	{
		PreparedStatement statement = null;
		Connection connect = null;
		
		//Connect to jar file
		//connection(); 
		
		try
		{			
			//Connect to the DB
			//connect = dbConnection();

			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			
			//NOTE that to send a string for comparison you must encapsulate the string in quotes like: '" + value + "'
			statement = connect.prepareStatement("UPDATE election E, game G SET E.YEA = E.YEA + (?) WHERE (E.CANDIDATE = '" + vote + "') AND (E._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");
			statement.setInt(1, 1);
			statement.executeUpdate();
			
			//Update voting history table
			statement = connect.prepareStatement("UPDATE voting_history V, player_to_game P2G SET V.ELECTION = 1 WHERE (V._FK_P2G = P2G._PK_P2G) AND (P2G._FK_PLAYER = " + pk + ") AND (P2G._FK_GAME = " + playerGame + ");");		
			statement.executeUpdate();
			
			//Disconnect from the database			
			statement.close();	
			connect.close();
		}
		
		catch(SQLException o)
		{
			o.printStackTrace();
		}
	}
	
	//This function returns the number of players who are in the game the user is currently in
	public Integer inGameCount(Integer playerGame)
	{			
		Integer playersInGame = 0;	
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;	

		//Connect to jar file
		//connection();
		
		try{	
			//connect = dbConnection();
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
	        	        			
			//Count the number of candidates in this game
			statement = connect.prepareStatement("SELECT _FK_GAME FROM player_to_game WHERE _FK_GAME = " + playerGame + ";");	
			resultSet = statement.executeQuery();
			
	        while (resultSet.next())
	        {
	        	playersInGame++;
	        }
	        
			//Disconnect from the database		   
		    statement.close();
		    connect.close();
		    
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
	public Boolean inGameCheck (Integer pk)
	{
		Boolean inGame = false;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		//Connect to jar file
		//connection();
			
		try
		{
			 //connect = dbConnection();
			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			
			 statement = connect.prepareStatement("SELECT P._PK_PLAYER, P2G._FK_PLAYER, P2G._FK_GAME FROM player P, player_to_game P2G " +
                                                     "WHERE P._PK_PLAYER = P2G._FK_PLAYER;");			 
			 //Return the player_to_game table with associated players
			 resultSet = statement.executeQuery();
			 
		     //retrieve games for this player	
		     while (resultSet.next()) 
			 {      				    		       			        			    	 
		    	    //User found database
			        if (pk == resultSet.getInt("_PK_PLAYER")) 
			        {			        			    				    
			        	//This player is already in a valid game
			        	if (resultSet.getInt("_FK_GAME") > 0)
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
		     statement.close();				 				
			 resultSet.close();		
			 connect.close();
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
	public Boolean gameStartedCheck(Integer playerGame)
	{
		Date startTime = null;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		//Connect to jar file
		//connection();
		try
		{
			//connect = dbConnection();//Connect to db	
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			
			statement = connect.prepareStatement("SELECT G.STARTED FROM game G WHERE G._PK_GAME = " + playerGame + ";");//Create the statement connection to the DB	
			resultSet = statement.executeQuery();
			
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
			    statement.close();				 				
				resultSet.close();
				connect.close();
				
				return false;
			}
			    //This game has started
				else
				{
					//Disconnect from the database					
				    statement.close();				 				
					resultSet.close();
					connect.close();
					
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
	public Boolean candidateCheck (Integer pk, Integer playerGame)
	{
		//Sort the player list, if user is in top ten for rp's and has not replied y or n extend offer
		Integer RP = 0;
		Integer candidateCounter = 0;//Users who've accepted nomination
		Integer answeredCounter = 0;//Users who've accepted or declined nomination
		Integer userCounter = 0;//To count user in this game in total
		Boolean answered = false;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
						
		//Connect to jar file
		//connection();
		
		try
		{
			 //connect = dbConnection();
			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			
			 //Count the number of candidates in this game
			 statement = connect.prepareStatement("SELECT COUNT(C.YEA) FROM candidates C, " 
                                                     + "game G WHERE (C.YEA = 1) AND (C._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");			 
			 //Set data in DAO
			 resultSet = statement.executeQuery();
			 //Advance cursor through data (i.e. retrieve it)
		     resultSet.next();
		     candidateCounter = resultSet.getInt(1);
		     
		     //Count the number of users in this game who have answered yea or nay
		     resultSet = statement.executeQuery("SELECT COUNT(C._PK_CANDIDATES) FROM candidates C, " +
                      "game G WHERE (C._FK_GAME = G._PK_GAME) AND (C.YEA = 1 OR NAY = 1) AND (G._PK_GAME = " + playerGame +");");
             //ADvance cursor to retrieve data stored in resultSet
		     resultSet.next();
		     answeredCounter = resultSet.getInt(1);
		     
		     //Count the number of users in this game's player database
		     //Count the number of users in this games player database
		     resultSet = statement.executeQuery("SELECT COUNT(P.USERNAME) FROM player P, player_to_game P2G, game G WHERE" +
		     		                             " (P._PK_PLAYER = P2G._FK_PLAYER) AND (P2G._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");
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
			     resultSet = statement.executeQuery("SELECT P._PK_PLAYER, P.REPUTATION_POINTS, C.YEA, C.NAY " +
			     		"FROM player P, candidates C, game G WHERE (P._PK_PLAYER = C._FK_PLAYER) AND (C.YEA = 0 AND C.NAY = 0) AND (C._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +") "  +
			     		"ORDER BY P.REPUTATION_POINTS, P._PK_PLAYER LIMIT 10;");
			     
			     //retrieve rp's FOR THIS USER	
			     while (resultSet.next()) 
				 {      				    		       			        			    	 
			    	    //User found in top ten!
				        if (pk == resultSet.getInt("_PK_PLAYER")) 
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
			    	  resultSet = statement.executeQuery("select * from candidates C, game G WHERE (C._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame + ");");			          
			    	  //Check to see if this user has already decided to accept or decline nomination
			          while (resultSet.next())
			          {			        	  			        	 
			        	  if (pk == resultSet.getInt("_FK_PLAYER"))
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
			     statement.close();				 				
				 resultSet.close();
				 connect.close();
				 
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
	public Boolean userNameCheck(String i)
	{
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		//Connect to jar file
		//connection();
		
		try{
			//connect = dbConnection();
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
	 	    
			//Retrieve username's for checking uniqueness 
			statement = connect.prepareStatement("SELECT USERNAME FROM player;");
			resultSet = statement.executeQuery();
			
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
		    statement.close();
		    connect.close();
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
	public Boolean electionVoteCheck(Integer pk, Integer playerGame)
	{
		Boolean voted = false;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		//Connect to jar file
		//connection();
		
		try
		{
			 //Connect to DB
			 //connect = dbConnection();	
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			
			 //Pull this player from voting_history
			 statement = connect.prepareStatement("SELECT ELECTION FROM voting_history V, player_to_game P2G " 
                     + "WHERE (V._FK_P2G = P2G._PK_P2G) AND (P2G._FK_PLAYER = " + pk + ") AND (P2G._FK_GAME = " + playerGame + ");");
			 //Set data into resultset
			 resultSet = statement.executeQuery();		     
		     //Pull resultSet data by advancing cursor
		     resultSet.next();

		     if (resultSet.getInt("ELECTION") == 1)
		     {
		    	 //This player has voted in this election
		    	 voted = true;
		     }
		     //Disconnect from the database		     
		     statement.close();
		     resultSet.close();
		     connect.close();
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
	public Boolean electionFinishedCheck(Integer playerGame)
	{
	    Boolean finished = true;
	    Connection connect = null;
	    PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		//Connect to jar file
		//connection();
		
		try
		{
			 //connect = dbConnection();
			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			
			 //Do a sort on voting_history for this game, check the election column--if any values == 0 then election not completed
			 statement = connect.prepareStatement("SELECT ELECTION FROM voting_history V, player_to_game P2G " 
                     + "WHERE (V._FK_P2G = P2G._PK_P2G) AND (P2G._FK_GAME = " + playerGame + ");");
			 //Store resultSet data
			 resultSet = statement.executeQuery();
			 
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
		     statement.close();
		     resultSet.close();
		     connect.close();
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
	public Vector <Integer> getAvailableGames()
	{
		//Since the there is in an unknown number of games at program start, use a Vector
		Vector<Integer> games = new Vector<Integer>();//use games.size() to get final size of Vector	
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		//Connect to jar file
		//connection();
		
		try
		{
			 //connect = dbConnection();
			 					
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			
			 //Return all games from game list that have not started
		     statement = connect.prepareStatement("SELECT G._PK_GAME from game G WHERE G.STARTED = 0;");
		     resultSet = statement.executeQuery();
		     		   
		     
		     //retrieve game ID's
		     while (resultSet.next()) 
			 {      				    		       			        			    	 
		    	 //Add available games to vector
		    	 games.addElement(new Integer(resultSet.getInt("_PK_GAME")));    
			 }
		     
		     //Disconnect from the database and close statements (might be possible to hack the statements if they stay open)		     
		     statement.close();				 				
			 resultSet.close();	
			 connect.close();
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
	public Vector <Integer> getPlayerGames(Integer pk)
	{
		//Since the player is in an unknown number of games at program start, use a Vector
		Vector<Integer> games = new Vector<Integer>();//use games.size() to get final size of Vector	
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		//Connect to jar file
		//connection();
			
		try
		{
			 //Connect to DB
			 //connect = dbConnection();
			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection(); 
			
			 //Return player_to_game_list						 
			 statement = connect.prepareStatement("SELECT P._PK_PLAYER, P2G._FK_PLAYER, P2G._FK_GAME FROM player P, player_to_game P2G " +
                                                     "WHERE P._PK_PLAYER = P2G._FK_PLAYER;");
			 //Store retrieved data
			 resultSet = statement.executeQuery();
			 
		     //retrieve game ID's
		     while (resultSet.next()) 
			 {      				    		       			        			    	 
		    	    //User found database
			        if (pk == resultSet.getInt("_PK_PLAYER")) 
			        {			        			    				    
			        	//This player is already in a valid game
			        	if (resultSet.getInt("_FK_GAME") > 0)//0 is default non-game value
	        			{
			        		//Set this gameID to the vector 
			        		games.addElement(new Integer(resultSet.getInt("_FK_GAME")));
			        		//step the vector
	        			}			        				        	
			        }
			 }
		     
		     //Disconnect from the database and close statements (might be possible to hack the statements if they stay open)		     
		     statement.close();				 				
			 resultSet.close();		
			 connect.close();
		}
		
			catch(SQLException o)
			{				
				o.printStackTrace();
			}
				
		return games;		
	}
	
	//Retrieve candidates reputation points list for this game's election for president
	//returns an array of size 10 with all RPs 
	public Integer[] getCandidatesRP(Integer playerGame)
	{
		Integer j = 0;
		Integer[] candidateRP = new Integer[10];	
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet result = null;		
		
		//Connect to jar file
		//connection();
		
		try{	
			//Connect to DB
			//connect = dbConnection();
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			
			//collect the candidates
			statement = connect.prepareStatement("select P.REPUTATION_POINTS " + 
			                                  "FROM player P, candidates C, player_to_game P2G, game G WHERE (C._FK_PLAYER = P._PK_PLAYER)" +
					                          " AND (P2G._FK_PLAYER = P._PK_PLAYER) AND (P2G._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");
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
			result.close();
			connect.close();
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		
		return candidateRP;
	}
			
	//Retrieve the candidates list for this games election for president
	//Returns an array of size 10
	public String[] getCandidates(Integer playerGame)
	{
		int j;
		String[] candidateList = new String[10];//Holds candidate list for each game
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		
		//Connect to the database
		//connection();				
				
		try{	
			//connect = dbConnection();
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();

			//collect the candidates
			statement = connect.prepareStatement("select E.CANDIDATE " + 
			                                  "FROM election E, player P, candidates C, player_to_game P2G, game G " + 
					                          "WHERE (E._FK_CANDIDATES = C._PK_CANDIDATES) AND (C._FK_PLAYER = P._PK_PLAYER) " +
					                          "AND (P2G._FK_PLAYER = P._PK_PLAYER) AND (P2G._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");							

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
			result.close();
			connect.close();
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		
		return candidateList;
	}
	
	//Retrieve the election results for this specific game
	//Returns an array of size 10
	public String[] getElectionResults(Integer playerGame)
	{
		int j = 0;
		Integer votes = 0;
		String[] electionResults = new String[10];
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;	
		
		//Connect to the jar file
		//connection();		
						
		try{				
			//Connect to the database
			//connect = dbConnection();
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			
			//collect the candidates
			statement = connect.prepareStatement("select E.CANDIDATE, E.YEA from election E, game G WHERE (E._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +") ORDER BY E.YEA DESC;");
			resultSet = statement.executeQuery();
			
			//populate the class member candidateList
			for (j = 0; j <= 9; j ++)
			{
				resultSet.next();//retrieve database data
				//Must concatenate into a String
				votes = resultSet.getInt(2);
				//convert that data into the arrays
				electionResults[j] = resultSet.getString(1) + " with " + Integer.toString(votes) + " votes";
			}
			//Disconnect from the database
			statement.close();			
			resultSet.close();
			connect.close();
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		
		return electionResults;
	}
	
	//This function returns the election winner for this game
	public String getElectionWinner(Integer playerGame)
	{
		String winner = null;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;	
		
		//Connect to the jar file
		//connection();		
						
		try{				
			//Connect to the database
			//connect = dbConnection();
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			
			//collect the candidates
			statement = connect.prepareStatement("select E.CANDIDATE, E.YEA from election E, game G WHERE (E._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +") ORDER BY E.YEA DESC LIMIT 1;");
			resultSet = statement.executeQuery();
			//Advance the result cursor
			resultSet.next();
			winner = resultSet.getString(1);
			
			//Disconnect from the database
			statement.close();			
			resultSet.close();
			connect.close();
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
		
		return winner;
	}
	
	//This function sets all player roles for this game (President or Senator)
	//Call this function after an election has been completed
	//***Maybe just call this function inside an election completed boolean function in this class?
	public void setPlayerRoles(Integer playerGame)
	{
		//Function variables
		Integer P2G_PK = 0;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		
		//Connect to the jar file
		//connection();
		
		try{				
			//Connect to the database
			//connect = dbConnection();
			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
						
			//First set president for this game
			statement = connect.prepareStatement("SELECT P2G._PK_P2G, E.YEA, E.CANDIDATE from election E, player_to_game P2G, candidates C, player P WHERE " + 
					                               "(E._FK_CANDIDATES = C._PK_CANDIDATES) AND (C._FK_PLAYER= P._PK_PLAYER) AND (P2G._FK_PLAYER = P._PK_PLAYER) AND (E._FK_GAME = " + playerGame +") " +
					                                  "AND (C._FK_GAME = " + playerGame + ") AND (P2G._FK_GAME = " + playerGame + ") ORDER BY E.YEA DESC LIMIT 1;");
			result = statement.executeQuery();//Capture result set
			result.next();//Bring up data into cursor
			P2G_PK = result.getInt("P2G._PK_P2G");//Store the primary key for later
            //Set this player to be president
			statement = connect.prepareStatement("UPDATE player_role PR SET PR.PRESIDENT = 1 WHERE PR._FK_P2G = " + result.getInt("P2G._PK_P2G") + ";");
			statement.executeUpdate();
			
			//Next set all other players in this game to be senators, exclude the current president
			statement = connect.prepareStatement("SELECT P2G._PK_P2G from player_to_game P2G WHERE (P2G._FK_GAME = " + playerGame + ") AND (P2G._PK_P2G != " + P2G_PK  + ");");
			result = statement.executeQuery();//Capture result set
			//Set all senators
			while (result.next()) 
			{
				statement = connect.prepareStatement("UPDATE player_role PR SET PR.SENATOR = 1 WHERE PR._FK_P2G = " + result.getInt("P2G._PK_P2G") + ";");
				statement.executeUpdate();
			}
						
			//Disconnect from the database
			statement.close();			
			result.close();
			connect.close();
		}
		catch(SQLException o)
		{
			o.printStackTrace();
		}
	}
}	

