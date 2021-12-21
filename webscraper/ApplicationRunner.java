package mdx.saad.webscraper;

import java.util.List;
import java.util.Scanner;

/**
 *using threads, it allows data to be scraped from multiple websites simultaneously
 */
public class ApplicationRunner {

	List<Thread> webScraperList;
	public void setThreadList(List<Thread> wsl) {
		this.webScraperList = wsl;
	}
	public List<Thread> getThreadList() {
		return this.webScraperList;
	}
	
	/**
	 * calls the start method for each of the threads representing the websites
	 * scraping stops when user types 'stop'
	 */
	public void startScraping() {
		for (Thread webScraper : webScraperList) {
			webScraper.start();
		}
		Scanner scanner = new Scanner(System.in);
		String userInput = scanner.nextLine();
		while (!userInput.equals("stop")) {
			userInput = scanner.nextLine();
		}
		scanner.close();
		for (Thread webScraper : webScraperList) {
			try {
				webScraper.interrupt();
				webScraper.join();
			} catch (InterruptedException ex) {
				System.out.println("Interrupted exception thrown: " + ex.getMessage());
			}
		}
		System.out.println("Web scraping complete");
	}
}
