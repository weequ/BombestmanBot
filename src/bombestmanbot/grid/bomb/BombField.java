package bombestmanbot.grid.bomb;

import bombestmanbot.BombestmanBot;
import bombestmanbot.grid.Grid;
import bombestmanbot.grid.Tile;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BombField {
    
    private List<Bomb> bombs;
    private Map<Bomb, Set<Tile>> explosionTiles;
    private Grid grid;
    
    
    public BombField(Grid grid) {
        this.grid = grid;
        bombs = new ArrayList<>();
        explosionTiles = new HashMap<>();
    }
    
    
    public void removeBomb(Bomb bomb) {
        bombs.remove(bomb);
        explosionTiles.remove(bomb);
        //recalculate explosion tiles..
        for (Map.Entry<Bomb, Set<Tile>> e : explosionTiles.entrySet()) {
            e.setValue(e.getKey().getExplosionTiles());
        }
    }
    

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
        explosionTiles.put(bomb, bomb.getExplosionTiles());
    }
    
    
    private boolean isBombInCoords(Bomb bomb, Collection<Point> bombCoords) {
        for (Point newCoords : bombCoords) {
            if (bomb.getTile().getX() == newCoords.x && bomb.getTile().getY() == newCoords.y) {
                return true;
            }
        }
        return false;
    }
    
    
    public Bomb getBombAt(Tile tile) {
        for (Bomb bomb : bombs) {
            if (bomb.getTile().equals(tile)) {
                return bomb;
            }
        }
        return null;
    }
    
    //i.e
    //bomb at 0,0 (owned by 0)
    //bomb at 0,9 (owned by 1)
    public void update(Map<Point, Integer> bombCoords) {
        for (Map.Entry<Point, Integer> bombCoord : bombCoords.entrySet()) {
            Bomb oldBomb = getBombAt(grid.getTile(bombCoord.getKey()));
            if (oldBomb != null) {
                oldBomb.nextTurn();
                continue;
            }
            Tile bombTile = grid.getTile(bombCoord.getKey().x, bombCoord.getKey().y);
            Bomb bomb = new Bomb(bombCoord.getValue(), BombestmanBot.bombForce, BombestmanBot.bombTimeDice, BombestmanBot.bombeTimerSides, bombTile);
            addBomb(bomb);
        }
        
        //Remove bombs that have been exploded.
        List<Bomb> removing = new ArrayList<>();
        for (Bomb bomb : bombs) {
            if (!isBombInCoords(bomb, bombCoords.keySet())) removing.add(bomb);
        }
        for (Bomb bomb : removing) {
            removeBomb(bomb);
        }
    }
    
    
    public Set<Tile> getExplosionTiles() {
        Set<Tile> result = new HashSet<>();
        for (Set<Tile> bombExplosion : explosionTiles.values()) {
            result.addAll(bombExplosion);
        }
        return result;
    }
    
    
    public int bombsLeft(int player) {
        int result = BombestmanBot.initialCountOfBombs;
        for (Bomb bomb : bombs) {
            if (bomb.getOwner() == player) {
                result--;
            }
        }
        return result;
    }
    
    
}
