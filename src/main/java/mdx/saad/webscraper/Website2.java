package mdx.saad.webscraper;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Website2 extends Thread {
	int crawlDelay;
	String postCode;
	Hibernate hibernateInstance = new Hibernate();

	public Website2() {}
	
	//getters and setters
	public void setCrawlDelay(int cd) {
		crawlDelay = cd;
	}
	public void setPostCode(String pc) {
		postCode = pc;
	}
	public void setHibernateInstance(Hibernate h) {
		hibernateInstance = h;
	}
	
	public int getCrawlDelay() {
		return crawlDelay;
	}
	
	public String getPostCode() {
		return postCode;
	}
	public Hibernate getHibernateInstance() {
		return hibernateInstance;
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
		
		// Work through the products
		for (int i = 0; i < rooms.size(); ++i) {

			// Get the product description
			Elements description = rooms.get(i).select(".listing-title");
			Elements x = rooms.select(".pim");
			Elements price = x.get(i).select(".pl-title");
			Elements websiteURL = rooms.get(i).select(".pli");
			Elements imageURL = rooms.get(i).select(".propertyPic");
			
			String[] parts = price.text().trim().split(" ");
			parts[0] = parts[0].replace(",", "");
			
			int sellerID = hibernateInstance.searchSeller("Ahmad","07459343041");
			
		      //Create an instance of a Room class 
			Room room = new Room();

		      //Set values of Room class that we want to add
		      room.setDescription(description.text());
		      room.setPostCode(postCode);
		      room.setRent(Integer.parseInt(parts[0].substring(1, parts[0].length())));
		      room.setImageURL(imageURL.attr("abs:src"));
		      room.setSellerID(sellerID);
		      room.setWebsiteURL("https://www.openrent.co.uk"+ websiteURL.attr("href"));
					
			hibernateInstance.addRoom(room);
			}
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
			System.out.println("Thread Website 2 stopping");
		}
	}

}
