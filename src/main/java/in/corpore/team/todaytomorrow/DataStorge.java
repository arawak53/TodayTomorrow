package in.corpore.team.todaytomorrow;

import java.util.List;

public interface DataStorge {
    void  getAllTasks(GetAllTaskCallback getAllTaskCallback);

    void deleteTaskById(int taskId, DeleteTaskCallback deleteTaskCallback);

    void duplicateTaskById(Task task, DublicateTaskCallback dublicateTaskCallback);

    void saveTask(Task task, Integer taskId, SaveTaskCallback saveTaskCallback);

}

