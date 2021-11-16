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
public class Hibernate {
  //Creates new Sessions when we need to interact with the database 
  private SessionFactory sessionFactory;
  
  
  /** Empty constructor */
  Hibernate() {
  }

  
  /** Sets up the session factory.
   *  Call this method first.  */
  public void init(){
      try {
          //Create a builder for the standard service registry
          StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

          //Load configuration from hibernate configuration file.
          //Here we are using a configuration file that specifies Java annotations.
          standardServiceRegistryBuilder.configure("hibernate-annotations.cfg.xml"); 

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
  public void addRoom(Room room){
      //Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();

      //Start transaction
      session.beginTransaction();

      //Add Room to database - will not be stored until we commit the transaction
      session.save(room);

      //Commit transaction to save it to database
      session.getTransaction().commit();
      
      //Close the session and release database connection
      session.close();
      System.out.println("Room added to database with ID: " + room.getId());
  }
  public void shutDown() {
	  sessionFactory.close();
  }
  public void updateRoom() {
      //Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();

      //Create an instance of a Room class 
      Room room = new Room();

      //set ID of cereal class - this controls the row in the table that we want to update
      room.setID(1);
      
      //Set values of room class that we want to add
      room.setDescription("example2");
      room.setPostCode("example2");
      room.setRent(999);
      room.setImageURL("example22");
      room.setSellerID(1);
      room.setWebsiteURL("example2");

      //Start transaction
      session.beginTransaction();

      //Add Room to database - will not be stored until we commit the transaction
      session.update(room);

      //Commit transaction to save it to database
      session.getTransaction().commit();
      
      //Close the session and release database connection
      session.close();
      System.out.println("Cereal updated in database. ID: " + room.getId());
  }
  public Boolean searchRooms(Room room) {
      //Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();
      
      session.beginTransaction();
      
      List<Room> roomList = session.createQuery("from Room where rent="+room.getRent()+" and description='"+room.getDescription()+"' and postCode='"+room.getPostCode()+"' and imageURL='"+room.getImageURL()+"' and websiteURL='"+room.getWebsiteURL()+"'").getResultList();
      if (roomList.size()==0) {
          session.close();
    	  return false;
      }
      else {
    	    //Close the session and release database connection
          session.close();
    	  return true;
      }   

  }
  public int searchSeller(String name, String contact_info) {
      //Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();
      
      session.beginTransaction();
      
      List<Seller> sellerList = session.createQuery("from Seller where name='"+name+"' and contactInfo='"+contact_info+"'").getResultList();
      if (sellerList.size()==0) {
		Seller seller = new Seller();
		seller.setName(name);
		seller.setContactInfo(contact_info);
	    //Add Room to database - will not be stored until we commit the transaction
	    session.save(seller);

	    //Commit transaction to save it to database
	    session.getTransaction().commit();
    	session.close();
    	return seller.getId();
      }
      else {
    	  Object sellerID = session.createQuery("select id from Seller where name='"+name+"' and contactInfo='"+contact_info+"'").getSingleResult();
    	  session.close();
    	  return (int)sellerID;  
      }
  }
  public int sellerTableSize() {
	//Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();
      
      session.beginTransaction();
      
      List<Seller> sellerList = session.createQuery("from Seller").getResultList();
      
      //Close the session and release database connection
      session.close();
      return sellerList.size();
     
  }
  public void deleteRoom() {
      //Get a new Session instance from the session factory
      Session session = sessionFactory.getCurrentSession();
      
      session.beginTransaction();
      
      Object persistentInstance = session.load(Room.class, 2);
      
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

	
