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
        for (int i = 0; i < 50; i++) {
            spatialPartition.add(new ArrayList<LinkedList<Entity>>());
            for (int j = 0; j < 50; j++) {
                spatialPartition.get(i).add(new LinkedList<Entity>());
            }
        }
        for (Entity e : entities.values()) {
            e.update(this, dt);
            // generate spatial partition :D
            int cx = (int) (e.pos.x / 100);
            int cy = (int) (e.pos.y / 100);
            int range = (int) ((e.radius + 50) / 100);
            for (int y = cy - range; y <= cy + range; y++) {
                for (int x = cx - range; x <= cx + range; x++) {
                    if (x > 0 && y > 0 && x < 50 && y < 50) spatialPartition
                        .get(y)
                        .get(x)
                        .add(e);
                }
            }
        }

        for (ArrayList<LinkedList<Entity>> row : spatialPartition) {
            for (LinkedList<Entity> chunk : row) {
                for (Entity entity : chunk) {
                    for (Entity other : chunk) {
                        if (entity.didCollide(other)) {
                            if (entity.onCollide(other)) remove(entity);
                        }
                    }
                }
            }
        }
    }
}
