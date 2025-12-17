package models;

import javafx.beans.property.*;
import java.util.Date;
import java.util.List;

public class transaction {

    private IntegerProperty transactionID;
    private IntegerProperty userID;
    private ObjectProperty<Date> dateCreated;
    private StringProperty status;
    private List<TransactionDetail> transactionDetails; // List to hold transaction details

    public transaction(Integer transactionID, Integer userID, Date dateCreated, String status, List<TransactionDetail> transactionDetails) {
        this.transactionID = new SimpleIntegerProperty(transactionID);
        this.userID = new SimpleIntegerProperty(userID);
        this.dateCreated = new SimpleObjectProperty<>(dateCreated);
        this.status = new SimpleStringProperty(status);
        this.transactionDetails = transactionDetails;
    }

    
    public IntegerProperty transactionIDProperty() {
        return transactionID;
    }

    public int getTransactionID() {
        return transactionID.get();
    }

    public void setTransactionID(int transactionID) {
        this.transactionID.set(transactionID);
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public int getUserID() {
        return userID.get();
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public ObjectProperty<Date> dateCreatedProperty() {
        return dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated.get();
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public List<TransactionDetail> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(List<TransactionDetail> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public void addTransactionDetail(TransactionDetail detail) {
        this.transactionDetails.add(detail);
    }
    
    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (TransactionDetail detail : transactionDetails) {
            totalQuantity += detail.getQuantity();
        }
        return totalQuantity;
    }

    public static class TransactionDetail {

        private Integer itemID;
        private Integer quantity;

        public TransactionDetail(Integer itemID, Integer quantity) {
            this.itemID = itemID;
            this.quantity = quantity;
        }

        public Integer getItemID() {
            return itemID;
        }

        public void setItemID(Integer itemID) {
            this.itemID = itemID;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
