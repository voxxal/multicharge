import static com.raylib.Jaylib.RAYWHITE;
import static com.raylib.Jaylib.VIOLET;
import static com.raylib.Raylib.*;

import java.io.*;
import java.net.*;

public class Client {

    private World world;
    private Camera2D camera;
    public ServerHandler serverHandler;

    public Client() {
        Camera2D camera = new Camera2D().zoom(1);
        world = new World();
    }

    public void draw() {
        // camera.offset(center).target(player.pos);
        BeginDrawing();
        ClearBackground(RAYWHITE);
        // BeginMode2D(camera);
        DrawText("hii", 0, 0, 20, VIOLET);
        EndMode2D();
        DrawFPS(20, 20);
        EndDrawing();
    }

    public static void main(String args[]) throws Exception {
        Vector2 center = new Vector2().x(400).y(300);

        Client client = new Client();
        client.serverHandler = new ServerHandler();
        client.serverHandler.start();

        InitWindow(800, 600, "multicharge");
        SetTargetFPS(60);

        while (!WindowShouldClose()) {
            client.draw();
        }
        client.serverHandler.disconnect();
        CloseWindow();
    }

    private static class ServerHandler extends Thread {

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
                    if (next instanceof Connect) {
                        playerId = ((Connect) next).playerId;
                    } else if (next instanceof Disconnect) {
                        System.out.println(
                            "[CLIENT] recieved message to disconnect: " +
                            ((Disconnect) next).message
                        );
                    } else {
                        System.out.println(
                            "[CLIENT] unknown packet " + next.toString()
                        );
                    }
                }
                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {}
        }
    }
}
