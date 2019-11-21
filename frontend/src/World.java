import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import javafx.scene.shape.*;

public class World extends Application {

    private ArrayList<Shape> nodes;
    private Scene login, game;

    @Override public void start(Stage primaryStage) {
        primaryStage.setTitle("Conquer all towns");

        //login scene
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Nick name:");
        grid.add(userName, 0, 1);
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Button startbtn = new Button("Start");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(startbtn);
        grid.add(hbBtn, 1, 4);
        startbtn.setOnAction(e -> primaryStage.setScene(game));

        login = new Scene(grid, 300, 275);

        //game scene
        Group root = new Group();
        game = new Scene(root, 400, 400);

        nodes = new ArrayList<>();
        nodes.add(new Circle(15, 15, 30));
        nodes.add(new Circle(90, 60, 30));
        nodes.add(new Circle(40, 200, 30));
        for (Shape block : nodes) {
            setDragListeners(block);
        }
        root.getChildren().addAll(nodes);
        checkShapeIntersection(nodes.get(nodes.size() - 1));

        primaryStage.setScene(login);
        primaryStage.show();
    }

    public void setDragListeners(final Shape block) {
        final Delta dragDelta = new Delta();

        block.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = block.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = block.getLayoutY() - mouseEvent.getSceneY();
                block.setCursor(Cursor.NONE);
            }
        });
        block.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                block.setCursor(Cursor.HAND);
            }
        });
        block.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                block.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                block.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                checkShapeIntersection(block);
            }
        });
    }

    private void checkShapeIntersection(Shape block) {
        boolean collisionDetected = false;
        for (Shape static_bloc : nodes) {
            if (static_bloc != block) {
                static_bloc.setFill(Color.GREEN);

                Shape intersect = Shape.intersect(block, static_bloc);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    collisionDetected = true;
                }
            }
        }

        if (collisionDetected) {
            block.setFill(Color.BLUE);
        } else {
            block.setFill(Color.GREEN);
        }
    }

    class Delta { double x, y; }
}