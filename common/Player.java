import static com.raylib.Raylib.*;

import java.util.concurrent.ConcurrentHashMap;

public class Player extends Entity {

    public int playerId = 0;
    public int speed = 100;
    public ConcurrentHashMap<Integer, Boolean> keys = new ConcurrentHashMap<
        Integer,
        Boolean
    >();

    public Player(float x, float y, int playerId) {
        super(x, y, 25);
        vel.x = 100;
        this.playerId = playerId;
    }

    public Player(float x, float y) {
        super(x, y, 25);
        vel.x = 100;
    }

    public void draw() {
        DrawCircleV(
            pos.toRaylib(),
            radius,
            new Color().r((byte) 10).b((byte) 10).g((byte) 10).a((byte) 255)
        );
        DrawCircleV(
            pos.toRaylib(),
            radius - 3,
            new Color().r((byte) 255).b((byte) 145).g((byte) 145).a((byte) 255)
        );
        DrawRectanglePro(
            new Rectangle().x(pos.x).y(pos.y).width(40).height(8),
            new Vector2().x(0).y(0),
            angle,
            new Color().r((byte) 140).b((byte) 136).g((byte) 126).a((byte) 255)
        );
    }

    public String toString() {
        return String.format(
            "Player { player = %d, pos = %s, vel = %s }",
            playerId,
            pos,
            vel
        );
    }

    @Override
    public void update(float dt) {
        if (Math.abs(vel.y) < 500) {
            if (keys.getOrDefault(KEY_W, false)) vel.y -= speed * dt;
            if (keys.getOrDefault(KEY_S, false)) vel.y += speed * dt;
        }
        if (Math.abs(vel.x) < 500) {
            if (keys.getOrDefault(KEY_A, false)) vel.x -= speed * dt;
            if (keys.getOrDefault(KEY_D, false)) vel.x += speed * dt;
        }
        super.update(dt);
    }
}
