import static com.raylib.Raylib.*;

public abstract class Entity implements Collideable<Entity> {

    public Vector2 pos = new Vector2();
    public Vector2 vel = new Vector2();
    public float angle;
    public float radius;

    public Entity(float x, float y, float r) {
        setPos(x, y);
        this.radius = r;
        this.angle = 0;
        setVel(0, 0);
    }

    public Entity(float x, float y, float r, float angle) {
        setPos(x, y);
        this.radius = r;
        this.angle = angle;
        setVel(0, 0);
    }

    public void setPos(float x, float y) {
        pos.x(x).y(y);
    }

    public void setVel(float dx, float dy) {
        vel.x(dx).y(dy);
    }

    public boolean didCollide(Entity other) {
        return (
            Math.pow(other.pos.x() - this.pos.x(), 2) +
                Math.pow(other.pos.y() - this.pos.y(), 2) <
            Math.pow(other.radius + this.radius, 2)
        );
    }

    public abstract void draw();

    public void update() {
        pos.x(pos.x() + vel.x()).y(pos.y() + vel.y());
    }
}
