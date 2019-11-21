package view;

import javafx.application.Application;
import javafx.stage.Stage;

public class World extends Application {

    @Override public void start(Stage primaryStage) {
        primaryStage.setTitle("Conquer all towns");
        primaryStage.setScene(new Login(primaryStage).getScene());
        primaryStage.show();
    }
}