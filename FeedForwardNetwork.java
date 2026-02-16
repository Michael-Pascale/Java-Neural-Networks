import java.util.Random;

import org.ejml.dense.row.MatrixFeatures_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.ejml.sparse.csc.CommonOps_DSCC;

public class FeedForwardNetwork{
    private SimpleMatrix weightsIH, weightsHO, biasH, biasO;
    private double learningRate;

    public FeedForwardNetwork(int input_len, int hidden_len, int output_len){
        Random rand = new Random();
        weightsIH = SimpleMatrix.random_DDRM(hidden_len, input_len,-1,1,rand);
        weightsHO = SimpleMatrix.random_DDRM(output_len, hidden_len,-1,1,rand);
        biasH = SimpleMatrix.random_DDRM(hidden_len, 1, -1, 1, rand);
        biasO = SimpleMatrix.random_DDRM(output_len, 1, -1, 1, rand);

        learningRate = 0.1;
    }

    public double[] feedForward(double[] input){
        //convert input into a matrix
        SimpleMatrix m = new SimpleMatrix(input.length, 1);
        m.setColumn(0,0, input);

        //multiply input by the hidden weights, add the bias
        SimpleMatrix hidden = weightsIH.mult(m).plus(biasH);
        hidden = Sigmoid(hidden);

        //multiply result by output weights, and add bias
        SimpleMatrix output = weightsHO.mult(hidden).plus(biasO);
        output = Sigmoid(output);

        return FeedForwardNetwork.MatrixToArray(output);
    }

    //uses stochastic gradient descent, rather than something like the mean squared error.
    public void train(double[] inputs, double[] expected){
        //need code from feed forward so we can access the data at the intermediate steps

        //convert input into a matrix
        SimpleMatrix m = new SimpleMatrix(inputs.length, 1);
        m.setColumn(0,0, inputs);

        //multiply input by the hidden weights, add the bias
        SimpleMatrix hidden = weightsIH.mult(m).plus(biasH);
        hidden = Sigmoid(hidden);
        

        //multiply result by output weights, and add bias
        SimpleMatrix output = weightsHO.mult(hidden).plus(biasO);
        output = Sigmoid(output);


        double[] outputArr = MatrixToArray(output);
        //make sure dimensions of matrix are correct(this is one row by x columns, we may need x rows by one column)
        SimpleMatrix guess = new SimpleMatrix(outputArr.length,1);
        guess.setColumn(0, 0, outputArr);
        SimpleMatrix answer = new SimpleMatrix(expected.length,1);
        answer.setColumn(0, 0, expected);

        //calculate the error
        SimpleMatrix outputErrors = answer.minus(guess);

        //calculate the gradients
        SimpleMatrix outputGradients = SigmoidDerivative(output);
        outputGradients = outputGradients.elementMult(outputErrors);
        outputGradients = outputGradients.scale(learningRate);

        //calculate deltas
        SimpleMatrix weightsHODeltas = outputGradients.mult(hidden.transpose());


        //change weights according to the delta
        this.weightsHO = weightsHO.plus(weightsHODeltas);
        //adjust bias by the gradient
        this.biasO = biasO.plus(outputGradients);

        //repeat for the hidden layer

        //calculate the hidden errors
        SimpleMatrix hiddenErrors = weightsHO.transpose().mult(outputErrors);

        //calculate the gradient
        SimpleMatrix hiddenGradients = SigmoidDerivative(hidden);
        hiddenGradients = hiddenGradients.elementMult(hiddenErrors);
        hiddenGradients = hiddenGradients.scale(learningRate);

        SimpleMatrix weightsIHDeltas = hiddenGradients.mult(m.transpose());

        this.weightsIH = weightsIH.plus(weightsIHDeltas);
        this.biasH = biasH.plus(hiddenGradients);
    }

    private static SimpleMatrix SigmoidDerivative(SimpleMatrix m){
        SimpleMatrix output = new SimpleMatrix(m);
        for(int i = 0; i < output.numRows(); i++){
            for(int j = 0; j < output.numCols(); j++){
                output.set(i,j, output.get(i,j) * (1d-output.get(i,j)));
            }
        }
        return output;
    }

    private static double Sigmoid(double input){
        double output = 1.0 / (1 + Math.exp(-input));
        return output;
    }

    private static SimpleMatrix Sigmoid(SimpleMatrix m){
        SimpleMatrix output = new SimpleMatrix(m);
        for(int i = 0; i < output.numRows(); i++){
            for(int j = 0; j < output.numCols(); j++){
                double s = Sigmoid(output.get(i,j));
                output.set(i,j,s);
            }
        }
        return output;
    }

    private static double[] MatrixToArray(SimpleMatrix m){
        double[] mat = new double[m.numCols() * m.numRows()];
        int counter = 0;
        for(int i = 0; i < m.numRows(); i++){
            for(int j = 0; j < m.numCols(); j++){
                mat[counter] = m.get(i, j);
                counter++;
            }
        }
        return mat;
    }
}