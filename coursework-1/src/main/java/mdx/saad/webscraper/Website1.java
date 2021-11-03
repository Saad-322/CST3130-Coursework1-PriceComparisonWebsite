package mdx.saad.webscraper;

import java.io.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Website1 extends Thread {
	volatile boolean end = false;
	String websiteName;
	int crawlDelay = 15;
	String postCode;
	String outCode;
	HibernateExample example = new HibernateExample();

	public Website1(String wn, HibernateExample e) {
		websiteName = wn;
		example = e;
	}

	void postCodeConversion(String pc) { // convert search outCode to OUTCODE that is required by the URL
		switch (pc) {
		case "NW1":
			outCode = "1855";
			break;
		case "NW2":
			outCode = "1858";
			break;
		case "NW3":
			outCode = "1859";
			break;
		case "NW4":
			outCode = "1860";
			break;
		case "NW5":
			outCode = "1861";
			break;
		case "NW6":
			outCode = "1862";
			break;
		case "NW7":
			outCode = "1863";
			break;
		case "NW8":
			outCode = "1864";
			break;
		case "NW9":
			outCode = "1865";
			break;
		case "NW10":
			outCode = "1856";
			break;
		case "NW11":
			outCode = "1857";
			break;
		}
	}

	void scrapeRightMove() throws Exception {
	
		postCodeConversion(postCode);
		// Download HTML document from website
		Document doc = Jsoup
				.connect("https://www.rightmove.co.uk/property-to-rent/find.html?locationIdentifier=OUTCODE%5E"
						+ outCode
						+ "&sortType=1&propertyTypes=&mustHave=&dontShow=&furnishTypes=&keywords=")
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").get();

		// Get all of the products on the page
		Elements rooms = doc.select(".l-searchResult");// check to see the box for the whole ad
//	        System.out.println(rooms.text());
		System.out.println(this.websiteName);
		// Work through the products
		for (int i = 0; i < rooms.size(); ++i) {

			// Get the product description
			Elements description = rooms.get(i).select(".propertyCard-address");// first part of class
			Elements price = rooms.get(i).select(".propertyCard-priceValue");// first ppart of class}
			Elements websiteURL = rooms.get(i).select(".propertyCard-link");
			Elements imageURL = rooms.get(i).select("img");
			
			// Output the data that we have downloaded

			System.out.println("DESCRIPTION: " + description.text());
			System.out.println("PRICE: " + price.text());

			System.out.println();
			
			String[] parts = price.text().trim().split(" ");
			parts[0] = parts[0].replace(",", "");
			example.addRoom(description.text(),Integer.parseInt(parts[0].substring(1, parts[0].length())),postCode,"https://www.rightmove.co.uk"+ websiteURL.attr("href"),imageURL.attr("abs:src"));
//			example.updateRoom();
//			example.searchRooms();
//			example.deleteRoom();
			
			//first check sellers DB if seller not present then add
//			if(!example.searchSellers("Saad")) {
//				example.addSeller("Saad","322");
//			}

		}
		
		
		System.out.println("-------------------------------------------");
	}

	public void run() {
		try {
			for(int j = 1; j<12; j++) {
				
				postCode = "NW"+j;
				// Web Scraping code goes here
				try {
					// jSoup Exercises
					scrapeRightMove();
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
