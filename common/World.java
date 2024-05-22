import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    public int nextId = 0;
    public ConcurrentHashMap<Integer, Entity> entities = new ConcurrentHashMap<
        Integer,
        Entity
    >();
    // for the collisions, server side
    public ArrayList<ArrayList<LinkedList<Entity>>> spatialPartition;

    public int add(Entity entity) {
        entities.put(++nextId, entity);
        entity.id = nextId;
        entity.created = true;
        return nextId;
    }

    public int add(int id, Entity entity) {
        entities.put(id, entity);
        entity.created = true;
        return id;
    }

    public Entity remove(Entity entity) {
        return remove(entity.id);
    }

    public Entity remove(int id) {
        return entities.put(id, new Tombstone(id));
    }

    public Entity delete(int id) {
        return entities.remove(id);
    }

    public void update(float dt) {
        spatialPartition = new ArrayList<ArrayList<LinkedList<Entity>>>();
        for (int i = 0; i < 25; i++) {
            spatialPartition.add(new ArrayList<LinkedList<Entity>>());
            for (int j = 0; j < 25; j++) {
                spatialPartition.get(i).add(new LinkedList<Entity>());
            }
        }
        for (Entity e : entities.values()) {
            e.update(this, dt);
            // generate spatial partition :D
            int cx = (int) (e.pos.x / 100);
            int cy = (int) (e.pos.y / 100);
            int rangex =
                (int) ((e.pos.x + e.radius) / 100) -
                (int) ((e.pos.x - e.radius) / 100) +
                1;
            int rangey =
                (int) ((e.pos.y + e.radius) / 100) -
                (int) ((e.pos.y - e.radius) / 100) +
                1;
            for (int y = cy - rangey; y <= cy + rangey; y++) {
                for (int x = cx - rangex; x <= cx + rangex; x++) {
                    if (x > 0 && y > 0 && x < 25 && y < 25) spatialPartition
                        .get(y)
                        .get(x)
                        .add(e);
                }
            }
        }

        for (ArrayList<LinkedList<Entity>> row : spatialPartition) {
            for (LinkedList<Entity> chunk : row) {
                for (int i = 0; i < chunk.size(); i++) {
                    Entity entity = chunk.get(i);
                    for (int j = 0; j <= i; j++) {
                        Entity other = chunk.get(j);
                        if (entity.didCollide(other)) {
                            if (entity.onCollide(other)) remove(entity);
                            if (other.onCollide(entity)) remove(other);
                        }
                    }
                }
                // for (Entity entity : chunk) {
                //     for (Entity other : chunk) {
                //         if (entity.didCollide(other)) {
                //             if (entity.onCollide(other)) remove(entity);
                //             if (other.onCollide(entity)) remove(other);
                //         }
                //     }
                // }
            }
        }
    }
}
