package com.example.demo;

import com.example.demo.utils.DatabaseConnection;
import com.example.demo.utils.FXMLScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField SignUp_email;

    @FXML
    private AnchorPane SignUp_form;

    @FXML
    private Label label;

    @FXML
    private Button signUp_Btn;

    @FXML
    private Hyperlink signUp_alreadyHaveAccount;

    @FXML
    private PasswordField signUp_password;

    @FXML
    private TextField signUp_username;

    @FXML
    private Hyperlink signin_createAccount;

    @FXML
    private AnchorPane signin_form;

    @FXML
    private Button signin_loginBtn;

    @FXML
    private PasswordField signin_password;

    @FXML
    private TextField signin_username;

    @FXML
    void loginHandle(ActionEvent event) throws SQLException, ClassNotFoundException {
        String username = signin_username.getText();
        String password = signin_password.getText();
        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            String sql = "SELECT * FROM users WHERE username = ? and password = ?";
            Connection connection = databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            if (preparedStatement.executeQuery().next()) {

                FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/dashboard.fxml");
                Scene scene = new Scene(fxmlScene.getRoot());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password");
                alert.showAndWait();

            }

        }
    }
    @FXML
    void creatAccount(ActionEvent event) {
        FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/register.fxml");
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
