package Entity;

import main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.Rectangle;

public abstract class MapObject {

    // tile
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;

    // position & vector
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;

    // dimensions
    protected int width;
    protected int height;

    // collision box
    protected int cwidth;
    protected int cheight;

    // collision 4 corners method
    protected int currRow;
    protected int currCol;
    protected double xdest;
    protected double ydest;
    protected double xtemp;
    protected double ytemp;
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;

    // animation
    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight;

    // movement
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;

    // movement attributes
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed;

    // constructor
    public MapObject(TileMap tm) {
        tileMap = tm;
        tileSize = tm.getTileSize();
        animation = new Animation();
        facingRight = true;
    }

    public boolean intersects(MapObject o) {
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }

    public boolean intersects(Rectangle r) {
        return getRectangle().intersects(r);
    }

    public boolean contains(MapObject o) {
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.contains(r2);
    }

    public boolean contains(Rectangle r) {
        return getRectangle().contains(r);
    }

    public Rectangle getRectangle() {
        return new Rectangle((int)x - cwidth, (int)y - cheight, cwidth, cheight);
    }

    public void calculateCorners(double x, double y) {

        // 4 corners method for determining tile collision
        int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize; // - 1 to not step over into the next col
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;

        if(topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols()) {
            topLeft = topRight = bottomLeft = bottomRight = false;
            return;
        }
        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);

        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;

    }

    public void checkTileMapCollision() {
        //this will check if we have run into a blocked tile or a normal one

        currCol = (int)x / tileSize; //what col we are on
        currRow = (int)y / tileSize; //current row

        // destination positions
        xdest = x + dx;
        ydest = y + dy;

        // to keep track of the original x & y
        xtemp = x;
        ytemp = y;

        calculateCorners(x, ydest); // 4 corners method for determining tile collision
        // calculate corners in y direction
        if(dy < 0) {
            // we are going upwards
            //we have to check the top tiles
            if(topLeft || topRight) {
                //we hit something   stop
                // stop moving upwards
                dy = 0;
                ytemp = currRow * tileSize + cheight / 2;
            }
            else {
                //free to keep going up
                ytemp += dy;
            }
        }
        if(dy > 0) {
            // we have landed/hit on a tile
            //we have to check the bottom tiles
            if(bottomLeft || bottomRight) {
                //if we hit something stop
                dy = 0;
                falling = false;
                ytemp = (currRow + 1) * tileSize - cheight / 2; // we have to set the y pos right above the tile where we landed on
            }
            else {
                //keep going
                ytemp += dy;
            }
        }

        calculateCorners(xdest, y);
        //calculate for x
        if(dx < 0) {
            //going left
            if(topLeft || bottomLeft) {
                //if true we hit a blocked tile ,we have to stop moving
                dx = 0;
                xtemp = currCol * tileSize + cwidth / 2; // to set the x pos to just right of the tile we just hit
            }
            else {
                //keep going
                xtemp += dx;
            }
        }
        if(dx > 0) {
            // going right
            if(topRight || bottomRight) {
                //we hit something
                dx = 0;
                xtemp = (currCol + 1) * tileSize - cwidth / 2;
            }
            else {
                xtemp += dx;
            }
        }
        // to check if we run of a cliff
        if(!falling) {

            calculateCorners(x, ydest + 1); // check the ground, make sure we have not fallen
            if(!bottomLeft && !bottomRight) {
                // that means we have fallen
                falling = true;
            }
        }

    }
    public int getx() { return (int)x; }
    public int gety() { return (int)y; }
    public void setPosition(double x, double y) {
        //regular position
        this.x = x;
        this.y = y;
    }
    public void setMapPosition() {
        //tells us where to draw the character
        xmap = tileMap.getx();
        ymap = tileMap.gety();
    }

    public void setLeft(boolean b) { left = b; }
    public void setRight(boolean b) { right = b; }
    public void setUp(boolean b) { up = b; }
    public void setDown(boolean b) { down = b; }
    public void setJumping(boolean b) { jumping = b; }

    public boolean notOnScreen() {
        return x + xmap + width < 0 ||
                x + xmap - width > GamePanel.WIDTH ||
                y + ymap + height < 0 ||
                y + ymap - height > GamePanel.HEIGHT;
    }

    public void draw(java.awt.Graphics2D g) {
        setMapPosition();
        if(facingRight) {
            g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
        }
        else {
            g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
        }

    }
}
