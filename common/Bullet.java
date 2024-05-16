import static com.raylib.Jaylib.RED;
import static com.raylib.Raylib.*;

public class Bullet extends Entity {

    public float lifetime;
    public int damage;

    public Bullet(
        Vec2 origin,
        float radius,
        float angle,
        float speed,
        float lifetime,
        int damage
    ) {
        super(origin.x, origin.y, radius);
        vel.x = (float) Math.cos(angle) * speed;
        vel.y = (float) Math.sin(angle) * speed;
        this.lifetime = lifetime;
        this.damage = damage;
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
