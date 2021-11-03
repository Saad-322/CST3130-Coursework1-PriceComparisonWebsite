package mdx.saad.webscraper;

import java.io.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Website2 extends Thread {
	volatile boolean end = false;
	String websiteName;
	int crawlDelay = 15;
	String postCode;
	HibernateExample example = new HibernateExample();

	public Website2(String wn, HibernateExample e) {
		websiteName = wn;
		example=e;
	}

	void scrapeOpenRent() throws Exception {
		
		// Download HTML document from website
		Document doc = Jsoup
				.connect("https://www.openrent.co.uk/properties-to-rent/?term=" + postCode
						+ "%20London&prices_min=266&prices_max=800")
				.userAgent(
						"Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Safari/605.1.15")
				.get();

		// Get all of the products on the page
		Elements rooms = doc.select(".pli");
		System.out.println(websiteName);
		// Work through the products
		for (int i = 0; i < rooms.size(); ++i) {

			// Get the product description
			Elements description = rooms.get(i).select(".listing-title");
			Elements x = rooms.select(".pim");
			Elements price = x.get(i).select(".pl-title");
			Elements websiteURL = rooms.get(i).select(".pli");
			Elements imageURL = rooms.get(i).select(".propertyPic");
			System.out.println();

			// Output the data that we have downloaded

			System.out.println("DESCRIPTION: " + description.text());
			System.out.println("PRICE: " + price.text());

			System.out.println();
			
			String[] parts = price.text().trim().split(" ");
			parts[0] = parts[0].replace(",", "");
			example.addRoom(description.text(),Integer.parseInt(parts[0].substring(1, parts[0].length())),postCode,"https://www.openrent.co.uk"+ websiteURL.attr("href"),imageURL.attr("abs:src"));
//			example.updateRoom();
//			example.searchRooms();
//			example.deleteRoom();
		}
		
		System.out
				.println("------------------------------------------------------------------------------------------");

	}

	public void run() {
		try {
			for(int j = 1; j<12; j++) {
				
				postCode = "NW"+j;
				// Web Scraping code goes here
				try {
					// jSoup Exercises
					scrapeOpenRent();
				} catch (Exception ex) {
					System.out.println("Exercise Exception: " + ex.getMessage());
				}
				sleep(1000 * crawlDelay);
			}
		} catch (InterruptedException ex) {
			System.out.println("Thread " + websiteName + " stopping");
		}
	}

}
