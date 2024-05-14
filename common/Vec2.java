import static com.raylib.Raylib.Vector2;

import java.io.Serializable;

public class Vec2 implements Serializable {

    public float x;
    public float y;

    public Vec2() {}

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 toRaylib() {
        return new Vector2().x(x).y(y);
    }

    public float mag() {
        return (float) Math.sqrt(
            (float) Math.pow(x, 2) + (float) Math.pow(y, 2)
        );
    }

    public Vec2 normalize() {
        float len = vec.mag();
        x /= len;
        y /= len;
        return new Vec2(x, y);
    }

    public Vec2 scale(float scalar) {
        return new Vec2(x * scalar, y * scalar)
    }

    public String toString() {
        return String.format("<%f, %f>", x, y);
    }
}
