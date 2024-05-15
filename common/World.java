import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    public int nextId = 0;
    public ConcurrentHashMap<Integer, Entity> entities = new ConcurrentHashMap<
        Integer,
        Entity
    >();
    // for the collisions, server side
    public LinkedList<Entity>[][] spatialPartition;

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
        //TODO loop backwards and remove peeps.
        for (Entity e : entities.values()) {
            e.update(this, dt);
        }
    }
}
