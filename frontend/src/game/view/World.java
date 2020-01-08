package game.view;
import game.controller.WorldController;
import game.model.WorldModel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.logging.Logger;

public class World implements ViewInterface {

    private Scene world;
    private WorldController controller;
    private int windowWidth = 1200;
    private int windowHeight = 800;
    private int gameMapHeight = 2000;
    private int gameMapWidth = 4000;
    private Image backgroundImage;
    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    public World(WorldController wc) {
        this.controller = wc;
        backgroundImage = new Image("assets/map.jpg");
    }

    @Override
    public Scene getScene() {

        controller.getObjects();

        Menu menu = new Menu("Menu");
        MenuBar menubar = new MenuBar();
        menubar.getMenus().add(menu);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menubar);

        ImageView background = new ImageView(backgroundImage);
        background.setFitHeight(gameMapHeight);
        background.setFitWidth(gameMapWidth);

        controller.stack.getChildren().setAll(
                background,
                borderPane,
                controller.group
        );

        // wrap the scene contents in a pannable scroll pane.
        ScrollPane scroll = controller.createScrollPane(controller.stack);

        world = new Scene(scroll, windowWidth, windowHeight);

        // bind the preferred size of the scroll area to the size of the scene.
        scroll.prefWidthProperty().bind(world.widthProperty());
        scroll.prefHeightProperty().bind(world.heightProperty());

        // center the scroll contents.
        scroll.setHvalue(scroll.getHmin() + (scroll.getHmax() - scroll.getHmin()) / 2);
        scroll.setVvalue(scroll.getVmin() + (scroll.getVmax() - scroll.getVmin()) / 2);

        return world;
    }
}
