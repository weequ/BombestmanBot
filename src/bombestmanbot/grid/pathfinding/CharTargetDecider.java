package bombestmanbot.grid.pathfinding;

import bombestmanbot.grid.Tile;


public class CharTargetDecider implements TargetDecider {

    private final char[] acceptedChars;
    
    public CharTargetDecider(char[] acceptedChars) {
        this.acceptedChars = acceptedChars;
    }
    
    @Override
    public boolean isTarget(Tile tile) {
        for (char c : acceptedChars) {
            if (tile.getChar() == c) {
                return true;
            }
        }
        return false;
    }
    
}
