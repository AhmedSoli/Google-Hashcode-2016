package com.company;

import java.util.ArrayList;
import java.util.List;


public class Main{

    public static void main(String[] ar) {
        double score = 0;
        String [] files = {"mother_of_all_warehouses.txt","busy_day.txt","redundancy.txt"};
        for(int fileNumber = 0;fileNumber<files.length;fileNumber++){
            String filePath = "./data/" + files[fileNumber];
            Interpreter env = new Interpreter(filePath);
            List <String> commands = new ArrayList<String>();
            for(int turn = 0;turn < env.simulation.deadline;turn++){
                for (Drone drone: env.drones){
                    List <String> commandsTemp = env.getNextCommand(drone,turn);
                    if (!commandsTemp.isEmpty()){
                        commands.addAll(commandsTemp);
                    }
                }
            }
            print("File Score" + "(" + files[fileNumber] +"): " + String.valueOf(env.score));
            score += env.score;
        }
        print("Overall Score: " + String.valueOf(score));
    }


    static void print(String toPrint){
        System.out.println(toPrint);
    }
}

