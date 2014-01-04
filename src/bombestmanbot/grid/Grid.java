package bombestmanbot.grid;

import java.util.HashSet;
import java.util.Set;


public class Grid {

    private Tile[][] tiles;
    
    public Grid(int width, int heigth) {
        tiles = new Tile[heigth][width];
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = new Tile(this, x, y, '@');
                tiles[y][x] = tile;
            }
        }
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
    
    
    public Set<Tile> getTreasureTiles() {
        Set<Tile> result = new HashSet<>();
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.isTreasure()) {
                    
                }
            }
        }
        return null;
    }
    
    
}
