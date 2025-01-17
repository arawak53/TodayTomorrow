package in.corpore.team.todaytomorrow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
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
         fieldDate.getEditor().setText(task.date);
         fieldTime.setText(task.time);
         fieldTitle.setText(task.title);
         fieldDescription.setText(task.description);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonSave.setOnAction(event -> {

            String date = fieldDate.getEditor().getText();
            String time = fieldTime.getText();
            String title = fieldTitle.getText();
            String description = fieldDescription.getText();
            Task newtask = new Task(date,time,title,description);
            result.onResult(newtask);
            buttonSave.getScene().getWindow().hide();

        });

    }
    public void setTaskResult (TaskResult result) {
        this.result = result;
    }
}
