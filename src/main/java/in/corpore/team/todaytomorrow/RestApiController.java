package in.corpore.team.todaytomorrow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestApiController implements DataStorge {

    private HttpClient httpClient = HttpClient.newHttpClient();
    private String url = "http://91.211.14.76:9090/";
    private Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public RestApiController() {
    }


    @Override
    public void getAllTasks(final GetAllTaskCallback callback) {
        final javafx.concurrent.Task<List<Task>> taskThread = new javafx.concurrent.Task<List<Task>>() {
            protected List<in.corpore.team.todaytomorrow.Task> call() {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url + "tasks"))
                        .GET()
                        .build();

                try {
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("код ответа" + response.statusCode());
                    System.out.println("Ответ от сервера: \n" + response.body());
                    ArrayList<Task> listTask = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
                    }.getType());
                    System.out.println("Вывод на консоль: " + listTask);
                    return listTask;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
        };
        taskThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent workerStateEvent) {
                try {
                    List <in.corpore.team.todaytomorrow.Task> result =  taskThread.get();
                    callback.onGetAllTask(result);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        this.executor.execute(taskThread);
    }

    @Override
    public void deleteTaskById(int taskId, DeleteTaskCallback deleteTaskCallback) {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpRequest deletedRequest = HttpRequest.newBuilder()
                        .uri(URI.create(url + taskId))
                        .header("Content-Type", "application/json")
                        .DELETE()
                        .build();
                try {
                    HttpResponse<String> deletedResponse = httpClient.send(deletedRequest, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Код ответа: " + deletedResponse.statusCode());
                    System.out.println("Ответ от сервера: " + deletedResponse.body());

                } catch (
                        Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void duplicateTaskById(final Task task, final DublicateTaskCallback callback) {
        final javafx.concurrent.Task<Task> taskThread = new javafx.concurrent.Task<Task>() {
            protected in.corpore.team.todaytomorrow.Task call() {
        String jsonCope = gson.toJson(task);
        HttpRequest requestCopy = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonCope))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(requestCopy, HttpResponse.BodyHandlers.ofString());
            System.out.println("Код ответа: " + response.statusCode());
            System.out.println("Ответ от сервера: " + response.body());
            if (response.statusCode() == 200) {
                Task newTask = gson.fromJson(response.body(), Task.class);

                if (newTask.id == null) {
                    System.out.println("⚠ Ошибка: сервер вернул задачу без id!");
                    return null;
                }
                return newTask;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
            }
    };
    taskThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        public void handle(WorkerStateEvent workerStateEvent) {
            try {
                in.corpore.team.todaytomorrow.Task result = (in.corpore.team.todaytomorrow.Task) taskThread.get();
                callback.onDublicateTask(result);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    });
        this.executor.execute(taskThread);
}


    @Override
    public void saveTask(final Task task, final Integer taskId, final SaveTaskCallback callback) {
        final javafx.concurrent.Task<Task> taskThread = new javafx.concurrent.Task<Task>() {
            protected Task call() throws Exception {
                Task processedTask = (taskId != null) ? new Task(task.date, task.time, task.title, task.description) : task;
                if (taskId != null) {
                    processedTask.id = taskId;
                }
                String jsonData = RestApiController.this.gson.toJson(processedTask);
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(RestApiController.this.url + "tasks")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(jsonData)).build();


                try {
                    HttpResponse<String> response = RestApiController.this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    if (response.statusCode() == 200) {
                        System.out.println("Код ответа: " + response.statusCode());
                        System.out.println("Ответ от сервера: " + (String) response.body());
                        return (Task) RestApiController.this.gson.fromJson((String) response.body(), Task.class);
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        taskThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent workerStateEvent) {
                try {
                    Task result = (Task) taskThread.get();
                    callback.onTaskSaved(result);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        this.executor.execute(taskThread);
    }
}




