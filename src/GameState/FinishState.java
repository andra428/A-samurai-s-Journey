package GameState;

import Exceptii.FileNotFound;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class FinishState extends GameState {

    private Background bg;
    private Color titleColor;
    private Font titleFont;

    private int currentChoice = 0;
    private String[] options = {
            "Restart",
            "Main Menu",
            "Quit"
    };

    private Font font;

    public FinishState(GameStateManager gsm) {

        super(gsm);

        try {

            bg = new Background("/Background/f1.png", 1);
            if(bg==null){
                throw new FileNotFound("File not found");
            }
            //bg.setVector(0, 0);
            titleColor = new Color(128,0,0);
            titleFont = new Font("Times New Roman",Font.BOLD,34);

            font = new Font("Arial", Font.BOLD, 18);

        }
        catch(FileNotFound e) {
            System.out.println(e.getMessage());
        }

    }

    public void init() {}

    public void update() {
        bg.update();
    }

    public void draw(Graphics2D g) {

        // draw bg
        bg.draw(g);
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("YOU FINISHED ",30,75);
        //draw menu options
        g.setFont(font);
        // draw menu options
        g.setFont(font);
        for(int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(new Color(0,0,0));
            }
            else {
                g.setColor(new Color(128,0,0));
            }
            g.drawString(options[i], 125,140 + i * 15);
        }

    }

    private void select() {
        if(currentChoice == 0) {
            gsm.setState(GameStateManager.LEVEL);
        }
        if(currentChoice == 1) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
        if(currentChoice == 2) {
            System.exit(0);
        }
    }

    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER){
            select();
        }
        if(k == KeyEvent.VK_UP) {
            currentChoice--;
            if(currentChoice == -1) {
                currentChoice = options.length - 1;
            }
        }
        if(k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if(currentChoice == options.length) {
                currentChoice = 0;
            }
        }
    }
    public void keyReleased(int k) {}

}
