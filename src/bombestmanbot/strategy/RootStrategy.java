/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot.strategy;

import static bombestmanbot.BombestmanBot.botId;
import static bombestmanbot.BombestmanBot.game;
import static bombestmanbot.BombestmanBot.myTile;


public class RootStrategy implements Strategy {

    private Strategy deadStrategy = new DeadStrategy();
    private Strategy explodingStrategy = new ExplodingStrategy();
    private Strategy preparingStrategy = new PreparingStrategy();
    private Strategy treasureStrategy = new TreasureStrategy();
    private Strategy dangerousStrategy = new DangerousStrategy();
    private static Strategy instance = new RootStrategy();
    
    public static Strategy getInstance() {
        return instance;
    }
    
    
    private RootStrategy() {
        
    }
    
    
    @Override
    public String execute() {
        System.out.println("deciding next command");
        Strategy myStrategy;
        if (myTile == null) {//Dead
            myStrategy = deadStrategy;
        } else if (myTile.isDangerous()) {
            myStrategy = dangerousStrategy;
        } else if (!game.getGrid().getTreasureTiles().isEmpty()) {
            myStrategy = treasureStrategy;
        } else if (game.getBombField().bombsLeft(botId) > 0) {
            myStrategy = explodingStrategy;
        } else {
            myStrategy = preparingStrategy;
        }
        return myStrategy.execute();
    }
    
}
