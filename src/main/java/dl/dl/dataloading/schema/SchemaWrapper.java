package dl.dl.dataloading.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.datavec.api.transform.ColumnType;
import org.datavec.api.transform.metadata.ColumnMetaData;
import org.datavec.api.transform.schema.Schema;

/**
 * Holds the Schema information and derived collections.
 */
public abstract class SchemaWrapper {
	private Schema schema;
	/** Integer columns */
	private List<String> intColumnList;
	/** String columns */
	private List<String> strColumnList;
	/** Both integer and string columns */
	private List<String> combinedColumnList;
	private String[] intColumnAr;
	private String[] strColumnAr;
	private String[] combinedColumnAr;

	public SchemaWrapper() {
		this.schema = this.createSchema();
		//get rid of NA for mean imputation for the integer columns
		this.intColumnList = this.makeColumnsList(ColumnType.Integer);
		//remove ones we are going to one-hot-encode as ints
		this.intColumnList.remove("MoSold"); 
		this.intColumnList.remove("YrSold");

		//get rid of NA for converting to Time for the date columns
		this.strColumnList = this.makeColumnsList(ColumnType.String);
		this.intColumnAr  =this.intColumnList.toArray(new String[0]);
		
		this.strColumnAr = this.strColumnList.toArray(new String[0]);
		this.combinedColumnList = new ArrayList<String>(this.intColumnList);
		this.combinedColumnList.addAll(strColumnList);
		 this.combinedColumnAr = this.combinedColumnList.toArray(new String[0]);
	}

    public abstract Schema createSchema();
    
	public Schema getSchema() {
		return this.schema;
	}
	
    protected Schema.Builder createBaseSchemaBuilder() {
    	Schema.Builder inputSchemaBuilder = new Schema.Builder()
    			.addColumnsInteger("Id")
    			.addColumnCategorical("MSSubClass", Arrays.asList("60", "20", "70", "50", "190", "45", "90", "120", "30", "85", "80", "160", "75", "180", "40", "150"))
    			.addColumnCategorical("MSZoning", Arrays.asList("RL", "RM", "C (all)", "FV", "RH", "NA"))
    			.addColumnsInteger("LotFrontage", "LotArea" )
    			.addColumnCategorical("Street", Arrays.asList("Pave", "Grvl"))
    			.addColumnCategorical("Alley", Arrays.asList("NA", "Grvl", "Pave"))
    			.addColumnCategorical("LotShape", Arrays.asList("Reg", "IR1", "IR2", "IR3"))
    			.addColumnCategorical("LandContour", Arrays.asList("Lvl", "Bnk", "Low", "HLS"))
    			.addColumnCategorical("Utilities", Arrays.asList("AllPub", "NoSeWa", "NA"))
    			.addColumnCategorical("LotConfig", Arrays.asList("Inside", "FR2", "Corner", "CulDSac", "FR3"))
    			.addColumnCategorical("LandSlope", Arrays.asList("Gtl", "Mod", "Sev"))
    			.addColumnCategorical("Neighborhood", Arrays.asList("CollgCr", "Veenker", "Crawfor", "NoRidge",
    					"Mitchel", "Somerst", "NWAmes", "OldTown", "BrkSide", "Sawyer", "NridgHt", "NAmes", "SawyerW",
    					"IDOTRR", "MeadowV", "Edwards", "Timber", "Gilbert", "StoneBr", "ClearCr", "NPkVill", "Blmngtn",
    					"BrDale", "SWISU", "Blueste"))
    			.addColumnCategorical("Condition1", Arrays.asList("Norm", "Feedr", "PosN", "Artery", "RRAe", "RRNn", "RRAn", "PosA", "RRNe"))
    			.addColumnCategorical("Condition2", Arrays.asList("Norm", "Artery", "RRNn", "Feedr", "PosN", "PosA", "RRAn", "RRAe"))
    			.addColumnCategorical("BldgType", Arrays.asList("1Fam", "2fmCon", "Duplex", "TwnhsE", "Twnhs"))
    			.addColumnCategorical("HouseStyle", Arrays.asList("2Story", "1Story", "1.5Fin", "1.5Unf", "SFoyer", "SLvl", "2.5Unf", "2.5Fin"))
    			.addColumnsInteger("OverallQual","OverallCond")
    			//.addColumnsString("YearBuilt","YearRemodAdd")
    			.addColumnsInteger("YearBuilt","YearRemodAdd")
    			.addColumnCategorical("RoofStyle", Arrays.asList("Gable", "Hip", "Gambrel", "Mansard", "Flat", "Shed"))
    			.addColumnCategorical("RoofMatl", Arrays.asList("CompShg", "WdShngl", "Metal", "WdShake", "Membran", "Tar&Grv", "Roll", "ClyTile"))
    			.addColumnCategorical("Exterior1st", Arrays.asList("VinylSd", "MetalSd", "Wd Sdng", "HdBoard", "BrkFace", "WdShing", "CemntBd", "Plywood", "AsbShng", "Stucco", "BrkComm", "AsphShn", "Stone", "ImStucc", "CBlock", "NA"))
    			.addColumnCategorical("Exterior2nd", Arrays.asList("VinylSd", "MetalSd", "Wd Shng", "HdBoard", "Plywood", "Wd Sdng", "CmentBd", "BrkFace", "Stucco", "AsbShng", "Brk Cmn", "ImStucc", "AsphShn", "Stone", "Other", "CBlock", "NA"))
    			.addColumnCategorical("MasVnrType", Arrays.asList("BrkFace", "None", "Stone", "BrkCmn", "nan", "NA"))
    			.addColumnInteger("MasVnrArea")
    			.addColumnCategorical("ExterQual", Arrays.asList("Ex", "Gd","TA","Fa","Po","NA"))
    			.addColumnCategorical("ExterCond", Arrays.asList("Ex", "Gd","TA","Fa","Po","NA"))
    			.addColumnCategorical("Foundation", Arrays.asList("PConc", "CBlock", "BrkTil", "Wood", "Slab", "Stone"))
    			.addColumnCategorical("BsmtQual", Arrays.asList("Ex", "Gd","TA","Fa","Po","NA"))
    			.addColumnCategorical("BsmtCond", Arrays.asList("Ex", "Gd","TA","Fa","Po","NA"))
    			.addColumnCategorical("BsmtExposure", Arrays.asList("No", "Gd", "Mn", "Av", "NA"))
    			.addColumnCategorical("BsmtFinType1", Arrays.asList("GLQ", "ALQ","BLQ","Rec","LwQ","Unf","NA"))
    			.addColumnInteger("BsmtFinSF1")
    			.addColumnCategorical("BsmtFinType2", Arrays.asList("GLQ", "ALQ","BLQ","Rec","LwQ","Unf","NA"))
    			.addColumnsInteger("BsmtFinSF2","BsmtUnfSF","TotalBsmtSF")
    			.addColumnCategorical("Heating", Arrays.asList("GasA", "GasW", "Grav", "Wall", "OthW", "Floor"))
    			.addColumnCategorical("HeatingQC", Arrays.asList("Ex", "Gd","TA","Fa","Po"))
    			.addColumnCategorical("CentralAir", Arrays.asList("Y", "N"))
    			.addColumnCategorical("Electrical", Arrays.asList("SBrkr", "FuseF", "FuseA", "FuseP", "Mix", "NA"))
    			.addColumnsInteger("1stFlrSF","2ndFlrSF","LowQualFinSF",
    							"GrLivArea","BsmtFullBath","BsmtHalfBath","FullBath",
    							"HalfBath","BedroomAbvGr","KitchenAbvGr")
    			.addColumnCategorical("KitchenQual", Arrays.asList("Ex", "Gd","TA","Fa","Po","NA"))
    			.addColumnInteger("TotRmsAbvGrd")
    			.addColumnCategorical("Functional", Arrays.asList("Typ", "Min1", "Maj1", "Min2", "Mod", "Maj2", "Sev", "NA"))
    			.addColumnInteger("Fireplaces")
    	    	.addColumnCategorical("FireplaceQu", Arrays.asList("Ex", "Gd","TA","Fa","Po","NA"))
    	    	.addColumnCategorical("GarageType", Arrays.asList("Attchd", "Detchd", "BuiltIn", "CarPort", "NA", "Basment", "2Types"))
    			.addColumnString("GarageYrBlt")
    			.addColumnCategorical("GarageFinish", Arrays.asList("RFn", "Unf", "Fin", "NA"))
    			.addColumnsInteger("GarageCars","GarageArea")
    	    	 .addColumnCategorical("GarageQual", Arrays.asList("Ex", "Gd","TA","Fa","Po","NA"))
    	    	 .addColumnCategorical("GarageCond", Arrays.asList("Ex", "Gd","TA","Fa","Po","NA"))
    	    	 .addColumnCategorical("PavedDrive", Arrays.asList("Y", "N", "P"))
    			.addColumnsInteger("WoodDeckSF","OpenPorchSF","EnclosedPorch","3SsnPorch",
    					"ScreenPorch","PoolArea")
    			.addColumnCategorical("PoolQC", Arrays.asList("Ex", "Gd","TA","Fa","NA"))
    	    	.addColumnCategorical("Fence", Arrays.asList("GdPrv", "MnPrv","GdWo","MnWw","NA"))
    	    	.addColumnCategorical("MiscFeature", Arrays.asList("NA", "Shed", "Gar2", "Othr", "TenC"))
    			.addColumnsInteger("MiscVal")
    			//.addColumnsString("MoSold","YrSold")
    			.addColumnsInteger("MoSold","YrSold")
    			
    			.addColumnCategorical("SaleType", Arrays.asList("WD", "New", "COD", "ConLD", "ConLI", "CWD", "ConLw", "Con", "Oth", "NA"))
    			.addColumnCategorical("SaleCondition", Arrays.asList("Normal", "Abnorml", "Partial", "AdjLand", "Alloca", "Family"))
    				;
    			
    	return inputSchemaBuilder;
    			
    }
    

    /**
     * Create a list of all the columns of a certain type.
     */
    public ArrayList<String> makeColumnsList(ColumnType wantedType) {
    	ArrayList<String> columns = new ArrayList<String>();

    	List<ColumnMetaData> metaDatas = this.schema.getColumnMetaData();
    	for(ColumnMetaData metaData : metaDatas) {
    		ColumnType type = metaData.getColumnType();
    		// We remove Id. 
    		if(type ==wantedType && !metaData.getName().equals("Id")) {
    			columns.add(metaData.getName());
    		}	
    	}
    	
    	return columns;
    }

	public List<String> getIntColumnList() {
		return intColumnList;
	}
	
	public String[] getIntColumnAr() {
		return intColumnAr;
	}
	public List<String> getStrColumnList(){
		return this.strColumnList;
	}
	public String[] getStrColumnAr() {
		return strColumnAr;
	}
	public List<String> getCombinedColumnList() {
		return this.combinedColumnList;
	}
	public String[] getCombinedColumnAr() {
		return this.combinedColumnAr;
	}

}
