package lab1.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private int id;
    private String name;
    private int hours;    // часы
    private int minutes;  // минуты
    private String status;
    private String executor;
    private LocalDate dueDate;
    private String context;

    public Task() {}

    public Task(int id, String name, int hours, int minutes, String status,
                String executor, LocalDate dueDate, String context) {
        this.id = id;
        this.name = name;
        this.hours = hours;
        this.minutes = minutes;
        this.status = status;
        this.executor = executor;
        this.dueDate = dueDate;
        this.context = context;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHours() { return hours; }
    public void setHours(int hours) { this.hours = hours; }

    public int getMinutes() { return minutes; }
    public void setMinutes(int minutes) { this.minutes = minutes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getExecutor() { return executor; }
    public void setExecutor(String executor) { this.executor = executor; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
}
