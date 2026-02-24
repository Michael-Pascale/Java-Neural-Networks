package com.michaelpascale.javaneuralnetworks.demo;

import com.michaelpascale.javaneuralnetworks.utils.SigmoidPerceptron;

import java.util.Random;
public class SigmoidRunner {
    public static void main(String[] args){
        // testSinX();
        testSigmoidRule220();
    }

    public static void testSinX() {
        System.out.println("This program demonstrates a perceptron which uses a sigmoid activation function, to \n"
                + "provide continuous answers.");
        System.out.println("How accurately can the perceptron find a boundary of sin of x?");
        double[][] data = new double[100][2];
        double[] expected = new double[100];
        Random r = new Random();
        for(int i = 0; i < data.length; i++){
            data[i][0] = r.nextDouble();
            data[i][1] = r.nextDouble() * 2 - 0.5;
            expected[i] = data[i][1] > Math.sin(data[i][0]) ? 1 : 0;
        }

        long time = System.currentTimeMillis();
        double error, avg;
        SigmoidPerceptron p = new SigmoidPerceptron(0.001f, 2, 3);
        do{
            int count = 0;
            avg = 0;
            for(int i = 0; i < data.length; i++){
                error = p.train(data[i], expected[i], false);
                avg += error;
                count++;
            }
            avg = avg / count;
            if(System.currentTimeMillis() - time > 1000){
                System.out.println("Average error: " + avg);
            }
        }while(avg > 0.05 && System.currentTimeMillis() - time < 60000);
        System.out.println("Average error at finish: " + avg);

        double[] input = {r.nextDouble(), r.nextDouble()*2-0.5};
        int expectedOutput = input[1] > Math.sin(input[0]) ? 1 : 0;
        double result = p.fire(input);
        System.out.println("Actual probability of " + input[0] + "," + input[1] + " being > sin(x) = " + expectedOutput);
        System.out.println("Extimated: " + result);
    }

    /**
     * Trying to get a runner to emulate rule 220 for sigmoid functions.
     * It is currently not working. I believe my counting of error and accuracy is off, which is causing issues.
     * However, doing some debugging it looks like its still not firing correctly(ie output is incorrect). More
     * Investigation is required.
     */
    public static void testSigmoidRule220(){
        System.out.println("This will test if a sigmoid perceptron can accurately emulate rule 220.");
        double[][] data = {{1,1,1,1},{1,1,1,0},{1,1,0,1},{1,1,0,0},{1,0,1,1},{1,0,1,0},{1,0,0,1},{1,0,0,0}};
        double[] expected = {0,0,0,1,1,1,1,0}; // values were previously -1, but the sigmoid perceptron returns 0-1 only
        double accuracy;

        // tweak parameters to see differences in learning rate and time to reach different accuracies.
        final float EXPECTED_ACCURACY = 0.85f;
        SigmoidPerceptron p = new SigmoidPerceptron(0.01f, 4, 3);
        long time = System.currentTimeMillis();
        long duration;
        do{
            // expectation is that over time this will improve.
            double avgError = p.train(data, expected, true);
            accuracy = 1.0 - avgError;
            duration = System.currentTimeMillis() - time;
        }while(accuracy < EXPECTED_ACCURACY && duration < 120000);

        System.out.println("Took " + duration + " ms to get to " + accuracy * 100.0 + "% accuracy.");
    }

}