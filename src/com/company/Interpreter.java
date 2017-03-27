package com.company;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Soli on 25/03/2017.
 */
class Interpreter {

    int maximumLoadOfDrone;
    int numberOfWareHouses;
    int numberOfOrders;
    int numberOfDrones;
    int numberOfProductTypes;
    double score;
    Product [] products;
    Drone [] drones;
    Simulation simulation;
    Warehouse [] warehouses;
    Order [] orders;

    Interpreter(String filePath){
        // Reading the file
        String content = readFile(filePath, StandardCharsets.UTF_8);
        String [] lines = content.split("\n");
        // Setting the score to 0
        score = 0;
        // Setting up the simulation
        String [] line0 = lines[0].split(" ");
        simulation = new Simulation();
        simulation.maxLocation.row = Integer.parseInt(line0[0]);
        simulation.maxLocation.column = Integer.parseInt(line0[1]);
        simulation.deadline = Integer.parseInt(line0[3]);
        // Setting up the products
        String [] line1 = lines[1].split(" ");
        numberOfProductTypes = Integer.parseInt(line1[0]);
        products = new Product[numberOfProductTypes];
        String [] line2 = lines[2].split(" ");
        for(int productID = 0;productID<numberOfProductTypes;productID++){
            Product product = new Product();
            product.productID = productID;
            product.productWeight = Integer.parseInt(line2[productID]);
            products[productID] = product;
        }
        // Setting up the warehouses
        String [] line3 = lines[3].split(" ");
        numberOfWareHouses = Integer.parseInt(line3[0]);
        warehouses = new Warehouse[numberOfWareHouses];
        for(int wareHouseID = 0; wareHouseID< numberOfWareHouses;wareHouseID++){
            String [] line4 = lines[4+(wareHouseID*2)].split(" ");
            Warehouse warehouse = new Warehouse();
            warehouse.location.row = Integer.parseInt(line4[0]);
            warehouse.location.column = Integer.parseInt(line4[1]);
            warehouse.numberOfAvailableProducts = new int[numberOfProductTypes];
            warehouse.warehouseID = wareHouseID;
            String [] line5 = lines[5+(wareHouseID*2)].split(" ");
            for(int productID = 0;productID<numberOfProductTypes;productID++){
                warehouse.numberOfAvailableProducts[productID] = Integer.parseInt(line5[productID]);
            }
            warehouses[wareHouseID] = warehouse;
        }
        // Setting up the drones
        numberOfDrones = Integer.parseInt(line0[2]);
        maximumLoadOfDrone = Integer.parseInt(line0[4]);
        drones = new Drone[numberOfDrones];
        for(int droneID = 0; droneID < numberOfDrones;droneID++){
            Drone drone = new Drone(maximumLoadOfDrone);
            drone.location = warehouses[0].location;
            drones[droneID] = drone;

        }
        // Setting up Customer orders
        int lineIndex = 6+(numberOfWareHouses - 1)*2;
        String [] line6 = lines[lineIndex].split(" ");
        numberOfOrders = Integer.parseInt(line6[0]);
        orders = new Order[numberOfOrders];
        for(int orderID = 0;orderID<numberOfOrders;orderID++){
            Order order = new Order();
            order.orderID = orderID;
            String [] line7 = lines[lineIndex+1+orderID*3].split(" ");
            order.location.row = Integer.parseInt(line7[0]);
            order.location.column = Integer.parseInt(line7[1]);
            String [] line8 = lines[lineIndex+2+orderID*3].split(" ");
            int numberOfItems = Integer.parseInt(line8[0]);
            String [] line9 = lines[lineIndex+3+orderID*3].split(" ");
            order.products = new Product[numberOfItems];
            for(int numberOfItem = 0;numberOfItem <numberOfItems;numberOfItem++){
                order.products[numberOfItem] = products[Integer.parseInt(line9[numberOfItem])];
            }
            // Sorting the orders after their size.
            QuickSort.quickSort(order.products,0,numberOfItems -1);
            orders[orderID] = order;
        }
    }

    List <String> getNextCommand(Drone drone,int turn){
        List commands = new ArrayList<>();

        double bestValue = Double.MAX_VALUE;
        Order bestOrder = null;
        if(turn == drone.turn){
            for (Order order: orders){
                if(!order.done  && order.products[order.loaded].productWeight <= drone.remainingSpace){
                    double value = getValue(drone,order);
                    if(value < bestValue){
                        bestOrder = order;
                        bestValue = value;
                    }
                }
            }
        }
        if(bestOrder != null) {
            int loadDistance = getDistanceBetweenLocations(bestOrder.location,warehouses[0].location);
            int deliveryDistance = getDistanceBetweenLocations(bestOrder.location,drone.location);
            if(loadDistance + deliveryDistance + 2 + drone.turn <= simulation.deadline) {
                drone.load(bestOrder,loadDistance);
                drone.deliver(bestOrder,deliveryDistance);
                if(bestOrder.remainingItemsStart == bestOrder.products.length){
                    bestOrder.done = true;
                    score += bestOrder.getScore(simulation.deadline,loadDistance + deliveryDistance + 2 + turn);
                }
            }
        }
        return commands;
    }

    private int getDistanceBetweenLocations(Location from, Location to){
        double distance = Math.sqrt(Math.abs(from.row - to.row) + Math.abs(from.column - to.column));
        return (int) Math.ceil(distance);
    }

    private double getValue(Drone drone, Order order){
        order.bestWarehouseValue = 1000000;
        for(Warehouse warehouse :warehouses){
            int remainingSpace = drone.maxCapacity;
            int itemNumber = order.remainingItemsStart;
            int itemAdded = 0;
            while(itemNumber<order.products.length){
                if(remainingSpace < order.products[itemNumber].productWeight ||
                    warehouse.numberOfAvailableProducts[order.products[itemNumber].productID]
                        == 0){
                    break;
                } else {
                    remainingSpace -= order.products[itemNumber].productWeight;
                    itemNumber++;
                    itemAdded++;
                }
            }
            double warehouseValue =  (double)order.products.length / (itemNumber) ;
            if(warehouseValue < order.bestWarehouseValue && itemAdded != 0){
                order.bestWarehouseValue = warehouseValue;
                order.warehouse = warehouse;
            }
        }
        double distance = getDistanceBetweenLocations(drone.location,order.warehouse.location) +
                getDistanceBetweenLocations(order.warehouse.location,order.location);
        return  (distance * order.bestWarehouseValue)*getSize(order);
    }

    private double getSize(Order order){
        double size = 0;
        for (Product product: order.products){
            size += product.productWeight;
        }
        return size;
    }

    @NotNull
    private static String readFile(String path, Charset encoding){
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
        } catch (IOException e) {
            System.out.println("File didn't load correctly! Please check path!");
            return "";
        }
    }
}
