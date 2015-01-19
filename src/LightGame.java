o/*	Ofek Gila
	April 22nd, 2014
	LightGame.java
	This program helps the user understand how light works in a fun way
*/
import java.awt.*;			// Imports
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Scanner;
import java.net.MalformedURLException;
import javax.sound.sampled.*;
import java.io.*;			// classes File, IOException
import javax.imageio.*;	// class ImageIO
import java.math.BigDecimal;
import java.lang.reflect.Array;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;

public class LightGame	{
	public JFrame LightGameFrame;
	
	public static void main(String[] args) {	// when I made snake.java, and I copied snake.java to have all the implements for this code, so don't
		LightGame LG = new LightGame();
		LG.run();
	}
	public void run(){
		LightGameFrame = new JFrame("Light Game");	// ask why I extend JApplet or implement all of those things ^_^
		LightGameFrame.setContentPane(new LightGamePanel());
		LightGameFrame.setSize(width(1000), height(820));		// Sets size of frame
		LightGameFrame.setResizable(false);						// Makes it so you can't resize the frame
		LightGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LightGameFrame.setVisible(true);
	}
	public int width(int w) {	// converts width and height to include boarders
		return w + 12;
	}
	public int height(int h) {
		return h + 31;
	}
}
class LightGamePanel	extends JPanel	{
	public JMenuBar menu;																// JMenuBar on top
	public JMenu file, edit, view, tools, help, speedupdrawing;							// JMenu on top
	public JMenuItem New, open, save, saveas, instructions, bbrcalc, bbrgraphclear;		// JMenuItems take you to pages
	
	public JCheckBox showpeak, showshades;												// toggle whether or not you show peaks / visible light shades
	public JSlider speedup;																// allows user to speed up drawing speed
	
	public JPanel WholePanel;															// JPanels for all of the pages
	public JPanel CenterPanel, BottomPanel;
	public JPanel Game;
	public JPanel WholeGame;
	public JPanel Instructions;
	public JPanel Start;
	public JPanel GameOver;
	public JPanel BBRCalc;
	public JPanel WinGame, LoseGame;
	public JPanel TeachPanel;
	
	public JPanel[] LightSources;														// holds each light source image
	public JComboBox LightSourcesBox;													// combobox for selecting light source
	
	public JLabel GameLabel1, GameLabel2, InstructionLabel, StartLabel, GameOverLabel, BBRCalcLabel;	// Labels for Pages
	public JLabel TempL, TempR;															// Labels for Temperature
	public JLabel TempLBBR;
	public JLabel WinGameLabel, LoseGameLabel;											// Labels for win / lose game pages
	public JLabel TurnOnLabel;															// Label saying what turn you are on
	
	public PlanckLaw PlanckL, PlanckR, PlanckBBR;										// Objects of PlanckLaw for calculating black body radiation
	public OGraph LeftGraph, RightGraph;												// Instances of the OGraph component for displaying graphs
	public OGraph BBRGraph;
	public MenuItems menuItems;															// Creates instance of MenuItems class
	
	public Timer LeftGraphDraw, RightGraphDraw, BBRGraphDraw;							// Creates timer for drawing graphs
	public double LeftGraphPoint, RightGraphPoint, BBRGraphPoint;						// Tells graph what point to draw
	public Object[][] LightSourceValues; // String name, double temperature, double[] spectrum, String[] description, double peak, double peakwavelength, String imagelocation
	
	public CardLayout lightsourcelayout;												// Creates layouts
	public BorderLayout centerpanel;
	public CardLayout wholepanel;
	public CardLayout wholegamepanel;

	public JPanel LightSource;															// Base JPanel for other light sources

	public int lightSourceOn;															// tells you what light source the user has currently selected
	public JButton Guess;																// JButton for guessing spectrum
	public JButton CreateGraph;															// JButton for creating the BBR Graph
	
	public int points;																	// keeps track of turn and of points
	public int turn;
	
	public int[] AnswerSequence;														// creates the answer sequence
	
	public JTextArea Description;														// text area for displaying description of each light source
	
	public JSlider BBRSlider;															// JSlider for black body radiation tool
	public Scanner input;																// Scanner input object for file input
	
	public boolean ShowPeak;															// boolean values for toggleable options
	public boolean ShowShades;
	public boolean ScaleGraphs;
	
	public double Grade;																// Grade percentage
	public int lightSourceL, lightSourceR, lightSourceBBG;								// what light source number is being displayed in each graph
	
	public double ArrayIncrement;														// amount to increment each turn by
	public int SpectrumGuess;															// int value of light source that you guessed
	
	public JButton StartGame;															// JButton to start game
	public double peak, peakWavelength;													// peak and peak wavelength value

	public JButton ContinueButton1, ContinueButton2;									// buttons to continue after losing or winning and to switch between the game and teach pages
	public JButton SwitchButton1, SwitchButton2;

	public ImageIcon Switch;															// ImageIcon for the switch image
	public boolean Teaching;															// boolean stating whether or not you are in the teach page

	public Color[] BallColors;															// array of ball colors for the game
	
	public LightGamePanel()	{		// Initialize Variables
		PlanckL = new PlanckLaw();
		PlanckR = new PlanckLaw();
		PlanckBBR = new PlanckLaw();
		LeftGraphDraw = new Timer(10, new DrawLeftGraph());
		RightGraphDraw = new Timer(10, new DrawRightGraph());
		BBRGraphDraw = new Timer(7, new DrawBBRGraph());
		ShowPeak = false;
		ShowShades = true;
		Teaching = true;
		setLayout(new BorderLayout());	// create panels and menubar
		createMenuBar();
		createCenterPanel();
		createBottomPanel();
		createWholePanel();
		points = 0;
		ArrayIncrement = 3;
		//drawLeftGraph(6500);
		//new PlanckLaw().saveBBRFile(2700, "EdisonBulbPoints.txt");
		//new PlanckLaw().saveBBRFile(6000, "SunlightPoints.txt");
	}
	public void createMenuBar()	{	// Create Menu Bar
		menu = new JMenuBar();
		menu.setPreferredSize(new Dimension(1000, 20));	// Initializes menus
		file	= new JMenu("File");
		file.setPreferredSize(new Dimension(40, 50));
		edit	= new JMenu("Edit");
		view = new JMenu("View");
		tools	= new JMenu("Tools");
		help	= new JMenu("Help");
		speedupdrawing = new JMenu("Change Drawing Speed");
	
		menuItems = new MenuItems();				// Initializes Menu Items	
		New	= new JMenuItem("New");
		New.addActionListener(menuItems);
		open	= new JMenuItem("Open");
		open.addActionListener(menuItems);
		save	= new JMenuItem("Save");
		save.addActionListener(menuItems);
		saveas	= new JMenuItem("Save As");
		saveas.addActionListener(menuItems);
		instructions = new JMenuItem("Instructions");
		instructions.addActionListener(menuItems);
		bbrcalc = new JMenuItem("Black Body Radiation Calculator");
		bbrcalc.addActionListener(menuItems);
		bbrgraphclear = new JMenuItem("Clear Black Body Radiation Graph");
		bbrgraphclear.addActionListener(menuItems);
		showpeak = new JCheckBox("Show Peak Wavelength");
		showpeak.addActionListener(menuItems);
		showshades = new JCheckBox("Show Color Shades");
		showshades.setSelected(true);
		showshades.addActionListener(menuItems);
		speedup = new JSlider(1, 5, 1);
		speedup.addChangeListener(menuItems);
		speedup.setValue(3);

		file.add(New);					// Adds menu items to menus
		//file.add(open);
		//file.add(save);
		//file.add(saveas);
		
		help.add(instructions);
		
		menu.add(file);					// adds menu to menubar
		menu.add(edit);
		menu.add(view);
		menu.add(tools);
		//menu.add(help);
		
		tools.add(bbrcalc);
		
		speedupdrawing.add(speedup);
		
		view.add(showpeak);
		view.add(showshades);
		view.add(speedupdrawing);
		
		edit.add(bbrgraphclear);
		
		add(menu, BorderLayout.NORTH);		// adds menubar to the main panel
	}
	class MenuItems implements ActionListener, ChangeListener	{	// Gets input from the JMenu Bar
		public void actionPerformed(ActionEvent e)	{	// decides what to do depending on what JMenuItem user clicked
			Object file = e.getSource();
			if (file == instructions)	wholepanel.show(WholePanel, "Instructions");
			if (file == New)				{	wholepanel.show(WholePanel, "Game");	wholegamepanel.show(WholeGame, "Game");	createNewGame();	}
			if (file == bbrcalc)			wholepanel.show(WholePanel, "BBRCalc");
			if (file == bbrgraphclear)	{	if (BBRGraphDraw.isRunning())	BBRGraphDraw.stop();	BBRGraph.removeAllPoints();	BBRGraph.removeAllVerticalLines();		BBRGraph.setYEnd(0.01);	CreateGraph.setEnabled(true);	}
			if (file == showpeak)	toggleShowPeak();
			if (file == showshades) toggleShowShades();
		}
		public void stateChanged(ChangeEvent e)	{
			Object file = e.getSource();
			if (file == speedup) ArrayIncrement = speedup.getValue();
		}
	}
	public void toggleTimerSpeed()	{	// toggles the graphing speed (OLD!)
		if (LeftGraphDraw.getDelay() == 10)	{	// note: this is not used!
			LeftGraphDraw.setDelay(5);
			RightGraphDraw.setDelay(5);
			BBRGraphDraw.setDelay(4);
		}
		else {
			LeftGraphDraw.setDelay(10);
			RightGraphDraw.setDelay(10);
			BBRGraphDraw.setDelay(7);
		}
	}
	public void toggleShowPeak()	{	// toggles whether or not you show peaks
		ShowPeak = !ShowPeak;
		LeftGraph.enableVerticalLines(ShowPeak);
		RightGraph.enableVerticalLines(ShowPeak);
		BBRGraph.enableVerticalLines(ShowPeak);
	}
	public void toggleShowShades()	{	// toggles whether or not you show visible light shades
		ShowShades = !ShowShades;
		LeftGraph.enableShades(ShowShades);
		RightGraph.enableShades(ShowShades);
		BBRGraph.enableShades(ShowShades);
	}
	public void createNewGame()	{		// Creates a new game
		turn = 0;	
		points = 0;	
		createAnswerSequence();	
		createRound();
	}
	public void createRound()	{		// Creates a new round
		RightGraph.removeAllVerticalLines();	// Resets values for graphs
		RightGraph.removeAllPoints();
		LeftGraph.removeAllPoints();
		drawRightGraph(Double.parseDouble(LightSourceValues[AnswerSequence[turn]][1].toString()));
		TempR.setText("Temperature (K): " + (int)PlanckR.T.doubleValue());
		TempL.setText("Temperature (K): ");
		TurnOnLabel.setText("Turn On: " + (turn + 1) + " / " + AnswerSequence.length);
		LeftGraph.removeAllVerticalLines();
		try	{							// changes ball colors
			setBallColors();
		} catch (NullPointerException e)	{}
		Guess.setEnabled(true);			// enables clicking of the guess button
	}
	public void createAnswerSequence()	{	// creates an answer sequence 
		AnswerSequence = new int[(int)(LightSources.length * 1.5 + 0.5)];
		//AnswerSequence = new int[1];
		for (int i = 0; i < AnswerSequence.length; i++)
			AnswerSequence[i] = -1;
		int light, i;
		while (!done())	{				// creates action sequence
			do {
				light = (int)(Math.random() * LightSources.length);
				i = (int)(Math.random() * AnswerSequence.length);
			}	while (!approved(i, light));	// saves the random light source in the random spot if it is approved
			AnswerSequence[i] = light;
		}
	}
	public boolean approved(int i, int light)	{	// decides whether or not a light source is approved
		if (AnswerSequence[i] != -1 || Touching(i, light))	return false;	// not approved if another light source is there or if that spot is touching another of the same light source
		if (allLightsUsed() || notUsed(light))	return true;	// true if either all the light sources were already used, or if this light source was not used
		return false;	// returns false if not true
	}
	public boolean done()	{	// checks if all the positions are filled
		for (int i = 0; i < AnswerSequence.length; i++)
			if (AnswerSequence[i] == -1) return false;
		return true;
	}
	public boolean Touching(int i, int light)	{	// checks if given light source is touching another light source if placed in that position 
		if (i > 0)	{	if (AnswerSequence[i-1] == light) return true;	}
		if (i < AnswerSequence.length - 1) {	if (AnswerSequence[i+1] == light) return true;	}
		return false;
	}
	public boolean allLightsUsed()	{	// checks if all light sources were used at least once
		for (int i = 0; i < LightSources.length; i++)
			if (notUsed(i))	return false;
		return true;
	}
	public boolean notUsed(int i)	{	// returns true if given light source was never used
		for (int a = 0; a < AnswerSequence.length; a++)
			if (AnswerSequence[a] == i)	return false;
		return true;
	}
	public void createWholePanel()	{	// creates a new whole panel
		WholePanel = new JPanel();		// initializes panels and cardlayout for panel
		wholepanel = new CardLayout();
		WholePanel.setLayout(wholepanel);
		
		Instructions = new InstructionPanel();
		Start = new StartPanel();
		GameOver = new GameOverPanel();
		BBRCalc = new BlackBodyRadiationCalculatorPanel();
		
		WholePanel.add(Start, "Start");			// adds panels to the wholepanel
		WholePanel.add(Instructions, "Instructions");
		WholePanel.add(GameOver, "GameOver");
		WholePanel.add(BBRCalc, "BBRCalc");
		WholePanel.add(CenterPanel, "Game");
		
		add(WholePanel, BorderLayout.CENTER);
	}
	public void createCenterPanel()	{	// creates the game panel
		centerpanel = new BorderLayout();	// initializes panels
		CenterPanel = new JPanel();
		CenterPanel.setLayout(centerpanel);
		WholeGame = new JPanel();
		wholegamepanel = new CardLayout();
		WholeGame.setLayout(wholegamepanel);
		
		Game = new GamePanel();
		LoseGame = new LoseGamePanel();
		WinGame = new WinGamePanel();
		TeachPanel = new TeachPanel();
	
		WholeGame.add(Game, "Game");	// adds panels to the center panel
		WholeGame.add(LoseGame, "Lose");
		WholeGame.add(WinGame, "Win");
		WholeGame.add(TeachPanel, "Teach");
		CenterPanel.add(WholeGame, BorderLayout.CENTER);
		
		add(CenterPanel, BorderLayout.CENTER);
	}
	class TeachPanel extends JPanel implements MouseListener	{	// the panel that teaches how to play
		Graphics g;
		TeachPanel()	{			// adds the game label and the switch button to the panel
			setLayout(null);
			add(GameLabel2);
			addMouseListener(this);
			add(SwitchButton1);
		}
		public void paintComponent(Graphics a)	{	// paints rectangles and text onto panel
			super.paintComponent(a);
			g = a;
			drawRectangles();
			drawText();
			//drawArrows();
		}
		void drawArrows()	{	// not used (paints diagonal rectangle)
			int[] x = {254, 420, 429, 263	};
			int[] y = {443, 468, 450, 425	};
			g.fillPolygon(x, y, 4);
		}
		void drawText()	{	// draws text for the teach panel
			g.setFont(new Font("Arial", Font.PLAIN, 18));
			g.drawString("Click Here", 460, 445);
			g.drawString("when you want to guess", 405, 465);

			g.drawString("Use this ComboBox", 120, 130);
			g.drawString("...or click Here", 120, 180);
			g.drawString("To select what", 120, 230);
			g.drawString("light source you", 120, 250);
			g.drawString("think creates the", 120, 270);
			g.drawString("goal light spectrum", 120, 290);

			g.drawString("Displays information about the light source", 350, 210);
			g.drawString("<- selected over to the left", 350, 230);

			g.setFont(new Font("URW Bookman L Light", Font.BOLD, 25));
			g.drawString("Click the arrows to switch", 445, 130);
			g.drawString("between this page and the game!", 400, 155);

			//g.drawString("Goal Light Spectrum", getWidth() / 2 + 50, getHeight());
		}
		void drawRectangles()	{	// draws rectangles for teach panel
			g.drawRect(400, 420, 200, 60);
			g.drawRect(100, 100, 200, 50);
			g.drawRect(100, 150, 200, 200);
			g.drawRect(330, 100, 600, 250);
		}
		public void mouseDragged(MouseEvent evt)	{	}
		public void mouseMoved(MouseEvent evt)	{	}	
		public void mouseEntered(MouseEvent evt) {	} 
		public void mousePressed(MouseEvent evt) {	}
	    public void mouseExited(MouseEvent evt) {	} 
	    public void mouseReleased(MouseEvent evt) {  } 
	    public void mouseClicked(MouseEvent evt) { 
	    //	System.out.println(evt.getX() + " " + evt.getY());
	    }
	}
	class LoseGamePanel extends JPanel implements ActionListener	{	// the panel that is shown when you lose the game
		Graphics g;
		Image image;
		int width, height;

		LoseGamePanel()	{	// adds button, label, and starts a timer for the class
			setLayout(null);
			createLabel();
			createButton();
			Timer t = new Timer(200, this);
			t.start();
		}
		void createLabel()	{	// creates label saying you are wrong
			LoseGameLabel = new JLabel("Wrong!");
			LoseGameLabel.setFont(new Font("Arial", Font.BOLD, 60));
			LoseGameLabel.setBounds(350, 10, 600, 100);
			add(LoseGameLabel);
		}
		public void paintComponent(Graphics a)	{	// paints the correct answer
			super.paintComponent(a);
			g = a;
			width = getWidth();
			height = getHeight();
			drawCorrectAnswer();
		}
		void drawCorrectAnswer()	{	// paints the correct answer and outputs its name
			try {
				image = ImageIO.read(new File(LightSourceValues[AnswerSequence[turn]][6].toString()));
			}
			catch (IOException e)	{
				System.err.println("ERROR: Cannot read image " + LightSourceValues[AnswerSequence[turn]][6].toString());
				System.exit(1);
			}
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("Correct Answer: ", 100, 250);
			g.setFont(new Font("URW Bookman L Light", Font.ITALIC, 35));
			g.drawString(LightSourceValues[AnswerSequence[turn]][0].toString(), 100, 300);	// outputs name
			g.drawImage(	image, 500, 150, 300, 300, this	);					// draws correct answer
		}
		void createButton()	{	// button to go back to the game
			ContinueButton1 = new JButton("Continue?");
			ContinueButton1.setFont(new Font("URW Bookman L Light", Font.BOLD, 35));
			ContinueButton1.setBounds(100, 350, 300, 100);
			ContinueButton1.addActionListener(this);
			add(ContinueButton1);
		}
		public void actionPerformed(ActionEvent e)	{	// either changes color of button or goes back to game panel
			if (e.getSource() == ContinueButton1)	{	// goes back to game panel
				if (turn == AnswerSequence.length - 1) {	endGame();	return;	}
				turn++;
				createRound();
				wholepanel.show(WholePanel, "Game");	
				if (Teaching)	wholegamepanel.show(WholeGame, "Teach");
				else 			wholegamepanel.show(WholeGame, "Game");
			}
			else {
				ContinueButton1.setBackground(getOpaqueColor((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256), 0.5f));	// goes back to the game
			}
		}
	}
	class WinGamePanel extends JPanel implements ActionListener	{	// panel shown when game is won (Similar to above panel ^^)
		Graphics g;
		Image image;
		int width, height;

		WinGamePanel()	{	
			setLayout(null);
			createLabel();
			createButton();
			Timer t = new Timer(200, this);
			t.start();
		}
		void createLabel()	{
			WinGameLabel = new JLabel("Correct!");
			WinGameLabel.setFont(new Font("URW Bookman L Light", Font.BOLD, 60));
			WinGameLabel.setBounds(350, 10, 600, 100);
			add(WinGameLabel);
		}
		public void paintComponent(Graphics a)	{
			super.paintComponent(a);
			g = a;
			width = getWidth();
			height = getHeight();
			drawCorrectAnswer();
		}
		void drawCorrectAnswer()	{
			try {
				image = ImageIO.read(new File(LightSourceValues[AnswerSequence[turn]][6].toString()));
			}
			catch (IOException e)	{
				System.err.println("ERROR: Cannot read image " + LightSourceValues[AnswerSequence[turn]][6].toString());
				System.exit(1);
			}
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("Correct Answer! ", 100, 250);
			g.setFont(new Font("URW Bookman L Light", Font.ITALIC, 38));
			g.drawString(LightSourceValues[AnswerSequence[turn]][0].toString(), 100, 300);
			g.drawImage(	image, 500, 150, 300, 300, this	);
		}
		void createButton()	{
			ContinueButton2 = new JButton("Continue?");
			ContinueButton2.setFont(new Font("URW Bookman L Light", Font.BOLD, 35));
			ContinueButton2.setBounds(100, 350, 300, 100);
			ContinueButton2.addActionListener(this);
			add(ContinueButton2);
		}
		public void actionPerformed(ActionEvent e)	{
			if (e.getSource() == ContinueButton2)	{
				if (turn == AnswerSequence.length - 1) {	endGame();	return;	}
				turn++;
				createRound();
				wholepanel.show(WholePanel, "Game");	
				if (Teaching)	wholegamepanel.show(WholeGame, "Teach");
				else 			wholegamepanel.show(WholeGame, "Game");
			}
			else {
				ContinueButton2.setBackground(getOpaqueColor((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256), 0.5f));
			}
		}
	}
	public void createBottomPanel()	{	// initializes and adds bottom panel to the center panel
		BottomPanel = new GraphPanel();
		CenterPanel.add(BottomPanel, BorderLayout.SOUTH);
	}
	class StartPanel extends JPanel implements ActionListener, MouseListener	{	// creates the panel that is initially shown
		Graphics g;
		Image image;
		int width, height;
		Timer t;		
		int S1x;
		int[] S2y;
		int S3size;
		int S3rotation;
		int stageOn;
		boolean goingRight;
		Color[] S2Colors;
		String light;
		String by;
		Rectangle StartGame;
		Rectangle ClickHere;
		Color RectangleColor;
		StartPanel()	{	// initializes values
			addMouseListener(this);
			//createLabel();
			t = new Timer(5, this);
			S1x = -100;
			goingRight = true;
			light = "Light Game";
			by = "By Ofek Gila";
			S2y = new int[light.length()];
			S2Colors = new Color[S2y.length];
			RectangleColor = new Color(0, 0, 0);
			//StartGame = new JButton("Start?");
			//StartGame.setPreferredSize(new Dimension(300, 50));
			//StartGame.setLocation(0, 700);
			//StartGame.addActionListener(menuItems);
			//add(StartGame);
			StartGame = new Rectangle(-300, 700, 220, 50);
			ClickHere = new Rectangle(0, 640, 1000, 70);
			for (int i = 0; i < S2y.length; i++)	{
				S2y[i] = -50;
				S2Colors[i] = new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256));
			}
			S3size = S3rotation = 0;
			stageOn = 1;
			t.start();
		}
		void createLabel()	{	// creates label for panel
			StartLabel = new JLabel("Welcome");
			StartLabel.setFont(new Font("URW Bookman L Light", Font.BOLD, 60));
			add(StartLabel);
		}
		public void paintComponent(Graphics a)	{	// paints background and draws text for panel
			super.paintComponent(a);
			g = a;
			width = getWidth();
			height = getHeight();
			drawBackground();
			drawText();
		}
		void drawBackground()	{	// paints image in the background
			try {
				image = ImageIO.read(new File("LightSpectrum.png"));
			}
			catch (IOException e)	{
				System.err.println("ERROR: Cannot read image file");
				System.exit(1);
			}
			g.drawImage(	image, 0, 0, width, height, this	);
		}
		void drawText()	{	// draws text for panel
			if (stageOn == 1) g.setFont(new Font("Arial", Font.PLAIN, 50));	// draws text based off stage on
			if (stageOn > 1) g.setFont(new Font("Arial", Font.ITALIC, 50));
			g.drawString("Welcome to the", S1x, 50);
			//g.setColor(Color.red);
			for (int i = 0; i < S2y.length; i++) {
				if (S2y[i] < 300)	g.setFont(new Font("Nimbus Mono L", Font.PLAIN, 90));
				else 					g.setFont(new Font("Nimbus Mono L", Font.BOLD, 100));
				g.setColor(S2Colors[i]);
				//g.setColor(new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256)));
				g.drawString(light.charAt(i)+ "", 150 + i * 70, S2y[i]);
			}
			g.setColor(Color.black);
			label_line(500, 600, S3rotation * java.lang.Math.PI/180, by, new Font("Nimbus Mono L", Font.ITALIC, S3size));
			
			g.setColor(RectangleColor);
			//g.drawRect(ClickHere.x, ClickHere.y, ClickHere.width, ClickHere.height);
			g.drawString("Start?", StartGame.x, StartGame.y);
		}
		public void actionPerformed(ActionEvent e)	{	// changes text based off of stage on 
			if (stageOn == 1)	S1x+=10;
			if (stageOn == 2) S1x+=5;
			if (S1x >= 255) stageOn = 2;
			if (S1x >= 285) stageOn = 3;
			if (stageOn == 3) dropLightGame();
			if (stageOn == 4) nameSpin();
			if (stageOn == 5) moveStartGameButton();
			repaint();
		}
		void moveStartGameButton()	{	// moves the start game button
			if (goingRight)	{
				if (StartGame.getX() >= width - StartGame.getWidth())	{	// changes between going left and right
					RectangleColor = new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256));	
					goingRight = false;	
					return;	
				}
				StartGame.x+=10;
			}
			else {
				if (StartGame.getX() <= 0)	{
					RectangleColor = new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256));	
					goingRight = true;	
					return;	
				}
				StartGame.x-=10;
			}
		}
		void nameSpin()	{	// spins the name
			if (S3rotation < 360) {	
				S3rotation += 12;
				S3size += 2;
			}
			else stageOn = 5;
		}
		void dropLightGame()	{	// drops the letters light game from the sky
			for (int i = 0; i < S2y.length; i++)	{
				if (S2y[i] < 300) S2y[i] += 8;
				else if (i == S2y.length - 1) stageOn = 4;
				if (S2y[i] < 50) break;
			}
		}
		void label_line(int x, int y, double theta, String label, Font theFont)	{	// not made by me - draws text with a given delta
			Graphics2D g2D = (Graphics2D)g;
			AffineTransform fontAt = new AffineTransform();
			fontAt.rotate(theta);
			Font theDerivedFont = theFont.deriveFont(fontAt);
			g2D.setFont(theDerivedFont);
			g2D.drawString(label, x, y);
		}
		public void mouseDragged(MouseEvent evt)	{	}
		public void mouseMoved(MouseEvent evt)	{	}	
		public void mouseEntered(MouseEvent evt) {	} 
		public void mousePressed(MouseEvent evt) {	}
	    public void mouseExited(MouseEvent evt) {	} 
	    public void mouseReleased(MouseEvent evt) {  } 
	    public void mouseClicked(MouseEvent evt) { 	// switches panel to the teach panel of the game
	    	if (ClickHere.contains(evt.getX(), evt.getY()))	{	wholepanel.show(WholePanel, "Game");	wholegamepanel.show(WholeGame, "Teach");	createNewGame();	}
	    }
	}
	class InstructionPanel extends JPanel	{	// this panel is not used
		JTextArea TheInstructions;
		InstructionPanel()	{
			setLayout(new FlowLayout());
			createLabel();
			createInstructions();
		}
		void createLabel()	{
			InstructionLabel = new JLabel("Instructions");
			InstructionLabel.setFont(new Font("Arial", Font.BOLD, 50));
			add(InstructionLabel);
		}
		void createInstructions()	{
			TheInstructions = new InstructionCreation();
			add(TheInstructions);
		}
		class InstructionCreation extends JTextArea	{
			InstructionCreation()	{
				setPreferredSize(new Dimension(900, 700));
				setBackground(null);
				setFont(new Font("URW Bookman L Light", Font.PLAIN, 30));
				setLineWrap(true);
				setWrapStyleWord(true);
				setEnabled(true);
			}
		}
	}
	public void setBallColors()	{	// changes the colors of the balls in the game panel
			for (int i = 0; i < BallColors.length; i++)
				BallColors[i] = getOpaqueColor((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256), 0.5f);
	}
	class GamePanel extends JPanel implements ActionListener	{	// the game panel
		int[][] Balls;
		Graphics g;
		int width, height;
		boolean initial = true;
		GamePanel()	{		// creates labels, panels, and other JComponents for this panel
			lightSourceOn = 0;
			setLayout(null);
			createLabel();
			createTemperatureLabels();
			createLightSourcePanel();
			addDescriptionArea();
			createComboBox();
			createButton();
			createTurnOnLabel();
		}
		void createTurnOnLabel()	{	// label that says what turn you are on
			TurnOnLabel = new JLabel();
			TurnOnLabel.setFont(new Font("Arial", Font.BOLD, 30));
			TurnOnLabel.setBounds(20, 0, 1000, 60);
			add(TurnOnLabel);
		}
		public void paintComponent(Graphics a)	{	// creates the ball background and draws balls
			super.paintComponent(a);
			g = a;
			width = getWidth();
			height = getHeight();
			if (initial)	{
				initial = false;
				createBallBackground();
			}
			drawBalls();
		}
		void drawBalls()	{	// draws the balls in the game panel
			for (int i = 0; i < Balls.length; i++)	{
				g.setColor(BallColors[i]);
				g.fillOval(Balls[i][0], Balls[i][1], Balls[i][2], Balls[i][3]);
			}
		}
		void createBallBackground()	{	// creates the ball background
			Balls = new int[10][5];
			BallColors = new Color[Balls.length];
			setBallColors();
			createBalls();
			new Timer(100, new BallTimer()).start();
		}
		class BallTimer implements ActionListener{	// timer that moves balls
			public void actionPerformed(ActionEvent e)	{
				moveBalls();
				repaint();
			}
		}
		void moveBalls()	{	// moves the balls
			for (int i = 0; i < Balls.length; i++)	{
				if (OnEdge(i)) Balls[i][4]++;
				switch (Balls[i][4] % 4)	{
					case 0: Balls[i][0]+=10;	Balls[i][1]+=10;	break;
					case 1:	Balls[i][0]-=10;	Balls[i][1]+=10;	break;
					case 2: Balls[i][0]-=10;	Balls[i][1]-=10;	break;
					case 3: Balls[i][0]+=10;	Balls[i][1]-=10;	break;
				}
			}
		}
		boolean OnEdge(int ball)	{	// tells you if the ball is on the edge
			if (Balls[ball][0] + Balls[ball][2] >= width) return true;
			if (Balls[ball][1] + Balls[ball][3] >= height) return true;
			if (Balls[ball][0] < 0 || Balls[ball][1] < 0)	return true;
			return false;
		}
		void createBalls()	{	// creates the balls
			int x, y;
			for (int i = 0; i < Balls.length; i++)	{
				Balls[i][2] = (int)(Math.random() * 60 + 50);
				Balls[i][3] = (int)(Math.random() * 60 + 50);
				Balls[i][4] = (int)(Math.random() * 4);
			}
			for (int i = 0; i < Balls.length; i++)	{
				do {
					x = (int)(Math.random() * width);
					y = (int)(Math.random() * height);
				}	while (!okToPlaceBall(i, x, y));
				Balls[i][0] = x;
				Balls[i][1] = y;
			}
		}
		boolean okToPlaceBall(int ball, int x, int y)	{	// returns true if the balls x, y, width, and height, are all within bounds
			if (Balls[ball][2] + x > width) return false;
			if (Balls[ball][3] + y > height) return false;
			return true;
		}
		void addDescriptionArea()	{	// adds a description area (light source descriptions)
			Description = new JTextArea(LightSourceValues[lightSourceOn][3].toString());
			Description.setFont(new Font("Courier New", Font.PLAIN, 20));
			Description.setLineWrap(true);
			Description.setWrapStyleWord(true);
			Description.setBounds(330, 100, 600, 250);
			Description.setEditable(false);
			Description.setBackground(new Color(0, 0, 0, 0));	//sets the background transparent
			add(Description);
		}
		void createLabel()	{	// creates the label for the light game (and for the teach panel)
			GameLabel1 = new JLabel("Light Game");
			GameLabel1.setFont(new Font("Arial", Font.BOLD, 50));
			GameLabel1.setBounds(350, 0, 1000, 60);
			GameLabel2 = new JLabel("Light Game");
			GameLabel2.setFont(new Font("Arial", Font.BOLD, 50));
			GameLabel2.setBounds(350, 0, 1000, 60);
			add(GameLabel1);
		}
		void createTemperatureLabels()	{	// creates labels for the temperature
			TempL = new TemperatureLabel();
			TempR = new TemperatureLabel();
			TempL.setBounds(15, 430, 300, 40);
			TempR.setBounds(615, 430, 300, 40);
			TempL.setText("Temperature (K): ");
			TempR.setText("Temperature (K): ");
			add(TempL);
			add(TempR);
		}
		void createLightSourcePanel()	{	// creates the light source panel
			LightSource = new LightSourcePanel();
			add(LightSource);
		}
		void createComboBox()	{	// creates the light sources combo box
			LightSourcesBox = new LightBox();
			add(LightSourcesBox);
		}
		void createButton()	{	// creates the guess button
			Guess = new GuessButton();
			add(Guess);
			Switch = new ImageIcon(getClass().getResource("switch.png"));	// gives the button this image icon
			SwitchButton2 = new JButton(Switch);
			SwitchButton2.setBounds(830, 10, 120, 75);
			SwitchButton2.addActionListener(this);
			SwitchButton1 = new JButton(Switch);
			SwitchButton1.setBounds(830, 10, 120, 75);
			SwitchButton1.addActionListener(this);
			add(SwitchButton2);
		}
		public void actionPerformed(ActionEvent e)	{	// when the switch button is clicked, switches between teach and game
			Teaching = !Teaching;
			if (Teaching)	wholegamepanel.show(WholeGame, "Teach");
			else 			wholegamepanel.show(WholeGame, "Game");
		}
		class GuessButton extends JButton implements ActionListener {	// creates the guess button
			GuessButton()	{	// initializes values
				addActionListener(this);
				setPreferredSize(new Dimension(200, 60));
				setBounds(400, 420, 200, 60);
				setFont(new Font("Arial", Font.BOLD, 30));
				setText("Guess!");
			}
			public void actionPerformed(ActionEvent e)	{	// when clicked, shows temperature of light source and draws the graph of the light source
				SpectrumGuess = lightSourceOn;
				drawLeftGraph(Double.parseDouble(LightSourceValues[lightSourceOn][1].toString()));
				TempL.setText("Temperature (K): " + (int)PlanckL.T.doubleValue());
				setEnabled(false);	// makes the button not enabled (so you cannot click twice)
			}
		}
		class LightBox extends JComboBox implements ActionListener	{	// creates the light source combo box
			LightBox()	{	// initializes values
				addActionListener(this);
				setPreferredSize(new Dimension(200, 50));
				setBounds(100, 100, 200, 50);
				for (int i = 0; i < LightSources.length; i++)	// adds items with names of each light source value
					addItem(LightSourceValues[i][0]);
			}
			public void actionPerformed(ActionEvent e)	{	// selects and shows specified item, switches description text
				lightSourceOn = getSelectedIndex();
				lightsourcelayout.show(LightSource, (String)LightSourceValues[lightSourceOn][0]);
				Description.setText(LightSourceValues[lightSourceOn][3].toString());
			}
		}
		class LightSourcePanel extends JPanel implements MouseListener	{	// panel for the light sources
			LightSourcePanel()	{	// initializes values
				setPreferredSize(new Dimension(200, 200));
				setBounds(100, 150, 200, 200);
				addMouseListener(this);
				lightsourcelayout = new CardLayout();
				setLayout(lightsourcelayout);
				initializeLightSources();
			}
			void initializeLightSources()	{	// initializes light sources with given values
				LightSources = new JPanel[8];
				LightSourceValues = new Object[LightSources.length][7];
				LightSources[0] = new LightSource("Sunlight", 5778, "sun.png", 0, "SunlightPoints.txt", "Sunlight.txt");
				add(LightSources[0], LightSourceValues[0][0]);
				LightSources[1] = new LightSource("Incandescent Light Bulb", 2200, "IncandescentLightBulb.png", 1, "EdisonBulbPoints.txt", "EdisonBulb.txt");
				add(LightSources[1], LightSourceValues[1][0]);
				LightSources[2] = new LightSource("Lightning", 30000, "Lightning.png", 2, "LightningPoints.txt", "Lightning.txt");
				add(LightSources[2], LightSourceValues[2][0]);
				LightSources[3] = new LightSource("Fluorescent Light", 5600, "FluorescentBulb.png", 3, "Fluorescent Spectrum.txt", "Fluorescent Light.txt");
				add(LightSources[3], LightSourceValues[3][0]);
				LightSources[5] = new LightSource("Red LED Light", 298, "RedLED.png", 4, "Red LED Spectrum.txt", "RedLED.txt");
				add(LightSources[5], LightSourceValues[4][0]);
				LightSources[5] = new LightSource("Green LED Light", 298, "GreenLED.png", 5, "Green LED Spectrum.txt", "GreenLED.txt");
				add(LightSources[5], LightSourceValues[5][0]);
				LightSources[6] = new LightSource("Blue LED Light", 298, "BlueLED.png", 6, "Blue LED Spectrum.txt", "BlueLED.txt");
				add(LightSources[6], LightSourceValues[6][0]);
				LightSources[7] = new LightSource("Mercury-Vapor Lamp", 6800, "MercuryVaporLamp.png", 7, "MercuryVaporLamp Spectrum.txt", "MercuryVaporLamp.txt");
				add(LightSources[7], LightSourceValues[7][0]);
			}
			public void mouseDragged(MouseEvent evt)	{	}
			public void mouseMoved(MouseEvent evt)	{	}
			public void mouseEntered(MouseEvent evt) {	} 
			public void mousePressed(MouseEvent evt) {		// when you click on the panel (image), increments the light on by 1
				lightSourceOn = (lightSourceOn + 1) % LightSources.length;
				LightSourcesBox.setSelectedIndex(lightSourceOn);
			}
    		public void mouseExited(MouseEvent evt) {	} 
    		public void mouseReleased(MouseEvent evt) {  } 
    		public void mouseClicked(MouseEvent evt) { }
    		class LightSource extends JPanel	{	// panel created for each light source
    			String imagelocation;
    			int width, height;
    			Graphics g;
    			Image image;
    			public LightSource(String sourceName, double temperature, String il, int lightSourceNumber, String spectrumLocation, String descriptionLocation)	{
    				LightSourceValues[lightSourceNumber][0] = sourceName;				// sets values for all of the light sources
    				LightSourceValues[lightSourceNumber][1] = temperature;
    				LightSourceValues[lightSourceNumber][2] = getSpectrum(spectrumLocation);
    				LightSourceValues[lightSourceNumber][3] = getFile(descriptionLocation);
    				LightSourceValues[lightSourceNumber][4] = peak;
    				LightSourceValues[lightSourceNumber][5] = peakWavelength;
    				LightSourceValues[lightSourceNumber][6] = il;
    				//System.out.println(peak);
    				imagelocation = il;
    				setBackground(Color.black);
				}
				void drawImage()	{	// draws the image of the light source
					try {
						image = ImageIO.read(new File(imagelocation));
					}
					catch (IOException e)	{
						System.err.println("ERROR: Cannot read image file");
						System.exit(1);
					}
					g.drawImage(	image, 0, 0, width, height, this	);
				}
				public void paintComponent(Graphics a)	{	// calls the draw image method
					super.paintComponent(a);
					width = getWidth();
					height = getHeight();
					g = a;
					drawImage();
				}
			}
		}
	}
	/*public double getPeak(int lSN)	{
		if (lSN < 3) return PL.getPeak(Double.parseDouble(LightSourceValues[lSN][1].toString()));
		if (lSN == 3) return 0;
		else return peak;
	}*/
	public double[] getSpectrum(int lSN, boolean usesBBR)	{ //lSN = light Source Number - not used
		if (usesBBR)	return PlanckL.getGraph(LightSourceValues[lSN][1], 0.01, 1.5, 0.01);
		//	return (getSpectrum(LightSourceValues[lSN][0]
		return getSpectrum(LightSourceValues[lSN][0].toString());
	}
	public double[] getSpectrum(String filelocation)	{	// reads spectrum from file
		try {
			input = new Scanner(new File(filelocation));// tries to open the file
		}
		catch (FileNotFoundException e)	{
			System.err.println("ERROR: Cannot open file " + filelocation);
			System.exit(97);
		}
		peak = 0;
		double[] d = new double[149*2];	// finds peak and peak wavelength while saving all double values from file to array
		for (int i = 0; i < 149*2; i++)	{
			d[i] = input.nextDouble();
			//System.out.println(d[i]);
			if (i % 2 == 1) if (d[i] > peak) {	peak = d[i];	peakWavelength = d[i-1];	}
		}
		input.close();
		return d;
	}
	class GraphPanel extends JPanel	{	// panel that creates two game graphs
		GraphPanel()	{	// initializes values and creates layout
			setLayout(new GridLayout(0, 2));
			setPreferredSize(new Dimension(1000, 300));
			setBackground(Color.green);
			createGraphs();
		}
		void createGraphs()	{	// creates left and right graphs
			LeftGraph = new OGraph("Selected Source's Light Spectrum", "Wavelength (nm)", "Relative Brightness");
			RightGraph = new OGraph("Goal Light Spectrum", "Wavelength (nm)", "Relative Brightness");
			LeftGraph.drawLines = true;
			RightGraph.drawLines = true;
			LeftGraph.autoUpdate = true;
			RightGraph.autoUpdate = true;
			LeftGraph.tellDimensions(500, 300);
			RightGraph.tellDimensions(500, 300);
			LeftGraph.setXEnd(1.5);
			RightGraph.setXEnd(1.5);
			LeftGraph.setYEnd(1.5E14);
			RightGraph.setYEnd(1.5E14);
			LeftGraph.enableVerticalLines(false);
			RightGraph.enableVerticalLines(false);
			LeftGraph.drawVerticalTicks(0.05);
			RightGraph.drawVerticalTicks(0.05);
			LeftGraph.drawMajorVerticalTicks(0.2);
			RightGraph.drawMajorVerticalTicks(0.2);
			LeftGraph.drawMajorVerticalTickLabels("nm", 1000);
			RightGraph.drawMajorVerticalTickLabels("nm", 1000);
			addLightSpectrum();
		}
		void addLightSpectrum()	{	// shades visible light in graphs
			LeftGraph.shadeVertical(0.38, 0.45, getOpaqueColor(238, 130, 238, 0.2f));
			LeftGraph.shadeVertical(0.45, 0.495, getOpaqueColor(0, 0, 255, 0.2f));
			LeftGraph.shadeVertical(0.495, 0.57, getOpaqueColor(0, 128, 0, 0.2f));
			LeftGraph.shadeVertical(0.57, 0.59, getOpaqueColor(255, 255, 0, 0.2f));
			LeftGraph.shadeVertical(0.59, 0.62, getOpaqueColor(255, 165, 0, 0.2f));
			LeftGraph.shadeVertical(0.62, 0.7, getOpaqueColor(255, 0, 0, 0.2f));
			
			RightGraph.shadeVertical(0.38, 0.45, getOpaqueColor(238, 130, 238, 0.2f));
			RightGraph.shadeVertical(0.45, 0.495, getOpaqueColor(0, 0, 255, 0.2f));
			RightGraph.shadeVertical(0.495, 0.57, getOpaqueColor(0, 128, 0, 0.2f));
			RightGraph.shadeVertical(0.57, 0.59, getOpaqueColor(255, 255, 0, 0.2f));
			RightGraph.shadeVertical(0.59, 0.62, getOpaqueColor(255, 165, 0, 0.2f));
			RightGraph.shadeVertical(0.62, 0.7, getOpaqueColor(255, 0, 0, 0.2f));
			
			add(LeftGraph);
			add(RightGraph);
		}
	}
	public Color getOpaqueColor(int r, int g, int b, float opacity)	{	// method I made for converting RGB + opacity level to a color
		float red = r / 255f;
		float green = g / 255f;
		float blue = b / 255f;
		return new Color(red, green, blue, opacity);
	}
	class GameOverPanel extends JPanel implements ActionListener	{	// panel shown when game ends
		Graphics g;
		String gradenote; // note given for each grade
		boolean initial = true;
		GameOverPanel()	{	// create a label
			createLabel();
			Timer t = new Timer(400, this);
			t.start();
		}
		void createLabel()	{	// creates label
			GameOverLabel = new JLabel("Game Over!");
			GameOverLabel.setFont(new Font("Utopia", Font.BOLD, 80));
			add(GameOverLabel);
		}
		public void paintComponent(Graphics a)	{	// shows your points
			super.paintComponent(a);
			if (initial)	{	// runs in only the initial time
				initial = false;
				gradenote = getGradeNote();
			}
			g = a;
			showPoints();
		}
		void showPoints()	{ // shows your score, grade letter, and grade note
			g.setFont(new Font("Utopia", Font.ITALIC, 60));
			g.setColor(Color.red);
			g.drawString("Score: " + points + " / " + AnswerSequence.length, 80, 250);
			g.drawString("Grade: " + getGrade(), 80, 300);
			g.setFont(new Font("Nimbus Mono L", Font.BOLD, 60));
			g.setColor(new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256)));
			String gradenote1, gradenote2;
			if (gradenote.length() >= 15)	{
				int i;
				for (i = 15; gradenote.charAt(i) != ' '; i++);
				gradenote1 = gradenote.substring(0, i);
				gradenote2 = gradenote.substring(i);
				g.drawString(gradenote1, 500 - gradenote1.length() * 20, 490);
				g.drawString(gradenote2, 500 - gradenote2.length() * 20, 610);
			}
			else g.drawString(gradenote, 500 - gradenote.length() * 20, 500);
		}
		String getGrade()	{	// gets your grade letter
			if (Grade < 60)	return "F";
			if (Grade < 63)	return "D-";
			if (Grade < 67)	return "D";
			if (Grade < 70)	return "D+";
			if (Grade < 73)	return "C-";
			if (Grade < 77)	return "C";
			if (Grade < 80)	return "C+";
			if (Grade < 83)	return "B-";
			if (Grade < 87)	return "B";
			if (Grade < 90)	return "B+";
			if (Grade < 93)	return "A-";
			if (Grade < 97)	return "A";
			if (Grade <= 100)	return "A+";
			return "Boso Alert!";
		}
		String getGradeNote()	{	// gets your grade note
			if (Grade < 60)		return getF();
			if (Grade < 63)		return getD();
			if (Grade < 67)		return getD();
			if (Grade < 70)		return getD();
			if (Grade < 73)		return getC();
			if (Grade < 77)		return getC();
			if (Grade < 80)		return getC();
			if (Grade < 83)		return getB();
			if (Grade < 87)		return getB();
			if (Grade < 90)		return getB();
			if (Grade < 93)		return getA();
			if (Grade < 97)		return getA();
			if (Grade <= 100)	return getAPlus();
			return "Boso Alert!";
		}
		String getF()	{			// grade notes for grade letter "F"
			switch ((int)(Math.random() * 7))	{
				case 0:	return	"Why?";
				case 1:	return	"Can't Blame the Misclicks";
				case 2:	return	"Hope is lost like a shooting star";
				case 3:	return	"Failure coming at the speed of light";
				case 4:	return	"Do you even know what light is?";
				case 5:	return	"Electro-Magnetic Failure";
				case 6:	return	"Just... no";
			}
			return "";
		}
		String getD()	{			// grade notes for grade letter "D"
			switch ((int)(Math.random() * 6))	{
				case 0:	return	"Way Below Average";
				case 1:	return	"Much More than Misclicks";
				case 2:	return	"Learn more about light";
				case 3:	return	"You could've done better";
				case 4:	return	"Really?";
				case 5:	return	"This is not for you";
			}
			return "";
		}
		String getC()	{			// grade notes for grade letter "C"
			switch ((int)(Math.random() * 4))	{
				case 0:	return	"Below Average";
				case 1:	return	"More than Misclicks";
				case 2:	return	"Learn more about light";
				case 3:	return	"You could've done better";
			}
			return "";
		}
		String getB()	{			// grade notes for grade letter "B"
			switch ((int)(Math.random() * 4))	{
				case 0:	return	"Average";
				case 1:	return	"Darn Misclicks!";
				case 2:	return	"OK";
				case 3:	return	"You could've done better";
			}
			return "";
		}
		String getA()	{			// grade notes for grade letter "A"
			switch ((int)(Math.random() * 5))	{
				case 0:	return	"So close...";
				case 1:	return	"Darn Misclicks!";
				case 2:	return	"Pretty darn good!";
				case 3:	return	"Great job!";
				case 4:	return	"Not too shabby";
			}
			return "";
		}
		String getAPlus()	{		// grade notes for grade letter "A+"
			switch((int)(Math.random() * 3))	{
				case 0:	return	"Perfect Score!";
				case 1:	return	"Spectrum Master!";
				case 2:	return	"King of Light!";
			}
			return "";
		}
		public void actionPerformed(ActionEvent e)	{	// repaints (giving note new color)
			repaint();
		}
	}
	public void checkCorrect()	{ // after each turn, checks if you were correct
		if (AnswerSequence[turn] == SpectrumGuess)	{
			points++;
			wholegamepanel.show(WholeGame, "Win");	// go to win panel if correct
		} 
		else {
			wholegamepanel.show(WholeGame, "Lose"); // go to lose panel if wrong
		}
		/*if (turn == AnswerSequence.length - 1) endGame();
		else {
			turn++;
			createRound();
		}
		LeftGraph.removeAllPoints();*/
	}
	public void endGame()	{	// ends the game
		Grade = points * 100.0 / AnswerSequence.length;	// gives grade
		wholepanel.show(WholePanel, "GameOver");		// switches to gameover panel
	}
	public void drawLeftGraph(double temperature)	{	// draws the left graph
		if (LeftGraphDraw.isRunning())	{	// this if statement will never run
			LeftGraphDraw.stop();
			finishLeftGraph(temperature);
			return;
		}
		PlanckL.setTemperature(temperature);	// sets temperature
		lightSourceL = lightSourceOn;
		//LeftGraph.setYEnd(PlanckL.getBrightnessAtPeak().doubleValue() * 1.18);
		LeftGraph.setYEnd(Double.parseDouble(LightSourceValues[lightSourceL][4].toString()) * 1.18);	// scales graph
		//System.out.println(LeftGraph.YEnd);
		LeftGraphPoint = 1;
		LeftGraphDraw.start();	// starts drawing
	}
	public void drawBBRGraph(double temperature)	{	// draws the blackbody radiation graph
		PlanckBBR.setTemperature(temperature);
		if (PlanckBBR.getBrightnessAtPeak().doubleValue() * 1.18 > BBRGraph.YEnd)	// scales graph up, never down
			BBRGraph.setYEnd(PlanckBBR.getBrightnessAtPeak().doubleValue() * 1.18);
		//System.out.println(RightGraph.YEnd);
		BBRGraphPoint = 0.01;
		BBRGraphDraw.start();	// starts drawing
	}
	public void finishLeftGraph(double temperature)	{	// finishes drawing left graph (not used)
		for (double d = LeftGraphPoint; d <= 1.5; d+=0.01)
			LeftGraph.addPoint(d, PlanckL.getPowerRadiated(d).doubleValue());
		LeftGraph.newDataSet();
		PlanckL.setTemperature(temperature);
		LeftGraphPoint = 1;
		LeftGraphDraw.start();
	}
	public void drawRightGraph(double temperature)	{	// draws the right graph
		RightGraphDraw.stop();
		//AnswerSequence[turn] = 3;
		PlanckR.setTemperature(temperature);		// (See drawLeftGraph)
		lightSourceR = AnswerSequence[turn];
		//RightGraph.setYEnd(PlanckR.getBrightnessAtPeak().doubleValue() * 1.18);
		RightGraph.setYEnd(Double.parseDouble(LightSourceValues[lightSourceR][4].toString()) * 1.18);
		//System.out.println(RightGraph.YEnd);
		RightGraphPoint = 1;
		RightGraphDraw.start();
	}
	class DrawLeftGraph implements ActionListener	{	// timer class for drawing left graph
		public void actionPerformed(ActionEvent e)	{
			//LeftGraph.addPoint(LeftGraphPoint, PlanckL.getPowerRadiated(LeftGraphPoint).doubleValue());
			double d = Array.getDouble(LightSourceValues [lightSourceL] [2], (int)(LeftGraphPoint*2-1));	// finds point from light source points array
			//System.out.println(d);
			LeftGraph.addPoint(Array.getDouble(LightSourceValues [lightSourceL] [2], (int)(LeftGraphPoint*2-2)), d);	// adds the point to the left graph
			if (lightSourceL >= 3) LeftGraphPoint+=1;
			else LeftGraphPoint+=ArrayIncrement;
			if (LeftGraphPoint > 149)	{	LeftGraphDraw.stop();	LeftGraph.newDataSet();	checkCorrect();	}	// stops timer and checks if answer is correct when done drawing
			repaint();
		}
	}
	class DrawRightGraph implements ActionListener	{	// timer class for drawing right graph
		public void actionPerformed(ActionEvent e)	{	// see (DrawLeftGraph)
			//RightGraph.autoUpdate = false;
			//RightGraph.addPoint(RightGraphPoint, PlanckR.getPowerRadiated(RightGraphPoint).doubleValue());
			//System.out.println(Array.getDouble(LightSourceValues [lightSourceR] [2], (int)(RightGraphPoint*2-2)) + " " + Array.getDouble(LightSourceValues [lightSourceR] [2], (int)(RightGraphPoint*2-1)));
			double d = Array.getDouble(LightSourceValues [lightSourceR] [2], (int)(RightGraphPoint*2-1));
			RightGraph.addPoint(Array.getDouble(LightSourceValues [lightSourceR] [2], (int)(RightGraphPoint*2-2)), d);
			if (lightSourceR >= 3) RightGraphPoint+=1;
			else RightGraphPoint+=ArrayIncrement;
			if (RightGraphPoint > 149)	{	RightGraphDraw.stop();	if (lightSourceR != 3 && lightSourceR != 7)	RightGraph.addVerticalLine(Double.parseDouble(LightSourceValues[lightSourceR][5].toString()));	RightGraph.renderGraph();	}	// when done drawing, also shows peak wavelength
			repaint();
			//System.out.println(RightGraph.maxY);
		}
	}
	class DrawBBRGraph implements ActionListener	{	// timer class for drawing blackbody radiation graph
		public void actionPerformed(ActionEvent e)	{
			BBRGraph.addPoint(BBRGraphPoint/100f, PlanckBBR.getPowerRadiated(BBRGraphPoint/100f).doubleValue());	// gets blackbody radiation from a calculator I made
			BBRGraphPoint+=ArrayIncrement;
			if (BBRGraphPoint > 251)	{	
				BBRGraphDraw.stop();	
				BBRGraph.addVerticalLine(PlanckBBR.getPeak().doubleValue() * 1E6);	// draws vertical line at peak
				CreateGraph.setEnabled(true);
				BBRGraph.newDataSet();
			}
			repaint();
		}
	}
	class BlackBodyRadiationCalculatorPanel extends JPanel	{	// panel for the blackbody calculator
		BlackBodyRadiationCalculatorPanel()	{	// creates graph and sets layout
			setLayout(new BorderLayout());
			createGraph();
			add(new TopBBRPanel(), BorderLayout.CENTER);
		}
		void createGraph()	{	// creates graph
			BBRGraph = new OGraph("Black Body Radiation", "Wavelength (nm)", "Relative Brightness");
			BBRGraph.setPreferredSize(new Dimension(1000, 400));	// sets values
			BBRGraph.drawLines = true;
			BBRGraph.autoUpdate = true;
			BBRGraph.tellDimensions(1000, 400);
			BBRGraph.setXEnd(2.5);
			//BBRGraph.setYEnd(1);
			add(BBRGraph, BorderLayout.SOUTH);
			BBRGraph.drawVerticalTicks(0.05);
			BBRGraph.drawMajorVerticalTicks(0.2);
			BBRGraph.drawMajorVerticalTickLabels("nm", 1000);
			addLightSpectrum();
			BBRGraph.enableVerticalLines(false);
		}
		void addLightSpectrum()	{	// shades visible light
			BBRGraph.shadeVertical(0.38, 0.45, getOpaqueColor(238, 130, 238, 0.2f));
			BBRGraph.shadeVertical(0.45, 0.495, getOpaqueColor(0, 0, 255, 0.2f));
			BBRGraph.shadeVertical(0.495, 0.57, getOpaqueColor(0, 128, 0, 0.2f));
			BBRGraph.shadeVertical(0.57, 0.59, getOpaqueColor(255, 255, 0, 0.2f));
			BBRGraph.shadeVertical(0.59, 0.62, getOpaqueColor(255, 165, 0, 0.2f));
			BBRGraph.shadeVertical(0.62, 0.7, getOpaqueColor(255, 0, 0, 0.2f));
		}
		class TopBBRPanel extends JPanel	{	// creates the top panel for this  class
			TopBBRPanel()	{	// adds components to graph
				setLayout(null);
				createLabel();
				createTemperatureLabels();
				createSlider();
				createCreateGraph();
			}
			void createCreateGraph()	{	// creates graph
				CreateGraph = new CreateGraphButton();
				add(CreateGraph);
			}
			void createLabel()	{	// creates label
				BBRCalcLabel = new JLabel("Blackbody Radiation Calculator");
				BBRCalcLabel.setFont(new Font("Utopia", Font.BOLD, 65));
				BBRCalcLabel.setBounds(15, 20, 990, 60);
				add(BBRCalcLabel);
			}
			void createSlider()	{	// creates the slider
				BBRSlider = new BlackBodyRadiationSlider();
				add(BBRSlider);
			}
			void createTemperatureLabels()	{	// creates the temperature label
				TempLBBR = new TemperatureLabel();
				TempLBBR.setBounds(15, 330, 300, 40);
				TempLBBR.setText("Temperature (K): ");
				add(TempLBBR);
			}
		}
		class CreateGraphButton extends JButton implements ActionListener {	// button for creating a graph
			CreateGraphButton()	{	// initializes values
				addActionListener(this);
				setBounds(350, 320, 300, 60);
				setFont(new Font("Arial", Font.BOLD, 30));
				setText("Draw Graph");
			}
			public void actionPerformed(ActionEvent e)	{	// sets temperature and draws the graph
				drawBBRGraph(BBRSlider.getValue());
				TempLBBR.setText("Temperature (K): " + (int)PlanckBBR.T.doubleValue());
				setEnabled(false);
			}
		}

		class BlackBodyRadiationSlider extends JSlider implements ChangeListener	{	// slider for setting temperature
			BlackBodyRadiationSlider()	{	// initializes values
				addChangeListener(this);
				setBounds(100, 100, 800, 200);
				setMinimum(2000);
				setMaximum(10000);
				setMajorTickSpacing(500);
				setMinorTickSpacing(100);
				setPaintLabels(true);
				setPaintTicks(true);
			}
			public void stateChanged(ChangeEvent e)	{	// changes text when moved
				TempLBBR.setText("Temperature (K): " + getValue());
			}
		}
	}
	class TemperatureLabel extends JLabel	{	// class for temperature labels
		TemperatureLabel()	{	// constructor
			setFont(new Font("URW Bookman L Light", Font.BOLD, 20));
		}
	}
	public String getFile(String filelocation)	{	// not used method
		String file = "";
		try {
			input = new Scanner(new File(filelocation));// tries to open the file
		}
		catch (FileNotFoundException e)	{
			System.err.println("ERROR: Cannot open file quotes.txt");
			System.exit(97);
		}
		while (input.hasNext())
			file += input.nextLine() + "\n";
		input.close();
		return file;
	}
}