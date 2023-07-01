package view_controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.AppointmentDAO;

public class Appt15MinWarningController implements Initializable {

    @FXML
    private TableView next15MinsAppointmentTable;

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
            next15MinsAppointmentTable.setItems(AppointmentDAO.allApptsForMinsHoursOrDays("minute", 15));
        } catch (SQLException ex) {
            Logger.getLogger(Appt15MinWarningController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Appt15MinWarningController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
