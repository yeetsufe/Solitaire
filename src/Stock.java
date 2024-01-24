/**Name: Jim Wu
 * Date: 1/24/2022
 * Description: This class is responsible for storing all of the cards in the stock in a solitaire game. Cards can be iterated through the stock by clicking on the 
 * unused pile and can be moved to the tableau or directly to the suit piles (the method implementing those are in the Driver class).
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Stock extends MouseAdapter{
	private Stack <Card> unused; //unused pile of stock
	private Stack <Card> used; //used pile of stock
	private JLabel unusedLabel; //JLabel that represents the unused pile 
	private JLabel usedLabel; //JLabel that represents the used pile
	private JPanel stockPanel; //JPanel that stores both the usedLabel and unusedLabel JLabels
	
	//Constructor
	public Stock(ArrayList <Card> stack) {
		this.unused = new Stack<>();
		this.unused.addAll(stack); //instantiate all cards given into the unused pile stack
		this.used = new Stack<>(); //used pile is initially empty
		
		//setting appearance for JPanels, JLabelswaterloo ccc
		this.stockPanel = new JPanel();
		this.stockPanel.setPreferredSize(new Dimension(160,120));
		this.stockPanel.setMinimumSize(new Dimension(160,120));
		this.stockPanel.setMaximumSize(new Dimension(160,120));
		this.stockPanel.setBackground(new Color(34,177,76));
		this.stockPanel.setBorder(BorderFactory.createBevelBorder(1));
		this.stockPanel.setBackground(Color.LIGHT_GRAY);
		
		this.unusedLabel = new JLabel(new ImageIcon(new ImageIcon ("back.png").getImage().getScaledInstance(70,95,java.awt.Image.SCALE_SMOOTH))); //the unused pile is represented by flipped cards
		this.unusedLabel.setPreferredSize(new Dimension(70,95));
		this.unusedLabel.setMaximumSize(new Dimension(70,95));
		this.unusedLabel.setMinimumSize(new Dimension(70,95));
		this.unusedLabel.addMouseListener(this); //add mouse listener to detect when the stack will be iterated
		
		this.usedLabel = new JLabel();
		this.usedLabel.setPreferredSize(new Dimension(70,95));
		this.usedLabel.setMaximumSize(new Dimension(70,95));
		this.usedLabel.setMinimumSize(new Dimension(70,95));
		this.usedLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		
		this.stockPanel.add(this.unusedLabel); //add labels
		this.stockPanel.add(this.usedLabel);
	}
	
	//Getters
	public Stack <Card> getUsed() {
		return this.used;
	}
	
	public JLabel getUnusedLabel() {
		return this.unusedLabel;
	}
	
	public JPanel getStockPanel() {
		return this.stockPanel;
	}
	
	/* Description: This method removes the current top card in the used pile in the stock
	 * Parameters: N/A - the card removed is implied to be the top card in the used stack
	 * Return: void - the instance variables are the only variables modified
	 */
	public void removeCard() {
		used.pop();
		stockPanel.remove(usedLabel);
		if(used.size() == 0) {
			usedLabel = new JLabel();
			usedLabel.setPreferredSize(new Dimension(70,95));
			usedLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		}
		else {
			usedLabel = used.peek().getLabel();
		}
		stockPanel.add(usedLabel);
	}
	
	/* Description: This method detects when the unused pile is clicked and draws an additional card from the unused pile in the Stock in response.
	 * Parameters: MouseEvent e - The MouseEvent containing the information of the clicked event
	 * Return: void - only the used, unused, and JLabel instance variables are modified
	 */
	public void mousePressed(MouseEvent e) {
		if(unused.size() != 0) { //if the unused pile is not empty
			used.push(unused.pop()); //remove a card from the unused pile and add it to the used pile
			stockPanel.remove(usedLabel); //remove the Jlabel of the last card
			if(used.peek().getFlipped() || used.peek().getPileNum() != 11) { //flip the card right side up if the card is facing backward, set the pile number to 11 if not done already.
				used.peek().flip();
				used.peek().setPileNum(11);
			}
			usedLabel = used.peek().getLabel(); //get the new JLabel of the card from the newly added Card object
			stockPanel.add(usedLabel); //add the new usedLabel
			if(unused.size() == 0) { //if the unused pile is empty after cycling through the stock, modify the JLabel such that it indicates that it is empty
				unusedLabel.setIcon(null);
				unusedLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			}
		}
		else { //If there are no cards left in the unused pile, all of the cards are returned to the unused Stack in their respective order.
			if(used.size() == 0) { //if there are no cards left in the entire stock, the MouseListener is removed to prevent the stock from iterating again
				unusedLabel.removeMouseListener(this);
			}
			else {
				iterate();
			}
		}
		stockPanel.revalidate();
		stockPanel.repaint(); //repaint, validate layout of the components since this method is called outside of the Driver class
	}
	
	/* Description: This method returns all cards in the used stack back into the unused stack in their respective order and updates all JLabels accordingly.
	 * Parameters: void - the instance variables of the class are directly modified
	 * Return: void - only the instance variables of the class are modified and no value is returned.
	 */
	public void iterate() {
		stockPanel.removeAll(); //remove all components from the stockPanel
		while(used.size() != 0) { //add all cards from the used stack to the unused stack in their respective order
			unused.push(used.pop());
		}
		unusedLabel.setIcon(new ImageIcon(new ImageIcon ("back.png").getImage().getScaledInstance(70,95,java.awt.Image.SCALE_SMOOTH))); //the unused pile is represented by cards facing back
		unusedLabel.setBorder(null); 
		usedLabel = new JLabel(); //display the unusedLabel as empty
		usedLabel.setPreferredSize(new Dimension(70,95));
		usedLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		stockPanel.add(unusedLabel);
		stockPanel.add(usedLabel); //add both JLabels back in order to update them
	}
}
