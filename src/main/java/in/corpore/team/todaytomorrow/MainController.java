package in.corpore.team.todaytomorrow;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @FXML
    private Button openMonth;

    private Task task;

    DataStorge dat = new Database();

    private int editingTaskIndex = -1;

    private int selectedDayOfWeek;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ЗАгрузка БД
        List<Task> listTask1 = dat.getAllTasks();
        listTask.clear();
        listTask.addAll(listTask1);
        updateTaskList();
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
        openMonth.setOnAction(actionEvent -> {
            try {
                // Загружаем новый FXML файл и контроллер
                FXMLLoader loader = new FXMLLoader(getClass().getResource("month.fxml"));
                Parent root = loader.load(); // загружаем FXML

                // Получаем текущее окно
                Stage currentStage = (Stage) openMonth.getScene().getWindow();

                // Устанавливаем новое содержимое в окно
                Scene newScene = new Scene(root);
                currentStage.setScene(newScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                List<Task> filteredTaskList = filterTaskSelectedofWeek();
                SelectionModel model = listTaskView.getSelectionModel();
                int selectedIndex = model.getSelectedIndex();
                Task task = filteredTaskList.get(selectedIndex);
                dat.deleteTaskById(task.id);
                listTask.remove(task);
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

        EventHandler<ActionEvent> hendlerDuplicate = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                List<Task> filteredTaskList = filterTaskSelectedofWeek();
                SelectionModel model = listTaskView.getSelectionModel();
                int selectedIndex = model.getSelectedIndex();
                Task task = filteredTaskList.get(selectedIndex);
                Task task1 = new Task(task.date, task.time, task.title, task.description);
                task1.setId(task.getId());  // Устанавливаем id перед дублированием
                Task dublicateTask = dat.duplicateTaskById(task1);
                if (dublicateTask != null) {
                    // Добавляем дубликат задачи в список
                    listTask.add(selectedIndex, dublicateTask);
                    updateTaskList();

                } else {
                    System.out.println("Ошибка при получении дубликата!");
                }
            }
        };
        menuItem3.setOnAction(hendlerDuplicate);
    }

    private List<Task> filterTaskSelectedofWeek() {
        return listTask.stream()
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) {
                        int week = task.getDayOfWeek();
                        return week == selectedDayOfWeek;
                    }
                })
                .toList();
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
            String dateInText = dateFormat.format(task.date);
            int week = task.getDayOfWeek();
            if (selectedDayOfWeek == week) {
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
                if (editingTaskIndex >= 0) {
                    int taskId = listTask.get(editingTaskIndex).getId();
                    task.setId(taskId);
                    Task editing = dat.saveTask(task, taskId);
                    listTask.set(editingTaskIndex, editing);
                    updateTaskList();
                    System.out.println("Задача успешно обновлена на сервере.");
                    editingTaskIndex = -1;

                } else {
                    Task taskNew = dat.saveTask(task, task.getId());
                    listTask.add(taskNew);
                    updateTaskList();
                }

            }
        });
        if (isEdit) {
            List<Task> filteredTaskList = filterTaskSelectedofWeek();
            SelectionModel model = listTaskView.getSelectionModel();
            int selectedIndex = model.getSelectedIndex();
            Task task = filteredTaskList.get(selectedIndex);
            editingTaskIndex = listTask.indexOf(task);
            controller.setTask(task);

        } else {
            editingTaskIndex = -1;
        }
        Stage stage = new Stage();
        stage.setTitle("TodayTomorrow!");
        stage.setScene(scene);
        stage.show();
    }
}
