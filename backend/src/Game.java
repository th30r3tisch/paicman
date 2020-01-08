import game.map.Boundry;
import game.map.Quadtree;
import game.model.TreeNode;
import game.model.Obstacle;
import game.model.Player;
import game.model.Town;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


class Game {
    private Quadtree map;
    private static Logger LOGGER = Logger.getLogger("InfoLogging");
    private int obstacles = 10;
    private int towns = 20;
    private int gameMapHeight = 2000; // has to match with the gameMapHeight in frontend
    private int gameMapWidth = 4000; // has to match with the gameMapWidth in frontend
    private ArrayList<TreeNode> areaContent;

    public Game() {
        this.map = new Quadtree(1, new Boundry(0, 0, gameMapWidth, gameMapHeight));

        LOGGER.log(Level.INFO,"Creating map");
        genereateInitialMap();
    }

    private void genereateInitialMap() {
        createObstacles();
        createTowns();
    }
     private void createTowns(){
         for (int i = 0; i < towns; i++){
             map.insert( new Town(
                     randomNumber(0, gameMapWidth),
                     randomNumber(0, gameMapHeight)
             ));
         }
     }

     private void createObstacles(){
         for (int i = 0; i < obstacles; i++){
             map.insert(new Obstacle(
                     randomNumber(0, gameMapWidth),
                     randomNumber(0, gameMapHeight),
                     randomNumber(0, 1),
                     randomNumber(50, 400)));
         }
     }

     private int randomNumber(int min, int max){
         Random r = new Random();
         return r.nextInt(max - min + 1) + min;
     }

     public ArrayList<TreeNode> getAreaContent(){
         this.areaContent = map.getAllContent(map, 0,0,2000,1000);
         System.out.println("------------------------------------------------------------------");
         map.getAreaContent(map, 0,0,2000,1000);
         return areaContent;
     }
}
