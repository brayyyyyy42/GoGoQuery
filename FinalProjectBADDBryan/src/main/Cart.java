package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.msitem;
import utils.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Cart extends Application {

    private List<CartItem> cartItems = new ArrayList<>();
    private Label grandTotalLabel;
    private Button checkoutButton, backtoshop;
    private BorderPane layout;
    private VBox content;
    private Scene scene;

    public void start(Stage primaryStage) {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1a1a2e;");

        backtoshop = new Button("Back to Shop");

        content = new VBox(15);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.TOP_LEFT);

        grandTotalLabel = new Label("Grand Total: Rp. 0.00");
        grandTotalLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        checkoutButton = new Button("Checkout");
        checkoutButton.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: #1a1a2e;");
        checkoutButton.setOnAction(e -> handleCheckout());

        content.getChildren().addAll(grandTotalLabel, checkoutButton, backtoshop);
        layout.setCenter(content);

        backtoshop.setOnAction(e -> {
            ShopHome shop = new ShopHome();
            Stage regs = new Stage();
            ((Stage) scene.getWindow()).close();
            try {
                shop.start(regs);
            } catch (Exception g) {
                g.printStackTrace();
            }
        });

        loadCartItems();
        displayCartItems();

        scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Shopping Cart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadCartItems() {
        Connection connection = Connect.getInstance().getConnection();
        int userId = getCurrentUserId();
        try {
            String query = "SELECT * FROM mscart WHERE UserID = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int itemId = rs.getInt("ItemID");
                int quantity = rs.getInt("Quantity");
                msitem item = fetchItemById(itemId);
                cartItems.add(new CartItem(item, quantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private msitem fetchItemById(int itemId) {
        Connection connection = Connect.getInstance().getConnection();
        msitem item = null;
        try {
            String query = "SELECT * FROM msitem WHERE ItemID = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, itemId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                item = new msitem(
                        rs.getInt("itemID"),
                        rs.getString("itemName"),
                        rs.getFloat("itemPrice"),
                        rs.getString("itemDesc"),
                        rs.getString("itemCategory"),
                        rs.getInt("itemStock")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    private int getCurrentUserId() {
        return 1; // Example: Replace with the actual logged-in user's ID
    }

    private void displayCartItems() {
        content.getChildren().clear();
        content.getChildren().addAll(grandTotalLabel, checkoutButton, backtoshop);

        if (cartItems.isEmpty()) {
            Label emptyCartLabel = new Label("Your cart is empty.");
            emptyCartLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            content.getChildren().add(emptyCartLabel);
            return;
        }

        for (CartItem cartItem : cartItems) {
            if (cartItem != null && cartItem.getItem() != null) {
                HBox itemBox = new HBox(10);
                itemBox.setAlignment(Pos.CENTER_LEFT);

                Label itemNameLabel = new Label(cartItem.getItem().getItemName());
                itemNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

                Label itemPriceLabel = new Label("Rp. " + cartItem.getItem().getItemPrice());
                itemPriceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

                Spinner<Integer> quantitySpinner = new Spinner<>(1, cartItem.getItem().getItemStock(), cartItem.getQuantity());
                quantitySpinner.setEditable(true);
                quantitySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                    cartItem.setQuantity(newValue);
                    updateGrandTotal();
                });

                Button removeButton = new Button("Remove Item");
                removeButton.setStyle("-fx-background-color: #ff4c4c; -fx-text-fill: white;");
                removeButton.setOnAction(e -> handleRemoveItem(cartItem));

                itemBox.getChildren().addAll(itemNameLabel, itemPriceLabel, quantitySpinner, removeButton);
                content.getChildren().add(itemBox);
            }
        }

        updateGrandTotal();
    }

    private void updateGrandTotal() {
        double grandTotal = 0;
        for (CartItem cartItem : cartItems) {
            grandTotal += cartItem.getItem().getItemPrice() * cartItem.getQuantity();
        }
        grandTotalLabel.setText("Grand Total: Rp. " + grandTotal);
    }

    private void handleRemoveItem(CartItem cartItem) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Remove Item");
        confirmationAlert.setHeaderText("Are you sure you want to remove " + cartItem.getItem().getItemName() + " from your cart?");
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                removeItemFromDatabase(cartItem);
                cartItems.remove(cartItem);
                displayCartItems();
            }
        });
    }

    private void removeItemFromDatabase(CartItem cartItem) {
        Connection connection = Connect.getInstance().getConnection();
        try {
            String query = "DELETE FROM mscart WHERE ItemID = ? AND UserID = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, cartItem.getItem().getItemID());
            pst.setInt(2, getCurrentUserId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addItemToCart(msitem item, int quantity) {
        Connection connection = Connect.getInstance().getConnection();
        int userId = getCurrentUserId();
        try {
            String checkQuery = "SELECT Quantity FROM mscart WHERE UserID = ? AND ItemID = ?";
            PreparedStatement checkPst = connection.prepareStatement(checkQuery);
            checkPst.setInt(1, userId);
            checkPst.setInt(2, item.getItemID());
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                int currentQuantity = rs.getInt("Quantity");
                String updateQuery = "UPDATE mscart SET Quantity = ? WHERE UserID = ? AND ItemID = ?";
                PreparedStatement updatePst = connection.prepareStatement(updateQuery);
                updatePst.setInt(1, currentQuantity + quantity);
                updatePst.setInt(2, userId);
                updatePst.setInt(3, item.getItemID());
                updatePst.executeUpdate();
            } else {
                String insertQuery = "INSERT INTO mscart (UserID, ItemID, Quantity) VALUES (?, ?, ?)";
                PreparedStatement insertPst = connection.prepareStatement(insertQuery);
                insertPst.setInt(1, userId);
                insertPst.setInt(2, item.getItemID());
                insertPst.setInt(3, quantity);
                insertPst.executeUpdate();
            }

            cartItems.add(new CartItem(item, quantity));
            displayCartItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleCheckout() {
        if (cartItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Checkout Failed");
            alert.setHeaderText("Your cart is empty.");
            alert.showAndWait();
            return;
        }

        Connection connection = Connect.getInstance().getConnection();
        try {
            // Memulai transaksi
            connection.setAutoCommit(false);

            // 1. Masukkan data ke transactionheader
            String insertHeaderQuery = "INSERT INTO transactionheader (UserID, DateCreated, Status) VALUES (?, NOW(), 'Pending')";
            PreparedStatement headerPst = connection.prepareStatement(insertHeaderQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            headerPst.setInt(1, getCurrentUserId());
            headerPst.executeUpdate();

            // Ambil TransactionID yang baru dibuat
            ResultSet rs = headerPst.getGeneratedKeys();
            int transactionId = 0;
            if (rs.next()) {
                transactionId = rs.getInt(1);
            }

            // 2. Masukkan setiap item ke transactiondetail
            String insertDetailQuery = "INSERT INTO transactiondetail (TransactionID, ItemID, Quantity) VALUES (?, ?, ?)";
            PreparedStatement detailPst = connection.prepareStatement(insertDetailQuery);

            for (CartItem cartItem : cartItems) {
                msitem item = cartItem.getItem();
                int quantity = cartItem.getQuantity();

                detailPst.setInt(1, transactionId);
                detailPst.setInt(2, item.getItemID());
                detailPst.setInt(3, quantity);
                detailPst.addBatch();
            }
            detailPst.executeBatch();

            
            String updateStockQuery = "UPDATE msitem SET itemStock = itemStock - ? WHERE itemID = ?";
            PreparedStatement stockPst = connection.prepareStatement(updateStockQuery);

            for (CartItem cartItem : cartItems) {
                stockPst.setInt(1, cartItem.getQuantity());
                stockPst.setInt(2, cartItem.getItem().getItemID());
                stockPst.addBatch();
            }
            stockPst.executeBatch();

            
            String deleteCartQuery = "DELETE FROM mscart WHERE UserID = ?";
            PreparedStatement deleteCartPst = connection.prepareStatement(deleteCartQuery);
            deleteCartPst.setInt(1, getCurrentUserId());
            deleteCartPst.executeUpdate();

            
            connection.commit();
            connection.setAutoCommit(true);

           
            cartItems.clear();
            displayCartItems();

            
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Checkout Successful");
            successAlert.setHeaderText("Your transaction has been processed successfully!");
            successAlert.setContentText("Transaction ID: " + transactionId);
            successAlert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback(); // 
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Checkout Failed");
            errorAlert.setHeaderText("An error occurred during checkout. Please try again.");
            errorAlert.showAndWait();
        }
    }


    private void clearCart() {
     
    }

    public static void main(String[] args) {
        launch(args);
    }

    class CartItem {
        private msitem item;
        private int quantity;

        public CartItem(msitem item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public msitem getItem() {
            return item;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
