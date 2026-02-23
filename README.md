# Java-Neural-Networks
An old Project of mine to learn AI and neural networks by writing them from scratch based on the fundamental math. 
This utilizes ejml for the matrices used in the network. This library is not specifically for
AI or large scale multiplication like this, so it is CPU bound rather than GPU.

## Usage
There are several runners in the project that are used for testing/demonstration purposes only.
The pom does not include them in the final build, nor any files they may reference.

If using as a library in another project, do not try to run the runners or MnistImporter(See Future Enhancements below)

### FFRunner
Demonstrates the feed forward neural network using the MNist Dataset.
Feel free to modify the number of iterations to train more/less

### PerceptronRunner
Demonstrates a single perceptron. Perceptrons are capable of learning anything that can map to a linear function.

### Sigmoid Runner
Another perceptron demonstration, this time using a sigmoid function. This makes them a better fit for problems with
a non-linear space.

## Setup
1. Clone repository
2. Setup *resources/mnist* folder under *src/main*
3. Locate and download mnist data set. Ensure formatted as instructed in MnistImport.java 

Instructions for setup to run yourself are WIP. Stay tuned for updated instructions.

## Build
```bash
mvn package
```
This will give you a jar file you can use as a dependency.

## Future enhancements
The current build includes the demo package, as well as any files you have in the resources directory of your project.
The "Runner" classes in this repo cannot be run without those files, and aren't necessary in the final build for other projects. 
Moving those to a separate repo and allowing this project purely for the util objects would be better.
