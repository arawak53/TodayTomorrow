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
    private ListView<String> listTaskView;
    @FXML
    private Button plus;
    @FXML
    private Button backToMain;
    @FXML
    private Button cardTask;
    @FXML
    private FlowPane taskContainer;
    @FXML
    private ScrollPane listTaskView2;


    private Task selectedTask;

    private ArrayList<Task> listTask = new ArrayList<>();

    DataStorge dat = new Database();

    private int selectedMonth;

    private int editingTaskIndex = -1;

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
                List<Task> filteredTaskList = filterTaskSelectedofWeek();
                SelectionModel model = listTaskView.getSelectionModel();
                int selectedIndex = model.getSelectedIndex();
                Task task = filteredTaskList.get(selectedIndex);
                Task task1 = new Task(task.date, task.time, task.title, task.description);
                task1.setId(task.getId());  // Устанавливаем id перед дублированием
                dat.duplicateTaskById(task1, new DublicateTaskCallback() {
                    @Override
                    public void onDublicateTask(Task task) {
                        if (task != null) {
                            // Добавляем дубликат задачи в список
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
            int month = task.getMonth();
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
        taskContainer.getChildren().clear();
        for (Task task : listTask) {
            if (task.getMonth() == selectedMonth) {
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
                card.getChildren().addAll(title, description, dateTime);

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

    private List<Task> filterTaskSelectedofWeek() {
        return listTask.stream()
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) {
                        int week = task.getMonth();
                        return week == selectedMonth;
                    }
                })
                .toList();
    }

    private Task getSelectedTask() {
        if (listTaskView.isVisible()) {
            List<Task> filteredTaskList = filterTaskSelectedofWeek();
            SelectionModel model = listTaskView.getSelectionModel();
            int selectedIndex = model.getSelectedIndex();
            Task task = filteredTaskList.get(selectedIndex);
            return task;
        } else {
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