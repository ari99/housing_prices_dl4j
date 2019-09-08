package dl.dl;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import dl.dl.submission.SubmissionMaker;

/** 
 * End to end tests for the SubmissionApp class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class SubmissionAppTest extends DataTest{
	
	@Autowired
	private SubmissionApp submissionApp;
	
	/** Run submission app and confirm a file was created with the correct
	 * 		number of predictions.
	 */
	@Test
	public void smokeTestSubmissionApp() throws IOException {
		this.setupMockCsvReader();
		
		this.deleteSubmissionFile();
		submissionApp.run();
		assertTrue(this.submissionFileExists());
		assertTrue(this.numInputsEqualsNumOutputs());
	}
	/** Delete the submission file if one was created from a previous run. */
	private void deleteSubmissionFile() {
        File file = new File(SubmissionMaker.SUBMISSION_PATH);
        file.delete();
	}
	
	private boolean submissionFileExists() {
        File file = new File(SubmissionMaker.SUBMISSION_PATH);
		return file.exists();
	}
	
	/**
	 * Confirm a prediction has been made for each input.
	 */
	private boolean numInputsEqualsNumOutputs() throws IOException {
		Path inputsPath = Paths.get(SUBMISSION_INPUT_PATH);
		long numberOfInputs = Files.lines(inputsPath).count();

		Path submissionPath = Paths.get(SubmissionMaker.SUBMISSION_PATH);
		long numberOfOutputs = Files.lines(submissionPath).count();

		return numberOfInputs == numberOfOutputs;
		
	}
	
}
