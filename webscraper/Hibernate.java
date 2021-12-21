package mdx.saad.webscraper;

import java.util.List;

//Hibernate imports
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Uses Hibernate to specify the mapping between a Room/Seller object and the
 * rooms/sellers table in the database.
 */
public class Hibernate {
	// Creates new Sessions when we need to interact with the database
	private SessionFactory sessionFactory;

	/** Empty constructor */
	Hibernate() {
	}

	/**
	 * Sets up the session factory. Call this method first.
	 */
	public void init() {
		try {
			// Create a builder for the standard service registry
			StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

			// Load configuration from hibernate configuration file.
			standardServiceRegistryBuilder.configure("hibernate-annotations.cfg.xml");

			// Create the registry that will be used to build the session factory
			StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
			try {
				// Create the session factory - this is the goal of the init method.
				sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
			} catch (Exception e) {
				/*
				 * The registry would be destroyed by the SessionFactory, but we had trouble
				 * building the SessionFactory, so destroy it manually
				 */
				System.err.println("Session Factory build failed.");
				e.printStackTrace();
				StandardServiceRegistryBuilder.destroy(registry);
			}

			// Ouput the result
			System.out.println("Session factory built.");

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("SessionFactory creation failed." + ex);
		}
	}

	/**
	 * Adds a new Room to the database
	 * 
	 * @param room the room to be added to the database
	 */
	public void addRoom(Room room) {
		// Get a new Session instance from the session factory
		Session session = sessionFactory.getCurrentSession();

		// Start transaction
		session.beginTransaction();

		// Add Room to database - will not be stored until we commit the transaction
		session.save(room);

		// Commit transaction to save it to database
		session.getTransaction().commit();

		// Close the session and release database connection
		session.close();
		System.out.println("Room added to database with ID: " + room.getId());
	}

	/**
	 * shuts down the session factory
	 */
	public void shutDown() {
		sessionFactory.close();
	}

	/**
	 * searches if a specified room is present rooms table in the database
	 * 
	 * @param room the room to be searched for in the database
	 * @return boolean variable signalling if the room is present in the database or
	 *         not
	 */
	public Boolean searchRooms(Room room) {
		// Get a new Session instance from the session factory
		Session session = sessionFactory.getCurrentSession();

		// Start transaction
		session.beginTransaction();

		// check if the room recieved as argument is present in the database
		List<Room> roomList = session.createQuery("from Room where rent=" + room.getRent() + " and description='"
				+ room.getDescription() + "' and postCode='" + room.getPostCode() + "' and imageURL='"
				+ room.getImageURL() + "' and websiteURL='" + room.getWebsiteURL() + "'").getResultList();
		// if not present, then return false
		if (roomList.size() == 0) {
			// Close the session and release database connection
			session.close();
			return false;
		} else {// return true if room is presend
			// Close the session and release database connection
			session.close();
			return true;
		}

	}

	/**
	 * searches if a specified seller is present in the database
	 * 
	 * @param name         the name of the seller
	 * @param contact_info of the seller
	 * @return id of the seller in the sellers table
	 */
	public int searchSeller(String name, String contact_info) {
		// Get a new Session instance from the session factory
		Session session = sessionFactory.getCurrentSession();

		session.beginTransaction();

		// check sellers table in database to see if it contains a specified seller
		try {
			List<Seller> sellerList = session
					.createQuery("from Seller where name='" + name + "' and contactInfo='" + contact_info + "'")
					.getResultList();
			// seller not found
			if (sellerList.size() == 0) {
				//create new seller with the provided name and contact_info
				Seller seller = new Seller();
				seller.setName(name);
				seller.setContactInfo(contact_info);
				// Add Seller to database - will not be stored until we commit the transaction
				session.save(seller);

				// Commit transaction to save it to database
				session.getTransaction().commit();
				session.close();
				return seller.getId();
			} else {// seller found. get sellerID from sellers table in database
				Object sellerID = session.createQuery(
						"select id from Seller where name='" + name + "' and contactInfo='" + contact_info + "'")
						.getSingleResult();
				session.close();
				return (int) sellerID;
			}
		} catch (Exception e) {//error in extracting seller details
			session.close();
			return 2;//seller_contact_info = "none", seller_name = "none"
		}
	}

	/**
	 * checks the size of the seller table
	 * 
	 * @return integer representing size of seller table
	 */
	public int sellerTableSize() {
		// Get a new Session instance from the session factory
		Session session = sessionFactory.getCurrentSession();

		session.beginTransaction();

		//store entries in sellers table in a list
		List<Seller> sellerList = session.createQuery("from Seller").getResultList();

		// Close the session and release database connection
		session.close();
		return sellerList.size();

	}

	/*
	 * delete rooms from the rooms table that are stored in database as a result of
	 * the tests
	 */
	public void deleteRoom() {
		// Get a new Session instance from the session factory
		Session session = sessionFactory.getCurrentSession();

		session.beginTransaction();

		Object persistentInstance = session.load(Room.class, 2);

		if (persistentInstance != null) {
			session.delete(persistentInstance);
		}

		// update database
		session.getTransaction().commit();

		// Close the session and release database connection
		session.close();
		System.out.println("Rooms updated in Database");
	}

	/*
	 * deletes a specified seller from the seller table
	 */
	public void deleteSeller(String name, String contact_info) {
		// Get a new Session instance from the session factory
		Session session = sessionFactory.getCurrentSession();

		session.beginTransaction();

		//find the specified seller in database
		Object sellerID = session
				.createQuery("select id from Seller where name='" + name + "' and contactInfo='" + contact_info + "'")
				.getSingleResult();

		Object persistentInstance = session.load(Seller.class, (int) sellerID);

		//delete seller from database
		if (persistentInstance != null) {
			session.delete(persistentInstance);
		}

		// update database
		session.getTransaction().commit();

		// Close the session and release database connection
		session.close();
		System.out.println("Sellers updated in Database. ID: " + (int) sellerID);
	}

}
