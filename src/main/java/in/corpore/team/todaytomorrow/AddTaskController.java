package in.corpore.team.todaytomorrow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            Task newtask = null;
            try {
                newtask = new Task(dateFormate.parse(date), time, title, description);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if (validateTaskData(newtask)) {
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
    private boolean validateTaskData(Task task) {
        if (task.getTitle().trim().isEmpty()) {
            showAlert("Ошибка", "Заголовок задачи не может быть пустым.");
            return false;
        }

        if (task.getDescription().trim().isEmpty()) {
            showAlert("Ошибка", "Описание задачи не может быть пустым.");
            return false;
        }

        if (task.getDate() == null) {
            showAlert("Ошибка", "Дата задачи не может быть пустой.");
            return false;
        }
        if (task.getTime().trim().isEmpty()){
            showAlert("Ошибка", "Дата задачи не может быть пустой.");
            return false;
        }
        return true;
    }

}