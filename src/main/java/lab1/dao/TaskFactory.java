package lab1.dao;

import lab1.dao.impl.DbTaskDAO;
import lab1.dao.impl.FileTaskDAO;
import lab1.dao.impl.ListTaskDAO;

public class TaskFactory {

    public static TaskDAO create(String type) {
        return switch (type.toUpperCase()) {
            case "DB" -> new DbTaskDAO();        // без аргументов
            case "FILE" -> new FileTaskDAO();    // аналогично FileTaskDAO без аргументов
            default -> new ListTaskDAO(10);      // генерация списка из 10 элементов
        };
    }
}
