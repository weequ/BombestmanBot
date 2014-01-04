package bombestmanbot.grid;

import bombestmanbot.grid.bomb.Bomb;

public class Tile {
    private static char TILE_TREASURE = '$';
    private static char TILE_HARDBLOCK = '#';
    private static char TILE_SOFTBLOCK = '?';
    private static char TILE_FLOOR = '.';
    
    
    private Bomb bomb;
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
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    
    public Grid getGrid() {
        return grid;
    }
    
    
    public Tile getNeighbor(String dir) {
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
    
    public Bomb getBomb() {
        return this.bomb;
    }
    
    
    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }
    
    
    public boolean isExploding() {
        return (c == TILE_FLOOR || c == TILE_TREASURE);
    }
    
    
    public double getExplosionProbability(int turns) {
        return 0;
    }
    
    
    public boolean isTreasure() {
        return (c == TILE_TREASURE);
    }
    
}
