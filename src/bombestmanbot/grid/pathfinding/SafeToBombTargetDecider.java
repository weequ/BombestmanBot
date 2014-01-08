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
public class SafeToBombTargetDecider implements TargetDecider {

    @Override
    public boolean isTarget(Tile tile) {
        boolean result;
        Tile myPos = bombestmanbot.BombestmanBot.myTile;
        bombestmanbot.BombestmanBot.myTile = tile;
        result = bombestmanbot.BombestmanBot.safeToBomb();
        bombestmanbot.BombestmanBot.myTile = myPos;
        return result;
    }
}
