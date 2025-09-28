package lab1.tasklist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Загружаем FXML из папки resources/view/
        URL fxmlLocation = getClass().getResource("/view/main-view.fxml");
        if (fxmlLocation == null) {
            throw new IllegalStateException("FXML не найден! Проверьте путь: /view/main-view.fxml");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Список задач");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
