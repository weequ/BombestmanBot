package bombestmanbot;

import bombestmanbot.grid.Tile;
import bombestmanbot.grid.bomb.Bomb;
import bombestmanbot.grid.pathfinding.BasicWeigthDecider;
import bombestmanbot.grid.pathfinding.Dijkstra;
import bombestmanbot.grid.pathfinding.SafeTargetDecider;
import bombestmanbot.grid.pathfinding.SafeToBombTargetDecider;
import bombestmanbot.grid.pathfinding.TargetDecider;
import bombestmanbot.grid.pathfinding.WeigthDecider;
import bombestmanbot.strategy.RootStrategy;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BombestmanBot {

    
    //private static ArrayList<Point> playerCoords;
    public static Game game;
    
    public static Communication communication;
    
    public static int mapWidth;
    public static int mapHeight;
    public static int bombersCount;
    public static int pointsPerTreasure;
    public static int pointsLostForDying;
    public static int dyingCoolDown;
    public static int bombTimeDice;
    public static int bombeTimerSides;
    public static int bombForce;
    public static int turns;
    public static double treasureChange;
    public static int initialCountOfBombs;
    public static Tile myTile;
    
    public static int botId;
    
    public static void main(String[] args) throws InterruptedException, IOException {
        init(args);
        while (true) {
            communication.readGame();
            updateMyPlayer();
            makeMove();
        }
    }
    
    public static void updateMyPlayer() {
        Point myPlayerCoords = game.getPlayerCoords().get(botId);
        if (myPlayerCoords == null) {
            myTile = null;
        } else {
            myTile = game.getGrid().getTile(myPlayerCoords);
        }
    }
    
    
    private static void redirectStreams() throws FileNotFoundException {
        FileOutputStream out = new FileOutputStream("output.txt");
        System.setOut(new PrintStream(out));
        FileOutputStream err = new FileOutputStream("error.txt");
        System.setErr(new PrintStream(err));
        File file = new File(".");
        if (file.isDirectory()) System.out.println("found directory.."+file.getAbsolutePath());
        file = new File("/");
        if (file.isDirectory()) System.out.println("found directory2.."+file.getAbsolutePath());
        file = new File("");
        if (file.isDirectory()) System.out.println("found directory3.."+file.getAbsolutePath());
    }
    
    
    private static void init(String[] args) throws IOException, InterruptedException {
        System.out.println("initializing game");
        redirectStreams();
        botId = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);
        communication = new Communication(port);
        communication.readSpecs();

//        playerCoords = new ArrayList<>(bombersCount);
//        for (int i = 0; i < bombersCount; i++) {
//            playerCoords.add(null);//Location is still unkown.
//        }
        game = new Game(mapWidth, mapHeight);
        System.out.println("game initialized");
    }
    
    
    
    public static String directionToCommand(String direction) {
        if (direction.equals(Tile.DIRECTION_UP)) {
            return Communication.COMMAND_MOVE_UP;
        } else if (direction.equals(Tile.DIRECTION_RIGHT)) {
            return Communication.COMMAND_MOVE_RIGHT;
        } else if (direction.equals(Tile.DIRECTION_DOWN)) {
            return Communication.COMMAND_MOVE_DOWN;
        } else if (direction.equals(Tile.DIRECTION_LEFT)) {
            return Communication.COMMAND_MOVE_LEFT;
        } else {
            return null;
        }
    }
    
    
    public static Double pathToNearestSafe(LinkedList<Tile> output) {
        TargetDecider tD = new SafeTargetDecider();
        WeigthDecider wD = new BasicWeigthDecider();
        Dijkstra dijkstra = new Dijkstra(game.getGrid(), myTile, tD, wD);
        return dijkstra.findShortestPath(output);
    }
    
    
    
    
    
    
    public static boolean canEscape(final Bomb testBomb2) {
        game.getBombField().addBomb(testBomb2);
        LinkedList<Tile> output = new LinkedList<>();
        TargetDecider tD = new SafeTargetDecider();
        WeigthDecider wD = new WeigthDecider() {
            @Override
            public double getWeigth(Tile tile, double currentDist) {
                for (Bomb bomb : tile.getThreateningBombs()) {
                    if (!bomb.equals(testBomb2)) return Double.POSITIVE_INFINITY;
                }
                if (!tile.isPassable()) {
                    return Double.POSITIVE_INFINITY;
                } else {
                    if (tile.isDangerous()) {
                        return 100;
                    } else {
                        return 1;
                    }
                }
            }
        };
        Dijkstra dijkstra = new Dijkstra(game.getGrid(), myTile, tD, wD);
        Double length = dijkstra.findShortestPath(output);
        game.getBombField().removeBomb(testBomb2);
        return (length != null && output.size() < 4);
        
    }
    
    
    public static boolean safeToBomb() {
        //Simulate escaping from the current tile b4 droppin bomb:
        Bomb testBomb = new Bomb(botId, 0, bombTimeDice, bombeTimerSides, myTile);
        game.getBombField().addBomb(testBomb);
        LinkedList<Tile> output = new LinkedList<>();
        Double length = pathToNearestSafe(output);
        game.getBombField().removeBomb(testBomb);
        
        if (length == null || length > 3) return false;
        //simulate dropping a bomb
        final Bomb testBomb2 = new Bomb(botId, bombForce, bombTimeDice, bombeTimerSides, myTile);
        return (canEscape(testBomb2));
        
    }
    
    
    public static Double pathToNearestTileThatCanThreatenTile(final Tile tile, LinkedList<Tile> output) {
        TargetDecider tD = new TargetDecider() {
            TargetDecider safeToBombDecider = new SafeToBombTargetDecider();
            List<Tile>  acceptedTiles = getThreateningTiles();
                    
            public List<Tile> getThreateningTiles() {
                List<Tile> result = new ArrayList<>();
                String[] directions = new String[] {Tile.DIRECTION_DOWN, Tile.DIRECTION_LEFT, Tile.DIRECTION_RIGHT, Tile.DIRECTION_UP};
                for (String direction : directions) {
                    Tile current = tile;
                    int steps = 0;
                    while((current = current.getNeighbour(direction)) != null && steps < bombForce) {
                        steps++;
                        if (!current.isPassable()) break;
                        result.add(current);
                    }
                }
                return result;
            }        
            
            @Override
            public boolean isTarget(Tile tile) {
                return acceptedTiles.contains(tile) && safeToBombDecider.isTarget(tile);
            }
        };
        
        WeigthDecider wD = new BasicWeigthDecider();     
        Dijkstra dijkstra = new Dijkstra(game.getGrid(), myTile, tD, wD);
        return dijkstra.findShortestPath(output);  
    }
    
    
    

    
    
    private static String decideNextCommand() {
        return RootStrategy.getInstance().execute();
    }
    
    
    public static void makeMove() {
        System.out.println("making the move");
        String nextCommand = decideNextCommand();
        System.out.println("next command = "+nextCommand);
        communication.sendCommand(nextCommand);
        System.out.println("move made");
    }
    
}