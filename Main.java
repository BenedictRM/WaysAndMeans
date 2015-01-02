import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.CardLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.JScrollBar;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
//Begin the GUI
public class Main {

	private JFrame frame;
	private JTextField username_1;
	private JTextField password1_1;
	private JTextField password2_1;
	private JTextField Password;
	private JTextField Username_1; 
	private JPanel StartMenu;
	private JPanel NewUser;
	private JPanel Login;	  
    private JPanel Candidate;   
    private JPanel Election;    
    private JPanel ElectionResults;
    private JPanel GamesDisplay;
  	private JPanel JoinGame;
  	private JPanel StartGame;
    private JButton btnSignIn;
    //hold candidate information   
  	private	String candidate = null;
  	private String vote = null;
  	private String ans = null;
  	private String electionResult = null;
  	private int primarykey;//Stores this player primary key
  	private boolean loggedIn;
  	private boolean gameContinued;
  	private int playerGame;//stores the game number player has continued/ joined
  	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {				
					Game.setPlayerRoles(1);
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		//Initialize local variables
		loggedIn = false;
		gameContinued = false;
		primarykey = 0;
		playerGame = 0;
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().setLayout(new CardLayout(0, 0));	    
	    
	    //********************************************************Begin Start Menu Methods*******************************************************************
	    StartMenu = new JPanel();
	    frame.getContentPane().add(StartMenu, "name_8122419407008");
	    StartMenu.setLayout(null);
	    StartMenu.setVisible(true);//Start out by showing the start menu
	    
	    final JButton btnLogin = new JButton("Sign In");
	    btnLogin.setBounds(167, 119, 89, 23);
	    StartMenu.add(btnLogin);
	    
	    btnLogin.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {	    		
	    		StartMenu.setVisible(false);	
	    		Login.setVisible(true);
	    	}
	    });
	   	    
	    final JButton btnNewUser = new JButton("New User");
	    btnNewUser.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		StartMenu.setVisible(false);	
	    		NewUser.setVisible(true);	    		    	
	    	}
	    });
	    btnNewUser.setBounds(167, 153, 89, 23);
	    StartMenu.add(btnNewUser);
	    //Game Title
	    JLabel lblWaysAndMeans = DefaultComponentFactory.getInstance().createTitle("Ways And Means");
	    lblWaysAndMeans.setFont(new Font("Wide Latin", Font.PLAIN, 25));
	    lblWaysAndMeans.setBounds(37, 38, 409, 43);
	    StartMenu.add(lblWaysAndMeans);
	    
	    //Create a join game button to allow users to join a game
	    final JButton btnJoinGame = new JButton("Join Game");
	    	    
	    btnJoinGame.setBounds(156, 187, 107, 23);
	    StartMenu.add(btnJoinGame);	   
	    if (getPrimaryKey() == 0)
	    {	    	
	    	 btnJoinGame.setEnabled(false);
	    }	
	    
	    btnJoinGame.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		boolean gameCheck = false;
	    		//System.out.println("entering 128\n");
	    		//Add user to game
	    		StartMenu.setVisible(false);
	    		JoinGame.setVisible(true);
	    	}
	    });
	   
	    
	    //Add a start game option
	    JButton btnStartGame = new JButton("Start Game");
	    btnStartGame.setBounds(156, 221, 107, 23);
	    StartMenu.add(btnStartGame);
	    //If not logged in set to false, if not in a game set to false
	    if ((getPrimaryKey() == 0) || (Game.inGameCheck(getPrimaryKey()) == false))
	    {	    	
	    	 btnStartGame.setEnabled(false);
	    }
	    //*****Eventually this will have to check that the specific game they're in evals to true, not that they're just in any game
	    if ((getPrimaryKey() != 0) && (Game.inGameCheck(getPrimaryKey()) == false) && (Game.inGameCount(getPlayerGame()) >= 10))
	    {	    	
	    	 btnStartGame.setEnabled(true);
	    }
	    //If start game selected follow this pattern
	    btnStartGame.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		
	    		
	    	}
	    });
	    
	  //********************************************************End of Start Menu Methods*******************************************************************
	    
	  //********************************************************Begin New User Methods*******************************************************************
	    NewUser = new JPanel();
		frame.getContentPane().add(NewUser, "name_8122433473805");
		NewUser.setLayout(null);
		NewUser.setVisible(false);
		
		JLabel lblReenterPassword = DefaultComponentFactory.getInstance().createLabel("Re-enter Password");
		lblReenterPassword.setBounds(41, 149, 129, 14);
		NewUser.add(lblReenterPassword);
		
		username_1 = new JTextField("Username");
		username_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				username_1.setText("");
			}
		});
		username_1.setColumns(10);
		username_1.setBounds(165, 84, 86, 20);
		NewUser.add(username_1);
		
		password1_1 = new JTextField("Password");
		password1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				password1_1.setText("");
			}
		});
		password1_1.setColumns(10);
		password1_1.setBounds(165, 115, 86, 20);
		NewUser.add(password1_1);
		
		password2_1 = new JTextField("Password");
		password2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				password2_1.setText("");
			}
		});
		password2_1.setColumns(10);
		password2_1.setBounds(165, 146, 86, 20);
		NewUser.add(password2_1);
		
		JButton button = new JButton("Create");
		button.setBounds(165, 177, 89, 23);
		NewUser.add(button);				
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = null;
				String pass = null;
				String passCheck = null;
				String primaryKey = null;				
				int pk = 0;							
				boolean candidateCheck = false;
				boolean gameCheck = false;//For checking if player is assigned a game yet
				user = username_1.getText();
				pass = password1_1.getText();
				passCheck = password2_1.getText();
				
				//Passwords do not match label
				JLabel lblPasswordsDoNot = DefaultComponentFactory.getInstance().createLabel("");
				lblPasswordsDoNot.setBounds(150, 204, 176, 14);
				NewUser.add(lblPasswordsDoNot);
				
				//Username taken label
				JLabel lblUsernameAlreadyTaken = DefaultComponentFactory.getInstance().createLabel("");
				lblUsernameAlreadyTaken.setBounds(151, 59, 210, 14);
				NewUser.add(lblUsernameAlreadyTaken);
				//End of new user methods
								
				if (pass.equals(passCheck) == false || pass.equals("password"))
				{										
					lblPasswordsDoNot.setText("Passwords do not match!");
					if (pass.equals("password"))
					{
						lblPasswordsDoNot.setText("Please don't use the password 'password'");
					}
				}
				
				if (pass.equals(passCheck) == true && (!pass.equals("password")))
				{
					if(Game.userNameCheck(user) == true)
					{
						lblUsernameAlreadyTaken.setText("Username Already Taken!");
					}
					System.out.println(Game.userNameCheck(user));
					if(Game.userNameCheck(user) == false)
					{	
						//Create the new user
						Game.createNew(primaryKey, user, pass);		
						//Login the new user to get the primary key
						pk = Game.login(user, pass);
												
						setPrimaryKey(pk);
						loggedIn = true;//Set the local boolean loggedIn check to true

						System.out.println("Congratulations Senator, you have been awarded 50 reputation points for joining the game!");											
						//Set the value of primary key 					
						System.out.println("Your Primary Key is: " + getPrimaryKey());									
						
						//Check if the user has been assigned a game (should be false since this is a new user
						gameCheck = Game.inGameCheck(pk);
						if (gameCheck == false)
						{												
							//If user logged in, make the 'Join Game' button available
							if (getPrimaryKey() > 0)
						    {					    	
						    	btnJoinGame.setEnabled(true);
						    	btnLogin.setEnabled(false);
						    	btnNewUser.setEnabled(false);
						    }
							
							//Return to start menu and invite them to join a game
							NewUser.setVisible(false);
							StartMenu.setVisible(true);
						}
							//****This next statement should not be reachable, consider deleting:
						    //This user is in a game and needs to be candidateChecked
							if (gameCheck == true)
							{
								//Check if this user is a valid candidate right off the bat, This may change later
								candidateCheck = Game.candidateCheck(getPrimaryKey(), getPlayerGame());
								
								if (candidateCheck == true)
								{
									//Make a jpanel visible for answering candidacy
									NewUser.setVisible(false);
									Candidate.setVisible(true);
								}
								//For now return to main menu
								else if (candidateCheck == false)
								{
									//Close the window and return to main menu
									//hide this candidate panel, for now return to title screen or if election is ready go to election
									if ((Game.electionSetup(getPlayerGame())==true))
									{
										Election.setVisible(true);	
							    		NewUser.setVisible(false);
									}
									if((Game.electionSetup(getPlayerGame())==false))
									{
										StartMenu.setVisible(true);	
							    		NewUser.setVisible(false);
									}		  
								}
							}						
					}
				}
			}
		});
		//********************************************************End of New User Methods*******************************************************************	
				
		//********************************************************Begin Login Methods*******************************************************************
		Login = new JPanel();
		frame.getContentPane().add(Login, "name_8754094989698");
		Login.setLayout(null);
		
		btnSignIn = new JButton("Sign In");
		btnSignIn.setBounds(169, 158, 89, 23);
		Login.add(btnSignIn);
		
		btnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = Username_1.getText();
				String password = Password.getText();				
				int pk = Game.login(user, password);
				boolean gameCheck = false;
                
				JLabel lblInvalidUsernameOr = DefaultComponentFactory.getInstance().createLabel("");
				lblInvalidUsernameOr.setBounds(139, 225, 259, 14);
				Login.add(lblInvalidUsernameOr);
											
				//Invalid login
				if (pk == 0)
				{
					lblInvalidUsernameOr.setText("Invalid username or password");					
				}
				
				if (pk != 0)
				{
					//Set the logged in user's pk
					setPrimaryKey(pk);
					loggedIn = true;//Set local loggedIn check to true
		
					Game.inGameCount(getPlayerGame());
					//Check if the user has been assigned a game 
					gameCheck = Game.inGameCheck(pk);
					if (gameCheck == false)
					{												
						//If user logged in, make the 'Join Game' button available
						if (getPrimaryKey() > 0)
					    {					    	
							btnJoinGame.setEnabled(true);
					    	btnLogin.setEnabled(false);
					    	btnNewUser.setEnabled(false);					    	
					    }
						
						//Return to start menu and invite them to join a game
						Login.setVisible(false);
						StartMenu.setVisible(true);
					}
					
					if (gameCheck == true)
					{
						//Update game display panel
						GamesDisplay.removeAll();								
						GamesDisplay.validate();
						GamesDisplay.repaint();
						
						//Run the candidate check
						retrieveGames();
						Login.setVisible(false);
						GamesDisplay.setVisible(true);						
					}
				}
			}
		});
				
		Password = new JTextField();
		Password.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Password.setText("");
			}
		});
		Password.setText("Password");
		Password.setBounds(169, 127, 86, 20);
		Login.add(Password);
		Password.setColumns(10);
		
		Username_1 = new JTextField();
		Username_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Username_1.setText("");
			}
		});
		Username_1.setText("Username");
		Username_1.setBounds(169, 96, 86, 20);
		Login.add(Username_1);
		Username_1.setColumns(10);		
		//********************************************************End of Login Methods*******************************************************************
		
		//********************************************************Begin Candidate Methods*******************************************************************
		Candidate = new JPanel();
		frame.getContentPane().add(Candidate, "name_156688107903713");
		Candidate.setLayout(null);
        //Update candidates panel
		candidatesUpdate();		
		
		//********************************************************End of Candidate Methods*******************************************************************
		
		//********************************************************Begin Election Methods******************************************************************		
		//Create Frame
		Election = new JPanel();
		frame.getContentPane().add(Election, "name_234158849574427");
		Election.setLayout(null);
		
		//Ensure this player has logged in and set the game they are in before creating values
		if((loggedIn == true) && (gameContinued == true))
		{	
			//if the election has been set up and this player has not voted, go ahead and populate the candidates
			if ((Game.electionSetup(getPlayerGame())==true) && ((Game.electionVoteCheck(getPrimaryKey(), getPlayerGame()) == false)))
			{									     
			     electionUpdate();
			}			
		}
		//********************************************************End of Election Methods********************************************************************
		
		//********************************************************Begin Election Results Methods*********************************************************
		ElectionResults = new JPanel();
		frame.getContentPane().add(ElectionResults, "name_217610321728531");
		ElectionResults.setLayout(null);
		
		//Ensure this player has logged in and set the game they are in before creating values
		if((loggedIn == true) && (gameContinued == true))
		{
			if (Game.electionSetup(getPlayerGame())==true)
			{
			     resultsUpdate();
			}
		}
		//********************************************************End of Election Results Methods*********************************************************

		//********************************************************Begin GamesDisplay Methods*********************************************************		
		GamesDisplay = new JPanel();
		frame.getContentPane().add(GamesDisplay, "name_427025453661026");
		GamesDisplay.setLayout(null);
							
		//If this user is in a game, retrieve all games and let them continue
		System.out.print("Entering 459 GUI\n");
		
		//Ensure this player has logged in 
		if((loggedIn == true))
		{					    
			if (Game.inGameCheck(getPrimaryKey()) == true)
			{
				System.out.print("Entering 466 GUI\n");  
				//Function to retrieve games player is in, displays in a dropdown menu
				retrieveGames();
				//Probably need to call all possible panel combinations here to redraw
			}										
		}				
		//********************************************************End of GamesDisplay Methods*********************************************************
		
		//********************************************************Begin Join Games Panel*********************************************************
		JoinGame = new JPanel();
		frame.getContentPane().add(JoinGame, "name_229670089069189");
		JoinGame.setLayout(null);
		
		JLabel lblAvailableGames = DefaultComponentFactory.getInstance().createTitle("Available Games");
		lblAvailableGames.setFont(new Font("Comic Sans MS", Font.BOLD, 17));
		lblAvailableGames.setBounds(165, 11, 157, 47);
		JoinGame.add(lblAvailableGames);
		
		//Function to retrieve games player can join, displays in a dropdown menu
		joinGame();		
		//********************************************************End of Join Games Panel*********************************************************
	    
		//********************************************************Begin Start Game panel********************************************************
		StartGame = new JPanel();
		frame.getContentPane().add(StartGame, "name_233910827980441");
		StartGame.setLayout(null);
		
		//Function to show start game button and number of players in game
		startGame();										
		//********************************************************End of Start Game Panel********************************************************
	}	
	
	//Begin Function Methods:
	
	//This function updates the candidates panel for a player
	public void candidatesUpdate()
	{
		//Ensure this player has logged in and set the game they are in before creating values
		if((loggedIn == true) || (gameContinued == true))
		{	
			//Yea button actions:
			JButton Yea = new JButton("Yea");
			Yea.setBounds(101, 153, 89, 23);
			Candidate.add(Yea);
			
			Yea.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ans = "Yea";
					//Call candidate add to store this candidates answer
					Game.candidateAdd(ans, getPrimaryKey());
					//reset ans class variable to null
					ans = null;
					//hide this candidate panel, for now return to title screen or if election is ready go to election
					if ((Game.electionSetup(getPlayerGame())==true))
					{
						//Update the election board
						Election.removeAll();						
						electionUpdate();
						Election.validate();
					    Election.repaint();
					     
						Election.setVisible(true);	
			    		Candidate.setVisible(false);
					}
					if((Game.electionSetup(getPlayerGame())==false))
					{
						StartMenu.setVisible(true);	
			    		Candidate.setVisible(false);
					}
				}
			});
			
			//Nay button actions
			JButton Nay = new JButton("Nay");
			Nay.setBounds(235, 153, 89, 23);
			Candidate.add(Nay);
			Nay.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ans = "Nay";
					//Call candidate add to store this candidates answer
					Game.candidateAdd(ans, getPrimaryKey());
					//reset ans class variable to null
					ans = null;
					//hide this candidate panel, for now return to title screen or if election is ready go to election
					if ((Game.electionSetup(getPlayerGame())==true))
					{
						//Update the election board
						Election.removeAll();						
						electionUpdate();
						Election.validate();
					    Election.repaint();
					     
						Election.setVisible(true);	
			    		Candidate.setVisible(false);
					}
					if((Game.electionSetup(getPlayerGame())==false))
					{					
						StartMenu.setVisible(true);	
			    		Candidate.setVisible(false);
					}
				}
			});
				
			JLabel lblNewJgoodiesLabel = DefaultComponentFactory.getInstance().createLabel("You have enough reputation to compete for the office of the President!");
			lblNewJgoodiesLabel.setBounds(33, 103, 424, 14);
			Candidate.add(lblNewJgoodiesLabel);
			
			JLabel lblWouldYouLike = DefaultComponentFactory.getInstance().createLabel("Would you like to throw your hat into the ring? ('Yea or Nay')");
			lblWouldYouLike.setBounds(70, 128, 354, 14);
			Candidate.add(lblWouldYouLike);
			
			JLabel lblWaysAndMeans_1 = DefaultComponentFactory.getInstance().createTitle("Ways And Means");
			lblWaysAndMeans_1.setFont(new Font("Wide Latin", Font.PLAIN, 25));
			lblWaysAndMeans_1.setBounds(37, 38, 409, 43);
			Candidate.add(lblWaysAndMeans_1);
			
			return;
		}
	}
	
	//This function updates the election candidates for which to vote on
	public void electionUpdate()
	{
		//Create an array that contains candidate RPs for this game
		final int []candidatesRP =  Game.getCandidatesRP(getPlayerGame());
		//Create an array that contains the candidate usernames for this game
		final String []candidateUN = Game.getCandidates(getPlayerGame());

		//Check to see if this player has already voted
		if (Game.electionVoteCheck(getPrimaryKey(), getPlayerGame()) == false)
		{					
			candidate = candidateUN[0] + " RP: ";
			final JRadioButton candidate0 = new JRadioButton(candidate + candidatesRP[0]);
			candidate0.setBounds(26, 82, 124, 23);
			Election.add(candidate0);								
			
			System.out.println("candidate: " + candidate + "\n");
			
			candidate = candidateUN[1] + " RP: ";
			final JRadioButton candidate1 = new JRadioButton(candidate + candidatesRP[1]);
			candidate1.setBounds(26, 108, 124, 23);
			Election.add(candidate1);				
			
			System.out.println("candidate: " + candidate + "\n");
			
			candidate = candidateUN[2] + " RP: ";
			final JRadioButton candidate2 = new JRadioButton(candidate + candidatesRP[2]);
			candidate2.setBounds(26, 134, 124, 23);
			Election.add(candidate2);
			
			System.out.println("candidate: " + candidate + "\n");
			
			candidate = candidateUN[3] + " RP: ";
			final JRadioButton candidate3 = new JRadioButton(candidate + candidatesRP[3]);
			candidate3.setBounds(26, 160, 124, 23);
			Election.add(candidate3);					
			
			candidate = candidateUN[4] + " RP: ";
			final JRadioButton candidate4 = new JRadioButton(candidate + candidatesRP[4]);
			candidate4.setBounds(152, 82, 130, 23);
			Election.add(candidate4);
			
			candidate = candidateUN[5] + " RP: ";
			final JRadioButton candidate5 = new JRadioButton(candidate + candidatesRP[5]);
			candidate5.setBounds(152, 108, 130, 23);
			Election.add(candidate5);					
				
			candidate = candidateUN[6] + " RP: ";
			final JRadioButton candidate6 = new JRadioButton(candidate + candidatesRP[6]);
			candidate6.setBounds(152, 134, 130, 23);
			Election.add(candidate6);					
			
			candidate = candidateUN[7] + " RP: ";
			final JRadioButton candidate7 = new JRadioButton(candidate + candidatesRP[7]);
			candidate7.setBounds(152, 160, 130, 23);
			Election.add(candidate7);					
			
			candidate = candidateUN[8] + " RP: ";
			final JRadioButton candidate8 = new JRadioButton(candidate + candidatesRP[8]);
			candidate8.setBounds(294, 82, 130, 23);
			Election.add(candidate8);				
			
			candidate = candidateUN[9] + " RP: ";
			final JRadioButton candidate9 = new JRadioButton(candidate + candidatesRP[9]);
			candidate9.setBounds(294, 108, 130, 23);
			Election.add(candidate9);	
			
			//Group all the radio buttons so we can retrieve data from them
			ButtonGroup group = new ButtonGroup();
			group.add(candidate0);
			group.add(candidate1);
		    group.add(candidate2);
		    group.add(candidate3);
		    group.add(candidate4);
		    group.add(candidate5);
		    group.add(candidate6);
		    group.add(candidate7);
		    group.add(candidate8);
		    group.add(candidate9);
		    
		    JButton Electbtn = new JButton("Vote!");
			Electbtn.setBounds(172, 213, 89, 23);
			Election.add(Electbtn);
			Electbtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {					
				
					//Capture this user's vote selection and submit it to the database
				    if (candidate0.isSelected())
					{
						vote = candidateUN[0];	
					}				    
				    
				    if (candidate1.isSelected())
					{
						vote = candidateUN[1];	
					}
				    
				    if (candidate2.isSelected())
					{
						vote = candidateUN[2];	
					}
				    
				    if (candidate3.isSelected())
					{
						vote = candidateUN[3];	
					}
				    
				    if (candidate4.isSelected())
					{
						vote = candidateUN[4];	
					}
				    
				    if (candidate5.isSelected())
					{
						vote = candidateUN[5];	
					}
				    
				    if (candidate6.isSelected())
					{
						vote = candidateUN[6];	
					}
				    
				    if (candidate7.isSelected())
					{
						vote = candidateUN[7];	
					}
				    
				    if (candidate8.isSelected())
					{
						vote = candidateUN[8];	
					}
				    
				    if (candidate9.isSelected())
					{
						vote = candidateUN[9];	
					}
				    
				    //Submit the vote to the database		
				    Game.elect(vote, getPrimaryKey(), getPlayerGame());
				    //Update the leader board
				    ElectionResults.removeAll();						
					resultsUpdate();
					ElectionResults.validate();
				    ElectionResults.repaint();
				    
					ElectionResults.setVisible(true);
					Election.setVisible(false);
				}
			}); 
		
			JLabel lblTheElectionFor = DefaultComponentFactory.getInstance().createLabel("The Election for president is now available!");
			lblTheElectionFor.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblTheElectionFor.setBounds(94, 23, 330, 14);
			Election.add(lblTheElectionFor);
			
			JLabel lblPleaseVoteFor = DefaultComponentFactory.getInstance().createLabel("Please vote for the candidate you would like to have as president:");
			lblPleaseVoteFor.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblPleaseVoteFor.setBounds(26, 48, 377, 14);
			Election.add(lblPleaseVoteFor);	
			
			return;
		}
		
		//This player has already voted, take them to election results
		else if (Game.electionVoteCheck(getPrimaryKey(), getPlayerGame()) == true)
		{
			//Update the leader board
		    ElectionResults.removeAll();						
			resultsUpdate();
			ElectionResults.validate();
			ElectionResults.paintImmediately(ElectionResults.getVisibleRect());//Force an immediate update
		    
		    //Display panel
		    Election.setVisible(false);
			ElectionResults.setVisible(true);
						
			return;
		}
	}//End of election update		
	
	//This function sets the results of the running election
	public void resultsUpdate()
	{
		//Create an array to hold current election results, size 10
		String[] electionResults = Game.getElectionResults(getPlayerGame());
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.setBounds(172, 228, 89, 23);
		ElectionResults.add(btnQuit);
		
		electionResult = electionResults[0];
		JLabel First = DefaultComponentFactory.getInstance().createLabel(electionResult);
		First.setBounds(172, 55, 234, 14);
		ElectionResults.add(First);		
		
		System.out.println("candidate: " + electionResult + " player game: " + getPlayerGame() + "\n");
		
		electionResult = electionResults[1];
		JLabel Second = DefaultComponentFactory.getInstance().createLabel(electionResult);
		Second.setBounds(172, 72, 234, 14);
		ElectionResults.add(Second);
		
		System.out.println("candidate: " + electionResult + "\n");
		
		electionResult = electionResults[2];
		JLabel Third = DefaultComponentFactory.getInstance().createLabel(electionResult);
		Third.setBounds(172, 89, 234, 14);
		ElectionResults.add(Third);
		
		System.out.println("candidate: " + electionResult + "\n");
		
		electionResult = electionResults[3];
		JLabel Fourth = DefaultComponentFactory.getInstance().createLabel(electionResult);
		Fourth.setBounds(172, 104, 234, 14);
		ElectionResults.add(Fourth);
		
		electionResult = electionResults[4];
		JLabel Fifth = DefaultComponentFactory.getInstance().createLabel(electionResult);
		Fifth.setBounds(172, 119, 237, 14);
		ElectionResults.add(Fifth);
		
		electionResult = electionResults[5];
		JLabel Sixth = DefaultComponentFactory.getInstance().createLabel(electionResult);
		Sixth.setBounds(172, 134, 234, 14);
		ElectionResults.add(Sixth);
		
		electionResult = electionResults[6];
		JLabel Seventh = DefaultComponentFactory.getInstance().createLabel(electionResult);
		Seventh.setBounds(172, 152, 234, 14);
		ElectionResults.add(Seventh);
		
		electionResult = electionResults[7];
		JLabel Eigth = DefaultComponentFactory.getInstance().createLabel(electionResult);
		Eigth.setBounds(172, 170, 234, 14);
		ElectionResults.add(Eigth);
		
		electionResult = electionResults[8];
		JLabel Ninth = DefaultComponentFactory.getInstance().createLabel(electionResult);
		Ninth.setBounds(172, 188, 234, 14);
		ElectionResults.add(Ninth);
		
		electionResult = electionResults[9];
		JLabel Tenth = DefaultComponentFactory.getInstance().createLabel(electionResult);
		Tenth.setBounds(172, 203, 234, 14);
		ElectionResults.add(Tenth);
	
		JLabel lblElectionResults = DefaultComponentFactory.getInstance().createLabel("Election Results:");
		lblElectionResults.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblElectionResults.setBounds(141, 11, 216, 33);
		ElectionResults.add(lblElectionResults);
		//Display panel
	    
		//Quit the program
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		return;
	}//End of resultsUpdate
	
	//This function sets the list of radio buttons to select the game player wants to play after login
	public void retrieveGames()
	{
		Vector<Integer> pGames = new Vector<Integer>();//use games.size() to get final size of Vector		
		//Get the vector of games this player is in
		pGames = Game.getPlayerGames(getPrimaryKey());
		
		//Test a method
		Game.gameStartedCheck(getPlayerGame());
		
		//This build uses java 1.8.2, will throw an error if using anything below Java 7
		final JComboBox<Integer> comboBox = new JComboBox<Integer>(pGames);
		comboBox.setBounds(174, 48, 121, 20);
		GamesDisplay.add(comboBox);
		
		JLabel lblContinueGame = DefaultComponentFactory.getInstance().createLabel("Continue Game:");
		lblContinueGame.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblContinueGame.setBounds(52, 51, 107, 14);
		GamesDisplay.add(lblContinueGame);
		
		JLabel lblGamesYouAre = DefaultComponentFactory.getInstance().createTitle("Games you are in:");
		lblGamesYouAre.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		lblGamesYouAre.setBounds(175, 11, 152, 23);
		GamesDisplay.add(lblGamesYouAre);	
				
		JButton btnContinue = new JButton("Continue");
		btnContinue.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Retrieve the selected game				
				int selectedGame =  (Integer) comboBox.getSelectedItem();
				System.out.println("game selected: "+ selectedGame +"\n");
				//Set the game this player is in 
				setPlayerGame(selectedGame);
				//Set gameContinued to true
				gameContinued = true;
				
				//Update panels for this game
			    if ((loggedIn = true) && (gameContinued == true))
			    {
					//Store this player's candidate decision for this game
			    	boolean candidateCheck = false;
					//Check if this user is a valid candidate right off the bat, This may change later
					candidateCheck = Game.candidateCheck(getPrimaryKey(), getPlayerGame());
					
			    	//Check to see if this game has started, if so go to candidates (eventually go to current events)
			    	if (Game.gameStartedCheck(getPlayerGame()) == true)
			    	{				    					    					    							
						//Election not set up, and user is a candidate--go to candidate selection
						if(candidateCheck == true)
						{
							//Update Candidate panel
					    	Candidate.removeAll();
					    	//Update the Candidate panel
					    	candidatesUpdate();
					    	Candidate.revalidate();
					    	Candidate.repaint();
					    	
							//Make a jpanel visible for answering candidacy
							GamesDisplay.setVisible(false);
							Candidate.setVisible(true);
							
							System.out.println("937\n");
						}
						
							else if (candidateCheck == false)
							{								
								//Election set up, go to election page
								if ((Game.electionSetup(getPlayerGame())==true))
								{									
									if((Game.electionVoteCheck(getPrimaryKey(), getPlayerGame()) == false))
									{
										//Update the election panel
									    Election.removeAll();						
										electionUpdate();
										Election.validate();
									    Election.repaint();
										
										Election.setVisible(true);	
							    		GamesDisplay.setVisible(false);
							    		System.out.println("repainted 950\n");
									}
									
						    		else if ((Game.electionVoteCheck(getPrimaryKey(), getPlayerGame()) == true))
						    		{
							    		//Update the leader board
									    ElectionResults.removeAll();						
										resultsUpdate();
										ElectionResults.validate();
										ElectionResults.paintImmediately(ElectionResults.getVisibleRect());//Force an immediate update
									    
									    //Display panel
									    GamesDisplay.setVisible(false);
										ElectionResults.setVisible(true);
						    		}
						    		return;
								}
								//Election not set up, return to start menu
								if((Game.electionSetup(getPlayerGame())==false))
								{
									StartMenu.setVisible(true);	
									GamesDisplay.setVisible(false);
						    		
						    		System.out.println("repainted 957\n");
						    		
						    		return;
								}	
							
							}
				    }
			    	
			    	//This game has not started, go to StartGame panel
			    	else if (Game.gameStartedCheck(getPlayerGame()) == false)
			    	{
			    		//Update StartGame panel
						StartGame.removeAll();
						startGame();
				    	StartGame.validate();		    	
				    	StartGame.repaint();
			    		
			    		GamesDisplay.setVisible(false);
			    		StartGame.setVisible(true);
			    		
			    		return;
			    	}
			    }
			}
		});
		btnContinue.setBounds(186, 208, 101, 23);
		GamesDisplay.add(btnContinue);		
	}
	
	//This function displays a list of games that are available to a player to joing i.e. games that have not started
	public void joinGame()
	{
		Vector<Integer> pGames = new Vector<Integer>();//use games.size() to get final size of Vector		
		//Get the vector of games this player is in
		pGames = Game.getAvailableGames();//Call function that returns games that are available to join
		
		//This build uses java 1.8.2, will throw an error if using anything below Java 7
		final JComboBox<Integer> comboBox = new JComboBox<Integer>(pGames);
		comboBox.setBounds(174, 48, 121, 20);
		JoinGame.add(comboBox);
		
		JLabel lblContinueGame = DefaultComponentFactory.getInstance().createLabel("Join Game:");
		lblContinueGame.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblContinueGame.setBounds(52, 51, 107, 14);
		JoinGame.add(lblContinueGame);
				
		JButton btnContinue = new JButton("Join Game");
		btnContinue.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Retrieve the selected game				
				int selectedGame = (Integer)comboBox.getSelectedItem();
				
				//Add this player to this game
				Game.addToGame(getPrimaryKey(), selectedGame);
				
				//Have them continue in this new game and go to new panel to see this game's status
				setPlayerGame(selectedGame);//Store the selected game in GUI
				//Set gameContinued to true
				gameContinued = true;
				//Redraw start game panel (to reflect players in game, start etc.
				
				//Update StartGame panel
				StartGame.removeAll();
				startGame();
		    	StartGame.validate();		    	
		    	StartGame.repaint();
		    	System.out.println("repainted start\n");
		    	
		    	//Open the startGame page IF the game has not started, otherwise go to candidate stuff
		    	if(Game.gameStartedCheck(getPlayerGame()) == false)
		    	{
		    		JoinGame.setVisible(false);	
		    		StartGame.setVisible(true);
		    		
		    		return;
		    	}		    	
			    	//Open the startGame page IF the game has not started, otherwise go to candidate stuff
			    	else if(Game.gameStartedCheck(getPlayerGame()) == true)
			    	{			    		
			    		//Update Candidate panel
				    	Candidate.removeAll();								
				    	Candidate.validate();
				    	Candidate.repaint();

				    	//if the election has been set up, go ahead and populate the candidates
						if (Game.electionSetup(getPlayerGame())==true)
						{
							//Update Election panel
					    	Election.removeAll();	
					    	electionUpdate();
					    	Election.validate();
					    	Election.repaint();
					    	
					    	return;
						}
				    	
						//Go to candidate methods
						boolean candidateCheck = false;
						//Check if this user is a valid candidate right off the bat, This may change later
						candidateCheck = Game.candidateCheck(getPrimaryKey(), getPlayerGame());
						
						if(candidateCheck == true)
						{
							//Make a jpanel visible for answering candidacy
							JoinGame.setVisible(false);
							Candidate.setVisible(true);
							
							return;
						}
						
							else if (candidateCheck == false)
							{
								//Close the window, should be logged in now without answering candidacy
								System.out.println("Your Primary Key is: " + getPrimaryKey());
								//hide this candidate panel, for now return to title screen or if election is ready go to election
								if ((Game.electionSetup(getPlayerGame())==true))
								{
									Election.setVisible(true);	
						    		JoinGame.setVisible(false);
						    		
						    		return;
								}
									else if((Game.electionSetup(getPlayerGame())==false))
									{
										StartMenu.setVisible(true);	
							    		JoinGame.setVisible(false);
							    		
							    		return;
									}							
							}
			    	}			
			}
		});
		btnContinue.setBounds(183, 180, 101, 23);
		JoinGame.add(btnContinue);		
	}
	
	//This function allows a user to start a game if  game has > 9 players, and see how many players are in this game
	//This function also redraws the entire StartGame panel
	public void startGame()
	{		
		JLabel lblGameNumber = DefaultComponentFactory.getInstance().createTitle("Game Number:");
		lblGameNumber.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblGameNumber.setBounds(64, 11, 162, 14);
		StartGame.add(lblGameNumber);
		
		JButton btnMainMenu = new JButton("Main Menu");
		btnMainMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				StartGame.setVisible(false);
				StartMenu.setVisible(true);
				
				return;
			}
		});
		btnMainMenu.setBounds(78, 215, 111, 23);
		StartGame.add(btnMainMenu);
		
		JButton btnQuit_1 = new JButton("Quit");
		btnQuit_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Quit out of program
				System.exit(0);
				
				return;
			}
		});
		btnQuit_1.setBounds(220, 215, 102, 23);
		StartGame.add(btnQuit_1);				
		
		JLabel lblNumberOfPlayers = DefaultComponentFactory.getInstance().createLabel("Number of players in game: ");
		lblNumberOfPlayers.setBounds(16, 135, 173, 14);
		StartGame.add(lblNumberOfPlayers);
		
		//Check to see if at least 10 players are in this game, if so allow a player to start the game
		if (Game.inGameCount(getPlayerGame()) >= 10)
		{
			JButton btnStartGame_1 = new JButton("Start Game!");
			btnStartGame_1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					Game.startGame(playerGame);
					
					//Go to candidate panel					
					Candidate.removeAll();	
					candidatesUpdate();	
			    	Candidate.validate();
			    	Candidate.repaint();
				    
			    	StartGame.setVisible(false);
			    	Candidate.setVisible(true);

			    	return;
				}
			});
			btnStartGame_1.setBounds(139, 163, 108, 23);
			StartGame.add(btnStartGame_1);
		}
		
		JTextArea noPlayers = new JTextArea();
		noPlayers.setBounds(182, 130, 44, 22);
		StartGame.add(noPlayers);
		noPlayers.setText("" + Game.inGameCount(getPlayerGame()));
		
		JTextArea gameNo = new JTextArea();
		gameNo.setBounds(214, 9, 51, 22);
		StartGame.add(gameNo);
		gameNo.setText("" + getPlayerGame());
	}
	
	//Set the primary key value for the class
	public void setPrimaryKey(int primarykey) {
		this.primarykey = primarykey;
		
		return;
	}
	//return the primary key for the class
	public int getPrimaryKey() {
		return primarykey;
	}
	//Set the primary key value for the class
	public void setPlayerGame(int gameID) {
		this.playerGame = gameID;
		
		return;
	}
	//return the primary key for the class
	public int getPlayerGame() {
		return playerGame;
	}
}//End of program