/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bombestmanbot;

import java.util.ArrayList;
import java.util.List;

/**
 * Polynomial calc..
 * May not work in all cases
 */
public class Extramath {
    public static List<Integer> mul(List<Integer> listA, List<Integer> listB) {
        List<Integer> result = new ArrayList<>(listA.size()+listB.size());
        for (int i = 0; i < listA.size()+listB.size(); i++) {
            result.add(0);
        }
        for (int i = 0; i < listA.size(); i++) {
            for (int j = 0; j < listB.size(); j++) {
                result.set(i+j, ((listA.get(i) * listB.get(j))+result.get(i+j)));
            }
        }
          
        return result;
    }
    
    
    public static List<Integer> pow(List<Integer> list, int exp) {
        List<Integer> result = new ArrayList(list);
        
        for (int i = 1; i < exp; i++) {
            result = mul(result, list);
        }
        return result;
    } 
    
    
    public static void main(String[] args) {
        Object o = null;
        System.out.println(o.toString());
        System.exit(0);
        //testing
        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        //list.add(1);
        //list.add(1);
        //list = mul(list, list);
        list = pow(list, 2);
        System.out.println(list);
    }
}
