package dl.dl.dataloading.schema;

import org.datavec.api.transform.schema.Schema;
import org.springframework.stereotype.Component;

/** Creates Schema for the training input data */
@Component
public class TrainSchemaWrapper extends SchemaWrapper {

	@Override
	public Schema createSchema() {
    	return this.createBaseSchemaBuilder().addColumnInteger("SalePrice").build();
	}

}
