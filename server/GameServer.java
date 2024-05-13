import java.io.*;
import java.net.*;

public class GameServer {

    private ServerSocket serverSocket;
    private boolean player0 = false;
    private boolean player1 = false;
    private World world = new World();
    private long prevTick = System.currentTimeMillis();
    private long TICK_DUR = 1000 / 20;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) new ClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private class ClientHandler extends Thread {

        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private int playerId;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());
                if (!player0) {
                    playerId = 0;
                    player0 = true;
                    world.add(new Player(0, 0, 0))
                } else if (!player1) {
                    playerId = 1;
                    player1 = true;
                    world.add(new Player(0, 0, 1))
                } else {
                    out.writeObject(new Disconnect("lobby full"));
                }

                out.writeObject(new Connect(playerId));

                out.writeObject(new Disconnect("Goodbye!"));
                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {}
        }
    }
}
