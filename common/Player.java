import static com.raylib.Raylib.*;

import java.util.concurrent.ConcurrentHashMap;

public class Player extends Entity {

    public int playerId = 0;
    public int speed = 200;
    public float decel = 1.3f;
    public int health = 100;
    public ConcurrentHashMap<Integer, Boolean> keys = new ConcurrentHashMap<
        Integer,
        Boolean
    >();
    public boolean shooting;
    public Weapon weapon = new Weapon.Sniper();

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
            new Color(0xfd, 0xc1, 0x77).toRaylib()
        );
        DrawRectanglePro(
            new Rectangle().x(pos.x).y(pos.y).width(40).height(8),
            new Vector2().x(0).y(0),
            angle * (180 / (float) Math.PI),
            new Color(140, 136, 126).toRaylib()
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
        if (keys.getOrDefault(KEY_W, false)) vel.y = -speed;
        else if (vel.y < 0.01) vel.y /= decel;
        if (keys.getOrDefault(KEY_S, false)) vel.y = speed;
        else if (vel.y > 0.01) vel.y /= decel;
        if (keys.getOrDefault(KEY_A, false)) vel.x = -speed;
        else if (vel.x < 0.01) vel.x /= decel;
        if (keys.getOrDefault(KEY_D, false)) vel.x = speed;
        else if (vel.x > 0.01) vel.x /= decel;

        if (Math.abs(vel.y) < 0.01) vel.y = 0;
        if (Math.abs(vel.x) < 0.01) vel.x = 0;

        if (keys.getOrDefault(KEY_R, false)) weapon.reload();

        if (shooting) shooting = weapon.shoot(world, this);

        weapon.update(dt);
        super.update(world, dt);
    }
}
