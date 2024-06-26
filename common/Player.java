import static com.raylib.Raylib.*;

import java.util.concurrent.ConcurrentHashMap;

public class Player extends Entity {

    public int playerId = 0;
    public int maxSpeed = 200;
    public int speed = 200;
    public int lakeSlow;
    public float decel = 1.3f;
    public int health = 100;
    public ConcurrentHashMap<Integer, Boolean> keys = new ConcurrentHashMap<
        Integer,
        Boolean
    >();
    public boolean shooting;
    public Weapon weapon1 = new Weapon.Empty();
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
            if (!weapon1.name.equals("hand")) {
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
        }
        if (!weaponNum) {
            if (!weapon2.name.equals("hand")) {
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
    }

    public boolean onCollide(Entity other) {
        if (other instanceof Bullet) {
            updated = true;
            health -= ((Bullet) other).damage;
        } else if (other instanceof Obstacle.Lake) {
            updated = true;
            lakeSlow = 50;
        } else {
            Vec2 angle = pos
                .sub(other.pos)
                .normalize()
                .scale(other.radius + radius);
            pos = other.pos.add(angle);
        }
        if (other instanceof Obstacle.Cactus) {
            updated = true;
            health -= 0.05;
        }

        if (health < 0) return true;
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
        // Switch weapon
        if (keys.getOrDefault(KEY_Q, false)) {
            weaponNum = !weaponNum;
            keys.put(KEY_Q, false);
        }
        // Update correct weapon
        // WeaponNum true = weapon 1
        if (weaponNum) {
            if (keys.getOrDefault(KEY_R, false)) weapon1.reload();
            if (shooting) shooting = weapon1.shoot(world, this);
            speed = maxSpeed - weapon1.weight - lakeSlow;
            weapon1.update(dt);
        }
        // WeaponNum true = weapon 2
        if (!weaponNum) {
            if (keys.getOrDefault(KEY_R, false)) weapon2.reload();
            if (shooting) shooting = weapon2.shoot(world, this);
            speed = maxSpeed - weapon2.weight - lakeSlow;
            weapon2.update(dt);
        }
        // Movement
        if (keys.getOrDefault(KEY_W, false)) vel.y = -speed;
        else if (vel.y < 0.01) vel.y /= decel;
        if (keys.getOrDefault(KEY_S, false)) vel.y = speed;
        else if (vel.y > 0.01) vel.y /= decel;
        if (keys.getOrDefault(KEY_A, false)) vel.x = -speed;
        else if (vel.x < 0.01) vel.x /= decel;
        if (keys.getOrDefault(KEY_D, false)) vel.x = speed;
        else if (vel.x > 0.01) vel.x /= decel;
        lakeSlow = 0;
        if (Math.abs(vel.y) < 0.01) vel.y = 0;
        if (Math.abs(vel.x) < 0.01) vel.x = 0;

        super.update(world, dt);
        updated = true;
    }
}
