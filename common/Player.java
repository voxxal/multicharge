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
    public Weapon weapon = new Weapon.Ak47();

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
        // Draw specific weapon
        if (weapon.getName().equals("Remington 870")) {
            DrawRectanglePro(
                new Rectangle().x(pos.x).y(pos.y).width(weapon.len).height(16),
                new Vector2().x(0).y(0),
                angle * (180 / (float) Math.PI),
                new Color(140, 136, 126).toRaylib()
            );
        } else if (weapon.getName().equals("Sniper")) {
            DrawRectanglePro(
                new Rectangle().x(pos.x).y(pos.y).width(weapon.len).height(8),
                new Vector2().x(0).y(0),
                angle * (180 / (float) Math.PI),
                new Color(140, 136, 126).toRaylib()
            );
        } else if (weapon.getName().equals("Pistol")) {
            DrawRectanglePro(
                new Rectangle().x(pos.x).y(pos.y).width(weapon.len).height(8),
                new Vector2().x(0).y(0),
                angle * (180 / (float) Math.PI),
                new Color(140, 136, 126).toRaylib()
            );
        } else if (weapon.getName().equals("uuddlrlrba")) {
            // Attempt to add outline to weapon test
            DrawRectanglePro(
                new Rectangle().x(pos.x).y(pos.y).width(weapon.len).height(3),
                new Vector2().x(0).y(0),
                angle * (180 / (float) Math.PI),
                new Color(191, 157, 0).toRaylib()
            );
            DrawRectanglePro(
                new Rectangle().x(pos.x).y(pos.y).width(weapon.len / 2).height(6),
                new Vector2().x(0).y(0),
                angle * (180 / (float) Math.PI),
                new Color(255, 210, 0).toRaylib()
            );
        } else {
            DrawRectanglePro(
                new Rectangle().x(pos.x).y(pos.y).width(40).height(8),
                new Vector2().x(0).y(0),
                angle * (180 / (float) Math.PI),
                new Color(140, 136, 126).toRaylib()
            );
        }
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
