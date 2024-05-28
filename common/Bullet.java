import static com.raylib.Jaylib.RED;
import static com.raylib.Raylib.*;

public class Bullet extends Entity {

    public float lifetime;
    public float damage;
    public Color col;

    public Bullet(
        Player player,
        float len1,
        float radius,
        float angle,
        float speed,
        float lifetime,
        float damage,
        Color col
    ) {
        super(
            player.pos.x + (float) (Math.cos(player.angle) * len1),
            player.pos.y + (float) (Math.sin(player.angle) * len1),
            radius
        );
        vel.x = (float) Math.cos(angle) * speed + player.vel.x;
        vel.y = (float) Math.sin(angle) * speed + player.vel.y;
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
        DrawCircleV(pos.toRaylib(), radius + 2, col.scale(0.75).toRaylib());
        DrawCircleV(pos.toRaylib(), radius, col.toRaylib());
    }
}
