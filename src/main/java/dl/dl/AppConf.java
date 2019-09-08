package dl.dl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import dl.dl.dataloading.MissingValuesImputer;
import dl.dl.dataloading.schema.SchemaWrapper;

@Configuration
public class AppConf {
	
		/** Create a MissingValuesImputer using a constructor arg */
	   @Bean
	   @Scope("prototype")
	   public MissingValuesImputer createImputer(SchemaWrapper arg) {
	        return new MissingValuesImputer(arg);
	    }
	   
		
}
