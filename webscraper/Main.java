package mdx.saad.webscraper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main class that starts the scraping process using application runner bean
 */
public class Main {

	private static ApplicationContext context;

	public static void main(String[] args) {		
	    context = new AnnotationConfigApplicationContext(AppConfig.class);
   
        //Get application runner bean
        ApplicationRunner run = (ApplicationRunner) context.getBean("applicationRunner");
        
        //Call methods on application runner bean
        run.startScraping();
	}
}
