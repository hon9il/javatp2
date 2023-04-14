package server;

import server.models.Course;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static server.Server.objectOutputStream;

public class Controler {

    public static void filtrerCourses(String session) {
        ArrayList<Course> courses;
        try {

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
                    Course courseDisponible = new Course(courseName, courseCode, session);

                    courses.add(courseDisponible);
                    System.out.println(courseDisponible.getCode() +" "+ courseDisponible.getName());
                }
            }
            /**
             *  Fermer le fichier texte
             */
            reader.close();


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void charger(){
        //TODO: this method should get the course code and name to fill the inscription form
    }
    public static void insccription(){
        // TODO: once everything has been filled, this method should print the data into a .txt file
    }
};
