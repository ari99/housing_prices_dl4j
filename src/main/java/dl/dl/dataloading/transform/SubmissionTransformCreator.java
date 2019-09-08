package dl.dl.dataloading.transform;

import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.analysis.DataAnalysis;
import org.datavec.api.transform.schema.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dl.dl.dataloading.schema.SubmissionSchemaWrapper;

/** Creates TransformProcess for submission data. */
@Component
public class SubmissionTransformCreator extends TransformCreator {

	@Autowired
	public SubmissionTransformCreator(SubmissionSchemaWrapper schemaWrapper) {
		super(schemaWrapper);
	}
	
	@Override
    public TransformProcess createFinalTransformProcess(Schema inputDataSchema, DataAnalysis analysis) {
    	TransformProcess transform = this.createBaseTransformBuilder(inputDataSchema, analysis)
    			.build();
    	return transform;
    }
	
}
