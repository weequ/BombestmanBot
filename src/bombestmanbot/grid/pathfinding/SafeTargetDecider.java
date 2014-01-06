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
public class SafeTargetDecider implements TargetDecider {

    @Override
    public boolean isTarget(Tile tile) {
        return !tile.isDangerous();
    }
    
}
