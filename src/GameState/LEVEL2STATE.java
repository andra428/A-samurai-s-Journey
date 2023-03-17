package GameState;

import Entity.*;
import TileMap.TileMap;
import TileMap.Background;
import main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class LEVEL2STATE extends GameState{

    private TileMap tilemap;
    private Background bg;

    private Player player;

    private ArrayList<Clue> clues;
    private ArrayList<Trap2> traps;
    private ArrayList<Enemy> enemies;

    private HUD2 hud;

    private Teleport teleport;
    private ArrayList<Enemy> mugens;


    // events
    private boolean eventFinish;
    private boolean eventDead;
    private boolean blockinput = false;
    //constructor
    public LEVEL2STATE(GameStateManager gsm){
      super(gsm);
        init();
    }
    public void init(){

        tilemap=new TileMap(30);
        tilemap.loadTiles("/Tiles/bun31.png");
        tilemap.loadMap("/Map/map17.map");
        tilemap.setPosition(0,0);
        tilemap.setTween(1);
        bg=new Background("/Background/bambo.jpg",0.1); // 0.1 tilemap speed

        player = player.getInstance(tilemap);
        player.setPosition(100, 300);
        player.setHealth(PlayerSave.getHealth());
        hud=new HUD2(player);

        populateClues();
        populateTraps();
        populateEnemies();


       teleport = new Teleport(tilemap);

       teleport.setPosition(4350,370);

    }

    public void update(){
       // eventFinish();
        if(blockinput == true)
            return;

        if(teleport.contains(player)) {
            eventFinish = blockinput = true;
        }

        if(player.gety() > tilemap.getHeight()) {
            reset();
            player.hit(1);
        }

        if(player.getHealth() == 0) {
            player.setDead();
            eventDead = blockinput = true;
        }

        // update player
        player.update();


        tilemap.setPosition(GamePanel.width / 2 - player.getx(), GamePanel.height / 2 - player.gety());

        if(eventDead) eventDead();
        if(eventFinish) {
            eventScore();
            eventFinish();
        }


        //update clues
        for(int i=0;i<clues.size();i++)
        {
            if(player.intersects(clues.get(i)) && clues.get(i).shouldRemove()!=true)
            {
                clues.get(i).Remove();
                player.colectClues(1);
                if(player.getClues()==player.getMaxClues()) {
                    player.ScoreClues(1);
                    player.hit(-1);
                }
            }
        }
        //update traps
        for(int i=0;i<traps.size();i++)
        {
            if(player.intersects(traps.get(i)) && traps.get(i).shouldRemove()!=true)
            {
                traps.get(i).Remove();
                player.hit(1);
            }
        }
        // attack enemies
        player.checkAttack(enemies);
        //update enemies
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if (e.isDead()) {
                enemies.remove(i);
                player.ScoreEnemies(1);
                i--;
            }
        }

        teleport.update();

    }
    private void populateClues() {

        clues = new ArrayList<Clue>();

        Clue s;
        Point[] points = new Point[] {
                new Point(259, 320),
                new Point(540,250),
                new Point(850, 300)
        };
        for(int i = 0; i < points.length; i++) {
            s = new Clue(tilemap);
            s.setPosition(points[i].x, points[i].y);
            clues.add(s);
        }

    }

    private void populateTraps(){
        traps=new ArrayList<Trap2>();
        Trap2 t;
        Point[] points = new Point[]{
                new Point(199,360),
                new Point(578,238),
                new Point(737,255),
                new Point(935,340),
                new Point(1245,310),
                new Point(1733,380),
                new Point(2540,255),
                new Point(3495,350)
        };
        for(int i = 0; i < points.length; i++) {
            t = new Trap2(tilemap);
            t.setPosition(points[i].x, points[i].y);
            traps.add(t);
        }

    }

    private void populateEnemies() {

        enemies = new ArrayList<Enemy>();

        Mukuro s;
        Point[] points = new Point[] {
             new Point(460,289),
             // new Point(1290, 280),
                new Point(1590,325),
                new Point(4150, 290)
        };
        for(int i = 0; i < points.length; i++) {
            s = new Mukuro(tilemap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }

    }

    public void draw(Graphics2D g){

        // draw background
        bg.draw(g);

        // draw tilemap
        tilemap.draw(g);


        for(int i=0;i<clues.size();i++)
        {
            if(clues.get(i).shouldRemove()==false)
            {
                clues.get(i).draw(g);
            }
        }
        for(int i=0;i<traps.size();i++)
        {
            if(traps.get(i).shouldRemove()==false)
            {
                traps.get(i).draw(g);
            }
        }

        // draw player
        player.draw(g);

        for(int i = 0; i < enemies.size(); i++) {
                enemies.get(i).draw(g);
        }


        //draw hud
        hud.draw(g);

        //draw teleport

       teleport.draw(g);

    }

    public void keyPressed(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setUp(true);
        if(k == KeyEvent.VK_DOWN) player.setDown(true);
        if(k == KeyEvent.VK_SPACE) player.setJumping(true);
        if(k == KeyEvent.VK_R) player.setAttacking();
        if(k == KeyEvent.VK_ESCAPE) {
            player.resetInstance();
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_SPACE) player.setJumping(false);
    }

    // reset level
    private void reset() {
        player.setPosition(100, 300);
    }

    // player has died
    private void eventDead() {
        if(player.getHealth()==0)
           // gsm.setState(GameStateManager.MENUSTATE);
            player.resetInstance();
            gsm.setState(GameStateManager.GAMEOVER);
    }

    private void eventFinish(){
        player.setTeleporting(true);
        player.stop();
        PlayerSave.setHealth(5);
        player.resetInstance();
        gsm.setState(GameStateManager.LEVEL3STATE);
    }
    private void eventScore(){
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:score6.db");
            c.setAutoCommit(false);

            int health = player.getHealth();
            int score = player.getEnemies();
            int clues = player.getClues();

            String sql = "INSERT INTO Level2 (Points,Lives,Clues) " +
                    "VALUES (?,?,?)";

            PreparedStatement update = c.prepareStatement(sql);
            update.setInt(1, score);
            update.setInt(2, health);
            update.setInt(3,clues);

            update.executeUpdate();
            update.close();

            c.commit();
            c.close();
        } catch (Exception e) {
            System.out.println("level2");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");

      //  gsm.setState(GameStateManager.SCORESTATE);

    }
}
