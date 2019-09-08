package dl.dl.dataloading;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.datavec.api.split.FileSplit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Reads and writes csv files using spark or DL4J.
 */
@Component
public class CsvReaderWriter {
	private final static Logger logger = LoggerFactory.getLogger(CsvReaderWriter.class);
	
	public Dataset<Row> loadData(String originalDataPath){
		SparkSession spark = SparkSession.builder()
				.appName("myapp")
				.master("local")
				.getOrCreate();

		Dataset<Row> trainData = spark.read()
				.format("csv")
				.option("sep", ",")
				.option("inferSchema", "true")
				.option("header", "true")
				.load(originalDataPath);
		
		return trainData;
	}

	public void writeSparkResult(Dataset<Row> trainData, String sparkResultPath)  {
		File file = new File(sparkResultPath);
		if(!FileSystemUtils.deleteRecursively(file)) {
			logger.error("Problem occurs when deleting the directory ");
		}
		
		trainData.coalesce(1).write().format("csv")
			.option("header", false)
			.option("delimeter", ",")
			.save(sparkResultPath);
		
		this.deleteExtraFiles(sparkResultPath);
	}
	
	/**
	 * DL4J's FileSplit class doesn't properly handle spark generated directories
	 * so delete the extra files.
	 * 
	 */
	private void deleteExtraFiles(String sparkResultPath) {
		Path path  = Paths.get(sparkResultPath);

		try (DirectoryStream<Path> newDirectoryStream = 
				Files.newDirectoryStream(path, "*"+".crc")) {
			for (final Path newDirectoryStreamItem : newDirectoryStream) {
				Files.delete(newDirectoryStreamItem);
			}
			Path toDelete = Paths.get(
					sparkResultPath+"/_SUCCESS");
			Files.delete(toDelete);

		} catch (final Exception e) { 
			logger.error("Error deleting extra files ", e);
		}
		
		
	}

	public RecordReader readCsv(String path) {
		RecordReader recordReader  = new CSVRecordReader();
		File input = new File(path);
		//This will not work unless you delete the two extra files created by spark in that dir
		try {
			recordReader.initialize(new FileSplit(input));
		} catch (IOException | InterruptedException e) {
			logger.error("Error reading CSV ", e);
		}
		
		return recordReader;
	}


}
