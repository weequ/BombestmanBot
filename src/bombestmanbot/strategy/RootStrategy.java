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
    private LootinStrategy lootingStrategy = new LootinStrategy();
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
        } else if (!game.getGrid().getTreasureTiles().isEmpty() && game.getBombField().bombsLeft(botId) == bombestmanbot.BombestmanBot.initialCountOfBombs) {
            myStrategy = treasureStrategy;
        } else if (lootingStrategy.shouldExecute()) {
            myStrategy = lootingStrategy;
        } else if (game.getBombField().bombsLeft(botId) > 0) {
            myStrategy = explodingStrategy;
        } else {
            myStrategy = preparingStrategy;
        }
        return myStrategy.execute();
    }
    
}
