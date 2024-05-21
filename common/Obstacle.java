import static com.raylib.Raylib.*;

public class Obstacle extends Entity {

    public float health;
    public float maxhealth;
    public Color color;
    public float maxRadius;

    public Obstacle(float x, float y, float r) {
        this(x, y, r, 100);
        this.maxRadius = r;
        this.color = new Color(255, 255, 255);
    }

    public Obstacle(float x, float y, float r, float health) {
        super(x, y, r);
        this.maxRadius = r;
        this.health = health;
        this.maxhealth = health;
        this.color = new Color(255, 255, 255);
    }

    public Obstacle(float x, float y, float r, float health, Color color) {
        super(x, y, r);
        this.maxRadius = r;
        this.health = health;
        this.maxhealth = health;
        this.color = color;
    }

    public boolean didCollide(Entity other) {
        return (
            Math.pow(other.pos.x - this.pos.x, 2) +
                Math.pow(other.pos.y - this.pos.y, 2) <
            Math.pow(
                other.radius +
                radius,
                2
            )
        );
    }

    public boolean onCollide(Entity other) {
        if (other instanceof Bullet) {
            health -= ((Bullet) other).damage;
            updateRadius();
            updated = true;
            if (health < 0) return true;
        }
        return false;
    }

    public void updateRadius(){
        this.radius = maxRadius * (float) (0.5 + this.health / (2.0 * this.maxhealth));
    }

    public void draw() {
        updateRadius();
        DrawCircleV(
            pos.toRaylib(),
            radius,
            color.toRaylib()
        );
    }

    public static class Rock extends Obstacle {

        public Rock(float x, float y) {
            super(
                x,
                y,
                50,
                ((int) (Math.random() * 4) + 7) *
                ((int) (Math.random() * 4) + 7),
                new Color(
                    0x77 + (int) (Math.random() * 20),
                    0x77 + (int) (Math.random() * 20),
                    0x77 + (int) (Math.random() * 20)
                )
            );
            super.maxhealth = 100;
            updateRadius();
        }
    }

    public static class Tree extends Obstacle {

        public Color leaves;
        public int leavesScale;

        public Tree(float x, float y) {
            super(
                x,
                y,
                30,
                10000,
                new Color(
                    151 + (int) (Math.random() * 20),
                    93 + (int) (Math.random() * 20),
                    57 + (int) (Math.random() * 20)
                )
            );
            leaves = new Color(
                50 + (int) (Math.random() * 60),
                143 + (int) (Math.random() * 60),
                64 + (int) (Math.random() * 60),
                (int) (Math.random() * 128) + 63
            );
            leavesScale = 3;
        }

        public void draw() {
            DrawCircleV(
                pos.toRaylib(),
                radius * leavesScale,
                leaves.toRaylib()
            );
            DrawCircleV(
                pos.toRaylib(),
                radius,
                color.toRaylib()
            );
        }
    }

    public static class Lake extends Obstacle {

        public Lake(float x, float y){
            super(x, y, 500, 10000000, new Color((int)(Math.random() * 80), 100 + (int)(Math.random() * 156), 150));
            collideable = false;
        }

        public boolean onCollide(Entity other){
            return false;
        }
    }

    public String toString() {
        return String.format(
            "%2f, %2f, %s, %2f",
            health,
            maxhealth,
            color,
            radius
        );
    }
}
