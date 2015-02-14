//Atilla Saadat
//Helicopter Game

//AJAX for xml update on website

//addNotify() removes null crash

//key listener for panel sometimes wont work. 1. doesnt have focus
//setFocusable(true)  current panel that is on focus
//requestFocus();  
import java.util.*;
import java.util.ArrayList;
import java.io.*;
import java.lang.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;
import javax.swing.Timer;
import java.applet.*;
import javax.sound.sampled.AudioSystem;

public class SimpleGame extends JFrame implements ActionListener{
	Timer myTimer;   
	GamePanel game;
	MainScreen ms;
	AudioClip back;
	
    public SimpleGame() {
		super("Elite Dangerous");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(new ImageIcon("img\\elite.png").getImage());//icon image
		ms= new MainScreen();//menuscreen
		ms.setSize(800,650);
		ms.setVisible(true);
		add(ms);

		myTimer = new Timer(10, this);	 // trigger every 10 ms
		

		game = new GamePanel(this);//gamescreen
		game.setSize(800,650);
		game.setVisible(false);
		add(game);
		setVisible(true);
		setSize(800,650);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setResizable(false);
		back = Applet.newAudioClip(getClass().getResource("res\\sound.wav"));//music
		back.loop();
    }
	public void start(){
		myTimer.start();
	}

	public void actionPerformed(ActionEvent evt){
		if(ms.getName().equals("quit")){//quits if quit is chosen
			System.exit(0);
		}
		if (ms.getName().equals("main")){//sets mainscreen
			game.setVisible(false);
			ms.setVisible(true);
			ms.requestFocus();
			ms.repaint();
		}else{
			if(game.getLose()){//if game is lost, change back to mainscreen
				game.gameReset();
				ms.goToMain();
				game.changeLose();
			}
			ms.setVisible(false);//set gamescreen
			game.setVisible(true);
			game.requestFocus();
			game.move();
			game.repaint();
			
		}

	}

    public static void main(String[] arguments) {
		SimpleGame frame = new SimpleGame();		
    }
}

class MainScreen extends JPanel implements MouseListener, MouseMotionListener{
	int mx,my;
	boolean click = false;
	
	Image mainMenu = new ImageIcon("img\\mainmenu.png").getImage();
	String screen = "main";
	ButtonClass playButton = new ButtonClass(350,385,"play");//button parameters
	ButtonClass quitButton = new ButtonClass (350,455,"quit");
	public MainScreen(){
		super();
		setFocusable(true);
		grabFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		
	}
	public String getName(){//screen state
		return screen;
	}
	
	public void goToMain(){
		screen = "main";
	}
	
	public void paintComponent(Graphics g){
		if (screen.equals("main")){//prints screen + buttons
		//	g.drawImage(background,0,0,this);
			g.drawImage(mainMenu,0,0,this);
			g.drawImage(playButton.getImage(mx,my,click),playButton.getX(),playButton.getY(),this);
			g.drawImage(quitButton.getImage(mx,my,click),quitButton.getX(),quitButton.getY(),this);
		}
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseReleased(MouseEvent e){click=false;}
	public void mousePressed(MouseEvent e){
		click=true;
		if (playButton.getRect().contains(mx,my) && screen.equals("main")){//changes values when clicked
			screen ="play";
		}
		else if (quitButton.getRect().contains(mx,my)&& screen.equals("main")){
			screen = "quit";//changes value and quits
			System.exit(0);
		}
	}
	public void mouseExited (MouseEvent e){}
	public void mouseClicked (MouseEvent e){}
	public void mouseDragged(MouseEvent e){//move mouse
		mx = e.getX();
		my = e.getY();
	}
	public void mouseMoved(MouseEvent e){
		mx =e.getX();
		my= e.getY();
	}
	
}

class ButtonClass{//class for changing button images
	private Image pic1,pic2,pic3;
	private int x,y;
	public ButtonClass(int x, int y,String name){
		this.x=x;
		this.y=y;
		//ex
		pic1 = new ImageIcon(name+"img\\Normal.png").getImage();
		pic2 = new ImageIcon(name+"img\\Hover.png").getImage();
		pic3 = new ImageIcon(name+"img\\Hover.png").getImage();
		
	}
	public Image getImage (int mx, int my, boolean click){
		Rectangle rect = new Rectangle(x,y,pic1.getWidth(null),pic1.getHeight(null));
		if (rect.contains(mx,my) && click){
			return pic3;
		}
		else if (rect.contains(mx,my)){
			return pic2;
		}
		else{
			return pic1;
		}
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public Rectangle getRect(){
		return new Rectangle(x,y,pic1.getWidth(null),pic1.getWidth(null));
	}
	
	
}

class Obstacle{//flying asteroids
	private Rectangle rect;
	private Random rand;
	private int posX,posY;
	private int START_X = 800;
	private int START_Y = 170;
	private final int OBS_WIDTH,OBS_HEIGHT;
	private final Image asteroid;
	private int obsId;
	public Obstacle(ImageIcon image, int obsId){
		rand = new Random();
		this.obsId=obsId;
		OBS_HEIGHT = image.getIconHeight();
		OBS_WIDTH = image.getIconWidth();
		if(obsId!=1){//make sure they dont overlap or have came x pos
			posX = rand.nextInt(1400-1000+1)+1000;
			posY = rand.nextInt((500-OBS_HEIGHT)-150+1)+150;
		}else{
			posX = START_X;
			posY = START_Y;
		}
		asteroid = image.getImage();
		rect = new Rectangle(START_X,START_Y,OBS_WIDTH,OBS_HEIGHT);
	}
	public Rectangle getRect(){
		rect = new Rectangle(posX,posY,OBS_WIDTH,OBS_HEIGHT);
		return rect;
	}
	public Image image(){
		return asteroid;
	}
	
	public void reset(){
		posX = START_X;
		posY = START_Y;
	}
	
	public int moveX(int speed){//move the asteroids at the set speed and reset if over
		if(posX<-(rect.getWidth())){
			posX = 800;
			if(obsId==2){
				posX = rand.nextInt(1400-1000+1)+1000;
			}
		}else{
			posX-=speed;
		}
		return posX;
	}
	public int moveY(){//random y when reset
		if(posX<-(rect.getWidth())){
			Random rand = new Random();
			posY = rand.nextInt((500-OBS_HEIGHT)-150+1)+150;
		}
		return posY;
	}
	
}

class Border{//top and bottom border
	private Polygon poly;
	private ArrayList<Integer> xPolyList = new ArrayList<Integer>();
	private ArrayList<Integer> yPolyList = new ArrayList<Integer>();
    
    private int [] xPoly,yPoly;
    private int maxX,minX,maxY,minY,lastX;
    private int pointCounter = 2;
    private boolean isTop;
    
    private boolean firstPoint = true;
    
    public Border(int topOrBot){
    	if(topOrBot==1){//top box coordinates
    		xPoly = new int [] {0,0,800,800};
    		yPoly = new int [] {0,100,100,0};
    		maxY = 150;
    		minY = 20;
    		isTop = true;
    	}else if(topOrBot==2){//bot box coordinates
    		xPoly = new int [] {0,0,800,800};
    		yPoly = new int [] {650,500,500,650};    	
    		maxY = 630;
    		minY = 500;
    		isTop = false;
    	}
    	maxX = 650;
    	minX = 100;
    	lastX= xPoly[xPoly.length-1];
		for(int i = 0; i < xPoly.length; i++){
    		xPolyList.add(xPoly[i]);
    		yPolyList.add(yPoly[i]);
    	}
    	for(int i = 0; i < 500; i++){
    		addPoints();		
    	}
    	poly = new Polygon(xPoly,yPoly,xPoly.length);
    	//in order to use polygon and add points, i made an arralisy which i can add elements to
    	//then convert to arry then make a new polygon, i do this adding of points a number of times
    }
    
    public Polygon getPoly(){
    	return poly;
    }
    
    private boolean checkRand(int randomNumXTemp){//check if no number less than the prevois x values
    	for(int i:xPolyList){
    		//System.out.println(i+","+randomNumXTemp);
	    	if(randomNumXTemp < i){
	    		return true;
	    	}
	    }
	    return false;
    }
    
    public Point randTop() {//picks a random x and y point for the new border point
    	Random rand = new Random();
	    int randomNumX = (rand.nextInt(pointCounter*(maxX - minX) + 1) +pointCounter*(minX));
	    while(checkRand(randomNumX)){
	    	randomNumX = (rand.nextInt(pointCounter*(maxX - minX) + 1) +pointCounter*(minX));
	    }
	    int randomNumY = rand.nextInt((maxY - minY) + 1) + minY;
	    Point temp = new Point(randomNumX,randomNumY);
	    pointCounter++;
	    return temp;
	}
    
    public Polygon addPoints(){
    	Point newPoint =  randTop();
    	//xPolyList.add();
    	//yPolyList.add();
    	
		xPolyList.remove(xPolyList.size()-1);
		yPolyList.remove(yPolyList.size()-1);//removes previous 0 point whoch closes of the poly
    	
    	xPolyList.add((int)newPoint.getX());//adds the new points
    	yPolyList.add((int)newPoint.getY());  	
    		
    	xPolyList.add(xPolyList.get(xPolyList.size()-1));//add the previous x and new y=0 or y=650 point
    	if(isTop){
    		yPolyList.add(0);	
    	}else{
    		yPolyList.add(650);	
    	}
    	
    	Integer[] xPolyTemp = new Integer[xPolyList.size()];//then convers to array and makes new polygon
    	Integer[] yPolyTemp = new Integer[yPolyList.size()];
    	
    	xPolyTemp = xPolyList.toArray(xPolyTemp);
    	yPolyTemp = yPolyList.toArray(yPolyTemp);
    	
    	xPoly = new int[xPolyTemp.length];
    	yPoly = new int[yPolyTemp.length];
    	
    	for(int i = 0; i < xPolyTemp.length;i++){
    		xPoly[i] = xPolyTemp[i].intValue();	
    		yPoly[i] = yPolyTemp[i].intValue();	
    	}
    	
    	poly = new Polygon(xPoly,yPoly,xPoly.length);
    	return poly;
    	
    }
    
    public Polygon move(int speed){//moves the whole polygon by speed 
    	lastX = xPoly[xPoly.length-2];  	
		if(speed>0){
    		for(int i = 0; i < xPoly.length; i++){
    			xPoly[i]-=speed;
    		}
    		poly = new Polygon(xPoly,yPoly,xPoly.length);	
    	}
    	if(lastX<800){//if last point ends the polygon, recreate the whole polygon
			poly = addPoints();
			return 	poly;
		}
    	return poly;
    }

	//g2.draw(polyline);
}
//class MainScreen extends JPanel implements KeyListener{
	
//}
class GamePanel extends JPanel implements KeyListener{
	private double boxx,boxy;
	private final int TOP_ID = 1;
	private final int BOT_ID = 2;
	private boolean []keys;
	public boolean ready = false;
	public double velocity = 0;
	public double counter = 0;
	private Image back, eagle;
	private SimpleGame mainFrame;
	Border topBorder = new Border(TOP_ID);
	Border botBorder = new Border(BOT_ID);
	Obstacle obs;
	Obstacle obs2;
	private Rectangle eagleBox, eagleTopBox,eagleBotBox;
	private Point eagleTopRight,eagleTopLeft,eagleBotRight,eagleBotLeft;
	
	private int OBS_SPEED = 6;
	private final int BORDER_SPEED = 5;
	private final int BACK_SPEED = 5;
	private final int IMG_WIDTH,IMG_HEIGHT;
	private final int IMG_HITBOX = 1;
	private int backX,backX2,backMaxX;
	private ImageIcon backTemp, eagleTemp;
	private Font font;
	private int level = 0;
	private boolean lose;
	
	private final double GRAV_ACCEL = 0.5 ;
	private final int TERMINAL_VELOCITY = 4;
	private final int MAX_THRUST = 6;
	
	private Scanner read = null;
    private PrintWriter write;
    private String score;
	
	private final String highScoreFile = "res\\highscore.txt";
	
	
	public GamePanel(SimpleGame m){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		setFocusable(true);
		grabFocus();
		backTemp = new ImageIcon("img\\back.png");
		backMaxX = backTemp.getIconWidth()-800;
		eagleTemp = new ImageIcon("img\\eagle.png");
		IMG_WIDTH = eagleTemp.getIconWidth();
		IMG_HEIGHT = eagleTemp.getIconHeight();
		back = backTemp.getImage();
		eagle = eagleTemp.getImage();
		//try{
		
		//}catch(Exception ex){
			//ex.printStackTrace();
		//}
		
		lose = false;
		try{
			read = new Scanner(new File(highScoreFile));//file reader
		}catch(IOException e){}
		try{
			score = read.nextLine();
		}catch(NoSuchElementException e){
			score = "0";
		}
		while(read.hasNextLine()){
			if(Integer.parseInt(score)<=Integer.parseInt(read.nextLine())){//gets the highest score in file
				score = read.nextLine();		
			}
		}
		
		obs = new Obstacle(new ImageIcon("img\\asteroid.png"),1);//astroid objects
		obs2 = new Obstacle(new ImageIcon("img\\asteroid2.png"),2);
		

		mainFrame = m;
		backX = 0;
		backX2 = 800;
	    boxx = 170;
        boxy = 270;
		setSize(800,600);
		eagleBox = new Rectangle((int)boxx,(int)boxy,IMG_WIDTH,IMG_HEIGHT);
		eagleTopBox = new Rectangle((int)boxx,(int)boxy-IMG_HITBOX,IMG_WIDTH,IMG_HITBOX);//top hitbox for collision
		eagleBotBox = new Rectangle((int)boxx,(int)boxy+IMG_HEIGHT+IMG_HITBOX,IMG_WIDTH,IMG_HITBOX);//bot box for collision
        addKeyListener(this);
        loadFont();//load custom font
	}
	
    public void addNotify() {//removes null crash
        super.addNotify();
        ready = true;
        requestFocus();
        mainFrame.start();
    }
	
	public void move(){
		if(keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_UP] ){
			boxy -= MAX_THRUST;//move spaceship by max thrust when space or up is pressed
			velocity = 0;
			counter = 0;
		}

		
		boxy += gravity(velocity);//gravitational acceleration

		
		boxx=boxx<0?0:boxx;//x limits
		boxx=boxx>getWidth()-IMG_HEIGHT?getWidth()-IMG_HEIGHT:boxx;
		
		boxy=Math.max(0,boxy);//y limits
		boxy=Math.min(getHeight()-IMG_HEIGHT,boxy);

		eagleBox = new Rectangle((int)boxx,(int)boxy,IMG_WIDTH,IMG_HEIGHT);
		eagleTopBox = new Rectangle((int)boxx,(int)boxy-IMG_HITBOX,IMG_WIDTH,IMG_HITBOX);//top hitbox for collision
		eagleBotBox = new Rectangle((int)boxx,(int)boxy+IMG_HEIGHT,IMG_WIDTH,IMG_HITBOX);//bot hitbox for collision
	}
	
	public double gravity(double vel){
		velocity =vel+(0.25)*counter;//velocity based on approx. gravitational acceleration up to terminal velocity
		counter += GRAV_ACCEL;
		if (velocity <=TERMINAL_VELOCITY){
			return velocity;
		}else{
			return TERMINAL_VELOCITY;
		}
	}
	public boolean getLose(){//get if the game is lost
		return lose;
	}
	
	public void gameReset(){//reset the game variables and parameters
		keys[KeyEvent.VK_SPACE] = false;
		keys[KeyEvent.VK_UP] = false;
		obs.reset();
		obs2.reset();
		topBorder = new Border(TOP_ID);
		botBorder = new Border(BOT_ID);
		backX = 0;
		backX2 = 800;
	    boxx = 170;
        boxy = 270;
        level = 0;
        velocity = TERMINAL_VELOCITY;
	}
	
	public void changeLose(){//change if the game is lost
		lose = false;
	}
	
	public void updateHighScore(int level){
        try {
        	write = new PrintWriter(new BufferedWriter(new FileWriter(highScoreFile)));//file writer
        	
        } catch (IOException e) {}
        if(score==null){//if there is no highscore, write it as current
        	write.print(level);
        	score = Integer.toString(level);
        }
        if(score!=null){
        	if(level>=Integer.parseInt(score)&&getLose()){//if they lost and the current level beats their highscore
        		write.print(level);
        		score = Integer.toString(level);
        	}
        }
        write.close();
	}
	
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }
    
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    public void loadFont(){
    	try{
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("res\\space_age.ttf"))).deriveFont(0,25);//import font
    	}
    	catch(IOException ioe){
    		System.out.println("font not laoded");
    	}
    	catch(FontFormatException ffe){
    		System.out.println("font not loaded");
    	}
    }
    public void paintComponent(Graphics g){ 
    	
    	if(!getLose()){//if the game is not lost
    	
    	g.setFont(font); 
    	
    		 
    	if(-backX>=backMaxX){//when new level arrives
    		g.drawImage(back,backX,0,this); 
    		backX = 0;
    		level++;
    		if(level>=Integer.parseInt(score)){//update score
    			score = Integer.toString(level);
    		}
    		OBS_SPEED++;//increase obs speed
    	}else{
    		g.drawImage(back,backX,0,this); 
    		backX-=BACK_SPEED;
    	}
    	
    	if(eagleBox.intersects(obs.getRect())||eagleBox.intersects(obs2.getRect())){//if colldies with the asteroids
			lose=true;//lose the game
			updateHighScore(level);//update highscore
		}
		if(topBorder.getPoly().contains(eagleTopBox.getBounds2D())||botBorder.getPoly().contains(eagleBotBox.getBounds2D())){//if colldies with borders
			lose = true;
			updateHighScore(level);
		}
		g.drawPolygon(topBorder.move(BORDER_SPEED));
		g.drawPolygon(botBorder.move(BORDER_SPEED));
		g.setColor(Color.BLACK);  
		g.fillPolygon(topBorder.getPoly());//borders
		g.fillPolygon(botBorder.getPoly());
		g.drawImage(obs.image(),obs.moveX(OBS_SPEED),obs.moveY(),this);//asteroids
		g.drawImage(obs2.image(),obs2.moveX(OBS_SPEED),obs2.moveY(),this);
		g.drawImage(eagle,(int)boxx,(int)boxy,this);//ship
		g.setColor(Color.WHITE);  
		g.drawString("Level "+level,320,20);
		g.drawString("HighScore: "+score,520,20);
		
    	}
    }
}