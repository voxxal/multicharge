import static com.raylib.Raylib.*;

public class Obstacle extends Entity{
    public float health;
    public float maxhealth;
    public Color color;
    public Obstacle(float x, float y, float r){
        this(x, y, r, 100);
        this.color = new Color(255, 255, 255);
    }

    public Obstacle(float x, float y, float r, float health){
        super(x, y, r);
        this.health = health;
        this.maxhealth = health;
        this.color = new Color(255, 255, 255);
    }

    public Obstacle(float x, float y, float r, float health, Color color){
        super(x, y, r);
        this.health = health;
        this.maxhealth = health;
        this.color = color;
    }

    public boolean didCollide(Entity other) {
        return (
            Math.pow(other.pos.x - this.pos.x, 2) +
                Math.pow(other.pos.y - this.pos.y, 2) <
            Math.pow(other.radius + radius * (0.5 + this.health/(2*this.maxhealth)), 2)
        );
    }

    public void draw() {
        DrawCircleV(
            pos.toRaylib(),
            radius,
            color.toRaylib()
        );
    }

    public static class Rock extends Obstacle {
        public Rock(float x, float y){
            super(x, y, 50, ((int)(Math.random()*4)+7)*((int)(Math.random()*4)+7), new Color(0x67 + (int)(Math.random() * 50), 0x77 + (int)(Math.random() * 50), 0x67 + (int)(Math.random() * 50)));
            super.maxhealth = 100;
        }
    }

    public static class Tree extends Obstacle {
        public Color leaves;
        public Tree(float x, float y){
            super(x, y, 30, 10000, new Color(151 + (int)(Math.random() * 20), 93 + (int)(Math.random() * 20), 57 + (int)(Math.random() * 20)));
            leaves = new Color(50 + (int)Math.random() * 30, 143 + (int)Math.random() * 30, 64 + (int)Math.random() * 30, (int)(Math.random() * 256));
        }

        public void draw() {
            DrawCircleV(
                pos.toRaylib(),
                radius * 2,
                leaves.toRaylib()
            );
            DrawCircleV(
                pos.toRaylib(),
                radius,
                color.toRaylib()
            );
        }
    }

    public String toString(){
        return String.format("%2f, %2f, %2f, %2f", health, maxhealth, color, radius);
    }
}