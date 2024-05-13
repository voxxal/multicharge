import static com.raylib.Jaylib.VIOLET;
import static com.raylib.Raylib.*;

public class Player extends Entity {

    public Player(float x, float y) {
        super(x, y, 10);
        vel.x(2);
    }

    public void draw() {
        DrawCircleV(pos, radius, VIOLET);
    }
}
