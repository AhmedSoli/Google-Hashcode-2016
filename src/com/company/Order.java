package com.company;

/**
 * Created by Soli on 25/03/2017.
 */
public class Order {
    int orderID;
    Product products[];
    Location location;
    int loaded;
    int remainingItemsStart;
    Warehouse warehouse;
    double bestWarehouseValue;
    boolean done;


    public Order() {
        location = new Location();
        done = false;
        loaded = 0;
        remainingItemsStart = 0;
        warehouse = null;
        bestWarehouseValue = -1;
    }

    public double getScore(int deadline, int turn){
       return  (100 *(deadline - turn) ) / deadline;
    }
}
