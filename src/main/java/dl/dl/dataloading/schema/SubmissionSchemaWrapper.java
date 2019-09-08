package dl.dl.dataloading.schema;

import org.datavec.api.transform.schema.Schema;
import org.springframework.stereotype.Component;

/** Creates Schema for the submission input data */
@Component("submissionSchemaWrapper")
public class SubmissionSchemaWrapper extends SchemaWrapper {

	@Override
	public Schema createSchema() {
    	return this.createBaseSchemaBuilder().build();
	}

}