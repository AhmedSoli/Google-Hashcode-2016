package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soli on 25/03/2017.
 */
public class Drone {
    int remainingSpace;
    int maxSpace;
    Location location;
    double turn;

    public Drone(int maxSpace){
        location = new Location();
        this.maxSpace = maxSpace;
        this.remainingSpace = maxSpace;
        turn = 0;
    }

    public List<Load> load(Order order,int distance){
        List<Load> loads = new ArrayList<Load>();
        outerLoop:
        while(order.loaded < order.products.length){
            Load load = new Load();
            load.product = order.products[order.loaded];
            innerLoop:
            while(true) {
                if(order.loaded == order.products.length || order.products[order.loaded] != load.product){
                    break innerLoop;
                } else if (remainingSpace < order.products[order.loaded].productWeight) {
                    break outerLoop;
                } else {
                    remainingSpace -= order.products[order.loaded].productWeight;
                    order.loaded++;
                    load.numberOfItems++;
                }
            }
            if(load.numberOfItems > 0){
                loads.add(load);
            }
        }
        turn += distance + loads.size();
        return loads;
    }

    public void deliver(Order order,int distance){
        remainingSpace = maxSpace;
        order.delivered = order.loaded;
        turn += 1 + distance;
    }
}
