package dl.dl.dataloading;


import org.apache.spark.ml.feature.Imputer;
import org.apache.spark.ml.feature.ImputerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;

import com.google.common.collect.ImmutableMap;

import dl.dl.dataloading.schema.SchemaWrapper;


/**
 * Replaces missing values with mean imputed values using Spark ML because 
 * 	I didn't see a good way to do it in DL4J.
 * 
 * Resources: 
 *   https://spark.apache.org/docs/2.2.0/ml-features.html#imputer
 *   https://spark.apache.org/docs/latest/quick-start.html	
 *
 */
public class MissingValuesImputer {
	
	
	private SchemaWrapper schema;
	
	/**
	 * @param schema Submission input and training input have different schemas
	 */
	public MissingValuesImputer(SchemaWrapper schema) {
		this.schema = schema;
	}

	

	
    public Dataset<Row> createImputedInputData(Dataset<Row> trainData) {
    	
    	trainData = trainData.na().replace(this.schema.getCombinedColumnAr(), ImmutableMap.of("NA", ""));
    	// The columns need to be double to do the imputation
    	trainData = this.convertColumnsToDouble(trainData);
    	
    	Dataset<Row> imputedData = this.imputeMissingValues(trainData);
    	
    	Dataset<Row> backToIntData = this.convertColumnsBackToInt(imputedData);
    	
    	return backToIntData;
    }
    
    private Dataset<Row> convertColumnsToDouble(Dataset<Row> trainData){

    	for(String columnName : this.schema.getCombinedColumnList()) {
    		trainData = trainData.withColumn(columnName, trainData.col(columnName).cast(DataTypes.DoubleType));
    	}
    	return trainData;
    }
 
    private Dataset<Row> convertColumnsBackToInt(Dataset<Row> trainData){

    	for(String columnName : this.schema.getCombinedColumnList()) {
    		trainData = trainData.withColumn(columnName, trainData.col(columnName).cast(DataTypes.IntegerType));
    	}
    	return trainData;
    }
    
    /**
     * Perform mean imputation.
     */
    private Dataset<Row> imputeMissingValues(Dataset<Row> trainData){
    	
    	Imputer imputer = new Imputer()
    			  .setInputCols(this.schema.getCombinedColumnAr())
    			  .setOutputCols(this.schema.getCombinedColumnAr())
    			  .setStrategy("mean");

    	ImputerModel model = imputer.fit(trainData);
    	Dataset<Row> transformedData = model.transform(trainData);
    	return transformedData;
    }
    

    
}
