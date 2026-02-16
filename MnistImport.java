import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Arrays;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
public class MnistImport {
    public double[][] training, test;
    public int[] trainingkeys, testKeys;
    public MnistImport() throws FileNotFoundException, CsvValidationException, IOException{
        trainingkeys = new int[50000];
        training = new double[50000][784];
        String[] line;
        CSVReader reader = new CSVReader(new FileReader("mnist/mnist_train.csv"));
        int counter = 0;
        while((line = reader.readNext()) != null){
            Integer num = Integer.parseInt(line[0]);
            double[] image = new double[line.length - 1];
            for(int i = 1; i < line.length; i++){
                image[i-1] = Integer.parseInt(line[i]);
            }
            trainingkeys[counter] = num;
            training[num] = image;
        }

        test = new double[10000][784];
        testKeys = new int[10000];
        reader = new CSVReader(new FileReader("mnist/mnist_train.csv"));
        counter = 0;
        while((line = reader.readNext()) != null){
            Integer num = Integer.parseInt(line[0]);
            double[] image = new double[line.length - 1];
            for(int i = 1; i < line.length; i++){
                image[i-1] = Integer.parseInt(line[i]);
            }
            testKeys[counter] = num;
            test[num] = image;
        }

        System.out.println("The data is finished being imported");
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