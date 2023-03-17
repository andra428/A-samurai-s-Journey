package GameState;

import Exceptii.DataBaseException;
import Exceptii.FileNotFound;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class RulesState extends GameState{

    private Background bg;
    private Font font;
    private Color color;
    private static ArrayList<Integer> l = new ArrayList<>();
    private int limit = 5;


    public RulesState(GameStateManager gsm){

        super(gsm);

        try {
            bg = new Background("/Background/pod.jpg", 1);
            if(bg==null){
                throw new FileNotFound("File not found");
            }
            color = new Color(250,250,250);
            font = new Font("Arial",Font.BOLD,12);
        }
        catch(FileNotFound e) {
            System.out.println(e.getMessage());
        }

        getdata();

    }

    public void init() {
    }

    public void update() {
        init();
        bg.update();
    }

    public void draw(Graphics2D g) {

        // draw bg
        bg.draw(g);
        // draw title
        g.setColor(color);
        g.setFont(font);
        g.drawString("RULES",140,20);
        g.drawString("Stanga",80,50);
        g.drawString("Dreapta",130,50);
        g.drawString("Sarit",180,50);
        g.drawString("Atac",220,50);
        g.drawString("<-",80,100);
        g.drawString("->",130,100);
        g.drawString("space",180,100);
        g.drawString("R",220,100);

        try{

            for(int i = 0;i<l.size();i++)
            {
                if (i % 4 == 0) {
                    g.drawString("->", 105, 80 + i * 15);
                } else if(i%4==1){
                    g.drawString("<-", 145, 80 + (i - 1) * 15);
                }
                else if(i%4==2){
                    g.drawString("space", 185, 80 + (i - 2) * 15);
                }
                else if(i%4==3){
                    g.drawString("R", 245, 80 + (i - 3) * 15);
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void getdata() {
        Connection c = null;
        Statement s = null;
        String q = "SELECT * FROM score";
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:score6.db");
            c.setAutoCommit(false);
            s = c.createStatement();

            String sql = "INSERT INTO Rules (Dreapta,Stanga,Sarit,Atac) " +
                    "VALUES ('->','<-','space','R')";
            s.executeUpdate(sql);
            s.close();

            c.commit();
            c.close();
        }catch ( Exception e ) {
            System.out.println("rules");
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfullyyyy");
    }

    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ESCAPE){
            l.clear();
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }
    public void keyReleased(int k) {}
}
