package models;

public class CartItem {

    private int userId;
    private int itemId;
    private int quantity;
    private float price;
    private String imageUrl;  
    private String itemName; 

    public CartItem(int userId, int itemId, int quantity, float price, String itemName, String imageUrl) {
        this.userId = userId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
        this.itemName = itemName;  
        this.imageUrl = imageUrl;  
    }

    public CartItem(int userId, int itemId, int quantity) {
        this.userId = userId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = 0.0f; // Default float price to 0 if not provided
        this.itemName = ""; //Default empty
        this.imageUrl = ""; //Default empty
    }

   
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemName() {  // Getter for itemName
        return itemName;
    }

    public void setItemName(String itemName) {  // Setter for itemName
        this.itemName = itemName;
    }
}
