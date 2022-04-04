
import java.awt.*;
import java.awt.event.*;

public class MouseInput extends Run implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener{

	private boolean spaceReady = true;

	//init kallar på superinit;
	public void init(){
		super.init();
		Window w = s.getFullScreenWindow();
		w.addMouseListener(this);
		w.addMouseWheelListener(this);
		w.addKeyListener(this);
		w.addMouseMotionListener(this);
	}

	public synchronized void draw (Graphics2D g){
		Window w = s.getFullScreenWindow();
		g.setColor(w.getBackground());
		g.fillRect(0,0,s.getWidth(), s.getHeight());

	}

	//mouselistener
	public void mousePressed(MouseEvent e){
		mouseDown = true; 

	}
	public void mouseReleased(MouseEvent e){ mouseDown = false; }
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	//mousemotion
	public void mouseDragged(MouseEvent e){}
	public void mouseMoved(MouseEvent e){

	}

	//wheelinterface
	public void mouseWheelMoved(MouseWheelEvent e){

	}

	//keylistener
	public void keyPressed(KeyEvent e){
		int keyCode = e.getKeyCode();
		space = false;
		if(keyCode == KeyEvent.VK_A)
			left = true;
		if(keyCode == KeyEvent.VK_D)
			right = true;
		if(keyCode == KeyEvent.VK_W)
			up = true;
		if(keyCode == KeyEvent.VK_S)
			down = true;
		if(keyCode == KeyEvent.VK_SPACE && spaceReady && !isJumping()){
			spaceReady = false;
			space = true;
		}
			



		if(keyCode == KeyEvent.VK_ESCAPE){
			stop();
		}

		e.consume();
	}




	public void keyReleased(KeyEvent e){

		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_A)
			left = false;
		if(keyCode == KeyEvent.VK_D)
			right = false;
		if(keyCode == KeyEvent.VK_SPACE)
			spaceReady = true;
		if(keyCode == KeyEvent.VK_W){
			up = false;

		}

		if(keyCode == KeyEvent.VK_S)
			down = false;

	}

	public void keyTyped(KeyEvent e){
		e.consume();
	}



}
