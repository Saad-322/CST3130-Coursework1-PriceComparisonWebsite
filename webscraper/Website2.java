package mdx.saad.webscraper;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

/**
 * Allows data to be scraped from the website 'OpenRent' which is stored in
 * database using hibernate
 */
public class Website2 extends Thread {
	int crawlDelay;
	String postCode;
	Hibernate hibernateInstance = new Hibernate();

	public Website2() {
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
	 * scrapes the data from website 'OpenRent' and stores it in database using
	 * hibernate
	 */
	public void scrapeOpenRent() throws Exception {

		// Download HTML document from website
		Document doc = Jsoup
				.connect("https://www.openrent.co.uk/properties-to-rent/?term=" + postCode
						+ "%20London&prices_min=266&prices_max=800")
				.userAgent(
						"Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Safari/605.1.15")
				.get();

		// Get all of the Rooms on the page
		Elements rooms = doc.select(".pli");

		// Work through the Rooms
		for (int i = 0; i < rooms.size(); ++i) {

			// Get the Room details
			Elements description = rooms.get(i).select(".listing-title");
			Elements class_price = rooms.select(".pim");
			Elements price = class_price.get(i).select(".pl-title");
			Elements websiteURL = rooms.get(i).select(".pli");
			Elements imageURL = rooms.get(i).select(".propertyPic");
			
			//string processing to get monthly rent
			String[] parts = price.text().trim().split(" ");
			parts[0] = parts[0].replace(",", "");

			//get seller details.
			Document ad = Jsoup.connect("https://www.openrent.co.uk" + websiteURL.attr("href")).get();
			Elements seller_name = ad.select(".mb-2 p");
			int sellerID = hibernateInstance.searchSeller(seller_name.get(3).text(), "none");

			// Create an instance of a Room class
			Room room = new Room();

			// Set values of Room class that we want to add
			room.setDescription(description.text());
			room.setPostCode(postCode);
			room.setRent(Integer.parseInt(parts[0].substring(1, parts[0].length())));
			room.setImageURL(imageURL.attr("abs:src"));
			room.setSellerID(sellerID);
			room.setWebsiteURL("https://www.openrent.co.uk" + websiteURL.attr("href"));

			//add room
			hibernateInstance.addRoom(room);
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
