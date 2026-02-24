package com.michaelpascale.javaneuralnetworks.utils;

import java.util.Random;

public class SigmoidPerceptron {
    private double bias;
    private double r;
    private double[] w;

    public SigmoidPerceptron(double r,int size, int wRange){
        this.r = r;
        w = new double[size];
        Random rand = new Random();
        for(int i = 0; i < this.w.length; i++){
            this.w[i] = rand.nextInt(wRange) - (wRange/2);
        }
        bias = rand.nextInt(wRange) - (wRange/2);
    }

    public double fire(double[] input){
        double output = bias;
        for(int i = 0; i < w.length; i++){
            output += w[i] * input[i];
        }
        return activation(output);
    }

    public double activation(double input){
        double output = 1.0 / (1 + Math.exp(-input));
        return output;
    }

    public double train(double[] input, double expected, boolean binary){
        double output = fire(input);
        if(binary)
            output = threshold(output);
        //calculate squared error
        double error = expected - output;
        bias += r * error;
        for(int i = 0; i < input.length; i++){
            w[i] += r * error * input[i];
        }
        return Math.abs(error);
    }

    /**
     * Train over a dataset and return the avg error
     * @param inputs
     * @param expected
     * @param binary
     *
     * Note that inputs and expected should be the same length, and non-zero to avoid a divide by zero error.
     * @return
     */
    public double train(double[][] inputs, double[] expected, boolean binary) {
        double totalError = 0;
        int counter;
        for (counter = 0; counter < inputs.length; counter++) {
            // trains against that input, add error to total.
            totalError += train(inputs[counter], expected[counter], binary);
        }
        // average error over the data.
        return totalError / (double) counter;
    }

    public int threshold(double input){
        return input <= 0.5 ? 0 : 1;
    }



}