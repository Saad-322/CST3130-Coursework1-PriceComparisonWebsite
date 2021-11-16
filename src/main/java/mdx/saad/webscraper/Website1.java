package mdx.saad.webscraper;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Website1 extends Thread {
	int crawlDelay;
	String postCode;
	String outCode;
	Hibernate hibernateInstance = new Hibernate();
	
	//getters and setters
	public void setCrawlDelay(int cd) {
		crawlDelay = cd;
	}
	public void setPostCode(String pc) {
		postCode = pc;
	}
	public void setOutCode(String ou) {
		outCode = ou;
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
	public String getOutCode() {
		return outCode;
	}
	public Hibernate getHibernateInstance() {
		return hibernateInstance;
	}

	public Website1() {}

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
		
		// Work through the products
		for (int i = 0; i < rooms.size(); ++i) {

			// Get the product description
			Elements description = rooms.get(i).select(".propertyCard-address");// first part of class
			Elements price = rooms.get(i).select(".propertyCard-priceValue");// first ppart of class}
			Elements websiteURL = rooms.get(i).select(".propertyCard-link");
			Elements imageURL = rooms.get(i).select("img");
			
			String[] parts = price.text().trim().split(" ");
			parts[0] = parts[0].replace(",", "");
			
			int sellerID = hibernateInstance.searchSeller("Saad","07459353041");
		      //Create an instance of a Room class 
		      Room room = new Room();

		      //Set values of Room class that we want to add
		      room.setDescription(description.text());
		      room.setPostCode(postCode);
		      room.setRent(Integer.parseInt(parts[0].substring(1, parts[0].length())));
		      room.setImageURL(imageURL.attr("abs:src"));
		      room.setSellerID(sellerID);
		      room.setWebsiteURL("https://www.rightmove.co.uk"+ websiteURL.attr("href"));
					
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
