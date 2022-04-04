

public class SentryGun {
	private int x;
	private int y;
	private int angle = 0;
	private int delayCounter = 0;
	
	public SentryGun(int x, int y){
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

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public int getDelayCounter() {
		return delayCounter;
	}

	public void setDelayCounter(int delayCounter) {
		this.delayCounter = delayCounter;
	}
	
	public void update(Frazze f){
		angle = (int)Math.toDegrees(Math.atan((double)(x - f.getXPos())/(f.getYPos() - y)));
		delayCounter++;
		if(delayCounter > 5)
			delayCounter = 0;
	}
	
	public boolean isReadyToFire(){
		return delayCounter == 5;
	}
}
