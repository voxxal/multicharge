import static com.raylib.Jaylib.RAYWHITE;
import static com.raylib.Jaylib.VIOLET;
import static com.raylib.Raylib.*;

import java.io.*;
import java.net.*;

public class Client {

    private World world;
    private Camera2D camera;
    private int player = -1;
    private float recentDamage = 0;
    public ServerHandler serverHandler;
    public Vec2 mouse = new Vec2();

    public Client() {
        camera = new Camera2D().zoom(1);
        serverHandler = new ServerHandler();
        world = new World();
    }

    public static enum Scene {
        START,
        GAME,
    }

    public Scene currentScene = Scene.START;

    public void drawStart() {
        Vec2 center = new Vec2(600, 450);
        BeginDrawing();
        ClearBackground(new Color(0x6e, 0xa0, 0x4d).toRaylib());
        DrawText("Play", 600, 450, 40, VIOLET);
        EndDrawing();
        // check for the button being pressed then...
        // client.serverHandler.start();
        // currentScene = Scene.GAME;
    }

    public void drawGame() {
        Vector2 center = new Vector2().x(600).y(450);
        Player playerObj = (Player) world.entities.get(player);

        int[] watchedKeys = { KEY_W, KEY_A, KEY_S, KEY_D, KEY_R, KEY_Q };
        for (int key : watchedKeys) {
            if (IsKeyReleased(key)) {
                serverHandler.send(new Packet.Input(key, true));
            } else if (IsKeyPressed(key)) {
                serverHandler.send(new Packet.Input(key, false));
            }
        }

        if (IsMouseButtonReleased(MOUSE_BUTTON_LEFT)) {
            serverHandler.send(new Packet.Mouse(MOUSE_BUTTON_LEFT, true));
        } else if (IsMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
            serverHandler.send(new Packet.Mouse(MOUSE_BUTTON_LEFT, false));
        }
        // wow nice hashing function
        float diff = mouse.x * 5449 + mouse.y * 3109;
        mouse.x = GetMouseX();
        mouse.y = GetMouseY();
        if (diff != mouse.x * 5449 + mouse.y * 3109) {
            serverHandler.send(
                new Packet.Turn(
                    (float) Math.atan2(
                        mouse.y - center.y(),
                        mouse.x - center.x()
                    )
                )
            );
        }

        if (playerObj != null) camera
            .offset(center)
            .target(playerObj.pos.toRaylib());
        BeginDrawing();
        ClearBackground(new Color(0x6e, 0xa0, 0x4d).toRaylib());
        BeginMode2D(camera);
        for (int x = 0; x < 25; x++) {
            DrawLine(
                x * 100,
                0,
                x * 100,
                2500,
                new Color(255, 255, 255, 60).toRaylib()
            );
        }

        for (int y = 0; y < 25; y++) {
            DrawLine(
                0,
                y * 100,
                2500,
                y * 100,
                new Color(255, 255, 255, 60).toRaylib()
            );
        }
        for (Entity e : world.entities.values()) {
            e.draw();
        }
        EndMode2D();
        if (playerObj != null) {
            if (playerObj.weaponNum) DrawText(
                playerObj.weapon1.toString(),
                20,
                900 - 40,
                20,
                RAYWHITE
            );
            if (!playerObj.weaponNum) DrawText(
                playerObj.weapon2.toString(),
                20,
                900 - 40,
                20,
                RAYWHITE
            );
            DrawRectangle(
                300,
                900 - 50,
                600,
                40,
                new Color(80, 80, 80, 80).toRaylib()
            );
            float percentLeft = playerObj.health / 100f;
            DrawRectangle(
                305,
                900 - 45,
                (int) (590f * percentLeft),
                30,
                new Color(255, 255, 255, 80).toRaylib()
            );
        }
        DrawFPS(20, 20);
        EndDrawing();
    }

    public static void main(String args[]) throws Exception {
        Client client = new Client();
        // client.serverHandler.start();

        InitWindow(1200, 900, "multicharge");
        SetTargetFPS(60);

        while (!WindowShouldClose()) {
            if (client.currentScene == Scene.START) {
                client.drawStart();
            } else {
                client.drawGame();
            }
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
            out.writeObject(new Packet.Disconnect("Goodbye!"));
            stopped = true;
            in.close();
            out.close();
            clientSocket.close();
        }

        public void send(Packet object) {
            try {
                out.writeUnshared(object);
                out.reset();
            } catch (Exception e) {
                System.out.println("[CLIENT] failed to send packet " + e);
            }
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
                        // yes yes another heap allocation is happening per updated entity
                        // do i care? no. computers are fast anyways ;)
                        for (Entity e : update.updates) {
                            world.add(e.id, e);
                        }

                        for (int id : update.removals) {
                            world.delete(id);
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
