package lab1.tasklist;

import lab1.model.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

public class TaskDialog extends Dialog<Task> {

    private TextField tfName = new TextField();
    private TextField tfHours = new TextField();
    private TextField tfMinutes = new TextField();
    private TextField tfExecutor = new TextField();
    private DatePicker dpDue = new DatePicker();
    private ComboBox<String> cbStatus = new ComboBox<>();

    public TaskDialog(Task task) {
        setTitle(task == null ? "Добавить задачу" : "Редактировать задачу");
        setHeaderText(null);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        cbStatus.getItems().addAll("Новая", "В процессе", "Выполнена");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        grid.add(new Label("Название:"), 0, 0);
        grid.add(tfName, 1, 0);
        grid.add(new Label("Время (чч:мм):"), 0, 1);
        grid.add(tfHours, 1, 1);
        grid.add(tfMinutes, 2, 1);
        grid.add(new Label("Исполнитель:"), 0, 2);
        grid.add(tfExecutor, 1, 2);
        grid.add(new Label("Срок:"), 0, 3);
        grid.add(dpDue, 1, 3);
        grid.add(new Label("Статус:"), 0, 4);
        grid.add(cbStatus, 1, 4);

        if (task != null) {
            tfName.setText(task.getName());
            tfHours.setText(String.valueOf(task.getHours()));
            tfMinutes.setText(String.valueOf(task.getMinutes()));
            tfExecutor.setText(task.getExecutor());
            dpDue.setValue(task.getDueDate());
            cbStatus.setValue(task.getStatus());
        }

        getDialogPane().setContent(grid);

        setResultConverter(btn -> {
            if (btn == okButton) {
                Task t = task != null ? task : new Task();
                t.setName(tfName.getText());
                int h = parseInt(tfHours.getText(), 0);
                int m = parseInt(tfMinutes.getText(), 0);
                t.setHours(h);
                t.setMinutes(m);
                t.setExecutor(tfExecutor.getText());
                t.setDueDate(dpDue.getValue() != null ? dpDue.getValue() : LocalDate.now());
                t.setStatus(cbStatus.getValue() != null ? cbStatus.getValue() : "Новая");
                return t;
            }
            return null;
        });
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return def; }
    }
}
