package lab1.tasklist;

import lab1.dao.TaskDAO;
import lab1.dao.TaskFactory;
import lab1.model.Task;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    @FXML private TableView<Task> table;
    @FXML private TableColumn<Task, Integer> colId;
    @FXML private TableColumn<Task, String> colName;
    @FXML private TableColumn<Task, String> colTime;
    @FXML private TableColumn<Task, String> colExecutor;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, LocalDate> colDue;
    @FXML private TextField tfSearch;
    @FXML private ChoiceBox<String> cbSource; // добавлено поле для выбора источника

    private TaskDAO dao;
    private ObservableList<Task> data = FXCollections.observableArrayList();

    public void initialize() {
        // Настройка ChoiceBox
        cbSource.setItems(FXCollections.observableArrayList("Генерация", "Файл", "База данных"));
        cbSource.setValue("Генерация");
        cbSource.setOnAction(e -> switchSource());

        setupTable();
        switchSource(); // сразу загрузить данные из выбранного источника
    }

    private void setupTable() {
        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        colName.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getName()));
        colTime.setCellValueFactory(c -> new SimpleObjectProperty<>(formatTime(c.getValue())));
        colExecutor.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getExecutor()));
        colStatus.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getStatus()));
        colDue.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getDueDate()));
        table.setItems(data);
    }

    private void switchSource() {
        String source = cbSource.getValue();
        if ("Файл".equals(source)) {
            dao = TaskFactory.create("FILE");
        } else if ("База данных".equals(source)) {
            dao = TaskFactory.create("DB");
        } else {
            dao = TaskFactory.create("RAM");
        }
        loadData();
    }

    private void loadData() {
        if (dao != null) {
            data.setAll(dao.getAllTasks());
        }
    }

    private String formatTime(Task t) {
        return String.format("%02d:%02d", t.getHours(), t.getMinutes());
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    // --- Добавление/изменение задачи через диалог ---
    @FXML private void onAdd() {
        Task task = showTaskDialog(null);
        if (task != null) {
            dao.addTask(task);
            loadData();
        }
    }

    @FXML private void onEdit() {
        Task sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Выберите задачу для редактирования."); return; }

        Task updated = showTaskDialog(sel);
        if (updated != null) {
            dao.updateTask(updated);
            loadData();
        }
    }

    private Task showTaskDialog(Task task) {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle(task == null ? "Добавить задачу" : "Редактировать задачу");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField tfName = new TextField();
        tfName.setPromptText("Название");
        TextField tfHours = new TextField();
        tfHours.setPromptText("Часы");
        TextField tfMinutes = new TextField();
        tfMinutes.setPromptText("Минуты");
        TextField tfExecutor = new TextField();
        tfExecutor.setPromptText("Исполнитель");
        DatePicker dpDue = new DatePicker();
        ChoiceBox<String> cbStatus = new ChoiceBox<>(FXCollections.observableArrayList("Новая", "В процессе", "Выполнена"));

        if (task != null) {
            tfName.setText(task.getName());
            tfHours.setText(String.valueOf(task.getHours()));
            tfMinutes.setText(String.valueOf(task.getMinutes()));
            tfExecutor.setText(task.getExecutor());
            dpDue.setValue(task.getDueDate());
            cbStatus.setValue(task.getStatus());
        }

        grid.addRow(0, new Label("Название:"), tfName);
        grid.addRow(1, new Label("Время:"), tfHours, new Label("ч"), tfMinutes, new Label("м"));
        grid.addRow(2, new Label("Исполнитель:"), tfExecutor);
        grid.addRow(3, new Label("Срок:"), dpDue);
        grid.addRow(4, new Label("Статус:"), cbStatus);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) {
                Task t = task == null ? new Task() : task;
                t.setName(tfName.getText());
                t.setHours(parseInt(tfHours.getText(), 0));
                t.setMinutes(parseInt(tfMinutes.getText(), 0));
                t.setExecutor(tfExecutor.getText());
                t.setDueDate(dpDue.getValue());
                t.setStatus(cbStatus.getValue() != null ? cbStatus.getValue() : "Новая");
                t.setContext(t.getContext() == null ? "" : t.getContext());
                return t;
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (NumberFormatException ex) { return def; }
    }

    // --- Поиск ---
    @FXML private void onSearch() {
        String q = tfSearch.getText().trim().toLowerCase();
        if (q.isEmpty()) { loadData(); return; }

        List<Task> res = dao.getAllTasks().stream()
                .filter(t -> (t.getExecutor() != null && t.getExecutor().toLowerCase().contains(q)) ||
                        (t.getName() != null && t.getName().toLowerCase().contains(q)) ||
                        (t.getStatus() != null && t.getStatus().toLowerCase().contains(q)))
                .collect(Collectors.toList());
        data.setAll(res);
    }
}
