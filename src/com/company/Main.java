package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main{

    public static void main(String[] args) {
        double score = 0;
        while(true){
            String [] files = {"mother_of_all_warehouses.txt","busy_day.txt","redundancy.txt"};
            System.out.println("Choose the number of file:\n" +
            "0 -- Mother of all warehouses\n" +
            "1 -- Busy day\n" +
            "2 -- Redundancy");
            Scanner scanner = new Scanner(System.in);
            String read = scanner.nextLine();
            int fileNumber = 0;
            try {
                fileNumber = Integer.parseInt(read);
                if(fileNumber != 0 && fileNumber != 1 && fileNumber !=2){continue;}
            } catch (NumberFormatException e){
                continue;
            }
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
                print(String.valueOf(turn));
            }
            print("File Score: " + String.valueOf(env.score));
            score += env.score;
            print("Overall Score: " + String.valueOf(score));
        }
    }


    static void print(String toPrint){
        System.out.println(toPrint);
    }
}

