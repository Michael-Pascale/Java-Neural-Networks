package com.michaelpascale.javaneuralnetworks.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Perceptron {
    private double[] w; // weights initialized with random numbers or passed in constructor
    private float r;//learning rate
    // Currently not used within the perceptron itself.
    private float a; //accuracy for good perceptron

    public Perceptron(float r, float a, int size, int wRange){
        this.w = new double[size];
        Random rand = new Random();
        for(int i = 0; i < this.w.length; i++){
            this.w[i] = rand.nextInt(wRange) - (wRange/2);
        }
        this.r =  r;
        this.a = a;
    }

    public Perceptron(float r, float a, double[] w){
        this.r = r;
        this.a = a;
        this.w = w;
    }

    public int threshold(double input){
        return input <= 0 ? -1 : 1;
    }

    public int fire(double[] input){
        double output = 0;
        for(int i = 0; i < w.length; i++){
            output += w[i] * input[i];
        }
        return threshold(output);
    }

    public boolean train(double[] input, int expected){
        int result = this.fire(input);
        if(result != expected){
            for(int i = 0; i < w.length; i++){
                w[i] += (r * (expected-result)*input[i]);
            }
            return false;
        }else{
            return true;
        }
    }

    public float train(double[][] input, int[] expected){
        int counter = 0;
        for(int i = 0; i < input.length; i++){
            if(this.train(input[i], expected[i]))
                counter++;
        }
        return counter/(input.length/1.0f);
    }

    public static Perceptron readFromFile(String filename) throws IOException{
        File file = new File(filename);
        if(!file.exists())
            throw new IOException("File not found");

        BufferedReader reader = new BufferedReader(new FileReader(file));
        Scanner scan = new Scanner(reader.readLine());
        scan.useDelimiter(",");
        int size = scan.nextInt();
        scan = new Scanner(reader.readLine());
        scan.useDelimiter(",");
        double[] weights = new double[size];
        for(int i = 0; i < size; i++){
            weights[i] = scan.nextDouble();
        }
        scan = new Scanner(reader.readLine());
        scan.useDelimiter(",");
        float rate = scan.nextFloat();
        scan = new Scanner(reader.readLine());
        scan.useDelimiter(",");
        float acc = scan.nextFloat();
        Perceptron p = new Perceptron(rate,acc,size,1);
        p.w = weights;
        scan.close();
        reader.close();
        return p;        
    }


    public String saveToFile(){
        String filename = "com.michaelpascale.javaneuralnetworks.utils.Perceptron" + w[0] + ".p";
        return saveToFile(filename);
        
    }

    public String saveToFile(String name){
        
        File file = new File(name);
        try{
            if(!file.exists())
                file.createNewFile();

            FileWriter writer = new FileWriter(file);
            String weights = "";
            //add the weights to the string
            for(int i = 0; i < w.length; i++){
                weights += w[i] + ",";
            }
            writer.append(w.length + "," + System.lineSeparator());
            writer.append(weights + System.lineSeparator());
            writer.append(r + "," + System.lineSeparator());
            writer.append(a + "," + System.lineSeparator());
            writer.close();
        }catch(IOException e){
            System.out.println("The file could not be created or accessed for some reason");
        }

        return name;
    }

}