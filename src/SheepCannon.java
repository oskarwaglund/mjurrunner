
public class SheepCannon {
	private int x;
	private int y;
	private double firingRate;
	private int delayCounter;
	private int dir;
	
	public SheepCannon(int x, int y){
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
	public double getFiringRate() {
		return firingRate;
	}
	public void setFiringRate(double firingRate) {
		this.firingRate = firingRate;
	}
	public int getDelayCounter() {
		return delayCounter;
	}
	public void setDelayCounter(int delayCounter) {
		this.delayCounter = delayCounter;
	}
	public int getDir() {
		return dir;
	}
	public void setDir(int dir) {
		this.dir = dir;
	}
	
	public void count(){
		delayCounter++;
	}
	
	public boolean isReadyToFire(){
		if(delayCounter >= firingRate*Run.fps){
			delayCounter = 0;
			return true;
		} else {
			return false;
		}
		
	}
}
