public class Entity implements Collideable<Entity>{
    public float x;
    public float y;
    public float angle;
    public float dx;
    public float dy;
    public float r;

    public Entity(float x, float y, float r){
        setPos(x, y);
        this.r = r;
        this.angle = 0;
        setVel(0, 0);
    }
    
    public Entity(float x, float y, float r, float angle){
        setPos(x, y);
        this.r = r;
        this.angle = angle;
        setVel(0, 0);
    }

    public void setPos(float x, float y){
        this.x = x;
        this.y = y;
    }
    
    public void setVel(float dx, float dy){
        this.dx = dx;
        this.dy = dy;
    }

    public void didCollide(Entity other){
        return (other.x - this.x)**2 + (other.y - this.y)**2 < (other.r + this.r)**2;
    }

}