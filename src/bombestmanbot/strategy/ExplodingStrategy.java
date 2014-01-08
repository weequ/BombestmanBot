/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot.strategy;

import static bombestmanbot.BombestmanBot.safeToBomb;
import bombestmanbot.Communication;

/**
 *
 * @author antti
 */
public class ExplodingStrategy implements Strategy {
    
    @Override
    public String execute() {
        System.out.println("using exploding strategy");
        if (safeToBomb()){
            return Communication.COMMAND_BOMB;
        } else {
            return Communication.COMMAND_WAIT;
        }
    }
    
}
