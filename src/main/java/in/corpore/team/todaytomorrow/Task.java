package in.corpore.team.todaytomorrow;

import java.util.Calendar;
import java.util.Date;

public class Task {
    public Date date;
    public String time;
    public String title;
    public String description;
    public Integer id;

    Task (Date date, String time, String title, String description) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.title = title;

    }
    Task (Integer id,Date date, String time, String title, String description ) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
        this.title = title;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public int getDayOfWeek (){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return week;
    }
    public int getMonth  (){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        return month;
    }




    @Override
    public String toString() {
        return "Date: "+ date + ", Time: " + time + ", Description: "+ description + ", Title " + title + "\n";
    }
}
