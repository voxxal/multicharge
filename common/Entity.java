import static com.raylib.Raylib.*;

import java.io.Serializable;

public abstract class Entity implements Collideable<Entity>, Serializable {

    public int id;
    // these are for server, no i am lazy, and the client will have these.
    // set tick when this is changed
    public boolean updated;
    // set tick when this is created
    public boolean created;
    public Vec2 pos = new Vec2();
    public Vec2 vel = new Vec2();
    public float angle;
    public float radius;
    public float deceleration = 0.5f;

    public Entity(float x, float y, float r) {
        setPos(x, y);
        this.radius = r;
        this.angle = 0;
        setVel(0, 0);
    }

    public Entity(float x, float y, float r, float angle) {
        this(x, y, r);
        this.angle = angle;
    }

    public void setPos(float x, float y) {
        pos.x = x;
        pos.y = y;
    }

    public void setVel(float dx, float dy) {
        vel.x = dx;
        vel.y = dy;
    }

    public boolean didCollide(Entity other) {
        return (
            Math.pow(other.pos.x - this.pos.x, 2) +
                Math.pow(other.pos.y - this.pos.y, 2) <
            Math.pow(other.radius + this.radius, 2)
        );
    }

    public abstract void draw();

    public void update(float dt) {
        // FOP hell :)
        if (vel.mag() < 0.01) return;
        pos.x += vel.x * dt;
        pos.y += vel.y * dt;
        float newMag = vel.mag() - deceleration * dt;
        vel = vel.normalize().scale(newMag);
        updated = true;
    }
}
