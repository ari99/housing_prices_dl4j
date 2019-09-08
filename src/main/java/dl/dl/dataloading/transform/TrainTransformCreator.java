package dl.dl.dataloading.transform;

import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.analysis.DataAnalysis;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.transform.transform.normalize.Normalize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dl.dl.dataloading.schema.TrainSchemaWrapper;


/** Creates TransformProcess for training data. */
@Component
public class TrainTransformCreator extends TransformCreator{

	@Autowired
	public TrainTransformCreator(TrainSchemaWrapper schemaWrapper) {
		super(schemaWrapper);
	}
	
	@Override
    public TransformProcess createFinalTransformProcess(Schema inputDataSchema, DataAnalysis analysis) {
        
    	TransformProcess transform = this.createBaseTransformBuilder(inputDataSchema, analysis)
    			.normalize("SalePrice", Normalize.Standardize, analysis)
    			.build();
    	
    	return transform;
    }

}
