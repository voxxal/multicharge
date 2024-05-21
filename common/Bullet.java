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

    public void draw() {
        System.out.println(col.r);
        System.out.println(col.g);
        System.out.println(col.b);
        DrawCircleV(pos.toRaylib(), radius + 2, (new Color((int) (col.r * 0.75f), (int) (col.g * 0.75f), (int) (col.b * 0.75f))).toRaylib());
        DrawCircleV(pos.toRaylib(), radius, col.toRaylib());
    }
}
