package server;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    // Les trois objets sont private parce qu'ils devraient être seulement manipulés dans class Client
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    // Constructeur de la classe Client pour initialiser la connexion au serveur
    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    // Méthode pour récupérer la liste des cours disponibles pour une session donnée
    public void showAvailableCourses() {
        try {
            Scanner scanner = new Scanner(System.in);
            boolean continueLoop = true;
            while (continueLoop) {
                System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours");
                System.out.println("1. Automne");
                System.out.println("2. Hiver");
                System.out.println("3. Ete");

                int session = scanner.nextInt();
                String sessionChoisie = "";

                if(session == 1){
                    sessionChoisie = "Automne";
                }
                if(session == 2){
                    sessionChoisie = "Hiver";
                }
                if(session == 3){
                    sessionChoisie = "Ete";
                }

                // Envoyer la commande handleLoadCourses au serveur avec le semestre demandé
                objectOutputStream.writeObject(Server.LOAD_COMMAND + " " + sessionChoisie);
                objectOutputStream.flush();

                // Afficher la liste de cours
                System.out.println("Les cours offerts pendant la session d' " + sessionChoisie + " sont:");

                // Lire la liste de cours renvoyée par le serveur
                ArrayList<Course> courses;
                courses = (ArrayList<Course>) objectInputStream.readObject();
                if (courses.isEmpty()) {
                    System.out.println("Aucun cours disponible pour la session sélectionnée.");
                } else {
                    for (Course courseDisponible : courses) {

                        System.out.println(courseDisponible);
                    }
                }

                // Demander à l'utilisateur s'il souhaite s'inscrire à un cours ou charger des cours pour un autre semestre

                System.out.println("Pour continuer:");
                System.out.println("1. Consulter les cours offerts pour une autre session");
                System.out.println("2. Inscription à un cours");

                int choix = scanner.nextInt();

                if (choix == 1) {
                    continueLoop = true;
                } else if (choix == 2) {
                    registerStudentToCourse();
                    continueLoop = false;
                } else {
                    System.out.println("Choix invalide. Veuillez entrer 1, 2 ou 3.");
                    continueLoop = true;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



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
            Course course = new Course(courseCode, courseTitle,"");
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
            Client client = new Client("localhost", 1337);

            // Appeler la méthode d'inscription
            client.showAvailableCourses();

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
