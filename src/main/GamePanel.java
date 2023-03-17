package main;

import GameState.GameStateManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

public class GamePanel extends JPanel implements Runnable, KeyListener{
    //dimensions
    public static final int width = 320;
    public static final int height = 240;
    public static int scale = 2;

    //game thread

    private Thread thread;
    private boolean running;
    private int FPS=60;
    private long targettime=1000/FPS; //milisec

    //image

    private BufferedImage image;
    private Graphics2D g;
    // game state manager
    private GameStateManager gsm;

    public GamePanel(){
        super();
        setPreferredSize(new Dimension(width*scale, height *scale));
        setFocusable(true);
        requestFocus();

    }
    public void addNotify(){
        super.addNotify();
        //game panel is done loading
        if(thread==null){
            thread=new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void init(){
        image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        g=(Graphics2D) image.getGraphics();
        running = true;
        gsm = new GameStateManager();
    }
 public void run(){
        init();

        // GAME LOOP

        long start;
        long elapsed;
        long wait;

        while(running){
            //sleep method
            start = System.nanoTime();
            update();
            draw();
            drawToScreen();
            elapsed = System.nanoTime()- start; //nanosec
            wait = targettime -elapsed/1000000; // conversie
            if(wait<0)
                wait=5;
            try{
                Thread.sleep(wait);
                // pauses the game loop
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void update(){
        gsm.update();
    }
    private void draw(){
        gsm.draw(g);
    }
    private void drawToScreen(){
        Graphics g2 = getGraphics();
        g2.drawImage(image,0,0,width*scale,height*scale,null);
        g2.dispose();

    }

    public void keyTyped(KeyEvent key){}

    public void keyPressed(KeyEvent key){
        gsm.keyPressed(key.getKeyCode());
    }

    public void keyReleased(KeyEvent key){
        gsm.keyReleased(key.getKeyCode());
    }


}
