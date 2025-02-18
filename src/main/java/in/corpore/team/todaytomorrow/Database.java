package in.corpore.team.todaytomorrow;


import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.concurrent.Task;


public class Database implements DataStorge {
    private static final String INSERT_TASK_SQL = "INSERT INTO Task (id,date,time,title, description) VALUES (?, ? ,? ,? ,?)";
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public Database() {
    }

    public Connection connect() {
        String url = "jdbc:sqlite:database.db"; // SQLite создаст файл базы, если его нет
        try {

            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            // Создание таблицы, если её нет
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Task (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "date DATE, " +
                    "time TEXT," +
                    "title TEXT," +
                    "description TEXT)";
            stmt.execute(createTableSQL);
            stmt.close();
            System.out.println("Подключение к SQLite успешно!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к SQLite: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void saveTask(final in.corpore.team.todaytomorrow.Task task, Integer taskId, final SaveTaskCallback callback) {
        final Task<in.corpore.team.todaytomorrow.Task> taskThread = new Task<in.corpore.team.todaytomorrow.Task>() {
            protected in.corpore.team.todaytomorrow.Task call() {
                String insertSQL = "INSERT INTO Task (date, time, title, description) VALUES (?, ?, ?, ?)";
                String updateSQL = "UPDATE Task SET date = ?, time = ?, title = ?, description = ? WHERE id = ?";
                try (Connection conn = Database.this.connect()) {
                    PreparedStatement pstmt;
                    if (task.id != null) {
                        pstmt = conn.prepareStatement(updateSQL);
                        pstmt.setDate(1, new Date(task.date.getTime()));
                        pstmt.setString(2, task.time);
                        pstmt.setString(3, task.title);
                        pstmt.setString(4, task.description);
                        pstmt.setInt(5, task.id);

                    } else {
                        pstmt = conn.prepareStatement(insertSQL, 1);
                        pstmt.setDate(1, new java.sql.Date(task.date.getTime()));
                        pstmt.setString(2, task.time);
                        pstmt.setString(3, task.title);
                        pstmt.setString(4, task.description);
                    }
                    pstmt.executeUpdate();
                    System.out.println("Задача сохранена в базе данных!");

                    if (task.getId() == null) {
                        try (ResultSet rs = pstmt.getGeneratedKeys()) {
                            if (rs.next()) {
                                int newId = rs.getInt(1);
                                task.setId(newId);  // Устанавливаем id в задачу
                                System.out.println("Новая задача успешно сохранена с id: " + newId);
                            }
                        }

                    } else {
                        System.out.println("Задача успешно обновлена!");
                    }
                } catch (SQLException e) {
                    System.out.println("Ошибка при сохранении задачи: " + e.getMessage());
                }
                return task;
            }
        };
        taskThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent workerStateEvent) {
                try {
                    in.corpore.team.todaytomorrow.Task result = (in.corpore.team.todaytomorrow.Task) taskThread.get();
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


    @Override
    public void deleteTaskById(int taskId, DeleteTaskCallback callback) {
        this.executor.execute(new Runnable() {

            String deleteSQL = "DELETE FROM Task WHERE id = ?";
            boolean success = false;

            @Override
            public void run() {
                try (Connection conn = connect();
                     PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

                    if (conn == null) {
                        System.out.println("Ошибка: соединение с БД не установлено!");
                        return;
                    }
                    pstmt.setInt(1, taskId);
                    int affectedRows = pstmt.executeUpdate();
                    success = affectedRows > 0;

                    if (affectedRows > 0) {
                        System.out.println("Задача с id " + taskId + " удалена.");
                    } else {
                        System.out.println("Задача с id " + taskId + " не найдена.");
                    }

                } catch (SQLException e) {
                    System.out.println("Ошибка при удалении задачи: " + e.getMessage());
                }
                if (callback != null) {
                    callback.onTaskDeleted(success);
                }
            }
        });
    }

    @Override
    public void getAllTasks(final GetAllTaskCallback callback) {
        final Task<List<in.corpore.team.todaytomorrow.Task>> taskThread = new Task<List<in.corpore.team.todaytomorrow.Task>> () {
            protected List <in.corpore.team.todaytomorrow.Task> call() {
                List<in.corpore.team.todaytomorrow.Task> tasks = new ArrayList();
                String selectSQL = "SELECT * FROM Task";

                try (Connection conn = connect();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(selectSQL)) {

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        java.util.Date date = new java.util.Date(rs.getDate("date").getTime());
                        String time = rs.getString("time");
                        String title = rs.getString("title");
                        String description = rs.getString("description");

                        in.corpore.team.todaytomorrow.Task duplicatedTask = new in.corpore.team.todaytomorrow.Task(id, date, time, title, description);
                        tasks.add(duplicatedTask);
                    }
                    System.out.println("Задачи успешно получены!");

                } catch (SQLException e) {
                    System.out.println("Ошибка при получении задач: " + e.getMessage());
                }
                return tasks;
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

    public void duplicateTaskById(final in.corpore.team.todaytomorrow.Task task, final DublicateTaskCallback callback) {
        final Task<in.corpore.team.todaytomorrow.Task> taskThread = new Task<in.corpore.team.todaytomorrow.Task>() {
            protected in.corpore.team.todaytomorrow.Task call() {

                String selectSQL = "SELECT date, time, title, description FROM Task WHERE id = ?";
                String insertSQL = "INSERT INTO Task (date, time, title, description) VALUES (?, ?, ?, ?)";

                try (Connection conn = connect();
                     PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
                     PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {

                    selectStmt.setInt(1, task.getId());
                    ResultSet rs = selectStmt.executeQuery();


                    if (rs.next()) {
                        // Получаем данные задачи
                        java.sql.Date date = rs.getDate("date");
                        String time = rs.getString("time");
                        String title = rs.getString("title");
                        String description = rs.getString("description");

                        // Вставляем новую задачу (SQLite сам создаст новый id)
                        insertStmt.setDate(1, date);
                        insertStmt.setString(2, time);
                        insertStmt.setString(3, title);
                        insertStmt.setString(4, description);

                        insertStmt.executeUpdate();

                        // Получаем новый id для только что вставленной задачи
                        Statement statement = conn.createStatement();
                        ResultSet rs2 = statement.executeQuery("SELECT last_insert_rowid()");  // Получаем последний вставленный ID
                        if (rs2.next()) {
                            int newId = rs2.getInt(1);  // Получаем новый ID задачи

                            // Создаем новый объект задачи с правильным ID
                            in.corpore.team.todaytomorrow.Task duplicatedTask = new in.corpore.team.todaytomorrow.Task(newId, date, time, title, description); // Новый объект задачи с правильным ID
                            System.out.println("Задача с id " + task.getId() + " успешно продублирована!");
                            in.corpore.team.todaytomorrow.Task var16 = duplicatedTask;
                            return var16;
                        }
                    } else {
                        System.out.println("Задача с id " + task.getId() + " не найдена!");
                    }
                    return null;

                } catch (SQLException e) {
                    System.out.println("Ошибка при дублировании задачи: " + e.getMessage());
                    return null;
                }
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
}
