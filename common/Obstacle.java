import static com.raylib.Raylib.*;
import java.io.*;
import java.util.*;

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
            Math.pow(other.radius + radius, 2)
        );
    }

    public boolean onCollide(Entity other) {
        if (health < 0){
            return true;
        }
        else if (other instanceof Bullet) {
            health -= ((Bullet) other).damage;
            updateRadius();
            updated = true;
            if (health < 0){
                incrementCounter();
                return true;
            }
        }
        return false;
    }

    public void incrementCounter(){
        try{
            File stats = new File("../stats.txt");
            int count;
            if(!stats.isFile()){
                stats.createNewFile();
                count = 0;
            }
            else{
                Scanner reader = new Scanner(stats);
                String[] line = reader.nextLine().split(" ");
                reader.close();
                count = Integer.parseInt(line[line.length - 1]);
            }
            FileWriter statsWriter = new FileWriter("../stats.txt");
            statsWriter.write(String.format("Obstacles killed: %d\n", count + 1));
            statsWriter.close();
        } catch (IOException e){
            System.out.println("Error writing to file: ../stats.txt");
            e.printStackTrace();
        }
    }

    public void updateRadius() {
        this.radius = maxRadius *
        (float) (0.5 + this.health / (2.0 * this.maxhealth));
    }

    public void draw() {
        updateRadius();
        DrawCircleV(pos.toRaylib(), radius + 2, color.scale(0.75).toRaylib());
        DrawCircleV(pos.toRaylib(), radius, color.toRaylib());
    }

    public static class Rock extends Obstacle {

        public Rock(float x, float y) {
            super(
                x,
                y,
                50,
                10 *
                ((int) (Math.random() * 4) + 7) *
                ((int) (Math.random() * 4) + 7),
                new Color(
                    0x77 + (int) (Math.random() * 20),
                    0x77 + (int) (Math.random() * 20),
                    0x77 + (int) (Math.random() * 20)
                )
            );
            super.maxhealth = 1000;
            updateRadius();
        }
    }

    public static class Tree extends Obstacle {

        public Color leaves;
        public float leavesScale;

        public Tree(float x, float y) {
            super(
                x,
                y,
                30,
                10_000,
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
            leavesScale = (float) Math.random() + 2;
        }

        public void draw() {
            DrawCircleV(
                pos.toRaylib(),
                radius * leavesScale,
                leaves.toRaylib()
            );
            super.draw();
        }
    }

    public static class Lake extends Obstacle {

        public Lake(float x, float y) {
            super(
                x,
                y,
                500,
                1_000_000_000,
                new Color(
                    (int) (Math.random() * 20),
                    (int) (Math.random() * 100),
                    200 + (int) (Math.random() * 40)
                )
            );
            collideable = false;
        }
    }

    public static class Cactus extends Obstacle {

        public Color flower;
        public float flowerScale;

        public Cactus(float x, float y) {
            super(
                x,
                y,
                50,
                10 *
                ((int) (Math.random() * 4) + 7) *
                ((int) (Math.random() * 4) + 7),
                new Color(
                    0x5d + (int) (Math.random() * 20),
                    0xbf + (int) (Math.random() * 20),
                    0x55 + (int) (Math.random() * 20)
                )
            );
            flower = new Color(
                0xd3 + (int) (Math.random() * 20),
                0x5f + (int) (Math.random() * 20),
                0xd4 + (int) (Math.random() * 20)
            );
            super.maxhealth = 1000;
            flowerScale = (float) 0.333;
            updateRadius();
        }

        public void draw() {
            super.draw();
            DrawCircleV(
                pos.toRaylib(),
                radius * flowerScale + 2,
                flower.scale(0.75).toRaylib()
            );
            DrawCircleV(
                pos.toRaylib(),
                radius * flowerScale,
                flower.toRaylib()
            );
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
