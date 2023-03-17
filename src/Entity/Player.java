package Entity;

import TileMap.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject {

    protected static Player instance = null;

    // player stuff
    public int health;
    private int maxHealth;
    private int clues;
    private int maxclues;
    private int score;
    private int enemies=0;


    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    private boolean attacking;
    private boolean teleporting;
    private int attackDamage;
    private int attackRange;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            2, 6, 1, 2, 6
    };

    // animation actions
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int ATTACKING = 4;

    public Player(TileMap tm) {

        super(tm);

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        clues=0;
        maxclues=3;
        attackDamage = 1;
        attackRange = 45;

        // load sprites
        try {

            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/sprites/Player/bun141.png"));

            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 5; i++) {

                BufferedImage[] bi = new BufferedImage[numFrames[i]];

                for(int j = 0; j < numFrames[i]; j++) {
                    if (i == 4) {
                        bi[j] = spritesheet.getSubimage(j *( width + 10), i * height, width+10, height);
                    } else {
                        bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);

                    }
                }

                sprites.add(bi);

            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

    }

    public static Player getInstance(TileMap tm){
        if(instance==null){
            instance=new Player(tm);
        }
        return instance;
    }

    public void resetInstance(){instance=null;}

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public void setHealth(int i){ health=i;}
    public void setAttacking() {
        attacking = true;
    }
    public int getClues(){return clues;}
    public int getMaxClues(){return maxclues;}

    public void setTeleporting(boolean b) { teleporting = b; }

    public void stop() {
        left = right = up = down = flinching = jumping = attacking = false;
    }

    public void setDead() {
        health = 0;
        stop();
    }
    public int getScore(){return score;}
    public void setClues(int i){clues=i;}

    public void hit(int damage) {
        if (flinching) return;
        health -= damage;
        if (health < 0) health = 0;
        if (health == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public void ScoreEnemies(int i){
        enemies=enemies+5;
    }
    public int getEnemies(){return enemies;}
    public void checkAttack(ArrayList<Enemy> enemies) {

        // loop through enemies
        for(int i = 0; i < enemies.size(); i++) {

            Enemy e = enemies.get(i);

            // scratch attack
            if(attacking) {
                if(facingRight) {
                    if(e.getx() > x && e.getx() < x + attackRange && e.gety() > y - height / 2 && e.gety() < y + height / 2
                    ) {
                        e.hit(attackDamage);
                    }
                }
                else {
                    if(e.getx() < x && e.getx() > x - attackRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {
                        e.hit(attackDamage);
                    }
                }
            }


            // check enemy collision
            if(intersects(e)) {
                hit(e.getDamage());
            }

        }

    }

    private void getNextPosition() {

        // movement
        if(left) {
            dx -= moveSpeed;
            if(dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        }
        else if(right) {
            dx += moveSpeed;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        else {
            if(dx > 0) {
                dx -= stopSpeed;
                if(dx < 0) {
                    dx = 0;
                }
            }
            else if(dx < 0) {
                dx += stopSpeed;
                if(dx > 0) {
                    dx = 0;
                }
            }
        }

        // cannot move while attacking, except in air
        if((currentAction == ATTACKING) && !(jumping || falling)) {
            dx = 0;
        }

        // jumping
        if(jumping && !falling) {
            dy = jumpStart;
            falling = true;
        }

        // falling
        if(falling) {

            dy += fallSpeed;

            if(dy > 0) jumping = false;
            if(dy < 0 && !jumping) dy += stopJumpSpeed;

            if(dy > maxFallSpeed) dy = maxFallSpeed;

        }

    }

    public void colectClues(int i){
        if(clues<getMaxClues())
        {
            clues=clues+1;
        }
    }
    public void ScoreClues(int i){
        if(getMaxClues()==3)
        {
            score=score+5;
        }
    }
    public void setScore(int i){
        int s=i;
    }
    public void update() {

        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        //check attack stop
        if(currentAction==ATTACKING){
            if(animation.hasPlayedOnce())
                attacking=false;
        }

        // check done flinching
        if(flinching) {
            long elapsed =
                    (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 1000) {
                flinching = false;
            }
        }

        // set animation
        if(attacking) {
            if(currentAction != ATTACKING) {
                currentAction = ATTACKING;
                animation.setFrames(sprites.get(ATTACKING));
                animation.setDelay(80);
                width = 40;
            }
        }
        else if(dy > 0) {
            if(currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 30;
            }
        }
        else if(dy < 0) {
            if(currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 30;
            }
        }
        else if(left || right) {
            if(currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        }
        else {
            if(currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 30;
            }
        }

        animation.update();

        // set direction
        if(currentAction != ATTACKING) {
            if(right) facingRight = true;
            if(left) facingRight = false;
        }

    }

    public void draw(Graphics2D g) {
        //tells us where to draw the character
        setMapPosition();

        // draw player

        if(facingRight) {
            g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
        }
        else {
            g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
        }
    }
}