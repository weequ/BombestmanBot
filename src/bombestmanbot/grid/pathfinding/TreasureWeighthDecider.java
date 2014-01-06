package bombestmanbot.grid.pathfinding;

import bombestmanbot.grid.Tile;

public class TreasureWeighthDecider implements WeigthDecider {

    @Override
    public double getWeigth(Tile tile, double currentDist) {
        if (tile.isPassable()) {
            if (tile.isDangerous()) {
                return Double.POSITIVE_INFINITY;
            } else {
                return 1;
            }
        } else if (tile.getChar() == Tile.TILE_SOFTBLOCK) {
            return 20;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }
    
}
