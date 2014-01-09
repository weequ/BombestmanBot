/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot.strategy;

import bombestmanbot.BombestmanBot;
import static bombestmanbot.BombestmanBot.safeToBomb;
import bombestmanbot.Communication;
import bombestmanbot.Game;
import bombestmanbot.grid.Tile;
import bombestmanbot.grid.bomb.Bomb;
import bombestmanbot.grid.pathfinding.BasicWeigthDecider;
import bombestmanbot.grid.pathfinding.Dijkstra;
import bombestmanbot.grid.pathfinding.TargetDecider;
import bombestmanbot.grid.pathfinding.WeigthDecider;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author antti
 */
public class ExplodingStrategy implements Strategy {
    

    public void lol() {
        TargetDecider tD = new TargetDecider() {

            @Override
            public boolean isTarget(Tile tile) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
    
    
    
    public Double findBombDroppingSpot(LinkedList<Tile> path) {
        WeigthDecider wD = new BasicWeigthDecider();
        TargetDecider tD = new TargetDecider() {

            @Override
            public boolean isTarget(Tile tile) {
                Tile oldTile = BombestmanBot.myTile;
                BombestmanBot.myTile = tile;
                boolean safeTile = BombestmanBot.safeToBomb();
                BombestmanBot.myTile = oldTile;
                if (!safeTile) return false;
                Bomb testBomb = new Bomb(BombestmanBot.botId, BombestmanBot.bombForce, BombestmanBot.bombTimeDice, BombestmanBot.bombeTimerSides, tile);
                Set<Tile> explosionTiles = testBomb.getExplosionTiles();
                for (Tile explosionTile : explosionTiles) {
                    if (explosionTile.getChar() == Tile.TILE_SOFTBLOCK) {
                        return true;
                    }
                }
                return false;
            }
        };
        Dijkstra dijkstra = new Dijkstra(BombestmanBot.game.getGrid(), BombestmanBot.myTile, tD, wD);
        return dijkstra.findShortestPath(path);
    }
    
    
    
    public boolean threateningEnemies() {
        List<Point> playerCoords = BombestmanBot.game.getPlayerCoords();
        for (Point p : playerCoords) {
            Game game = BombestmanBot.game;
            Bomb testBomb = new Bomb(BombestmanBot.botId, BombestmanBot.bombForce, BombestmanBot.bombTimeDice, BombestmanBot.bombeTimerSides, BombestmanBot.myTile);
            //Simulate escaping as him:
            Tile oldTile = BombestmanBot.myTile;
            BombestmanBot.myTile = game.getGrid().getTile(p);
            boolean canEscape = BombestmanBot.canEscape(testBomb);
            BombestmanBot.myTile = oldTile;
            if (!canEscape) {
                return true;
            }
        }
        return false;
    }
    
    
    @Override
    public String execute() {

        
        System.out.println("using exploding strategy");
        if (safeToBomb()) {
            if (threateningEnemies()) {
                return Communication.COMMAND_BOMB;
            } 
        }
        LinkedList<Tile> path = new LinkedList<>();
        Double length = findBombDroppingSpot(path);
        if (length != null && length < 100) {
            if (path.size() == 0) {
                return Communication.COMMAND_BOMB;
            }
            return BombestmanBot.directionToCommand(BombestmanBot.myTile.getDirection(path.get(0)));
        } else {
            System.out.println(BombestmanBot.game.getBombField().getExplosionTiles().size() + "EXPLODING TILES!!!");
            return Communication.COMMAND_WAIT;
        }
    }
    
}
