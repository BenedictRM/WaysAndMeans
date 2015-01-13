import java.util.Vector;
/**
 * This interface represents a contract for a DAO for the {@link User} model.
 * Note that all methods which returns the {@link User} from the DB, will not
 * fill the model with the password, due to security reasons.
 *
 * @author BalusC
 * @link http://balusc.blogspot.com/2008/07/dao-tutorial-data-layer.html
 */
public interface GameDAO {

    // Actions ------------------------------------------------------------------------------------

	/**
	 * Create the new user entry into player table in game database
	 */
	public void createNew (String pk, String username, String password) throws DAOException;;

	/**
	 * Create a login function to retrieve user data
	 * @param username
	 * @param password
	 * @return
	 */
	public Integer login (String username, String password) throws DAOException;;
	
	/**
	 * Delete user from player table and game database all together
	 * @param pk
	 */
	public void deleteUser (Integer pk) throws DAOException;;
	
	/**
	 * This function creates a new game in the database table 'game' that users can now join and play
	 */
	public void createGame() throws DAOException;;
	
	/**
	*This function is used to add a user to a game, sets their initial values across several tables
	*Prereq: No games can be called 'game 0', this will cause failures--games can be any positive integer, just increment as necessary
	*/
	public void addToGame (Integer pk, Integer playerGame) throws DAOException;;
	
	/**
	 * This function starts a game with at least 10 players in it
	 * @param playerGame
	 */
	public void startGame(Integer playerGame) throws DAOException;;
	
	/**
	 * This function will be called to add a user's response to running for president to the database
	 * @param ans
	 * @param pk
	 */
	public void candidateAdd (String ans, Integer pk) throws DAOException;;
	
	/**
	 * Sets up election table, if it has already been set up then it skips setup
	 * @param playerGame
	 * @return
	 */
	public Boolean electionSetup(Integer playerGame) throws DAOException;;
	
	/**
	 * This function sets player's vote for president, players votes are kept in voting_history table
	 * @param vote
	 * @param pk
	 * @param playerGame
	 */
	public void elect (String vote, Integer pk, Integer playerGame) throws DAOException;;
	
	/**
	 * This function returns the number of players who are in the game the user is currently in
	 * @param playerGame
	 * @return
	 */
	public Integer inGameCount(Integer playerGame) throws DAOException;;
	
	/**
	 * This function is used to check if a logged in player is in a game, if not ask them to join a game
	 * @param pk
	 * @return
	 */
	public Boolean inGameCheck (Integer pk) throws DAOException;;
	
	/**
	 * Check to see if the game this player is actively in has started
	 * @param playerGame
	 * @return
	 */
	public Boolean gameStartedCheck(Integer playerGame) throws DAOException;;
	
	/**
	 * while candidate list <= 10 and user rp >= to top candidates and user has not already replied nay to running return true else false
	 * @param pk
	 * @param playerGame
	 * @return
	 */
	public Boolean candidateCheck (Integer pk, Integer playerGame) throws DAOException;;
	
	/**
	 * This function is used to check a new user's userName against the database to make sure their name is unique
	 * @param i
	 * @return
	 */
	public Boolean userNameCheck(String user) throws DAOException;;
	
	/**
	 * This function determines if a player has already cast a vote for this election
	 * returns true if player has already cast their vote, false if not
	 * @param pk
	 * @param playerGame
	 * @return
	 */	
	public Boolean electionVoteCheck(Integer pk, Integer playerGame) throws DAOException;;
	
	/**
	 * This function determines if an election has been completed (***Currently just checks if everyone has voted, later add time constraint)
	 * Returns True if election has been completed, False if election is ongoing
	 * @param playerGame
	 * @return
	 */	
	public Boolean electionFinishedCheck(Integer playerGame) throws DAOException;;
	
	/**
	 * This function returns a Vector of available games for a player to join 
	 * Prereq: The games added to this vector cannot have already started
	 * @return
	 */	
	public Vector <Integer> getAvailableGames() throws DAOException;;
	
	/**
	 * Return all games this player is in
	 * Returns a Vector data type
	 * @param pk
	 * @return
	 */	
	public Vector <Integer> getPlayerGames(Integer pk) throws DAOException;;
	
	/**
	 * Retrieve candidates reputation points list for this game's election for president
	 * returns an array of size 10 with all RPs 
	 * @param playerGame
	 * @return
	 */
	public Integer[] getCandidatesRP(Integer playerGame) throws DAOException;;
	
	/**
	 * Retrieve the candidates list for this games election for president
	 * Returns an array of size 10
	 * @param playerGame
	 * @return
	 */	
	public String[] getCandidates(Integer playerGame) throws DAOException;;
	
	/**
	 * Retrieve the election results for this specific game
	 * Returns an array of size 10
	 * @param playerGame
	 * @return
	 */	
	public String[] getElectionResults(Integer playerGame) throws DAOException;;
	
	/**
	 * This function returns the election winner for this game
	 * @param playerGame
	 * @return
	 */
	public String getElectionWinner(Integer playerGame) throws DAOException;;
	
	/**
	 * This function sets all player roles for this game (President or Senator)
	 * Call this function after an election has been completed
	 * @param playerGame
	 */
	public void setPlayerRoles(Integer playerGame) throws DAOException;;
}