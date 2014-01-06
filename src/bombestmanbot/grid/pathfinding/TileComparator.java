/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot.grid.pathfinding;

import bombestmanbot.grid.Tile;
import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author antti
 */
public class TileComparator implements Comparator<Tile> {

    private final Map<Tile, Double> distances;
   
    
    public TileComparator(Map<Tile, Double> distances) {
        this.distances = distances;
    }
    
    @Override
    public int compare(Tile t, Tile t1) {
        double erotus = distances.get(t)-distances.get(t1);
        if (erotus < 0) {
            return -1;
        } else if (erotus > 0) {
            return 1;
        } else {
            return 0;
        }
    }
    
}
