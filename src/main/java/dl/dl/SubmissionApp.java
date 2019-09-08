package dl.dl;

import java.util.Map;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dl.dl.dataloading.datamaker.SubmissionDataMaker;
import dl.dl.dataloading.datamaker.TrainDataMaker;
import dl.dl.network.NetworkMaker;
import dl.dl.submission.SubmissionMaker;

/**
 * This is the main class which creates a network and a submission.
 */
@Component
public class SubmissionApp implements App {
	private SubmissionMaker submissionMaker;
	private NetworkMaker networkMaker;
	private TrainDataMaker trainDataMaker;

	private SubmissionDataMaker submissionDataMaker;
	
	/**
	 * Autowired contructor.
	 * 
	 */
	@Autowired
	public SubmissionApp(
			SubmissionMaker submissionMaker, 
			NetworkMaker networkMaker,
			TrainDataMaker trainDataMaker,
			SubmissionDataMaker submissionDataMaker) {
		this.submissionMaker = submissionMaker;
		this.networkMaker = networkMaker;
		this.trainDataMaker = trainDataMaker;
		this.submissionDataMaker = submissionDataMaker;
	}
	
	@Override
	public void run() {
		MultiLayerNetwork net = this.makeNetwork();
		// Create input to network
    	INDArray submissionInput = submissionDataMaker.makeSubmissionInput();
    	// Create map to hold results
    	Map<Integer, Double> idsToPrice = submissionDataMaker.makeIdsToPriceMap();
    	// Create submission file
    	this.submissionMaker.makeSubmission(submissionInput, net, idsToPrice); 
	}
	/**
	 * 
	 * @return A neural network trained using the full training data set.
	 */
	private MultiLayerNetwork makeNetwork() {
		
		DataSetIterator dataIterator = this.trainDataMaker.makeSubmissionNetworkTrainIterator();
		MultiLayerNetwork net = networkMaker.makeNetwork( );
    	networkMaker.trainNetwork(net, dataIterator);
    	
    	return net;
		
	}
	
	
  
    
}
