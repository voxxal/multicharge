import static com.raylib.Jaylib.RAYWHITE;
import static com.raylib.Jaylib.VIOLET;
import static com.raylib.Raylib.*;

import java.io.*;
import java.net.*;

public class Client {

    private World world;
    private Camera2D camera;
    private int player = -1;
    public ServerHandler serverHandler;

    public Client() {
        camera = new Camera2D().zoom(1);
        serverHandler = new ServerHandler();
        world = new World();
    }

    public void draw() {
        Vector2 center = new Vector2().x(400).y(300);
        if (player != -1) camera
            .offset(center)
            .target(world.entities.get(player).pos.toRaylib());
        BeginDrawing();
        ClearBackground(RAYWHITE);
        BeginMode2D(camera);
        for (Entity e : world.entities.values()) {
            e.draw();
        }
        DrawText("hii", 0, 0, 20, VIOLET);
        EndMode2D();
        DrawFPS(20, 20);
        EndDrawing();
    }

    public static void main(String args[]) throws Exception {
        Client client = new Client();
        client.serverHandler.start();

        InitWindow(800, 600, "multicharge");
        SetTargetFPS(60);

        while (!WindowShouldClose()) {
            client.draw();
        }
        client.serverHandler.disconnect();
        CloseWindow();
    }

    private class ServerHandler extends Thread {

        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private int playerId;
        private boolean stopped = false;

        public void disconnect() throws IOException {
            out.writeObject(new Disconnect("Goodbye!"));
            stopped = true;
            in.close();
            out.close();
            clientSocket.close();
        }

        public void run() {
            try {
                clientSocket = new Socket("127.0.0.1", 4200);
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());

                while (!stopped) {
                    Object next = in.readObject();
                    if (next instanceof Packet.Connect) {
                        playerId = ((Packet.Connect) next).playerId;
                    } else if (next instanceof Packet.Disconnect) {
                        System.out.println(
                            "[CLIENT] recieved message to disconnect: " +
                            ((Packet.Disconnect) next).message
                        );
                        break;
                    } else if (next instanceof Packet.Update) {
                        Packet.Update update = (Packet.Update) next;
                        // System.out.println("[CLIENT] update: " + update);
                        for (Entity e : update.creations) {
                            world.add(e.id, e);
                            if (
                                e instanceof Player &&
                                ((Player) e).playerId == playerId
                            ) {
                                player = e.id;
                            }
                        }
                        for (Entity e : update.updates) {
                            world.add(e.id, e);
                        }
                    } else {
                        System.out.println(
                            "[CLIENT] unknown packet " + next.toString()
                        );
                    }
                }
                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
