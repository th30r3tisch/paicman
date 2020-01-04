package game.view;
import game.controller.WorldController;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class World implements ViewInterface {

    private Scene world;
    private WorldController controller;
    private int windowWidth = 800;
    private int windowHeight = 1200;
    private int gameMapHeight = 2000;
    private int gameMapWidth = 4000;
    private Image backgroundImage;

    public World(WorldController wc) {
        this.controller = wc;
        backgroundImage = new Image("assets/map.jpg");
    }

    @Override
    public Scene getScene() {

        Group g = new Group();
        Circle c = new Circle(0, 0, 50, Color.GREEN);
        Circle b = new Circle(-50, 0, 50, Color.RED);
        Circle a = new Circle(50, 0, 50, Color.BLACK);
        Rectangle r = new Rectangle(100, 100, Color.BLUE);
        r.setHeight(100);
        r.setWidth(50);
        g.getChildren().addAll(
                c,
                b,
                a
        );

        Menu menu = new Menu("Menu");
        MenuBar menubar = new MenuBar();
        menubar.getMenus().add(menu);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menubar);

        ImageView background = new ImageView(backgroundImage);
        background.setFitHeight(gameMapHeight);
        background.setFitWidth(gameMapWidth);

        StackPane stack = new StackPane();
        stack.getChildren().setAll(
                background,
                borderPane,
                g
        );

        // wrap the scene contents in a pannable scroll pane.
        ScrollPane scroll = controller.createScrollPane(stack);

        world = new Scene(scroll, windowWidth, windowHeight);

        // bind the preferred size of the scroll area to the size of the scene.
        scroll.prefWidthProperty().bind(world.widthProperty());
        scroll.prefHeightProperty().bind(world.heightProperty());

        // center the scroll contents.
        scroll.setHvalue(scroll.getHmin() + (scroll.getHmax() - scroll.getHmin()) / 2);
        scroll.setVvalue(scroll.getVmin() + (scroll.getVmax() - scroll.getVmin()) / 2);

        //game loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                //add update method
            }
        };
        timer.start();
        return world;
    }
}
