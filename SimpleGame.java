//Atilla Saadat
//Helicopter Game

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;

public class SimpleGame extends JFrame implements ActionListener{
	Timer myTimer;   
	GamePanel game;
		
    public SimpleGame() {
		super("Move the Box");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,650);

		myTimer = new Timer(10, this);	 // trigger every 10 ms
		

		game = new GamePanel(this);
		add(game);

		setResizable(false);
		setVisible(true);
    }
	
	public void start(){
		myTimer.start();
	}

	public void actionPerformed(ActionEvent evt){
		game.move();
		game.repaint();
	}

    public static void main(String[] arguments) {
		SimpleGame frame = new SimpleGame();		
    }
}

class GamePanel extends JPanel implements KeyListener{
	private int boxx,boxy;
	private boolean []keys;
	private Image back;
	private SimpleGame mainFrame;
	
	public GamePanel(SimpleGame m){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		back = new ImageIcon("OuterSpace.jpg").getImage();
		mainFrame = m;
	    boxx = 170;
        boxy = 170;
		setSize(800,600);
        addKeyListener(this);
	}
	
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
	
	public void move(){
		if(keys[KeyEvent.VK_RIGHT] ){
			boxx += 5;
		}
		if(keys[KeyEvent.VK_LEFT] ){
			boxx -= 5;
		}
		if(keys[KeyEvent.VK_UP] ){
			boxy -= 5;
		}
		if(keys[KeyEvent.VK_DOWN] ){
			boxy += 5;
		}
		
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		Point offset = getLocationOnScreen();
		System.out.println("("+(mouse.x-offset.x)+", "+(mouse.y-offset.y)+")");
	}
	
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }
    
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    
    public void paintComponent(Graphics g){ 	
    	g.drawImage(back,0,0,this);  
		g.setColor(Color.blue);  
		g.fillRect(boxx,boxy,40,40);
    }
}