
public class SheepSpawner {
	private int x;
	private int y;
	private int type; // 1: Timed spawner 2: Random spawner
	private double spawnInterval;
	private double spawnTimer = 0;
	private double spawnChance;
	private int dir = 1;
	
	public SheepSpawner(int x, int y, int type){
		this.setX(x);
		this.setY(y);
		this.setType(type);
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getSpawnInterval() {
		return spawnInterval;
	}

	public void setSpawnInterval(double spawnInterval) {
		this.spawnInterval = spawnInterval;
	}

	public double getSpawnChance() {
		return spawnChance;
	}

	public void setSpawnChance(double spawnChance) {
		this.spawnChance = spawnChance;
	}
	
	public boolean check(){
		boolean ret = false;
		if(type == 1){
			spawnTimer += 1./Run.fps;
			if(spawnTimer >= spawnInterval){
				spawnTimer = 0;
				ret = true;
			}
			else 
				ret = false;
		}
		if(type == 2){
			if(Math.random() < spawnChance)
				ret = true;
			else 
				ret = false;
		}
		return ret;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}
	
	
}
