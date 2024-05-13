import static com.raylib.Jaylib.RAYWHITE;
import static com.raylib.Jaylib.VIOLET;
import static com.raylib.Raylib.*;

public class Client {

    public static void main(String args[]) throws Exception {
        World world = new World();
        GameClient gameClient = new GameClient();
        gameClient.startConnection("127.0.0.1", 4200);
        String response = gameClient.sendMessage("hello server");

        InitWindow(800, 450, "Demo");
        SetTargetFPS(60);
        Camera3D camera = new Camera3D()
            ._position(new Vector3().x(18).y(16).z(18))
            .target(new Vector3())
            .up(new Vector3().x(0).y(1).z(0))
            .fovy(45)
            .projection(CAMERA_PERSPECTIVE);

        while (!WindowShouldClose()) {
            UpdateCamera(camera, CAMERA_ORBITAL);
            BeginDrawing();
            ClearBackground(RAYWHITE);
            BeginMode3D(camera);
            DrawGrid(20, 1.0f);
            EndMode3D();
            DrawText(response, 190, 200, 20, VIOLET);
            DrawFPS(20, 20);
            EndDrawing();
        }
        CloseWindow();
    }
}
