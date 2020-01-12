import game.model.map.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


class Game {
    private WorldModel world;
    private static Logger LOGGER = Logger.getLogger("InfoLogging");
    private int obstacles = 10;
    private int towns = 20;
    private int gameMapHeight = 2000; // has to match with the gameMapHeight in frontend
    private int gameMapWidth = 4000; // has to match with the gameMapWidth in frontend
    private int distanceToEdge = 100;
    private ArrayList<TreeNode> areaContent;

    public Game() {
        this.world = new WorldModel(0, 0, gameMapWidth, gameMapHeight);
        LOGGER.log(Level.INFO,"Creating map");
        genereateInitialMap();
    }

    private void genereateInitialMap() {
        createObstacles();
        createTowns();
    }
     private void createTowns(){
         for (int i = 0; i < towns; i++){
             this.world.insert( new Town(
                     randomNumber(distanceToEdge, gameMapWidth - distanceToEdge),
                     randomNumber(distanceToEdge, gameMapHeight - distanceToEdge)
             ));
         }
     }

     private void createObstacles(){
         for (int i = 0; i < obstacles; i++){
             this.world.insert(new Obstacle(
                     randomNumber(distanceToEdge, gameMapWidth - distanceToEdge),
                     randomNumber(distanceToEdge, gameMapHeight - distanceToEdge),
                     randomNumber(0, 1),
                     randomNumber(50, 400)));
         }
     }

     private int randomNumber(int min, int max){
         Random r = new Random();
         return r.nextInt(max - min + 1) + min;
     }

     public ArrayList<TreeNode> getAreaContent(){
         return this.world.getAreaContent(0,0,4000,2000);
     }

     public Quadtree getInitialMap(){
        return this.world.getQuadtree();
     }
}
