package in.corpore.team.todaytomorrow;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

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

    private int selectedDayOfWeek ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monday.setOnAction(event -> {
            selectedDayOfWeek = 2;
            disableButtonStyle();
            updateTaskList();
            monday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        tuesday.setOnAction(event -> {
            selectedDayOfWeek = 3;
            disableButtonStyle();
            updateTaskList();
            tuesday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");


        });
        wednesday.setOnAction(actionEvent -> {
            selectedDayOfWeek = 4;
            disableButtonStyle();
            updateTaskList();
            wednesday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        thursday.setOnAction(actionEvent -> {
            selectedDayOfWeek = 5;
            disableButtonStyle();
            updateTaskList();
            thursday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        friday.setOnAction(actionEvent -> {
            selectedDayOfWeek = 6;
            disableButtonStyle();
            updateTaskList();
            friday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        saturday.setOnAction(actionEvent -> {
            selectedDayOfWeek = 7;
            disableButtonStyle();
            updateTaskList();
            saturday.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        sunday.setOnAction(actionEvent -> {
            selectedDayOfWeek = 1;
            disableButtonStyle();
            updateTaskList();
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
                List<Task> filteredTaskList =  listTask.stream().filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(task.date);
                        int week = calendar.get(Calendar.DAY_OF_WEEK);
                        return week == selectedDayOfWeek;
                    }
                }).toList();
                int selectedIndex = model.getSelectedIndex();
                Task task = filteredTaskList.get(selectedIndex);
                listTask.remove(task);
                updateTaskList();
            }
        };
        menuItem1.setOnAction(hendler);

        EventHandler<ActionEvent> hendlerEdit = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                SelectionModel model = listTaskView.getSelectionModel();
                List<Task> filteredTaskList =  listTask.stream().filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(task.date);
                        int week = calendar.get(Calendar.DAY_OF_WEEK);
                        return week == selectedDayOfWeek;
                    }
                }).toList();
                int selectedIndex = model.getSelectedIndex();
                Task task = filteredTaskList.get(selectedIndex);
                editingTaskIndex = listTask.indexOf(task);

                openWindows(true);
            }
        };
        menuItem2.setOnAction(hendlerEdit);
        EventHandler<ActionEvent> hendlerDuplicate = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SelectionModel model = listTaskView.getSelectionModel();
                List<Task> filteredTaskList =  listTask.stream().filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(task.date);
                        int week = calendar.get(Calendar.DAY_OF_WEEK);
                        return week == selectedDayOfWeek;
                    }
                }).toList();

                int selectedIndex = model.getSelectedIndex();
                Task task = filteredTaskList.get(selectedIndex);
                Task task1 = new  Task(task.date, task.time, task.title, task.description);
                listTask.add(selectedIndex,task1);
                updateTaskList();
            }
        };
        menuItem3.setOnAction(hendlerDuplicate);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        for (int i = 0; i < listTask.size(); ++i) {
            Task task = listTask.get(i);
            String dateInText =dateFormat.format(task.date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(task.date);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            if (selectedDayOfWeek == week){
                textList.add(dateInText + " | " + task.time + " | " + task.title + " | " + task.description);
            }


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

            Task task = listTask.get(editingTaskIndex);
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