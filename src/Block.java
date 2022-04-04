
public class Block {
	private int x;
	private int y;
	private int type;
	private final int size = 50;
	
	
	public Block(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int size(){
		return size;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	public void setType(int type){
		this.type = type;
	}
	public int getType(){
		return type;
	}
}
