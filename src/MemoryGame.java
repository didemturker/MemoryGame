import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.*;
import javax.xml.soap.Text;

import java.util.Arrays;
import java.util.Random;
import javax.swing.JOptionPane;
public class MemoryGame implements ActionListener
{
	// data fields
	private JFrame mainFrame;					// top level window
	private JFrame mainFrame2;
	private Container mainContentPane;			// frame that holds card field and turn counter
	private TurnCounterLabel turnCounterLabel;
	private ImageIcon cardIcon[]; // 0-7 are faces, 8 is back
	private String ImageDirectory="Penguins";
	private String nameofuser;
	private JTextField logname;
	private JPanel panellogin;
	/**
	 * Make a JMenuItem, associate an action command and listener, add to menu
	*/
	private static void newMenuItem(String text, JMenu menu, ActionListener listener)
	{
		JMenuItem newItem = new JMenuItem(text);
		newItem.setActionCommand(text);
		newItem.addActionListener(listener);
		menu.add(newItem);
	}
	
	/**
	 * Loads the card icons from GIF files
	 *
	 * @return ImageIcon[9] containing the card icons
	*/
	
	private ImageIcon[] loadCardIcons()
	{
		// allocate array to store icons
		ImageIcon icon[] = new ImageIcon[17];
		// for each icon
		for(int i = 0; i < 17; i++ )
		{
			// make a new icon from a cardX.gif file
			String fileName = "images/"+ImageDirectory+"/card" + i + ".jpg";
			icon[i] = new ImageIcon(fileName);
			// unable to load icon
			if(icon[i].getImageLoadStatus() == MediaTracker.ERRORED)
			{
				// inform the user of the error and then quit
				JOptionPane.showMessageDialog(this.mainFrame
					, "The image " + fileName + " could not be loaded."
					, "Matching Game Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		
		}
		return icon;
	}
	
	/**
	 * Default constructor loads card images, makes window
	*/
	public MemoryGame()
	{
		// make toplevel window
		this.mainFrame2= new JFrame("Login");
		this.mainFrame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame2.setSize(400, 500);
		
		// make counter label
		this.panellogin = new JPanel();
		mainFrame2.setContentPane(panellogin);
		this.logname=new JTextField();
		panellogin.add(new JLabel("Enter your name for login"));
		panellogin.add(logname);
		logname.setPreferredSize(new Dimension(200, 30));
		JButton Lgbutton=new JButton("Login");
		panellogin.add(Lgbutton);
		Lgbutton.addActionListener(this);
		
	}
	
	/**
	 * Handles menu events.  Necessary for implementing ActionListener.
	 *
	 * @param e object with information about the event
	*/
	public void actionPerformed(ActionEvent e)
	{
		dprintln("actionPerformed " + e.getActionCommand());
		if(e.getActionCommand().equals("New Game 4x4"))
			{
			newGame("New Game 4x4");
			this.mainFrame.setSize(400, 500);
			}
		else if(e.getActionCommand().equals("New Game 4x8"))
			{
			newGame("New Game 4x8");
			this.mainFrame.setSize(800, 500);
			}
		else if(e.getActionCommand().equals("Penguins"))
		{	
			
			this.ImageDirectory="Penguins";
			this.cardIcon = loadCardIcons();
			newGame("New Game 4x4");
			this.mainFrame.setSize(400, 500);
		}
		else if(e.getActionCommand().equals("Cartoons"))
				{
					
					this.ImageDirectory="Cartoons";
					this.cardIcon = loadCardIcons();
					newGame("New Game 4x4");
					this.mainFrame.setSize(400, 500);
				}
		else if(e.getActionCommand().equals("cardImages"))
		{
			this.ImageDirectory="cardImages";
			this.cardIcon = loadCardIcons();
			newGame("New Game 4x4");
			this.mainFrame.setSize(400, 500);
		}
		else if(e.getActionCommand().equals("20"))
		{
			this.turnCounterLabel.setmoves(20);
			newGame("New Game 4x4");
			this.mainFrame.setSize(400, 500);
		}
		else if(e.getActionCommand().equals("40"))
		{
			this.turnCounterLabel.setmoves(40);
			newGame("New Game 4x4");
			this.mainFrame.setSize(400, 500);
		}
		else if(e.getActionCommand().equals("60"))
		{
			this.turnCounterLabel.setmoves(60);
			newGame("New Game 4x4");
			this.mainFrame.setSize(400, 500);
		}
		else if(e.getActionCommand().equals("limitless"))
		{
			this.turnCounterLabel.setmoves(9999);
			newGame("New Game 4x4");
			this.mainFrame.setSize(400, 500);
		}
		else if(e.getActionCommand().equals("Login"))
		{
			if(logname.getText().equals(""))
			{
				 JOptionPane.showMessageDialog(null,"Please Enter your login name","Message",1);
			}
			else
			{
			this.mainFrame2.setVisible(false);
			this.mainFrame = new JFrame("Matching Game");
			this.mainFrame.setVisible(true);
			this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.mainFrame.setSize(400,500);
			this.mainContentPane = this.mainFrame.getContentPane();
			this.mainContentPane.setLayout(new BoxLayout(this.mainContentPane, BoxLayout.PAGE_AXIS));
			this.turnCounterLabel = new TurnCounterLabel();
			String[] directories=this.ReadDirectory();
			// Menu bar
			JMenuBar menuBar = new JMenuBar();
			this.mainFrame.setJMenuBar(menuBar);
			// Game menu
			JMenu gameMenu = new JMenu("Game");
			menuBar.add(gameMenu);
			newMenuItem("New Game 4x4", gameMenu, this);
			newMenuItem("New Game 4x8",gameMenu,this);
			newMenuItem("Show Scores", gameMenu, this);
			newMenuItem("Exit", gameMenu, this);
			JMenu gameMenu2 = new JMenu("Categories");
			menuBar.add(gameMenu2);
			for(String item:directories)
			newMenuItem(item, gameMenu2, this);
			JMenu gameMenu3 = new JMenu("Turns");
			menuBar.add(gameMenu3);
			newMenuItem("limitless", gameMenu3, this);
			newMenuItem("20", gameMenu3, this);
			newMenuItem("40",gameMenu3,this);
			newMenuItem("60",gameMenu3,this);
			

			this.cardIcon = loadCardIcons();
			this.nameofuser=this.logname.getText();
			this.newGame("New Game 4x4");
			}
		}
		else if(e.getActionCommand().equals("Show Scores"))
		{
			try {
				dbconnection myconn=new dbconnection();
				JTable table=myconn.showquery();
				JScrollPane tableContainer=new JScrollPane(table);
				this.turnCounterLabel.reset();
				this.mainContentPane.removeAll();
				mainContentPane.add(tableContainer);
				mainFrame.setSize(400,501);		
				} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		
		else if(e.getActionCommand().equals("Exit")) System.exit(0);
	}
	
	/**
	 * Prints debugging messages to the console
	 *
	 * @param message the string to print to the console
	 */
	static public void dprintln( String message )
	{
		//System.out.println( message );
	}
	
	/**
	 * Randomize the order of elements in an int array
	 *
	 * The method iterates over the array.  For each position in the array,
	 * another position is chosen randomly and the two integer values are
	 * exchanged.
	 *
	 * @param a the int[] to randomize
	*/
	public static void randomizeIntArray(int[] a)
	{
		Random randomizer = new Random();
		// iterate over the array
		for(int i = 0; i < a.length; i++ )
		{
			// choose a random int to swap with
			int d = randomizer.nextInt(a.length);
			// swap the entries
			int t = a[d];
			a[d] = a[i];
			a[i] = t;
		}
	}
	
	/**
	 * Makes a new set of cards and puts them in a new card field (a JPanel)
	 *
	 * @return new panel populated with new cards
	*/
	public JPanel makeCards()
	{
		// make the panel to hold all of the cards
		JPanel panel = new JPanel(new GridLayout(4, 4));
		// this set of cards must have their own manager
		TurnedCardManager turnedManager = new TurnedCardManager(this.turnCounterLabel,8,nameofuser);
		// all cards have same back
		ImageIcon backIcon = this.cardIcon[16];
		
		// make an array of card numbers: 0, 0, 1, 1, 2, 2, ..., 7, 7
		int cardsToAdd[] = new int[16];
		for(int i = 0; i < 8; i++)
		{
			cardsToAdd[2*i] = i;
			cardsToAdd[2*i + 1] = i;
		}
		// randomize the order of the array
		randomizeIntArray(cardsToAdd);
		
		// make each card object
		for(int i = 0; i < cardsToAdd.length; i++)
		{
			// number of the card, 0-7, randomized
			int num = cardsToAdd[i];
			// make the card object
			Card newCard = new Card(turnedManager, this.cardIcon[num], backIcon, num);
			// add it to the panel
			panel.add(newCard);
		}
		// return the filled panel
		return panel;
	}
	
	/**
	 * Prepares a new game (first game or non-first game)
	*/
	public void newGame(String size)
	{
		// reset the turn counter to zero
		this.turnCounterLabel.reset();
		// clear out the content pane (removes turn counter label and card field)
		this.mainContentPane.removeAll();
		// make a new card field with cards, and add it to the window
		if(size=="New Game 4x8")
			this.mainContentPane.add(makeCards2());
		else
			this.mainContentPane.add(makeCards());
		// add the turn counter label back in again
		this.mainContentPane.add(this.turnCounterLabel);
		// show the window (in case this is the first game)
		
		this.mainFrame.setVisible(true);
		
	}
	public JPanel makeCards2()
	{
		// make the panel to hold all of the cards
		JPanel panel = new JPanel(new GridLayout(4, 8));
		// this set of cards must have their own manager
		TurnedCardManager turnedManager = new TurnedCardManager(this.turnCounterLabel,16,nameofuser);
		
		// all cards have same back
		ImageIcon backIcon = this.cardIcon[16];
		
		// make an array of card numbers: 0, 0, 1, 1, 2, 2, ..., 7, 7
		int cardsToAdd[] = new int[32];
		for(int i = 0; i < 16; i++)
		{
			cardsToAdd[2*i] = i;
			cardsToAdd[2*i + 1] = i;
		}
		// randomize the order of the array
		randomizeIntArray(cardsToAdd);
		
		// make each card object
		for(int i = 0; i < cardsToAdd.length; i++)
		{
			// number of the card, 0-7, randomized
			int num = cardsToAdd[i];
			// make the card object
			Card newCard = new Card(turnedManager, this.cardIcon[num], backIcon, num);
			// add it to the panel
			panel.add(newCard);
		}
		// return the filled panel
		return panel;
	}
	public String[] ReadDirectory()
	{
		File file = new File("./images");
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		return directories;
	}
	public void Startgame()
	{
		mainFrame2.setVisible(true);
	}
}
