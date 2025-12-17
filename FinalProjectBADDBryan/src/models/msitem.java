package models;

public class msitem {
	private Integer ItemID;
	private String ItemName;
	private Float ItemPrice;
	private String ItemDesc;
	private String ItemCategory;
	private Integer ItemStock;
	public msitem(Integer itemID, String itemName, Float itemPrice, String itemDesc, String itemCategory,
			Integer itemStock) {
		super();
		ItemID = itemID;
		ItemName = itemName;
		ItemPrice = itemPrice;
		ItemDesc = itemDesc;
		ItemCategory = itemCategory;
		ItemStock = itemStock;
	}
	public Integer getItemID() {
		return ItemID;
	}
	public void setItemID(Integer itemID) {
		ItemID = itemID;
	}
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public Float getItemPrice() {
		return ItemPrice;
	}
	public void setItemPrice(Float itemPrice) {
		ItemPrice = itemPrice;
	}
	public String getItemDesc() {
		return ItemDesc;
	}
	public void setItemDesc(String itemDesc) {
		ItemDesc = itemDesc;
	}
	public String getItemCategory() {
		return ItemCategory;
	}
	public void setItemCategory(String itemCategory) {
		ItemCategory = itemCategory;
	}
	public Integer getItemStock() {
		return ItemStock;
	}
	public void setItemStock(Integer itemStock) {
		ItemStock = itemStock;
	}
	
}
