package game.view;
import game.controller.WorldController;
import game.controller.WorldScene;
import javafx.animation.AnimationTimer;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class World implements ViewInterface {

    private WorldScene world;
    private WorldController controller;
    private int windowWidth = 800;
    private int windowHeight = 600;
    private int gameMapHeight = 2000;
    private int gameMapWidth = 4000;
    private Image backgroundImage;

    public World() {
        this.controller = new WorldController();
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
                controller.getObjects()
        );
        g.getChildren().add(a);



        BorderPane borderPane = new BorderPane();
        borderPane.setTop(getMenu());


        ImageView imageView = new ImageView(backgroundImage);
        imageView.setFitHeight(gameMapHeight);
        imageView.setFitWidth(gameMapWidth);
        //BackgroundImage backgroundImageView = new BackgroundImage(backgroundImage,BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.AUTO);


        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        //splitPane.setDividerPosition(800, 900);
        //splitPane.getDividers().get(0).setPosition(100);
        splitPane.getItems().add(getInfoBox());
        //splitPane.getItems().add(gamePane);

        AnchorPane anchorPane1 = new AnchorPane();

        StackPane stack = new StackPane();
        stack.getChildren().setAll(imageView);
        ScrollPane scroll = controller.createScrollPane(stack);
        scroll.setPannable(true);
        scroll.setOnDragOver(e -> {
            //todo try to add infinite scroll
            int marge = 10;
            System.out.println("scroll to edge");
            //TODO make camera move
            //left top corner
            if(marge > e.getX() && marge > e.getY()){
                scroll.setVvalue(scroll.getVvalue() -0.001);
                scroll.setHvalue(scroll.getHvalue() -0.001);
            }//left bottom corner
            else if(marge > e.getX() && scroll.getHeight()-marge < e.getY()){
                scroll.setVvalue(scroll.getVvalue() +0.001);
                scroll.setHvalue(scroll.getHvalue() -0.001);
            }//right top corner
            else if(scroll.getWidth()-marge < e.getX() && marge > e.getY()){
                scroll.setVvalue(scroll.getVvalue() -0.001);
                scroll.setHvalue(scroll.getHvalue() +0.001);
            }//right bottom corner
            else if(scroll.getWidth()-marge < e.getX() && scroll.getHeight()-marge < e.getY() ){
                scroll.setVvalue(scroll.getVvalue() +0.001);
                scroll.setHvalue(scroll.getHvalue() +0.001);
            }//top
            else if(marge > e.getY()){
                scroll.setVvalue(scroll.getVvalue() -0.001);
            }//right
            else if(scroll.getWidth()-marge < e.getX()){
                scroll.setHvalue(scroll.getHvalue() +0.001);
            }//bottom
            else if(scroll.getHeight()-marge < e.getY()){
                scroll.setVvalue(scroll.getVvalue() +0.001);
            }//left
            else if(marge > e.getX()){
                scroll.setHvalue(scroll.getHvalue() -0.001);
            }

        });
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

        // wrap the scene contents in a pannable scroll pane.


        world = new WorldScene(rootAnchor, windowWidth, windowHeight);

        // bind the preferred size of the scroll area to the size of the scene.
        scroll.prefWidthProperty().bind(world.widthProperty());
        scroll.prefHeightProperty().bind(world.heightProperty());

        // center the scroll contents.
        scroll.setHvalue(scroll.getHmin() + (scroll.getHmax() - scroll.getHmin()) / 2);
        scroll.setVvalue(scroll.getVmin() + (scroll.getVmax() - scroll.getVmin()) / 2);

        //game loop

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

    //infobox of villages
    //should be called on click
    private Text villageHpText;
    private Text villageSoldierText;
    private int villageHp = 0;
    private int numOfVillageSoldiers = 0;
    private VBox detailInfoBox(){
        VBox infoBox = new VBox();

        villageHpText = new Text();
        villageHpText.setText("Village Hp: " + villageHp);

        villageSoldierText = new Text();
        villageSoldierText.setText("Number of Soldiers: " + numOfVillageSoldiers);

        infoBox.getChildren().addAll(
                villageHpText,
                villageSoldierText
        );
        return infoBox;
    }


    private ScrollPane getGameMap(){
        ScrollPane scrollPane = new ScrollPane();
        Text hitPointText = new Text();
        //exampleText
        hitPointText.setText("hitPoint: 10");

        Text numSoldierText = new Text();
        numSoldierText.setText("Number of Soldiers: 10");
        return scrollPane;
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
