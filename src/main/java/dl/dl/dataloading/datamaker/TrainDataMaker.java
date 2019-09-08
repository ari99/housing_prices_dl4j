package dl.dl.dataloading.datamaker;

import java.util.List;

import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dl.dl.App;
import dl.dl.dataloading.CsvReaderWriter;
import dl.dl.dataloading.MissingValuesImputer;
import dl.dl.dataloading.schema.TrainSchemaWrapper;
import dl.dl.dataloading.transform.TrainTransformCreator;
/**
 * Creates training data.
 */
@Component
public class TrainDataMaker extends DataMaker{
	private static final double PERCENT_TRAIN = .65;
	
	@Autowired
	public TrainDataMaker(CsvReaderWriter csvReaderWriter, ObjectProvider<MissingValuesImputer> imputerProvider,
			TrainSchemaWrapper schemaWrapper, TrainTransformCreator transformCreator, ArtifactRepository repo) {
		super(csvReaderWriter, imputerProvider, schemaWrapper, transformCreator, repo);
		
	}
	
	public SplitTestAndTrain createSplitTestAndTrain() {
		DataSetIterator readAllIterator = this.createInputIterator(App.FULL_DATA_SIZE);
		DataSet allData = readAllIterator.next();
		SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(PERCENT_TRAIN);
		return testAndTrain;
	}
	
	/** Make iterator for the training data. */
	public DataSetIterator makeSplitTrainIterator(SplitTestAndTrain testAndTrain) {
		List<DataSet> trainingDataList = testAndTrain.getTrain().asList();
		DataSetIterator trainDataIterator = new ListDataSetIterator<DataSet>(trainingDataList, App.TRAINING_BATCH_SIZE);
		return trainDataIterator;
	}
	
	/** Make iterator for the test data. */
	public DataSetIterator makeSplitTestIterator(SplitTestAndTrain testAndTrain) {
		final List<DataSet> list = testAndTrain.getTest().asList();
		DataSetIterator testDataIterator = new ListDataSetIterator<DataSet>(list, App.TRAINING_BATCH_SIZE);
		return testDataIterator;
	}

}
