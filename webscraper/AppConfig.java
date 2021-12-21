package mdx.saad.webscraper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.*;

/**
 *Uses spring to handle dependencies between classes in the program
 */
@Configuration
public class AppConfig {
	
	@Bean
	public ApplicationRunner applicationRunner() {
		ApplicationRunner applicationRunner = new ApplicationRunner();
		
		//Create a list of web scrapers
		List<Thread> threadList = new ArrayList<Thread>();
		threadList.add(rightMove());
		threadList.add(openRent());
		threadList.add(zoopla());
		threadList.add(gumtree());
		
		//Inject into application runner
		applicationRunner.setThreadList(threadList);
		return applicationRunner;
	}

	//Create bean for Hibernate class which is used to start session factory
	@Bean
	public Hibernate hibernateInstance() {
		Hibernate tmpInstance = new Hibernate();
		tmpInstance.init();
		return tmpInstance;
	}
	
	//Create beans for all four websites
	@Bean
	public Website1 rightMove() {
		Website1 tmpWebsite1 = new Website1();
		tmpWebsite1.setPostCode("");
		tmpWebsite1.setOutCode("");
		tmpWebsite1.setCrawlDelay(15);
		tmpWebsite1.setHibernateInstance(hibernateInstance());
		return tmpWebsite1;
	}
	
	@Bean
	public Website2 openRent() {
		Website2 tmpWebsite2 = new Website2();
		tmpWebsite2.setPostCode("");
		tmpWebsite2.setCrawlDelay(15);
		tmpWebsite2.setHibernateInstance(hibernateInstance());
		return tmpWebsite2;
	}
	
	@Bean
	public Website3 zoopla() {
		Website3 tmpWebsite3 = new Website3();
		tmpWebsite3.setPostCode("");
		tmpWebsite3.setCrawlDelay(15);
		tmpWebsite3.setHibernateInstance(hibernateInstance());
		return tmpWebsite3;
	}
	
	@Bean
	public Website4 gumtree() {
		Website4 tmpWebsite4 = new Website4();
		tmpWebsite4.setPostCode("");
		tmpWebsite4.setCrawlDelay(15);
		tmpWebsite4.setHibernateInstance(hibernateInstance());
		return tmpWebsite4;
	}

}
 