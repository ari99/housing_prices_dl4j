package dl.dl.dataloading.datamaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import dl.dl.DataTest;
import dl.dl.TestConfig;

import static org.junit.Assert.assertTrue;

import java.util.Map;


/**
 *Test creation of idsToPrice map.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class IdsToPriceMapTest extends DataTest{
	
	
	@Autowired
	private SubmissionDataMaker dataMaker;
	
	@Test
	public void testMakeIdsToPriceMap() {
		this.setupMockCsvReader();
		
		Map<Integer, Double> idsToPrice = this.dataMaker.makeIdsToPriceMap();
		boolean allAboveTwoK = true;
		
		// Confirm all ids have an associated price
		for(Double price: idsToPrice.values()) {
			if(price == null || price < 2000 ) {
				allAboveTwoK = false;
			}
		}
		
		assertTrue(allAboveTwoK);
	}
	

	
}
