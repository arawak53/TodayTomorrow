package in.corpore.team.todaytomorrow;

import java.util.List;

public interface DataStorge {
    List <Task> getAllTasks();

    void deleteTaskById(int taskId);

    Task duplicateTaskById(Task task);

    Task saveTask(Task task, Integer taskId);

}

