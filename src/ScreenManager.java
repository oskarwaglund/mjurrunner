
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.*;
import javax.swing.*;
import java.awt.Cursor;

public class ScreenManager{

	private GraphicsDevice vc;

	//Ger vc tillg�ng till datorsk�rmen
	public ScreenManager(){
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = e.getDefaultScreenDevice();
	}

	//h�mtar alla tillg�ngliga displaymodes som finns i datorns grafikkort
	public DisplayMode[] getComatibleDisplayModes(){
		return vc.getDisplayModes();
	}

	//j�mf�r displaymodes som lagts in i vc och ser om dom mathar
	public DisplayMode findFirstCompatibleMode(DisplayMode modes []){
		DisplayMode goodModes[] = vc.getDisplayModes();
		for(int i = 0; i<modes.length;i++){
			for(int j = 0; j<goodModes.length;j++){
				if(displayModesMatch(modes[i] , goodModes[j])){
					return modes[i];
				}
			}
		}
		return null;
	}

	//tar fram den nuvarande displaymoden
	public DisplayMode getCurrentDisplayMode(){
		return vc.getDisplayMode();
	}

	//kollar om tv� stycken displaymodes matchar
	public boolean displayModesMatch(DisplayMode m1, DisplayMode m2){
		if(m1.getWidth() != m2.getWidth() || m1.getHeight() != m2.getHeight()){
			return false;
		}
		if(m1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m1.getBitDepth() != m2.getBitDepth()){
			return false; 
		}
		if(m1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m1.getRefreshRate() != m2.getRefreshRate()){
			return false;
		}

		return true;
	}

	//g�r sk�rmen till helbild
	public void setFullScreen(DisplayMode dm){
		JFrame f =  new JFrame();
		f.setUndecorated(true);
		f.setIgnoreRepaint(true);
		f.setResizable(false);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image cursorImage = tk.getImage("images/aim.png");
		Cursor c = tk.createCustomCursor(cursorImage, new Point(16, 16), "Curse");
		
		
		f.setCursor(c);
		vc.setFullScreenWindow(f); // g�r den till helsk�rm ** tar bort denna om du vill ha en liten sk�rm**

		if(dm != null && vc.isDisplayChangeSupported()){
			try{
				vc.setDisplayMode(dm);
			}catch(Exception ex){}
		}
		f.createBufferStrategy(2);
	}

	//denna metod kommer s�tta grafikobjektet till nedanst�ende
	public Graphics2D getGraphics(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			BufferStrategy s = w.getBufferStrategy();
			return (Graphics2D)s.getDrawGraphics();
		}else{
			return null;
		}
	}
	//updaterar displayen
	public void update(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			BufferStrategy s = w.getBufferStrategy();
			if(!s.contentsLost()){
				s.show();
			}
		}
	}

	//retunerar f�nstret
	public Window getFullScreenWindow(){
		return vc.getFullScreenWindow();
	}

	public int getWidth(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			return w.getWidth();
		}
		else{
			return 0;
		}
	}

	public int getHeight(){
		Window w = vc.getFullScreenWindow();
		if(w!=null){
			return w.getHeight();
		}
		else{
			return 0;
		}
	}

	// avslutar helsk�rmen
	public void restoreScreen(){
		Window w = vc.getFullScreenWindow();
		if( w != null){
			w.dispose();
		}
		vc.setFullScreenWindow(null);
	}

	//skapar en bild som �r kompatibel med sk�rmen
	public BufferedImage CreateCompatibleImage (int w, int h, int t){
		Window win = vc.getFullScreenWindow();
		if(win != null){
			GraphicsConfiguration gc = win.getGraphicsConfiguration();
			return gc.createCompatibleImage(w,h,t);
		}
		return null;
	}









}
