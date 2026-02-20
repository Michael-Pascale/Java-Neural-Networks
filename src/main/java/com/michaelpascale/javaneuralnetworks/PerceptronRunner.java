package com.michaelpascale.javaneuralnetworks;

import java.io.IOException;
import java.util.Random;

public class PerceptronRunner{
    public static void main(String[] args){
        System.out.println("This is a program to show what A single perceptron can do.");
        PerceptronRunner.testRules();
    }

    public static void functionTest(){
        System.out.println("Tests if a perceptron can figure out a simple inequality. Say y>3x");
        
        //initialize data
        int[] expected = new int[5000];
        double[][] data = new double[5000][3];

        Random r = new Random();
        for(int i = 0; i < expected.length; i++){
            data[i][0] = 1;
            data[i][1] = r.nextDouble()*100;
            data[i][2] = r.nextDouble()*100;
            expected[i] = data[i][2] > data[i][1]*3 ? 1 : -1;
        }

        Perceptron p = new Perceptron(0.01f, 0.5f, 3, 3);
        float accuracy;
        do{
            accuracy = p.train(data, expected);
            System.out.println("Total accuracy over 5000 points is " + accuracy);
        }while(accuracy < 0.75);

        double[] testInput = {1.0,30.0,94.0};
        System.out.println("Next input is 3,10. The result should be 1: " + p.fire(testInput));
        
        //fire on some more data
        System.out.println("Firing on another 50 thousand points");
        expected = new int[50000];
        data = new double[50000][3];
        for(int i = 0; i < expected.length; i++){
            data[i][0] = 1;
            data[i][1] = r.nextDouble()*100;
            data[i][2] = r.nextDouble()*100;
            expected[i] = data[i][2] > data[i][1]*3 ? 1 : -1;
        }
        int counter = 0;
        for(int i = 0; i < expected.length; i++){
            counter += p.fire(data[i]) == expected[i] ? 1 : 0;
        }
        System.out.println("It got " + counter + " correct out of 50000");
        String filename = p.saveToFile();

        //testing reading to file
        System.out.println("reading from the file and checking to see if it still works");
        try{
            p = Perceptron.readFromFile(filename);
            System.out.println("Firing on another 50 thousand points");
            expected = new int[50000];
            data = new double[50000][3];
            for(int i = 0; i < expected.length; i++){
                data[i][0] = 1;
                data[i][1] = r.nextDouble()*100;
                data[i][2] = r.nextDouble()*100;
                expected[i] = data[i][2] > data[i][1]*3 ? 1 : -1;
            }
            counter = 0;
            for(int i = 0; i < expected.length; i++){
                counter += p.fire(data[i]) == expected[i] ? 1 : 0;
            }
            System.out.println("It got " + counter + " correct out of 50000");
        }catch(IOException e){
            e.printStackTrace();
        }
        

    }
    
    public static void testRules(){
        System.out.println("This will test if a perceptron can accurately emulate rule 220.");
        double[][] data = {{1,1,1,1},{1,1,1,0},{1,1,0,1},{1,1,0,0},{1,0,1,1},{1,0,1,0},{1,0,0,1},{1,0,0,0}};
        int[] expected = {-1,-1,-1,1,1,1,1,-1};
        float accuracy;
        Perceptron p = new Perceptron(1.0f, 0.5f, 4, 3);
        long time = System.currentTimeMillis();
        do{
            accuracy = p.train(data,expected);
        }while(accuracy < 1.0 && (System.currentTimeMillis() - time) < 60000);
        System.out.println("Took " + System.currentTimeMillis() + " to get to " + accuracy + " accuracy.");
        if(accuracy == 1.0)
            p.saveToFile("PerceptronRule3220.p");
    }

    public static void ORGate(){
        System.out.println("This seeks to create a perceptron which functions as a 3 input or gate");
        Perceptron p = new Perceptron(0.0001f, 1.0f, 4, 4);
        double[][] data = {{1,0,0,0},{1,0,0,1},{1,0,1,0},{1,0,1,1},
                        {1,1,0,0},{1,1,0,1},{1,1,1,0},{1,1,1,1}};
        int[] expected = {-1,1,1,1,1,1,1,1};
        float accuracy;
        long time = System.currentTimeMillis();
        do{
            accuracy = p.train(data,expected);
        }while(accuracy < 1.0 && System.currentTimeMillis() - time < 60000);
        System.out.println("Took " + (System.currentTimeMillis() - time)/1000.0 + " seconds");
        String filename = p.saveToFile();
        System.out.println("File: " + filename);
        try{
            p = Perceptron.readFromFile(filename);
        }catch(IOException e){
            System.out.println("File couldnt be read. doubles with E probably dont work with scanner.");
        }
    }

    public static void Rule30PerceptronTraining(){
        System.out.println("This method will train 3 new perceptrons to solve the following functions");
        double[] weights = {-1.0,-1.0,2.0};
        Perceptron p = new Perceptron(0.001f,1.0f,weights);
        double[][] dataA = {{1,0,0},{1,0,1},{1,1,0},{1,1,1}};
        int[] expectedA = {-1,1,-1,-1};
        float accuracy;
        long time = System.currentTimeMillis();
        do{
            accuracy = p.train(dataA,expectedA);
            if(System.currentTimeMillis() - time > 1000){
                System.out.println("Accuracy: " + accuracy);
                time = System.currentTimeMillis();
            }
        }while(accuracy < 1.0);
        System.out.println("!X AND Y: DONE - " + p.saveToFile("PerceptronNOTXANDY.p"));
        //--------------------------------------------------------
        double[] weightsB = {-1,0,0,0};
        p = new Perceptron(1.0f,1.0f,weightsB);
        double[][] dataB = {{1,0,0,0},{1,0,0,1},{1,0,1,0},{1,0,1,1},{1,1,0,0},{1,1,0,1},{1,1,1,0},{1,1,1,1}};
        int[] expectedB = {-1,1,-1,-1,-1,-1,-1,-1};
        time = System.currentTimeMillis();
        do{
            accuracy = p.train(dataB,expectedB);
            if(System.currentTimeMillis() - time > 1000){
                System.out.println("Accuracy: " + accuracy);
                time = System.currentTimeMillis();
            }
        }while(accuracy < 1.0);
        System.out.println("!X AND !Y AND Z: DONE - " + p.saveToFile("PerceptronNOTXANDNOTYANDZ.p"));
        //--------------------------------------------------------
        p = new Perceptron(1.0f,1.0f,weightsB);
        int[] expectedC = {-1,-1,-1,-1,1,-1,-1,-1};
        time = System.currentTimeMillis();
        do{
            accuracy = p.train(dataB,expectedC);
            if(System.currentTimeMillis() - time > 1000){
                System.out.println("Accuracy: " + accuracy);
                time = System.currentTimeMillis();
            }
        }while(accuracy < 1.0);
        System.out.println("X AND !Y AND !Z: DONE - " + p.saveToFile("PerceptronXANDNOTYANDNOTZ.p")); 
    }

    public static void Rule30(){
        System.out.println("This is a program that will attempt to show rule 30 using 4 perceptrons");
        try{
            Perceptron p0 = Perceptron.readFromFile("ORGATE3Input0.p");
            Perceptron p1 = Perceptron.readFromFile("PerceptronNOTXANDY.p");
            Perceptron p2 = Perceptron.readFromFile("PerceptronNOTXANDNOTYANDZ.p");
            Perceptron p3 = Perceptron.readFromFile("PerceptronXANDNOTYANDNOTZ.p");
            double[][] data = {{1,1,1,1},{1,1,1,0},{1,1,0,1},{1,1,0,0},{1,0,1,1},{1,0,1,0},{1,0,0,1},{1,0,0,0}};
            String result = "";
            for(int i = 0; i < data.length; i++){
                double[] d1 = {data[i][0],data[i][1],data[i][2]};
                int r1 = p1.fire(d1) == -1 ? 0:1;
                int r2 = p2.fire(data[i]) == -1 ? 0:1;
                int r3 = p3.fire(data[i]) == -1 ? 0:1;
                double[] input = {1,r1,r2,r3};
                int r0 = p0.fire(input);
                result += r0 == -1 ? "0" : "1";
            }

            System.out.println("Resulting number should be 30: " + Integer.parseInt(result, 2));

            System.out.println("Testing an actual dataset:");
            String gen = "0000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000";
            System.out.println(gen);
            NeuralNetAutomaton n = new NeuralNetAutomaton();
            long time = System.currentTimeMillis();
            do{
                gen = n.generation(gen);
                System.out.println(gen);
                Thread.sleep(500);
            }while(System.currentTimeMillis() - time < 60000);
        }catch(IOException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}