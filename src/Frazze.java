import java.awt.MouseInfo;
import java.util.ArrayList;


public class Frazze {
	
	private double x;
	private int y;
	private double xSpeed = 0; 
	private int ySpeed = 0;
	private final int gravity = 2;
	private final int jumpSpeed = 22;
	private final int maxSpeed = 7;
	private final int accSpeed = 1;
	private boolean jumping = true;
	private boolean climbing = false;
	private int killCount = 0;
	private double stepCount = 0;
	private double climbCount = 0;
	private int groundLevel;
	private int health = 100;
	private boolean invincible = false;
	private int invincibleTimer = 0;
	private final int dist = 50;
	private final int healthDrop = 5;
	private final double friction = 0.9;
	private boolean standing = false;
	private boolean dead = false;
	
	
	public Frazze(int x, int y){
		this.x = x;
		this.y = this.groundLevel = y;
	}
	
	public void move(){
		this.x += xSpeed;
		
		if(jumping){
			this.ySpeed += gravity;
			if(this.y > 2000)
				dead = true;
		}
		this.y += ySpeed;
		
	}
	
	public void accelerate(int dir){
		xSpeed += dir * accSpeed;
		if(xSpeed > maxSpeed)
			xSpeed = maxSpeed;
		else if(xSpeed < -maxSpeed)
			xSpeed = -maxSpeed;
		if(climbing){
			climbCount += 0.25;
			if(climbCount >= 2)
				climbCount = 0;
		}
	}
	
	public void step(){
		stepCount += 0.2;
		if(stepCount >= 5 || stepCount < 1)
			stepCount = 1;
	}
	
	public void slide(){
		if(!jumping){
			if(climbing)
				this.xSpeed *= 0.8;
			else
				this.xSpeed *= friction;
		}
		stepCount = 0;
		
	}
	
	public void jump(){
		this.jumping = true;
		this.climbing = false;
		this.ySpeed = -jumpSpeed;
		stepCount = 0;
		
	}
	
	public double fire(int mouseX, int mouseY){
		return (Math.atan((mouseY-this.y)/(mouseX - this.x)));
		
		
	}
	
	public int getXPos(){
		return (int)this.x;
	}
	
	public int getYPos(){
		return (int)this.y;
	}
	
	public void setY(int y){
		this.y = y;
	}
	public double getXSpeed(){
		return xSpeed;
	}
	
	public int getYSpeed(){
		return ySpeed;
	}
	
	public int getStepCount(){
		return (int)this.stepCount;
	}
	
	public int getClimbCount(){
			return (int)this.climbCount;
	}
	
	public void increaseKillCount(){
		killCount++;
	}
	
	public int getKills(){
		return killCount;
	}
	
	public boolean isJumping(){
		return jumping;
	}
	
	public boolean collide(Sheep s){
		boolean temp = (Math.sqrt(Math.pow(this.x - s.getXPos(), 2) + (Math.pow(this.y - s.getYPos(), 2))) < dist);
		if(temp && health > 0){
			invincible = true;
			jumping = true;
			ySpeed = -10;
		} 
		return temp;
	}
	
	public void handleInvincibility(){
		if(invincible){
			invincibleTimer++;
			if(invincibleTimer > 25){
				invincible = false;
				invincibleTimer = 0;
			}
		}
	}
	
	public boolean getInvincibility(){
		return invincible;
	}
	
	public int getHealth(){
		return health;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public void dropHealth(int drop){
		health -= drop;
		if(health <= 0){
			dead = true;		
			health = 0;
		}
	}
	
	public boolean checkLadder(Ladder l){
		if(l != null){
			if(this.x > l.getX() - 25 && this.x < l.getX() + 25 && this.y > l.getY() - 25 && this.y < l.getY() + 25){

				return true;

			}
			else {

				return false;


			} 	
		}else return false;

	}
	
	public void climb(int dir){
		y += 5*dir;
		climbCount += 0.25;
		if(climbCount >= 2)
			climbCount = 0;
	}
	
	public boolean isClimbing(){
		return climbing;
	}
	
	public void setClimbing(boolean flag){
		climbing = flag;
		
	}
	public void setJumping(boolean flag){
		jumping = flag;
		
	}
	
	public void setYSpeed(int s){
		ySpeed = s;
	}
	
	public void setXSpeed(int s){
		xSpeed = s;
	}
	
	public boolean isStanding(){
		return standing;
	}
	
	public void setStanding(boolean flag){
		standing = flag;
	}
	
	public boolean collide(Block b, int x, int y){
		return this.x + x > b.getX() - 25 && this.x + x < b.getX() + 25 && this.y + y > b.getY() - 25 && this.y + y < b.getY() + 25; 
	}
	
	public boolean collide(Stalactite s){
		return this.x + 25 > s.getX() && this.x - 25 < s.getX() && this.y + 50 > s.getY() && this.y - 50 < s.getY(); 
	}
	
	public boolean collide(SentryBullet b){
		return x + 25 > b.getX() && x - 25 < b.getX() && y + 50 > b.getY() && y - 50 < b.getY();
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public int getAngle(int mouseX, int mouseY, int screenX){
		if(mouseX >= this.x)
		return (int)(90 + Math.toDegrees(Math.atan((mouseY-this.y)/(mouseX - this.x)))) / 2;
		else {
			
		
		return (int)(90 + Math.toDegrees(Math.atan((mouseY-this.y)/(-(mouseX - this.x))))) / 2;
		}
	}
	
	public void checkWaterSlowDown(ArrayList<Water> water){
		
		for(int i = 0; i < water.size(); i++){
			Water w = water.get(i);
			if (y + 40 > w.getY()-25 && y + 40 < w.getY()+25 && x > w.getX() -25 && x < w.getX()+25){
				xSpeed *= w.getSlowDown();
			}
		}
		
	}
}
