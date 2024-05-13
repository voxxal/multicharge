public class Server {

    public static void main(String[] args) throws Exception {
        GameServer serv = new GameServer();
        System.out.println("[SERVER] listening on port 4200");
        serv.start(4200);
    }
}
