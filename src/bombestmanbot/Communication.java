/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot;

import static bombestmanbot.BombestmanBot.bombForce;
import static bombestmanbot.BombestmanBot.bombTimeDice;
import static bombestmanbot.BombestmanBot.bombeTimerSides;
import static bombestmanbot.BombestmanBot.bombersCount;
import static bombestmanbot.BombestmanBot.dyingCoolDown;
import static bombestmanbot.BombestmanBot.game;
import static bombestmanbot.BombestmanBot.initialCountOfBombs;
import static bombestmanbot.BombestmanBot.mapHeight;
import static bombestmanbot.BombestmanBot.mapWidth;
import static bombestmanbot.BombestmanBot.pointsLostForDying;
import static bombestmanbot.BombestmanBot.pointsPerTreasure;
import static bombestmanbot.BombestmanBot.treasureChange;
import static bombestmanbot.BombestmanBot.turns;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author antti
 */
public class Communication {
    public static final String COMMAND_WAIT = "wait";
    public static final String COMMAND_BOMB = "bomb";
    public static final String COMMAND_MOVE_UP = "move u";
    public static final String COMMAND_MOVE_RIGHT = "move r";
    public static final String COMMAND_MOVE_DOWN = "move d";
    public static final String COMMAND_MOVE_LEFT = "move l";
    
    private BufferedReader read;
    private PrintWriter write;
    
    
    public Communication(int port) throws InterruptedException, IOException {
        Socket sock = connect("127.0.0.1", port);
        write = new PrintWriter(sock.getOutputStream());
        read = new BufferedReader(new InputStreamReader(sock.getInputStream()));
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
    
    
    public void sendCommand(String command) {
        write.append(command);
        write.append("\n");
        write.flush();
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
    public void readSpecs() throws IOException {
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
    
    
    private String readEndOfLine(int beginIndex) throws IOException {
        return read.readLine().substring(beginIndex);
    }
    
    public void readGame() throws IOException {
        readTurns();
        readMap();
        readPlayerInfo();
        readBombs();
    }
    
    
    public void readTurns() throws IOException {
        String line;
        while (!(line = read.readLine()).startsWith("Turns left:")) {
            
        }
    }
    
    
    public void readMap() throws IOException {
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
    
    
    public void readPlayerInfo() throws IOException {
        for (int i = 0; i < bombersCount; i++) {
            String playerInfoLine = read.readLine();
            Point newCoords = parseCoordinates(playerInfoLine);
            game.setPlayerCoords(i, newCoords);
        }
    }
    
    
    public void readBombs() throws IOException {
        String line;
        Map<Point, Integer> bombs = new HashMap();
        while (!(line = read.readLine()).isEmpty()) {
            String owner = line.substring(line.length()-2, line.length()-1);
            bombs.put(parseCoordinates(line), Integer.parseInt(owner));
        }
        //read.readLine();
        game.getBombField().update(bombs);
    }
}
