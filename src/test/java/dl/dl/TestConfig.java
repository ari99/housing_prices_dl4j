package dl.dl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;



/**
 * This class is used to filter out CommandLineRunner and AppRunner classes
 * so that the application isn't automatically run when the unit tests are run.
 * 
 * Reference: 
 * https://stackoverflow.com/questions/29344313/prevent-application-commandlinerunner-classes-from-executing-during-junit-test
 * 
 */
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
									value = {CommandLineRunner.class, AppRunner.class}))
@EnableAutoConfiguration
public class TestConfig {
}