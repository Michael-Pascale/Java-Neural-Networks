import java.io.IOException;

public class NeuralNetAutomaton {
    private Perceptron p0,p1,p2,p3;

    public NeuralNetAutomaton() throws IOException{
        p0 = Perceptron.readFromFile("ORGATE3Input0.p");
        p1 = Perceptron.readFromFile("PerceptronNOTXANDY.p");
        p2 = Perceptron.readFromFile("PerceptronNOTXANDNOTYANDZ.p");
        p3 = Perceptron.readFromFile("PerceptronXANDNOTYANDNOTZ.p");
    }

    public int fire(String parents){
        double[] data = {1.0,Integer.parseInt(parents.charAt(0) + ""),
            Integer.parseInt(parents.charAt(1) + ""),
            Integer.parseInt(parents.charAt(2) + "")};
        double[] d1 = {data[0],data[1],data[2]};
        int r1 = p1.fire(d1) == -1 ? 0:1;
        int r2 = p2.fire(data) == -1 ? 0:1;
        int r3 = p3.fire(data) == -1 ? 0:1;
        double[] input = {1,r1,r2,r3};
        int r0 = p0.fire(input);
        return r0 == -1 ? 0:1;
    }

    public String generation(String parents){
        String children = "";
        //children += Integer.toString(fire("0" + parents.substring(0,2)));
        children += "0";
        for(int i = 1; i < parents.length() - 1; i++){
            children += Integer.toString(fire(parents.substring(i-1, i+2)));
        }
        children += "0";
        //children += Integer.toString(fire(parents.substring(parents.length()-2,parents.length()) + "0"));
        return children;
    }
}