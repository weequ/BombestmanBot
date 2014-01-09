/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot.strategy;

import bombestmanbot.BombestmanBot;
import static bombestmanbot.BombestmanBot.botId;
import static bombestmanbot.BombestmanBot.directionToCommand;
import static bombestmanbot.BombestmanBot.game;
import static bombestmanbot.BombestmanBot.myTile;
import bombestmanbot.Communication;
import bombestmanbot.grid.Tile;
import bombestmanbot.grid.pathfinding.BasicWeigthDecider;
import bombestmanbot.grid.pathfinding.Dijkstra;
import bombestmanbot.grid.pathfinding.TargetDecider;
import bombestmanbot.grid.pathfinding.WeigthDecider;
import java.util.LinkedList;
import java.util.Set;

/**
 * Always call shouldExecute before calling execute!!!
 * @author antti
 */
public class LootinStrategy implements Strategy {
    
    private String command = null;
    
    
    
    public boolean shouldExecute() {
        final Set<Tile> tiles = BombestmanBot.game.getGrid().getExplodingSoftBlocks();
        WeigthDecider wD = new BasicWeigthDecider();
        TargetDecider tD = new TargetDecider() {

            @Override
            public boolean isTarget(Tile tile) {
                for (Tile targetTile : tiles) {
                    if (targetTile.getNeighbours().contains(tile)) {
                        return true;
                    }
                }
                return false;
            }
        };
        Dijkstra dijkstra = new Dijkstra(BombestmanBot.game.getGrid(), BombestmanBot.myTile, tD, wD);
        LinkedList<Tile> path = new LinkedList<>();
        Double length = dijkstra.findShortestPath(path);
        if (length == null|| path.size() > 7) {
            System.out.println("2 ("+path.size()+")");
            command = null;
            return false;
        } else if (path.size() == 0 || path.get(0).isDangerous()) {
            command = Communication.COMMAND_WAIT;
            return true;
        } else {
            command = directionToCommand(myTile.getDirection(path.get(0)));
            return true;
        }
        
    }
    
    
    @Override
    public String execute() {
        System.out.println("using looting strategy");
        if (command == null) throw new RuntimeException("Only call execute when shouldExecute returns true...");
        String result = command;
        command = null;
        return result;
    }
}
