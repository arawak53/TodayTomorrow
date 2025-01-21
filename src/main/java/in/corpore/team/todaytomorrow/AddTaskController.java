package in.corpore.team.todaytomorrow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
                newtask = new Task(dateFormate.parse(date),time,title,description);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            result.onResult(newtask);
            buttonSave.getScene().getWindow().hide();

        });

    }
    public void setTaskResult (TaskResult result) {
        this.result = result;
    }
}
