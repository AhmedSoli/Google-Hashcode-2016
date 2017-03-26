package com.company;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Main{

    public static void main(String[] args) {
        String filePath = "./data/mother_of_all_warehouses.txt";
        Interpreter env = new Interpreter(filePath);
        List <String> commands = new ArrayList<String>();
        for (Drone drone: env.drones){
            for(int turn = 0;turn < env.simulation.deadline;turn++){
                List <String> commandsTemp = env.getNextCommand(drone,turn);
                if (!commandsTemp.isEmpty()){
                    commands.addAll(commandsTemp);
                }
            }
        }
        print(String.valueOf(env.score));

    }


    static void print(String toPrint){
        System.out.println(toPrint);
    }
}

