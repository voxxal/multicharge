import static com.raylib.Raylib.*;

import java.util.concurrent.ConcurrentHashMap;

public class Player extends Entity {

    public int playerId = 0;
    public int speed = 200;
    public float decel = 1.3f;
    public int health = 75;
    public ConcurrentHashMap<Integer, Boolean> keys = new ConcurrentHashMap<
        Integer,
        Boolean
    >();
    public boolean shooting;
    public Weapon weapon1 = new Weapon.Flamethrower();
    public Weapon weapon2 = new Weapon.Debugger();
    public boolean weaponNum = true;

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
            radius + 2,
            new Color(189, 145, 89).toRaylib()
        );
        DrawCircleV(
            pos.toRaylib(),
            radius,
            new Color(253, 193, 119).toRaylib()
        );
        if (weaponNum) {
            DrawRectanglePro(
                new Rectangle()
                    .x(pos.x)
                    .y(pos.y)
                    .width(weapon1.len1)
                    .height(weapon1.wid1),
                new Vector2().x(0).y(0),
                angle * (180 / (float) Math.PI),
                weapon1.c1.toRaylib()
            );
            DrawRectanglePro(
                new Rectangle()
                    .x(pos.x)
                    .y(pos.y)
                    .width(weapon1.len2)
                    .height(weapon1.wid2),
                new Vector2().x(0).y(0),
                angle * (180 / (float) Math.PI),
                weapon1.c2.toRaylib()
            );
        }
        if (!weaponNum) {
            DrawRectanglePro(
                new Rectangle()
                    .x(pos.x)
                    .y(pos.y)
                    .width(weapon2.len1)
                    .height(weapon2.wid1),
                new Vector2().x(0).y(0),
                angle * (180 / (float) Math.PI),
                weapon2.c1.toRaylib()
            );
            DrawRectanglePro(
                new Rectangle()
                    .x(pos.x)
                    .y(pos.y)
                    .width(weapon2.len2)
                    .height(weapon2.wid2),
                new Vector2().x(0).y(0),
                angle * (180 / (float) Math.PI),
                weapon2.c2.toRaylib()
            );
        }
    }

    public boolean onCollide(Entity other) {
        if (other instanceof Bullet) {
            health -= ((Bullet) other).damage;
            if (health < 0) return true;
        }
        return false;
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
        if (keys.getOrDefault(KEY_Q, false)) {
            weaponNum = !weaponNum;
            keys.put(KEY_Q, false);
        }
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

        if (weaponNum) {
            if (keys.getOrDefault(KEY_R, false)) weapon1.reload();
            if (shooting) shooting = weapon1.shoot(world, this);
            weapon1.update(dt);
        }
        if (!weaponNum) {
            if (keys.getOrDefault(KEY_R, false)) weapon2.reload();
            if (shooting) shooting = weapon2.shoot(world, this);
            weapon2.update(dt);
        }

        super.update(world, dt);
    }
}
