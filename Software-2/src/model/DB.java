package model;

import java.sql.*;
import java.util.concurrent.Callable;
import com.mysql.jdbc.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Mason
 */
public class DB {

//    static final String DBNAME = "U06Z4x";
//    static final String DB_URL = "jdbc:mysql://3.227.166.251/" + DBNAME;
//    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//
//    static final String USER = "U06Z4x";
//    static final String PASS = "53688910137";
//    
    
    
    
    static final String DBNAME = "test";
    static final String DB_URL = "jdbc:mysql://localhost/" + DBNAME + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    //jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    static final String USER = "debian-sys-maint";
    static final String PASS = "8ouv2tJjBbC7Wr5c";
    
    
    
    
    
    
    
    
    
    
    public static Connection conn;

    public DB() {

    }

    public static void makeConnection() throws ClassNotFoundException, SQLException, Exception {
        Class.forName(JDBC_DRIVER);
        conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception {
        conn.close();
        System.out.println("connection closed");
    }

}
