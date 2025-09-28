package lab1.dao.impl;

import lab1.dao.TaskDAO;
import lab1.model.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Читает задачи из файла tasks.txt в папке resources.
 * Формат строки: название;часы;минуты;статус;исполнитель;срок;контекст
 */
public class FileTaskDAO implements TaskDAO {

    private final List<Task> tasks = new ArrayList<>();

    public FileTaskDAO() {
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            InputStream is = getClass().getResourceAsStream("/tasks.txt");
            if (is == null) throw new RuntimeException("tasks.txt не найден в resources");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isBlank()) continue;
                    String[] parts = line.split(";");
                    if (parts.length < 7) continue;

                    Task t = new Task();
                    t.setId(Integer.parseInt(parts[0].trim()));
                    t.setName(parts[1].trim());
                    t.setHours(Integer.parseInt(parts[2].trim()));
                    t.setMinutes(Integer.parseInt(parts[3].trim()));
                    t.setStatus(parts[4].trim());
                    t.setExecutor(parts[5].trim());
                    t.setDueDate(parts[6].trim().isEmpty() ? null : LocalDate.parse(parts[6].trim()));
                    t.setContext(parts[7].trim());
                    tasks.add(t);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения tasks.txt", e);
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
        tasks.add(task);
    }

    @Override
    public void updateTask(Task task) {
        // для простоты предполагаем, что объект уже изменен в списке
    }

    @Override
    public void deleteTask(int id) {
        tasks.removeIf(t -> t.getId() == id);
    }
}
