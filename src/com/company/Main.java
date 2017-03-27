package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main{

    public static void main(String[] ar) {
        double score = 0;
        String [] files = {"mother_of_all_warehouses.txt","busy_day.txt","redundancy.txt"};
        for(int fileNumber = 0;fileNumber<files.length;fileNumber++){
            String filePath = "./data/" + files[fileNumber];
            Interpreter env = new Interpreter(filePath);
            String commands = "";
            for(int turn = 0;turn < env.simulation.deadline;turn++){
                for (Drone drone: env.drones){
                    String commandsTemp = env.getNextCommand(drone,turn);
                    if (commandsTemp != ""){
                        commands += commandsTemp;
                    }
                }
            }
            print("File Score" + "(" + files[fileNumber] +"): " + String.valueOf(env.score));
            score += env.score;
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("solution/sol_" + files[fileNumber]));
                out.write(env.numberOfCommands + "\n" + commands);
                out.close();
            } catch (IOException e) {
                System.out.println("Exception");
            }
        }
        print("Overall Score: " + String.valueOf(score));

    }


    static void print(String toPrint){
        System.out.println(toPrint);
    }
}

