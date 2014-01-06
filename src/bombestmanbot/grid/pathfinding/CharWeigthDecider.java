package bombestmanbot.grid.pathfinding;

import bombestmanbot.grid.Tile;
import java.util.Map;


public class CharWeigthDecider implements WeigthDecider {
    private final Map<Character, Double> weigths;


    public CharWeigthDecider(Map<Character, Double> weigths) {
        this.weigths = weigths;
    }
    
    @Override
    public double getWeigth(Tile tile) {
        if (tile.isDangerous()) return 1000;
        return weigths.get(tile.getChar());
    }
    
}
