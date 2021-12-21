package mdx.saad.webscraper;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

/**
 * Allows data to be scraped from the website 'Zoopla' which is stored in
 * database using hibernate
 */
public class Website3 extends Thread {
	int crawlDelay;
	String postCode;
	Hibernate hibernateInstance = new Hibernate();

	public Website3() {
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
	 * scrapes the data from website 'Zoopla' and stores it in database using
	 * hibernate
	 */
	public void scrapeZoopla() throws Exception {

		// Download HTML document from website
		Document doc = Jsoup.connect("https://www.zoopla.co.uk/to-rent/property/london/" + postCode
				+ "/?page_size=25&price_frequency=per_month&q=" + postCode
				+ "&radius=0.5&results_sort=lowest_price&search_source=facets").get();

		// Get all of the Rooms on the page
		Elements rooms = doc.select(".e2uk8e20");// check to see the box for the whole ad
		// Work through the products
		for (int i = 3; i < rooms.size(); ++i) {

			// Get the Room details
			Elements description = rooms.get(i).select(".css-vthwmi-Heading2-StyledAddress");// first part of class
			Elements address = rooms.get(i).select(".css-nwapgq-Text");
			Elements price = rooms.get(i).select(".css-1o565rw-Text");// first ppart of class
			Elements websiteURL = rooms.get(i).select(".e33dvwd0");

			//get seller details. both none in this case as mentioned in the report
			int sellerID = hibernateInstance.searchSeller("none", "none");
			
			//open the ad page each time to extract image
			Document URLdoc = Jsoup.connect("https://www.zoopla.co.uk" + websiteURL.attr("href")).get();
			Elements class_imageURL = URLdoc.select(".e16xseoz1");

			//string processing to get monthly rent
			String[] parts = price.text().trim().split(" ");
			parts[0] = parts[0].replace(",", "");

			// Create an instance of a Room class
			Room room = new Room();

			// Set values of Room class that we want to add
			room.setDescription(description.text() + ", " + address.text());
			room.setPostCode(postCode);
			room.setRent(Integer.parseInt(parts[0].substring(1, parts[0].length())));
			room.setImageURL(class_imageURL.get(1).select("img").attr("abs:src"));
			room.setSellerID(sellerID);
			room.setWebsiteURL("https://www.zoopla.co.uk" + websiteURL.attr("href"));

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
					scrapeZoopla();
				} catch (Exception ex) {
					System.out.println("Exercise Exception: " + ex.getMessage());
				}
				sleep(1000 * crawlDelay);
			}
		} catch (InterruptedException ex) {
			System.out.println("Thread Website3 stopping");
		}
	}

}
