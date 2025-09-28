package lab1.dao.impl;

import lab1.dao.TaskDAO;
import lab1.model.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListTaskDAO implements TaskDAO {
    private final List<Task> tasks;

    public ListTaskDAO(int size) {
        tasks = new ArrayList<>();
        Random random = new Random();
        String[] statuses = {"Новая", "В процессе", "Выполнена"};
        String[] executors = {"Алиса", "Саша", "Олег", "Диана"};
        for (int i = 0; i < size; i++) {
            Task t = new Task();
            t.setId(i + 1);
            t.setName("Задача " + (i + 1));
            t.setHours(random.nextInt(24));
            t.setMinutes(random.nextInt(60));
            t.setStatus(statuses[random.nextInt(statuses.length)]);
            t.setExecutor(executors[random.nextInt(executors.length)]);
            t.setDueDate(LocalDate.now().plusDays(random.nextInt(30)));
            t.setContext("Описание задачи " + (i + 1));
            tasks.add(t);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    @Override
    public Task getTaskById(int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void addTask(Task task) {
        int nextId = tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
        task.setId(nextId);
        tasks.add(task);
    }

    @Override
    public void updateTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                return;
            }
        }
    }

    @Override
    public void deleteTask(int id) {
        tasks.removeIf(t -> t.getId() == id);
    }
}
