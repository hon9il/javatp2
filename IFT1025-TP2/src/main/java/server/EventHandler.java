package server;

/**
 * Cette interface définit une méthode pour gérer les événements.
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * Gère l'événement donné avec les arguments correspondants.
     * @param cmd La commande à exécuter.
     * @param arg Les arguments à passer à la commande.
     */
    void handle(String cmd, String arg);
}