/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot.grid.bomb;

import bombestmanbot.BombestmanBot;
import bombestmanbot.grid.Grid;
import bombestmanbot.grid.Tile;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Antti
 */
public class BombField {
    
    private List<Bomb> bombs;
    private Map<Bomb, Set<Tile>> explosionTiles;
    private Grid grid;
    
    
    public BombField(Grid grid) {
        this.grid = grid;
        bombs = new ArrayList<>();
    }
    
    

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
    }
    
    //i.e
    //bomb at 0,0 (owned by 0)
    //bomb at 0,9 (owned by 1)
    public void update(List<Point> bombCoords) {
        outerloop:
        for (Point bombCoord : bombCoords) {
            int x = bombCoord.x;
            int y = bombCoord.y;
            for (int i = 0; i < bombs.size(); i++) {
                Tile tile = bombs.get(i).getTile();
                if (tile.getX() == x && tile.getY() == y) {
                    continue outerloop;
                }
            }
            Bomb bomb = new Bomb(BombestmanBot.bombForce, BombestmanBot.bombTimeDice, BombestmanBot.bombeTimerSides, grid.getTile(x, y));
        }
    }
    
    
}
