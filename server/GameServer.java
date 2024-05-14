import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameServer {

    private ServerSocket serverSocket;
    private boolean running;
    private boolean player0 = false;
    private boolean player1 = false;
    private World world = new World();
    private GameLoop loop;
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<
        ClientHandler
    >();

    public void start(int port) throws IOException {
        running = true;
        serverSocket = new ServerSocket(port);
        loop = new GameLoop();
        loop.start();
        while (running) new ClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private class GameLoop extends Thread {

        private long TICK_DUR = 1000 / 30;
        private long prevTick = System.currentTimeMillis();

        public void run() {
            while (running) {
                long dt = System.currentTimeMillis() - prevTick;
                if (dt < TICK_DUR) continue;
                world.update(((float) dt) / 1000);
                prevTick += TICK_DUR;
                for (ClientHandler handler : clientHandlers) {
                    handler.updateClient();
                }
            }
        }
    }

    private class ClientHandler extends Thread {

        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private int playerId;
        private Player player;
        private AtomicBoolean tick = new AtomicBoolean(false);

        public ClientHandler(Socket socket) {
            System.out.println("[SERVER] connection from socket");
            clientSocket = socket;
            clientHandlers.add(this);
        }

        public void updateClient() {
            tick.set(true);
        }

        public void run() {
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());
                if (!player0) {
                    playerId = 0;
                    player0 = true;
                    player = new Player(0, 0, 0);
                    world.add(player);
                } else if (!player1) {
                    playerId = 1;
                    player1 = true;
                    player = new Player(0, 0, 1);
                    world.add(player);
                } else {
                    out.writeObject(new Packet.Disconnect("lobby full"));
                }

                (new Inbound()).start();

                out.writeObject(new Packet.Connect(playerId));

                Packet.Update initPacket = new Packet.Update();
                initPacket.creations.addAll(world.entities.values());
                out.writeObject(initPacket);

                while (running) {
                    if (tick.getAndSet(false)) {
                        out.writeUnshared(new Packet.Update(world));
                        out.reset();
                    }
                }

                out.writeObject(new Packet.Disconnect("Goodbye!"));
                if (playerId == 0) player0 = false;
                else player1 = false;
                in.close();
                out.close();
                clientSocket.close();
                clientHandlers.remove(this);
            } catch (Exception e) {
                System.out.println("[SERVER] exception: " + e);
                clientHandlers.remove(this);
            }
        }

        private class Inbound extends Thread {

            public void run() {
                try {
                    while (running) {
                        Object next = in.readObject();
                        if (next instanceof Packet.Input) {
                            Packet.Input input = (Packet.Input) next;
                            player.keys.put(input.key, !input.released);
                        } else if (next instanceof Packet.Turn) {
                            player.angle = (((Packet.Turn) next).angle *
                                (180 / (float) Math.PI));
                        } else {
                            System.out.println(
                                "[SERVER] unknown packet " + next.toString()
                            );
                        }
                    }
                } catch (Exception e) {
                    System.out.println("[SERVER] exception: " + e);
                }
            }
        }
    }
}
