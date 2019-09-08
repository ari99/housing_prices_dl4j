package dl.dl.dataloading.datamaker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.transform.TransformProcess;
import org.datavec.api.util.ndarray.RecordConverter;
import org.datavec.api.writable.Writable;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dl.dl.App;
import dl.dl.dataloading.CsvReaderWriter;
import dl.dl.dataloading.MissingValuesImputer;
import dl.dl.dataloading.schema.SubmissionSchemaWrapper;
import dl.dl.dataloading.transform.SubmissionTransformCreator;

/**
 * Creates data submission input data.
 */
@Component
public class SubmissionDataMaker extends DataMaker {
	private final static Logger logger = LoggerFactory.getLogger(SubmissionDataMaker.class);
	private final static int NUMBER_SUBMISSION_RECORDS = 1460;
	
	@Autowired
	public SubmissionDataMaker(CsvReaderWriter csvReaderWriter, ObjectProvider<MissingValuesImputer> imputerProvider,
			SubmissionSchemaWrapper schemaWrapper, SubmissionTransformCreator transformCreator,  ArtifactRepository repo) {
		super(csvReaderWriter, imputerProvider, schemaWrapper, transformCreator, repo);
	}
	
	public INDArray makeSubmissionInput() {
		Pair<TransformProcess, RecordReader> inputData  = this.createInputData(
				App.SUBMISSION_INPUTS_PATH,
				App.SUBMISSION_SPARK_RESULTS_PATH
				);

		RecordReader transformProcessRecordReader = inputData.getValue();


		List<List<Writable>> featuresList = transformProcessRecordReader.next(NUMBER_SUBMISSION_RECORDS);
		try {
			transformProcessRecordReader.close();
		} catch (IOException e) {
			logger.error("Error closing record reader ", e);
		}
		INDArray featuresArray = RecordConverter.toMatrix(featuresList);

		return featuresArray;
	}
	
	/**
	 * The idsToPrice map is used to hold the list of ids in order and later
	 *   to fill in the predicted price.
	 * 
	 */
	public Map<Integer, Double> makeIdsToPriceMap(){
		Dataset<Row> trainData = this.csvReaderWriter.loadData(App.SUBMISSION_INPUTS_PATH);
		
		Dataset<Row> ids = trainData.select("Id");
		
		List<Double> salesPriceList = new ArrayList<>();
		List<String> columns = Arrays.asList(trainData.columns());
		
		// Create list of prices
		if(columns.contains("SalePrice")) {
			Dataset<Row> salesPrice = trainData.select("SalePrice");
			salesPriceList = salesPrice.as(Encoders.DOUBLE()).collectAsList();
		}
		
		// Create list of id's
		List<Integer> idsList = ids.as(Encoders.INT()).collectAsList();
		
		
		Map<Integer, Double> idsToPrice = this.matchPricesToIds(idsList, salesPriceList);
		
		return idsToPrice;
	}
	
	/**
	 *  Match list of prices to list of id's
	 */
	private Map<Integer, Double> matchPricesToIds(
				List<Integer> idsList, List<Double> salesPriceList){
		Map<Integer, Double> idsToPrice = new TreeMap<>();

		for (int i=0; i < idsList.size(); i++) {
			if(!salesPriceList.isEmpty()) {
				idsToPrice.put(idsList.get(i), salesPriceList.get(i));
			}else {
				// Put null and fill in later
				idsToPrice.put(idsList.get(i), null); 

			}
		}
		return idsToPrice;
	}
}
