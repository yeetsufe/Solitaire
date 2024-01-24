/**Name: Jim Wu
 * Date: 1/25/2022
 * Description: This class is responsible for storing cards in a specific order in a pile within the tableau and removing and adding cards to each pile instance.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Pile extends MouseAdapter{
	protected static Pile selectedPile = null; //currently selected pile if the pile
	protected Deque <Card> cards; //Deque storing the specific order of each card contained
	private JPanel pilePanel; //panel each card's JLabel is contained in
	protected Card topCard; //card instance at the head of the pile
	private int pileNum; //unique identifier for each pile
	protected JLabel base; //label indicating an empty pile
	
	/* Description: Constructor
	 * Parameters:
	 * LinkedList <Card> cards - all of the cards added to the queue in their respective order
	 * int pileNum - assigned identifier for each pile
	 */
	public Pile(LinkedList <Card> cards, int pileNum) {
		this.cards = cards;
		this.pilePanel = new JPanel();
		this.pilePanel.setPreferredSize(new Dimension(70, 600));
		this.pilePanel.setLayout(null);
		this.base = new JLabel();
		this.base.setPreferredSize(new Dimension(70,95));
		this.base.setBorder(BorderFactory.createLineBorder(Color.white, 3, true));
		this.pilePanel.add(this.base);
		this.base.setBounds(0,0,70,95);
		this.base.addMouseListener(this);
		
		this.topCard = cards.peekFirst();
		this.pileNum = pileNum;
		if(this.topCard != null) { 
			this.topCard.flip(); //each card is instantiated facing downwards and each card at the top of the pile is flipped facing upwards.
		}
	}
	
	/* Description: This method is responsible for drawing each card's JFrame in the cascading style of Solitaire and updating their order on the JFrame
	 * Parameters: N/A - this method updates the order for all of the cards since the JLabel that is painted first will always overlap proceeding components painted,
	 * thus each card has to be repainted in a pile when a card is added/removed
	 */
	public void updateOrder() {
		this.pilePanel.removeAll(); //remove all JLabels from the pilePanel JPanel
		Iterator <Card> iter = cards.iterator();
		int drawIndex = cards.size()-1; //the relative y-position of the corresponding card drawn
		while(iter.hasNext()) {
			Card next = iter.next();
			next.setPileNum(this.pileNum);
			pilePanel.add(next.getLabel());
			next.getLabel().setBounds(0,30*drawIndex,70,95); //the first card is drawn first, then all proceeding cards in their respective positions.
			drawIndex--;
		}
		this.pilePanel.add(base);
	}
	
	/* Description: Removes a specific card from the pile
	 * Parameters: Card c - the card being removed
	 * Return: void - the "cards" instance variable is the only variable modified
	 */
	public void removeCard(Card c) {
		cards.remove(c);
		pilePanel.remove(c.getLabel()); //the JLabel and Card object are removed from the pilePanel and cards Deque
		topCard = cards.peekFirst();
		if(topCard != null && topCard.getFlipped()) { //if the new top card is facing downwards, flip and reveal the new top card.
			topCard.flip();
		}
	}
	
	/* Description: adds a sub-pile of cards to the top of the pile
	 * Parameters: Deque <Card> d - the cards being added on top of the pile in their respective order
	 * Return: void - Only the cards Deque instance variable is modified
	 */
	public void addCards(Deque <Card> d) {
		d.addAll(cards); //the current order of cards have to be added to the Deque added in order to be in the correct order
		cards = d;
		updateOrder(); //repaint all the cards
		this.topCard = cards.getFirst();
	}
	
	/* Description: Adds a single card to the top of the pile
	 * Parameters: Card c - the card being added
	 * Return: void - the "cards" instance variable is the only variable modified
	 */
	public void addCards(Card c) {
		c.setPileNum(this.pileNum);
		cards.addFirst(c);
		updateOrder(); //repaint all the cards
		this.topCard = cards.getFirst();
	}
	
	//Getters
	public JPanel getPanel() {
		return this.pilePanel;
	}
	
	public int getNum() {
		return this.pileNum;
	}
	
	public Card getTopCard() {
		return this.topCard;
	}
	
	public JLabel getBase() {
		return this.base;
	}
	
	public Deque <Card> getCards(){
		return this.cards;
	}
	
	public static Pile getSelected() {
		return selectedPile;
	}
	
	/* Description: This method detects when a empty pile instance is clicked by the user. This method is overwritten from the MouseAdapter class.
	 * Parameters: MouseEvent e - MouseEvent created when the user clicks on an empty pile
	 * Return: void - only the "selectedPile" variable is modified.
	 */
	public void mousePressed(MouseEvent e) {
		selectedPile = this;
	}
	
	public static void resetSelected() {
		selectedPile = null;
	}
}