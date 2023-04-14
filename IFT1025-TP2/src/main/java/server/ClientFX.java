package server;

// CAMBIAR VALORES DEL ANCHO DEL COSO

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.cell.PropertyValueFactory;
import server.models.Course;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ClientFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        HBox root = new HBox();
        root.setStyle("-fx-background-color: beige;");

        /**
         * LISTE DES COURS
         */

        VBox listeCours = new VBox();
        listeCours.setPrefWidth(500);

        Text cours = new Text("Liste des cours");
        cours.setTextAlignment(TextAlignment.CENTER);
        cours.setFont(Font.font("sans serif", 20));

        TableView<Course> TableauCourses = new TableView<Course>();
        TableColumn<Course, String> code = new TableColumn<>("Code");
        code.setCellValueFactory(new PropertyValueFactory<>("Code"));
        code.setPrefWidth(150);

        TableColumn<Course, String> coursSession = new TableColumn<>("Name");
        coursSession.setCellValueFactory(new PropertyValueFactory<>("Name"));
        coursSession.setPrefWidth(250);
        TableauCourses.getColumns().addAll(code, coursSession);

        listeCours.setPadding(new Insets(0,50,0,50));

        HBox options = new HBox();
        MenuItem automne = new MenuItem("Automne");
        automne.setOnAction(actionEvent -> {
            Controler.filtrerCourses("Automne");
        });
        MenuItem hiver = new MenuItem("Hiver");
        hiver.setOnAction(actionEvent -> {
            Controler.filtrerCourses("Hiver");
            }
        );
        MenuItem ete = new MenuItem("Été");
        ete.setOnAction(actionEvent -> {
            Controler.filtrerCourses("Ete");
        });

        MenuButton sessions = new MenuButton("sessions", null, automne, hiver,ete);
        sessions.setPrefWidth(100);

        Button charger = new Button("charger");
        charger.setOnAction(actionEvent -> {
            Controler.charger();
        });

        options.getChildren().addAll(sessions, charger);
        options.setAlignment(Pos.CENTER);
        options.setSpacing(100);
        options.setPadding(new Insets(20));


        listeCours.getChildren().addAll(cours, TableauCourses,  options);
        listeCours.setAlignment(Pos.CENTER);


        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        separator.setStyle("-fx-border-width: 2px;");

        /**
         * INSCRIPTION AUX COURS
         */

        VBox inscription = new VBox();
        inscription.setPrefWidth(500);

        Text formTitle = new Text("Formulaire d'inscription");
        formTitle.setFont(Font.font("sans serif", 20));

        Text prenom = new Text("Prénom");

        TextField prenomField = new TextField();
        Text nom = new Text("Nom");
        TextField nomField = new TextField();
        Text email = new Text("Email");
        TextField emailField = new TextField();
        Text matricule = new Text("Matricule");
        TextField matriculeField = new TextField();

        GridPane gridFormulaire = new GridPane();
        gridFormulaire.setHgap(10);
        gridFormulaire.setVgap(10);

        gridFormulaire.add(nom, 0, 0);
        gridFormulaire.add(nomField, 3, 0);
        gridFormulaire.add(prenom, 0, 1);
        gridFormulaire.add(prenomField, 3, 1);
        gridFormulaire.add(email, 0, 2);
        gridFormulaire.add(emailField, 3, 2);
        gridFormulaire.add(matricule, 0, 3);
        gridFormulaire.add(matriculeField, 3, 3);

        VBox donnees = new VBox();
        VBox fields = new VBox();
        HBox formulaire = new HBox();

        formulaire.getChildren().add(gridFormulaire);
        formulaire.setAlignment(Pos.CENTER);
        formulaire.setPadding(new Insets(20));


        formulaire.getChildren().addAll(donnees, fields);
        Button envoyer = new Button("envoyer");
        envoyer.setPrefWidth(100);
        envoyer.setOnAction(actionEvent -> {
            Controler.insccription();
        });

        inscription.getChildren().addAll(formTitle, formulaire, envoyer);

        inscription.setAlignment(Pos.BASELINE_CENTER);

        root.getChildren().addAll(listeCours, separator, inscription);

        Scene scene = new Scene(root, 1000, 500);
        stage.setTitle("Inscription UdeM");
        stage.setScene(scene);
        stage.show();

    }
}
