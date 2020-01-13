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
    private int townMinDist = 100;
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
             createTown();
         }
     }

     public Town createTown(){
         Town t = null;
         while(t == null){
             int x = randomNumber(distanceToEdge, gameMapWidth - distanceToEdge);
             int y = randomNumber(distanceToEdge, gameMapHeight - distanceToEdge);
             if (this.getAreaContent((x - townMinDist), (y - townMinDist), (x + townMinDist), (y + townMinDist)).size() == 0) {
                 t = new Town(x, y);
             }
         }
         this.world.insert(t);
         return t;
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

     public ArrayList<TreeNode> getAreaContent(int startX, int startY, int endX, int endY){
         return this.world.getAreaContent(startX, startY, endX, endY);
     }

     public Quadtree getInitialMap(){
        return this.world.getQuadtree();
     }
}
