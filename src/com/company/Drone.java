package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soli on 25/03/2017.
 */
public class Drone {
    int remainingSpace;
    int maxCapacity;
    Location location;
    int turn;

    public Drone(int maxCapacity){
        location = new Location();
        this.maxCapacity = maxCapacity;
        this.remainingSpace = maxCapacity;
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
                if (order.loaded == order.products.length ||remainingSpace < order.products[order.loaded].productWeight
                        || order.warehouse.numberOfAvailableProducts[order.products[order.loaded].productID] == 0) {
                    break outerLoop;
                } else  if(order.products[order.loaded] != load.product){
                    break innerLoop;
                } else {
                    order.warehouse.numberOfAvailableProducts[order.products[order.remainingItemsStart].productID]--;
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
        remainingSpace = maxCapacity;
        order.remainingItemsStart = order.loaded;
        turn += 1 + distance;
        this.location = order.location;
    }
}
