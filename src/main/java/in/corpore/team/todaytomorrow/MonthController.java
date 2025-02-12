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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class MonthController implements Initializable {
    @FXML
    private Button january;
    @FXML
    private Button february;
    @FXML
    private Button march;
    @FXML
    private Button april;
    @FXML
    private Button may;
    @FXML
    private Button june;
    @FXML
    private Button jule;
    @FXML
    private Button august;
    @FXML
    private Button september;
    @FXML
    private Button october;
    @FXML
    private Button november;
    @FXML
    private Button december;
    @FXML
    private ListView <String> listTaskView;
    @FXML
    private Button plus;
    @FXML
    private Button backToMain;
    @FXML
    private Button cardTask;

    private ArrayList<Task> listTask = new ArrayList<>();

    DataStorge dat = new Database();

    private int selectedMonth;

    private int editingTaskIndex = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ЗАгрузка БД
        List<Task> listTask1 = dat.getAllTasks();
        listTask.clear();
        listTask.addAll(listTask1);
        updateTaskList();


        january.setOnAction(event -> {
            selectedMonth = 0;
            disableButtonStyle();
            updateTaskList();
            january.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        february.setOnAction(event -> {
            selectedMonth = 1;
            disableButtonStyle();
            updateTaskList();
            february.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        march.setOnAction(event -> {
            selectedMonth = 2;
            disableButtonStyle();
            updateTaskList();
            march.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        april.setOnAction(event -> {
            selectedMonth = 3;
            disableButtonStyle();
            updateTaskList();
            april.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        may.setOnAction(event -> {
            selectedMonth = 4;
            disableButtonStyle();
            updateTaskList();
            may.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        june.setOnAction(event -> {
            selectedMonth = 5;
            disableButtonStyle();
            updateTaskList();
            june.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        jule.setOnAction(event -> {
            selectedMonth = 6;
            disableButtonStyle();
            updateTaskList();
            jule.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        august.setOnAction(event -> {
            selectedMonth = 7;
            disableButtonStyle();
            updateTaskList();
            august.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        september.setOnAction(event -> {
            selectedMonth = 8;
            disableButtonStyle();
            updateTaskList();
            september.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        october.setOnAction(event -> {
            selectedMonth = 9;
            disableButtonStyle();
            updateTaskList();
            october.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
        november.setOnAction(event -> {
            selectedMonth = 10;
            disableButtonStyle();
            updateTaskList();
            november.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        });
       december.setOnAction(event -> {
           selectedMonth = 11;
           disableButtonStyle();
           updateTaskList();
           december.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
       });
        plus.setOnAction(actionEvent -> {
            openWindows(false);
        });
        backToMain.setOnAction(actionEvent -> {
            try {
                // Загружаем новый FXML файл и контроллер
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
                Parent root = loader.load(); // загружаем FXML

                // Получаем текущее окно
                Stage currentStage = (Stage) backToMain.getScene().getWindow();

                // Устанавливаем новое содержимое в окно
                Scene newScene = new Scene(root);
                currentStage.setScene(newScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        cardTask.setOnAction(actionEvent -> {
            try {
                // Загружаем новый FXML файл и контроллер
                FXMLLoader loader = new FXMLLoader(getClass().getResource("task-cards.fxml"));
                Parent root = loader.load(); // загружаем FXML

                // Получаем текущее окно
                Stage currentStage = (Stage) cardTask.getScene().getWindow();

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

    private void disableButtonStyle() {
        january.setStyle("");
        february.setStyle("");
        march.setStyle("");
        april.setStyle("");
        may.setStyle("");
        june.setStyle("");
        jule.setStyle("");
        august.setStyle("");
        september.setStyle("");
        october.setStyle("");
        november.setStyle("");
        december.setStyle("");
    }

    private void updateTaskList() {
        ArrayList<String> textList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        for (int i = 0; i < listTask.size(); ++i) {
            Task task = listTask.get(i);
            String dateInText = dateFormat.format(task.date);
            int month = task.getMonth ();
            if (selectedMonth == month) {
                textList.add(dateInText + " | " + task.time + " | " + task.title + " | " + task.description);
            }
            //listTaskView.setItems(FXCollections.observableArrayList(textList));
            if (listTaskView != null) {
              listTaskView.setItems(FXCollections.observableArrayList(textList));
            } else {
              System.out.println("Ошибка: listTaskView == null");
            }
        }

    }
    private List<Task> filterTaskSelectedofWeek() {
        return listTask.stream()
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) {
                        int week = task.getMonth ();
                        return week == selectedMonth;
                    }
                })
                .toList();
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