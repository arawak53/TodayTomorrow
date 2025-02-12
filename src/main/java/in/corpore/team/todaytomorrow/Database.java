package in.corpore.team.todaytomorrow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database implements DataStorge{
    private static final String INSERT_TASK_SQL = "INSERT INTO Task (id,date,time,title, description) VALUES (?, ? ,? ,? ,?)";

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
    public Task saveTask(Task task, Integer taskId) {
        String insertSQL = "INSERT INTO Task (date, time, title, description) VALUES (?, ?, ?, ?)";
        String updateSQL = "UPDATE Task SET date = ?, time = ?, title = ?, description = ? WHERE id = ?";
        try (Connection conn = connect()) {
            PreparedStatement pstmt;
            if (task.id != null) {
                pstmt = conn.prepareStatement(updateSQL);
                pstmt.setDate(1, new Date(task.date.getTime()));
                pstmt.setString(2, task.time);
                pstmt.setString(3, task.title);
                pstmt.setString(4, task.description);
                pstmt.setInt(5, task.id);

            } else {
                pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
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

    @Override
    public void deleteTaskById(int taskId) {
        String deleteSQL = "DELETE FROM Task WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            if (conn == null) {
                System.out.println("Ошибка: соединение с БД не установлено!");
                return;
            }

            pstmt.setInt(1, taskId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Задача с id " + taskId + " удалена.");
            } else {
                System.out.println("Задача с id " + taskId + " не найдена.");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при удалении задачи: " + e.getMessage());
        }
    }
    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
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

                Task task = new Task(id, date, time, title, description);
                tasks.add(task);
            }
            System.out.println("Задачи успешно получены!");

        } catch (SQLException e) {
            System.out.println("Ошибка при получении задач: " + e.getMessage());
        }
        return tasks;
    }

    public Task duplicateTaskById(Task task) {
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
                    Task duplicatedTask = new Task(newId, date, time, title, description); // Новый объект задачи с правильным ID
                    System.out.println("Задача с id " + task.getId() + " успешно продублирована!");

                    return duplicatedTask;
                }

            } else {
                System.out.println("Задача с id " + task.getId() + " не найдена!");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при дублировании задачи: " + e.getMessage());
        }
        return null;
    }


}
