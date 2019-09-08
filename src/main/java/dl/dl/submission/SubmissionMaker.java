package dl.dl.submission;


import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dl.dl.dataloading.datamaker.Denormalizer;
import dl.dl.network.DebugHelper;

/**
 *  Gets predictions from network and create submission csv.
 */
@Component
public class SubmissionMaker {
	private final static Logger logger = LoggerFactory.getLogger(SubmissionMaker.class);
	private Denormalizer denormalizer;
	public static final String SUBMISSION_PATH = "./data/submission/submission.csv";
	private static final String LINE_SEPARATOR_PROPERTY = "line.separator";
	
	@Autowired
	public SubmissionMaker(
				Denormalizer denormalizer) {
		this.denormalizer = denormalizer;
	}
	
	public void makeSubmission(INDArray features, MultiLayerNetwork net, Map<Integer, Double> idsToPrice) {
		logger.debug("Features length "+ features.length());
		
		// Get predictions from network
		INDArray predicted = net.output(features, false);
		
		// Denormalize predictions
		this.denormalizer.denormalize(predicted);
		
		logger.debug("Predicted length: " + predicted.length() + "  idToPrices length "+ idsToPrice.size());
		idsToPrice = this.fillInPredictions(idsToPrice, predicted);
	    
		logger.debug("Final submission result");
		DebugHelper.debugIdsToPrice(idsToPrice);
		
		// Write submission CSV file
		this.writeSubmissionCsv(idsToPrice);	
    
	}
	
	/**
	 * Put predictions into idsToPrice map.
	 */
	private Map<Integer,Double> fillInPredictions(Map<Integer,Double> idsToPrice, INDArray predicted){
		long c=0;
		for(Integer key : idsToPrice.keySet()) {
			logger.debug("idsToPrice key: "+ key);
			logger.debug("predicted value "+ predicted.getDouble(c));
			
			idsToPrice.put(key, predicted.getDouble(c));
			c++;
		}	    
		return idsToPrice;
	}
	
	private void writeSubmissionCsv(Map<Integer, Double> idsToPrice) {
		TreeMap<Integer, Double> sorted = new TreeMap<>(idsToPrice);
		
		String eol = System.getProperty(LINE_SEPARATOR_PROPERTY);

		try (Writer writer = new FileWriter(SUBMISSION_PATH)) {
		  for (Map.Entry<Integer, Double> entry : sorted.entrySet()) {
		    writer.append(entry.getKey().toString())
		          .append(',')
		          .append(entry.getValue().toString())
		          .append(eol);
		  }
		} catch (IOException e) {
		  logger.error("Error writing csv ", e);
		}
	}
	
}
