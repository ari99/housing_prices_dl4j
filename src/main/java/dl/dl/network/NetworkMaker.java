package dl.dl.network;


import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dl.dl.dataloading.datamaker.ArtifactRepository;

/**
 * Configures and trains a network.
 */
@Component
public class NetworkMaker {
	private final static Logger logger = LoggerFactory.getLogger(NetworkMaker.class);
	
	private ArtifactRepository repo;
	
	@Autowired
	public NetworkMaker(ArtifactRepository repo) {
		this.repo = repo;
	}
	
	public MultiLayerNetwork makeNetwork() {
		MultiLayerConfiguration conf = this.makeConfiguration();
		MultiLayerNetwork net = new MultiLayerNetwork(conf);
		return net;
	}
	
	/**
	 * Conducts training epochs. 
	 */
	public void trainNetwork(MultiLayerNetwork net, DataSetIterator dataSetIterator ) {
		int numEpochs = 800;
		net.init();
	    net.addListeners(new ScoreIterationListener(10));
        for( int i=0; i<numEpochs; i++ ){
        	logger.debug("=========Epoch number: "+i);
            net.fit(dataSetIterator); // the fit method takes care of running batches and resetting the iterator          
        }
	}
	
	/**
	 * Configure network. 
	 */
	private MultiLayerConfiguration makeConfiguration() {
		int numInput = this.repo.getFinalTrainSchema().numColumns()-1;
		int numHidden  = numInput;
		int numOutputs = 1;
		
		MultiLayerConfiguration conf =
				 new NeuralNetConfiguration.Builder()
				//.l2(0.0005)
				.seed(145)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
				//.gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue)
				.updater(new Adam(0.1)) 
				//.weightInit(WeightInit.XAVIER)
				//.updater(new Nesterovs(.0001, 0.9))
				//.dropOut(.9)
                .list()
                .layer(0, new DenseLayer.Builder().nIn(numInput).nOut(numHidden) // -10 -20 -20 -30 seems good
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new DenseLayer.Builder().nIn(numHidden).nOut(numHidden-10)
                        .activation(Activation.RELU)
                        .build())
                .layer(2, new DenseLayer.Builder().nIn(numHidden-10).nOut(numHidden-20)
                        .activation(Activation.RELU)
                        .build())
                .layer(3, new DenseLayer.Builder().nIn(numHidden-20).nOut(numHidden-20)
                        .activation(Activation.RELU)
                        .build())
                .layer(4, new DenseLayer.Builder().nIn(numHidden-20).nOut(numHidden-30)
                        .activation(Activation.RELU)
                        .build())
                .layer(5, new DenseLayer.Builder().nIn(numHidden-30).nOut(numHidden-40)
                        .activation(Activation.RELU)
                        .build())
                .layer(6, new DenseLayer.Builder().nIn(numHidden-40).nOut(numHidden-50)
                        .activation(Activation.RELU)
                        .build())
                .layer(7, new DenseLayer.Builder().nIn(numHidden-50).nOut(numHidden-60)
                        .activation(Activation.RELU)
                        .build())
                .layer(8, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)//mse
                        .activation(Activation.IDENTITY) 
                        .nIn(numHidden-60).nOut(numOutputs).build())
                .pretrain(false).backprop(true)
				.build();
		
		
		return conf;
	}
}


/*

Network architecture - Multilayer perceptron for regression

Output layer  -
		Loss function - MSE (mean squared error) or L2 (sume of squared errors)
		Activation function - identity (linear) output function
		Single neuron for regression
Input layer - same number of input
Hidden layer count - 3 to 4. More hidden layers are useful with more data. 
					With less data you are more likely to overfit.
Neuron count per layer - More neurons can lead to overfitting. 
						  - Progressive layers should have declining neuron count usually
						  - No layer should have less than .25 the number of input layer's nodes
						  - Need right combo of layer size, regularization, and amount of data
Weight initialization - Biases can generally be initialized to zero
					   - Use WeightInit.XAVIER to break symmetry between hidden units
Learning rate - Begin with .001 and make smaller if the learning process diverges
			  - Use Adam learning rate method
			  - Use Parameter ratios graph to set the learning rate
Momentum - Helps learning process escape local minima 
			- usually around .9 or .5 annealing to .9
			
Sparsity - set between .01 and .000000001 - Use the histogram of mean activities of the hidden units to set
Optimization - For smaller data sets use second-order optimization methods and set the batch size to the full dataset
			 - For larger datasets use SGB with a mini-batch setting
			 - Most people use SGD with (momentum, adagrad, RMSProp etc)
Batch size - Training time will decrease as batch size increases until batch size gets too large then
				training time will increase as batch size increases. Use multiples of 32 for batch size. 32 to 256
				is common for CPU training.
Regularization - L2 regularization is more frequently used
Dropout - on input and hidden layers, not output. Avoid using dropout on the first layer (disable it by setting
					dropout(0.0) on that layer)
		- set to .5 probability 
		- often combined with regularization

To Address overfitting: 
	- Increased Regularization(L1, L2, Dropout)
	- Early Stopping
	- A lalrger training dataset
	- A smaller network
	
	Regression:
	https://github.com/deeplearning4j/oreilly-book-dl4j-examples/blob/master/dl4j-examples/src/main/java/org/deeplearning4j/examples/feedforward/regression/RegressionSum.java
	Has evaluation:
	https://github.com/deeplearning4j/oreilly-book-dl4j-examples/blob/master/dl4j-examples/src/main/java/org/deeplearning4j/examples/feedforward/classification/MLPClassifierSaturn.java
	
	
	
*/



