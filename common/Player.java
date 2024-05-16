import static com.raylib.Raylib.*;

import java.util.concurrent.ConcurrentHashMap;

public class Player extends Entity {

    public int playerId = 0;
    public int speed = 125;
    public int health = 100;
    public ConcurrentHashMap<Integer, Boolean> keys = new ConcurrentHashMap<
        Integer,
        Boolean
    >();
    public boolean shooting;
    public Weapon weapon = new Weapon.Pistol();

    public Player(float x, float y, int playerId) {
        super(x, y, 25);
        vel.x = 100;
        this.playerId = playerId;
    }

    public Player(float x, float y) {
        super(x, y, 25);
        vel.x = 100;
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
        if (Math.abs(vel.y) < 500) {
            if (keys.getOrDefault(KEY_W, false)) pos.y -= speed * dt;
            if (keys.getOrDefault(KEY_S, false)) pos.y += speed * dt;
        }
        if (Math.abs(vel.x) < 500) {
            if (keys.getOrDefault(KEY_A, false)) pos.x -= speed * dt;
            if (keys.getOrDefault(KEY_D, false)) pos.x += speed * dt;
        }

        if (keys.getOrDefault(KEY_R, false)) weapon.reload();

        if (shooting) shooting = weapon.shoot(world, this);

        weapon.update(dt);
        super.update(world, dt);
    }
}
