import static com.raylib.Jaylib.VIOLET;
import static com.raylib.Raylib.*;

public class Player extends Entity {

    public int playerId = 0;

    public Player(float x, float y, int playerId) {
        super(x, y, 10);
        vel.x = 2;
        this.playerId = playerId;
    }

    public Player(float x, float y) {
        super(x, y, 25);
        vel.x = 2;
    }

    public void draw() {
        DrawCircleV(pos.toRaylib(), radius, VIOLET);
    }

    public String toString() {
        return String.format(
            "Player { player = %d, pos = %s, vel = %s }",
            playerId,
            pos,
            vel
        );
    }
}
