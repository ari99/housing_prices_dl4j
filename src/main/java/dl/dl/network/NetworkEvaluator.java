package dl.dl.network;


import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.eval.RegressionEvaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Methods to help with the evaluation of a neural network.
 */
@Component
public class NetworkEvaluator {
	private final static Logger logger = LoggerFactory.getLogger(NetworkEvaluator.class);
	DebugHelper debugHelper;
	
	@Autowired
	public NetworkEvaluator(DebugHelper debugHelper) {
		this.debugHelper = debugHelper;
	}
    public void evalNetwork(MultiLayerNetwork net, DataSetIterator testDataSetIterator) {
    	RegressionEvaluation eval = new RegressionEvaluation(1);
        while(testDataSetIterator.hasNext()){
            DataSet testData = testDataSetIterator.next();
            INDArray features = testData.getFeatures();
            INDArray labels = testData.getLabels();
            INDArray predicted = net.output(features,false);
            
            //this denormalizes both arrays
            this.debugHelper.compareLabelsWithPredicted(labels, predicted);
            eval.eval(labels, predicted);            
        }
        
        logger.debug(eval.stats());

    }
    
    
  

    /**
     * Reference:
     * https://github.com/eclipse/deeplearning4j/issues/3346
     */
    public void setupUi(MultiLayerNetwork net) {
        //Initialize the user interface backend
        UIServer uiServer = UIServer.getInstance();
        
        //Configure where the network information (gradients, score vs. time etc) is to be stored. Here: store in memory.
        StatsStorage statsStorage = new InMemoryStatsStorage(); 
        
        
        //Attach the StatsStorage instance to the UI: this allows the contents of the StatsStorage to be visualized
        uiServer.attach(statsStorage);
        
        
        //Then add the StatsListener to collect this information from the network, as it trains
        net.addListeners(new StatsListener(statsStorage));
        
    }
    
    
    
    
}
