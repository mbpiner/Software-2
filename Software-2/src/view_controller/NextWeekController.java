
package view_controller;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import static java.time.Instant.now;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.time.*;
import static java.time.Instant.now;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.AppointmentDAO;

/**
 * 
 *
 * @author Mason
 */
public class NextWeekController implements Initializable {

    @FXML
    private TableView nextWeekAppointmentTable;

    @FXML
    private TableColumn customerColumn;

    @FXML
    private TableColumn consultantColumn;

    @FXML
    private TableColumn typeColumn;

    @FXML
    private TableColumn dateTimeColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
          
            customerColumn.setCellValueFactory(new PropertyValueFactory<>("Customer"));
            consultantColumn.setCellValueFactory(new PropertyValueFactory<>("Contact"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
            dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));
            nextWeekAppointmentTable.setItems(AppointmentDAO.allApptsForMinsHoursOrDays("day", 7));

        } catch (Exception ex) {
            Logger.getLogger(NextWeekController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
