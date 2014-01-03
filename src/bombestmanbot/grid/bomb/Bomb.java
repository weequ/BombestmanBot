package bombestmanbot.grid.bomb;

import bombestmanbot.Extramath;
import bombestmanbot.grid.Tile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bomb {
    public int age;
    public int dices;
    public int sides;
    public int combinationsLeft;
    public int force;
    private Tile tile;
    
    public Bomb(int force, int dices, int sides, Tile tile) {
        this.tile = tile;
        this.force = force;
        this.dices = dices;
        this.sides = sides;
        age = 0;
        combinationsLeft = initialCombinations();
    }
    
    public Tile getTile() {
        return tile;
    }
    
    public void nextTurn() {
        combinationsLeft-=explosionCombinationsOnTurn(1);
        age++;
    }
    
    public double getCurrentExplosionPropability() {
        try {
            return getExplosionProbabilityOnTurn(1);
        } catch (AlreadyExplodedException ex) {
            Logger.getLogger(Bomb.class.getName()).log(Level.SEVERE, null, ex);
            return 666;
        }
    }
    
    public double getExplosionProbabilityOnTurn(int turn) throws AlreadyExplodedException {
        //if (turn < 1) throw new IllegalArgumentException("turn must be at least 1!");
        return (double)explosionCombinationsOnTurn(turn)/combinationsLeft;
    }
    
    
    private int initialCombinations() {
        return (int) Math.pow(sides, dices);
    }
    
    
    /**
     * For example turn 4 turns after initial turn:
     * OOXOO 1
     * OXOOO 2
     * XOOOO 3
     * OOOOO 4
     * OOOOO 5
     * 12345
     * Return 3 (three X's) because 3^1 = 3
     * 
     * @param turn How many turns from the current turn?
     * @return 
     */
    private int explosionCombinationsOnTurn(int turn) {
        List<Integer> poly = new ArrayList<>();
        for (int i = 0; i < sides; i++) {
            poly.add(1);
        }
        poly = Extramath.pow(poly, dices);
        int index = poly.size()-(this.age+turn);
//        if (index < 0) {
//            throw new AlreadyExplodedException();
//        }
        try {
            return poly.get(poly.size()-(this.age+turn));
        } catch (IndexOutOfBoundsException ex) {
            return 0;
        }
    }
    
    
    /**
     * Gets the explosion tiles. 
     * Including the tiles that would explode from other bombs if this bomb exploded.
     * @return 
     */
    private Set<Tile> getExplosionTiles() {
        Set<Tile> explosionTiles = new HashSet<>();
        String[] directions = {"up", "right", "down", "left"};
        Tile current;
        for (String direction : directions) {
            int steps = 0;
            while((current = tile.getNeighbor(direction)) != null && steps < force) {
                steps++;
                explosionTiles.add(current);
                if (current.getBomb() != null) {
                    explosionTiles.addAll(current.getBomb().getExplosionTiles());
                }
                if (!current.isExploding()) break;
            }        
        }
        return explosionTiles;
    }
    
    
    
    //Kesken
//    private int possibleCombinationsAfterTurns(int turns) {
//         return initialCombinations()-risingfactorial(turns, dices)/factorial(dices);
//    }
    
    
    
//    private static int risingfactorial(int from, int n) {
//        int fac = from;
//        for (int i = 1; i <= n; i++) {
//            fac *= (from+i);
//        } 
//        return fac;
//    }
//    
//    
//    private static int factorial(int n) {
//        int fac = 1;
//        for (int i = 1; i <= n; i++) {
//            fac *= i;
//        } 
//        return fac;
//    }
    
    
    public static void main(String[] args) throws AlreadyExplodedException {
        Bomb b = new Bomb(4, 2, 7, null);
        double d = b.getExplosionProbabilityOnTurn(14);
        System.out.println(d);
    }
    
    
}
