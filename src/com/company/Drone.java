package com.company;

/**
 * Created by Soli on 25/03/2017.
 */
public class Drone {
    int remainingSpace;
    Location location;
    double turn;

    public Drone(){
        location = new Location();
        turn = 0;
    }

    public Load load(Order order,double distance){
        while(order.loaded < order.products.length){
            if(remainingSpace < order.products[order.loaded].productWeight ||
                    (order.loaded != 0 && order.products[order.loaded] != order.products[order.loaded -1])){
                break;
            } else {
                remainingSpace -= order.products[order.loaded].productWeight;
                order.loaded++;
            }
        }
        turn += distance + 1;
        // Todo
        return null;
    }

    public String deliver(Product [] products, Order order){
        // Todo
        remainingSpace += 0;
        order.delivered += products.length;
        // Todo
        return null;
    }
}
