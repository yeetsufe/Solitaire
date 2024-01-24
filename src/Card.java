/**Name: Jim Wu
 * Date: 1/25/2022
 * Description: This class represents an instance of a card in a solitaire game and stores variables and methods relating to the card's value and suit 
 * and providing click detection for each card instantiated
 */
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Card extends MouseAdapter{
	private int suit,value,pileNum; //pileNum represents which pile the card is currently stored in. The suit variable stores the suit the pile instance represents. Diamonds is 0, clubs is 1, hearts is 2, spades is 3.
	private boolean flipped = true; //flipped is true if the card's value is facing down/hidden, vice versa
	private JLabel cardLabel; //JLabel of card instance
	private ImageIcon faceIcon, backIcon = new ImageIcon(new ImageIcon ("back.png").getImage().getScaledInstance(70,95,java.awt.Image.SCALE_SMOOTH)); //imagelabel of card facing backwards
	private static Card selected = null; //currently selected card
	
	/* Description: Card constructor
	 * Parameters:
	 * int assigned - a random number between 0 and the total number of cards that will determine the card's suit and value
	 * int total - total number of cards for the current game
	 */
	public Card(int assigned, int total) {
		this.suit = assigned/(total/4); //the suit is determined by the the assigned number divided by a quarter of the total number of cards
		this.value = assigned%(total/4)+1; //the value is determined by the modulo of the assigned number by a quarter of the total number of cards, added by 1 because the assigned number starts from 0
		this.faceIcon = new ImageIcon(new ImageIcon (Integer.toString(this.suit)+Integer.toString(this.value)+".png").getImage().getScaledInstance(70,95,java.awt.Image.SCALE_SMOOTH)); //create image icon or card facing upwards
		this.cardLabel = new JLabel(backIcon);
		this.cardLabel.setMinimumSize(new Dimension(70,95));
		this.cardLabel.setMaximumSize(new Dimension(70,95));
		this.cardLabel.setPreferredSize(new Dimension(70,95));
		this.cardLabel.addMouseListener(this);
	}
	
	//Setters
	public void setPileNum(int n) {
		this.pileNum = n;
	}
	
	//Getters
	public int getPileNum() {
		return this.pileNum;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public int getSuit() {
		return this.suit;
	}
	
	public boolean getFlipped() {
		return this.flipped;
	}
	
	public JLabel getLabel() {
		return this.cardLabel;
	}
	
	@Deprecated
	public int getSomeBitches() {
		return 0;
	}
	
	public static Card getSelected() {
		return selected;
	}
	
	/* Description: This method is responsible for determining whether one card is equal to another
	 * Parameters: Card card - the card instance the current instance is being compared to
	 * Return: boolean - false if they are not equal, true if they are equal
	 */
	public boolean equals(Card card) {
		return card.suit == this.suit && card.value == this.value;
	}
	
	/* Description: This method changes the card's JLabel to display the opposite side of the card.
	 * Parameters: N/A - the flipped instance variable already indicates whether the card has been flipped
	 * Return: void - only the JLabel is modified and does not require a returned value
	 */
	public void flip() {
		if(flipped) { //if the card is facing downwards
			this.cardLabel.setIcon(faceIcon);
			flipped = false;
		}
		else { //if the card is facing upwards
			this.cardLabel.setIcon(backIcon);
			flipped = true;
		}
	}
	
	/* Description: This method removes the yellow border indicating that a card has been selected from the currently selected card, then sets the currently selected
	 * card as null.
	 * Parameters: N/A - The currently selected card is already stored in the "selected" variable
	 * Return: void - Only the "selected" variable is modified, accessible for the entire instance.
	 */
	public static void resetSelected() {
		if(selected != null) {
			selected.getLabel().setBorder(null);
		}
		selected = null;
	}
	
	/* Description: This method detects when a user selects on the specific card instance and sets the "selected" variable to the current instance. This method is
	 * overwritten from MouseAdapter
	 * Parameters: MouseEvent e - MouseEvent created when the user clicks on the Card's JLabel
	 * Return: void - only the "selected" variable is modified.
	 */
	public void mousePressed(MouseEvent e) {
		selected = this;
	}
}
