package GameState;
import Exceptii.FileNotFound;
import TileMap.Background;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState{
    private Background bg;
    private int currentChoice = 0;
    private final String[] options= {"Start", "Level","Rules","Quit"};
    private Color titleColor;
    private Font titleFont;
    private Font Font;

    public MenuState(GameStateManager gsm){

       super(gsm);
        try{
            bg = new Background("/Background/pod.jpg",1);
            if(bg == null){
                throw new FileNotFound("File not found");
            }

            titleColor = new Color(128,0,0);
            titleFont = new Font("Times New Roman",Font.BOLD,34);
            Font = new Font("Arial",Font.BOLD, 14);
        }
        catch(FileNotFound e){
            System.out.println(e.getMessage());
        }

    }

    public void init(){}
    public void update(){
        //update background
        bg.update();
    }
    public void draw(Graphics2D g){
        //draw background
        bg.draw(g);
        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("A Ronin's Journey ",27,75);
        //draw menu options
        g.setFont(Font);
        for(int i=0;i<options.length;i++){
            if(i==currentChoice){
                g.setColor(new Color(255,255,255));
            }
            else {
                g.setColor(new Color(128,0,0));
            }
            // draws the menu items one after another
            g.drawString(options[i],145,140 + i * 15);
        }
    }
    private void select(){
        if(currentChoice==0){
         //start
            gsm.setState(GameStateManager.LEVEL1STATE);
        }

        if(currentChoice==3){
            //quit
            System.exit(0);
        }
        if(currentChoice==1){
            gsm.setState(GameStateManager.LEVEL);
        }
        if(currentChoice==2){
            gsm.setState(GameStateManager.RULES);
        }
    }
    public void keyPressed(int k){
        if(k== KeyEvent.VK_ENTER){
            select();
        }
        if(k==KeyEvent.VK_UP){
            currentChoice--;
            if(currentChoice==-1){
                currentChoice=options.length-1;
            }
        }
        if(k==KeyEvent.VK_DOWN){
            currentChoice++;
            if(currentChoice==options.length){
                currentChoice=0;
            }
        }
    }
    public void keyReleased(int k){}
}
