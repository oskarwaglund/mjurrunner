import java.util.*;
import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Run{
	private Frazze f;
	
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<SentryBullet> sentryBullets = new ArrayList<SentryBullet>();
	private ArrayList<Ladder> ladders = new ArrayList<Ladder>();
	private ArrayList<Sheep> sheep = new ArrayList<Sheep>();
	private ArrayList<SuperSheep> superSheep = new ArrayList<SuperSheep>();
	private ArrayList<Level> levels = new ArrayList<Level>();
	private ArrayList<SheepSpawner> sheepSpawners = new ArrayList<SheepSpawner>();
	private ArrayList<Water> water = new ArrayList<Water>();
	private ArrayList<SheepCannon> cannons = new ArrayList<SheepCannon>();
	private ArrayList<Stalactite> stalactites = new ArrayList<Stalactite>();
	private ArrayList<SentryGun> sentryGuns = new ArrayList<SentryGun>();
	Finish finish;
	private final int B = 1, L = 2, E = 3, TSS = 4, RSS = 5, W = 6, SC = 7, ST = 8, F = 9, SG = 10;
	public final int gridSize = 50;
	public boolean right = false;
	public boolean left = false;
	public boolean up = false;
	public boolean down = false;
	public boolean space = false;
	private boolean ladderHit = false;
	private double time = 0;
	private int level = 1;
	public final static int fps = 25;
	private Sound intro = new Sound("sounds/tempintro.wav");
	private Sound hopp = new Sound("sounds/hopp.wav");
	private Sound aj = new Sound("sounds/aj.wav");
	private Sound oj = new Sound("sounds/oj.wav");
	private Sound helvete = new Sound("sounds/helvete.wav");
	private Sound dieSheep = new Sound("sounds/dödittfår.wav");
	private int nextAction = 1;
	
	private int shotsFired = 0;
	private int shotsHit = 0;
	public boolean released = false;
	private double screenX = 0;
	public boolean mouseDown = false;

	
	public static void main(String[] args) throws InterruptedException{
		new MouseInput().run();
		
	}
	protected ScreenManager s;
	private Image[][][] fransar = new Image[9][4][2];
	private Image[] climbImages = new Image[2];
	private Image bg, bullet, sheepIL, sheepHIL, sheepIR, sheepHIR, superSheepL,
	ladder, ladderRTilt, ladderLTilt, cannon,
	b1, b2, b3, b4, b5, b6, b7, b8, b9, stalactite, mapOfGotland, finishImage;
	private boolean running;

	private static final DisplayMode modes1[] = {
		new DisplayMode(800,600,32,0),
		new DisplayMode(800,600,24,0),
		new DisplayMode(800,600,16,0),
		new DisplayMode(800,480,32,0),
		new DisplayMode(800,480,24,0),
		new DisplayMode(800,480,16,0),
	};

	public void stop(){
		running = false;
	}

	//laddar in bilder och lägger till scener
	public void loadImages(){
		bg = new ImageIcon("images/gotland.jpg").getImage();
		for(int angle = 0; angle < 90; angle += 10){
			for(int stepType = 1; stepType < 4; stepType++){
				
				fransar[angle/10][stepType-1][0] = new ImageIcon("images/frans_"+angle+"_"+stepType+"_R.png").getImage();
				fransar[angle/10][stepType-1][1] = new ImageIcon("images/frans_"+angle+"_"+stepType+"_L.png").getImage();
				
			}
			fransar[angle/10][3][0] = new ImageIcon("images/frans_"+angle+"_J_R.png").getImage();
			fransar[angle/10][3][1] = new ImageIcon("images/frans_"+angle+"_J_L.png").getImage();
		}
		climbImages[0] = new ImageIcon("images/frans_40_2_R.png").getImage();
		climbImages[1] = new ImageIcon("images/frans_40_2_L.png").getImage();
		bullet = new ImageIcon("images/bullet.png").getImage();
		sheepIL = new ImageIcon("images/sheepLeft.png").getImage();
		sheepHIL = new ImageIcon("images/sheepLeftHit.png").getImage();
		sheepIR = new ImageIcon("images/sheepRight.png").getImage();
		sheepHIR = new ImageIcon("images/sheepRightHit.png").getImage();
		superSheepL = new ImageIcon("images/superSheepFlying.png").getImage();
		ladder = new ImageIcon("images/ladder.png").getImage();
		ladderRTilt = new ImageIcon("images/ladderRTilt.png").getImage();
		ladderLTilt = new ImageIcon("images/ladderLTilt.png").getImage();
		b1 = new ImageIcon("images/B1.png").getImage();
		b2 = new ImageIcon("images/B2.png").getImage();
		b3 = new ImageIcon("images/B3.png").getImage();
		b4 = new ImageIcon("images/B4.png").getImage();
		b5 = new ImageIcon("images/B5.png").getImage();
		b6 = new ImageIcon("images/B6.png").getImage();
		b7 = new ImageIcon("images/B7.png").getImage();
		b8 = new ImageIcon("images/B8.png").getImage();
		b9 = new ImageIcon("images/B9.png").getImage();
		stalactite = new ImageIcon("images/stalactite.png").getImage();
		cannon = new ImageIcon("images/cannon.png").getImage();
		mapOfGotland = new ImageIcon("images/mapOfGotland.png").getImage();
		finishImage = new ImageIcon("images/finish.png").getImage();
	}

	//huvudmetoden som kallas från main
	public void run() throws InterruptedException{
		try{
			
			init();
			loadImages();
			createLevels();
			while(running){
				performNextAction();
			}
		}finally{
			s.restoreScreen();
		}
	}
	///init
	public void init(){
		s = new ScreenManager();
		DisplayMode dm = s.findFirstCompatibleMode(modes1);
		s.setFullScreen(dm);
		
		Window w = s.getFullScreenWindow();
		
		running = true;
	} 






	//spelar upp "filmen"
	public void gameLoop(){
			long loopTime = System.currentTimeMillis();
		
			
			//Ritar och uppdaterar innehållet
			
			checkKeys();
			sheepSpawner();
			checkScreenScroll(); 
			checkFire();
			checkFinish();
			moveObjects();
			Graphics2D g = s.getGraphics();
			draw(g);
			g.dispose();
			s.update();  // ej samma som i animationsklassen
			if(f.isDead()){
				nextAction = 5;
			}
			try{
				Thread.sleep(1000/fps - System.currentTimeMillis() + loopTime);
				time += 1./fps;
			}catch(Exception ex){}  
		//}
	}

	//ritar grafiken
	public void draw(Graphics g){
		int position = (int)screenX/bg.getWidth(null) / 2;
		for(int i = position - 2; i <= position + 2; i++){
			g.drawImage(bg, bg.getWidth(null) * i -(int)screenX/2, 0, null);
		}
		if(finish != null)
			g.drawImage(finishImage, finish.getX() - gridSize/2 - (int)screenX, finish.getY() - (int)(2.5*gridSize), null);
		
		for(int i = 0; i < stalactites.size(); i++){
			g.drawImage(stalactite, stalactites.get(i).getX() - gridSize/2 - (int)screenX, stalactites.get(i).getY()-gridSize/2 - 10, null);
		}
		
		for(int i = 0; i < blocks.size(); i++){
			Block b = blocks.get(i);
			if(b.getType() == 1)
				g.drawImage(b1, b.getX() - (int)screenX - gridSize/2, b.getY() - gridSize/2, null);
			if(b.getType() == 2)
				g.drawImage(b2, b.getX() - (int)screenX - gridSize/2, b.getY() - gridSize/2, null);
			if(b.getType() == 3)
				g.drawImage(b3, b.getX() - (int)screenX - gridSize/2, b.getY() - gridSize/2, null);
			if(b.getType() == 4)
				g.drawImage(b4, b.getX() - (int)screenX - gridSize/2, b.getY() - gridSize/2, null);
			if(b.getType() == 5)
				g.drawImage(b5, b.getX() - (int)screenX - gridSize/2, b.getY() - gridSize/2, null);
			if(b.getType() == 6)
				g.drawImage(b6, b.getX() - (int)screenX - gridSize/2, b.getY() - gridSize/2, null);
			if(b.getType() == 7)
				g.drawImage(b7, b.getX() - (int)screenX - gridSize/2, b.getY() - gridSize/2, null);
			if(b.getType() == 8)
				g.drawImage(b8, b.getX() - (int)screenX - gridSize/2, b.getY() - gridSize/2, null);
			if(b.getType() == 9)
				g.drawImage(b9, b.getX() - (int)screenX - gridSize/2, b.getY() - gridSize/2, null);
			
		}
		for(int i = 0; i < ladders.size(); i++){
			Ladder l = ladders.get(i);
			if(l.getType() == 0)
				g.drawImage(ladder, l.getX() - (int)screenX - gridSize/2, l.getY() -gridSize/2, null);
			else if(l.getType() == 1)
				g.drawImage(ladderRTilt, l.getX() - (int)screenX - gridSize/2, l.getY() -gridSize/2, null);
			else
				g.drawImage(ladderLTilt, l.getX() - (int)screenX - gridSize/2, l.getY() -gridSize/2, null);

		}
		if(f.isClimbing()){
			g.drawImage(climbImages[f.getClimbCount()], Math.round(f.getXPos()-(int)screenX - climbImages[0].getWidth(null) / 2), Math.round(f.getYPos() - climbImages[0].getHeight(null)/2), null);
		} else {
			int angleIndex = f.getAngle(MouseInfo.getPointerInfo().getLocation().x + (int)screenX, MouseInfo.getPointerInfo().getLocation().y, (int)screenX)/10;
			int stepIndex = f.getStepCount();
			int dirIndex = 1;
			if(MouseInfo.getPointerInfo().getLocation().x + (int)screenX> f.getXPos())
				dirIndex = 0;
			if(stepIndex == 0 || stepIndex == 4)
				stepIndex = 2;
			if(f.isJumping())
				g.drawImage(fransar[angleIndex][3][dirIndex], Math.round(f.getXPos()-(int)screenX - fransar[0][3][0].getWidth(null) / 2), Math.round(f.getYPos() - fransar[0][3][0].getHeight(null)/2), null);
			else
				g.drawImage(fransar[angleIndex][stepIndex-1][dirIndex], Math.round(f.getXPos()-(int)screenX - fransar[0][2][0].getWidth(null) / 2), Math.round(f.getYPos() - fransar[0][2][0].getHeight(null)/2), null);
		}
		
		g.fillRect(f.getXPos() - 3 - (int)screenX, f.getYPos()-3, 6, 6);

		
		int sizeDiv2 = sheepIL.getHeight(null) / 2;
		
		for(int i = 0; i < sheep.size(); i++){
			Sheep sh = sheep.get(i);
			if(sh.getDir() == -1){
				if(sh.isHit()){
					g.drawImage(sheepHIL, Math.round(sh.getXPos()-(int)screenX - sizeDiv2), Math.round(sh.getYPos() - sizeDiv2), null);
					sh.setHit();
					
				} else {
					g.drawImage(sheepIL, Math.round(sh.getXPos()-(int)screenX - sizeDiv2), Math.round(sh.getYPos() - sizeDiv2), null);
				}
			} else {
				if(sh.isHit()){
					g.drawImage(sheepHIR, Math.round(sh.getXPos()-(int)screenX - sizeDiv2), Math.round(sh.getYPos() - sizeDiv2), null);
					sh.setHit();
					
				} else {
					g.drawImage(sheepIR, Math.round(sh.getXPos()-(int)screenX - sizeDiv2), Math.round(sh.getYPos() - sizeDiv2), null);
				}
			}

		}
		
		for(int i = 0; i < superSheep.size(); i++){
			SuperSheep sh = superSheep.get(i);
			g.drawImage(sheepIL, Math.round(sh.getXPos()-(int)screenX - sizeDiv2), Math.round(sh.getYPos() - sizeDiv2), null);
		}

		
		sizeDiv2 = bullet.getHeight(null) / 2;
		for(int i = 0; i < bullets.size(); i++){
			g.drawImage(bullet, Math.round(bullets.get(i).getX()-(int)screenX - sizeDiv2), Math.round(bullets.get(i).getY() - sizeDiv2), null);
		}
		
		for(int i = 0; i < sentryBullets.size(); i++){
			g.drawImage(bullet, Math.round(sentryBullets.get(i).getX()-(int)screenX - sizeDiv2), Math.round(sentryBullets.get(i).getY() - sizeDiv2), null);
		}
		
		drawHUD(g);
		g.setColor(Color.BLUE);
		for(int i = 0; i < water.size(); i++){
			g.fillRect(water.get(i).getX()-gridSize/2 - (int)screenX, water.get(i).getY()-gridSize/2 + 10, gridSize, gridSize);
		}
		
		
		
		for(int i = 0; i < cannons.size(); i++){
			g.drawImage(cannon, cannons.get(i).getX() - gridSize/2 - (int)screenX, cannons.get(i).getY()-gridSize/2, null);
		}
	
	}



	public void checkKeys(){
		ladderHit = false;
		boolean clearRight = true;
		boolean clearLeft = true;
		boolean clearDown = true;
		boolean clearUp = true;
		if(f.getXSpeed() >= 0){
			clearRight = true;
			for(int i = 0; i < blocks.size(); i++){
				if(f.collide(blocks.get(i), 30, 25) || f.collide(blocks.get(i), 30, -25))
					clearRight = false;
			}
			if(!clearRight) f.setXSpeed(0);
		}
		if(f.getXSpeed() <= 0){
			clearLeft = true;
			for(int i = 0; i < blocks.size(); i++){
				if(f.collide(blocks.get(i), -30, 25) || f.collide(blocks.get(i), -30, -25))
					clearLeft = false;
			}
			if(!clearLeft) f.setXSpeed(0);
		}
		if(f.getYSpeed() <= 0 || f.isClimbing()){
			int index = 0;
			clearDown = true;
			for(int i = 0; i < blocks.size(); i++){
				if(f.collide(blocks.get(i), 0, -50)){
					clearUp = false;
					index = i;
				}
			}
			if(f.isJumping() && !clearUp){
				f.setYSpeed(-f.getYSpeed());
			}
		}
		if(f.getYSpeed() >= 0){
			int index = 0;
			clearDown = true;
			for(int i = 0; i < blocks.size(); i++){
				if(f.getYSpeed() < 40){
					if(f.collide(blocks.get(i), -10, 50) || f.collide(blocks.get(i), 10, 50)){
						clearDown = false;
						index = i;
					}
				} else {
					if(f.collide(blocks.get(i), 0, 50) || f.collide(blocks.get(i), 0, 25)){
						clearDown = false;
						index = i;
					}
				}
			}
			if(!clearDown){
				f.setYSpeed(0); 
				f.setJumping(false); 
				f.setStanding(true); 
				f.setClimbing(false);
				if(!up && !ladderHit)
				f.setY(blocks.get(index).getY()-70);
			}
		}
		for(int i = 0; i < ladders.size(); i++){
			Ladder l = ladders.get(i);
			if(f.checkLadder(l))
				ladderHit = true;
		}

		if(left){
			f.step();
			if(clearLeft)
			f.accelerate(-1);
			f.checkWaterSlowDown(water);
			
		} else if(right){
			f.step();
			if(clearRight)
			f.accelerate(1);
			f.checkWaterSlowDown(water);
		} 
		if(space){
			if(!f.isJumping()){
				f.jump();
				hopp.play();
				space = false;
			} 
		} else {
			if(ladderHit)
				if(up){
					f.setYSpeed(0);
					if(clearUp)
					f.climb(-1);
					f.setClimbing(true);
					f.setJumping(false);
					f.setStanding(false);
				} else if(down){
					f.setYSpeed(0);
					f.climb(1);
					f.setClimbing(true);
					f.setJumping(false);
					f.setStanding(false);
				} 
		}
		if(clearDown && !f.isClimbing()){
			f.setStanding(false);
			f.setJumping(true);
		}
		if(!ladderHit && f.isClimbing()){
			f.setJumping(true);
			f.setClimbing(false);
		} 
		
		if(!right && !left && !space)
			f.slide();
	}

	void expressPain() {
		ArrayList<Sound> sounds = new ArrayList<>();
		sounds.add(aj);
		sounds.add(oj);
		sounds.add(helvete);
		
		int index = (int)(Math.random() * sounds.size());
		sounds.get(index).play();
	}

	public void moveObjects(){
		//Frazze
		f.move();
		f.handleInvincibility();
		for(int i = 0; i < sheep.size() && !f.getInvincibility(); i++){
			if(f.collide(sheep.get(i))){
				expressPain();
				f.dropHealth(10);
				break;
				
			}
		}
		for(int i = 0; i < superSheep.size() && !f.getInvincibility(); i++){
			if(f.collide(superSheep.get(i))){
				expressPain();
				f.dropHealth(15);
				break;
				
			}
		}
		//Bullets
		for(int i = bullets.size() - 1; i >= 0; i--){
			boolean remove = false;
			for(int j = 0; j < blocks.size(); j++){
				if(bullets.get(i).collide(blocks.get(j)))
					remove = true;
			}
			if(bullets.get(i).fly() || remove)
				bullets.remove(i);	
		}
		
		for(int i = sentryBullets.size() - 1; i >= 0; i--){
			boolean remove = false;
			if(f.collide(sentryBullets.get(i))){
				f.dropHealth(5);
				sentryBullets.remove(i);
				break;
			}
			for(int j = 0; j < blocks.size(); j++){
				if(sentryBullets.get(i).collide(blocks.get(j))){
					remove = true;
					break;
				}
			}
			if(sentryBullets.get(i).fly() || remove)
				sentryBullets.remove(i);	
		}
		//Sheep
		for(int i = sheep.size() - 1; i >= 0; i--){
			Boolean fall = true;
			Sheep s = sheep.get(i);
			s.move();
			Block actualB = null;
			for(int j = 0; j < blocks.size(); j++){
				Block b = blocks.get(j);
				if(s.checkFloor(b)){
					fall = false;
					actualB = b;
				}
				sheep.get(i).collide(b);
			}
			if(!fall && s.isFalling()){
				s.setGroundLevel(actualB.getY() - actualB.size() / 2 - 28);
				s.setFalling(false);
				s.setYSpeed(0);
			} else if(fall && !s.isFalling()){
				s.setFalling(true);
			}
			
			if(sheep.get(i).getYPos() > 2000){
				sheep.remove(i);
			} else {


				for(int j = bullets.size() - 1; j >= 0; j--){
					if(sheep.get(i).collide(bullets.get(j))){
						bullets.remove(j);
						shotsHit++;
						if(sheep.get(i).getHealth() <= 0){
							sheep.remove(i);
							j = -1;
							f.increaseKillCount();
						} 
					} 
				}
			}
			
			

		}
		
		for(int i =  superSheep.size()-1; i >= 0; i--){
			superSheep.get(i).move();
			boolean removed = false;
			for(int j = 0; j < blocks.size(); j++){
				if(superSheep.get(i).collide(blocks.get(j))){
					removed = true;
					superSheep.remove(i);
					break;
				}
			}
			if(!removed)
			for(int j = bullets.size() - 1; j >= 0; j--){
				if(superSheep.get(i).collide(bullets.get(j))){
					bullets.remove(j);
					shotsHit++;
					if(superSheep.get(i).getHealth() <= 0){
						superSheep.remove(i);
						f.increaseKillCount();
						break;
					} 
				} 
			}
			
				
		}
		
		//Cannons
		for(int i = 0; i < cannons.size(); i++){
			
			SheepCannon c = cannons.get(i);
			c.count();
			if(c.isReadyToFire()){
				SuperSheep sh = null;
				superSheep.add(sh = new SuperSheep(c.getX() - gridSize, c.getY()));
				sh.setDirection(c.getDir());
			}
		}
		
		checkForStalactites();
		for(int i = stalactites.size()-1; i >= 0; i--){
			if(stalactites.get(i).isFalling()){
				stalactites.get(i).move();
				
				if(f.collide(stalactites.get(i))){
					f.dropHealth(stalactites.get(i).getYSpeed());
					stalactites.remove(i);
				}
			}
		}
		
		//Sentry Gun 
		for(int i = sentryGuns.size() - 1; i >= 0; i--){
			SentryGun sg = sentryGuns.get(i);
			sg.update(f);
			if(sg.isReadyToFire()){
				if(Math.abs(sg.getAngle()) <= 90){
					sentryBullets.add(new SentryBullet(sg.getX(), sg.getY(), Math.toRadians(sg.getAngle() + 90), (int)screenX));
				}	
			}
		}
	}

	public void checkScreenScroll(){
		int xPos = f.getXPos();
		//double xSpeed = f.getXSpeed();

		if(xPos - screenX > 600)
			screenX = xPos - 600;
		if(xPos - screenX < 100)
			screenX = xPos - 100;
		
	}

	public void sheepSpawner(){
		for(int i = 0; i < sheepSpawners.size(); i++){
			SheepSpawner ss = sheepSpawners.get(i);
			if(ss.check()){
				Sheep sh = null;
				sheep.add(sh = new Sheep(ss.getX(), ss.getY()));
				sh.setDirection(ss.getDir());
			}
		}
	}

	public void checkFire(){
		if(mouseDown && !f.isClimbing()){
			double angle = f.fire(MouseInfo.getPointerInfo().getLocation().x + (int)screenX, MouseInfo.getPointerInfo().getLocation().y);
			if(f.getXPos() - (int)screenX>= MouseInfo.getPointerInfo().getLocation().x){
				angle += Math.PI;
			}
			bullets.add(new Bullet(f.getXPos(), f.getYPos(), angle, screenX));
			shotsFired++;
			
		}
	}

	public void createLevels(){
		//Level 1
		levels.add(new Level(60, 13));
		Level l = levels.get(0);
		l.setMapXY(230, 240);
		l.insertRect(1, 13, 59, 1);
		l.insert(6, 12, B);
		l.insert(12, 12, B);
		l.insertLadder(4, 12, 10);
		l.insertLadder(18, 13, 10);
		l.insertRect(5, 4, 3, 1);
		l.insertRect(9, 4, 2, 2);
		l.insertRect(13, 4, 2, 2);
		l.insertRect(17, 4, 2, 2);
		l.insertRect(7, 8, 6, 2);
		
		//Level 2
		levels.add(new Level(20, 13));
		
		l = levels.get(1);
		l.setMapXY(200, 300);
		l.insertRect(12, 3, 1, 6);
		l.insertRect(10, 5, 2, 2);
		l.insertRect(8, 6, 2, 1);
		l.insertRect(5, 5, 1, 4);
		l.insertRect(6, 8, 7, 1);
		l.insertRect(1, 12, 15, 1);
		
		//Level 3
		levels.add(new Level(20, 12));
		l = levels.get(2);
		l.setStartPoint(1, 2);
		l.setLevelName("Sheepspawner Test");
		l.setDescription("Testing a timed sheepSpawner at (5, 10) and a random sheepSpawner at (10, 10).");
		l.setMapXY(120, 500);
		l.insertRect(1, 5, 1, 1);
		l.insertRect(1, 12, 20, 1);
		l.insertTimedSheepSpawner(5, 10, 1, -1);
		l.insertRandomSheepSpawner(10, 10, 0.01, 1);
		
		//Level 4 Almedalen
		levels.add(new Level(300, 12));
		l = levels.get(3);
		l.setStartPoint(60, 1);
		l.setLevelName("The great stampede of Almedalen");
		l.setDescription("The sheep are storming Almedalen with all their forces. There is no use in shooting them now, get to the finish!");
		l.setLocation("Almedalen");
		l.setMapXY(100, 225);
		l.setHours(10);
		l.setMinutes(59);
		l.setSeconds(57);
		l.setParTime(60);
		l.insertLadder(5, 11, 5);
		l.insert(3, 11, W);
		
		l.insertRect(1, 12, 299, 1);
		l.insertRect(6, 8, 5, 1);
		l.insert(14, 9, B);
		l.insertRect(15, 7, 1, 3);
		l.insertRect(16, 5, 1, 5);
		l.insertRect(17, 3, 1, 7);
		l.insertRect(22, 10, 5, 1);
		l.insertLadder(29, 10, 5);
		l.insertLadder(32, 10, 5);
		l.insertLadder(35, 10, 5);
		l.insertRect(29, 5, 6, 1);
		l.insertRect(37, 3, 3, 1);
		l.insertLadder(40, 2, 2);
		l.insertLadder(41, 2, 2);
		l.insertLadder(42, 2, 2);
		l.insertLadder(43, 2, 2);
		l.insertRect(48, 9, 10, 1);
		l.insert(58, 10, B);
		l.insert(60, 9, B);
		l.insert(64, 9, B);
		l.insert(68, 9, B);
		l.insert(71, 7, B);
		l.insertRect(76, 10, 4, 1);
		l.insertLadder(81, 10, 10);
		l.insertRect(83, 3, 1, 8);
		l.insertRect(83, 2, 2, 1);
		l.insertRect(88, 1, 1, 7);
		l.insertRect(86, 10, 8, 1);
		l.insertRect(86, 5, 2, 1);
		l.insertRect(84, 8, 1, 1);
		for(int i = 13; i < 290; i++){
			l.insert(i, 11, E);
		}
		l.insertSheepCannon(290, 11, 1, -1);
		l.insertTimedSheepSpawner(289, 11, 0.4, -1);
		l.insert(93, 9, F);
		
		//Level 5 
		levels.add(new Level(20, 12));
		l = levels.get(4);
		l.setLevelName("Water and Stalagmite test");
		l.setDescription("Just tryin' out da shiet...");
		l.setMapXY(200, 200);
		l.insertRect(1, 12, 20, 1);
		l.insert(2, 11, F);
		l.insert(4, 11, B);
		l.insert(5, 11, W);
		l.insert(6, 11, W);
		l.insert(7, 11, W);
		l.insert(8, 11, B);
		l.insertSheepCannon(15, 5, 1, -1);
		l.insert(15, 6, B);
		l.insert(15, 7, ST);
		l.insert(5, 5, B);
		l.insert(5, 1, SG);
		
		//Level 6
				levels.add(new Level(100,12));
				l = levels.get(5);
				l.setLevelName("Complete darkness");
				l.setDescription("Enemy sheep forces have occupied the caves of Lummelunda. Your mission is to go into the cave and cleanse it.");
				l.setMapXY(140, 160);
				l.setLocation("Lummelundagrottan");
				l.setStartPoint(2, 5);
				l.setHours(6);
				l.setMinutes(59);
				l.setSeconds(57);
				l.insertRect(1, 1, 1, 12);
				l.insertRect(1, 1, 98, 1);
				l.insertRect(1, 12, 98, 1);
				l.insertRect(2, 9, 4, 3);
				l.insertRect(10, 10, 2, 2);
				l.insertRect(12, 8, 2, 4);
				l.insertRect(14, 6, 2, 6);
				l.insertRect(22, 11, 7, 1);
				l.insertRect(25, 1, 2, 8);
				l.insertRect(27, 6, 1, 3);
				l.insertRect(28, 8, 6, 1);
				l.insertRect(32, 7, 1, 1);
				l.insertRect(29, 4, 2, 2);
				l.insertRect(31, 4, 5, 1);
				l.insertRect(36, 6, 2, 6);
				l.insertRect(35, 10, 1, 1);
				
				l.insertRect(39, 2, 1, 8);
				l.insertRect(41, 5, 20, 7);
				l.insertRect(45, 2, 20, 3);
				
				l.insert(44, 4, F);
				l.insertLadder(40, 11, 10);
				
				l.insert(7, 10, E);
				l.insert(18, 10, E);
				l.insert(32, 10, E);
				l.insert(29, 6, E);
	}

	public void loadLevel(){
		resetVariables();
		Level lev = levels.get(level - 1);
		f = new Frazze(lev.getStartX()*gridSize - gridSize/2, lev.getStartY()*gridSize - gridSize/2);
		for(int x = 1; x <= lev.getWidth(); x++){
			for(int y = 1; y <= lev.getHeight(); y++){
				int objectID = lev.get(x, y);
				Block b = null;
				Ladder l = null;
				Sheep sh = null;
				SheepSpawner ss = null;
				SheepCannon sc = null;
				switch(objectID){
					case 1: blocks.add(b = new Block((int)((x-0.5)*gridSize) , (int)((y-0.5)*gridSize))); break;
					case 2: ladders.add(l = new Ladder((int)((x-0.5)*gridSize), (int)((y-0.5)*gridSize))); break;
					case 3: sheep.add(sh = new Sheep((int)((x-0.5)*gridSize), y * gridSize)); break;
					case 4: sheepSpawners.add(ss = new SheepSpawner((int)((x-0.5)*gridSize), (int)((y-0.5)*gridSize), 1)); break;
					case 5: sheepSpawners.add(ss = new SheepSpawner((int)((x-0.5)*gridSize), (int)((y-0.5)*gridSize), 2)); break;
					case 6: water.add(new Water((int)((x-0.5)*gridSize), y * gridSize)); break;
					case 7: cannons.add(sc = new SheepCannon((int)((x-0.5)*gridSize), (int)((y-0.5)*gridSize))); break;
					case 8: stalactites.add(new Stalactite((int)((x-0.5)*gridSize), (int)((y-0.5)*gridSize))); break;
					case 9: finish = new Finish((int)((x-0.5)*gridSize), (int)((y-0.5)*gridSize)); break;
					case 10: sentryGuns.add(new SentryGun((int)((x-0.5)*gridSize), (int)((y-0.5)*gridSize))); break;
				}
				if(objectID == 1){
					boolean noUp = true;
					boolean noRight = true;
					boolean noLeft = true;
					boolean noDown = true;
					if(y!=1){
						if(lev.get(x, y-1) == 1){
							noUp = false;
						}

					}
					if(x!=1){
						if(lev.get(x-1, y) == 1){
							noLeft = false;
						}
					}
					if(x!=lev.getWidth()){
						if(lev.get(x+1, y) == 1)
							noRight = false;
					}
					if(y!=lev.getHeight()){
						if(lev.get(x, y+1) == 1){
							noDown = false;
						}
					}

					if(noUp && noLeft && !noRight)
						b.setType(1);
					else if(noUp && !noRight && !noLeft || noUp && noRight && noLeft && !noDown || noUp && noRight && noLeft && noDown){
						b.setType(2);
					}
					else if(noUp && noRight && !noLeft)
						b.setType(3);
					else if(noLeft && !noUp && !noRight && !noDown)
						b.setType(4);
					else if(noRight && !noUp && !noLeft && !noDown)
						b.setType(6);
					else if(noDown && noLeft && !noUp && !noRight)
						b.setType(7);
					else if(noDown && !noUp && !noRight && !noLeft)
						b.setType(8);
					else if(noDown && noRight && !noUp && !noLeft)
						b.setType(9);
					else
						b.setType(5);
				}
				
				if(objectID == 2){
					l.setType((int)Math.round(Math.random()*3));
				}
				
				if(objectID == 4){
					ss.setSpawnInterval(lev.getObjectValue(x, y));
					ss.setDir(lev.getDirValue(x, y));
				}
				if(objectID == 5){
					ss.setSpawnChance(lev.getObjectValue(x, y));
					ss.setDir(lev.getDirValue(x, y));
				}
				
				if(objectID == 7){
					sc.setFiringRate(lev.getObjectValue(x, y));
					sc.setDir(lev.getDirValue(x, y));
				}
					
			
			}
		}
		nextAction = 3;
	}
	
	public void drawHUD(Graphics g){
		g.setColor(Color.BLACK);
		//g.setFont();
		g.drawString("Kills: " + f.getKills() + "   Sheep: " + sheep.size(), 50, 50);

		int seconds = (int)(time % 60);
		int minutes = (int)(time / 60);
		int parSeconds = levels.get(level-1).getParTime()%60;
		int parMinutes = levels.get(level-1).getParTime()/60;
		String k = "";
		String parK = "";
		if (seconds < 10)
			k="0";
		if (parSeconds < 10)
			parK="0";
		g.drawString("Time: " + minutes + ":" + k + seconds + " (" + parMinutes + ":" + parK + parSeconds + ")", 50, 30);
		int health = f.getHealth();
		g.fillRect(18, 58, 104, 24);
		g.setColor(Color.RED);
		g.fillRect(20, 60, 100, 20);
		g.setColor(Color.GREEN);
		g.fillRect(20, 60, health, 20);
		g.setColor(Color.BLACK);
		g.drawString(health + "%", 60, 76);
		
		
		
	}
	
	public void showLevelIntro() throws InterruptedException{
		intro.play();
		Graphics2D g = s.getGraphics();
		Level l = levels.get(level-1);
		Font levelFont = new Font("Arial", Font.PLAIN, 50);
		Font areaFont = new Font("Arial", Font.PLAIN, 30);
		Font descriptionFont = new Font("Arial", Font.PLAIN, 20);
		String levelName = l.getLevelName();
		String k = l.getDescription();
		int length = k.length();
		String[] descRows = new String[length / 50 + 1];
		int currentRow = 0;
		descRows[0] = "";

		double seconds = l.getSeconds();
		int minutes = l.getMinutes();
		int hours = l.getHours();

		double loopTime = 0.04;
		int i = 0;
		double timePassed = 0;
		
		while (timePassed < 5 + length/40){
			g = s.getGraphics();
			

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, s.getWidth(), s.getHeight());
			g.setColor(Color.WHITE);
			g.setFont(levelFont);
			g.drawString(levelName, 50, 100);
			
			g.setFont(areaFont);
			g.drawString(l.getLocation(), 50, 140);
			String sString = "";
			String mString = "";
			String hString = "";

			if(seconds < 10)
				sString = "0";
			if(minutes < 10)
				mString = "0";
			if(hours < 10)
				hString = "0";

			seconds += loopTime;
			timePassed += loopTime;
			if(seconds >= 60){
				minutes++;
				seconds = 0;
			}
			if(minutes > 59){
				hours++;
				minutes = 0;
			}
			if(hours > 23)
				hours = 0;
			g.drawString(hString + hours + ":" + mString + minutes + ":" + sString + (int)seconds, 50, 175);
			
			
			if(i < length){
				descRows[currentRow] += k.substring(i, i+1);
				i++;
			}
			if(descRows[currentRow].length() > 50){
				int spaceIndex = descRows[currentRow].lastIndexOf(32);
				descRows[currentRow+1] = descRows[currentRow].substring(spaceIndex+1);
				descRows[currentRow] = descRows[currentRow].substring(0, spaceIndex);
				currentRow++;
			}
			g.setColor(Color.WHITE);
			g.setFont(descriptionFont);
			for(int j = 0; j <= currentRow; j++){
				g.drawString(descRows[j], 50, 210 + j*35);
			}
			if(timePassed > 1){
				g.drawString("Click to skip!", 50, 500);
				if(mouseDown){
					intro.stop();
					break;
				}
			}
			s.update();
			Thread.sleep((long)(loopTime*1000));
		}
		nextAction = 4;
		
	}

	public boolean isJumping(){
		return f.isJumping();
	}
	
	public void checkForStalactites(){
		for(int i = 0; i < stalactites.size(); i++){
			Stalactite st = stalactites.get(i);
			if(Math.abs(f.getXPos() - st.getX()) < gridSize && f.getYPos() > st.getY())
				st.fall();
		}
	}
	
	public void showMap() throws InterruptedException{
		
		int fontSize = 12;
		int dotSize = 20;
		int length = levels.size();
		int[] levelX = new int[length];
		int[] levelY = new int[length];
		String[] levelName = new String[length];
		String[] location = new String[length];
	    int[][] time = new int[length][3];
		boolean goOn = false;
		boolean drawBig = false;
		int showIndex = 0;
		
		for(int i = 0; i < length; i++){
			Level l = levels.get(i);
			levelX[i] = l.getMapX();
			levelY[i] = l.getMapY();
			levelName[i] = l.getLevelName();
			location[i] = l.getLocation();
			time[i][0] = l.getHours();
			time[i][1] = l.getMinutes();
			time[i][2] = l.getSeconds();
		}
		while(!goOn && running){
			Graphics2D g = s.getGraphics();
			g.drawImage(mapOfGotland, 0, 0, null);
			int i = 0;
			drawBig = false;
			if(mouseDown && Math.sqrt(Math.pow(MouseInfo.getPointerInfo().getLocation().x - levelX[i], 2) + Math.pow(MouseInfo.getPointerInfo().getLocation().y - levelY[i], 2)) < dotSize){
				running = false;
				goOn = true;
			}
			for(i = 0; i < length; i++){
				g.setColor(Color.RED);
				g.fillOval(levelX[i] - dotSize/2, levelY[i] - dotSize/2, dotSize, dotSize);
				if(Math.sqrt(Math.pow(MouseInfo.getPointerInfo().getLocation().x - levelX[i], 2) + Math.pow(MouseInfo.getPointerInfo().getLocation().y - levelY[i], 2)) < dotSize){
					drawBig = true;
					showIndex = i;
					if(mouseDown){
						goOn = true;
						level = i+1;
						nextAction = 2;
					}
				}
			}
			if(drawBig){
				String sString = "";
				String mString = "";
				String hString = "";

				if(time[showIndex][2] < 10)
					sString = "0";
				if(time[showIndex][1] < 10)
					mString = "0";
				if(time[showIndex][0] < 10)
					hString = "0";
				g.fillOval(levelX[showIndex] - dotSize, levelY[showIndex] - dotSize, 2 * dotSize, 2 * dotSize);
				//Show level info
				g.setColor(Color.DARK_GRAY);
				g.fillRect(levelX[showIndex] + dotSize, levelY[showIndex] + dotSize, 200, 100);
				g.setColor(Color.WHITE);
				g.drawRect(levelX[showIndex] + dotSize - 1, levelY[showIndex] + dotSize - 1, 202, 102);
				g.drawString(levelName[showIndex], levelX[showIndex] + dotSize + 10, levelY[showIndex] + dotSize + 10 + fontSize + 2);
				g.drawString(location[showIndex], levelX[showIndex] + dotSize + 10, levelY[showIndex] + dotSize + 10 + 2*fontSize + 2);
				g.drawString(hString + time[showIndex][0] + ":" + mString + time[showIndex][1] + ":" + sString + time[showIndex][2], levelX[showIndex] + dotSize + 10, levelY[showIndex] + dotSize + 10 + 3*fontSize + 2);
			}

			s.update();
			Thread.sleep((long)(1./fps * 1000));
		}

	 

	}

	public void performNextAction() throws InterruptedException{
		switch(nextAction){
		case 1: showMap(); break;
		case 2: loadLevel(); break;
		case 3: showLevelIntro(); break;
		case 4: gameLoop(); break;
		case 5: showDeadScreen(); break;
		case 6: showFinishScreen(); break;
		}
	}
	
	public void resetVariables(){
		blocks = new ArrayList<Block>();
		bullets = new ArrayList<Bullet>();
		sentryBullets = new ArrayList<SentryBullet>();
		ladders = new ArrayList<Ladder>();
		sheep = new ArrayList<Sheep>();
		superSheep = new ArrayList<SuperSheep>();
		sheepSpawners = new ArrayList<SheepSpawner>();
		water = new ArrayList<Water>();
		cannons = new ArrayList<SheepCannon>();
		stalactites = new ArrayList<Stalactite>();
		sentryGuns = new ArrayList<SentryGun>();
		ladderHit = false;
		time = 0;
		screenX = 0;
		shotsHit = shotsFired = 0;
	}
	
	public void showDeadScreen(){
		int timer = 0;
		intro.stop();
		while(timer < 200){
			Graphics2D g = s.getGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, s.getWidth(), s.getHeight());
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.PLAIN, 40));
			g.drawString("YOU DEAD MADDAFAKKA!", 200, 200);
			s.update();
			timer++;
		}
		
		
		nextAction = 1;
	}
	
	public void showFinishScreen() throws InterruptedException{
		int accuracy = (int)(100 * shotsHit/shotsFired);
		int score = 100 * f.getKills() + accuracy * 10;
		intro.stop();
		dieSheep.play();
		Graphics2D g;
		for(int i = 0; i <= fps; i++){
			g = s.getGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, s.getWidth(), s.getHeight());
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("Level Completed!", 100, 200);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			
			g.drawString("Time:", 100, 300); g.drawString("" + (int)time, 200, 300);
			g.drawString("Health:", 100, 350); g.drawString(f.getHealth() + "%", 200, 350);
			g.drawString("Accuracy:", 100, 400); g.drawString(accuracy + "%", 200, 400);
			g.drawString("Score: " + i*score / fps, 100, 450);
			s.update();
			Thread.sleep(1000/fps);
		}
		
		Thread.sleep(2000);
			
		nextAction = 1;
	}
	
	public void checkFinish(){
		if(finish != null)
		if(f.getXPos() > finish.getX() - gridSize/2 && f.getXPos() < finish.getX() + gridSize/2 && f.getYPos() + 25 > finish.getY() - gridSize/2 && f.getYPos() + 25 < finish.getY() + gridSize/2)
			nextAction = 6;
	}
}
