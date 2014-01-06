/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot.grid.pathfinding;

import bombestmanbot.grid.Tile;

/**
 *
 * @author antti
 */
public class BasicWeigthDecider implements WeigthDecider {

    @Override
    public double getWeigth(Tile tile, double currentDist) {
        if (!tile.isPassable()) {
            return Double.POSITIVE_INFINITY;
        } else {
            if (tile.isDangerous()) {
                return 100;
            } else {
                return 1;
            }
        }
    }
    
}
