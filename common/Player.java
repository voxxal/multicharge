import static com.raylib.Raylib.*;

public class Player extends Entity {

    public int playerId = 0;

    public Player(float x, float y, int playerId) {
        super(x, y, 25);
        vel.x = 2;
        this.playerId = playerId;
    }

    public Player(float x, float y) {
        super(x, y, 25);
        vel.x = 2;
    }

    public void draw() {
        DrawCircleV(pos.toRaylib(), radius, new Color(255, 145, 145, 255));
        DrawCircleLinesV(pos.toRayLib(), radius, new Color(10, 10, 10, 255));
        DrawRectanglePro(new Rectangle(pos.x - 10, pos.y - radius + 4, 8, 40), new Vector2(pos.x, pos.y), angle, new Color(140, 136, 126, 255));
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
