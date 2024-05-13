import static com.raylib.Jaylib.RAYWHITE;
import static com.raylib.Jaylib.VIOLET;
import static com.raylib.Raylib.*;

public class Client {

    public static void main(String args[]) throws Exception {
        World world = new World();
        Player player = new Player(0, 0);
        GameClient gameClient = new GameClient();
        gameClient.startConnection("127.0.0.1", 4200);
        String response = gameClient.sendMessage("hello server");

        InitWindow(800, 600, "Demo");
        SetTargetFPS(60);
        Vector2 center = new Vector2().x(400).y(300);
        Camera2D camera = new Camera2D()
            .offset(center)
            .target(player.pos)
            .zoom(1);
        while (!WindowShouldClose()) {
            player.update();

            camera.offset(center).target(player.pos);

            BeginDrawing();
            ClearBackground(RAYWHITE);
            BeginMode2D(camera);
            DrawText(response, 0, 0, 20, VIOLET);
            player.draw();
            EndMode2D();
            DrawFPS(20, 20);
            EndDrawing();
        }
        CloseWindow();
    }
}
