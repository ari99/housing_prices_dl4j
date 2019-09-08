package dl.dl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.junit.BeforeClass;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import dl.dl.dataloading.CsvReaderWriter;

/**
 * Superclass for other tests. Setups up data and mocks.
 * 
 */
@SpringBootTest(classes = TestConfig.class)
public class DataTest {
	
	@MockBean
	protected static CsvReaderWriter mockCsvReader;
	
	
	protected static Dataset<Row> inputData;
	protected static RecordReader trainRecordReader = new CSVRecordReader();
	protected static RecordReader submissionRecordReader = new CSVRecordReader();
	protected static String SUBMISSION_INPUT_PATH = "./data/unitTests/transformedSubmissionUnitTestData.csv";
	

	@BeforeClass
	public static void setupFixtureData() throws IOException, InterruptedException {
		// Prevents a hadoop filesystem error
		String absolutePath = FileSystems.getDefault().getPath("./data/hadoop").normalize().toAbsolutePath().toString();
		System.setProperty("hadoop.home.dir", absolutePath);
		
		String inputDataPath =  "./data/unitTests/unitTestData.csv";
		String transformedTrainInputDataPath = "./data/unitTests/transformedUnitTestData.csv";

		CsvReaderWriter readerWriter = new CsvReaderWriter();
		inputData = readerWriter.loadData(inputDataPath);
		
		File input = new File(transformedTrainInputDataPath);
		trainRecordReader.initialize(new FileSplit(input));
		
		File submissionInput = new File(SUBMISSION_INPUT_PATH);
		submissionRecordReader.initialize(new FileSplit(submissionInput));
		
	}
	
	protected void setupMockCsvReader() {
		when(mockCsvReader.loadData(anyString())).thenReturn(inputData);
		when(mockCsvReader.readCsv(anyString())).thenAnswer(new Answer<RecordReader>() {
			 		public RecordReader answer(InvocationOnMock invocation) throws Throwable {
			 			
			 			String arg = (String) invocation.getArguments()[0];
			 			if(arg.equals(App.SUBMISSION_SPARK_RESULTS_PATH)) {
				 			// If this a request for submission data load file without salesprice
			 				SubmissionAppTest.submissionRecordReader.reset();
			 				return SubmissionAppTest.submissionRecordReader;
			 			} else {
			 				// Load file that includes sale price
			 				SubmissionAppTest.trainRecordReader.reset();
			 				return SubmissionAppTest.trainRecordReader;
			 			}
				     }
				 });
	}
	
	
	
}
