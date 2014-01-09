package bombestmanbot.strategy;

import bombestmanbot.BombestmanBot;
import static bombestmanbot.BombestmanBot.directionToCommand;
import bombestmanbot.Communication;
import bombestmanbot.grid.Tile;
import bombestmanbot.grid.pathfinding.CharTargetDecider;
import bombestmanbot.grid.pathfinding.Dijkstra;
import bombestmanbot.grid.pathfinding.TargetDecider;
import bombestmanbot.grid.pathfinding.TreasureWeighthDecider;
import bombestmanbot.grid.pathfinding.WeigthDecider;
import java.util.LinkedList;


public class TreasureStrategy implements Strategy {
    
    
    
    
    @Override
    public String execute() {
        System.out.println("Using treasure strategy");
        TargetDecider tD = new CharTargetDecider(new char[]{Tile.TILE_TREASURE});
        WeigthDecider wD = new TreasureWeighthDecider();
        Dijkstra dijkstra = new Dijkstra(BombestmanBot.game.getGrid(), BombestmanBot.myTile, tD, wD);
        LinkedList<Tile> path = new LinkedList<>();
        Double length = dijkstra.findShortestPath(path);
        if (length == null) {
            return Communication.COMMAND_WAIT;
        } else {
            for (Tile tile : path) {
                if (tile.getChar() == Tile.TILE_SOFTBLOCK) {
                    path = new LinkedList<>();
                    length = BombestmanBot.pathToNearestTileThatCanThreatenTile(tile, path);
                    System.out.println("navigating to nearesttilethatcantreatentile");
                    if (length == null || length >= 100) {
                        if (length == null) {
                            System.out.println("abc");
                        } else {
                            System.out.println("123");
                        }
                        return Communication.COMMAND_WAIT;
                    } else if (length == 0) {
                        return Communication.COMMAND_BOMB;
                    } else {
                        return directionToCommand(BombestmanBot.myTile.getDirection(path.get(0)));
                    }
                    
                }
            }
            
            Tile nextTile = path.get(0);
           
            if (nextTile.getChar() == Tile.TILE_SOFTBLOCK) {
                if (nextTile.isDangerous() || !BombestmanBot.safeToBomb()) {
                    return Communication.COMMAND_WAIT;
                } else {
                    return Communication.COMMAND_BOMB;
                }
            } else {
                return directionToCommand(BombestmanBot.myTile.getDirection(nextTile));
            }
        }
    }
}
