package in.corpore.team.todaytomorrow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskDetailsController {

    @FXML
    private Label labelDate;
    @FXML
    private Label labelTime;
    @FXML
    private Label labelTitle;
    @FXML
    private TextArea labelDescription;

    private Task task; // Храним текущую задачу

    public void setTask(Task task) {
        this.task = task;

        // Форматируем дату в строку
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(task.getDate());

        // Заполняем данные
        labelDate.setText(formattedDate);
        labelTime.setText(task.getTime());
        labelTitle.setText(task.getTitle());
        labelDescription.setText(task.getDescription());
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) labelDate.getScene().getWindow();
        stage.close();
    }
}