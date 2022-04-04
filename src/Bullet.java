import java.awt.MouseInfo;


public class Bullet {
	
	private double xSpeed;
	private double ySpeed;
	private double x;
	private double y;
	private int distance = 0;
	private final int speed = 20;
	private final int range = 1000;
	
	public Bullet(int x, int y, double angle, double screenX){
		double rand = Math.random() * 0.1 - 0.05;
		
		
		this.xSpeed = Math.cos(angle + rand)*speed;
		this.ySpeed = Math.sin(angle + rand)*speed;
		this.x = x;
		this.y = y;
		
			
	}
	
	public boolean fly(){
		this.x += xSpeed;
		this.y += ySpeed;
		distance += speed;
		return (distance > range);
	}
	
	public int getX(){
		return (int)this.x;
	}
	
	public int getY(){
		return (int)this.y;
	}
	
	public boolean collide(Block b){
		return this.x + xSpeed > b.getX() - 25 && this.x + xSpeed < b.getX() + 25 && this.y + ySpeed > b.getY() - 25 && this.y + ySpeed < b.getY() + 25;
	}
	
	

}
