package in.corpore.team.todaytomorrow;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private ArrayList<Task> listTask = new ArrayList<>();
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
    @FXML
    private Button plus;
    @FXML
    private ListView<String> listTaskView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        plus.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-task-view.fxml"));

            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 770, 240);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AddTaskController controller = fxmlLoader.getController();
            controller.setTaskResult(new TaskResult() {
                @Override
                public void onResult(Task task) {
                    listTask.add(task);
                    updateTaskList();
                }
            });
            Stage stage = new Stage();
            stage.setTitle("TodayTomorrow!");
            stage.setScene(scene);
            stage.show();
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
    private void updateTaskList (){
        ArrayList <String> textList = new ArrayList<>();
        for (int i = 0 ;i < listTask.size(); ++i){
            textList.add(listTask.get(i).date+" | "+listTask.get(i).time+" | "+listTask.get(i).title+" | "+listTask.get(i).description);
        }
        listTaskView.setItems(FXCollections.observableArrayList(textList));

    }

}