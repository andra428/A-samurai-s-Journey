package GameState;

import Entity.Player;
import Exceptii.FileNotFound;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Level extends GameState {

    private Background bg;
    Player player;


    private int currentChoice = 0;
    private String[] options = {
            "Level1",
            "Level2",
            "Level3"
    };


    public Level(GameStateManager gsm) {

        super(gsm);

        try {

            bg = new Background("/Background/pod.jpg", 1);
            if(bg==null) {
                throw new FileNotFound("File not found");
            }


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

        for(int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(new Color(128,0,0));
            }
            else {
                g.setColor(new Color(64, 224, 208));
            }
            g.drawString(options[i], 125,140 + i * 15);
        }

    }

    private void select() {
        if(currentChoice == 0) {
            gsm.setState(GameStateManager.LEVEL1STATE);

        }
        if(currentChoice == 1) {
            gsm.setState(GameStateManager.LEVEL2STATE);
        }
        if(currentChoice == 2) {
           gsm.setState(GameStateManager.LEVEL3STATE);

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
