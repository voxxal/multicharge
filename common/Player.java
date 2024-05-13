import static com.raylib.Jaylib.VIOLET;
import static com.raylib.Raylib.*;

public class Player extends Entity {
    public int playerId = 0;

    public Player(float x, float y, int playerId = 0) {
        super(x, y, 10);
        vel.x(2);
        this.playerId = playerId;
    }

    public void draw() {
        DrawCircleV(pos, radius, VIOLET);
    }
}
