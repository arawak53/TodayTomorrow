package in.corpore.team.todaytomorrow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class RestApiController {

    private HttpClient httpClient ;
    private String url;
    private Gson gson;

    public RestApiController  (){
        httpClient = HttpClient.newHttpClient();
        url = "http://91.211.14.76:9090/";
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
    }




    public List<Task> getTask() {
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


    public void deleteTask(int taskId) {
        HttpRequest deletedRequest = HttpRequest.newBuilder()
                .uri(URI.create(url + taskId))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        try {
            HttpResponse<String> deletedResponse = httpClient.send(deletedRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Код ответа: " + deletedResponse.statusCode());
            System.out.println("Ответ от сервера: " + deletedResponse.body());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Task dublicateTask(Task task) {
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


    public Task editTask(Task task, int taskId) {
        Task updatedTask = new Task(task.date, task.time, task.title, task.description);
        updatedTask.id = taskId;
        String jsonData = gson.toJson(updatedTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();
        
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Код ответа: " + response.statusCode());
                System.out.println("Ответ от сервера: " + response.body());
                return gson.fromJson(response.body(), Task.class);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Task сreateNewTask(Task task) {
        // Преобразуем объект Task в JSON
        String jsonData = gson.toJson(task);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request1, HttpResponse.BodyHandlers.ofString());
            System.out.println("Код ответа: " + response.statusCode());
            System.out.println("Ответ от сервера: " + response.body());
            if (response.statusCode() == 200) {
                Task createdTask = gson.fromJson(response.body(), Task.class);

                return createdTask;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}



