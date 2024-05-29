import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

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
        world.add(new Obstacle.Tree(1200, 2000));
        world.add(new Obstacle.Tree(600, 400));
        world.add(new Obstacle.Tree(400, 800));
        world.add(new Obstacle.Tree(100, 1200));
        world.add(new Obstacle.Rock(2000, 800));
        world.add(new Obstacle.Rock(1200, 1400));
        world.add(new Obstacle.Rock(1800, 2800));
        world.add(new Obstacle.Rock(400, 600));
        world.add(new Obstacle.Lake(1500, 2000));
        world.add(new Obstacle.Cactus(200, 200));
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
                Packet.Update update = new Packet.Update(world);
                for (ClientHandler handler : clientHandlers) {
                    handler.updateClient(update);
                }
                if(Math.random() < 0.003){
                    world.add(new Obstacle.Cactus((float)Math.random()*5000, (float)Math.random()*5000));
                }
                else if(Math.random() < 0.003){
                    world.add(new Obstacle.Tree((float)Math.random()*5000, (float)Math.random()*5000));
                }
                else if(Math.random() < 0.003){
                    world.add(new Obstacle.Rock((float)Math.random()*5000, (float)Math.random()*5000));
                }
                else if(Math.random() < 0.000003){
                    world.add(new Obstacle.Lake((float)Math.random()*5000, (float)Math.random()*5000));
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
        private AtomicReference<Packet.Update> tick = new AtomicReference<
            Packet.Update
        >(null);

        public ClientHandler(Socket socket) {
            System.out.println("[SERVER] connection from socket");
            clientSocket = socket;
            clientHandlers.add(this);
        }

        public void updateClient(Packet.Update update) {
            tick.set(update);
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
                    Packet.Update update = tick.get();
                    if (update != null) {
                        out.writeUnshared(update);
                        out.reset();
                        tick.set(null);
                    }
                }

                out.writeObject(new Packet.Disconnect("Goodbye!"));
                if (playerId == 0) player0 = false;
                else player1 = false;
                in.close();
                out.close();
                clientSocket.close();
                clientHandlers.remove(this);
                world.remove(player);
            } catch (Exception e) {
                System.out.println("[SERVER] exception: " + e);
                clientHandlers.remove(this);
                if (playerId == 0) player0 = false;
                else player1 = false;
                world.remove(player);
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
                            player.updated = true;
                        } else if (next instanceof Packet.Turn) {
                            player.angle = ((Packet.Turn) next).angle;
                            player.updated = true;
                        } else if (next instanceof Packet.Mouse) {
                            player.shooting = !((Packet.Mouse) next).released;
                            player.updated = true;
                        } else if (next instanceof Packet.SelectWeapons) {
                            Packet.SelectWeapons selectWeapon =
                                (Packet.SelectWeapons) next;
                            player.weapon1 = Weapon.fromString(
                                selectWeapon.weapon1
                            );
                            player.weapon2 = Weapon.fromString(
                                selectWeapon.weapon2
                            );
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
