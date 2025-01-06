package in.corpore.team.todaytomorrow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Button monday;
    @FXML
    private Button tuesday;
    @FXML
    private Button wednesday;
    @FXML
    private Button thursday;
    @FXML
    private Button friday;
    @FXML
    private Button saturday;
    @FXML
    private Button sunday;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monday.setText("Monday");
        monday.setOnAction(event ->{
            tuesday.setStyle("");
            wednesday.setStyle("");
            thursday.setStyle("");
            friday.setStyle("");
            saturday.setStyle("");
            sunday.setStyle("");
            monday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        tuesday.setOnAction(event -> {
            monday.setStyle("");
            wednesday.setStyle("");
            thursday.setStyle("");
            friday.setStyle("");
            saturday.setStyle("");
            sunday.setStyle("");
            tuesday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");


        });
        wednesday.setOnAction(actionEvent -> {
            monday.setStyle("");
            tuesday.setStyle("");
            thursday.setStyle("");
            friday.setStyle("");
            saturday.setStyle("");
            sunday.setStyle("");
            wednesday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        thursday.setOnAction(actionEvent -> {
            monday.setStyle("");
            tuesday.setStyle("");
            wednesday.setStyle("");
            friday.setStyle("");
            saturday.setStyle("");
            sunday.setStyle("");
            thursday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        friday.setOnAction(actionEvent -> {
            monday.setStyle("");
            tuesday.setStyle("");
            wednesday.setStyle("");
            thursday.setStyle("");
            saturday.setStyle("");
            sunday.setStyle("");
            friday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        saturday.setOnAction(actionEvent -> {
            monday.setStyle("");
            tuesday.setStyle("");
            wednesday.setStyle("");
            thursday.setStyle("");
            friday.setStyle("");
            sunday.setStyle("");
            saturday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        sunday.setOnAction(actionEvent -> {
            monday.setStyle("");
            tuesday.setStyle("");
            wednesday.setStyle("");
            thursday.setStyle("");
            friday.setStyle("");
            saturday.setStyle("");
            sunday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
    }
}