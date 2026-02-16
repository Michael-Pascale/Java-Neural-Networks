import org.ejml.simple.SimpleMatrix;
import java.util.Random;

public class MatrixTest {
    public static void main(String[] args){
        System.out.println("This program is testing the functionality of the matrix library for usage later");
        SimpleMatrix W = new SimpleMatrix(2, 3);
        W.set(0, 0, 1);
        System.out.println(W);
        Random r = new Random();
        for(int i = 0; i < W.numRows();i++){
            for(int j = 0; j < W.numCols(); j++){
                double digit = r.nextDouble()*10;
                W.set(i,j,digit);
                System.out.println("i:" + i + ", j:" + j + ", digit:" + digit);
            }
        }
        System.out.println(W);
        SimpleMatrix x = new SimpleMatrix(3,1);
        double[] input = {0d,1d,0d};
        x.setColumn(0, 0, input);
        System.out.println(x);
        System.out.println(W.mult(x));
        double[][] data = {{1},{1},{1}};
        SimpleMatrix b = new SimpleMatrix(data);
        System.out.println(b);
        System.out.println(W.mult(x.plus(b)));
        W.set(0,2,data[0][0]);
        W.set(1,2,data[1][0]);
        double[] input2 = {0,1};
        x.setColumn(0, 0, new double[]{input2[0],input2[1],1});
        System.out.println(W.mult(x));
    }
}