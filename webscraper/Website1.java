package mdx.saad.webscraper;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

/**
 * Allows data to be scraped from the website 'RightMove' which is stored in
 * database using hibernate
 */
public class Website1 extends Thread {
	int crawlDelay;
	String postCode;
	String outCode;
	Hibernate hibernateInstance = new Hibernate();

	/**
	 * Sets the crawl delay.
	 * 
	 * @param cd the crawlDelay to set
	 */
	public void setCrawlDelay(int cd) {
		crawlDelay = cd;
	}

	/**
	 * Sets the postcode.
	 * 
	 * @param pc the postCode to set
	 */
	public void setPostCode(String pc) {
		postCode = pc;
	}

	/**
	 * Sets the outcode.
	 * 
	 * @param ou the OutCode to set
	 */
	public void setOutCode(String ou) {
		outCode = ou;
	}

	/**
	 * Sets the Hibernate Instance.
	 * 
	 * @param h the HibernateInstance to set
	 */
	public void setHibernateInstance(Hibernate h) {
		hibernateInstance = h;
	}

	/**
	 * gets the crawl delay.
	 */
	public int getCrawlDelay() {
		return crawlDelay;
	}

	/**
	 * gets the post code.
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * Sets the outcode.
	 */
	public String getOutCode() {
		return outCode;
	}

	/**
	 * gets the hibernate instance.
	 */
	public Hibernate getHibernateInstance() {
		return hibernateInstance;
	}

	public Website1() {
	}

	/**
	 * converts postcode to its respective outcode that the website url can use
	 * 
	 * @param pc helps in the conversion of postcode to outcode
	 */
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

	/**
	 * scrapes the data from website 'RightMove' and stores it in database using
	 * hibernate
	 */
	public void scrapeRightMove() throws Exception {

		postCodeConversion(postCode);
		// Download HTML document from website
		Document doc = Jsoup
				.connect("https://www.rightmove.co.uk/property-to-rent/find.html?locationIdentifier=OUTCODE%5E"
						+ outCode + "&sortType=1&propertyTypes=&mustHave=&dontShow=&furnishTypes=&keywords=")
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").get();

		// Get all of the rooms on the page
		Elements rooms = doc.select(".l-searchResult");// check to see the box for the whole ad

		int sellerID;

		// Work through the rooms
		for (int i = 0; i < rooms.size(); ++i) {

			// Get the room details
			Elements description = rooms.get(i).select(".propertyCard-address");
			Elements price = rooms.get(i).select(".propertyCard-priceValue");
			Elements websiteURL = rooms.get(i).select(".propertyCard-link");
			Elements imageURL = rooms.get(i).select("img");

			// string processing to get monthly rent price
			String[] parts = price.text().trim().split(" ");
			parts[0] = parts[0].replace(",", "");

			// get seller details
			Elements seller_number = rooms.get(i).select(".propertyCard-contactsPhoneNumber");
			Elements seller_name = rooms.get(i).select(".propertyCard-branchLogo a");
			sellerID = hibernateInstance.searchSeller(seller_name.attr("title"), seller_number.text());

			// Create an instance of a Room class
			Room room = new Room();

			// Set values of Room class that we want to add
			room.setDescription(description.text());
			room.setPostCode(postCode);
			room.setRent(Integer.parseInt(parts[0].substring(1, parts[0].length())));
			room.setImageURL(imageURL.attr("abs:src"));
			room.setSellerID(sellerID);
			room.setWebsiteURL("https://www.rightmove.co.uk" + websiteURL.attr("href"));

			// add room to database
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
					scrapeRightMove();
				} catch (Exception ex) {
					System.out.println("Exercise Exception: " + ex.getMessage());
				}
				sleep(1000 * crawlDelay);
			}
		} catch (InterruptedException ex) {
			System.out.println("Thread Website1 stopping");
		}
	}

}
