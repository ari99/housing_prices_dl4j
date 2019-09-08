package dl.dl.network;

import java.util.List;
import java.util.Map;

import org.datavec.api.transform.schema.Schema;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import dl.dl.dataloading.datamaker.Denormalizer;

/**
 * Helper methods for debugging.
 */
@Component
public class DebugHelper {
	private final static Logger logger = LoggerFactory.getLogger(DebugHelper.class);
    private Denormalizer denormalizer;
	
	public DebugHelper(Denormalizer denormalizer) {
		this.denormalizer = denormalizer;
	}
	
	public static void debugIdsToPrice(Map<Integer, Double> idsToPrice) {
		 int c=0;
			for(Integer key : idsToPrice.keySet()) {
				logger.debug(" ID " + key +  " PRICE "+ idsToPrice.get(key));
				if(c>10) {
					break;
				}
				c++;
				
			}
	}
	
    public void debugINDArrays(INDArray first, INDArray second ) {
    	double[] firstAr = first.toDoubleVector();
    	double[] secondAr = second.toDoubleVector();
    	
    	logger.debug("Lengths should be equal : "+ firstAr.length + "    " + secondAr.length);
    	for(int c=0; c < firstAr.length; c++ ) {
    		logger.debug(Double.toString(firstAr[c]));
    		logger.debug(Double.toString(secondAr[c]));
    		logger.debug("--");
    	}
    	
    	
    	
    }
    public void compareLabelsWithPredicted( INDArray labels, INDArray predicted) {
    	
    	  this.denormalizer.denormalize(labels);
    	  this.denormalizer.denormalize(predicted);
    	  
    	  double[] labelsAr = labels.toDoubleVector();
          double[] predictedAr = predicted.toDoubleVector();
         
          logger.debug("labels length " + labelsAr.length);
          logger.debug("predicted length " + predictedAr.length);
          
          for(int c=0; c<labelsAr.length; c++) {
          	double labelVal = labelsAr[c];
          	double predictedVal = predictedAr[c];
          	logger.debug("label "+ labelVal +  " predicted "+ predictedVal);
          }
    	
    	
    }
  
    
    public void debugPredictions(DataSet data, MultiLayerNetwork model) {

   	 INDArray features = data.getFeatures();
        INDArray labels = data.getLabels();
        INDArray predicts = model.output(features,false);
        double[] labelsd = labels.toDoubleVector();
        double[] predictedd = predicts.toDoubleVector();
       
        logger.debug("labels length " +labelsd.length);
        logger.debug("predictedd length " +predictedd.length);
        
        
        for(int c=0; c<labelsd.length; c++) {
        	double label = labelsd[c];
        	double predicted = predictedd[c];
        	logger.debug("label "+ label +  " predicted "+ predicted);
        }
        	
        	
    }
    

    public void debugData(DataSetIterator dataSetIterator, Schema finalSchema, MultiLayerNetwork model) {
    	List<String> columnNames = finalSchema.getColumnNames();    	
    	
    	dataSetIterator.reset();
    	DataSet data = dataSetIterator.next();
    	logger.debug("Num examples" + data.numExamples());
    	DataSet row = data.get(50);
    	logger.debug("COLUMNS  "+row.getColumnNames());
    	INDArray features = row.getFeatures();
    	logger.debug(features.toString());
    	double[] featuresDoubles  = features.toDoubleVector();
    	int c =0;
    	for(double val : featuresDoubles) {
    		logger.debug(columnNames.get(c) +" : "+val);
    		c++;
    	}
    	
    	
    	
    	INDArray labels = row.getLabels();
    	
    	logger.debug("Label " + labels);
    	
    	
         
         this.debugPredictions(data , model);      
         
    }
    
    
    
    
    
    
}
