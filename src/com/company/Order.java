package com.company;

/**
 * Created by Soli on 25/03/2017.
 */
public class Order {
    int orderID;
    Product products[];
    Location location;
    int loaded;
    int delivered;
    boolean done;


    public Order() {
        location = new Location();
        done = false;
        loaded = 0;
        delivered = 0;
    }

    public double getScore(int deadline, int turn){
        return ((deadline - turn) / turn) * 100;
    }
}
