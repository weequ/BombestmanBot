/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot.grid.pathfinding;

import bombestmanbot.grid.Tile;


public interface TargetDecider {
    public boolean isTarget(Tile tile);
}
