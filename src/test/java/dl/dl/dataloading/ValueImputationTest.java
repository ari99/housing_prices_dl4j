package dl.dl.dataloading;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import dl.dl.DataTest;
import dl.dl.dataloading.schema.TrainSchemaWrapper;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
/**
 * Tests for replacing missing values with imputed ones.
 */
public class ValueImputationTest extends DataTest{
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ValueImputationTest.class);

	
	/** Confirm there are no more missing values after imputation. */
	@Test
	public void testValueImputation() {
		List<Row> listData  =inputData.collectAsList();
		this.calculateMissingValues(listData);
		logger.debug("Imputing data");
		MissingValuesImputer imputer = new MissingValuesImputer(new TrainSchemaWrapper());
		Dataset<Row> imputed = imputer.createImputedInputData(inputData);
		int result = this.calculateMissingValues(imputed.collectAsList());
		assertTrue(result == 0);
	}
	
	/** Calculate the number of values equal to "NA" or empty **/
	private int calculateMissingValues(List<Row> listData) {
		TrainSchemaWrapper wrapper = new TrainSchemaWrapper();
		List<String> intColumnList = wrapper.getIntColumnList();

		int numNa = 0;
		int numEmpty =0;
		for(Row row : listData) {
			for(String col : intColumnList) {
				String val = row.getAs(col).toString();
				if(val.isEmpty()) {
					numEmpty++;
				}
				if(val.equalsIgnoreCase("NA")) {
					numNa++;
				}
			}
		}
		logger.debug("Num NA "+ numNa);
		logger.debug("Num empty "+ numEmpty);
		
		return numNa + numEmpty;
	}
	
	
}
