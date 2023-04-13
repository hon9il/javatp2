package server;

// J'ai modifié les importations en java.io.* pour que le code soit plus court.
import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Cette classe représente un serveur qui écoute sur un port spécifique et attend les connexions des clients.
 * Le serveur reçoit des commandes à partir des clients, les traite et répond en conséquence.
 */
public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private static Socket client;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Constructeur de la classe Server.
     *
     * @param port le numéro de port sur lequel le serveur doit écouter les connexions entrantes
     * @throws IOException si une erreur se produit lors de la création du serveur socket
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajoute un objet EventHandler à la liste des gestionnaires d'événements.
     *
     * @param h l'objet EventHandler à ajouter à la liste des gestionnaires d'événements
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Avertissement des gestionnaires d'événements enregistrés.
     *
     * @param cmd la commande qui a été traitée
     * @param arg les arguments de la commande qui a été traitée
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Méthode principale qui écoute les connexions entrantes et traite les commandes reçues.
     * La méthode reste en boucle infinie et s'exécute jusqu'à ce que le programme soit arrêté.
     */

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Méthode qui attend les commandes entrantes du client, les traite et les renvoie en conséquence.
     * La méthode attend une ligne de texte à partir du flux d'entrée de l'objet client,
     * la divise en deux parties (la commande et les arguments) et les envoie aux gestionnaires d'événements enregistrés.
     *
     * @throws IOException si une erreur se produit lors de la lecture du flux d'entrée ou de la communication avec le client
     * @throws ClassNotFoundException si la classe de l'objet lu depuis le flux d'entrée n'est pas trouvée
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Lit une commande envoyée par le client sous forme de chaîne de caractères et la sépare en deux parties : la commande et les arguments.
     * Renvoie ces deux parties sous forme de paire de chaînes de caractères.
     *
     * @param line la commande envoyée par le client
     * @return une paire de chaînes de caractères contenant la commande et les arguments
     */

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Ferme les flux d'entrée/sortie et la connexion client.
     * Gère les exceptions en cas d'erreur lors de la fermeture.
     *
     * @throws IOException si une erreur se produit lors de la fermeture des flux ou de la connexion
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Traite la commande et les arguments envoyés par le client.
     * Appelle la méthode correspondante en fonction de la commande.
     *
     * @param cmd la commande envoyée par le client
     * @param arg les arguments associés à la commande
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     * Lit un fichier texte contenant des informations sur les cours et les transfome en liste d'objets 'Course'.
     * La méthode filtre les cours par la session spécifiée en argument.
     * Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     * La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     *
     * @param arg la session pour laquelle on veut récupérer la liste des cours
     * @return  une liste d'objets 'Course' correspondant aux cours de la session spécifiée en argument
     */
    public static Object handleLoadCourses(String arg) {
        ArrayList<Course> courses;
        try {

            String session = arg;
            File coursInfo = new File("IFT1025-TP2/src/main/java/server/data/cours.txt");
            FileReader fr = new FileReader(coursInfo);
            BufferedReader reader = new BufferedReader(fr);

            /**
             * Lire le fichier texte et creer la liste des cours
             */

            courses = new ArrayList<Course>();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                String courseCode = parts[0];
                String courseName = parts[1];
                String courseSession = parts[2];
                if (courseSession.equals(session)) {
                    Course courseDisponible = new Course(courseName, courseCode, courseSession);
                    courses.add(courseDisponible);

                }
            }
            /**
             *  Fermer le fichier texte
             */
            reader.close();
            /**
             * Envoyer la liste des cours au client
             */
            objectOutputStream.writeObject(courses);
            objectOutputStream.flush();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return courses;

    }

    /**
     * Récupère l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistre dans un fichier texte
     * et renvoie un message de confirmation au client.
     * La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    private void handleRegistration() {
        try {
            RegistrationForm form = (RegistrationForm) objectInputStream.readObject();

            /**
             * Ouvrir le fichier pour ajouter des données.
             */
            FileWriter writer = new FileWriter("registrations.txt", true);

            /**
             * Écrire le formulaire d'inscription dans un fichier.
             */
            writer.write(form.toString() + "\n");
            writer.close();

            /**
             * Envoyer un message de réussite au client et gérer et afficher les exceptions en cas d'erreurs.
             */
            objectOutputStream.writeObject("Inscription réussie");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}