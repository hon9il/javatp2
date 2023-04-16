package server;

/**
 * Cette classe lance un serveur qui écoute sur le port 1337.
 */
public class ServerLauncher {
    /**
     * Port sur lequel le serveur écoute.
     */
    public final static int PORT = 1337;
    /**
     * Méthode principale qui lance le serveur et affiche un message de confirmation.
     * @param args Les arguments de ligne de commande.
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}