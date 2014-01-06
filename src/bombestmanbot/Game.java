/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot;

import bombestmanbot.grid.Grid;
import bombestmanbot.grid.bomb.BombField;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Antti
 */
public class Game {
    private BombField bombField;
    private List<Point> playerCoords;
    private Grid grid;
    
    public Game(int mapWidth, int mapHeigth) {
        this.grid = new Grid(this, mapWidth, mapHeigth);
        this.bombField = new BombField(grid);
        playerCoords = new ArrayList<>(BombestmanBot.bombersCount);
        for (int i = 0; i < BombestmanBot.bombersCount; i++) {
            playerCoords.add(null);//Location is still unkown.
        }
    }
    
    public List<Point> getPlayerCoords() {
        return playerCoords;
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
    
    public Grid getGrid() {
        return grid;
    }
}
