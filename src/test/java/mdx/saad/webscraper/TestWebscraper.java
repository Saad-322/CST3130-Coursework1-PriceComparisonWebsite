package mdx.saad.webscraper;

//JUnit 5 imports
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class TestWebscraper 
{	
    @Test
    @DisplayName("Test Add room for Website 1")
    public void testAddRoom_Website1()
    {
    	Hibernate hibernateInstance = new Hibernate();
    	hibernateInstance.init();
    	Website1 w1 = new Website1();//need to edit according to spring
    	w1.setHibernateInstance(hibernateInstance);
		w1.setPostCode("NW"+1);
		// Web Scraping code goes here
		try {
			// jSoup Exercises
			w1.scrapeRightMove();
		} catch (Exception ex) {
			System.out.println("Exercise Exception: " + ex.getMessage());
		}
		
    	//create an instance of a room
		Room room = new Room();

	      //Set values of Room class that we want to add
	      room.setDescription("60 Harmood House, Harmood Street, Camden, London, NW18DY");
	      room.setPostCode("NW1");
	      room.setRent(600);
	      room.setImageURL("https://media.rightmove.co.uk:443/dir/crop/10:9-16:9/111k/110330/108335915/110330_ARU-233552_IMG_00_0000_max_476x317.jpeg");
	      room.setSellerID(1);
	      room.setWebsiteURL("https://www.rightmove.co.uk/properties/108335915#/?channel=RES_LET");
	    	//check if room exists
	      assertEquals(hibernateInstance.searchRooms(room), true );
	        hibernateInstance.shutDown();
    }
    @Test
    @DisplayName("Test Add room for Website 2")
    public void testAddRoom_Website2()
    {
    	Hibernate hibernateInstance = new Hibernate();
    	hibernateInstance.init();
    	Website2 w2 = new Website2();
    	w2.setHibernateInstance(hibernateInstance);
		w2.setPostCode("NW"+1);
		// Web Scraping code goes here
		try {
			// jSoup Exercises
			w2.scrapeOpenRent();
		} catch (Exception ex) {
			System.out.println("Exercise Exception: " + ex.getMessage());
		}
		
		//take a property form internet to check
    	//create an instance of a room
		Room room = new Room();

	      //Set values of Room class that we want to add
	      room.setDescription("2 Bed Maisonette, Troutbeck, NW1");
	      room.setPostCode("NW1");
	      room.setRent(2250);
	      room.setImageURL("https://d36pgh4m67wnlt.cloudfront.net/listings/699377/o_1du5p80bn2bc15843cj4ig1tsr1h.JPG_homepage.JPG");
	      room.setSellerID(1);
	      room.setWebsiteURL("https://www.openrent.co.uk/property-to-rent/london/2-bed-maisonette-troutbeck-nw1/1146332");
	    	//check if room exists
	        assertEquals(hibernateInstance.searchRooms(room), true );
	        hibernateInstance.shutDown();
    }
//    @Test
//    @Order(1)
//    @DisplayName("Test Add room for Website 3")
//    public void testAddRoom_Website3()
//    {
//    	Hibernate hibernateInstance = new Hibernate();
//    	hibernateInstance.init();
//    	Website3 w3 = new Website3(hibernateInstance);
//		w3.setHibernateInstance(hibernateInstance);
//		w3.setPostCode("NW"+1);
//		// Web Scraping code goes here
//		try {
//			// jSoup Exercises
//			w3.scrapeZoopla();
//		} catch (Exception ex) {
//			System.out.println("Exercise Exception: " + ex.getMessage());
//		}
//		
//		//take a property form internet to check
//    	//create an instance of a room
//		Room room = new Room();
//
//	      //Set values of Room class that we want to add
//	      room.setDescription("5 bed terraced house to rent ");
//	      room.setPostCode("NW1");
//	      room.setRent(75833);
//	      room.setImageURL("https://lid.zoocdn.com/u/2400/1800/f20d680373c13dc191aad526ff37fd7b8aae01e2.jpg:p");
//	      room.setSellerID(1);
//	      room.setWebsiteURL("https://www.zoopla.co.uk/to-rent/details/60137171/?search_identifier=71fb343ad6bf2fc83c0a0044e2e7739e");
//	    	//check if room exists
//	        assertEquals(hibernateInstance.searchRooms(room), true );
//	        hibernateInstance.shutDown();
//    }
    @Test
    @DisplayName("Test Add room for Website 4")
    public void testAddRoom_Website4()
    {
    	Hibernate hibernateInstance = new Hibernate();
    	hibernateInstance.init();
    	Website4 w4 = new Website4();
    	w4.setHibernateInstance(hibernateInstance);
		w4.setPostCode("NW"+1);
		// Web Scraping code goes here
		try {
			// jSoup Exercises
			w4.scrapeGumtree();
		} catch (Exception ex) {
			System.out.println("Exercise Exception: " + ex.getMessage());
		}
		
		//take a property form internet to check
    	//create an instance of a room
		Room room = new Room();

	      //Set values of Room class that we want to add
	      room.setDescription("Studio To Rent Camden High Street, Camden Town NW1 0NE");
	      room.setPostCode("NW1");
	      room.setRent(751);
	      room.setImageURL("https://www.gumtree.com/search?featured_filter=false&q=NW1&search_location=NW1&search_category=property-to-rent&urgent_filter=false&sort=price_lowest_first&search_distance=3&search_scope=false&photos_filter=false");
	      room.setSellerID(3);
	      room.setWebsiteURL("https://www.gumtree.com/p/property-to-rent/studio-to-rent-camden-high-street-camden-town-nw1-0ne/1402360521");
	        assertEquals(hibernateInstance.searchRooms(room), true );
	        hibernateInstance.shutDown();
    }
    
    @Test
    @Order(1)
    @DisplayName("Test if program stores new seller with unique name/contact_info")
    public void testSearchSeller_newEntry()
    {
    	Hibernate hibernateInstance = new Hibernate();
    	hibernateInstance.init();
    	int tableSize = hibernateInstance.sellerTableSize();
    	hibernateInstance.searchSeller("New Seller","12");
    	assertEquals(hibernateInstance.sellerTableSize(), tableSize+1 );//size expected to increment as new row added
        hibernateInstance.shutDown();
    }
    
    @Test
    @Order(2)
    @DisplayName("Test to prevent redundancies in the seller table i.e program does not store new seller with same name/contact_info")
    public void testSearchSeller_existingEntry()
    {
    	Hibernate hibernateInstance = new Hibernate();
    	hibernateInstance.init();
    	int tableSize = hibernateInstance.sellerTableSize();
    	hibernateInstance.searchSeller("New Seller","12"); 	
    	assertEquals(hibernateInstance.sellerTableSize(), tableSize);//size expected to remain same as no new row added
        hibernateInstance.shutDown();
    }
    
//    @Test
//    @DisplayName("Test WebsiteURL validity")
//    public void TestURL_Valid()
//    {
//        assertTrue( true );
//    }
    
    
}
