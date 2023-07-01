package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Mason
 */
public class Main extends Application {

    public static ObservableList<String> allCities = FXCollections.observableArrayList();
    public static ObservableList<String> allConsultants = FXCollections.observableArrayList();
    public static String filePath = System.getProperty("user.dir");

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/loginView.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, Exception {

        DB.makeConnection();

        String allCitiesQuery = "SELECT city.city FROM city";
        Query.makeQuery(allCitiesQuery);
        ResultSet allCitiesResult = Query.getResult();

        while (allCitiesResult.next()) {
            allCities.add(allCitiesResult.getString("city"));
        }
        System.out.println(filePath);

        launch(args);
    }

}
