package bombestmanbot.grid;

import bombestmanbot.grid.bomb.Bomb;
import java.util.HashSet;
import java.util.Set;

public class Tile {
    public static final char TILE_TREASURE = '$';
    public static final char TILE_HARDBLOCK = '#';
    public static final char TILE_SOFTBLOCK = '?';
    public static final char TILE_FLOOR = '.';
    
    public static final String DIRECTION_UP = "up";
    public static final String DIRECTION_RIGHT = "right";
    public static final String DIRECTION_DOWN = "down";
    public static final String DIRECTION_LEFT = "left";
    
    private static final String[] dirs = {DIRECTION_UP, DIRECTION_RIGHT, DIRECTION_DOWN, DIRECTION_LEFT};
    
    private int x;
    private int y;
    private Grid grid;
    private char c;
    
    public Tile(Grid grid, int x, int y, char c) {
        setChar(c);
        this.x = x;
        this.y = y;
        this.grid = grid;
    }
    
    public final void setChar(char c) {
        if (c == TILE_TREASURE || c == TILE_HARDBLOCK || c == TILE_SOFTBLOCK || c == TILE_FLOOR) {
            this.c = c;
        } else {
            this.c = TILE_FLOOR;
        }
    }
    
    public char getChar() {
        return this.c;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    
    public Grid getGrid() {
        return grid;
    }
    
    
    public Tile getNeighbour(String dir) {
        if (dir.equals(DIRECTION_UP)) {
            return grid.getTile(x, y-1);
        } else if (dir.equals(DIRECTION_RIGHT)) {
            return grid.getTile(x+1, y);
        } else  if (dir.equals(DIRECTION_DOWN)) {
            return grid.getTile(x, y+1);
        } else if (dir.equals(DIRECTION_LEFT)) {
            return grid.getTile(x-1, y);
        }
        return null;
    }
    
    public Set<Tile> getNeighbours() {
        Set<Tile> result = new HashSet<>();
        for (String dir : dirs) {
            Tile neighbourTile = getNeighbour(dir);
            if (neighbourTile != null) {
                result.add(neighbourTile);
            }
        }
        return result;
    }
    
    
    public Bomb getBomb() {
        return grid.getGame().getBombField().getBombAt(this);
    }
    
    
    public boolean isPassable() {
        return (c == TILE_FLOOR || c == TILE_TREASURE);
    }
  
    
    
    public double getExplosionProbability(int turns) {
        return 0;
    }
    
    
    public boolean isTreasure() {
        return (c == TILE_TREASURE);
    }
    
    public boolean isDangerous() {
        return (grid.getGame().getBombField().getExplosionTiles().contains(this));
    }
    
    public String getDirection(Tile neighbour) {
        for (String dir : dirs) {
            if (getNeighbour(dir).equals(neighbour)) return dir;
        }
        return null;
    }
    
}
