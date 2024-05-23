import static com.raylib.Jaylib.RED;
import static com.raylib.Raylib.*;

public class Bullet extends Entity {

    public float lifetime;
    public float damage;
    public Color col;

    public Bullet(
        Vec2 origin,
        float radius,
        float angle,
        float speed,
        float lifetime,
        float damage,
        Color col
    ) {
        super(origin.x, origin.y, radius);
        vel.x = (float) Math.cos(angle) * speed;
        vel.y = (float) Math.sin(angle) * speed;
        this.lifetime = lifetime;
        this.damage = damage;
        this.col = col;
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
        return !(other instanceof Bullet) && other.collideable;
    }

    public void draw() {
        DrawCircleV(
            pos.toRaylib(),
            radius + 2,
            col.scale(0.75).toRaylib()
        );
        DrawCircleV(pos.toRaylib(), radius, col.toRaylib());
    }
}
