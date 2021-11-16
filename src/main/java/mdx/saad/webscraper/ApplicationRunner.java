package mdx.saad.webscraper;

import java.util.Scanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationRunner {
	private static ApplicationContext context;

	public static void run() {
		context = new AnnotationConfigApplicationContext(AppConfig.class);

		// Get Car bean
		Website1 w1 = (Website1) context.getBean("RightMove");
		Website2 w2 = (Website2) context.getBean("OpenRent");
		Website3 w3 = (Website3) context.getBean("Zoopla");
		Website4 w4 = (Website4) context.getBean("Gumtree");


		// Call methods on car bean
		w1.start();
		w2.start();
		w3.start();
		w4.start();

		Scanner scanner = new Scanner(System.in);
		String userInput = scanner.nextLine();
		while (!userInput.equals("stop")) {
			userInput = scanner.nextLine();
		}
		scanner.close();

		w1.interrupt();
		w2.interrupt();
		w3.interrupt();
		w4.interrupt();
		// Wait for threads to finish - join can throw an InterruptedException
		try {
			w1.join();
			w2.join();
			w3.join();
			w4.join();

		} catch (InterruptedException ex) {
			System.out.println("Interrupted exception thrown: " + ex.getMessage());
		}
		System.out.println("Web scraping complete");
	}
	
//	public static void testRunner() {
//		context = new AnnotationConfigApplicationContext(AppConfig.class);
//
//		// Get Car bean
//		Website1 w1 = (Website1) context.getBean("RightMove");
//		Website2 w2 = (Website2) context.getBean("OpenRent");
//		Website3 w3 = (Website3) context.getBean("Zoopla");
//		Website4 w4 = (Website4) context.getBean("Gumtree");
//	}

}
