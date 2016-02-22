import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

public class TurnedCardManager implements ActionListener
{
	// data fields
	boolean flag=true;
	private final String DESCRIPTION = "Turns: ";
	int numberofturnedcards=0;
	private Vector turnedCards;			
	private TurnCounterLabel turnCounterLabel;
	private Timer turnDownTimer;				
	private final int turnDownDelay = 2000;
	private int gamesize;
	private String username1;
	
	/**
	 * Constructor
	*/
	public TurnedCardManager(TurnCounterLabel turnCounterLabel,int sizeofcard,String username)
	{
		// save parameter
		this.turnCounterLabel = turnCounterLabel;
		// create list
		
		this.turnedCards = new Vector(2);
		// make timer object
		this.turnDownTimer = new Timer(this.turnDownDelay, this);
		this.turnDownTimer.setRepeats(false);
		gamesize=sizeofcard;
		username1=username;
	}
	
	/**
	 * Adds the card to the list, handles cards matching, starts timer to turn down
	 *
	 * @param card the new card to be added
	 * @return true
	*/
	private boolean doAddCard(Card card)
	{
		// add the card to the list
		this.turnedCards.add(card);
		if(this.turnedCards.size() == 2)
		{

			this.turnCounterLabel.increment();

			Card otherCard = (Card)this.turnedCards.get(0);

			if( otherCard.getNum() == card.getNum())
			{
				this.numberofturnedcards++;
				
				if(numberofturnedcards==gamesize)
				{
					this.turnCounterLabel.setText("YOU WIN !!!");
					try {
						dbconnection myConn=new dbconnection();
						if(gamesize==8)
						{
							myConn.insertquery(username1,this.turnCounterLabel.getnumturns(),"4x4",this.turnCounterLabel.getmoves());
						}
						else if(gamesize==16)
						{
							myConn.insertquery(username1,this.turnCounterLabel.getnumturns(),"4x8",this.turnCounterLabel.getmoves());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					flag=false;
				}
				if(this.turnCounterLabel.getnumturns()==this.turnCounterLabel.getmoves())
				{
						this.turnCounterLabel.setText("GAME OVER !!!No moves left");
						flag=false;
				}
				this.turnedCards.clear();
			}

			else {
				if(this.turnCounterLabel.getnumturns()==this.turnCounterLabel.getmoves())
				{
					
						this.turnCounterLabel.setText("GAME OVER !!!No moves left");
						flag=false;
				}
				this.turnDownTimer.start();
			}
			
			
		}
		return true;
	}
	
	/**
	 * The specified card wants to turn, add if currently less than 2 cards
	 *
	 * @param card the Card object that wants to turn
	 * @return true if the card is allowed to turn, otherwise false
	*/
	public boolean turnUp(Card card)
	{
		if(flag)
		{
		if(this.turnedCards.size() < 2) return doAddCard(card);
		}
		return false;
	}
	
	/**
	 * Remove the specified card from the list.
	 *
	 * @param card the Card object to be removed from the list of turned cards
	*/

	
	/**
	 * Invoked when timer event occurs, turns non-matching cards down
	 *
	 * @param e the timer event information
	*/
	public void actionPerformed(ActionEvent e)
	{
		// turn each card back down
		for(int i = 0; i < this.turnedCards.size(); i++ )
		{
			Card card = (Card)this.turnedCards.get(i);
			card.turnDown();
		}
		// forget the cards
		this.turnedCards.clear();
	}
	
}
