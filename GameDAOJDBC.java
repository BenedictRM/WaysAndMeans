import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.PreparedStatement;//To connect to the database
import java.sql.SQLException;
import java.util.Vector;

/**
*API Documentation (in progress)

*Function requirements for this class:
*Statement vs PreparedStatement -- ALWAYS go with PreparedStatement (strongly preferred) since it helps prevent SQL injection attacks
*Class variables: Keep out of this class to keep it like a state machine
*Parameters in this class should be wrapper datatype objects (Long, Integer, etc.) instead of primitive datatypes (long, int, etc.). Due to datatypes in sql being able to be null and primitive types in Java cannot be null
*Close DB connections in reverse order as acquired to avoid unexpected errors, this is how to close connections properly
*it's a good practice to set the database to use the Unicode character encoding UTF-8 by default in the mysql DB
*Also a good idea to create a new user 'java' to represent java code accesses, and give it a complex password
*Keep methods non-static to allow for copies of the DAO to be created by the DAOFactory i.e. one copy per thread/GUI out there
*External jar and DB mysql connections only occur through the DAOFactory
*Close all connections in a 'finally' block after all catch block, close connections through DAO, close resultSets and statements as well in this class
*Connections to MySQL jar file and MySQL DB now occur within the DAOFactory.  Any security changes happen there and inside the dao.properties file
*All accesses/updates to the database happen as 'transactions' following the pattern in http://tutorials.jenkov.com/jdbc/transaction.html
*@author Russ Mehring
*/

/**
 * FUTURE TESTING IDEAS
 * 1) if have 10 candidates, and 11 users, if one CADIDATE deletes profile, will candidate pool update? 
 * 2) probably should try to have driver prog's that run unit tests
 */
//DO THIS NEXT
//Connection pool-- included with Apache Tomcat, implement there

//**********Potentially then move the DAO and DB to a server and see if you can connect directly with the GUI, then move on to implementing server class
public class GameDAOJDBC implements GameDAO{	 
	//Connections to MySQL jar file and MySQL DB now occur within the DAOFactory.  Any security changes happen there and inside the dao.properties file
	
	// Vars ---------------------------------------------------------------------------------------
    private DAOFactory daoFactory;//An instance of DAOFactory

    // Constructors -------------------------------------------------------------------------------
    /**
     * Construct a Game DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this Game DAO for.
     */
    GameDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------   
	/**
	 * Create the new user entry into player table in game database
	 */
	public void createNew (String pk, String username, String password) throws DAOException
	{
		//Declare variables
		Connection connect = null;		
		PreparedStatement statement = null;
		
		try
		{			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			//Create a transaction
			connect.setAutoCommit(false);
						
			statement = connect.prepareStatement("INSERT INTO player(_PK_PLAYER, USERNAME, PASSWORD, REPUTATION_POINTS) VALUES (?,?,?,?);");
			statement.setString(1, pk);
			statement.setString(2, username);
			statement.setString(3, password);
			statement.setInt(4, 50);
			statement.executeUpdate();		
			
			//Commit all changes as a transaction
			connect.commit();
		}
					
			catch(SQLException o)
			{
				try 
				{
					//try collecting data again
					connect.rollback();
				} 
				
					catch (SQLException e) 
					{
						throw new DAOException(e);
					}			
			}
				
		//Finally blocks ALWAYS execute (even if try block exited abnormally)
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				statement.close();
				daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}				
	}
		
	//Create a login function to retrieve user data
	public Integer login (String username, String password) throws DAOException
	{
		//Declare variables
		String dbUsername = null;
		String dbPassword = null;
		Integer dbPrimarykey = 0;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try
		{						
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			 //Create transaction
			 connect.setAutoCommit(false);
			 			 			 
			 statement = connect.prepareStatement("select * from player;");
			 resultSet = statement.executeQuery();
			 //commit data as transaction
			 connect.commit();
			 
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
		}
	
			catch(SQLException o)
			{
				try
				{
					//try collecting data again
					connect.rollback();
				}
				
					catch(SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				resultSet.close();
				statement.close();	
				//Not sure if this is a good idea or not, would this hit a critical section and cause race conditions?
				daoFactory.closeConnection(connect);	
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
		return dbPrimarykey;
	}
	
	//Delete user from player table and game database all together
	public void deleteUser (Integer pk) throws DAOException
	{
		//Create variables
		Connection connect = null;
		PreparedStatement statement = null;	
				
		try
		{			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			//create transaction
			connect.setAutoCommit(false);
			
		    statement = connect.prepareStatement("DELETE FROM player WHERE _PK_PLAYER = " + pk);
			statement.executeUpdate();	
			//commit data as transaction
			connect.commit();
		}
		
			catch(SQLException o)
			{
				try
				{
					//try collecting data again
					connect.rollback();
				}
				
					catch(SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				statement.close();	
				daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		System.out.println("Your profile has now been deleted from the database");
	}
	
	//This function creates a new game in the database table 'game' that users can now join and play
	//***Suggestion--let user input the 'title' of their game (include a string size check!!!) that lets them identify their game more easily
	//**Maybe also add a 'created by' field to let them see that they themselves are the 'owner' of that game
	public void createGame() throws DAOException
	{	
		//Declare variables
		Connection connect = null;		
		PreparedStatement statement = null;
						
		try
		{					
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();		
			//Create a transaction
			connect.setAutoCommit(false);
			
			//Create a new game
			statement = connect.prepareStatement("INSERT INTO game (CREATED) values (null);");
			statement.executeUpdate();
			
			//commit data as transaction
			connect.commit();
		}
		
			catch(SQLException o)
			{
				try
				{
					//try collecting data again
					connect.rollback();
				}
					catch(SQLException e)
					{
					    throw new DAOException(e);
					}
			}	
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				statement.close();	
				daoFactory.closeConnection(connect);	
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
	}
	
	//This function is used to add a user to a game, sets their initial values across several tables
	//Prereq: No games can be called 'game 0', this will cause failures--games can be any positive integer, just increment as necessary
	public void addToGame (Integer pk, Integer playerGame) throws DAOException
	{
		Integer game = playerGame;//users game that they wish to join
		Integer dbpk = pk;//user's pk
		Integer PK_P2G = 0;//This will set the FK player role for this user
		Connection connect = null;
		PreparedStatement statement1 = null; 
		PreparedStatement statement2 = null; 
		PreparedStatement statement3 = null; 
		PreparedStatement statement4 = null; 
		ResultSet resultSet = null;//For retrieving database data
																
		try
		{			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			 //create transaction
			 connect.setAutoCommit(false);
			 //Go ahead and add them to the candidate list now, that function will sort who is worthy of president there
			 statement1 = connect.prepareStatement("INSERT INTO candidates (_FK_PLAYER, _FK_GAME) VALUES (?,?);");	
			 statement1.setInt(1, dbpk);	
			 statement1.setInt(2, game);	
			 statement1.executeUpdate();			
			 
			 //Set player_to_game to reflect this new game for this player
			 statement2 = connect.prepareStatement("INSERT INTO player_to_game (_FK_PLAYER, _FK_GAME, WHEN_JOINED) VALUES (?,?,?);");
			 statement2.setInt(1, dbpk);
			 statement2.setInt(2, game);
			 statement2.setString(3, null);//Send a null value to trigger an update to joined timestamp			      					  	        					 	        					
			 statement2.executeUpdate();				 
			 
			 //Retrieve the player_to_game PK for this user to add to the player_role and voting_history tables
			 resultSet = statement2.executeQuery("SELECT P2G._PK_P2G FROM player_to_game P2G WHERE (P2G._FK_PLAYER = " + dbpk + ") ORDER BY P2G._PK_P2G DESC;");
			 resultSet.next();//Advance the resultSet cursor
			 //Should now be able to store the most recent game this player is in
			 PK_P2G = resultSet.getInt("P2G._PK_P2G");   		
			 
			 //Set voting_history for this player for this game
			 statement3 = connect.prepareStatement("INSERT INTO voting_history (_FK_P2G) VALUES (?);");	
			 statement3.setInt(1, PK_P2G);	
			 statement3.executeUpdate();
			 
			 //Set default player_role for this player for this game
			 statement4 = connect.prepareStatement("INSERT INTO player_role (_FK_P2G) VALUES (?);");	
			 statement4.setInt(1, PK_P2G);		
			 statement4.executeUpdate();
			 
			 //commit data as transaction
			 connect.commit();
		}
			catch(SQLException o)
			{		
				try
				{
					//try collecting data again
					connect.rollback();
				}
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close();
				 statement1.close();	
				 statement2.close();
				 statement3.close();
				 statement4.close();
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
		return;
	}
	
	//This function starts a game with at least 10 players in it
	//User Story: the player who created this game can start but no-one else
	public void startGame(Integer playerGame) throws DAOException
	{
		Connection connect = null;
		PreparedStatement statement = null;
		
		try
		{		
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			//Create transaction
			connect.setAutoCommit(false);
			//Start the game that this player is currently in
			statement = connect.prepareStatement("UPDATE game G SET G.STARTED = (?) WHERE G._PK_GAME = " + playerGame + ";");
			statement.setString(1, null);//Send a null value to trigger an update (effectively starts the game)
			statement.executeUpdate();
			//commit data as transaction
			connect.commit();
		}
			catch(SQLException o)
			{
				try
				{
					//try collecting data again
					connect.rollback();
				}
					catch (SQLException e)
					{
					    throw new DAOException(o);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 statement.close();
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
	}
							
	//This function will be called to add a user's response to running for president to the database
	public void candidateAdd (String ans, Integer pk, Integer playerGame) throws DAOException
	{
		Connection connect = null;
		PreparedStatement stmnt = null; 		
				
		try
		{		
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			 //create transaction
			 connect.setAutoCommit(false);
			 while (!(ans.equals("Yea")) || !((ans.equals("Nay"))))
			 {
				 //Add user's response to the Yea column
				 if (ans.equals("Yea"))    			  
				 {		        					       					 
					 stmnt = connect.prepareStatement("UPDATE candidates C SET C.YEA = (?) WHERE (C._FK_PLAYER = " + pk + ") AND (C._FK_GAME = " + playerGame + ");");
					 stmnt.setInt(1, 1);	        					  	        					 	        					
					 stmnt.executeUpdate();
					  
					 break;
				 }
				     //Add user's response to the Nay column
				     else if (ans.equals("Nay"))
				     {
					     stmnt = connect.prepareStatement("UPDATE candidates C SET C.NAY = (?) WHERE (C._FK_PLAYER = " + pk + ") AND (C._FK_GAME = " + playerGame + ");");
					     stmnt.setInt(1, 1);
					     stmnt.executeUpdate();				  
					     break;
				     }				  			  
			 }
			 
			 //commit data as transaction
			 connect.commit();
		}
		
			catch(SQLException o)
			{
				try
				{
					//try collecting data again
					connect.rollback();
				}
					catch(SQLException e)
					{
					    throw new DAOException(o);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 stmnt.close();
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
	}
	
	//Sets up election table, if it has already been set up then it skips setup
	//****Maybe only return t/f if setup, then have separate function that actually sets up the election rather than doing everything here
	public Boolean electionSetup(Integer playerGame) throws DAOException
	{
		Boolean setup = false; //Default
		Integer candidateCounter = 0;//Users who've accepted nomination
		Integer userCounter = 0;//To count user in this game in total	
		Integer FK_CANDIDATE = 0;//This will set the FK for election table referencing candidates PK
		Integer _FK_GAME = 0;
		String dbUsername = null;//Used to store each user name from candidates list
		Connection connect = null;
		PreparedStatement statement = null;
		PreparedStatement stmnt = null;
		ResultSet resultSet = null;
						
		try{			 
			   //connect to the database and jar file through the DAO Factory
			   connect = daoFactory.getConnection();
			   //create transaction
			   connect.setAutoCommit(false);
			   
			   //Count the number of candidates who have answered yea in this game
			   statement = connect.prepareStatement("SELECT COUNT(C.YEA) FROM candidates C, game G WHERE (C._FK_GAME = G._PK_GAME) AND (YEA = 1) AND (G._PK_GAME = " + playerGame +");");
			   resultSet = statement.executeQuery();			   
			   //Step through collected data
		       resultSet.next();
		       candidateCounter = resultSet.getInt(1);
		     		    		       
		       //if we have all of our candidates then populate the election table and exit
		       if(candidateCounter >= 10)
		       {
		    	   resultSet.close();//Reset the resultSet connection for new query
		    	   //Check if the election table has already been populated
			       resultSet = statement.executeQuery("SELECT COUNT(E.CANDIDATE) FROM election E, game G WHERE (E._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");
			       //Advance cursor
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
			       }			   	   			    
                   
			       connect.commit();
			       setup = true;
		 	   }
		}
			catch(SQLException o)
			{			
				try
				{
					connect.rollback();
				}
					catch(SQLException e)
					{
					    throw new DAOException(e);
					}					
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{				 				 				 
				 if (stmnt != null)//Possible that stmnt will never make a connection to the DB
				 {	 
				      stmnt.close();
				 }
				 resultSet.close();
				 statement.close();
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
		//The election table is empty, no vote yet
		return setup;
	}
	
	//This function sets player's vote for president, players votes are kept in voting_history table
	public void elect (String vote, Integer pk, Integer playerGame) throws DAOException
	{
		Connection connect = null;
		PreparedStatement statement1 = null;
		PreparedStatement statement2 = null;	
			
		try
		{			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			//Create transaction
			connect.setAutoCommit(false);
			
			//NOTE that to send a string for comparison you must encapsulate the string in quotes like: '" + value + "'
			statement1 = connect.prepareStatement("UPDATE election E, game G SET E.YEA = E.YEA + (?) WHERE (E.CANDIDATE = '" + vote + "') AND (E._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");
			statement1.setInt(1, 1);
			statement1.executeUpdate();
			
			//Update voting history table
			statement2 = connect.prepareStatement("UPDATE voting_history V, player_to_game P2G SET V.ELECTION = 1 WHERE (V._FK_P2G = P2G._PK_P2G) AND (P2G._FK_PLAYER = " + pk + ") AND (P2G._FK_GAME = " + playerGame + ");");		
			statement2.executeUpdate();
			//commit data
			connect.commit();
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}
				
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 statement1.close();
				 statement2.close();
				 daoFactory.closeConnection(connect);	
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
	}
	
	//This function returns the number of players who are in the game the user is currently in
	public Integer inGameCount(Integer playerGame) throws DAOException
	{			
		Integer playersInGame = 0;	
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;	
		
		try
		{				
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
	        //Create transaction
			connect.setAutoCommit(false);
			
			//Count the number of candidates in this game
			statement = connect.prepareStatement("SELECT _FK_GAME FROM player_to_game WHERE _FK_GAME = " + playerGame + ";");	
			resultSet = statement.executeQuery();
			//commit data
	        connect.commit();
	        
	        while (resultSet.next())
	        {
	        	playersInGame++;
	        }	        		   	        		
	    }		
			
		    //Catch any errors	
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}
					catch(SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close(); 
				 statement.close();	
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
	    return playersInGame;
	}
	
	//This function is used to check if a logged in player is in a game, if not ask them to join a game
	public Boolean inGameCheck (Integer pk) throws DAOException
	{
		Boolean inGame = false;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
				
		try
		{			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			 //Create transaction
			 connect.setAutoCommit(false);
			 
			 statement = connect.prepareStatement("SELECT P._PK_PLAYER, P2G._FK_PLAYER, P2G._FK_GAME FROM player P, player_to_game P2G " +
                                                     "WHERE P._PK_PLAYER = P2G._FK_PLAYER;");			 
			 //Return the player_to_game table with associated players
			 resultSet = statement.executeQuery();
			 //commit data
		     connect.commit();
		     
		     //retrieve games for this player	
		     while (resultSet.next()) 
			 {      				    		       			        			    	 
		    	    //User found database
			        if (pk == resultSet.getInt("_PK_PLAYER")) 
			        {			        			    				    
			        	//This player is already in a valid game
			        	if (resultSet.getInt("_FK_GAME") > 0)
	        			{
		       			    //User in a game
		       			    inGame = true;
		       			    break;
	        			}
				        	//This player is not in a game
			        		else
				        	{
			        			//User not found or user not in game
			       			    inGame = false;		        		
				        	}			        	
			        }
			 }		     
		}
		
			catch(SQLException o)
			{	
				try
				{
					connect.rollback();
				}
					catch(SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close();
				 statement.close();	
				 daoFactory.closeConnection(connect);//Compiler not smart enough to see other class that closes connect
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		//Player not found, can't be in game (this will be the default return)
		return inGame;
	}
	
	//Check to see if the game this player is actively in has started
	//***May want to pass in an argument later that will let all games that have started be displayed, not just the one this user is in?
	public Boolean gameStartedCheck(Integer playerGame) throws DAOException
	{
		Boolean started = false;
		Date startTime = null;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			//create transaction
			connect.setAutoCommit(false);
			
			statement = connect.prepareStatement("SELECT G.STARTED FROM game G WHERE G._PK_GAME = " + playerGame + ";");//Create the statement connection to the DB	
			resultSet = statement.executeQuery();
			//commit data to pull in result
			connect.commit();
			
			//Advance the resultSet, if true retrieve the date, otherwise it will be false on program start returning empty set
			//This if statement should block that from happening
			if (resultSet.next() == true)
			{
				startTime = resultSet.getDate(1);
			}
									
			//This game has not started
			if (startTime == null)
			{
				started = false;
			}
			    //This game has started
				else
				{
					started = true;
				}
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}
					catch(SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close(); 
				 statement.close();	
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
		return started;
	}
	
	//while candidate list <= 10 and user rp >= to top candidates and user has not already replied nay to running return true else false
	public Boolean candidateCheck (Integer pk, Integer playerGame) throws DAOException
	{
		//Sort the player list, if user is in top ten for rp's and has not replied y or n extend offer
		Integer RP = 0;
		Integer candidateCounter = 0;//Users who've accepted nomination
		Integer answeredCounter = 0;//Users who've accepted or declined nomination
		Integer userCounter = 0;//To count user in this game in total
		Boolean answered = false;
		Boolean candidate = false;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		ResultSet resultSet4 = null;
		ResultSet resultSet5 = null;						
		
		try
		{			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			 //create transaction
			 connect.setAutoCommit(false);
			 
			 //Count the number of candidates in this game
			 statement = connect.prepareStatement("SELECT COUNT(C.YEA) FROM candidates C, " 
                                                     + "game G WHERE (C.YEA = 1) AND (C._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");			 
			 //Set data in DAO
			 resultSet1 = statement.executeQuery();
			 //Advance cursor through data (i.e. retrieve it)
		     resultSet1.next();
		     candidateCounter = resultSet1.getInt(1);
		     
		     //Count the number of users in this game who have answered yea or nay
		     resultSet2 = statement.executeQuery("SELECT COUNT(C._PK_CANDIDATES) FROM candidates C, " +
                      "game G WHERE (C._FK_GAME = G._PK_GAME) AND (C.YEA = 1 OR NAY = 1) AND (G._PK_GAME = " + playerGame +");");
             //Advance cursor to retrieve data stored in resultSet
		     resultSet2.next();
		     answeredCounter = resultSet2.getInt(1);
		     
		     //Count the number of users in this game's player database
		     //Count the number of users in this games player database
		     resultSet3 = statement.executeQuery("SELECT COUNT(P.USERNAME) FROM player P, player_to_game P2G, game G WHERE" +
		     		                             " (P._PK_PLAYER = P2G._FK_PLAYER) AND (P2G._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");
		     resultSet3.next();
		     userCounter = resultSet3.getInt(1);
		     
		     System.out.println("This game has " + candidateCounter + " viable candidates for president");
		     System.out.println("This game has " + answeredCounter + " candidates who've accepted or declined nomination");
		     System.out.println("This game has " + userCounter + " users in game");
		    
		     //***If candidate pool already 10 viable candidates (***need to add ->) or if all users have answered then skip all this***
		     if (candidateCounter < 10)
		     {
		    	 System.out.println("ENTERING line 497\n");
		    	 //Return the top ten users by rep points and FIFO join time from the same game
			     resultSet4 = statement.executeQuery("SELECT P._PK_PLAYER, P.REPUTATION_POINTS, C.YEA, C.NAY " +
			     		"FROM player P, candidates C, game G WHERE (P._PK_PLAYER = C._FK_PLAYER) AND (C.YEA = 0 AND C.NAY = 0) AND (C._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +") "  +
			     		"ORDER BY P.REPUTATION_POINTS, P._PK_PLAYER LIMIT 10;");
			     
			     //retrieve rp's FOR THIS USER	
			     while (resultSet4.next()) 
				 {      				    		       			        			    	 
			    	    //User found in top ten!
				        if (pk == resultSet4.getInt("_PK_PLAYER")) 
				        {			        			    				    
				        	//This user was found in the top ten
				        	RP = resultSet4.getInt("REPUTATION_POINTS");						       
				        	break;
				        }
				 }
			     
			     //Now check to see if this user has already entered the candidates table with a yea or nay, if not then invite them to do so
			     //If RP is still 0 then this user wasn't found in the top ten qualified candidates and this code will be skipped
			     if (RP > 0)
			     {	    	 
			    	  resultSet5 = statement.executeQuery("select * from candidates C, game G WHERE (C._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame + ");");			          
			    	  //Check to see if this user has already decided to accept or decline nomination
			          while (resultSet5.next())
			          {			        	  			        	 
			        	  if (pk == resultSet5.getInt("_FK_PLAYER"))
			        	  {
			        		  if (resultSet5.getInt("Yea") == 1 || resultSet5.getInt("Nay") == 1)
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
	        	    	  candidate = true;
	        		  }		        	  		          		        	  
			     }			     		    		     				 
			  }	
		      //commit changes
		      connect.commit();
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}
					catch(SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet1.close();
				 resultSet2.close();
				 resultSet3.close();
				 //possible for resultSet4 to be null
				 if(resultSet4 != null)
				 {	 
				     resultSet4.close();
				 }
					 //possible for resultSet5 to be null
					 if(resultSet5 != null)
					 {	 
					     resultSet5.close();
					 }			 				 
				 statement.close();	
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		//Default return, don't risk adding candidates in case an error happens that sends the code here
		return candidate;
	}
	
	//This function is used to check a new user's userName against the database to make sure their name is unique
	public Boolean userNameCheck(String user) throws DAOException
	{
		Boolean check = false;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
				
		try{			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
	 	    //Create transaction
			connect.setAutoCommit(false);
			
			//Retrieve username's for checking uniqueness 
			statement = connect.prepareStatement("SELECT USERNAME FROM player;");
			resultSet = statement.executeQuery();
			
			//Commit query
			connect.commit();
			
	        //Check the database user names against this user name
	        while (resultSet.next())
	        {
	        	if(resultSet.getString(1).equals(user))
	        	{	
	        		check = true;
	        		break;
	        	}
	        }	        
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}			
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close(); 
				 statement.close();	
				 daoFactory.closeConnection(connect);//Causes leak warnings on connect variable
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
		return check;	
	}
	
	//This function determines if a player has already cast a vote for this election
	//returns true if player has already cast their vote, false if not
	public Boolean electionVoteCheck(Integer pk, Integer playerGame) throws DAOException
	{
		Boolean voted = false;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
			
		try
		{			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			 //create transaction
			 connect.setAutoCommit(false);
			 
			 //Pull this player from voting_history
			 statement = connect.prepareStatement("SELECT ELECTION FROM voting_history V, player_to_game P2G " 
                     + "WHERE (V._FK_P2G = P2G._PK_P2G) AND (P2G._FK_PLAYER = " + pk + ") AND (P2G._FK_GAME = " + playerGame + ");");
			 //Set data into resultset
			 resultSet = statement.executeQuery();	
			 //Commit query
			 connect.commit();
			 
		     //Pull resultSet data by advancing cursor
		     resultSet.next();

		     if (resultSet.getInt("ELECTION") == 1)
		     {
		    	 //This player has voted in this election
		    	 voted = true;
		     }
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}			
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close();
				 statement.close();
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
		//Default is false
		return voted;
	}
	
	//This function determines if an election has been completed (***Currently just checks if everyone has voted, later add time constraint)
	//Returns True if election has been completed, False if election is ongoing
	public Boolean electionFinishedCheck(Integer playerGame) throws DAOException
	{
	    Boolean finished = true;
	    Connection connect = null;
	    PreparedStatement statement = null;
		ResultSet resultSet = null;
	
		try
		{			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			 //create transaction
			 connect.setAutoCommit(false);
			 
			 //Do a sort on voting_history for this game, check the election column--if any values == 0 then election not completed
			 statement = connect.prepareStatement("SELECT ELECTION FROM voting_history V, player_to_game P2G " 
                     + "WHERE (V._FK_P2G = P2G._PK_P2G) AND (P2G._FK_GAME = " + playerGame + ");");
			 //Store resultSet data
			 resultSet = statement.executeQuery();
			 //commit query
			 connect.commit();
			 
		     //Cycle through resultSet data
		     while(resultSet.next())
		     {
			     if (resultSet.getInt("ELECTION") == 0)
			     {
			    	 //There are still players who need to vote in this election
			    	 finished = false;		    	 
			    	 break;
			     }
		     }	     
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}			
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close();				
				 statement.close();	
				 daoFactory.closeConnection(connect);//Causes leak warnings on connect variable wtf
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
	    	    
		//Default is True 
		return finished;
	}
	
	//This function returns a Vector of available games for a player to join 
	//Prereq: The games added to this vector cannot have already started
	public Vector <Integer> getAvailableGames() throws DAOException
	{
		//Since the there is in an unknown number of games at program start, use a Vector
		Vector<Integer> games = new Vector<Integer>();//use games.size() to get final size of Vector	
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try
		{		 					
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection();
			 //create transaction
			 connect.setAutoCommit(false);
			 
			 //Return all games from game list that have not started
		     statement = connect.prepareStatement("SELECT G._PK_GAME from game G WHERE G.STARTED = 0;");
		     resultSet = statement.executeQuery();
		     //commit query
		     connect.commit();
		     
		     //retrieve game ID's
		     while (resultSet.next()) 
			 {      				    		       			        			    	 
		    	 //Add available games to vector
		    	 games.addElement(new Integer(resultSet.getInt("_PK_GAME")));    
			 }		     
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}			
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close();
				 statement.close();	
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
		//Return the vector of available games
		return games;
	}
	
	//Return all games this player is in
	//Returns a Vector data type
	public Vector <Integer> getPlayerGames(Integer pk) throws DAOException
	{
		//Since the player is in an unknown number of games at program start, use a Vector
		Vector<Integer> games = new Vector<Integer>();//use games.size() to get final size of Vector	
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
				
		try
		{			
			 //connect to the database and jar file through the DAO Factory
			 connect = daoFactory.getConnection(); 
			 //create transaction
			 connect.setAutoCommit(false);
			 
			 //Return player_to_game_list						 
			 statement = connect.prepareStatement("SELECT P._PK_PLAYER, P2G._FK_PLAYER, P2G._FK_GAME FROM player P, player_to_game P2G " +
                                                     "WHERE P._PK_PLAYER = P2G._FK_PLAYER;");
			 //Store retrieved data
			 resultSet = statement.executeQuery();
			 //commit query
			 connect.commit();
			 
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
	        			}			        				        	
			        }
			 }
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}			
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close();
				 statement.close();				 
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
				
		return games;		
	}
	
	//Retrieve candidates reputation points list for this game's election for president
	//returns an array of size 10 with all RPs 
	public Integer[] getCandidatesRP(Integer playerGame) throws DAOException
	{
		Integer j = 0;
		Integer[] candidateRP = new Integer[10];	
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;		
			
		try{			
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			//create transaction
			connect.setAutoCommit(false);
			
			//collect the candidates
			statement = connect.prepareStatement("select P.REPUTATION_POINTS " + 
			                                  "FROM player P, candidates C, player_to_game P2G, game G WHERE (C._FK_PLAYER = P._PK_PLAYER)" +
					                          " AND (P2G._FK_PLAYER = P._PK_PLAYER) AND (P2G._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +");");
			//Creating a variable to execute query
			resultSet = statement.executeQuery();
			//commit query
			connect.commit();
			
			//populate the class member candidateList
			for (j = 0; j <= 9; j++)
			{
				resultSet.next();//retrieve database data				
				//convert that data into the arrays
				candidateRP[j] = resultSet.getInt(1);
			}
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}			
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close();
				 statement.close();	
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
		return candidateRP;
	}
	
	
	// Retrieve the candidates list for this games election for president
    // Returns an array of size 10 	
	public String[] getCandidates(Integer playerGame) throws DAOException
	{
		int j;
		String[] candidateList = new String[10];//Holds candidate list for each game
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
					
		try{				
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
            //create transaction
			connect.setAutoCommit(false);
			
			//collect the candidates
			statement = connect.prepareStatement("select E.CANDIDATE " + 
			                                  "FROM election E, player P, candidates C, player_to_game P2G, game G " + 
					                          "WHERE ((E._FK_CANDIDATES = C._PK_CANDIDATES) AND (C._FK_PLAYER = P._PK_PLAYER) " +
					                          "AND (P2G._FK_PLAYER = P._PK_PLAYER) AND (P2G._FK_GAME = G._PK_GAME) AND (E._FK_GAME = G._PK_GAME)" +
					                          "AND (G._PK_GAME = " + playerGame +"));");							

			//Creating a variable to execute query
			resultSet = statement.executeQuery();
			//commit query
			connect.commit();
			
			//populate the class member candidateList
			for (j = 0; j <= 9; j++)
			{
				resultSet.next();//retrieve database data				
				//convert that data into the arrays
				candidateList[j] = resultSet.getString(1);
			}
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}			
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close();
				 statement.close();		
				 daoFactory.closeConnection(connect);	
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
		return candidateList;
	}
	
	// Retrieve the election results for this specific game
	// Returns an array of size 10
	public String[] getElectionResults(Integer playerGame) throws DAOException
	{
		int j = 0;
		Integer votes = 0;
		String[] electionResults = new String[10];
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;				
						
		try{							
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			//create transaction
			connect.setAutoCommit(false);
			
			//collect the candidates
			statement = connect.prepareStatement("select E.CANDIDATE, E.YEA from election E, game G WHERE (E._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +") ORDER BY E.YEA DESC;");
			resultSet = statement.executeQuery();
			//commit query
			connect.commit();
				
			//populate the class member candidateList
			for (j = 0; j <= 9; j ++)
			{
				resultSet.next();//retrieve database data
				//Must concatenate into a String
				votes = resultSet.getInt(2);
				//convert that data into the arrays
				electionResults[j] = resultSet.getString(1) + " with " + Integer.toString(votes) + " votes";
			}
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}			
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close();
				 statement.close();	
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
		return electionResults;
	}
	
	//This function returns the election winner for this game
	public String getElectionWinner(Integer playerGame) throws DAOException
	{
		String winner = null;
		Connection connect = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;			
						
		try
		{				
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			//Create transaction
			connect.setAutoCommit(false);
			
			//collect the candidates
			statement = connect.prepareStatement("select E.CANDIDATE, E.YEA from election E, game G WHERE (E._FK_GAME = G._PK_GAME) AND (G._PK_GAME = " + playerGame +") ORDER BY E.YEA DESC LIMIT 1;");
			resultSet = statement.executeQuery();
			//commit query
			connect.commit();
			
			//Advance the result cursor to move off null
			resultSet.next();
			winner = resultSet.getString(1);
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}			
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet.close();
				 statement.close();		
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
		
		return winner;
	}
	
	//This function sets all player roles for this game (President or Senator)
	//Call this function after an election has been completed
	//***Maybe just call this function inside an election completed boolean function in this class?
	public void setPlayerRoles(Integer playerGame) throws DAOException
	{
		//Function variables
		Integer P2G_PK = 0;
		Connection connect = null;
		PreparedStatement statement1 = null;
		PreparedStatement statement2 = null;
		PreparedStatement statement3 = null;
		PreparedStatement statement4 = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;	
		
		try{							
			//connect to the database and jar file through the DAO Factory
			connect = daoFactory.getConnection();
			//create transaction
			connect.setAutoCommit(false);
			
			//First set president for this game
			statement1 = connect.prepareStatement("SELECT P2G._PK_P2G, E.YEA, E.CANDIDATE from election E, player_to_game P2G, candidates C, player P WHERE " + 
					                               "(E._FK_CANDIDATES = C._PK_CANDIDATES) AND (C._FK_PLAYER= P._PK_PLAYER) AND (P2G._FK_PLAYER = P._PK_PLAYER) AND (E._FK_GAME = " + playerGame +") " +
					                                  "AND (C._FK_GAME = " + playerGame + ") AND (P2G._FK_GAME = " + playerGame + ") ORDER BY E.YEA DESC LIMIT 1;");
			resultSet1 = statement1.executeQuery();//Capture result set
			resultSet1.next();//Bring up data into cursor
			P2G_PK = resultSet1.getInt("P2G._PK_P2G");//Store the primary key for later
            
			//Set this player to be president
			statement2 = connect.prepareStatement("UPDATE player_role PR SET PR.PRESIDENT = 1 WHERE PR._FK_P2G = " + resultSet1.getInt("P2G._PK_P2G") + ";");
			statement2.executeUpdate();
			
			//commit setting president, need to commit here to exclude president
			connect.commit();		
			
			//Next set all other players in this game to be senators, exclude the current president
			statement3 = connect.prepareStatement("SELECT P2G._PK_P2G from player_to_game P2G WHERE (P2G._FK_GAME = " + playerGame + ") AND (P2G._PK_P2G != " + P2G_PK  + ");");
			resultSet2 = statement3.executeQuery();//Capture result set
			//Set all senators
			while (resultSet2.next()) 
			{
				statement4 = connect.prepareStatement("UPDATE player_role PR SET PR.SENATOR = 1 WHERE PR._FK_P2G = " + resultSet2.getInt("P2G._PK_P2G") + ";");
				statement4.executeUpdate();
			}
			
			//commit setting senators
			connect.commit();
		}
		
			catch(SQLException o)
			{
				try
				{
					connect.rollback();
				}			
					catch (SQLException e)
					{
					    throw new DAOException(e);
					}
			}
		
		//Ensure Disconnection from database
		finally
		{
			//Disconnect from the database in reverse order of acquired	
			try 
			{
				 resultSet1.close();
				 resultSet2.close();
				 statement1.close();
				 statement2.close();
				 statement3.close();
				 statement4.close();	
				 daoFactory.closeConnection(connect);
			} 
			
				catch (SQLException e) 
				{
					throw new DAOException(e);
				}			
		}
	}
}	

