import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {

    public static class Connect extends Packet {

        public int playerId;

        public Connect(int playerId) {
            this.playerId = playerId;
        }
    }

    public static class Disconnect extends Packet {

        public String message;

        public Disconnect(String message) {
            this.message = message;
        }
    }

    public static class Move extends Packet {

        public int target;
        public float x;
        public float y;
        public float angle;
    }

    public static class Input extends Packet {

        public int key;
        public boolean released;

        public Input(int key, boolean released) {
            this.key = key;
            this.released = released;
        }
    }

    public static class Update extends Packet {

        public ArrayList<Entity> creations;
        public ArrayList<Entity> updates;

        public Update() {
            creations = new ArrayList<Entity>();
            updates = new ArrayList<Entity>();
        }

        public Update(World world) {
            creations = new ArrayList<Entity>();
            updates = new ArrayList<Entity>();
            for (Entity entity : world.entities.values()) {
                if (entity.created) {
                    creations.add(entity);
                    entity.created = false;
                }

                if (entity.updated) {
                    updates.add(entity);
                    entity.updated = false;
                }
            }
        }

        public String toString() {
            return String.format(
                "Packet.Update { creations = %s, updates = %s }",
                creations,
                updates
            );
        }
    }
}
