/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot.strategy;

import static bombestmanbot.BombestmanBot.directionToCommand;
import static bombestmanbot.BombestmanBot.myTile;
import static bombestmanbot.BombestmanBot.pathToNearestSafe;
import bombestmanbot.Communication;
import bombestmanbot.grid.Tile;
import java.util.LinkedList;

/**
 *
 * @author antti
 */
public class DangerousStrategy implements Strategy {

    @Override
    public String execute() {
        System.out.println("Using dangerous strategy");
        LinkedList<Tile> output = new LinkedList<>();
        Double length = pathToNearestSafe(output);
        if (length == null || output.size() == 0 || output.get(0).equals(myTile)) {
            return Communication.COMMAND_WAIT;
        } else {
            return directionToCommand(myTile.getDirection(output.get(0)));
        }
    }
}
