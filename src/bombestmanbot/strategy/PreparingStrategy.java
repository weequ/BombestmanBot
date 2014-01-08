/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot.strategy;

import bombestmanbot.Communication;

/**
 *
 * @author antti
 */
public class PreparingStrategy implements Strategy {

    @Override
    public String execute() {
        System.out.println("using preparing stragy");
        return Communication.COMMAND_WAIT;
    }
    
}
