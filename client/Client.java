import static com.raylib.Jaylib.BLACK;
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
    public int[] selectedWeapons = { 0, 0 };
    private boolean urlBarSelected = false;
    private String urlBar = "127.0.0.1:4200";
    private String[] weaponList = {
        "Ak-47",
        "Remington 870",
        "Pistol",
        "Sniper",
        "SMG",
        "Flamethrower",
        "RPG",
    };

    public Client() {
        camera = new Camera2D().zoom(1);
        serverHandler = new ServerHandler(this);
        world = new World();
    }

    public static enum Scene {
        START,
        GAME,
    }

    public Scene currentScene = Scene.START;

    public void drawStart(Client client) {
        if (urlBarSelected) {
            int nextChar = 0;
            while ((nextChar = GetCharPressed()) != 0) {
                urlBar += (char) nextChar; // don't enter unicode characters you will cause this to explode :)
            }
            if (IsKeyPressed(KEY_BACKSPACE) && urlBar.length() > 0) {
                urlBar = urlBar.substring(0, urlBar.length() - 1);
            }
        }
        Vec2 center = new Vec2(600, 450);
        BeginDrawing();
        ClearBackground(new Color(0x6e, 0xa0, 0x4d).toRaylib());
        int width = MeasureText(">", 30);
        DrawText("<", 400, 200, 30, RAYWHITE);
        DrawText(">", 800 - width, 200, 30, RAYWHITE);
        String weapon0 = weaponList[selectedWeapons[0]];
        DrawText(
            weapon0,
            600 - MeasureText(weapon0, 30) / 2,
            200,
            30,
            RAYWHITE
        );
        DrawText("<", 400, 300, 30, RAYWHITE);
        DrawText(">", 800 - width, 300, 30, RAYWHITE);
        String weapon1 = weaponList[selectedWeapons[1]];
        DrawText(
            weapon1,
            600 - MeasureText(weapon1, 30) / 2,
            300,
            30,
            RAYWHITE
        );

        DrawRectangle(400, 375, 400, 30, RAYWHITE);
        DrawText(urlBar, 405, 380, 20, BLACK);
        if (urlBarSelected) DrawRectangle(
            400 + MeasureText(urlBar, 20) + 10,
            380,
            2,
            20,
            BLACK
        );
        DrawText("Play", 600 - MeasureText("Play", 40) / 2, 450, 40, VIOLET);
        if (IsMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
            int x = GetMouseX();
            int y = GetMouseY();
            if (x > 400 && x < 400 + width && y > 200 && y < 230) {
                selectedWeapons[0]--;
                if (selectedWeapons[0] == -1) selectedWeapons[0] =
                    weaponList.length - 1;
            }

            if (x > 800 - width && x < 800 && y > 200 && y < 230) {
                selectedWeapons[0] = (selectedWeapons[0] + 1) %
                weaponList.length;
            }

            if (x > 400 && x < 400 + width && y > 300 && y < 330) {
                selectedWeapons[1]--;
                if (selectedWeapons[1] == -1) selectedWeapons[1] =
                    weaponList.length - 1;
            }

            if (x > 800 - width && x < 800 && y > 300 && y < 330) {
                selectedWeapons[1] = (selectedWeapons[1] + 1) %
                weaponList.length;
            }

            if (
                x > 600 - MeasureText("Play", 40) / 2 &&
                x < 600 + MeasureText("Play", 40) / 2 &&
                y > 450 &&
                y < 490
            ) {
                client.serverHandler.start();
                currentScene = Scene.GAME;
            }
            urlBarSelected = x > 400 && x < 800 && y > 375 && y < 375 + 30;
        }
        EndDrawing();
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
                client.drawStart(client);
            } else {
                client.drawGame();
            }
        }
        if (
            client.currentScene == Scene.GAME
        ) client.serverHandler.disconnect();
        CloseWindow();
    }

    private class ServerHandler extends Thread {

        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private int playerId;
        private boolean stopped = false;
        private Client client;

        public ServerHandler(Client client) {
            super();
            this.client = client;
        }

        public void disconnect() throws IOException {
            out.writeObject(new Packet.Disconnect("Goodbye!"));
            stopped = true;
            in.close();
            out.close();
            clientSocket.close();
        }

        public void send(Packet object) {
            try {
                if (out != null) {
                    out.writeUnshared(object);
                    out.reset();
                }
            } catch (Exception e) {
                System.out.println("[CLIENT] failed to send packet " + e);
            }
        }

        public void run() {
            try {
                String[] parts = urlBar.split(":");
                clientSocket = new Socket(parts[0], Integer.parseInt(parts[1]));
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());

                while (!stopped) {
                    Object next = in.readObject();
                    if (next instanceof Packet.Connect) {
                        playerId = ((Packet.Connect) next).playerId;
                        // if (out == null || in == null) System.out.println(
                        //     "screw you java"
                        // );
                        send(
                            new Packet.SelectWeapons(
                                weaponList[selectedWeapons[0]],
                                weaponList[selectedWeapons[1]]
                            )
                        );
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
                            Entity e = world.entities.get(id);
                            if (
                                e instanceof Player &&
                                ((Player) e).playerId == playerId
                            ) {
                                System.out.println("[CLIENT] ded");
                                client.currentScene = Scene.START;
                                client.serverHandler = new ServerHandler(
                                    client
                                );
                                client.player = -1;
                                client.world = new World();
                                disconnect();
                            }
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
