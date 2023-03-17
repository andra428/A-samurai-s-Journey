package GameState;
import Exceptii.DataBaseException;
import Exceptii.FileNotFound;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;


public class ScoreState extends GameState{

    private Background bg;
    private Font font;
    private Color color;
    private static ArrayList<Integer> l = new ArrayList<>();
    private int limit = 5;


    public ScoreState(GameStateManager gsm){

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
        g.drawString("Score",140,20);
        g.drawString("Points",100,50);
        g.drawString("Lives",150,50);
        g.drawString("Clues",200,50);

        try{
            if(l.size()==0){
                throw new DataBaseException("Date Base null");
            }
            for(int i = 0;i<l.size();i++)
            {
                if (i % 3 == 0) {
                    g.drawString(String.valueOf(l.get(i)), 115, 80 + i * 15);
                } else if(i%3==1){
                    g.drawString(String.valueOf(l.get(i)), 155, 80 + (i - 1) * 15);
                }
                else if(i%3==2){
                    g.drawString(String.valueOf(l.get(i)), 195, 80 + (i - 2) * 15);
                }
            }
        }catch(DataBaseException e){
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

            ResultSet rs = s.executeQuery("SELECT * FROM score");
            ResultSet ord = s.executeQuery(q + " ORDER BY Points DESC");
            while(rs.next() && l.size() < 2*limit)
            {
                int unu = rs.getInt("Points");
                l.add(unu);
                int doi = rs.getInt("Lives");
                l.add(doi);
                int trei=rs.getInt("Clues");
                l.add(trei);

                System.out.println(unu + " "+ doi);
            }
            ord.close();
            rs.close();
            s.close();

            c.commit();
            c.close();
        }catch ( Exception e ) {
            System.out.println("score");
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
