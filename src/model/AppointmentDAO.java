package model;

import view_controller.LoginController;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static model.CustomerDAO.allCustomers;
import static model.Query.stmt;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Mason
 */
public class AppointmentDAO {

    public AppointmentDAO() {

    }   

    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    public static ObservableList<Appointment> nextMonthAppointments = FXCollections.observableArrayList();
    public static ObservableList<Appointment> nextWeekAppointments = FXCollections.observableArrayList();
    public static ObservableList<Appointment> allAppointmentsBetweenNowAndInterval = FXCollections.observableArrayList();
    
    public static Appointment getAppointment(int id) throws ClassNotFoundException, SQLException, Exception {

        String query = "SELECT appointment.appointmentId, appointment.customerId, appointment.userId,"
                + " appointment.type, appointment.time, appointment.location, appointment.contact, appointment.customer"
                + " FROM appointment WHERE appointmentId=" + id;

        Query.makeQuery(query);
        ResultSet rs = Query.getResult();
        rs.next();
        Appointment appointment = new Appointment(id, rs.getInt("customerId"), rs.getInt("userId"), rs.getString("customer"), rs.getString("location"), rs.getString("type"),
                rs.getString("contact"), rs.getString("time"));
        return appointment;

    }

    public static Appointment createAppointment(int customerID, String customer, String location, String type, String contact, String time) throws ClassNotFoundException, SQLException, Exception {
        int userID = LoginController.globalCurrentUserID;
        String userName = LoginController.globalCurrentUserName;
        String description = type + " appointment with " + customer + " in " + location;
        Instant now = Instant.now();
        LocalDateTime datetime = LocalDateTime.ofInstant(now, ZoneOffset.UTC);
        String formatted = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(datetime);

        String createAppointmentQuery = "INSERT into appointment VALUES (0, " + customerID + ", "
                + userID + ", '" + customer + "' , '" + description + "' ,'" + location + "', '" + contact
                + "','" + type + "', '" + time + "' , CURRENT_DATE(), '" + userName + "' , CURRENT_TIMESTAMP(), '" + userName + "')";

        Query.makeQuery(createAppointmentQuery);
        ResultSet rs = Query.getResult();
        rs.next();
        int appointmentID = rs.getInt(1);
        Appointment appointment = getAppointment(appointmentID);
        return appointment;
    }
    
    public static void correctApptCustName(int customerID, String customerName){

        String description = " appointment with " + customerName + " in ";
        String apptCustNameUpdateQuery = "UPDATE appointment SET customer = '"+customerName
                + "', description = CONCAT(TYPE, ' appointment with "+customerName+" in ', LOCATION)"
                + " WHERE customerId="+customerID;
        Query.makeQuery(apptCustNameUpdateQuery);
    }
    

    public static void updateAppointment(int appointmentID, int customerID, String customer, String location, String type, String time) throws ClassNotFoundException, SQLException, Exception {
        int userID = LoginController.globalCurrentUserID;
        String userName = LoginController.globalCurrentUserName;
        
        String description = type + " appointment with " + customer + " in " + location;
        String appointmentUpdateQuery = "UPDATE appointment SET customerId =" + customerID
                + ", userId = " + userID
                + ",customer = '" + customer
                + "' ,description  = '" + description
                + "',location  = '" + location
                + "' ,type = '" + type
                + "' ,time = '" + time
                + "' ,lastUpdate = CURRENT_DATE()"
                + ",lastUpdateBy = '" + userName
                + "' WHERE appointmentId =" + appointmentID + ";";

        Query.makeQuery(appointmentUpdateQuery);
    }

    public static void delAppointment(int appointmentID) {
        
        String appointmentDeleteQuery = "DELETE FROM appointment WHERE appointmentId =" + appointmentID + ";";
        Query.makeQuery(appointmentDeleteQuery);
    }

    public static ObservableList<Appointment> allApptsForMinsHoursOrDays(String minsHoursOrDays, int numMinsHoursDays) throws ClassNotFoundException, SQLException, Exception {
        
        int userID = LoginController.globalCurrentUserID;
        String userName = LoginController.globalCurrentUserName;
        allAppointmentsBetweenNowAndInterval.clear();

        String query = "SELECT * FROM appointment WHERE userId =" + userID + " AND time BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL " + numMinsHoursDays + " " + minsHoursOrDays + ")";
        Query.makeQuery(query);
        ResultSet rs = Query.getResult();
        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt("appointmentId"), rs.getInt("customerId"), rs.getInt("userId"), rs.getString("customer"), rs.getString("location"),
                    rs.getString("type"), rs.getString("contact"), rs.getString("time"));
            allAppointmentsBetweenNowAndInterval.add(appointment);
        }
        return allAppointmentsBetweenNowAndInterval;
    }

    public static ObservableList<Appointment> getAllAppointments(int userID) throws ClassNotFoundException, SQLException, Exception {
        allAppointments.clear();
        String getAllAppointmentsQuery = "SELECT appointment.appointmentId, appointment.customerId, appointment.userId,"
                + " appointment.type, appointment.time, appointment.location, appointment.contact, appointment.customer"
                + " FROM appointment WHERE appointment.userId = " + userID;

        Query.makeQuery(getAllAppointmentsQuery);
        ResultSet rs = Query.getResult();

        while (rs.next()) {
            Appointment appointment = new Appointment(rs.getInt("appointmentId"), rs.getInt("customerId"), rs.getInt("userId"), rs.getString("customer"), rs.getString("location"),
                    rs.getString("type"), rs.getString("contact"), rs.getString("time"));
            allAppointments.add(appointment);
        }
        return allAppointments;
    }

    public static boolean userHasOverlappingAppointment(ZonedDateTime appointmentTime, int appointmentID) throws ClassNotFoundException, SQLException, Exception {
        int userID = LoginController.globalCurrentUserID;
        String userName = LoginController.globalCurrentUserName;
        
        ZonedDateTime aptTime = appointmentTime.withZoneSameInstant(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        OffsetDateTime offset_utcTime = aptTime.toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC);
        String UTC_appointmentTime = offset_utcTime.format(formatter);
        String oneHourOffsetBefore = offset_utcTime.minusHours(1).format(formatter);
        String oneHourOffsetAfter = offset_utcTime.plusHours(1).format(formatter);

        String OverlappingAppointmentCheckQuery = "SELECT appointment.appointmentId FROM appointment WHERE time BETWEEN '" + oneHourOffsetBefore + "' AND '" + oneHourOffsetAfter + "' AND appointment.userId =" + userID + " AND appointment.appointmentId <> " + appointmentID;
        Query.makeQuery(OverlappingAppointmentCheckQuery);
        ResultSet rs = Query.getResult();
        return rs.next();
    }

}
