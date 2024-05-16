import static com.raylib.Raylib.*;

import java.util.concurrent.ConcurrentHashMap;

public class Player extends Entity {

    public int playerId = 0;
    public int accel = 200;
    public float decel = 1.1f;
    public int health = 100;
    public ConcurrentHashMap<Integer, Boolean> keys = new ConcurrentHashMap<
        Integer,
        Boolean
    >();
    public boolean shooting;
    public Weapon weapon = new Weapon.Pistol();

    public Player(float x, float y, int playerId) {
        super(x, y, 25);
        vel.x = 0;
        this.playerId = playerId;
    }

    public Player(float x, float y) {
        super(x, y, 25);
        vel.x = 0;
    }

    public synchronized void draw() {
        DrawCircleV(
            pos.toRaylib(),
            radius,
            new Color()
                .r((byte) 0xfd)
                .g((byte) 0xc1)
                .b((byte) 0x77)
                .a((byte) 255)
        );
        DrawRectanglePro(
            new Rectangle().x(pos.x).y(pos.y).width(40).height(8),
            new Vector2().x(0).y(0),
            angle * (180 / (float) Math.PI),
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
    public void update(World world, float dt) {
        // vel = new Vec2();
        if (keys.getOrDefault(KEY_W, false) && vel.y <= 300) vel.y -= accel * dt;
        else if (vel.y < 0.01) vel.y /= decel;
        if (keys.getOrDefault(KEY_S, false) && vel.y >= -300) vel.y += accel * dt;
        else if (vel.y > 0.01) vel.y /= decel;
        if (keys.getOrDefault(KEY_A, false) && vel.x <= 300) vel.x -= accel * dt;
        else if (vel.x < 0.01) vel.x /= decel;
        if (keys.getOrDefault(KEY_D, false) && vel.x >= -300) vel.x += accel * dt;
        else if (vel.x > 0.01) vel.x /= decel;

        if (Math.abs(vel.y) < 0.01) vel.y = 0;
        if (Math.abs(vel.x) < 0.01) vel.x = 0;

        if (keys.getOrDefault(KEY_R, false)) weapon.reload();

        if (shooting) shooting = weapon.shoot(world, this);

        weapon.update(dt);
        super.update(world, dt);
    }
}
