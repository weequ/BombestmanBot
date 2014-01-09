package bombestmanbot.grid;

import bombestmanbot.Game;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;


public class Grid {

    private Tile[][] tiles;
    private Game game;
    
    public Grid(Game game, int width, int heigth) {
        this.game = game;
        tiles = new Tile[heigth][width];
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = new Tile(this, x, y, '@');
                tiles[y][x] = tile;
            }
        }
    }
    
    
    public Game getGame() {
        return game;
    }
    
    
    public void update(String[] rows) {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                getTile(x, y).setChar(rows[y].charAt(x));
            }
        }    
    }
    
    
    public Tile getTile(int x, int y) {
        try {
            return tiles[y][x];
        } catch(ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    } 
    
    
    public Tile getTile(Point coords) {
        return getTile(coords.x, coords.y);
    }
    
    
    public Set<Tile> getTreasureTiles() {
        Set<Tile> result = new HashSet<>();
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.isTreasure()) {
                    result.add(tile);
                }
            }
        }
        return result;
    }
    
    public Set<Tile> getExplodingSoftBlocks() {
        Set<Tile> explosionTiles = game.getBombField().getExplosionTiles();
        Set<Tile> result = new HashSet<>();
        for (Tile tile : explosionTiles) {
            if (tile.getChar() == Tile.TILE_SOFTBLOCK) {
                result.add(tile);
            }
        }
        return result;
    }
    
    
}
