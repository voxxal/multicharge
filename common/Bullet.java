import static com.raylib.Jaylib.RED;
import static com.raylib.Raylib.*;

public class Bullet extends Entity {

    public float lifetime;
    public float damage;

    public Bullet(
        Vec2 origin,
        float radius,
        float angle,
        float speed,
        float lifetime,
        float damage
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

    public boolean onCollide(Entity other) {
        return !(other instanceof Bullet);
    }

    public void draw() {
        DrawCircleV(
            pos.toRaylib(),
            radius + 2,
            (new Color(131, 116, 72)).toRaylib()
        );
        DrawCircleV(
            pos.toRaylib(),
            radius,
            (new Color(175, 155, 96)).toRaylib()
        );
    }
}
