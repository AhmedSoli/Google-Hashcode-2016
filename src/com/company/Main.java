package com.company;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main{

    public static void main(String[] args) {
        double score = 0;
        while(true){
            String [] files = {"mother_of_all_warehouses.txt","busy_day.txt","redundancy.txt"};
            System.out.println("Choose the number of file:");
            System.out.println("0 -- Mother of all warehouses");
            System.out.println("1 -- Busy day");
            System.out.println("2 -- Redundancy");
            Scanner scanner = new Scanner(System.in);
            String read = scanner.nextLine();
            String filePath = "./data/" + files[Integer.parseInt(read)];
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
            print(String.valueOf(env.score));
            score += env.score;
            print(String.valueOf(score));
        }
    }


    static void print(String toPrint){
        System.out.println(toPrint);
    }
}

