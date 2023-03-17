package TileMap;
import Entity.Player;
import main.GamePanel;

import java.awt.*;
import java.awt.image.*;

import java.io.*;
import javax.imageio.ImageIO;
import GameState.Level1State;
public class TileMap  {

    Player player;
    //position
    private double x,y;

    //bounds
    public int xmin,ymin;
    public int xmax,ymax;

    private double tween; // for smoothly following the player

    //map  matrix
    private int[][] map;
    private final int tileSize;
    // the number of rows and cols of the matrix
    private int numRows;
    private int numCols;
    // the dimensions of the map in pixels
    private int  width;
    private int height;


    //tile set

    public BufferedImage tileset;
    private int numTileAcross;
    private Tile[][] tiles;

    //drawing

    private int rowOffset; //which row to start drawing
    private int colOffset; //which col to start drawing
    private final int numRowsToDraw; //how many rows to draws
    private final int numColsToDraw; //how many cols to draw

    public int getNumRows() { return numRows; }
    public int getNumCols() { return numCols; }

    public TileMap(int tileSize){
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.height/tileSize+2; //240/30=8 + 2
        numColsToDraw = GamePanel.width/tileSize+2;
        tween = 0.07;
    }

    public void loadTiles(String s){
        try{
            tileset = ImageIO.read(getClass().getResourceAsStream(s)); // to get resources
            numTileAcross = tileset.getWidth() / tileSize;
            tiles = new Tile[2][numTileAcross]; // matrix with the tiles

            BufferedImage subimage;
            for(int col = 0; col<numTileAcross; col++){
                // going through the matrix with the tiles
                //the tile 0 to 30 are not interactive
                subimage = tileset.getSubimage(col*tileSize,0,tileSize,tileSize);
                tiles[0][col] = new Tile(subimage, Tile.BAD) ;
                subimage= tileset.getSubimage(col*tileSize,tileSize,tileSize,tileSize);
                tiles[1][col] = new Tile(subimage,Tile.BLOCKED);
                subimage= tileset.getSubimage(col*tileSize,tileSize,tileSize,tileSize);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }
    public void loadMap(String s){
        try {
            InputStream in = getClass().getResourceAsStream(s); //to get resources
            BufferedReader br = new BufferedReader(new InputStreamReader(in)); // to read the text file

            numCols = Integer.parseInt(br.readLine()); // read the number of rows
            numRows = Integer.parseInt(br.readLine()); //read the number of cols
            map = new int[numRows][numCols]; //matrix
            width = numCols*tileSize; // width in pixels
            height = numRows*tileSize; // height in pixels

            xmin =GamePanel.width -  width;
            xmax=0;
            ymin=GamePanel.height -height;
            ymax=0;
            String delims = "\\s+";
            // reading the map
            for(int row = 0;row<numRows;row++){
                String line=br.readLine();
                String tokens[] = line.split(delims);
                for(int col=0;col<numCols;col++){
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getTileSize(){
        return tileSize;
    }
    public int getx(){return (int)x;}

    public int gety(){return (int)y;}

    public int getWidth(){ return width;}
    public int getHeight(){return height;}

    public int getType(int row, int col){
        //to check if the tile is normal or blocked
        int rc = map[row][col];
        int r = rc/ numTileAcross;
        int c = rc % numTileAcross;
       return tiles[r][c].getType();
    }

    public void setTween(double d) { tween = d; }

    public void setPosition(double x, double y) {

        //following the player smoothly
        //moving the camera gradually to the next position
        this.x += (x - this.x) * tween;
        this.y += (y - this.y) * tween;

        fixBounds();
        //where to start drawing
        colOffset = (int)-this.x / tileSize; // which row to start drawing
        rowOffset = (int)-this.y / tileSize; // which col to start drawing

    }

    private void fixBounds() {
        //to make sure the bounds aren't being passed
        if(x < xmin) x = xmin;
        if(y < ymin) y = ymin;
        if(x > xmax) x = xmax;
        if(y > ymax) y = ymax;
    }

    public void draw(Graphics2D g) {
        //drawing the tiles that are necessary
        for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {

            if(row >= numRows) break;

            for(int col = colOffset; col < colOffset + numColsToDraw; col++) {

                if(col >= numCols) break;

                if(map[row][col] == 0) continue;

                int rc = map[row][col];
                int r = rc / numTileAcross;
                int c = rc % numTileAcross;
                // draw the tile
                g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);

            }

        }

    }

    private void reset() {
        player.setPosition(100, 300);
    }
}