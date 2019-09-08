package dl.dl.dataloading.datamaker;

import org.datavec.api.transform.schema.Schema;
import org.springframework.stereotype.Component;

/**
 * This class holds artifacts created as part of the data loading process.
 * It can be expanded to throw exceptions or create the artifact if it does not yet exist.
 * Has to remain the Singleton scope, default in Spring.
 */
@Component
public class ArtifactRepository {
	private Schema finalTrainSchema;
	public Schema getFinalTrainSchema() {
		return this.finalTrainSchema;
	}
	
	public void setFinalTrainSchema(Schema finalSchema) {
		this.finalTrainSchema = finalSchema;
	}	
}
