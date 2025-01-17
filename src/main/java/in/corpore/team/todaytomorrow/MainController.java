package in.corpore.team.todaytomorrow;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
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

    private int editingTaskIndex = -1;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monday.setOnAction(event -> {
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
            openWindows(false);
        });
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("delite");
        MenuItem menuItem2 = new MenuItem("Edit");
        MenuItem menuItem3 = new MenuItem("Duplicate");
        listTaskView.setContextMenu(contextMenu);
        contextMenu.getItems().add(menuItem1);
        contextMenu.getItems().add(menuItem2);
        contextMenu.getItems().add(menuItem3);

        EventHandler<ActionEvent> hendler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SelectionModel model = listTaskView.getSelectionModel();
                int selectedIndex = model.getSelectedIndex();
                listTask.remove(selectedIndex);
                updateTaskList();
            }
        };
        menuItem1.setOnAction(hendler);

        EventHandler<ActionEvent> hendlerEdit = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                openWindows(true);
            }
        };
        menuItem2.setOnAction(hendlerEdit);
    }

    private void disableButtonStyle() {
        monday.setStyle("");
        tuesday.setStyle("");
        wednesday.setStyle("");
        thursday.setStyle("");
        friday.setStyle("");
        saturday.setStyle("");
        sunday.setStyle("");
    }

    private void updateTaskList() {
        ArrayList<String> textList = new ArrayList<>();
        for (int i = 0; i < listTask.size(); ++i) {
            textList.add(listTask.get(i).date + " | " + listTask.get(i).time + " | " + listTask.get(i).title + " | " + listTask.get(i).description);
        }
        listTaskView.setItems(FXCollections.observableArrayList(textList));

    }

    private void openWindows(boolean isEdit) {
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
                if( editingTaskIndex >= 0){
                    listTask.set(editingTaskIndex, task);
                    editingTaskIndex = -1;
                }
                else{
                    listTask.add(task);
                }
                updateTaskList();
            }
        });
        if (isEdit) {
            SelectionModel model = listTaskView.getSelectionModel();
            editingTaskIndex = model.getSelectedIndex();
            Task task = listTask.get(model.getSelectedIndex());
            controller.setTask(task);
        }
        else {
            editingTaskIndex = -1;
        }
        Stage stage = new Stage();
        stage.setTitle("TodayTomorrow!");
        stage.setScene(scene);
        stage.show();

    }

}