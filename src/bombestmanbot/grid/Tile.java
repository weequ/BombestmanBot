package bombestmanbot.grid;

import bombestmanbot.grid.bomb.Bomb;
import java.util.HashSet;
import java.util.Set;

public class Tile {
    public static char TILE_TREASURE = '$';
    public static char TILE_HARDBLOCK = '#';
    public static char TILE_SOFTBLOCK = '?';
    public static char TILE_FLOOR = '.';
    
    
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
        if (dir.equals("up")) {
            return grid.getTile(x, y-1);
        } else if (dir.equals("right")) {
            return grid.getTile(x+1, y);
        } else  if (dir.equals("down")) {
            return grid.getTile(x, y+1);
        } else if (dir.equals("left")) {
            return grid.getTile(x-1, y);
        }
        return null;
    }
    
    public Set<Tile> getNeighbours() {
        Set<Tile> result = new HashSet<>();
        String[] dirs = {"up", "right", "down", "left"};
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
    
}
