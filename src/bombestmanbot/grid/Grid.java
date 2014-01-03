/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot.grid;

import bombestmanbot.BombestmanBot;
import bombestmanbot.grid.bomb.BombField;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


public class Grid {

    private Tile[][] tiles;
    private BombField bombField;
    private List<Point> playerCoords;
    
    public Grid(int width, int heigth) {
        tiles = new Tile[width][heigth];
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = new Tile(this, x, y, '?');
                tiles[y][x] = tile;
            }
        }
        
        playerCoords = new ArrayList<>(BombestmanBot.bombersCount);
        for (int i = 0; i < BombestmanBot.bombersCount; i++) {
            playerCoords.add(null);//Location is still unkown.
        }
        bombField = new BombField(this);
    }
    
    
    public void setPlayerCoords(int player, Point coords) {
        Point oldCoords = playerCoords.get(player);
        if (oldCoords == null || coords == null) {
            playerCoords.set(player, coords);
        } else {
            oldCoords.setLocation(coords);
        }
    }
  
    
    public BombField getBombField() {
        return bombField;
    }
    
    
    public void update(String[] rows) {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                getTile(x, y).setChar(rows[y].charAt(x));
            }
        }    
    }
    
    
    public Tile getTile(int x, int y) {
        return tiles[y][x];
    } 
    
    
}