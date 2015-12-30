/// Anindit 
/// DogFight.java
/// 4/21-5/25
///This program is my final project for java and it allows th euser to control a tank to shoot down enemy planes. ALSO - Mr. Greenstein, this might make it easier to grade:
/*
Components used: JFrame, JPanel, JTextArea, JLabel, JTextField, JButton
File input: to read questions from file questions.txt
File output: to be able to input your own questions
Estimated work time: ~17 hours
*/ 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
public class DogFight{ //main class, all classes are nested
	JFrame frame; //the frame which holds everything
	OptionPanel op;//the panel with the options
	GamePanel gp;//the panel which you play the game on
	InstructionPanel ip; //the panel with instructions
	EnterQuestionPanel ep = new EnterQuestionPanel(); //the panel where you enter the questions
	BufferedImage img, tank, background2, rtank, boom, boom2, instructions; //pictures used in my game
	ArrayList<Bomber> bombers = new ArrayList<Bomber>(); //the arraylist which holds all the bombers
	int health = 1; //the health of the tank at any moment
	int finalhealth = 1; //the max health of the tank
	int mx = 0;//how much to move the tank along the x and y
	int my = 0; 
	boolean tstartredtimer = false; //the boolean whic turens the tank red when hit
	Timer time; //the timer which controls everything
	int numplanes  = 1; //the number of enemy bombers
	int time1 = 0;//the amount of time it takes to get the cooldown on bullts
	int Time1 = 0;//amount of time to get cooldown on movement
	Question question1 = new Question("What is the mathematical sum of 1+1", "2"); //test questions (example)
	Question question2 = new Question("What is the mathematical difference if 1-1", "0");
	ArrayList <Question> questions = new ArrayList<Question>();	//arraylist of all the questions
	BufferedImage shotman, lshotman, boomshotman, lboomshotman;
	Tank t;
	public static void main(String[] args){
		DogFight df = new DogFight();
		df.run();
	}
	public void run(){ //sets up the overal Jframe
		frame = new JFrame("Dogfight");
		frame.setSize(368,385);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		op = new OptionPanel();
		gp = new GamePanel(numplanes);
		ip = new InstructionPanel();
		frame.getContentPane().add(op);
		frame.addKeyListener(gp);
		frame.setVisible(true);
		initializeQuestionArray();
		
	}
	
	public void initializeQuestionArray(){ //initializes all the questions in the question array
		File file = new File ("Questions.txt");
		questions.clear();
		try {
            //
            // Create a new Scanner object which will read the data 
            // from the file passed in. To check if there are more 
            // line to read from it we check by calling the 
            // scanner.hasNextLine() method. We then read line one 
            // by one till all line is read.
            //
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                Question q = new Question(scanner.nextLine(), scanner.nextLine());
				questions.add(q);
				
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	
	class OptionPanel extends JPanel{ //this panel holds al the options of what you cn do in my game
		JButton play = new JButton("Play Game");
		JButton settings = new JButton("Settings");
		JButton directions = new JButton("Directions");
		JButton enterquestion = new JButton("Enter Question");
		JButton highscores = new JButton("High Scores");
 		public OptionPanel(){ //the constructor initializes all the pics as well as the buttons on screan
			setLayout(null);
			
			getPictures();
			initializeButtons();
			
			
		}	
		
		public void getPictures(){ //gets all the pictures needed
			try{
				img = ImageIO.read(new File("background.jpg"));
				tank = ImageIO.read(new File("Tank.png"));
				background2 = ImageIO.read(new File("background2.png"));
				
				rtank = ImageIO.read(new File("RTank.png"));
				boom = ImageIO.read(new File("Boom.png"));
				boom2 = ImageIO.read(new File("Boom2.png"));
				instructions = ImageIO.read(new File("Instructions.png"));
				shotman = ImageIO.read(new File("ShotMan.png"));
				lshotman = ImageIO.read(newFile("LShotMan.png"));
				boomshotman = ImageIO.read(new File("AfterShotMan"));
				lboomshotman = ImageIO.read(new File("LAfterShotMan"));
				
			}catch(IOException e){
				System.err.println("d");
			}
		}
		
		public void initializeButtons(){//initializes all the buttons on the instruction panel, adds their action listeners, and sets their positions
			play.setBounds(40,90,280,30);
			add(play);
			play.addActionListener(new PlayGame());
			play.setVisible(true);
			
			enterquestion.setBounds(40,140,280,30);
			add(enterquestion);
			enterquestion.addActionListener(new EnterQuestion());
			enterquestion.setVisible(true);
			
			directions.setBounds(40,190,280,30);
			add(directions);
			directions.addActionListener(new Instructor());
			directions.setVisible(true);
			
			
		}
 		public void paintComponent(Graphics g){ //paints the image on the background as well as the title
			g.drawImage(img, 0,0,getWidth(), getHeight(), this);
			g.setFont(new Font("Times", Font.BOLD, 55));
			g.drawString("DOGFIGHT", 30, 60);
		}
		
		class PlayGame implements ActionListener{ //the action listener which goes with the button to play the game
			public void actionPerformed(ActionEvent e){
				frame.setSize(1105,705);
				frame.remove(op);
				gp = new GamePanel(numplanes);
				frame.add(gp);
				frame.setBackground(Color.black);
				gp.requestFocus();
				
				frame.setLocationRelativeTo(null);
				time.start();
			}
		}
		class Instructor implements ActionListener{//the actionListener which goes with the button to see the instructions
			public void actionPerformed(ActionEvent e){
				
				frame.remove(op);
				frame.getContentPane().add(ip, BorderLayout.CENTER);
				frame.setSize(800,630);
				frame.setLocationRelativeTo(null);
				ip.requestFocus();
				
			}
		}
		class EnterQuestion implements ActionListener{//the actuionlistener which goes with the button to see the enter question panel
			public void actionPerformed(ActionEvent e){
				frame.remove(op);
				frame.getContentPane().add(ep, BorderLayout.CENTER);
				frame.setSize(500,350);
				frame.setLocationRelativeTo(null);
				ep.requestFocus();
			}
		}
	}
	class InstructionPanel extends JPanel{//the panel which actually controls the displaying of the instructions
		public InstructionPanel(){ //initializes the instructionpanel variables
			setLayout(null);
			JButton b = new JButton ("back");
			b.setBounds(350,550,80, 30);
			add(b);
			b.addActionListener(new Quitter());
			
		}
		
		public void paintComponent(Graphics g){ //paints the graphics onto the screen; i made the instructions onto the power point  and just display the image of it
			super.paintComponent(g);
			g.drawImage(instructions, 0, 0, getWidth(), 540, null);
		}
		class Quitter implements ActionListener{ //allows you to go back to optionpanel and see the other options
			public void actionPerformed(ActionEvent e){
				frame.remove(ip);
				frame.setSize(368, 385);
				frame.setLocationRelativeTo(null);
				frame.getContentPane().add(op, BorderLayout.CENTER);
				op.requestFocus();
			}
		}
	}
	class EnterQuestionPanel extends JPanel{ //This allows you to enter a question into the database of questions
		BufferedWriter pw;
		JTextField question = new JTextField("Enter Question here"); //the textfield which gets the question u wish to be answered
		JTextField answer = new JTextField("Enter the answer to your Question here"); // 
		JButton enter, back; //jbuttons which allow you to navigate through the panels
			public EnterQuestionPanel (){ //the constructor which initializes the graphics go the panel
				question = new JTextField("Enter your question here");
				
				
				enter = new JButton("Enter"); //the button which is pressed to enter the information into te file
				back = new JButton("Back"); //the button which is pressed to go back to he
				answer = new JTextField("Enter the answer to your question here");
				setLayout(null);
				setSize(500,350);
				question.setBounds(0,0,500,130);
				answer.setBounds(100,175,250,40);
				answer.setFont(new Font("Serif", Font.PLAIN, 15));
				enter.setBounds(100, 220, 100,30);
				back.setBounds(250,220,100,30);
				add(question);//adds everything to the pane;
				add(answer);
				add(enter);
				add(back);
				enter.addActionListener(new Enter());
				back.addActionListener(new Back());
				
				
		}
		class Enter implements ActionListener{ //this enters the data into the final
			public void actionPerformed(ActionEvent e){
				try{ //trys to print it into the the file, which NOT work, and has error handling to keep up with this
					pw = new BufferedWriter(new FileWriter("Questions.txt" ,true));
					pw.newLine();//prints it in a specific format 
					pw.write( question.getText());
					pw.newLine();
					pw.write( answer.getText());
					pw.close();
				}
				catch(IOException est){
					System.err.println("sucks");
				}
				
				JFrame goodframe = new JFrame ("Frame"); //creates a frame to show if you entered your question correctly
				goodframe.setSize(300,200);
				goodframe.getContentPane().add(new JLabel("Succesfully entered question"), BorderLayout.CENTER);
				goodframe.setLocationRelativeTo(null);
				goodframe.setVisible(true);
				answer.setText("Enter the answer to your question here");
				question.setText("Enter your question here");
				initializeQuestionArray();
					
				
			}
			
		}
		class Back implements ActionListener{ //takes you back to the home screen
			public void actionPerformed(ActionEvent e){
				frame.remove(ep);
				frame.setSize(368, 385);
				frame.setLocationRelativeTo(null);
				frame.getContentPane().add(op, BorderLayout.CENTER);
				op.requestFocus();
			}
		}
	}
	class GamePanel extends JPanel implements  KeyListener, ActionListener, MouseMotionListener, MouseListener{ //the panel upon which you play the game
		 //the tank object which controls the character which the player controls
		Bomber bomber; //the head bomber
		JFrame moveframe; //the frame whcih handles the questions to move on or stay, etc
		boolean pressed = false; //the boolean to regulate clicking
		boolean canbemoved = true; //the boolean which controls the key pressed
		
		public GamePanel(int numplanes){ //the constructor has the argument of (numplanes) which is the number of planes there will be in the game
			bombers.clear(); //it clears the arraylist everytime so each initialization of the game will have fresh bombers
			pressed = false;
			canbemoved = true;
			if (numplanes == 1){ //adds planes depending on numplanes
				bombers.add(new Bomber());
			}
			else if (numplanes == 2){
				bombers.add(new Bomber());
				bombers.add(new Bomber(450,80));
			}
			else if (numplanes ==3){
				bombers.add(new Bomber(220,15));
				bombers.add(new Bomber(650,15));
				bombers.add(new Bomber(450,80));
			}
			else if (numplanes == 4){
				bombers.add(new Bomber(220,15));
				bombers.add(new Bomber(650,15));
				bombers.add(new Bomber(220,80));
				bombers.add(new Bomber(650,80));
			}
			addKeyListener(this);
			t = new Tank();
			bomber = new Bomber();
			time = new Timer(20,this);
			addMouseMotionListener(this);
			addMouseListener(this);
		}
		
		class TwoTime implements ActionListener{// this takes you back to the main menu, dont ask why its called 2 time xD
			public void actionPerformed(ActionEvent e){
				frame.setSize(368, 385);
				frame.remove(gp);
				frame.add(op);
				op.requestFocus();
				moveframe.dispose();
				frame.setLocationRelativeTo(null);
				
				
			}
		}
		class QuestionPanelLoss extends JPanel{ //this is the panel which brings up the question for you to answer
			JTextArea questionarea; //the text area where the question is printed
			JButton enter; //the button which enters your answer
			String attempt; //the attemps that the user must do
			String attempt2;
			JTextField getattempt; //gets the attempt that the user tries
			Question q1, q2; //the 2 questions that are given to the user
			int type;
			int forq1 = 0;
			int forq2 = 0;
			EnterText et = new EnterText();
			FinalText ft = new FinalText();
			public QuestionPanelLoss(Question qe, Question qe1, int type ){//this initializes the location of all the components on the frame
				questionarea = new JTextArea(qe.question);
				q1 = qe;
				q2 = qe1;
				this.type = type;
				enter = new JButton("Enter");
				getattempt = new JTextField("Enter your answer here");
				setLayout(null);
				setSize(500,350);
				questionarea.setBounds(0,0,500,150); //the location of the question area
				questionarea.setFont(new Font("Serif", Font.PLAIN, 25));
				questionarea.setLineWrap(true);
				questionarea.setWrapStyleWord(true);
				getattempt.setBounds(125,150,250,60);
				getattempt.setFont(new Font("Serif", Font.PLAIN, 25));
				enter.setBounds(200, 220, 100,30);
				add(questionarea);//ads everything
				add(getattempt);
				add(enter);
				enter.addActionListener(et);
			}
			class EnterText implements ActionListener{ //This allows you to enter your answer into the panel
				public void actionPerformed(ActionEvent e){
					if (q1.isAnswerCorrect(getattempt.getText())){ //if its correct
						forq1 = 1;
					}
					else forq1 = 0; //if it is incorrect
					attempt = getattempt.getText();
					
					questionarea.setText(q2.question);//changes everything for the 2nd question after entering the first question
					getattempt.setText("Enter your answer here");
					//enter.remove(et);
					enter.addActionListener(ft);
					
				}
			}
			class FinalText implements ActionListener{ //the final question that will be shown
				public void actionPerformed(ActionEvent e){
					if (q2.isAnswerCorrect(getattempt.getText())){ //if you get the 2nd question correct
						forq2 = 1;
					}
					
					int score = forq1 + forq2; //score is the sum of the individual questions
					attempt2 = getattempt.getText(); //removes all the own components to make way for the new ones
					questionarea.setVisible(false);
					enter.setVisible(false);
					getattempt.setVisible(false);
					JLabel result = new JLabel("RESULT: " + score + "/2"); //the result of your quizz
					result.setFont(new Font("Serif", Font.BOLD, 50));
					result.setBounds(110,15, 400,50);
					JTextArea question1 = new JTextArea(q1.question);
					JTextArea question2 = new JTextArea(q2.question);
				
					//displays the correct answer your answer, and the question
					JLabel header = new JLabel("Question                                                   Your  Answer       Correct Ans.");
					header.setFont(new Font("Serif", Font.BOLD, 15));
					header.setBounds(10, 65,500,50);
					
					question1.setBounds(10,110, 280,50);
					question1.setLineWrap(true);
					JTextArea youranswer = new JTextArea (attempt);
					youranswer.setBounds(300, 110, 50,30);
					JTextArea theanswer = new JTextArea(q1.answer);
					theanswer.setBounds(410, 110,50,30);
					
					question2.setBounds(10,160, 280,50);
					question2.setLineWrap(true);
					JTextArea youranswer2 = new JTextArea (attempt2);
					youranswer2.setBounds(300, 160 ,50,30);
					JTextArea theanswer2 = new JTextArea(q2.answer);
					theanswer2.setBounds(410, 160,50,30);
					//adds everything to the JPanel
					add(result);
					add(question1);
					add(question2);
					add(header);
					add(youranswer);
					add(theanswer);
					add(youranswer2);
					add(theanswer2);
					
					JButton quit = new JButton("Exit"); //the button that allows you to quit
					quit.setBounds(50,230,65,20);
					quit.addActionListener(new Quitter());
					add(quit);
					if(type == 0){ //the type - if after u win a level, then you only need 1 right to pass, but if you lost, then u need 2 questins to pass, type == 0 is for loss
						if (score == 2){
							JButton retrylevel = new JButton("Retry");
							retrylevel.setBounds(350,230,65,20);
							retrylevel.addActionListener(new Retry());
							add(retrylevel);	
						}
						else {
							JButton retrylevel = new JButton("New Game");
							retrylevel.setBounds(350,210,100,20);
							retrylevel.addActionListener(new Retry());
							add(retrylevel);	
							numplanes = 1;
							
						}
						health = finalhealth;
					}
					else if (type == 1){ //if you just cleared a wave of planes, this pops up
						if (score == 2){
							JButton retrylevel = new JButton("Continue");
							retrylevel.setBounds(330,230,100,20);
							retrylevel.addActionListener(new Retry());
							
							finalhealth++;
							health = finalhealth;
							add(retrylevel);	
						}
						else if (score==1){ //lets you pass on but no health bonus
							JButton retrylevel = new JButton("Continue");
							retrylevel.setBounds(330,230,100,20);
							retrylevel.addActionListener(new Retry());
							
							health = finalhealth;
							add(retrylevel);
						}
						else { //forces you to restart
							JButton retrylevel = new JButton("Restart Level");
							retrylevel.setBounds(350,210,120,20);
							retrylevel.addActionListener(new Retry());
							add(retrylevel);
							health=finalhealth;
							numplanes--;
						}
					}
					
				}
			}
			class Quitter implements ActionListener{//allows you to go back to the main menu
				public void actionPerformed(ActionEvent e){
					frame.setSize(368, 385);
					frame.remove(gp);
					frame.add(op);
					op.requestFocus();
					moveframe.dispose();
					frame.setLocationRelativeTo(null);
				}
			}
			class Retry implements ActionListener{//allows you to retry the game
				public void actionPerformed(ActionEvent e){
					t = new Tank(); //reinitializes all the varibles
					moveframe.dispose(); 
					pressed = false;
					canbemoved = true;
					bombers.clear();
					if (numplanes == 1){//resets the bombers depending on the number of planes
						bombers.add(new Bomber());
					}
					else if (numplanes == 2){
						bombers.add(new Bomber());
						bombers.add(new Bomber(450,80));
					}
					else if (numplanes ==3){
						bombers.add(new Bomber(220,15));
						bombers.add(new Bomber(650,15));
						bombers.add(new Bomber(450,80));
					}
					else if (numplanes == 4){
						bombers.add(new Bomber(220,15));
						bombers.add(new Bomber(650,15));
						bombers.add(new Bomber(220,80));
						bombers.add(new Bomber(650,80));
					}
					time.start();	//restarts the timer	
					
				
				}
			}

	}
		public void paintComponent(Graphics g){ //draws all the graphics of the game
			super.paintComponent(g);
			g.setColor(Color.black);
			g.fillRect(0,0,getWidth(), getHeight());//paints the background black
			t.drawTank(g);//draws the tank
			boolean b = false;
			for (int i = 0; i < t.bullets.size(); i++){ //draws the bullets. if the bullets either hit a plane or go off the screen, they are removed from the array to not clog up memory
				t.bullets.get(i).drawBullet(g);
				t.bullets.get(i).move();

				for (int j = 0; j < bombers.size(); j++){ //draws the bombers, removes them if they are shot down
					if (bombers.get(j).checkHit(t.bullets.get(i))) {t.bullets.remove(i); b = true; break;}
				}				
				if (!b)
					if (t.bullets.get(i).bx < 0 || t.bullets.get(i).bx > 1090 || t.bullets.get(i).by < 0 || t.bullets.get(i).by > 730) t.bullets.remove(i);
			}
			
			for (int i = 0; i < bombers.size(); i ++){ 
				bombers.get(i).drawBomber(g);
			}
		 	if (t.lost == true){ //manages the loosing of the game
				
				if (t.startboomtimer){
					if (t.boomtimer < 10) g.drawImage(boom2, t.x +25, 565, 100, 60, null);
					else if (t.boomtimer < 20) g.drawImage(boom2, t.x,535, 150,100,null);
					else if (t.boomtimer == 30) t.startboomtimer = false;
					t.boomtimer ++;
				}
				g.setColor(Color.WHITE);
				g.setFont(new Font("Times New Roman", Font.BOLD, 50));
				g.drawString("YOU LOST :(", 400, 300);
				if (t.onetime == 31){ //this method adds a question panel so that when you loose you can revive yourself with the questions
					
					moveframe = new JFrame ("Move on");
					
					moveframe.setSize(510,320);
					Random rand = new Random();
					Question question1 = questions.get(rand.nextInt(questions.size())); //selects 2 seperate questions from the question array to be used
					Question question2;
					do {
						question2 = questions.get(rand.nextInt(questions.size()));
					} while(question2.equals(question1));
					
					moveframe.getContentPane().add(new QuestionPanelLoss(question1, question2, 0), BorderLayout.CENTER); //adds the questionpanel
					moveframe.setLocationRelativeTo(null);;
					moveframe.setVisible(true);

					
					time.stop();
				}
				t.onetime ++ ;
			
			}


		}
		public void keyPressed(KeyEvent e){//controlls the moving of the plane
			
			if (canbemoved){
				if(e.getKeyCode() == KeyEvent.VK_A){t.move(-200);}
				else if (e.getKeyCode() == KeyEvent.VK_D){ t.move(200); }
				canbemoved = false;
			}
			
		}
		public void keyTyped(KeyEvent e){
		}
		public void keyReleased(KeyEvent e){ //makes sure you can not spam move and move too outrageously fast
			canbemoved = true;

		}
		public void mouseMoved(MouseEvent e){ //controls the rotations of the tank gun
			t.rotateStick(e.getX(), e.getY());
			mx = e.getX();
			my = e.getY();
			repaint();

		}
		public void mouseDragged(MouseEvent e){ //allows you to drag the gun around as well
			t.rotateStick(e.getX(), e.getY());
			mx = e.getX();
			my = e.getY();
			repaint();
		}
		public void actionPerformed(ActionEvent e){ //method called every 10 ms
			t.rotateStick(mx, my);
			for (int i = 0; i < bombers.size(); i ++){ //manages the dropping of the bombs of the bombers
				 if (bombers.get(i).exists) bombers.get(i).moveBomber();
				 bombers.get(i).manageDropping(t);
		 	}
			for(int i = 0; i < bombers.size(); i ++){ //manages the removing of the bombers from the array
				if (bombers.get(i).exists == false && bombers.get(i).startboomtimer == false) bombers.remove(i);
			}
			
			if (bombers.isEmpty()){ //manages what happens after waveclear is complete
				numplanes ++;
				
				if (numplanes <= 4){ //if the waves have not maxed out and you have not beat the game, crete question panel to move on
					moveframe = new JFrame ("Move on");
					
					Random rand = new Random();
					moveframe.setSize(500,320);
					Question question1 = questions.get(rand.nextInt(questions.size()));
					Question question2;
					do {
						question2 = questions.get(rand.nextInt(questions.size()));
					} while(question2.equals(question1));
					moveframe.getContentPane().add(new QuestionPanelLoss(question1, question2, 1), BorderLayout.CENTER);
					moveframe.setLocationRelativeTo(null);
					moveframe.setVisible(true);

					
					time.stop();
				}
				else{ //make a congragulatory frame if you clear all the waves
					moveframe = new JFrame ("gratz u won");
					moveframe.setSize(300,200);
					moveframe.getContentPane().add(new JLabel("YAYYY U WON"), BorderLayout.CENTER);
					moveframe.setLocationRelativeTo(null);
					moveframe.setVisible(true);
					JButton exit = new JButton("exit");
					exit.addActionListener(new TwoTime());
					moveframe.getContentPane().add(exit, BorderLayout.SOUTH);
					time.stop();
					
				}
			}
			
			
			
			repaint();
			 
		}
		public void mouseClicked(MouseEvent e){
		}
		public void mousePressed(MouseEvent e){//controls the firing of the bullets
			 t.bullets.add(new Bullet(t.theta, t.lx, t.ly));	
		}
		public void mouseReleased(MouseEvent e){
			pressed = false;
		}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
	
	}
	class Tank  extends Rectangle{ //the tank class which controlls the player tank
		int x,y; //x, y position of the tank
		double lx,ly; //endpoints of the gun
		double theta = 90; //degree of rotation of the gun
		int redtimer = 5; //the timer which controlls the tank going red upon taking damange
		boolean startboomtimer = false;
		int boomtimer = 0;//the explosion animation timer
		boolean lost = false; //checks to see if lost
		int onetime = 0; 
		ArrayList<Bullet> bullets = new ArrayList<Bullet>(); //has all the bullet that the tank has
		public Tank(){ //initializes the x, y, and rectangle
			x = 350;
			lx = x + 100;
			ly = 490;
			this.setBounds(x,y,540,200);
		}
		public void drawTank(Graphics g){ //draws the tank
			Graphics2D g2d = (Graphics2D)g;
			if (!lost){
				g2d.setStroke(new BasicStroke(6));
				onetime = 0;
				if (tstartredtimer){ //draws it in red if hit
					g.drawImage(rtank, x,540,150, 100, null);
					g2d.setColor(Color.RED);
					redtimer --;
					if (redtimer == 0){
						redtimer = 5;
						health--;
						if(health == 0){ //draws the explosion animation
							lost = true;
							startboomtimer= true;
						}
						tstartredtimer = false;
					}
				}
				
				else { //else draws it in white
					g.drawImage(tank, x, 540, 150, 100, null);
					g2d.setColor(Color.WHITE);	
				}
				g2d.drawLine(x + 100, 595, (int)lx, (int)ly); //draws the gun of the tank
				g2d.setStroke(new BasicStroke(1));
				
				this.setBounds(x,540,150,100);
			}
			
			g.setColor(Color.white); //draws the health bar of the tank
			g.setFont(new Font("Times New Roman", Font.BOLD, 15));
			g.drawString("Health", 1030, 541);
			g.setColor(Color.red);
			g.drawRect(1026, 548, 62, 120);
			g.fillRect(1026,548,62,120);
			g.setColor(Color.black);
			g.fillRect(1027,549,61,(120/finalhealth)*(finalhealth-health)-1);
			System.out.println(finalhealth + " - " + health);
		}
		public void rotateStick(double eX, double eY){ //rotates the stick, or gun of the tank accordingly
			double dx = eX - (x + 100); //uses basic trignometry to figure out the points, then ints them for drawing
			double dy = 595 - (eY);
			double slope = dy/dx;
			theta = Math.atan(slope);
			
			if (dx < 0 && dy > 0) theta = theta+Math.PI;
			else if (dx < 0 && dy < 0) theta = theta +  Math.PI;
			lx = x + 100 + 105*Math.cos(theta);
			ly = (595 - 105*Math.sin(theta));
		}
		public void updateGraphics(){
			lx = x+ 100 + 105*Math.cos(theta);

		}
		public void move(int mv){ //allows you to move the tank
			x+= mv;
		}
	}
	class Bullet extends Rectangle{ //the bullet class which controls the bullet objects
		double movex = 0; //how far they move in the x,y directions
		double movey = 0;
		int bx , by; //the x and y position
		double speed = 50; //the speed of the bullet
		public Bullet(double theta, double x, double y){
			movex = speed*Math.cos(theta); //calculates the move speed based on the angle upon which it is fired from
			movey = -speed*Math.sin(theta);					
			bx = (int)x;
			by=(int) y;
			this.setBounds((int)bx,(int)by,10,10);

		}
		public void move(){ //moves the bullets as well as updating the rectangle (which is used to check for collisions)
			bx += (int)movex;
			by += (int)movey;
			this.setBounds((int)bx,(int)by,10,10);

			
		}
		public void drawBullet(Graphics g){ //draws the bullet
			g.setColor(Color.WHITE);
			g.fillOval(bx,by,10,10);
		}  
		
	}
	class Bomber extends Rectangle{ //the class which control the bombers in the game
		int x, y; //x,y, positions
		int health; //health of the plane
		int speed; //speed at which it moves
		int randamount; //how often to drop bombs
		int dropspeed; //speed at whcih bombs are dropd
		boolean r;
		int directiontimer, direction; //controls the direction
		Random rand;
		boolean exists = true; //sees if it still exists
		int redtimer = 0;
		boolean startredtimer = false; //controls the red animation when hit
		boolean startboomtimer = false; //controls the explosion animation when shot down
		int boomtimer = 0; //controls the booooom  aka explosino
		Bomb b; //the bomb which all the bombers have
		boolean dropping = false; //controls droping
		int bombtimer = 0;
		double dropamount = 7;
		public Bomber(){ //constructor initializes x,y positions as well as initializing the rectangle to stick onto it
			x = 450;
			y = 15; 
			
			health = 4;
			speed = 5;
			directiontimer = 0;
			direction = 1;
			rand = new Random();
			r = true;
			b = new Bomb(x + 35, y + 50);
			randamount =rand.nextInt(4) + 1;
			this.setBounds(x,y,100,50);
		}
		public Bomber(int cx, int cy){ //another constructor used if not want the default, does essentially the same thing but not set in a default positino
			x = cx;
			y = cy; 
			
			health = 4;
			speed = 5;
			directiontimer = 0;
			direction = 1;
			rand = new Random();
			r = true;
			b = new Bomb(x + 35, y + 50);
			this.setBounds(x,y,100,50);
		}
		public boolean checkHit(Rectangle r){ //checks to see if it has been hit by any rectangle
			if (exists){
				if(this.intersects(r) || this.contains(r)){
					health -= 1; //reduces the curret health
					if (health == 0) {
						exists = false;
						startboomtimer = true;
					}
					
					startredtimer = true; //starts the red animation
					return true;
				}
				else return false;
			}
			return false;
		}

		public void drawBomber(Graphics g){ //draws the bomber based on the situation 
			if (exists){
				if (!startredtimer){ //if everything is normal
					
					g.setColor(Color.white);
					g.fillOval(x,y+10,100,30);
					g.fillOval(x+25,y,50,20);
				}
				else{  //if it needs to be in red because it has recently been hit by something
					
					g.setColor(Color.red);
					g.fillOval(x,y +10,100,30);
					g.fillOval(x+25,y,50,20);
				}
				g.setColor(Color.RED);
				g.drawRect(x,y-15,100,4);
			//	g.drawRect(x,y-15,100,2);
				g.fillRect(x, y-15, health*25, 4); 
				
			}
			else if(startboomtimer) { //if the plane has been destroyed and you want to draw it in red
				if (boomtimer<5) { g.drawImage(boom, x,y, 100, 100, null);}
				else if (boomtimer < 10) g.drawImage(boom, x + 38, y + 12, 50, 50, null);
				else if (boomtimer == 15) startboomtimer = false;
				boomtimer++;
				
			}	
			if (dropping)b.drawBomb(g); //draws the bomb if the bomb is dropping only

		}
		public void moveBomber(){ //moves the bomber by picking a random integer from 0-1 if 0, you move left for 1 sec, then it rerandomizes and so forth. 1 means to gor gith
			
			if (startredtimer) redtimer++;
			if (redtimer > 5) {
				redtimer = 0;
				startredtimer = false;
			}
			
			
			if (rand.nextInt(21) == 20){ //controls the switching of directions
				if (direction == 1) direction = 0;
				else if (direction == 0) direction = 1;
			}
			if (direction == 1){ //actually moves the plain
				x += 10;
				r = true;
			}
			else if (direction == 0){
				x -= 10;
				r = false;
			}
			
			if (x <= 0) x = 992; //handles the ofscrean plane
			else if ( x >= 992) x = 0;
			
			this.setBounds(x,y,100,50); //updates the bounds
			
		}
		public void manageDropping(Rectangle r){ //drops a bomb every time the correct random number is drawn from the random number generator
			if (!dropping){ //does the random number part
				b.reset(x+ 35, y+50);
				int d = rand.nextInt(50);
				//System.out.println(randamount);
				if ((d == 1) && exists) {
					dropping =true; 
					
				}
			}
			else { //resets the bomb after it has been dropped
				if(b.y < 1000) b.dropBomb(dropamount);
				bombtimer = 0;
				//dropamount += .1;
				//dropamount = 5;
				
				if (b.y >= 700 || b.collided(r)) {
					if (b.collided(r)) tstartredtimer = true;
					if (exists) dropping = false;

				}
				
			}
			
		}
	}
	class Bomb extends Rectangle{ //the class which controls the bombs which fall from the planes
		int x;
		double y;
		
		public Bomb(int x, int y){ //initializes x, y, and constructor
			this.x = x;
			this.y = y;
			setBounds(x,y,30,30);
		}
		public void dropBomb(double mvy){ //drops the bomb
		 	y += mvy;
		}
		public void drawBomb(Graphics g){ //draws the bomb
			g.setColor(Color.white);
			g.fillOval(  x, (int)y, 30,30);
			this.setBounds(x,(int)y,30,30);

		}
		public void reset(int x, int y){//resets the bomb to a specified position
			this.x = x;
			this.y = y;
		}
		public boolean collided(Rectangle r){ //checks to see if the bomb has colided into something
			if (this.intersects(r)) { return true;} 
			else return false;
		}
		
	}
	
	class Question{ //the question object makes it easy to store questions
		String question, answer; //strings whcih hold the question that will be asked and the answer to the question
		public Question(String q, String a){ //initializes the question and answer
			question = q;
			answer = a;
		}
		public boolean isAnswerCorrect(String attempt){//checks to see if the answer is correct
			if (answer.equals(attempt)) return true;
			else return false;
		}
		public boolean equals(Question q){ //checks to see if it is equal to another question (for checking to make sure no 2 questions are alike in the array)
			if (question.equals(q.question) && answer.equals(q.answer)) return true;
			else return false;
		}
	
	}
	
	class GIJoe extends Rectangle{
		int x, y;
		boolean exists = true;
		int draw = 0;
		int takeshot = 0;
		int doshot = 0;
		
		ArrayList<Bullet> bulletarray = new ArrayList<Bullet>();
		public GIJoe (int x, int y){
			this.x = x;
			this.y = y;
			setBounds(x,y,100,50);
			
		}
		public void paintComponent(Graphics g){
			setBounds(x,y,100,50);
			BufferedImage img;
			if (exists){
				switch(draw){
					case 0 : img  = shotman; break;
					case 1: img =  lshotman; break;
					case 2: img = boomshotman; break;
					case 3: img = lboomshotman; break;

							
				
				}
				g.drawImage(img, x, y, 100,50);
			}
			for (int i = 0; i < bulletarray.size(); i ++){
				if ((bulletarray.get(i).bx > 1200)  || bulletarray.get(i).bx < 0 ) bulletarray.remove(i);
			}
			for (int i = 0; i < bulletarray.size(); i ++){
				bulletarray.get(i).drawBullet(g);
				
			}
			if (t.intersects(this)) exists = false;
			for (int i = 0; i < t.bullets.size(); i ++){
				if (t.bullets.get(i).intersects(this)) {
					exists = false; 
				}
			}
			i
			
		}
		
	}
	
	
}


