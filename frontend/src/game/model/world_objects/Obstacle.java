package game.model.world_objects;

public class Obstacle extends WorldObject {

    ObstacleType obstacleType;

    public Obstacle(ObstacleType obstacleType) {
        this.obstacleType = obstacleType;
    }
}
