package com.example.demo;

import com.example.demo.utils.DatabaseConnection;
import com.example.demo.utils.FXMLScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField SignUp_email;

    @FXML
    private AnchorPane SignUp_form;

    @FXML
    private Label label;

    @FXML
    private TextField name;

    @FXML
    private PasswordField signUp_password;

    @FXML
    private TextField signUp_username;

    @FXML
    void handleAlreadyAccount(ActionEvent event) {
        FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/login.fxml");
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();


    }

    @FXML
    void handleSignup(ActionEvent event) throws SQLException, ClassNotFoundException {

        String username = signUp_username.getText();
        String password = signUp_password.getText();
        String email = SignUp_email.getText();
        String name = this.name.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }
        else {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            String sql = "SELECT name FROM users WHERE username = ? or email = ?";
            Connection connection = databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Username or email already exists");
                alert.showAndWait();
            } else {
                String insertSql = "INSERT INTO users (username, password, email, name) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                insertStatement.setString(1, username);
                insertStatement.setString(2, password);
                insertStatement.setString(3, email);
                insertStatement.setString(4, name);
                int effectedRow  = insertStatement.executeUpdate();
                if (effectedRow > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Registration successful");
                    alert.showAndWait();

                    FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/login.fxml");
                    Scene scene = new Scene(fxmlScene.getRoot());
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Registration failed");
                    alert.showAndWait();
                }

            }


        }

    }

}
