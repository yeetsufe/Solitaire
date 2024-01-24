/**Name: Jim Wu
 * Date: 1/25/2022
 * Description: This class stores the cards the player inserts into each of the suit piles and is responsible for inserting cards into each of the suit piles.
 * This class also stores each card stored in each suit pile in a static map variable for the sake of convenience and is a subclass of the Pile class as they
 * exhibit similar purposes.
 */
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Suit extends Pile implements MouseListener{
	private static int totalCards = 0; //total number of cards stored in all of the suit piles
	private static HashMap <Integer,Suit> suitMap = new HashMap <>(); //Hashmap storing all of the suit instances
	private static Suit selectedSuit = null; //currently selected suit instance
	private static JPanel suitPanel = new JPanel();
	private int suit; //the suit the pile instance represents. Diamonds is 0, clubs is 1, hearts is 2, spades is 3.
	
	/* Description: Constructor
	 * Parameters: int suit - the suit the pile instance represents.
	 */
	public Suit(int suit) {
		super(new LinkedList <Card>(),suit+7); //instantiate superclass, the suit's pile number must be outside of the 6 piles in the tableau
		suitMap.put(suit, this);
		this.base = new JLabel(new ImageIcon(new ImageIcon ("i"+suit+".png").getImage().getScaledInstance(70,95,java.awt.Image.SCALE_SMOOTH))); //image used to represent an empty suit pile
		this.base.setBorder(BorderFactory.createBevelBorder(1));
		this.base.setBounds(0,0,70,95);
		this.base.addMouseListener(this);
		suitPanel.add(base);
		this.suit = suit;
	}
	
	/* Description: This method adds a card to the top of the suit pile and displays the updated pile accordingly. This method is overwritten from the Pile class.
	 * Parameters: Card c - the card being added
	 * Return: void - only the cards Deque is modified and calls the updateOrder method
	 */
	public void addCards(Card c) {
		c.setPileNum(this.getNum());
		this.cards.push(c);
		this.topCard = c;
		this.topCard.getLabel().addMouseListener(this);
		totalCards++;
		updateOrder(); //update appearance of this instance's suit pile
	}
	
	/* Description: This method removes the top card of the suit pile and displays the updated pile accordingly. This method is overwritten from the Pile class.
	 * Parameters: Card c - the card being removed
	 * Return: void - only the cards Deque is modified and calls the updateOrder method
	 */
	public void removeCard(Card c) {
		this.cards.pop();
		suitPanel.remove(c.getLabel());
		topCard = cards.peek();
		totalCards--;
		updateOrder(); //update appearance of this instance's suit pile
	}
	
	/* Description: This method is responsible for updating the JLabel representing the current suit and drawing the suit instance's top card. This method is
	 * overwritten from the Pile superclass
	 * Parameters: N/A - only instance variables and JLabels are modified
	 * Return: void - only instance variables and JLabels are modified
	 */
	public void updateOrder() {
		suitPanel.removeAll();
		if(this.cards.peek() == null) { //if there are no cards left in the cards Deque
			this.base = new JLabel(new ImageIcon(new ImageIcon ("i"+suit+".png").getImage().getScaledInstance(70,95,java.awt.Image.SCALE_SMOOTH))); //JLabel indicating an empty suit pile
			this.base.setBorder(BorderFactory.createBevelBorder(1));
			this.base.setBounds(0,0,70,95);
			this.base.addMouseListener(this); //detect when the pile instance is clicked when it is empty
		}
		else {
			this.base = topCard.getLabel(); //update JLabel to display the card underneath
		}
		for(int i=0; i<4; i++) {
			suitPanel.add(suitMap.get(i).base); //add all JLabels from all suits back into the suitPanel to update their appearance
		}
	}
	
	/* Description: This method is called when starting a new game to completely erase all previous suit instances stored in the suitMap and remove all cards stored.
	 * Parameters: N/A, all variables modified are static
	 * Return: N/A, no variables are modified outside of the static variables
	 */
	public static void resetAll() {
		totalCards = 0;
		suitMap = new HashMap <>();
		selectedSuit = null;
		suitPanel = new JPanel();
	}
	
	/* Description: Detects when an empty suit pile is selected and sets the selectedSuit variable to the current instance accordingly. This method is overwritten
	 * from the Pile class.
	 * Parameters: MouseEvent e - MouseEvent when the empty suit pile is selected
	 * Return: void - this method only reassigns the static variable
	 */
	public void mousePressed(MouseEvent e) {
		selectedSuit = this;
	}
	
	//Getters
	public int getSuit() {
		return this.suit;
	}
	
	public static JPanel getSuitPanel() {
		return suitPanel;
	}
	
	public static Suit getSelected() {
		return selectedSuit;
	}
	
	public static int getTotal() {
		return totalCards;
	}
	
	public static HashMap <Integer,Suit> getSuitMap(){
		return Suit.suitMap;
	}
}