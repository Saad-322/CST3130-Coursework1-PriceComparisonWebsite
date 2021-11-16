
package mdx.saad.webscraper;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Website4 extends Thread {
	int crawlDelay;
	String postCode;
	Hibernate hibernateInstance = new Hibernate();

	public Website4() {}
	
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

	void scrapeGumtree() throws Exception {

		// Download HTML document from website
		Document doc = Jsoup.connect("https://www.gumtree.com/search?featured_filter=false&q=" + postCode
				+ "&search_location=" + postCode
				+ "&search_category=property-to-rent&urgent_filter=false&sort=price_lowest_first&search_distance=3&search_scope=false&photos_filter=false")
				.userAgent(
						"Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Safari/605.1.15")
				.get();
		// Get all of the products on the page
		Elements rooms = doc.select(".listing-content");// check to see the box for the whole ad
		Elements y = doc.select(".natural");
		// Work through the products
		for (int i = 1; i < rooms.size(); ++i) {

			// Get the product description
			Elements description = rooms.get(i).select(".listing-title");// first part of class
			Elements price = rooms.get(i).select(".listing-price");// first ppart of class

			Elements websiteURL = y.get(i-1).select(".listing-link");
			Elements imageURL = y.get(i-1).select("img");
			

			// Output the data that we have downloaded


			int sellerID = hibernateInstance.searchSeller("Lee","07459553041");
			
		      //Create an instance of a Room class 
			Room room = new Room();
		      
		    //Set values of room class that we want to add
		      room.setDescription(description.text());
		      room.setPostCode(postCode);	    
		      room.setImageURL(imageURL.attr("abs:src"));
		      room.setSellerID(sellerID);
		      room.setWebsiteURL("https://www.gumtree.com"+ websiteURL.attr("href"));

			String x = price.text().trim();
			if (x.charAt(x.length() - 1) == 'w') {
				int modifiedPrice = (int) (Integer.parseInt(x.substring(1, 4)) * 52.14) / 12;		      
				  room.setRent(modifiedPrice);
				hibernateInstance.addRoom(room);
			} else {
				String[] parts = price.text().trim().split("p");
				parts[0] = parts[0].replace(",", "");
				room.setRent(Integer.parseInt(parts[0].substring(1, parts[0].length())));
				hibernateInstance.addRoom(room);
			}
		
		
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
					scrapeGumtree();
				} catch (Exception ex) {
					System.out.println("Exercise Exception: " + ex.getMessage());
				}
				sleep(1000 * crawlDelay);
			}
		} catch (InterruptedException ex) {
			System.out.println("Thread Webiste4 stopping");
		}
	}

}
