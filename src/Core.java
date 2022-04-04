
import java.awt.*;
import javax.swing.*;

public abstract class Core{
  
  private static DisplayMode modes[] = {
    new DisplayMode(800,600,32,0),
    new DisplayMode(800,600,24,0),
    new DisplayMode(800,600,16,0),
    new DisplayMode(800,480,32,0),
    new DisplayMode(800,480,24,0),
    new DisplayMode(800,480,16,0),
  };
  private boolean running;
  protected ScreenManager s;
  
  //stop-metod
  public void stop(){
   running = false; 
  }
  
  //call init and gameloop
  public void run(){
    try{
      init();
      gameLoop();
    }finally{
      s.restoreScreen();
    }
  }
  
  //startar helskärmen
  public void init(){
    s = new ScreenManager();
    DisplayMode dm = s.findFirstCompatibleMode(modes);
    s.setFullScreen(dm);
    
    Window w = s.getFullScreenWindow();
    w.setFont(new Font("Arial", Font.PLAIN,20));
    w.setBackground(Color.RED);
    w.setForeground(Color.BLACK);
    running = true;
 }
  
//main game loop
public void gameLoop(){
  long startTime = System.currentTimeMillis();
  long cumTime = startTime;
  
  while(running){
    long timePassed = System.currentTimeMillis() - cumTime;
    cumTime += timePassed;
    
    update(timePassed);
    Graphics2D g = s.getGraphics();
    draw(g);
    g.dispose();
    s.update();
    
    try{
     Thread.sleep(20); 
    }catch(Exception ex){}
  }
}

//update animation
public void update(long timePassed){
}

//draws to the screen
public void draw(Graphics2D g){
}



}