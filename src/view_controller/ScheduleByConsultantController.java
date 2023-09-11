
package view_controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

import model.Appointment;
import model.AppointmentDAO;

public class ScheduleByConsultantController implements Initializable {

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
    private ChoiceBox consultantList;

    public static Map<String, Integer> userNameToIDMap = new HashMap<String, Integer>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            customerColumn.setCellValueFactory(new PropertyValueFactory<>("Customer"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
            dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
            apptTableView.setItems(AppointmentDAO.getAllAppointments(LoginController.globalCurrentUserID));
            System.out.println("initbefore tableview set");

            // Here we'll set a few users to test the software       

            userNameToIDMap.put("test", 1);
            userNameToIDMap.put("jacksmith", 2);
            userNameToIDMap.put("george", 3);
            userNameToIDMap.put("securityman", 4);

            consultantList.setItems(FXCollections.observableArrayList(
                    "test", "jacksmith", "george", "securityman"));


            consultantList.setOnAction((event) -> {

                try {
                    String selectedUser = (String) consultantList.getSelectionModel().getSelectedItem();
                    int selectedUserID = userNameToIDMap.get(selectedUser);
                    apptTableView.setItems(AppointmentDAO.getAllAppointments(selectedUserID));
                } catch (SQLException ex) {
                    Logger.getLogger(ScheduleByConsultantController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(ScheduleByConsultantController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

        } catch (SQLException ex) {
            Logger.getLogger(ScheduleByConsultantController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ScheduleByConsultantController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

}
