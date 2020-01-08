package game.view;
import game.controller.WorldController;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.logging.Logger;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

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

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(getMenu());

        ImageView imageView = new ImageView(backgroundImage);
        imageView.setFitHeight(gameMapHeight);
        imageView.setFitWidth(gameMapWidth);

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        Platform.runLater(()->{splitPane.setDividerPositions(0.1f);});
        splitPane.getItems().add(getInfoBox());

        controller.stack.getChildren().setAll(
                imageView,
                controller.group
        );

        // wrap the scene contents in a pannable scroll pane.
        ScrollPane scroll = controller.createScrollPane(controller.stack);
        scroll.setPannable(true);
        splitPane.getItems().add(scroll);

        AnchorPane rootAnchor = new AnchorPane();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                borderPane,
                splitPane

        );
        rootAnchor.getChildren().addAll(
                vBox
        );

        world = new Scene(rootAnchor, windowWidth, windowHeight);

        // bind the preferred size of the scroll area to the size of the scene.
        scroll.prefWidthProperty().bind(world.widthProperty());
        scroll.prefHeightProperty().bind(world.heightProperty());

        // center the scroll contents.
        scroll.setHvalue(scroll.getHmin() + (scroll.getHmax() - scroll.getHmin()) / 2);
        scroll.setVvalue(scroll.getVmin() + (scroll.getVmax() - scroll.getVmin()) / 2);

        return world;
    }

    //infobox of current player status
    //todo get these number from server eventually
    private Text villageAmountText;
    private Text numSoldierText;
    private int numOfVillages = 0;
    private int numOfSoldiers = 0;
    private AnchorPane getInfoBox(){
        AnchorPane parent = new AnchorPane();
        VBox infoBox = new VBox();
        villageAmountText = new Text();
        //exampleText
        villageAmountText.setText("Villages: " + numOfVillages);

        numSoldierText = new Text();
        numSoldierText.setText("Number of Soldiers: " + numOfSoldiers);

        infoBox.getChildren().addAll(
                villageAmountText,
                numSoldierText
        );
        parent.getChildren().add(infoBox);

        return  parent;
    }

    //todo maybe add eventhandlers
    private MenuBar getMenu(){
        Menu menu = new Menu("Menu");
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setText("test");
        menu.getItems().add(menuItem1);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        return  menuBar;
    }


    public void update(){
        numOfVillages++;
        villageAmountText.setText("Villages: " + numOfVillages);

    }
}
