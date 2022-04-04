
public class Water {
	private int x;
	private int y;
	private final double slowDown = 0.8;
	
	public Water(int x, int y){
		this.x = x;
		this.y = y;
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
	
	public double getSlowDown(){
		return slowDown;
	}
}
