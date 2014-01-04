package bombestmanbot;

import bombestmanbot.grid.Grid;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BombestmanBot {

    
    private static BufferedReader read;
    private static PrintWriter write;
    //private static ArrayList<Point> playerCoords;
    private static Game game;
    
    
    private static int mapWidth;
    private static int mapHeight;
    public static int bombersCount;
    private static int pointsPerTreasure;
    private static int pointsLostForDying;
    private static int dyingCoolDown;
    public static int bombTimeDice;
    public static int bombeTimerSides;
    public static int bombForce;
    private static int turns;
    private static double treasureChange;
    private static int initialCountOfBombs;
    
    
    public static int botId;
    
    public static void main(String[] args) throws InterruptedException, IOException {
        init(args);
        while (true) {
            readGame();
            makeMove();
        }
    }
    
    
    private static void redirectStandardOutput() throws FileNotFoundException {
        FileOutputStream f = new FileOutputStream("output.txt");
        System.setOut(new PrintStream(f));
    }
    
    
    private static void init(String[] args) throws IOException, InterruptedException {
        System.out.println("initializing game");
        redirectStandardOutput();
        botId = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);
        Socket sock = connect("127.0.0.1", port);

        //initialize reader and writer for the socket
        write = new PrintWriter(sock.getOutputStream());
        read = new BufferedReader(new InputStreamReader(sock.getInputStream()));

        readSpecs();
//        playerCoords = new ArrayList<>(bombersCount);
//        for (int i = 0; i < bombersCount; i++) {
//            playerCoords.add(null);//Location is still unkown.
//        }
        game = new Game(mapWidth, mapHeight);
        System.out.println("game initialized");
    }
    
    
    public static void makeMove() {
        System.out.println("making the move");
        String[] commands = {"move u", "move d", "move l", "move r", "bomb", "wait"};
        write.append(commands[new Random().nextInt(commands.length)]);
        write.append("\n");
        write.flush();
        System.out.println("move made");
    }

    
//    BEGIN MSG
//    map width: 11
//    map height: 11
//    bombersCount 2
//    POINTS_PER_TREASURE 1
//    POINTS_LOST_FOR_DYING 3
//    DYING_COOL_DOWN 10
//    BOMB_TIMER_DICE 4
//    BOMB_TIMER_SIDES 3
//    BOMB_FORCE 4
//    TURNS 200
//    TREASURE_CHANGE 0.2
//    INITIAL_COUNT_OF_BOMBS 2
    public static void readSpecs() throws IOException {
        System.out.println("reading game specs");
        //Ignore BEGIN MSG line
        String line;
        while (!(line = read.readLine()).startsWith("BEGIN MSG")) {
        }
        
        mapWidth = Integer.parseInt(readEndOfLine(11));
        mapHeight = Integer.parseInt(readEndOfLine(12));
        bombersCount = Integer.parseInt(readEndOfLine(13));
        pointsPerTreasure = Integer.parseInt(readEndOfLine(20));
        pointsLostForDying = Integer.parseInt(readEndOfLine(22));
        dyingCoolDown = Integer.parseInt(readEndOfLine(16));
        bombTimeDice = Integer.parseInt(readEndOfLine(16));
        bombeTimerSides = Integer.parseInt(readEndOfLine(17));
        bombForce = Integer.parseInt(readEndOfLine(11));
        turns = Integer.parseInt(readEndOfLine(6));
        treasureChange = Double.parseDouble(readEndOfLine(16));
        initialCountOfBombs = Integer.parseInt(readEndOfLine(23));
        
        //ignore the map...
        while (!(line = read.readLine()).isEmpty()) {
        }
        read.readLine();
    }
    
    
    private static String readEndOfLine(int beginIndex) throws IOException {
        return read.readLine().substring(beginIndex);
    }
    
    public static void readGame() throws IOException {
        readTurns();
        readMap();
        readPlayerInfo();
        readBombs();
    }
    
    
    public static void readTurns() throws IOException {
        String line;
        while (!(line = read.readLine()).startsWith("Turns left:")) {
            
        }
    }
    
    
    public static void readMap() throws IOException {
        System.out.println("reading map");
        String[] lines = new String[mapHeight];
        for (int i = 0; i < mapHeight; i++) {
            lines[i] = read.readLine();
            System.out.println(lines[i]);
        }
        game.getGrid().update(lines);
    }
    
    
    private static Point parseCoordinates(String line) {
        Pattern pattern = Pattern.compile("\\d*,\\d*");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String xCommaY = matcher.group();
            String[] numbers = xCommaY.split(",");
            int x = Integer.parseInt(numbers[0]);
            int y = Integer.parseInt(numbers[1]);
            return new Point(x, y);
        }
        return null;
    }
    
    
    public static void readPlayerInfo() throws IOException {
        System.out.println("reading player info");
        for (int i = 0; i < bombersCount; i++) {
            String playerInfoLine = read.readLine();
            Point newCoords = parseCoordinates(playerInfoLine);
            game.setPlayerCoords(i, newCoords);
        }
    }
    
    public static void readBombs() throws IOException {
        System.out.println("reading bombs");
        String line;
        List<Point> bombs = new ArrayList();
        while (!(line = read.readLine()).isEmpty()) {
            bombs.add(parseCoordinates(line));
        }
        //read.readLine();
        game.getBombField().update(bombs);
    }

    private static Socket connect(String host, int port) throws InterruptedException {
        //a crude but simple connection method. It simply retries connection until it is established
        while (true) {
            try {
                return new Socket(host, port);
            } catch (Exception e) {
                System.err.println("Starting socket failed: " + e.toString());
                Thread.sleep(500);
                System.err.println("Retrying connection...");
            }
        }

    }
}