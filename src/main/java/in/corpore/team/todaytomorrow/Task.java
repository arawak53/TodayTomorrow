package in.corpore.team.todaytomorrow;

import java.util.Date;

public class Task {
    public Date date;
    public String time;
    public String title;
    public String description;

    Task (Date date, String time, String title, String description) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.title = title;
    }

}
