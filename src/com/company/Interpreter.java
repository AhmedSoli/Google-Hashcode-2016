package com.company;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Created by Soli on 25/03/2017.
 */
public class Interpreter {

    int maximumLoadOfDrone;
    int numberOfWareHouses;
    int numberOfOrders;
    int numberOfDrones;
    int numberOfProductTypes;
    Product [] products;
    Drone [] drones;
    Simulation simulation;
    Warehouse [] warehouses;
    Order [] orders;

    public Interpreter(String filePath){
        // Reading the file
        String content = readFile(filePath, StandardCharsets.UTF_8);
        String [] lines = content.split("\n");
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
            String [] line4 = lines[4+(wareHouseID*3)].split(" ");
            Warehouse warehouse = new Warehouse();
            warehouse.location.row = Integer.parseInt(line4[0]);
            warehouse.location.column = Integer.parseInt(line4[1]);
            warehouse.numberOfAvailableProducts = new int[numberOfProductTypes];
            String [] line5 = lines[5+(wareHouseID*3)].split(" ");
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
            Drone drone = new Drone();
            drone.location = warehouses[0].location;
            drone.remainingSpace = maximumLoadOfDrone;
            drones[droneID] = drone;

        }
        // Setting up Customer orders
        int lineIndex = 6+(numberOfWareHouses - 1)*3;
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

    public String[] getNextCommand(Drone drone,int turn){
        String[] commands = new String[3];
        commands[0] = "";
        commands[1] = "";
        commands[2] = "";

        double bestValue = 0;
        Order bestOrder = null;
        if(turn == drone.turn){
            double value = 0;
            for (Order order: orders){
                if(order.done == false){
                    value = calculateValue(drone,order);
                    if(value > bestValue){
                        bestOrder = order;
                    }
                }
            }
        }
        if(bestOrder != null) {
            drone.load(bestOrder,calculateDistancteBetweenLocations(bestOrder.location,warehouses[0].location));
            // Todo
            drone.deliver(products,bestOrder);
            if(bestOrder.loaded == bestOrder.products.length){
                bestOrder.done = true;
            }
        }
        return commands;
    }

    public double calculateDistancteBetweenLocations(Location from, Location to){
        double distance = Math.sqrt(Math.abs(from.row - to.row) + Math.abs(from.column - to.column));
        return Math.ceil(distance);
    }

    public double calculateValue(Drone drone, Order order){
        double distance = calculateDistancteBetweenLocations(drone.location, warehouses[0].location) +
                calculateDistancteBetweenLocations(warehouses[0].location,order.location);
        return distance / getSize(order);
    }

    public double getSize(Order order){
        double size = 0;
        for (Product product: order.products){
            size += product.productWeight;
        }
        return size;
    }

    @NotNull
    static String readFile(String path, Charset encoding){
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
        } catch (IOException e) {
            System.out.println("File didn't load correctly! Please check path!");
            return "";
        }
    }
}
