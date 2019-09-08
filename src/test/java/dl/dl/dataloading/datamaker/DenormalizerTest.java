package dl.dl.dataloading.datamaker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import dl.dl.DataTest;
import dl.dl.TestConfig;

import static org.junit.Assert.assertTrue;


/** Tests denormalization. */ 
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class DenormalizerTest extends DataTest{

	@Autowired
	private Denormalizer denorm;
	
	@Test
	public void testDenormilizer() {
		
		this.setupMockCsvReader();
		
		int numRows = 20;
		int numColumns = 1;
		
		// Create data to denormalize
        INDArray allPointNines = Nd4j.valueArrayOf(numRows, numColumns, .9);
        
        // Denorm data
		denorm.denormalize(allPointNines);
		
		// Confirm data was denormilized into a reasonable range for housing prices
		for(int i=0; i < 20; i++) {
			double val = allPointNines.getDouble(i, 0); 
			assertTrue(val > 2000);	
		}
		
	}
	
	
}
