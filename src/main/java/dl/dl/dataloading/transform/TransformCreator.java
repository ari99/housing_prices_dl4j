package dl.dl.dataloading.transform;

import java.util.List;

import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.analysis.DataAnalysis;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.transform.transform.normalize.Normalize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dl.dl.dataloading.schema.SchemaWrapper;

/** Configure Transform Processes */
public abstract class TransformCreator {
	private final static Logger logger = LoggerFactory.getLogger(TransformCreator.class);
	private SchemaWrapper schemaWrapper; 
	
	public TransformCreator(SchemaWrapper schemaWrapper) {
		this.schemaWrapper = schemaWrapper;
	}
	
	/** 
	 * Currently not being used. Create a transform process with many
	 *    columns removed. 
	 **/
	private TransformProcess.Builder createBaseTransformBuilderMinimal(Schema inputDataSchema, DataAnalysis analysis){
		TransformProcess.Builder builder = new TransformProcess.Builder(inputDataSchema)
				.removeColumns("Id", "MSSubClass", "MSZoning", "Street", "Alley",
						"LotShape", "LandContour", "Utilities", "LotConfig", "LandSlope",
						"HouseStyle", "RoofStyle", "RoofMatl", "Exterior1st", "Exterior2nd", 
						"MasVnrType", "BsmtExposure", "BsmtFinType1", "BsmtFinType2",
						"HeatingQC", "Functional", "FireplaceQu", "GarageType",
						"GarageFinish", "GarageCond", "PavedDrive", "PoolQC", "Fence",
						"MiscFeature","GarageYrBlt", "MasVnrArea", "BsmtFinSF1",
						"BsmtFinSF2", "BsmtUnfSF", "LowQualFinSF", "GrLivArea",
						"WoodDeckSF", "OpenPorchSF", "EnclosedPorch","GarageArea",
						"Electrical","Heating", "CentralAir", "SaleType", "Condition2",
						"LotFrontage","BsmtCond", "ExterCond", "GarageQual"
						)
				.categoricalToOneHot("Neighborhood")
				.categoricalToOneHot("Condition1")
                .categoricalToOneHot("BldgType")
                .categoricalToOneHot("ExterQual")
                .categoricalToOneHot("Foundation")
                .categoricalToOneHot("BsmtQual")
    			.categoricalToOneHot("KitchenQual")
    			.normalize("YearBuilt", Normalize.Standardize, analysis)
    			.normalize("YearRemodAdd", Normalize.Standardize, analysis)
    			.integerToOneHot("MoSold", 1, 12)
    			.integerToOneHot("YrSold", 2006, 2010)
    			.categoricalToOneHot("SaleCondition")
    			.normalize("LotArea", Normalize.Standardize, analysis)
    			.normalize("TotalBsmtSF", Normalize.Standardize, analysis)
    			.normalize("1stFlrSF", Normalize.Standardize, analysis)
    			.normalize("2ndFlrSF", Normalize.Standardize, analysis)
				;
		
		return builder;
	}
	
	public TransformProcess.Builder createBaseTransformBuilder(Schema inputDataSchema, DataAnalysis analysis){
		
		TransformProcess.Builder builder = new TransformProcess.Builder(inputDataSchema)
				.removeColumns("Id")
    			.categoricalToOneHot("MSSubClass")
                .categoricalToOneHot("MSZoning")
                .categoricalToOneHot("Street")
                .categoricalToOneHot("Alley")
                .categoricalToOneHot("LotShape")
                .categoricalToOneHot("LandContour")
                .categoricalToOneHot("Utilities")
                .categoricalToOneHot("LotConfig")
                .categoricalToOneHot("LandSlope")
                 //.categoricalToInteger("LandSlope")
                .categoricalToOneHot("Neighborhood")
                .categoricalToOneHot("Condition1")
                .categoricalToOneHot("Condition2")
                .categoricalToOneHot("BldgType")
                .categoricalToOneHot("HouseStyle")
                .categoricalToOneHot("RoofStyle")
                .categoricalToOneHot("RoofMatl")
                .categoricalToOneHot("Exterior1st")
                .categoricalToOneHot("Exterior2nd")
                .categoricalToOneHot("MasVnrType")
                //.categoricalToInteger("ExterQual")
                //.categoricalToInteger("ExterCond")
                .categoricalToOneHot("ExterQual")
                .categoricalToOneHot("ExterCond")
                .categoricalToOneHot("Foundation")
                //.categoricalToInteger("BsmtQual")
                //.categoricalToInteger("BsmtCond")
                .categoricalToOneHot("BsmtQual")
                .categoricalToOneHot("BsmtCond")
                
                .categoricalToOneHot("BsmtExposure")
                //.categoricalToInteger("BsmtFinType1")
                //.categoricalToInteger("BsmtFinType2")
                .categoricalToOneHot("BsmtFinType1")
                .categoricalToOneHot("BsmtFinType2")
                .categoricalToOneHot("Heating")
    			//.categoricalToInteger("HeatingQC")    	
    			.categoricalToOneHot("HeatingQC")

    			.categoricalToOneHot("CentralAir")
    			.categoricalToOneHot("Electrical")
                //.categoricalToInteger("KitchenQual")         
    			.categoricalToOneHot("KitchenQual")

    			.categoricalToOneHot("Functional")
                //.categoricalToInteger("FireplaceQu")
    			.categoricalToOneHot("FireplaceQu")

    			.categoricalToOneHot("GarageType")
    			.categoricalToOneHot("GarageFinish")
                //.categoricalToInteger("GarageQual")
                //.categoricalToInteger("GarageCond")
    			.categoricalToOneHot("GarageQual")
    			.categoricalToOneHot("GarageCond")

    			.categoricalToOneHot("PavedDrive")
                //.categoricalToInteger("PoolQC")
                //.categoricalToInteger("Fence")   			
                .categoricalToOneHot("PoolQC")
    			.categoricalToOneHot("Fence")
    			.categoricalToOneHot("MiscFeature")
    			//.removeColumns("GarageYrBlt")
                //.removeColumns("MoSold", "YrSold","GarageYrBlt", "YearBuilt", "YearRemodAdd")
    			/*.stringToTimeTransform("MoSold","MM", DateTimeZone.UTC)
                .stringToTimeTransform("YrSold","YYYY", DateTimeZone.UTC)
                .stringToTimeTransform("YearBuilt","YYYY", DateTimeZone.UTC)
                .stringToTimeTransform("YearRemodAdd","YYYY", DateTimeZone.UTC)
                .stringToTimeTransform("GarageYrBlt","YYYY", DateTimeZone.UTC)
                */
    			//.integerToOneHot(columnName, minValue, maxValue)
    			//.categoricalToInteger("MoSold")
    			//.categoricalToInteger("YrSold")
    			//.categoricalToInteger("YearBuilt")
    			//.categoricalToInteger("YearRemodAdd")
    			//.normalize("MoSold", Normalize.Standardize, analysis)
    			//.normalize("YrSold", Normalize.Standardize, analysis)
    			.normalize("YearBuilt", Normalize.Standardize, analysis)
    			.normalize("YearRemodAdd", Normalize.Standardize, analysis)
    			
    			.integerToOneHot("MoSold", 1, 12)
    			.integerToOneHot("YrSold", 2006, 2010)
    			.removeColumns("GarageYrBlt")
    			
    			.categoricalToOneHot("SaleType")
    			.categoricalToOneHot("SaleCondition")
    			.normalize("LotFrontage", Normalize.Standardize, analysis)
    			.normalize("LotArea", Normalize.Standardize, analysis)
    			.normalize("MasVnrArea", Normalize.Standardize, analysis)
    			.normalize("BsmtFinSF1", Normalize.Standardize, analysis)
    			.normalize("BsmtFinSF2", Normalize.Standardize, analysis)
    			.normalize("BsmtUnfSF", Normalize.Standardize, analysis)
    			.normalize("TotalBsmtSF", Normalize.Standardize, analysis)
    			.normalize("1stFlrSF", Normalize.Standardize, analysis)
    			.normalize("2ndFlrSF", Normalize.Standardize, analysis)
    			.normalize("LowQualFinSF", Normalize.Standardize, analysis)
    			.normalize("GrLivArea", Normalize.Standardize, analysis)
    			
    			.normalize("GarageArea", Normalize.Standardize, analysis)
    			.normalize("WoodDeckSF", Normalize.Standardize, analysis)
    			.normalize("OpenPorchSF", Normalize.Standardize, analysis)
    			.normalize("EnclosedPorch", Normalize.Standardize, analysis)
    			;

		
		return builder;
		
	}
	

    /** 
     * Creates a TransformProcess that converts all integers to doubles.
     */
    public TransformProcess createTransformToDoubleTransformProcess(Schema inputSchema) {
    	List<String> intColumns  = this.schemaWrapper.getIntColumnList();
    	logger.debug("Integer Columns");
    	logger.debug(intColumns.toString());
    	TransformProcess.Builder transformBuild =
    			new TransformProcess.Builder(inputSchema);
    	for(String intColumn : intColumns ) {
    		transformBuild.convertToDouble(intColumn);
    	}
    	return transformBuild.build();
    	
    }
    
    public abstract TransformProcess createFinalTransformProcess(Schema inputDataSchema, DataAnalysis analysis) ;
    
}
