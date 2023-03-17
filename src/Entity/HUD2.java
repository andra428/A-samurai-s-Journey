package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class HUD2 {

    private Player player;
    private BufferedImage image;
    private Font font;


    public HUD2(Player p){
        player=p;
        try{
            image=ImageIO.read(getClass().getResourceAsStream("/HUD/hudl2.png"));
            font=new Font("Arial",Font.PLAIN,14);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g){
        g.drawImage(image,0,10,null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
        g.drawString(player.getClues()+ "/"+player.getMaxClues(), 30, 45);
        g.drawString(player.getScore()+"/"+5, 30, 65);
        g.drawString(player.getEnemies()+"",30,85);

    }

}
