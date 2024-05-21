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
        return !(other instanceof Bullet);
    }

    public void draw() {
        // temporary colour fix
        int r = 0;
        int g = 0;
        int b = 0;
        if (col.r < 0) {
            r = 256 + col.r;
        } else {
            r = col.r;
        }
        if (col.g < 0) {
            g = 256 + col.g;
        } else {
            g = col.g;
        }
        if (col.b < 0) {
            b = 256 + col.b;
        } else {
            b = col.b;
        }
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
