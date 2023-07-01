package view_controller;

import view_controller.AddCustomerController;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.TemporalQueries.localTime;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.AppointmentDAO;
import model.Customer;
import model.CustomerDAO;
import model.Query;
import static model.Main.allCities;

public class AddAppointmentController implements Initializable {

    @FXML
    private ChoiceBox newAppointmentType;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Label customer;

    @FXML
    private TableView custTable;

    @FXML
    private TableColumn custIDColumn;

    @FXML
    private TableColumn custNameColumn;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox newAppointmentHour;

    @FXML
    private ChoiceBox newAppointmentMinute;

    @FXML
    private ChoiceBox newAppointmentCity;

    @FXML
    private Label newAppointmentConsultant;

    @FXML
    private Label newAppointmentCountry;

    //do this this way...?
    String selectedCustomerName = "";
    int selectedCustomerID = 0;
    String hour;
    String type;
    String minute;
    String consultant;
    String city;

    String alertTitle = "Appointment Add Failed!";
    String alertHeader = "Cannot select past dates/times for appointments!";
    String alertContent = "Please add a future date/time.";

    String alertApptOverlapTitle = "Appointment Add Failed!";
    String alertApptOverlapHeader = "Appointment time already reserved!";
    String alertApptOverlapContent = "Please pick an earlier or later time for this appointment.";

    public void addCustomerToNewAppointment(ActionEvent event) throws ClassNotFoundException, SQLException, Exception {

        Customer customerNameAndID = (Customer) custTable.getSelectionModel().getSelectedItem();

        selectedCustomerName = customerNameAndID.getCustomer();
        selectedCustomerID = customerNameAndID.getCustomerID();

        customer.setText(selectedCustomerName);

    }

    public void addNewAppointment(ActionEvent actionEvent) throws ClassNotFoundException, SQLException, Exception, IllegalArgumentException {

        if (selectedCustomerName.equals("") || selectedCustomerID == 0) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding appointment!");
            alert.setHeaderText("No customer was selected.");
            alert.setContentText("Please select a customer from the list, press the 'add customer' button, and try again.");
            alert.showAndWait();

        }

        hour = (String) newAppointmentHour.getSelectionModel().getSelectedItem();
        minute = (String) newAppointmentMinute.getSelectionModel().getSelectedItem();
        type = (String) newAppointmentType.getSelectionModel().getSelectedItem();
        city = (String) newAppointmentCity.getSelectionModel().getSelectedItem();
        consultant = LoginController.globalCurrentUserName;
        LocalDate date = datePicker.getValue();
        String time = hour + ":" + minute + ":" + "00";
        String appointmentTime = date + "T" + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId timeZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = LocalDateTime.parse(appointmentTime, DateTimeFormatter.ISO_DATE_TIME).atZone(timeZone);
        OffsetDateTime offset_utcTime = zonedDateTime.toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC);
        String UTC_appointmentTime = offset_utcTime.format(formatter);
        ZonedDateTime now = ZonedDateTime.now(timeZone);

        if (!AppointmentDAO.userHasOverlappingAppointment(zonedDateTime, 0)) {

            if (zonedDateTime.isBefore(now)) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(alertTitle);
                alert.setHeaderText(alertHeader);
                alert.setContentText(alertContent);
                alert.showAndWait();

            } else {

                Appointment appointment = AppointmentDAO.createAppointment(selectedCustomerID, selectedCustomerName, city, type, consultant, UTC_appointmentTime);
                custTable.setItems(CustomerDAO.getAllCustomers());
            }

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(alertApptOverlapTitle);
            alert.setHeaderText(alertApptOverlapHeader);
            alert.setContentText(alertApptOverlapContent);
            alert.showAndWait();
            throw new IllegalArgumentException("Invalid Appointment Time input. Overlaps with another appointment this user already has within the database.");

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        newAppointmentConsultant.setText(LoginController.globalCurrentUserName);

        //Here we use a Lambda function to prevent our datePicker from being able to select any date before today's date.
        //This is shorter and much more readable with a lambda expression.
        datePicker.setDayCellFactory(picker -> {
            return new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 0);
                }
            };
        });

        try {

            newAppointmentType.setItems(FXCollections.observableArrayList(
                    "Initial Consultation", "Follow-Up", "Sales Presentation", "Product Support", "General Consultation"));

            newAppointmentHour.setItems(FXCollections.observableArrayList(
                    "09", "10", "11", "12", "13", "14", "15", "16"));

            newAppointmentMinute.setItems(FXCollections.observableArrayList(
                    "00", "15", "30", "45"));
            newAppointmentCity.setItems(allCities);
            custNameColumn.setCellValueFactory(new PropertyValueFactory<>("Customer"));
            custIDColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
            custTable.setItems(CustomerDAO.getAllCustomers());
            newAppointmentCity.setOnAction((event) -> {
                try {
                    newAppointmentCountry.setText(Query.getCountryGivenCity((String) newAppointmentCity.getSelectionModel().getSelectedItem()));
                } catch (SQLException ex) {
                    Logger.getLogger(AddCustomerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        } catch (SQLException ex) {
            Logger.getLogger(AddAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AddAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
