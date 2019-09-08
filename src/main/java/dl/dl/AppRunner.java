package dl.dl;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/** Run application **/
@SpringBootApplication
public class AppRunner {
	private final static Logger logger = LoggerFactory.getLogger(AppRunner.class);

    public static void main(String[] args) {
        SpringApplication.run(AppRunner.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
        	
        	logger.debug("Starting Application");
        	// TrainerApp just builds and evaluates the network
        	// SubmissionApp does the same as TrainerApp and 
        	// 		generates the Kaggle submission in addition
        	
        	//App app = ctx.getBean(TrainerApp.class);
        	App app = ctx.getBean(SubmissionApp.class);

        	app.run();
        };
    }

}
