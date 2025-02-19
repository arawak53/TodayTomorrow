package in.corpore.team.todaytomorrow;

import javafx.application.Platform;
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
    @FXML
    private Button cardTask;
    @FXML
    private FlowPane taskContainer;
    @FXML
    private ScrollPane listTaskView2;

    DataStorge dat = new Database();

    private int editingTaskIndex = -1;

    private int selectedDayOfWeek;

    private Task selectedTask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ЗАгрузка БД
        dat.getAllTasks(new GetAllTaskCallback() {
            @Override
            public void onGetAllTask(List<Task> task) {
                listTask.clear();
                listTask.addAll(task);
                updateTaskList();
            }
        });

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
        cardTask.setOnAction(actionEvent -> {
            if (listTaskView.isVisible()){
                listTaskView.setVisible(false);
                listTaskView2.setVisible(true);
            } else {
                listTaskView.setVisible(true);
                listTaskView2.setVisible(false);
            }
        });


        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("delite");
        MenuItem menuItem2 = new MenuItem("Edit");
        MenuItem menuItem3 = new MenuItem("Duplicate");
        MenuItem menuItem4 = new MenuItem("OpenTask");
        listTaskView.setContextMenu(contextMenu);
        listTaskView2.setContextMenu(contextMenu);
        contextMenu.getItems().add(menuItem1);
        contextMenu.getItems().add(menuItem2);
        contextMenu.getItems().add(menuItem3);
        contextMenu.getItems().add(menuItem4);
        EventHandler<ActionEvent> handle = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                final Task task = getSelectedTask();
                if (task != null) {
                    dat.deleteTaskById(task.id, new DeleteTaskCallback() {
                        @Override
                        public void onTaskDeleted(final boolean success) {
                            // Обновляем UI в главном (JavaFX) потоке
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (success) {
                                        listTask.remove(task);
                                        updateTaskList();
                                        System.out.println("Задача с id " + task.id + " успешно удалена.");
                                    } else {
                                        System.out.println("Ошибка при удалении задачи.");
                                    }
                                }
                            });
                        }
                    });
                }
            }
        };
        menuItem1.setOnAction(handle);

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
                Task task = getSelectedTask();
                int selectedIndex = listTask.indexOf(task);
                Task task1 = new Task(task.date, task.time, task.title, task.description);
                task1.setId(task.getId());  // Устанавливаем id перед дублированием
                dat.duplicateTaskById(task1, new DublicateTaskCallback() {
                    @Override
                    public void onDublicateTask(Task task) {
                        if (task != null) {
                            listTask.add(selectedIndex, task);
                            updateTaskList();

                        } else {
                            System.out.println("Ошибка при получении дубликата!");
                        }
                    }
                });

            }
        };
        menuItem3.setOnAction(hendlerDuplicate);


        EventHandler<ActionEvent> hendlerOpenTask = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Task task = getSelectedTask();
                if (task != null) {
                    showTaskDetails(task);
                } else {
                    System.out.println("Ошибка " +  "Выберите задачу для просмотра.");
                }
            }
        };
        menuItem4.setOnAction(hendlerOpenTask);

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

    private void showTaskDetails(Task task) {
        try {
            var loader = new FXMLLoader(getClass().getResource("task_details.fxml"));
            Parent root = loader.load();

            TaskDetailsController controller = loader.getController();
            controller.setTask(task);

            Stage stage = new Stage();
            stage.setTitle("Детальный просмотр задачи");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        taskContainer.getChildren().clear();
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

        listTaskView.setItems(FXCollections.observableArrayList(textList));
    }

    private Task getSelectedTask (){
        if (listTaskView.isVisible()){
            List<Task> filteredTaskList = filterTaskSelectedofWeek();
            SelectionModel model = listTaskView.getSelectionModel();
            int selectedIndex = model.getSelectedIndex();
            Task task = filteredTaskList.get(selectedIndex);
            return task;
        }else {
            return selectedTask;
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
        controller.setTaskResult(new TaskResult() {
            @Override
            public void onResult(Task task) {
                if (editingTaskIndex >= 0) {
                    int taskId = listTask.get(editingTaskIndex).getId();
                    task.setId(taskId);
                    dat.saveTask(task, taskId, new SaveTaskCallback() {
                        @Override
                        public void onTaskSaved(Task task) {
                            Task editing = task;
                            listTask.set(editingTaskIndex, editing);
                            updateTaskList();
                            System.out.println("Задача успешно обновлена на сервере.");
                            editingTaskIndex = -1;
                        }
                    });
                } else {
                    dat.saveTask(task, task.getId(), new SaveTaskCallback() {
                        @Override
                        public void onTaskSaved(Task task) {
                            Task taskNew = task;
                            listTask.add(taskNew);
                            updateTaskList();
                        }
                    });
                }
            }
        });
        if (isEdit) {
            Task task = getSelectedTask();
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

