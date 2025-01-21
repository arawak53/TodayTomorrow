package in.corpore.team.todaytomorrow;

import java.util.Calendar;
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


    public int getDayOfWeek (){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return week;
    }
}
