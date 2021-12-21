package mdx.saad.webscraper;

//JUnit 5 imports
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class TestWebscraper {
	@Test
	@DisplayName("Test Add room for Website 1")
	public void testAddRoom_Website1() {
		Hibernate hibernateInstance = new Hibernate();
		hibernateInstance.init();
		Website1 w1 = new Website1();
		w1.setHibernateInstance(hibernateInstance);
		
		//scrape website for all rooms in 'NW1'
		w1.setPostCode("NW" + 1);
		try {
			w1.scrapeRightMove();
		} catch (Exception ex) {
			System.out.println("Exercise Exception: " + ex.getMessage());
		}

		// create an instance of a room
		Room room = new Room();

		// create a mock room that exists on the website for 'NW1' to check if website was able to scrape it
		room.setDescription("St. Pancras Chambers, Euston Road, London, NW1");
		room.setPostCode("NW1");
		room.setRent(11917);
		room.setImageURL(
				"https://media.rightmove.co.uk:443/dir/crop/10:9-16:9/119k/118930/115206464/118930_KGQ278589_IMG_00_0001_max_476x317.jpeg");
		room.setSellerID(1);
		room.setWebsiteURL("https://www.rightmove.co.uk/properties/115206464#/?channel=RES_LET");
		
		// check if room exists
		assertEquals(hibernateInstance.searchRooms(room), true);
		
		//shutdown hibernate instance
		hibernateInstance.shutDown();
	}

	@Test
	@DisplayName("Test Add room for Website 2")
	public void testAddRoom_Website2() {
		Hibernate hibernateInstance = new Hibernate();
		hibernateInstance.init();
		Website2 w2 = new Website2();
		w2.setHibernateInstance(hibernateInstance);
		w2.setPostCode("NW" + 1);
		try {
			w2.scrapeOpenRent();
		} catch (Exception ex) {
			System.out.println("Exercise Exception: " + ex.getMessage());
		}

		Room room = new Room();

		room.setDescription("1 Bed Flat, Eversholt Street, NW1");
		room.setPostCode("NW1");
		room.setRent(1396);
		room.setImageURL(
				"https://d36pgh4m67wnlt.cloudfront.net/listings/1191979/o_1fdfap9fp1mq3soc1aa9d9u1qt2l270.JPG_homepage.JPG");
		room.setSellerID(1);
		room.setWebsiteURL(
				"https://www.openrent.co.uk/property-to-rent/london/1-bed-flat-eversholt-street-nw1/1266631");

		//check if room exists
		assertEquals(hibernateInstance.searchRooms(room), true);
		hibernateInstance.shutDown();
	}

	@Test
	@DisplayName("Test Add room for Website 3")
	public void testAddRoom_Website3() {
		Hibernate hibernateInstance = new Hibernate();
		hibernateInstance.init();
		Website3 w3 = new Website3();
		w3.setHibernateInstance(hibernateInstance);
		w3.setPostCode("NW" + 1);
		try {
			w3.scrapeZoopla();
		} catch (Exception ex) {
			System.out.println("Exercise Exception: " + ex.getMessage());
		}

		Room room = new Room();

		room.setDescription("Studio to rent, Greenland Street, London NW1 0.1 miles Camden Town 0.3 miles Camden Road");
		room.setPostCode("NW1");
		room.setRent(1326);
		room.setImageURL("https://lid.zoocdn.com/u/2400/1800/bc1071f980d6a100e809aa4f0811b6c00f4d32fd.jpg");
		room.setSellerID(2);
		room.setWebsiteURL("https://www.zoopla.co.uk/to-rent/details/48411508/");
		
		// check if room exists
		assertEquals(hibernateInstance.searchRooms(room), true);
		hibernateInstance.shutDown();
	}

	@Test
	@DisplayName("Test Add room for Website 4")
	public void testAddRoom_Website4() {
		Hibernate hibernateInstance = new Hibernate();
		hibernateInstance.init();
		Website4 w4 = new Website4();
		w4.setHibernateInstance(hibernateInstance);
		w4.setPostCode("NW" + 1);
		try {
			w4.scrapeGumtree();
		} catch (Exception ex) {
			System.out.println("Exercise Exception: " + ex.getMessage());
		}

		Room room = new Room();

		room.setDescription("Studio To Rent Camden High Street, Camden Town NW1 0NE");
		room.setPostCode("NW1");
		room.setRent(751);
		room.setImageURL("https://i.ebayimg.com/00/s/NTAwWDUwMA==/z/-A4AAOSwF8ZgdY9p/$_99.JPG");
		room.setSellerID(3);
		room.setWebsiteURL(
				"https://www.gumtree.com/p/property-to-rent/studio-to-rent-camden-high-street-camden-town-nw1-0ne/1402360521");
		
		//check if room exists
		assertEquals(hibernateInstance.searchRooms(room), true);
		hibernateInstance.shutDown();
	}

	@Test
	@Order(1)
	@DisplayName("Test if program stores new seller with unique name/contact_info")
	public void testSearchSeller_newEntry() {
		Hibernate hibernateInstance = new Hibernate();
		hibernateInstance.init();
		int tableSize = hibernateInstance.sellerTableSize();//check initial sellers table size
		hibernateInstance.searchSeller("New Seller", "12345");//search for seller. create if none found
		assertEquals(hibernateInstance.sellerTableSize(), tableSize + 1);// size expected to increment as new row added
		hibernateInstance.shutDown();
	}

	@Test
	@Order(2)
	@DisplayName("Test to prevent redundancies in the seller table i.e program does not store new seller with same name/contact_info")
	public void testSearchSeller_existingEntry() {
		Hibernate hibernateInstance = new Hibernate();
		hibernateInstance.init();
		int tableSize = hibernateInstance.sellerTableSize();//check initial sellers table size
		hibernateInstance.searchSeller("New Seller", "12345");//search for seller. should not create new seller as it was created in previous test
		assertEquals(hibernateInstance.sellerTableSize(), tableSize);// size expected to remain same as no new row added
		hibernateInstance.deleteSeller("New Seller", "12345");// delete new seller
		hibernateInstance.shutDown();
	}
}
