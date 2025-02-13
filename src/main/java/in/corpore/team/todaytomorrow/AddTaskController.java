package in.corpore.team.todaytomorrow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.ResourceBundle;

public class AddTaskController implements Initializable {

    private  TaskResult result;
    @FXML
    private Button buttonSave;
    @FXML
    private DatePicker fieldDate;
    @FXML
    private TextField fieldTime;
    @FXML
    private TextField fieldTitle;
    @FXML
    private TextArea fieldDescription;


    public void setTask (Task task) {
        SimpleDateFormat dateFormate = new SimpleDateFormat("dd.MM.yyyy");
        fieldDate.getEditor().setText(dateFormate.format(task.date));
        fieldTime.setText(task.time);
        fieldTitle.setText(task.title);
        fieldDescription.setText(task.description);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonSave.setOnAction(event -> {
            SimpleDateFormat dateFormate = new SimpleDateFormat("dd.MM.yyyy");
            String date = fieldDate.getEditor().getText();
            String time = fieldTime.getText();
            String title = fieldTitle.getText();
            String description = fieldDescription.getText();


            if (validateTaskData( date,time,title,description)) {
                Task newtask = null;

                try {
                    newtask = new Task(dateFormate.parse(date), time, title, description);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (result != null) {
                    result.onResult(newtask);
                }
                Stage stage = (Stage) buttonSave.getScene().getWindow();
                stage.close();
            }
        });

    }
    public void setTaskResult (TaskResult result) {
        this.result = result;
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateTaskData(String date, String time, String title, String description) {




        if (title.trim().isEmpty()) {
            showAlert("Ошибка", "Заголовок задачи не может быть пустым.");
            return false;
        }

        if (description.trim().isEmpty()) {
            showAlert("Ошибка", "Описание задачи не может быть пуст.");
            return false;
        }

        if (date.trim().isEmpty()) {
            showAlert("Ошибка", "Дата задачи не может быть пустой.");
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false); // Убираем нестрогий режим
        try {
            dateFormat.parse(date.toString()); // Пробуем распарсить дату в строковом формате
        } catch (ParseException e) {
            showAlert("Ошибка", "Дата должна быть в формате ДД.ММ.ГГГГ.");
            return false;
        }
        if (time.trim().isEmpty()){
            showAlert("Ошибка", "Время задачи не может быть пустым.");
            return false;
        }
        if (!time.matches("([01]?\\d|2[0-3]):[0-5]\\d")) {
            showAlert("Ошибка", "Время должно быть в формате ЧЧ:ММ (00:00 - 23:59).");
            return false;
        }
        return true;
    }

}