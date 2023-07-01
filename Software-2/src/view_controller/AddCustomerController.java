package view_controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.CustomerDAO;
import model.Query;
import static model.Main.allCities;

/**
 *
 *
 * @author Mason
 */
public class AddCustomerController implements Initializable {

    @FXML
    private TextField newCustomerName;

    @FXML
    private TextField newCustomerAddress;

    @FXML
    private TextField newCustomerAddress2;

    @FXML
    private ChoiceBox newCustomerCity;

    @FXML
    private TextField newCustomerZip;

    @FXML
    private TextField newCustomerPhone;

    @FXML
    private Button newCustomerSaveButton;

    @FXML
    private Button newCustomerCancelButton;

    @FXML
    private Label newCustomerCountry;

    String inputValidationFailTitle = "Creating new customer failed!";
    String inputValidationFailHeader = "Incorrect Input";
    String inputValidationFailContent = "All boxes except address 2 must be filled and all selections made. Names must be first or first&last and use only letters a-z and spaces.\n"
            + "Address must be a street number and name, Ex: 123 Main Street. Limit names and addresses to under 25 characters.\n"
            + "Phone numbers must be in 123-456-7890 format.\n"
            + "Zip codes must be exactly 5 digits 0-9.\n"
            + "City must be selected for country to populate.";

    public void clearNewCustomerData(ActionEvent event) throws Exception {

    }

    public void addNewCustomer(ActionEvent event) throws Exception {

        try {
            String name = newCustomerName.getText();
            String address = newCustomerAddress.getText();
            String address2 = newCustomerAddress2.getText();

            int zip = Integer.parseInt(newCustomerZip.getText());
            String phone = newCustomerPhone.getText();
            String zipString = String.valueOf(zip);

            if (CustomerDAO.customerInfoValidates(name, address, zipString, phone)) {
                String city = (String) newCustomerCity.getSelectionModel().getSelectedItem();
                String country = Query.getCountryGivenCity(city);
                Customer customer = CustomerDAO.createCustomer(name, address, address2, city, zip, country, phone);
            } else {
                Alert failedValidationAlert = new Alert(Alert.AlertType.ERROR);
                failedValidationAlert.setTitle(inputValidationFailTitle);
                failedValidationAlert.setHeaderText(inputValidationFailHeader);
                failedValidationAlert.setContentText(inputValidationFailContent);
                failedValidationAlert.showAndWait();
            }
        } catch (Exception e) {
            Alert failedValidationAlert = new Alert(Alert.AlertType.ERROR);
            failedValidationAlert.setTitle(inputValidationFailTitle);
            failedValidationAlert.setHeaderText(inputValidationFailHeader);
            failedValidationAlert.setContentText(inputValidationFailContent);
            failedValidationAlert.showAndWait();

        }

    }

    @Override

    public void initialize(URL url, ResourceBundle rb) {

        try {

            newCustomerCity.setItems(allCities);
            newCustomerCity.setOnAction((event) -> {
                try {
                    newCustomerCountry.setText(Query.getCountryGivenCity((String) newCustomerCity.getSelectionModel().getSelectedItem()));
                } catch (SQLException ex) {
                    Logger.getLogger(AddCustomerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        } catch (Exception ex) {
            Logger.getLogger(AddCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
