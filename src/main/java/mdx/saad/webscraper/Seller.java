package mdx.saad.webscraper;

import javax.persistence.*;


/** Represents a Rooms.
    Java annotation is used for the mapping. */
@Entity
@Table(name="sellers")
public class Seller {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "contact_info")
    private String contactInfo;
    
    /** Empty constructor */
    public Seller(){
    }
    
    //Getters and setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String n) {
        this.name = n;
    }
    public void setContactInfo(String ci) {
        this.contactInfo = ci;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getContactInfo() {
    	return contactInfo;
    }
    
}