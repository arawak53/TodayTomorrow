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
            disableButtonStyle();
            monday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        tuesday.setOnAction(event -> {
            disableButtonStyle();
            tuesday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");


        });
        wednesday.setOnAction(actionEvent -> {
            disableButtonStyle();
            wednesday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        thursday.setOnAction(actionEvent -> {
            disableButtonStyle();
            thursday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        friday.setOnAction(actionEvent -> {
            disableButtonStyle();
            friday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        saturday.setOnAction(actionEvent -> {
            disableButtonStyle();
            saturday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        sunday.setOnAction(actionEvent -> {
            disableButtonStyle();
            sunday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });

    }
    private void disableButtonStyle(){
        monday.setStyle("");
        tuesday.setStyle("");
        wednesday.setStyle("");
        thursday.setStyle("");
        friday.setStyle("");
        saturday.setStyle("");
        sunday.setStyle("");
    }
}