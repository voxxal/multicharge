import static com.raylib.Jaylib.RED;
import static com.raylib.Raylib.*;

public class Bullet extends Entity {

    public float lifetime;
    public float damage;
    public Color col;
    public boolean hit;

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
        hit = false;
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
        // temporary colour fix
        int r = col.r & 255;
        int g = col.g & 255;
        int b = col.b & 255;
        r *= 0.75;
        g *= 0.75;
        b *= 0.75;

        DrawCircleV(
            pos.toRaylib(),
            radius + 2,
            (new Color(r, g, b)).toRaylib()
        );
        DrawCircleV(pos.toRaylib(), radius, col.toRaylib());
    }
}
