package com.example.demo;

import com.example.demo.utils.DatabaseConnection;
import com.example.demo.utils.FXMLScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.*;

public class CustomerController {

    @FXML private Button customers_clearBtn;
    @FXML private Button customers_deleteBtn;
    @FXML private TextField customers_search;
    @FXML private AnchorPane customers_form;

    @FXML private Label customers_movieTitle;
    @FXML private Label customers_genre;
    @FXML private Label customers_time;
    @FXML private Label customers_date;
    @FXML private Label customers_ticketNumber;
    @FXML private Label username;

    @FXML private TableView<Customer> customers_tableView;
    @FXML private TableColumn<Customer, String> customers_col_movieTitle;
    @FXML private TableColumn<Customer, String> customers_col_genre;
    @FXML private TableColumn<Customer, String> customers_col_tableNo;
    @FXML private TableColumn<Customer, String> customers_col_time;
    @FXML private TableColumn<Customer, String> customers_col_date;
    @FXML private TableColumn<Customer, String> customers_col_totalPayment;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        customers_col_movieTitle.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        customers_col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        customers_col_tableNo.setCellValueFactory(new PropertyValueFactory<>("tableNo"));
        customers_col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
        customers_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customers_col_totalPayment.setCellValueFactory(new PropertyValueFactory<>("totalPayment"));

        loadCustomerData();

        customers_tableView.setOnMouseClicked(e -> {
            Customer selected = customers_tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                customers_movieTitle.setText(selected.getMovieTitle());
                customers_genre.setText(selected.getGenre());
                customers_time.setText(selected.getTime());
                customers_date.setText(selected.getDate());
                customers_ticketNumber.setText(selected.getTicketNumber());
            }
        });

        customers_search.textProperty().addListener((obs, oldVal, newVal) -> filterCustomers(newVal));
    }

    private void loadCustomerData() {
        customerList.clear();
        try (Connection conn = new DatabaseConnection().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("movie_title"),
                        rs.getString("genre"),
                        rs.getString("table_no"),
                        rs.getString("time"),
                        rs.getString("date"),
                        rs.getString("total_payment"),
                        rs.getString("ticket_number")
                );
                customerList.add(customer);
            }
            customers_tableView.setItems(customerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterCustomers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            customers_tableView.setItems(customerList);
            return;
        }
        ObservableList<Customer> filtered = FXCollections.observableArrayList();
        for (Customer c : customerList) {
            if (c.getMovieTitle().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(c);
            }
        }
        customers_tableView.setItems(filtered);
    }

    @FXML
    void handleDeleteCustomer(ActionEvent event) {
        Customer selected = customers_tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try (Connection conn = new DatabaseConnection().getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM customers WHERE id = ?")) {

                stmt.setInt(1, selected.getId());
                stmt.executeUpdate();
                customerList.remove(selected);
                customers_tableView.refresh();
                clearDetails();
                showAlert("Deleted", "Customer record deleted.");

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Could not delete customer.");
            }
        }
    }

    @FXML
    void handleClear(ActionEvent event) {
        clearDetails();
    }

    private void clearDetails() {
        customers_tableView.getSelectionModel().clearSelection();
        customers_movieTitle.setText("");
        customers_genre.setText("");
        customers_time.setText("");
        customers_date.setText("");
        customers_ticketNumber.setText("");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // NAVIGATION
    @FXML void handleAddMovieBtn(ActionEvent event) { switchScene("/com/example/demo/addmovies.fxml", event); }
    @FXML void handleAvailableMovieBtn(ActionEvent event) { switchScene("/com/example/demo/availablemovies.fxml", event); }
    @FXML void handleCustomersBtn(ActionEvent event) { switchScene("/com/example/demo/customers.fxml", event); }
    @FXML void handleDashboardBtn(ActionEvent event) { switchScene("/com/example/demo/dashboard.fxml", event); }
    @FXML void handleEditScreeningBtn(ActionEvent event) { switchScene("/com/example/demo/editscreening.fxml", event); }
    @FXML void handleSignoutBtn(ActionEvent event) { switchScene("/com/example/demo/login.fxml", event); }

    private void switchScene(String path, ActionEvent event) {
        Scene scene = new Scene(FXMLScene.load(path).getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
