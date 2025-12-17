package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.transaction;
import utils.Connect;  

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueueManagementPage extends Application {

    private TableView<transaction> tableView;
    private ObservableList<transaction> transactionList;

   
    private Connection connect;

    @Override
    public void start(Stage primaryStage) throws Exception {
       
        connect = Connect.getInstance().getConnection();  

        tableView = new TableView<>();
        transactionList = FXCollections.observableArrayList();

        TableColumn<transaction, Integer> transactionIDCol = new TableColumn<>("Transaction ID");
        transactionIDCol.setCellValueFactory(cellData -> cellData.getValue().transactionIDProperty().asObject());

        TableColumn<transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        tableView.getColumns().addAll(transactionIDCol, statusCol);

        loadData();

        Button sendPackageButton = new Button("Send Package");
        sendPackageButton.setOnAction(e -> sendPackage());

        VBox vbox = new VBox(tableView, sendPackageButton);
        Scene scene = new Scene(vbox, 600, 400);

        primaryStage.setTitle("Queue Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadData() {
        try {
            String query = "SELECT * FROM transactionheader WHERE status = 'Pending'";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int transactionID = resultSet.getInt("transactionID");
                int userID = resultSet.getInt("userID");
                Date dateCreated = resultSet.getDate("dateCreated");
                String status = resultSet.getString("status");

                List<transaction.TransactionDetail> transactionDetails = new ArrayList<>();


                transaction txn = new transaction(transactionID, userID, dateCreated, status, transactionDetails);
                transactionList.add(txn);
            }

            tableView.setItems(transactionList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendPackage() {
        transaction selectedTransaction = tableView.getSelectionModel().getSelectedItem();

        if (selectedTransaction == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No transaction selected");
            alert.setContentText("Please select a transaction to completed.");
            alert.showAndWait();
            return;
        }

        // Update status of the selected transaction to "Completed"
        try {
            String updateQuery = "UPDATE transactionheader SET status = 'Completed' WHERE transactionID = ?";
            PreparedStatement preparedStatement = connect.prepareStatement(updateQuery);
            preparedStatement.setInt(1, selectedTransaction.getTransactionID());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                selectedTransaction.setStatus("Completed");
                tableView.refresh();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setHeaderText("Package sent");
                successAlert.setContentText("The selected transaction has been marked as 'Completed'.");
                successAlert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error updating transaction");
            errorAlert.setContentText("An error occurred while updating the transaction status.");
            errorAlert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
