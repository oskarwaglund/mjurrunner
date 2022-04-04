
public class Stalactite {
	
	private int x;
	private int y;
	private int ySpeed = 0;
	private final int gravity = 1;
	private boolean falling = false;
	
	public Stalactite(int x, int y){
		this.setX(x);
		this.setY(y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void fall(){
		falling = true;
	}
	
	public boolean isFalling(){
		return falling;
	}
	
	public void move(){
		ySpeed += gravity;
		y += ySpeed;
	}
	
	public int getYSpeed(){
		return ySpeed;
	}
	
}
