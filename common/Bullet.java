import static com.raylib.Jaylib.RED;
import static com.raylib.Raylib.*;

public class Bullet extends Entity {

    public float deceleration = 0;

    public Bullet(Vec2 origin, float radius, float angle, float speed) {
        super(origin.x, origin.y, radius);
        vel.x = (float) Math.cos(angle) * speed;
        vel.y = (float) Math.sin(angle) * speed;
    }

    public void draw() {
        DrawCircleV(pos.toRaylib(), radius, RED);
    }
}
