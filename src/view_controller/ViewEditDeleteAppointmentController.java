package view_controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.AppointmentDAO;
import model.Customer;
import model.CustomerDAO;

public class ViewEditDeleteAppointmentController implements Initializable {

    @FXML
    public Button saveButton;

    @FXML
    public Button deleteButton;

    @FXML
    public Button editButton;

    @FXML
    private TableView apptTableView;

    @FXML
    private TableColumn customerColumn;

    @FXML
    private TableColumn typeColumn;

    @FXML
    private TableColumn dateTimeColumn;

    @FXML
    private TableColumn locationColumn;

    @FXML
    private ChoiceBox editAppointmentCustomer;

    @FXML
    private Label locationLabel;

    @FXML
    private ChoiceBox editAppointmentType;

    @FXML
    private ChoiceBox editAppointmentHour;

    @FXML
    private ChoiceBox editAppointmentMinute;

    @FXML
    private DatePicker datePicker;

    String hour;
    String type;
    String minute;
    String city;

    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    public static ObservableList<String> allCustomerNames = FXCollections.observableArrayList();
    public int editAppointmentID;
    public int editCustomerID = 0;
    public String editCustomerName;

    String alertTitleDateFormat = "Appointment Edit Failed!";
    String alertHeaderDateFormat = "Incorrect entry of Appointment Date/Time!";
    String alertContentDateFormat = "Appointment Date/Time must be in exact format YYYY-MM-DD HH:MM:SS. EXAMPLE: 2020-01-31 12:00:00";

    String alertTitleDateInPast = "Appointment Edit Failed!";
    String alertHeaderDateInPast = "Cannot select past dates/times for appointments!";
    String alertContentDateInPast = "Please add a future date/time.";

    String alertApptOverlapTitle = "Appointment Add Failed!";
    String alertApptOverlapHeader = "Appointment time already reserved!";
    String alertApptOverlapContent = "Please pick an earlier or later time for this appointment.";


    public void deleteAppointment() {

        try {
            Appointment deleteAppointment = (Appointment) apptTableView.getSelectionModel().getSelectedItem();
            int deleteAppointmentID = deleteAppointment.getAppointmentID();
            AppointmentDAO.delAppointment(deleteAppointmentID);
            apptTableView.setItems(AppointmentDAO.getAllAppointments(LoginController.globalCurrentUserID));
        } catch (SQLException ex) {
            Logger.getLogger(ViewEditDeleteAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ViewEditDeleteAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void editAppointment() {

        Appointment editAppointment = (Appointment) apptTableView.getSelectionModel().getSelectedItem();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeFromServer = editAppointment.getTime();
        String localTimeToDisplay = LocalDateTime.parse(dateTimeFromServer, formatter)
                .atOffset(ZoneOffset.UTC)
                .atZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()))
                .format(formatter);

        String[] timeAndDateParts = localTimeToDisplay.split(" ");
        String dateToLoad = timeAndDateParts[0];
        String timeToLoad = timeAndDateParts[1];

        String hoursToLoad = timeToLoad.substring(0, 2);
        String minutesToLoad = timeToLoad.substring(3, 5);

        editAppointmentHour.setValue(hoursToLoad);
        editAppointmentMinute.setValue(minutesToLoad);

        DateTimeFormatter datePickerFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate loadDate = LocalDate.parse(dateToLoad, datePickerFormatter);
        datePicker.setValue(loadDate);
        editAppointmentID = editAppointment.getAppointmentID();
        editCustomerName = editAppointment.getCustomer();
        editCustomerID = editAppointment.getCustomerID();
        editAppointmentCustomer.setValue(editCustomerName);

        locationLabel.setText(editAppointment.getLocation());

        editAppointmentType.setValue(editAppointment.getType());

    }

    public void saveAppointment() throws ClassNotFoundException, SQLException, Exception {


        if (editAppointmentID == 0) {

            Alert saveBeforeEditAlert = new Alert(Alert.AlertType.ERROR);
            saveBeforeEditAlert.setTitle("Invalid Button Press!");
            saveBeforeEditAlert.setHeaderText("No information to save.");
            saveBeforeEditAlert.setContentText("You must select an appointment from the list, click edit to load it, and make any edits before saving.");
            saveBeforeEditAlert.showAndWait();

        } else {


            type = (String) editAppointmentType.getSelectionModel().getSelectedItem();
            editCustomerName = (String) editAppointmentCustomer.getSelectionModel().getSelectedItem();
            String location = locationLabel.getText();
            hour = (String) editAppointmentHour.getSelectionModel().getSelectedItem();
            minute = (String) editAppointmentMinute.getSelectionModel().getSelectedItem();

            LocalDate date = datePicker.getValue();
            String time = hour + ":" + minute + ":" + "00";
            String appointmentTime = date + "T" + time;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            ZoneId timeZone = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = LocalDateTime.parse(appointmentTime,
                    DateTimeFormatter.ISO_DATE_TIME).atZone(timeZone);

            OffsetDateTime offset_utcTime = zonedDateTime.toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC);
            String UTC_appointmentTime = offset_utcTime.format(formatter);
            ZonedDateTime now = ZonedDateTime.now(timeZone);

            if (!AppointmentDAO.userHasOverlappingAppointment(zonedDateTime, editAppointmentID)) {

                if (zonedDateTime.isBefore(now)) {
                    Alert dateInPastAlert = new Alert(Alert.AlertType.ERROR);
                    dateInPastAlert.setTitle(alertTitleDateInPast);
                    dateInPastAlert.setHeaderText(alertHeaderDateInPast);
                    dateInPastAlert.setContentText(alertContentDateInPast);
                    dateInPastAlert.showAndWait();

                } else {
                    AppointmentDAO.updateAppointment(editAppointmentID, editCustomerID, editCustomerName, location, type, UTC_appointmentTime);
                    apptTableView.setItems(CustomerDAO.getAllCustomers());
                }

            } else {

                Alert apptOverlapAlert = new Alert(Alert.AlertType.ERROR);
                apptOverlapAlert.setTitle(alertApptOverlapTitle);
                apptOverlapAlert.setHeaderText(alertApptOverlapHeader);
                apptOverlapAlert.setContentText(alertApptOverlapContent);
                apptOverlapAlert.showAndWait();
                throw new IllegalArgumentException("Invalid Appointment Date/Time input. This Date/Time overlaps with another appointment on your schedule. Please try another time.");

            }

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        try {

            //Here we use a Lambda function to prevent our datePicker from being able to select any date before today's date.
            //This is shorter and much more readable with a lambda expression.
            datePicker.setDayCellFactory(picker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 0);
                }
            });


            editAppointmentHour.setItems(FXCollections.observableArrayList(
                    "09", "10", "11", "12", "13", "14", "15", "16"));

            editAppointmentMinute.setItems(FXCollections.observableArrayList(
                    "00", "15", "30", "45"));

            editAppointmentCustomer.setItems(CustomerDAO.getAllCustomerNames());

            editAppointmentType.setItems(FXCollections.observableArrayList(
                    "Initial Consultation", "Follow-Up", "Sales Presentation", "Product Support", "General Consultation"));

            customerColumn.setCellValueFactory(new PropertyValueFactory<>("Customer"));

            typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));

            dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

            locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
            apptTableView.setItems(AppointmentDAO.getAllAppointments(LoginController.globalCurrentUserID));

        } catch (SQLException ex) {
            Logger.getLogger(ViewEditDeleteAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ViewEditDeleteAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
