package com.example.demo;

import com.example.demo.utils.FXMLScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Label dashboard_availableMovies;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_totalEarnToday;

    @FXML
    private Label dashboard_totalSoldTicket;

    @FXML
    private Label username;

    @FXML
    void handleAddMovieBtn(ActionEvent event) {

        FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/addmovies.fxml");
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void handleAvailableMovieBtn(ActionEvent event) {

        FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/availablemovies.fxml");
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void handleCustomersBtn(ActionEvent event) {
        FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/customers.fxml");
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleDashboardBtn(ActionEvent event) {
        FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/dashboard.fxml");
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleEditScreeningBtn(ActionEvent event) {
        FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/editscreening.fxml");
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleSignoutBtn(ActionEvent event) {
        FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/login.fxml");
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
