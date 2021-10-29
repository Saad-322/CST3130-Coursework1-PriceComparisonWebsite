package mdx.saad.webscraper;

import java.util.List;

//Hibernate imports
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


/** Simple Hibernate example that uses annotation to specify the mapping between 
*  a Room object and the Rooms table in the price_comparison database. */
public class HibernateExample {
  //Creates new Sessions when we need to interact with the database 
  private SessionFactory sessionFactory;
  
  
  /** Empty constructor */
  HibernateExample() {
  }

  
  /** Sets up the session factory.
   *  Call this method first.  */
  public void init(){
      try {
          //Create a builder for the standard service registry
          StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

          //Load configuration from hibernate configuration file.
          //Here we are using a configuration file that specifies Java annotations.
          standardServiceRegistryBuilder.configure("resources/hibernate-annotations.xml"); 

          //Create the registry that will be used to build the session factory
          StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
          try {
              //Create the session factory - this is the goal of the init method.
              sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
          }
          catch (Exception e) {
                  /* The registry would be destroyed by the SessionFactory, 
                      but we had trouble building the SessionFactory, so destroy it manually */
                  System.err.println("Session Factory build failed.");
                  e.printStackTrace();
                  StandardServiceRegistryBuilder.destroy( registry );
          }

          //Ouput result
          System.out.println("Session factory built.");

      }
      catch (Throwable ex) {
          // Make sure you log the exception, as it might be swallowed
          System.err.println("SessionFactory creation failed." + ex);
      }
  }
  
  
  /** Adds a new Room to the database */
  public void addRoom(String description, int rent, String postCode, String websiteURL, String imageURL){
      //Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();

      //Create an instance of a Room class 
      RoomsAnnotation Room = new RoomsAnnotation();

      //Set values of Room class that we want to add
      Room.setDescription(description);
      Room.setPostCode(postCode);
      Room.setRent(rent);
      Room.setImageURL(imageURL);
      Room.setSellerID(1);
      Room.setWebsiteURL(websiteURL);

      //Start transaction
      session.beginTransaction();

      //Add Room to database - will not be stored until we commit the transaction
      session.save(Room);

      //Commit transaction to save it to database
      session.getTransaction().commit();
      
      //Close the session and release database connection
      session.close();
      System.out.println("Room added to database with ID: " + Room.getId());
  }
  public void addSeller(String sellerName, String sellerContactInfo) {
      //Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();

      //Create an instance of a Room class 
      Seller seller = new Seller();

      //Set values of Room class that we want to add
      seller.setName(sellerName);
      seller.setContactInfo(sellerContactInfo);


      //Start transaction
      session.beginTransaction();

      //Add Room to database - will not be stored until we commit the transaction
      session.save(seller);

      //Commit transaction to save it to database
      session.getTransaction().commit();
      
      //Close the session and release database connection
      session.close();
      System.out.println("Seller added to database with ID: " + seller.getId());
  }
  public void shutDown() {
	  sessionFactory.close();
  }
  public void updateRoom() {
      //Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();

      //Create an instance of a Room class 
      RoomsAnnotation Room = new RoomsAnnotation();

      //set ID of cereal class - this controls the row in the table that we want to update
      Room.setID(1);
      
      //Set values of Room class that we want to add
      Room.setDescription("example2");
      Room.setPostCode("example2");
      Room.setRent(999);
      Room.setImageURL("example22");
      Room.setSellerID(1);
      Room.setWebsiteURL("example2");

      //Start transaction
      session.beginTransaction();

      //Add Room to database - will not be stored until we commit the transaction
      session.update(Room);

      //Commit transaction to save it to database
      session.getTransaction().commit();
      
      //Close the session and release database connection
      session.close();
      System.out.println("Cereal updated in database. ID: " + Room.getId());
  }
  public void searchRooms() {
      //Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();
      
      session.beginTransaction();
      
      List<RoomsAnnotation> roomList = session.createQuery("from RoomsAnnotation where rent=2566").getResultList();
      for(RoomsAnnotation room : roomList) {
    	  System.out.println(room.toString());
      }
    //Close the session and release database connection
      session.close();
  }
  public Boolean searchSellers(String sellerName) {
      //Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();
      
      session.beginTransaction();
      
      List<Seller> sellerList = session.createQuery("from Seller where name ="+sellerName).getResultList();
      
      if (sellerList.size()==0) {
    	  session.close();
    	  return false;
      }
      else {
    	  session.close();
    	  return true;
      }
//      for(Seller seller : sellerList) {
//    	  System.out.println(seller.toString());
//      }
    //Close the session and release database connection

  }
  
//  public Seller getSeller(String sellerName / number) {
//	  //search for seller using HQL
//	  Create if not found
//	  return seller
//			  
//  }
//  
  public void deleteRoom() {
      //Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();
      
      session.beginTransaction();
      
      Object persistentInstance = session.load(RoomsAnnotation.class, 2);
      
      if(persistentInstance != null) {
    	  session.delete(persistentInstance);
      }
      
      //update database
      session.getTransaction().commit();
      
      //Close the session and release database connection
      session.close();
      System.out.println("Rooms updated in Database. ID: 2");
  }
  
}

	
