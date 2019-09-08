package dl.dl;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dl.dl.dataloading.datamaker.TrainDataMaker;
import dl.dl.network.NetworkEvaluator;
import dl.dl.network.NetworkMaker;


/**
*
* The TrainerApp class creates and evaluates a neural network.
*
* Resources:
* https://github.com/deeplearning4j/oreilly-book-dl4j-examples/tree/master/dl4j-examples/src/main/java/org/deeplearning4j/examples/feedforward/regression
* https://github.com/deeplearning4j/oreilly-book-dl4j-examples/tree/master/dl4j-examples/src/main/java/org/deeplearning4j/examples/recurrent/regression
* https://machinelearningmastery.com/regression-tutorial-keras-deep-learning-library-python/
* https://towardsdatascience.com/deep-neural-networks-for-regression-problems-81321897ca33
* https://nodepit.com/node/org.knime.ext.dl4j.base.nodes.predict.feedforward.regression.FeedforwardRegressionPredictorNodeFactory
* https://deeplearning4j.org/docs/latest/datavec-transforms
* https://github.com/deeplearning4j/dl4j-examples/blob/master/datavec-examples/src/main/java/org/datavec/transform/debugging/PrintSchemasAtEachStep.java
* https://github.com/deeplearning4j/dl4j-examples/blob/master/datavec-examples/src/main/java/org/datavec/transform/basic/BasicDataVecExample.java
* https://github.com/eclipse/deeplearning4j/issues/5220
* https://github.com/deeplearning4j/DataVec/blob/master/datavec-api/src/main/java/org/datavec/api/records/reader/impl/transform/TransformProcessRecordReader.java
* https://stackoverflow.com/questions/44346756/transformprocess-transform-data-while-using-datasetiterator
*
*/
@Component
public class TrainerApp implements App {


	private NetworkMaker networkMaker;
	private TrainDataMaker trainDataMaker;
	private NetworkEvaluator evaluator;
	
	@Autowired
	public TrainerApp(
			NetworkMaker networkMaker, TrainDataMaker trainDataMaker,
			NetworkEvaluator evaluator) {
		this.networkMaker = networkMaker;
		this.trainDataMaker = trainDataMaker;
		this.evaluator = evaluator;
	}
	
	@Override
	public void run() {
		// Split the data into test and train data
		SplitTestAndTrain testAndTrain = this.trainDataMaker.createSplitTestAndTrain();
		DataSetIterator trainData = this.trainDataMaker.makeSplitTrainIterator(testAndTrain);  
		DataSetIterator testData = this.trainDataMaker.makeSplitTestIterator(testAndTrain);
		
    	MultiLayerNetwork net = this.networkMaker.makeNetwork();
    	networkMaker.trainNetwork(net, trainData);
    	evaluator.evalNetwork(net, testData);
		
	}


}
