import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    // Les trois objets sont private pcq'ils devraient être seulement manipulés dans class Client
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    // Constructeur de la classe Client pour initialiser la connexion au serveur
    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    // Code de F1 ici !!

    // Méthode pour enregistrer un étudiant à un cours
    public void registerStudentToCourse() {
        Scanner scanner = new Scanner(System.in);

        // Récupérer les informations de l'étudiant et du cours
        System.out.print("Entrez le prénom: ");
        String prenom = scanner.nextLine();
        System.out.print("Entrez le nom: ");
        String nom = scanner.nextLine();
        System.out.print("Entrez l'email: ");
        String email = scanner.nextLine();
        System.out.print("Entrez le matricule: ");
        String matricule = scanner.nextLine();
        System.out.print("Entrez le code du cours: ");
        String courseCode = scanner.nextLine();
        System.out.print("Entrez le titre du cours: ");
        String courseTitle = scanner.nextLine();

        // Créer des objets Course et RegistrationForm avec les informations saisies
        Course course = new Course(courseCode, courseTitle);
        RegistrationForm form = new RegistrationForm(prenom, nom, email, matricule, course);

        // Envoyer la commande et le formulaire d'inscription au serveur
        try {
            objectOutputStream.writeObject(Server.REGISTER_COMMAND + " " + form.toString());
            objectOutputStream.flush();

            // Lire la réponse du serveur et afficher le résultat
            String response = (String) objectInputStream.readObject();
            System.out.println(response);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Méthode "main" pour exécuter le client
    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 9090);

            // Tu peux appeler ton méthode F1 ici !!

            // Appeler la méthode d'inscription
            client.registerStudentToCourse();

            // Fermer la connexion
            client.closeConnection();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour fermer la connexion au serveur
    public void closeConnection() throws IOException {
        objectInputStream.close();
        objectOutputStream.close();
        socket.close();
    }
}
