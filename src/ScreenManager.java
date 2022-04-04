
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.*;
import javax.swing.*;
import java.awt.Cursor;

public class ScreenManager{

	private GraphicsDevice vc;

	//Ger vc tillgång till datorskärmen
	public ScreenManager(){
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = e.getDefaultScreenDevice();
	}

	//hämtar alla tillgängliga displaymodes som finns i datorns grafikkort
	public DisplayMode[] getComatibleDisplayModes(){
		return vc.getDisplayModes();
	}

	//jämför displaymodes som lagts in i vc och ser om dom mathar
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

	//kollar om två stycken displaymodes matchar
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

	//gör skärmen till helbild
	public void setFullScreen(DisplayMode dm){
		JFrame f =  new JFrame();
		f.setUndecorated(true);
		f.setIgnoreRepaint(true);
		f.setResizable(false);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image cursorImage = tk.getImage("images/aim.png");
		Cursor c = tk.createCustomCursor(cursorImage, new Point(16, 16), "Curse");
		
		
		f.setCursor(c);
		vc.setFullScreenWindow(f); // gör den till helskärm ** tar bort denna om du vill ha en liten skärm**

		if(dm != null && vc.isDisplayChangeSupported()){
			try{
				vc.setDisplayMode(dm);
			}catch(Exception ex){}
		}
		f.createBufferStrategy(2);
	}

	//denna metod kommer sätta grafikobjektet till nedanstående
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

	//retunerar fönstret
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

	// avslutar helskärmen
	public void restoreScreen(){
		Window w = vc.getFullScreenWindow();
		if( w != null){
			w.dispose();
		}
		vc.setFullScreenWindow(null);
	}

	//skapar en bild som är kompatibel med skärmen
	public BufferedImage CreateCompatibleImage (int w, int h, int t){
		Window win = vc.getFullScreenWindow();
		if(win != null){
			GraphicsConfiguration gc = win.getGraphicsConfiguration();
			return gc.createCompatibleImage(w,h,t);
		}
		return null;
	}









}
