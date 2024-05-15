import static com.raylib.Jaylib.RED;
import static com.raylib.Raylib.*;

public class Bullet extends Entity {

    public float lifetime;

    public Bullet(
        Vec2 origin,
        float radius,
        float angle,
        float speed,
        float lifetime
    ) {
        super(origin.x, origin.y, radius);
        deceleration = 0;
        vel.x = (float) Math.cos(angle) * speed;
        vel.y = (float) Math.sin(angle) * speed;
        this.lifetime = lifetime;
    }

    public void update(World world, float dt) {
        lifetime -= dt;
        if (lifetime <= 0) {
            world.remove(this);
            return;
        }
        super.update(world, dt);
    }

    public void draw() {
        DrawCircleV(pos.toRaylib(), radius, RED);
    }
}
