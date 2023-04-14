package server;

import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import server.models.Course;

import java.io.*;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import server.models.ModeleCourse;

public class Controler {

    public static ObservableList fillTable(String sessionChoisie){
        ObservableList<ModeleCourse> courses;

        try {
            File coursInfo = new File("IFT1025-TP2/src/main/java/server/data/cours.txt");
            FileReader fr = new FileReader(coursInfo);
            BufferedReader reader = new BufferedReader(fr);

            courses = FXCollections.observableArrayList();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                String courseCode = parts[0];
                String courseName = parts[1];
                String courseSession = parts[2];
                if (courseSession.equals(sessionChoisie)) {
                    ModeleCourse courseDisponible = new ModeleCourse(courseCode, courseName, courseSession);
                    courses.add(courseDisponible);

                }
            }
            reader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    return courses;

    }

};
