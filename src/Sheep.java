
public class Sheep {
	
	private int x;
	private int y;
	private int health = 100;
	protected int speed = 5;
	private int dir = -1;
	private int groundLevel;
	private final int dist = 28;
	private final int healthDrop = 10;
	public boolean hit;
	private boolean falling = false;
	private int ySpeed = 0;
	private final int gravity = 1;
	
	public Sheep(int x, int y){
		this.x = x;
		this.y = this.groundLevel = y;
	}
	
	public void move(){
		this.x += dir*speed;
		if(!falling){
			this.y = groundLevel + (int)(Math.random()*6) - 3;
			
		}
		else { 
			ySpeed += gravity;
			this.y += ySpeed;
		}
	}
	
	public int getXPos(){
		return this.x;
	}
	
	public int getYPos(){
		return this.y;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public boolean collide(Bullet b){
		boolean temp = (Math.sqrt(Math.pow(this.x - b.getX(), 2) + (Math.pow(this.y - b.getY(), 2))) < dist);
		if(temp){
			health -= healthDrop;
			hit = true;
		}
		return temp;
	}
	
	public boolean isHit(){
		return hit;
	}

	public void setHit(){
		hit = false;
	}
	
	public boolean isFalling(){
		return falling;
	}
	
	public void setFalling(boolean flag){
		falling = flag;
	}

	public boolean collide(Block b){
		if(dir < 0 && this.x - 28 < b.getX() + b.size() / 2 && this.x - 28 > b.getX() && this.y + 14 < b.getY() - 5 + b.size() / 2 && this.y + 14 > b.getY() + 5 - b.size() / 2){
			dir = 1;
			return true;
		}
		else if(dir > 0 && this.x + 28 > b.getX() - b.size() / 2 && this.x + 28 < b.getX() && this.y + 14 < b.getY() + b.size() / 2 && this.y + 14 > b.getY() - b.size() / 2){
			dir = -1;
			return true;
		} else return false;

	}
	
	public boolean checkFloor(Block b){
		return ((this.x - 4 < b.getX() + b.size() / 2 && this.x - 4 > b.getX() - b.size() / 2 || this.x + 4 < b.getX() + b.size() / 2 && this.x + 4 > b.getX() - b.size() / 2) && this.y + 28 > b.getY() - b.size() / 2 && this.y + 28 < b.getY() + b.size() / 2);
	}

	public void setGroundLevel(int y){
		groundLevel = y;
	}

	public void setYSpeed(int yS){
		ySpeed = yS;
	}
	
	public void setXSpeed(int xS){
		 speed = xS;
	}
	
	public int getDir(){
		return dir;
	}
	
	public void setDirection(int dir){
		this.dir = dir;
	}

}
