package dl.dl.dataloading.datamaker;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.transform.TransformProcess;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.primitives.Pair;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dl.dl.App;
import dl.dl.dataloading.CsvReaderWriter;
import dl.dl.dataloading.MissingValuesImputer;
import dl.dl.dataloading.schema.TrainSchemaWrapper;
import dl.dl.dataloading.transform.SubmissionTransformCreator;
/**
 * Create data for use in denormilization. This has to be the same data used to normilize the input.
 */
@Component
public class DenormilizationDataMaker extends DataMaker{

	@Autowired
	public DenormilizationDataMaker(CsvReaderWriter csvReaderWriter,
			ObjectProvider<MissingValuesImputer> imputerProvider,
			TrainSchemaWrapper schemaWrapper,
			SubmissionTransformCreator transformCreator, //We dont want to normilize price so we use the submission TransformProcess
			ArtifactRepository repo) {
		super(csvReaderWriter, imputerProvider, schemaWrapper, transformCreator, repo);
	}
	public DataSetIterator makeTrainingDenormilizationInputData() {
		return this.makeDenormilizationInputData(App.TRAINING_INPUTS_PATH,
				App.TRAINING_SPARK_RESULTS_PATH, App.FULL_DATA_SIZE);
	}
	
	public DataSetIterator makeDenormilizationInputData(String inputPath,
			String sparkResultPath, int numInputs) {
		Pair<TransformProcess, RecordReader> inputData  = this.createInputData(
				inputPath,
				sparkResultPath
				);

		 return this.buildIterator(inputData, numInputs);
	}

}
