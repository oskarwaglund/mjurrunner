
public class Level {
	
	private int[][] objects;
	private double[][] extraObjectValues;
	private int[][] dirValues;
	private int height;
	private int width;
	private int startX = 1;
	private int startY = 1;
	private int finishX;
	private int finishY;
	private String levelName = "A level";
	private String location = "Somewhere in Gotland";
	private int hours = 0, minutes = 0, seconds = 0;
	private String description = "Do this and that!";
	private int mapX;
	private int mapY;
	private int parTime = 300;
	
	public Level(int xSize, int ySize){
		height = ySize;
		width = xSize;
		objects = new int[xSize][ySize];
		extraObjectValues = new double[xSize][ySize];
		dirValues = new int[xSize][ySize];
	}
	
	public void insert(int x, int y, int object){
		objects[x-1][y-1] = object;
	}
	
	public int get(int x, int y){
		return objects[x-1][y-1];
	}
	
	public void insertRect(int x, int y, int width, int height){
		for(int i = x; i < x + width; i++){
			for(int j = y; j < y + height; j++){
				insert(i, j, 1);
				
			}
		}
	}
	
	public void insertLadder(int x, int yBase, int height){
		for(int i = yBase; i > yBase - height; i--){
			insert(x, i, 2);
		}
		
	}
	
	public void insertTimedSheepSpawner(int x, int y, double spawnInterval, int dir){
		insert(x, y, 4);
		extraObjectValues[x-1][y-1] = spawnInterval;
		dirValues[x-1][y-1] = dir;
	}
	
	public void insertRandomSheepSpawner(int x, int y, double spawnChance, int dir){
		insert(x, y, 5);
		extraObjectValues[x-1][y-1] = spawnChance;
		dirValues[x-1][y-1] = dir;
	}
	
	public void insertSheepCannon(int x, int y, double fireInterval, int dir){
		insert(x, y, 7);
		extraObjectValues[x-1][y-1] = fireInterval;
		dirValues[x-1][y-1] = dir;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setStartPoint(int x, int y){
		this.startX = x;
		this.startY = y;
	}
	
	public int getStartX(){
		return startX;
	}
	
	public int getStartY(){
		return startY;
	}	
	
	public double getObjectValue(int x, int y){
		return extraObjectValues[x-1][y-1];
	}
	
	public int getDirValue(int x, int y){
		return dirValues[x-1][y-1];
	}

	public int getMapX() {
		return mapX;
	}

	public int getMapY() {
		return mapY;
	}

	public void setMapXY(int mapX, int mapY) {
		this.mapX = mapX;
		this.mapY = mapY;
	}

	public int getFinishX() {
		return finishX;
	}

	public void setFinishX(int finishX) {
		this.finishX = finishX;
	}

	public int getFinishY() {
		return finishY;
	}

	public void setFinishY(int finishY) {
		this.finishY = finishY;
	}

	public int getParTime() {
		return parTime;
	}

	public void setParTime(int parTime) {
		this.parTime = parTime;
	}
}
