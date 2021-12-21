
package mdx.saad.webscraper;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

/**
 * Allows data to be scraped from the website 'Gumtree' which is stored in
 * database using hibernate
 */
public class Website4 extends Thread {
	int crawlDelay;
	String postCode;
	Hibernate hibernateInstance = new Hibernate();

	public Website4() {
	}

	// getters and setters
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

	/**
	 * scrapes the data from website 'Gumtree' and stores it in database using
	 * hibernate
	 */
	public void scrapeGumtree() throws Exception {

		// Download HTML document from website
		Document doc = Jsoup.connect("https://www.gumtree.com/search?featured_filter=false&q=" + postCode
				+ "&search_location=" + postCode
				+ "&search_category=property-to-rent&urgent_filter=false&sort=price_lowest_first&search_distance=3&search_scope=false&photos_filter=false")
				.userAgent(
						"Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Safari/605.1.15")
				.get();

		// Get all of the Rooms on the page
		Elements rooms = doc.select(".listing-content");// check to see the box for the whole ad
		Elements class_websiteURL = doc.select(".natural");
		Elements class_imageURL = doc.select(".natural img");

		// custom counter to get the images
		int image_counter = 1;

		// Work through the Rooms on the page
		for (int i = 1; i < rooms.size(); ++i) {

			// Get the Room details
			Elements description = rooms.get(i).select(".listing-title");
			Elements price = rooms.get(i).select(".listing-price");
			Elements websiteURL = class_websiteURL.get(i - 1).select(".listing-link");
			image_counter = image_counter + 2;

			// get seller details. both are 'none' for this web site as mentioned in the report
			int sellerID = hibernateInstance.searchSeller("none", "none");

			// Create an instance of a Room class
			Room room = new Room();

			// Set values of room class that we want to add
			room.setDescription(description.text());
			room.setPostCode(postCode);
			room.setImageURL(class_imageURL.get(image_counter).select("img").attr("abs:src"));
			room.setSellerID(sellerID);
			room.setWebsiteURL("https://www.gumtree.com" + websiteURL.attr("href"));

			// string processing and conversion of weekly rent to monthly rent where needed
			String x = price.text().trim();
			if (x.charAt(x.length() - 1) == 'w') {
				int modifiedPrice = (int) (Integer.parseInt(x.substring(1, 4)) * 52.14) / 12;
				room.setRent(modifiedPrice);
				// add room
				hibernateInstance.addRoom(room);
			} else {
				String[] parts = price.text().trim().split("p");
				parts[0] = parts[0].replace(",", "");
				room.setRent(Integer.parseInt(parts[0].substring(1, parts[0].length())));
				// add room
				hibernateInstance.addRoom(room);
			}
		}
	}

	/**
	 * runs the thread which scrapes the website after every 15 second instance to
	 * get data for rooms between the postcodes NW1 and NW11
	 */
	public void run() {
		try {
			for (int j = 1; j < 12; j++) {// loop through post codes from NW1 to NW11
				postCode = "NW" + j;
				try {
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
