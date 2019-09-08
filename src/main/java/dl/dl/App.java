package dl.dl;


/**
 * Interface for main entry point for the app, which is 
 * the run method. 
 */
public interface App {
	public static final int FULL_DATA_SIZE = 1460;
	public static final int TRAINING_BATCH_SIZE = 128;
	/** Data used to train and build the network. */
	public static final String TRAINING_INPUTS_PATH = "./data/train.csv";
	/** Path to write out data to after spark preprocessing */
	public static final String TRAINING_SPARK_RESULTS_PATH = "./data/trainSparkResult";
	/** Data used to generate predictions to submit to kaggle */
	public static final String SUBMISSION_INPUTS_PATH = "./data/test.csv";
	/** Path to write out data to after spark preprocessing */
	public static final String SUBMISSION_SPARK_RESULTS_PATH = "./data/submissionSparkResult";

	public abstract void run();
}
