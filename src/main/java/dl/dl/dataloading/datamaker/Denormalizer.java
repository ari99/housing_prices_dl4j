

package dl.dl.dataloading.datamaker;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The predictions from the neural network are normalized,so this class is used 
 *   to denormalize them.
 */
@Component
public class Denormalizer {
	private final static Logger logger = LoggerFactory.getLogger(Denormalizer.class);
	
	private DenormilizationDataMaker dataMaker;
	
	@Autowired
	public Denormalizer(DenormilizationDataMaker dataMaker) {
		this.dataMaker = dataMaker;
	}
	

	public void denormalize(INDArray predicted) {
		DataSet trainingData = dataMaker.makeTrainingDenormilizationInputData().next();
		NormalizerStandardize normalizer = new NormalizerStandardize();
		
        normalizer.fitLabel(true);
    	normalizer.fit(trainingData);
    	INDArray mean = normalizer.getLabelMean();
    	INDArray standardDeviation = normalizer.getLabelStd();
    	logger.debug("Mean " + mean + "  standard dev " + standardDeviation);
    	
    	// Relies on pass by reference (Java passes the location of the object actually)
    	normalizer.revertLabels(predicted);	
	}
	

}
