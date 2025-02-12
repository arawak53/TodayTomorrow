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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class TaskCardsController implements Initializable {
    private ArrayList<Task> listTask = new ArrayList<>();
    @FXML
    private ScrollPane listTaskView;
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
    private FlowPane taskContainer;
    @FXML
    private Button backToMain;
    @FXML
    private Button openMonth;

    private Task selectedTask;
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
                if (selectedTask != null) {
                    dat.deleteTaskById(selectedTask.getId());
                    listTask.remove(selectedTask);
                    updateTaskList();
                } else {
                    System.out.println("Задача не выбрана для удаления.");
                }
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
                if (selectedTask != null) {
                    Task task1 = new Task(selectedTask.getDate(), selectedTask.getTime(), selectedTask.getTitle(), selectedTask.getDescription());
                    task1.setId(selectedTask.getId());
                    Task duplicateTask = dat.duplicateTaskById(task1);
                    if (duplicateTask != null) {
                        listTask.add(listTask.indexOf(selectedTask), duplicateTask);
                    } else {
                        System.out.println("Ошибка при получении дубликата!");
                    }
                } else {
                    System.out.println("Задача не выбрана для дублирования.");
                }
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
        taskContainer.getChildren().clear();
        ArrayList<String> textList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        for (Task task : listTask) {
            if (task.getDayOfWeek() == selectedDayOfWeek) {
                // Создание карточки
                VBox card = new VBox(5); // Отступы между элементами
                card.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-padding: 10; -fx-background-color: white;");
                card.setPrefSize(200, 100); // Фиксированный размер карточки

                // Заголовок задачи
                Label title = new Label(task.getTitle());
                title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                // Описание задачи
                Label description = new Label(task.getDescription());
                description.setWrapText(true); // Автоматический перенос текста

                // Дата и время
                Label dateTime = new Label(dateFormat.format(task.getDate()) + " | " + task.getTime());

                // Добавление всех элементов в карточку
                card.getChildren().addAll( title, description, dateTime);

                card.setOnMouseClicked(event -> {
                    selectedTask = task; // Задача, на которую нажали
                    System.out.println("Выбрана задача: " + task.getTitle()); // Выводим название выбранной задачи (или другие действия)
                });
                // Добавление карточки в контейнер
                taskContainer.getChildren().add(card);
            }
        }
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
        controller.setTaskResult(task -> {
            if (editingTaskIndex >= 0) {
                int taskId = listTask.get(editingTaskIndex).getId();
                task.setId(taskId);
                Task updatedTask = dat.saveTask(task, taskId);
                listTask.set(editingTaskIndex, updatedTask);
                System.out.println("Задача успешно обновлена на сервере.");
                editingTaskIndex = -1;
            } else {
                Task newTask = dat.saveTask(task, task.getId());
                listTask.add(newTask);
            }
            updateTaskList();
        });
        if (isEdit) {
            // Используем selectedTask, который уже был обновлен при клике на карточку
            if (selectedTask != null) {
                editingTaskIndex = listTask.indexOf(selectedTask); // Находим индекс выбранной задачи
                controller.setTask(selectedTask); // Отправляем задачу в контроллер для редактирования
            } else {
                System.out.println("Задача не выбрана для редактирования.");
            }
        } else {
            editingTaskIndex = -1;
        }
        Stage stage = new Stage();
        stage.setTitle("TodayTomorrow!");
        stage.setScene(scene);
        stage.show();
    }

}
