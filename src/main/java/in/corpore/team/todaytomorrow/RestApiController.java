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
    public List<Task> getTask() {
        URI uri = URI.create("http://91.211.14.76:9090/tasks");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            Gson gson = new Gson();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("код ответа" + response.statusCode());
            System.out.println("Ответ от сервера: \n" + response.body());

            ArrayList<Task> listTask1 = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
            }.getType());
            System.out.println("Вывод на консоль: " + listTask1);

            return listTask1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void deleteTask(int taskId) {
        URI uri = URI.create("http://91.211.14.76:9090/tasks/" + taskId);

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest deletedRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        try {
            HttpResponse<String> deletedResponse = client.send(deletedRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Код ответа: " + deletedResponse.statusCode());
            System.out.println("Ответ от сервера: " + deletedResponse.body());

        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    public Task dublicateTask(Task task) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        String jsonCope = gson.toJson(task);
        URI uri = URI.create("http://91.211.14.76:9090/tasks");

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest requestCopy = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonCope))
                .build();

        try {
            HttpResponse<String> response = client.send(requestCopy, HttpResponse.BodyHandlers.ofString());
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
        Gson gson1 = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        Task updatedTask = new Task(task.date, task.time, task.title, task.description);
        updatedTask.id = taskId;
        String jsonData = gson1.toJson(updatedTask);


        URI uri = URI.create("http://91.211.14.76:9090/tasks");

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Код ответа: " + response.statusCode());
                System.out.println("Ответ от сервера: " + response.body());
                return gson1.fromJson(response.body(), Task.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public Task сreateNewTask (Task task){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        // Преобразуем объект Task в JSON
        String jsonData = gson.toJson(task);

        HttpClient client1 = HttpClient.newBuilder().build();
        HttpRequest request1 = HttpRequest.newBuilder()

                .uri(URI.create("http://91.211.14.76:9090/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();
        try {
            HttpResponse<String> response = client1.send(request1, HttpResponse.BodyHandlers.ofString());
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



