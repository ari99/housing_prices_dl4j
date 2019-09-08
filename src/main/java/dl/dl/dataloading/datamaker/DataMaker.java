package dl.dl.dataloading.datamaker;

import java.io.File;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.transform.TransformProcessRecordReader;
import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.analysis.DataAnalysis;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.transform.ui.HtmlAnalysis;
import org.datavec.local.transforms.AnalyzeLocal;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.primitives.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;

import dl.dl.App;
import dl.dl.dataloading.CsvReaderWriter;
import dl.dl.dataloading.MissingValuesImputer;
import dl.dl.dataloading.schema.SchemaWrapper;
import dl.dl.dataloading.transform.TransformCreator;

/**
 * Creates input data for networks.
 */
public class DataMaker {
	private final static Logger logger = LoggerFactory.getLogger(DataMaker.class);
	private final static String ANALYSIS_PATH = "./analysis.html";
	
	protected CsvReaderWriter csvReaderWriter;
	private MissingValuesImputer imputer;
	private TransformCreator transformCreator;
	private SchemaWrapper schemaWrapper;
	private ArtifactRepository repo;

	
	public DataMaker(CsvReaderWriter csvReaderWriter,
			ObjectProvider<MissingValuesImputer> imputerProvider,
			SchemaWrapper schemaWrapper,
			TransformCreator transformCreator,
			ArtifactRepository repo) {
		this.csvReaderWriter = csvReaderWriter;
		
		this.imputer = imputerProvider.getObject(schemaWrapper);

		this.transformCreator = transformCreator;

		this.schemaWrapper = schemaWrapper;
		this.repo = repo;


	}
	
	/**
	 * Prepares the input data for training or getting results from a neural network.
	 */
	protected Pair<TransformProcess, RecordReader> createInputData(
			String inputPath,
			String sparkResultPath
			) {
			
		this.prepareImputedData(inputPath, sparkResultPath);

		RecordReader sparkTransformedReader = this.csvReaderWriter.readCsv(sparkResultPath);
		
		
		TransformProcess convertToDoubleTransformProcess = 
				this.transformCreator.createTransformToDoubleTransformProcess(this.schemaWrapper.getSchema());

		Schema convertedToDoubleSchema = convertToDoubleTransformProcess.getFinalSchema();	

		//This hasn't done the conversion yet
		DataAnalysis analysis = this.createInputAnalysis(convertedToDoubleSchema, sparkTransformedReader);
		// You either need to reset or call initialize again because
		// DataAnalysis runs through the recordReader and next will throw an error
		sparkTransformedReader.reset();


		RecordReader convertToDoubleRecordReader = new TransformProcessRecordReader(sparkTransformedReader,
				convertToDoubleTransformProcess);

		TransformProcess finalTransformProcess = this.transformCreator.createFinalTransformProcess(
				convertedToDoubleSchema, analysis);
		
		this.repo.setFinalTrainSchema(finalTransformProcess.getFinalSchema());
		
		RecordReader transformProcessRecordReader = new TransformProcessRecordReader(
				convertToDoubleRecordReader, finalTransformProcess);
		
		
		return new Pair<TransformProcess, RecordReader>(finalTransformProcess,transformProcessRecordReader);

	}
	
	/**
	 * Do the mean imputation and write out the result.
	 */
	private void prepareImputedData(String inputPath, String sparkResultPath) {
		//Read input
		Dataset<Row> trainData = this.csvReaderWriter.loadData(inputPath);
		Dataset<Row> transformedData  = this.imputer.createImputedInputData(trainData);
				
		//Write out data including imputed data
		this.csvReaderWriter.writeSparkResult(transformedData, sparkResultPath);
	}
	
	protected DataSetIterator createInputIterator(int batchSize) {
		Pair<TransformProcess, RecordReader> inputData  = this.createInputData(
				App.TRAINING_INPUTS_PATH,
				App.TRAINING_SPARK_RESULTS_PATH
				);
		 return this.buildIterator(inputData, batchSize);
	}
	
	/**
	 *
	 * @return DataSetIterator built from a TransformProcess and RecordReader
	 */
	protected DataSetIterator buildIterator(Pair<TransformProcess, RecordReader> inputData, int batchSize) {
		RecordReader transformProcessRecordReader = inputData.getValue();
		TransformProcess finalTransformProcess = inputData.getKey();

		Schema finalSchema = finalTransformProcess.getFinalSchema();
		
		int labelIndex = finalSchema.getIndexOfColumn("SalePrice");
		logger.debug("Label index "+ labelIndex);
		
		DataSetIterator readAllIterator = 
				new RecordReaderDataSetIterator.Builder(transformProcessRecordReader, batchSize)
				.regression(labelIndex)
				.build();

		return readAllIterator;   
	}
	

	public DataSetIterator makeSubmissionNetworkTrainIterator() {
		return this.createInputIterator(App.TRAINING_BATCH_SIZE);
	}

	

	public DataAnalysis createInputAnalysis(Schema convertedToDoubleSchema,
			RecordReader sparkTransformedReader)  {
		DataAnalysis analysis = AnalyzeLocal.analyze(
				convertedToDoubleSchema,
				sparkTransformedReader);
		try {
			HtmlAnalysis.createHtmlAnalysisFile(analysis, new File(ANALYSIS_PATH));
		} catch (Exception e) {
			logger.error("Error creating analysis file ", e);
		}
		return analysis;

	}



}
