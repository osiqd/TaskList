module lab.tasklist {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires dotenv;


    opens lab1.tasklist to javafx.fxml;
    exports lab1.tasklist;
}