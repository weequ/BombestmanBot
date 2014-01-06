package bombestmanbot.grid.pathfinding;

import bombestmanbot.grid.Tile;


public interface WeigthDecider {
    public double getWeigth(Tile tile, double currentDist);
}
