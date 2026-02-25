package com.michaelpascale.javaneuralnetworks.demo;

import java.util.Arrays;
import java.util.Random;

import com.michaelpascale.javaneuralnetworks.utils.FeedForwardNetwork;
import org.ejml.simple.SimpleMatrix;

public class FFRunner {

    public static final int MNIST_TRAINING_ITERATIONS = 50;
    public static void main(String[] args){
        System.out.println("This program tests the feed forward neural network, with some challenges.");
        //testFunctionality();
        // testXOR();
        testMNist();
    }

    public static void testFunctionality(){
        System.out.println("Test if the network can fire do what it needs to do.");
        double[] input = {0,1};
        FeedForwardNetwork nn = new FeedForwardNetwork(2, 2, 1);
        double[] answer = {1};
        nn.train(input, answer);
        //double[] output = nn.feedForward(input);
        //System.out.println(Arrays.toString(output));
    }

    public static void testXOR(){
        System.out.println("Test if the network can perform XOR.");
        double[][] inputs = {{0,1},{1,0},{1,1},{0,0}};
        double[][] expected = {{1},{1},{0},{0}};
        Random r = new Random();
        FeedForwardNetwork nn = new FeedForwardNetwork(2, 2, 1);
        for(int j = 0; j < 50000; j++){
            int x = r.nextInt(inputs.length);
            nn.train(inputs[x], expected[x]);
        }
        System.out.println(Arrays.toString(nn.feedForward(new double[] {0,0})));
        System.out.println(Arrays.toString(nn.feedForward(new double[] {0,1})));
        System.out.println(Arrays.toString(nn.feedForward(new double[] {1,0})));
        System.out.println(Arrays.toString(nn.feedForward(new double[] {1,1})));
    }

    public static void testMNist() {
        System.out.println("This will test the networks performance over mnist data for digit recognition");
        //initialize the network
        FeedForwardNetwork nn = new FeedForwardNetwork(784, 256, 10, 0.01);
        double[] expected = {0,0,0,0,0,0,0,0,0,0};
        int prev = 0;
        int numIterations = MNIST_TRAINING_ITERATIONS;
        try{
            //get the data
            MnistImport importer = new MnistImport();
            long time = System.currentTimeMillis();
            for(int counter = 0; counter < numIterations; counter++){
                prev = 0;
                // iterate over training data.
                for(int i = 0; i < importer.trainingkeys.length; i++){
                    // get key for training data, aka the number represented by the data in training[i]
                    int key = importer.trainingkeys[i];

                    //initialize expected data
                    // expected[prev] resets to zero
                    // expected[key] is 1. We expect the model to say with 100 percent confidence that the number
                    // in training[i] is trainingKeys[i].
                    expected[prev] = 0;
                    expected[key] = 1;
                    prev = key;
                    //train network over the data
                    // if output network on training[i] doesnt match expected, it will adjust weights.
                    nn.train(importer.training[i], expected);
                }
                System.out.println("The network has finished one iteration over the data.");
            }

            time = System.currentTimeMillis() - time;
            System.out.println("The network trained over the data " + numIterations + " times in " + time / 1000d + " seconds.");

            // Iterate over some never before seen data, and determine if the training was successful.
            //get the test data
            //HashMap<Integer, double[]> test = com.michaelpascale.javaneuralnetworks.demo.MnistImport.importTestData();
            double[] avgError = {0,0,0,0,0,0,0,0,0,0};
            SimpleMatrix answer = new SimpleMatrix(expected.length,1);
            SimpleMatrix guess = new SimpleMatrix(expected.length,1);
            SimpleMatrix outputErrors = answer.minus(guess);
            SimpleMatrix avgErrorMat = new SimpleMatrix(avgError.length, 1);
            avgErrorMat.setColumn(0, 0, avgError);
            int counter = 0;
            time = System.currentTimeMillis();
            for(int i = 0; i < importer.testKeys.length; i++){
                int key = importer.testKeys[i];
                expected[prev] = 0;
                expected[key] = 1;
                prev = key; // reset prev for next iteration

                double[] output = nn.feedForward(importer.test[i]);
                guess.setColumn(0, 0, output);
                answer.setColumn(0, 0, expected);
                outputErrors = answer.minus(guess);
                avgErrorMat = avgErrorMat.plus(outputErrors);
                counter++;
            }
            time = System.currentTimeMillis() - time;
            avgErrorMat = avgErrorMat.scale(1d/(double)counter);
            System.out.println("System took " + time + " seconds to fire over test data. Below is the average error");
            System.out.println(avgErrorMat);

            System.out.println("Below is an example");
            Random r = new Random();
            int index = r.nextInt(importer.testKeys.length);
            int testKey = importer.testKeys[index];
            double[] testInput = importer.test[index];
            System.out.println("Expected number: " + testKey);
            double[] output = nn.feedForward(testInput);
            System.out.println("Received output: " + Arrays.toString(output));
            

        }catch(IllegalArgumentException e){
            System.out.println("Something happened while training the data.");
            e.printStackTrace();
        }catch(Exception e){
            System.out.println("Something happened when trying to read the file.");
            e.printStackTrace();
        }
    }
}