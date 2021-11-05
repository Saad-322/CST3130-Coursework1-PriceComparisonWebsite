package mdx.saad.webscraper;

import java.io.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Website3 extends Thread {
	volatile boolean end = false;
	String websiteName;
	int crawlDelay = 15;
	String postCode;
	HibernateExample example = new HibernateExample();

	public Website3(String wn, HibernateExample e) {
		websiteName = wn;
		example=e;
	}

	void scrapeZoopla() throws Exception {
			
		// Download HTML document from website
		Document doc = Jsoup.connect(
				"https://www.zoopla.co.uk/to-rent/property/london/"+postCode+"/?page_size=25&price_frequency=per_month&q="+postCode+"&radius=0.5&results_sort=lowest_price&search_source=facets")
				.get();
		// Get all of the products on the page
		Elements rooms = doc.select(".e2uk8e18");// check to see the box for the whole ad
//		Elements imageURL = doc.select(".earci3d1");
//	        System.out.println(rooms.text());
		System.out.println(websiteName);
		// Work through the products
		for (int i = 0; i < rooms.size(); ++i) {

			// Get the product description
			Elements description = rooms.get(i).select(".css-vthwmi-Heading2-StyledAddress");// first part of class
//	            Elements description = rooms.get(i).select("#listing-description");
			Elements address = rooms.get(i).select(".css-nwapgq-Text");
//	            Elements x = rooms.select(".listingPrice");
			Elements price = rooms.get(i).select(".css-1o565rw-Text");// first ppart of class
			Elements websiteURL = rooms.get(i).select(".e33dvwd0");
//			Elements y = imageURL.get(i).select("img");
			
			Document URLdoc = Jsoup.connect(
					"https://www.zoopla.co.uk"+ websiteURL.attr("href"))
					.get();
			Elements y = URLdoc.select(".e16xseoz1");
			
			// Output the data that we have downloaded

			System.out.print("DESCRIPTION: " + description.text());
			System.out.println(", " + address.text());
			System.out.println("PRICE: " + price.text());
			System.out.println("Website URL: "+"https://www.zoopla.co.uk"+ websiteURL.attr("href"));
//			System.out.println("IMAGE: " + y.get(1).select("img"));
			
//			String x = price.text().trim();
//			String[] parts = x.split(" ");
//			String part1 = parts[0]; // 004
//			String part2 = parts[1]; // 034556
//			String [] finalParts = part1.split("£");
//			String finalPrice = finalParts[1];
//			System.out.println("FINAL PRICE: "+ (Integer.parseInt(finalPrice)*52.14)/12);
			
			String[] parts = price.text().trim().split(" ");
			parts[0] = parts[0].replace(",", "");
			int sellerID = example.searchSeller("Saad","07459353041");
			example.addRoom(description.text()+", "+address.text(),Integer.parseInt(parts[0].substring(1, parts[0].length())),postCode,"https://www.zoopla.co.uk"+ websiteURL.attr("href"),y.get(1).select("img").attr("abs:src"),sellerID);
//			example.updateRoom();
//			example.searchRooms();
//			example.deleteRoom();
			
			
			System.out.println();
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
					scrapeZoopla();
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
