package mdx.saad.webscraper;

import javax.persistence.*;


/** Represents a Rooms.
    Java annotation is used for the mapping. */
@Entity
@Table(name="rooms")
public class RoomsAnnotation {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "rent")
    private int rent;
    
    @Column(name = "postcode")
    private String postCode;
    
    @Column(name = "image_url")
    private String imageURL;
    
    @Column(name = "seller_id")
    private int sellerID;
    
    @Column(name = "website_url")
    private String websiteURL;

    
    /** Empty constructor */
    public RoomsAnnotation(){
    }
    
    //Getters and setters
    public int getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public int getRent() {
        return rent;
    }
    public String getPostCode() {
        return postCode;
    }
    public String getImageURL() {
        return imageURL;
    }
    public int getsellerID() {
        return sellerID;
    }
    public String getWebsiteURL() {
        return websiteURL;
    }
    public void setID(int id) {
        this.id = id;
    }
    public void setDescription(String Description) {
        this.description = Description;
    }
    public void setRent(int Rent) {
        this.rent = Rent;
    }
    public void setPostCode(String PostCode) {
        this.postCode = PostCode;
    }

    public void setImageURL(String ImageURL) {
        this.imageURL = ImageURL;
    }
    public void setSellerID(int SellerID) {
    	this.sellerID = SellerID;
    }
    public void setWebsiteURL(String WebsiteURL) {
    	this.websiteURL = WebsiteURL;
    }
        
    /** Returns a String representation of the Rooms */
    public String toString(){
        String str = "Rooms. id: " + id + "; Description: " +
                 description + "; Rent: " + rent + "; Postcode: " + postCode + "; Address: TBD";
        return str;
    } 
}

