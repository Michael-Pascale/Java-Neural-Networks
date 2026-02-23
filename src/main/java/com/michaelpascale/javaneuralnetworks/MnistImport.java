package com.michaelpascale.javaneuralnetworks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
public class MnistImport {
    public double[][] training, test;
    public int[] trainingkeys, testKeys;

    /**
     * Reads csv files for training and testing the MNIST dataset.
     * Make sure that files are called mnist_train.csv and mnist_test.csv, and are in an mnist folder.
     * Also ensure that row/col count match format.
     *
     * Files are rows of numbers, the first being a single digit identifying the image, followed by data representing grayscale pixel data.
     */
    public MnistImport() throws FileNotFoundException, CsvValidationException, IOException, URISyntaxException, Exception {

        /*
         * training data. Large in size. Network will iterate over this multiple times to train.
         */
        final int TRAINING_ROW_COUNT = 50000;
        final int TRAINING_COL_COUNT = 784;
        trainingkeys = new int[TRAINING_ROW_COUNT];
        training = new double[TRAINING_ROW_COUNT][TRAINING_COL_COUNT];
        URL trainingFileResource = getClass().getClassLoader().getResource("mnist/mnist_train.csv");
        if (trainingFileResource == null) {
            throw new FileNotFoundException("Resource URL for training data not found.");
        }

        File trainingFile = new File(trainingFileResource.toURI());
        try(CSVReader trainingReader = new CSVReader(new FileReader(trainingFile))) {
            String[] line;
            int counter = 0;
            while((line = trainingReader.readNext()) != null && counter < training.length){
                //parse first item in line as the number for this row
                Integer num = Integer.parseInt(line[0]);
                double[] image = new double[line.length - 1];
                // parse rest of line into image for the training data.
                for(int i = 1; i < line.length; i++){
                    image[i-1] = Integer.parseInt(line[i]);
                }
                trainingkeys[counter] = num;
                training[counter] = image;
                counter++;
            }
        }

        /*
         * Test Data. Unique from training data, and smaller in size, used to validate the accuracy of the trained model.
         */
        final int TEST_DATA_ROW_COUNT = 10000;
        final int TEST_DATA_COL_COUNT = 784;
        test = new double[TEST_DATA_ROW_COUNT][TEST_DATA_COL_COUNT];
        testKeys = new int[TEST_DATA_ROW_COUNT];

        URL testingURL = getClass().getClassLoader().getResource("mnist/mnist_test.csv");
        if (testingURL == null) {
            throw new FileNotFoundException("\"Resource URL for test data not found.");
        }
        File testingFile = new File(testingURL.toURI());

        try(CSVReader testingReader = new CSVReader(new FileReader(testingFile))) {
            String[] line;
            int counter = 0;
            while((line = testingReader.readNext()) != null && counter < test.length){
                Integer num = Integer.parseInt(line[0]);
                double[] image = new double[line.length - 1];
                for(int i = 1; i < line.length; i++){
                    image[i-1] = Integer.parseInt(line[i]);
                }
                testKeys[counter] = num;
                test[counter] = image;
                counter++;
            }
        }

        System.out.println("The data is finished being imported");
    }

    /**
     * Slapped together print for a single row. Should work in most terminals with monospace fonts.
     */
    public static void debugPrintLine(String[] image) {
        System.out.printf("Number: %s%n", image[0]);
        for (int i = 1; i < image.length; i++) {
            int num = Integer.parseInt(image[i]);
            if (num == 0) {
                System.out.print("0");
            } else {
                System.out.print("1");
            }


            if (i % 28 == 0) {
                System.out.println();
            }
        }

        // Add newline after entire row
        System.out.println();
    }

    public static void debugPrintLine(int key, double[] image) {
        System.out.printf("Number: %s%n", key);
        for (int i = 0; i < image.length; i++) {
            if (image[i] == 0) {
                System.out.print("0");
            } else {
                System.out.print("1");
            }


            if (i > 0 && i % 28 == 0) {
                System.out.println();
            }
        }

        // Add newline after entire row
        System.out.println();
    }



    public static HashMap<Integer, double[]> importTrainingData() throws FileNotFoundException, CsvValidationException, IOException{
        HashMap<Integer, double[]> data = new HashMap();
        String[] line;
        CSVReader reader = new CSVReader(new FileReader("mnist/mnist_train.csv"));
        while((line = reader.readNext()) != null){
            Integer num = Integer.parseInt(line[0]);
            double[] image = new double[line.length - 1];
            for(int i = 1; i < line.length; i++){
                image[i-1] = Integer.parseInt(line[i]);
            }
            data.put(num,image);
        }
        return data;
    }

    public static HashMap<Integer, double[]> importTestData() throws FileNotFoundException, CsvValidationException, IOException{
        HashMap<Integer, double[]> data = new HashMap();
        String[] line;
        CSVReader reader = new CSVReader(new FileReader("mnist/mnist_test.csv"));
        while((line = reader.readNext()) != null){
            Integer num = Integer.parseInt(line[0]);
            double[] image = new double[line.length - 1];
            for(int i = 1; i < line.length; i++){
                image[i-1] = Integer.parseInt(line[i]);
            }
            data.put(num,image);
        }
        return data;
    }

    public static void main(String[] args) throws Exception{
        System.out.println("Simple test to see if the data is read correctly");
        long time = System.currentTimeMillis();
        HashMap<Integer, double[]> data = importTrainingData();
        time = System.currentTimeMillis() - time;
        System.out.println("Done, took " + time / 1000d + " seconds.");
    }

}