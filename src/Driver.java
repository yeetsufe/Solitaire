/* Name: Jim Wu
 * Date: 1/25/2022
 * Description: This program is a recreation of the popular card game Solitaire. This program also contains an interactive leaderboard that stores each player's 
 * high scores and allows for the scores to be deleted. The scores are calculated using a timer included with the program.
 */
import java.util.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class Driver extends MouseAdapter implements ActionListener{
	TreeMap<String,Integer> leaderboard = new TreeMap<>(); //leaderboard represented by TreeMap
	HashMap <Integer,Suit> suits = Suit.getSuitMap(); //suits instances are stored in a HashMap static variable
	Stock stock;
	Pile[] tableau; //An array used to store all seven piles in the tableau
	Card lastSelected = null; //stores the previously selected card by the user
	String searchName; //stores the name the user searches for in the leaderboard
	int level,timer; //level represents the total amount of cards in each solitaire game, timer counts the number of seconds elapsed in a separate thread
	Timer t; //Timer sub-class that implements the timer through threading
	
	//*JFrame objects and containers
	JFrame gameFrame,optionsFrame,aboutFrame;
	JPanel gamePanel,optionsPanel,aboutPanel,tableauPanel,suitPanel,topPanel,stockPanel,topRight,searchPanel,promptPanel;
	JLabel titleScreen,timerLabel, searchPlayer;
	JTextArea aboutText,optionsText,searchText;
	JTextField nameField;
	JButton searchButton,deleteButton;
	JMenuBar mainMenu;
	JMenu gameMenu,optionsMenu,aboutMenu,gameOption;
	JMenuItem exitOption,options,about,easy,medium,hard;
	
	//Constructor, sets up the program's graphics
	public Driver(){
		gameFrame = new JFrame("Solitaire");
		gameFrame.setResizable(false);
		gamePanel = new JPanel();
		gamePanel.setPreferredSize(new Dimension(900, 700));
		gamePanel.setLocation (300,300);
		gamePanel.setBackground (new Color(34,177,76));
		gamePanel.setLayout(new BoxLayout(gamePanel,BoxLayout.PAGE_AXIS));
		gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		titleScreen = new JLabel(new ImageIcon(new ImageIcon ("background.jpg").getImage().getScaledInstance(900,700,java.awt.Image.SCALE_SMOOTH))); //title screen image
		gamePanel.add(titleScreen);
		
		//Menus and Options
		gameOption = new JMenu("New Game");
		easy = new JMenuItem("Easy (52 Cards)"); //Game options
		medium = new JMenuItem("Medium (60 Cards)");
		hard = new JMenuItem("Hard (68 Cards)");
		gameOption.add(easy);
		gameOption.add(medium);
		gameOption.add(hard);
		
		exitOption = new JMenuItem("Exit");
		gameMenu = new JMenu ("Game"); 
		gameMenu.add (gameOption);
		gameMenu.add(exitOption);
		
		options = new JMenuItem("Options");
		optionsMenu = new JMenu("Options");
		optionsMenu.add(options);
		
		about = new JMenuItem("About"); //About menu
		aboutMenu = new JMenu("About");
		aboutMenu.add(about);
		aboutFrame = new JFrame("About");
		aboutFrame.setResizable(false);
		aboutFrame.setLocation(100, 100);
		aboutPanel = new JPanel();
		aboutPanel.setPreferredSize(new Dimension(500,400));
		aboutPanel.setBackground(Color.green);
		aboutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		aboutPanel.setLayout(new BorderLayout());
		aboutText = new JTextArea();
		aboutText.setText("Solitaire\n\nGraphics by Jim Wu\nProgramming by Jim Wu\n\nHow to Play:\n\nThe objective of Solitaire is to stack all cards into their respective piles in the foundation according to their suits in ascending order, starting from the ace card.\n\nCards that are flipped cannot be moved and can only be unflipped if the uncovered card is at the top of a pile. Cards can only be moved on top of another card if the card's value is one below and the opposite colour.\n\nClick on the \"Options\" menu to view and modify the leaderboard and click on the \"Game\" menu to start a new game.\n\nGood Luck!");
		aboutText.setLineWrap(true);
		aboutText.setWrapStyleWord(true);
		aboutText.setEditable(false);
		aboutText.setBackground(Color.white);
		aboutText.setFont(new Font("Arial", 0, 14));
		aboutText.setBorder(BorderFactory.createBevelBorder(1));
		aboutPanel.add(aboutText,BorderLayout.CENTER);
		aboutFrame.add(aboutPanel);
		aboutFrame.pack();
		
		easy.setActionCommand ("easy");
		easy.addActionListener(this);
		medium.setActionCommand ("medium");
		medium.addActionListener(this);
		hard.setActionCommand ("hard");
		hard.addActionListener(this);
		exitOption.setActionCommand ("Exit");
		exitOption.addActionListener(this);
		options.setActionCommand ("Options");
		options.addActionListener(this);
		about.setActionCommand("About");
		about.addActionListener(this);
		
		mainMenu = new JMenuBar ();
		mainMenu.add (gameMenu);
		mainMenu.add(optionsMenu);
		mainMenu.add(aboutMenu);
		gameFrame.setJMenuBar (mainMenu);
		gameFrame.setJMenuBar (mainMenu);
		
		gameFrame.add(gamePanel);
		gameFrame.pack();
		gameFrame.setVisible(true);
		
		optionsFrame = new JFrame("Options"); //options menu
		optionsFrame.setResizable(false);
		optionsFrame.setLocation(450,0);
		optionsPanel = new JPanel();
		optionsPanel.setPreferredSize(new Dimension(400,600));
		optionsPanel.setBackground(new Color(173,216,230));
		optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		optionsPanel.setLayout(new BoxLayout(optionsPanel,BoxLayout.PAGE_AXIS));
		searchPanel = new JPanel();
		searchPanel.setPreferredSize(new Dimension(400,300));
		searchPanel.setMaximumSize(new Dimension(400,300));
		searchPanel.setMinimumSize(new Dimension(400,300));
		searchPanel.setBackground(Color.LIGHT_GRAY);
		searchPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		searchPlayer = new JLabel();
		searchPlayer.setSize(new Dimension(300,25));
		searchPlayer.setFont(new Font("Arial",1,18));
		searchPlayer.setText("Search for a Player:");
		
		promptPanel = new JPanel();
		promptPanel.setPreferredSize(new Dimension(300,30));
		promptPanel.setMaximumSize(new Dimension(300,30));
		promptPanel.setMinimumSize(new Dimension(300,30));
		promptPanel.setBackground(Color.white);
		promptPanel.setLayout(new BorderLayout());
		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(200,30));
		nameField.setMinimumSize(new Dimension(200,30));
		nameField.setMaximumSize(new Dimension(200,30));
		nameField.setFont(new Font("Arial",0,24));
		nameField.setBorder(BorderFactory.createLineBorder(Color.black));
		promptPanel.add(nameField,BorderLayout.WEST);
		searchButton = new JButton("Enter");
		searchButton.setActionCommand("search");
		searchButton.addActionListener(this);
		searchButton.setPreferredSize(new Dimension(100,30));
		promptPanel.add(searchButton,BorderLayout.EAST);
		searchText = new JTextArea();
		searchText.setPreferredSize(new Dimension(300,100));
		searchText.setMinimumSize(new Dimension(300,100));
		searchText.setMaximumSize(new Dimension(300,100));
		searchText.setLineWrap(true);
		searchText.setWrapStyleWord(true);
		searchText.setEditable(false);
		searchText.setBackground(Color.white);
		searchText.setFont(new Font("Arial",1,18));
		searchText.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		deleteButton = new JButton("Delete Score");
		deleteButton.setActionCommand("delete");
		deleteButton.addActionListener(this);
		deleteButton.setPreferredSize(new Dimension(200,50));
		deleteButton.setMinimumSize(new Dimension(200,50));
		deleteButton.setMaximumSize(new Dimension(200,50));
		deleteButton.setVisible(false);
		searchPanel.add(searchPlayer);
		searchPanel.add(promptPanel);
		searchPanel.add(searchText);
		searchPanel.add(deleteButton);
		
		optionsText = new JTextArea();
		optionsText.setPreferredSize(new Dimension(380,250));
		optionsText.setMinimumSize(new Dimension(380,250));
		optionsText.setMaximumSize(new Dimension(380,250));
		optionsText.setLineWrap(true);
		optionsText.setWrapStyleWord(true);
		optionsText.setEditable(false);
		optionsText.setBackground(Color.white);
		optionsText.setFont(new Font("Arial",1,18));
		optionsText.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		optionsPanel.add(optionsText);
		optionsPanel.add(searchPanel);
		optionsFrame.add(optionsPanel);
		optionsFrame.pack();
		leaderboardIn(); //load in the leaderboard from the leaderboard file
	}
	
	/* Description: This method listens for ActionEvents created by JLabel components and handles them accordingly, such as starting a new game or viewing/modifying
	 * the leaderboard, overwritten from ActionListener class
	 * Parameters: ActionEvent event - the ActionEvent created by a component
	 * Return: void - no value is returned as only global/instance variables are modified.
	 */
	public void actionPerformed (ActionEvent event) {
		String eventName = event.getActionCommand();
		if (eventName.equals ("Exit")) {			
			System.exit (0); //Exits the window
		}
		else if (eventName.equals ("easy")) { //starting a game at easy difficulty
			level = 52; //total number of cards is 52, 13 for each suit
			if(t != null) {
				t.reset(); //reset timer
			}
			t = new Timer(); //start a new timer
			newGame(); //instantiate a new game
		}
		else if (eventName.equals ("medium")) {
			level = 60; //total number of cards is 60, 15 for each suit
			if(t != null) {
				t.reset(); //reset timer
			}
			t = new Timer(); //start a new timer
			newGame(); //instantiate a new game
		}
		else if (eventName.equals ("hard")) {
			level = 68; //total number of cards is 68, 17 for each suit
			if(t != null) {
				t.reset(); //reset timer
			}
			t = new Timer(); //start a new timer
			newGame(); //instantiate a new game
		}
		else if (eventName.equals("About")) {
			aboutFrame.setVisible(true); //display the about frame
		}
		else if(eventName.equals("search")) { //searching for a name in the leaderboard
			searchName = null;
			if(nameField.getText() != null) {
				searchName = nameField.getText(); //get the user's input from the nameField JTextField
				int score = search(nameField.getText()); //calls the search method and gets the associated score of the name in the leaderboard
				if(score >= 0) {
					searchText.setText(String.format("Player: %s%n%nHigh Score: %s", nameField.getText(),score)); //set the searchText JTextArea in the optionsPanel to display the corresponding player and score
					deleteButton.setVisible(true); //show the delete score button
				}
				else {
					searchText.setText("Player not found."); //set the searchText JTextArea in the optionsPanel to indicate the player is not found
					deleteButton.setVisible(false); //hide the delete score button
				}
			}
		}
		else if(eventName.equals("delete")) { //deleting a player from the leaderboard
			searchText.setText("Removed score."); //the delete button is only set visible if the player can be found, indicate that the player is deleted
			nameField.setText(null);
			leaderboard.remove(searchName); //remove the player from the leaderboard map
			leaderboardOut(); //save the leaderboard to file
			deleteButton.setVisible(false); //hide the delete button
			updateLeaderboard(); //updates the optionsFrame to the latest leaderboard results
		}
		else if (eventName.equals("Options")) {
			optionsFrame.setVisible(true); //displays the optionsFrame JFrame
			updateLeaderboard(); //updates the optionsFrame to the latest leaderboard results
		}
	}
	
	/* Description: This method is responsible for updating the text displaying the leaderboard in the options menu
	 * Parameters: N/A - references global variables only, including leaderboard and optionsText
	 * Return: void - only the JTextArea and the leaderboard file are modified.
	 */
	public void updateLeaderboard() {
		ArrayList <String> keySet = new ArrayList<>(leaderboard.keySet());
		MapSortByScore sort = new MapSortByScore(leaderboard); //interface for sorting scores
		Collections.sort(keySet,sort);
		optionsText.setText("High Scores:\n");
		for(int i=0; i<5; i++) {
			if(i == keySet.size()) { //if there are less than 5 scores, break such that the current scores are displayed without error
				break;
			}
			optionsText.append(String.format("%n%s\t %s",keySet.get(i),leaderboard.get(keySet.get(i)))); //add a new line indicating the next player and score
		}
	}
	
	/* Description: Initializes a new Solitaire game and randomly distributes cards into each pile.
	 * Parameters: N/A - this method only references global variables
	 * Return: void - this method does not return any value as it only initializes all variables for the Solitaire game.
	 */
	public void newGame() {
		gamePanel.removeAll(); //remove all elements from the gamePanel, including the title screen
		Suit.resetAll(); //reset all variables in the Suit class
		lastSelected = null; //reset the previously selected card
		Card.resetSelected(); //reset the currently selected card in the Card class
		
		topPanel = new JPanel();
		topPanel.setMinimumSize(new Dimension(900,110));
		topPanel.setMaximumSize(new Dimension(900,110));
		topPanel.setLayout(new BorderLayout());
		topPanel.setBackground(new Color(34,177,76));
		gamePanel.add(topPanel);
		tableauPanel = new JPanel(); //stores the JPanel from each Pile
		tableauPanel.setMaximumSize(new Dimension(550,600));
		tableauPanel.setMinimumSize(new Dimension(550,600));
		tableauPanel.setBackground(new Color(34,177,76));
		gamePanel.add(tableauPanel);
		
		ArrayList <Card> cards = new ArrayList<>();
		for(int i=0; i<level; i++) { //instantiate the corresponding total amount of cards and store them in a temporary ArrayList
			cards.add(new Card(i,level));
			cards.get(i).getLabel().addMouseListener(this); //add a mouse listener to each card for click detection
		}
		for(int i=level-1; i>=0; i--) { //shuffle every card into a random position
			int swap = (int)Math.round(Math.random()*i);
			Card prev = cards.get(swap);
			cards.set(swap, cards.get(i));
			cards.set(i, prev);
		}
		tableau = new Pile[7]; //create a new array for the tableau array	
		for(int i=1; i<=7; i++) { //for each pile, add an increasing amount of cards starting from 1 card to 7 cards per pile
			tableau[i-1] = new Pile(new LinkedList<>(cards.subList(0, i)),i-1);
			cards.removeAll(cards.subList(0, i));
			tableauPanel.add(tableau[i-1].getPanel());
			tableau[i-1].getPanel().setBackground(new Color(34,177,76));
			tableau[i-1].updateOrder();
			tableau[i-1].getBase().addMouseListener(this); //add a mouse detector to detect when an empty pile is selected
		}
		stock = new Stock(cards); //instantiate a new Stock
		stockPanel = stock.getStockPanel();
		topPanel.add(stockPanel,BorderLayout.WEST);
		stock.getUnusedLabel().addMouseListener(this);
		
		suitPanel = Suit.getSuitPanel();
		suitPanel.setPreferredSize(new Dimension(350,120));
		suitPanel.setMinimumSize(new Dimension(350,120));
		suitPanel.setMaximumSize(new Dimension(350,120));
		suitPanel.setBackground(Color.LIGHT_GRAY);
		suitPanel.setBorder(BorderFactory.createBevelBorder(0));
		for(int i=0; i<4; i++) { //create four new suits and add them to the Suit map
			new Suit(i);
			Suit.getSuitMap().get(i).getBase().addMouseListener(this);
		}
		
		timerLabel = new JLabel(); //timer JFrame components
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timerLabel.setFont(new Font("Arial",0,32));
		timerLabel.setPreferredSize(new Dimension(100,60));
		timerLabel.setMinimumSize(new Dimension(100,60));
		timerLabel.setMaximumSize(new Dimension(100,60));
		timerLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		timerLabel.setOpaque(true);
		timerLabel.setBackground(Color.gray);
		topRight = new JPanel();
		topRight.setPreferredSize(new Dimension(460,120));
		topRight.setMinimumSize(new Dimension(460,120));
		topRight.setMaximumSize(new Dimension(460,120));
		topRight.setLayout(new BoxLayout(topRight,BoxLayout.LINE_AXIS));
		topRight.add(timerLabel);
		topRight.add(suitPanel);
		topRight.setBackground(null);
		topPanel.add(topRight,BorderLayout.EAST);
		gameFrame.validate();
		gameFrame.repaint();
		timer = 0; //reset the time elapsed to 0
		t.start(); //start the timer
	}
	
	/* Description: This method loads the saved leaderboard from the leaderboard.txt file.
	 * Parameters: N/A - this method does not reference external variables to perform its function
	 * Return: void - only the leaderboard TreeMap is modified
	 */
	public void leaderboardIn() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("leaderboard.txt"));
			String line;
			while((line = reader.readLine()) != null) {
				leaderboard.put(line.substring(0,line.lastIndexOf(" ")), Integer.parseInt(line.substring(line.lastIndexOf(" ")+1))); //the name can be any string above 0 length, but the last characters after a space is the associated score
			}
			reader.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("Leaderboard not found.");
			System.exit(0); //the game cannot function without the leaderboard
		}
		catch(IOException e) {
			System.out.println("IOException occurred.");
			System.exit(0);
		}
	}
	
	/* Description: This method updates and writes the leaderboard back into the leaderboard file.
	 * Parameters: N/A - this method does not reference external variables to perform its function
	 * Return: void - only the leaderboard TreeMap is modified
	 */
	public void leaderboardOut() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("leaderboard.txt"));
			
			Iterator<String> iter = leaderboard.keySet().iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				writer.write(key+" "+leaderboard.get((Object)key)+"\n"); //write a new line for each player
			}
			writer.close();
		}
		catch(IOException e) {
			System.out.println("IOException occurred.");
		}
	}
	
	/* Description: This method is responsible for searching for a specific player within the leaderboard
	 * Parameters: String s - name of the player
	 * Return: int - returns the associated score for the searched player, if the player is not found, it will return a negative value from the found variable
	 */
	public int search(String s) {
		int found = Collections.binarySearch(new ArrayList <String>(leaderboard.keySet()),s);
		if(found >= 0) {
			return leaderboard.get(s);
		}
		return found;
	}
	
	/* Description: This method is responsible for click detection and the movement of selected cards. The reason why the mouseReleased method is used is because
	 * it will always be called after the mousePressed method.
	 * Parameters: MouseEvent e - mouse event of the associated component clicked
	 * Return: void - all variables modified/referenced are global/instance
	 */
	public void mouseReleased(MouseEvent e) {
		if(e.getSource().equals(stock.getUnusedLabel())) { //if the unused pile is selected in the stock
			Card.resetSelected();
			Pile.resetSelected();
			lastSelected = null;
		}
		else if(lastSelected == null && Card.getSelected() != null && !Card.getSelected().getFlipped()) { //detects selection of a first card
			lastSelected = Card.getSelected();
			lastSelected.getLabel().setBorder(BorderFactory.createLineBorder(Color.orange, 3, true));
		}
		else if(lastSelected.equals(Card.getSelected())) { //if the previously selected card is the same as the currently selected card
			//if a suit or column is selected but already has cards on it: (the JLabel indicating an empty suit/column is covered by cards but this is added for redundancy's sake)
			if(Pile.getSelected() != null && Pile.getSelected().getCards().size() > 0 && Suit.getSelected() != null && Suit.getSelected().getCards().size() > 0) {
				Card.resetSelected();
				Suit.resetSelected();
			}
			//if the card is placed on an empty column in the tableau:
			else if(lastSelected.getValue() == level/4 && !lastSelected.getLabel().equals(e.getSource()) && Pile.getSelected() != null) {
				moveCard(Pile.getSelected());
				Pile.resetSelected();
			}
			//if an ace card is placed on an empty suit:
			else if(lastSelected.getValue() == 1 && !lastSelected.getLabel().equals(e.getSource()) && Suit.getSelected() != null && Suit.getSelected().getSuit() == lastSelected.getSuit()){
				if(lastSelected.getPileNum() < 7) {
					tableau[lastSelected.getPileNum()].removeCard(lastSelected);
				}
				else { //if the ace card originated from the stock:
					stock.removeCard();
				}
				Suit.getSelected().addCards(lastSelected);
				gamePanel.revalidate();
				gamePanel.repaint();
				if(Suit.getTotal() == level) { //detect the total number of cards added and determine whether the game is finished or not
					finished();
				}
				Suit.resetSelected(); //reset the selected suit
			}
			else {
				//reset selected Card before proceeding with the next else statement
				Card.resetSelected();
			}
			lastSelected.getLabel().setBorder(null);
			lastSelected = null;
		}
		else {
			//if a card from the tableau or stock is placed onto one of the suits if the suit is not empty:
			if(Card.getSelected().getPileNum() > 6 && Card.getSelected().getPileNum() < 11 && lastSelected.getSuit() == Suit.getSuitMap().get(Card.getSelected().getPileNum()-7).getSuit() && Card.getSelected().getValue()+1 == lastSelected.getValue()) {
				if(lastSelected.getPileNum() == 11) { //if the card moved is from the stock
					stock.removeCard();
				}
				else {
					tableau[lastSelected.getPileNum()].removeCard(lastSelected);
				}
				Suit.getSuitMap().get(Card.getSelected().getPileNum()-7).addCards(lastSelected);
				gamePanel.revalidate();
				gamePanel.repaint();
				if(Suit.getTotal() == level) { //check the total number of cards in all Suits to determine whether the game is finished
					finished();
				}
			}
			//if the card is moved to a card within the tableau:
			else if(Card.getSelected().getPileNum() < 7 && Card.getSelected().equals(tableau[Card.getSelected().getPileNum()].getTopCard()) && Card.getSelected().getValue() == lastSelected.getValue()+1 && Card.getSelected().getSuit()%2 != lastSelected.getSuit()%2) {
				moveCard(tableau[Card.getSelected().getPileNum()]);
			}
			Card.resetSelected();
			Suit.resetSelected();
			lastSelected.getLabel().setBorder(null);
			lastSelected = null;
		}
		gamePanel.repaint();
	}
	
	/* Description: Method responsible for moving cards originating from the tableau
	 * Parameters: Pile p - the pile the last selected card is being added to
	 * Return: void - the method only modifies global/instance variables
	 */
	public void moveCard(Pile p) {
		int n = lastSelected.getPileNum();
		if(n<7) { //if the card moved is within the tableau
			Deque <Card> subList = new LinkedList<>();
			while(!tableau[n].getCards().peek().equals(lastSelected)){
				subList.offer(tableau[n].getCards().peek());
				tableau[n].removeCard(tableau[n].getCards().peek());
			}
			subList.offer(tableau[n].getCards().peek());
			tableau[n].removeCard(tableau[n].getCards().peek());
			p.addCards(subList);
		}
		else if(n>6 && n<11) { //if the card moved originates from the suit piles
			p.addCards(lastSelected);
			Suit.getSuitMap().get(n-7).removeCard(lastSelected);
			if(Suit.getSuitMap().get(n-7).getCards().peek() == null) {
				Suit.getSuitMap().get(n-7).getBase().addMouseListener(this);
			}
		}
		else { //if the card originates from the stock
			p.addCards(lastSelected);
			stock.removeCard();
		}
		gamePanel.repaint();
	}
	
	/* Description: This method is called when the game is finished and asks the player for their name to record their score in the leaderboard.
	 * Parameters: null - this method only references global/instance variables
	 * Return: void - this method only modifies the global/instance variables
	 */
	public void finished() {
		t.reset(); //stop timer
		int score = level*1200/(timer); //calculate score
		String s = ""; //Player name
		while(s != null && s.length() == 0) { //empty string is invalid name
			s = JOptionPane.showInputDialog(gamePanel, String.format("Game completed in: %d seconds%nScore: %d%nPlease enter your name:", timer,score), "Game Complete!", JOptionPane.INFORMATION_MESSAGE);
			if(s != null && s.length() == 0) { //if the player cancels, do not record a name
				JOptionPane.showMessageDialog (gamePanel, "Please enter a valid name.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		if(s != null && leaderboard.get(s) < score) { //record score only if the score is higher than an existing one associated with the player's name
			leaderboard.put(s, score); 
			leaderboardOut(); //update leaderboard to file
			updateLeaderboard(); //update leaderboard within JFrames and JFrame components
		}
		gamePanel.removeAll(); 
		gamePanel.add(titleScreen); //return to title screen
	}
	
	/* Description: This method is a subclass that is responsible for implementing threads for a timer, and increases the value of the timer variable per second.
	 */
	public class Timer extends Thread{
		public boolean interrupted = false;
		/* Description: This method is responsible for indicating the thread to stop running
		 * Parameters: only the global, instance variables are referenced
		 * Return: void - only the global, instance variables are modified
		 */
		public void run() {
			while(!interrupted) {
				timerLabel.setText(Integer.toString(timer)); //update the timerLabel in-game to the current time
				try{
					timer++;
					sleep (1000); //wait for one second
				}
				catch (Exception e){}
			}
		}
		
		/* Description: This method is responsible for indicating the thread to stop running
		 * Parameters: only the global, instance variables are referenced
		 * Return: void - only the global, instance variables are modified
		 */
		public void reset() {
			interrupted = true;
		}
	}
	
	//main code
	public static void main(String[] args){
		new Driver();
	}
}