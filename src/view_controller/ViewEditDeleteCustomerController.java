
package view_controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.CustomerDAO;
import model.Query;

import static model.Main.allCities;
import static model.Main.allConsultants;

public class ViewEditDeleteCustomerController implements Initializable {

    @FXML
    private TextField custName;

    @FXML
    private TextField custAddress;

    @FXML
    private TextField custAddress2;

    @FXML
    private TextField custCity;

    @FXML
    private TextField custZip;

    @FXML
    private TextField custPhone;

    @FXML
    private Button custSaveButton;

    @FXML
    private Button custDeleteButton;

    @FXML
    private ChoiceBox editCustomerCity;

    @FXML
    private Label editCustomerCountry;

    @FXML
    private Button custCancelButton;

    @FXML
    private TableView custTableView;

    @FXML
    private TableColumn custIDColumn;

    @FXML
    private TableColumn custNameColumn;

    String inputValidationFailTitle = "Creating new customer failed";
    String inputValidationFailHeader = "Incorrect Input";
    String inputValidationFailContent = "All boxes must be filled and all selections made.\n Names must be letters a-z and spaces.\n"
            + "Addresses must be letters a-z, numbers, and spaces.\n"
            + "Phone numbers must be in 123-456-7890 format using only numbers and dashes.\n"
            + "Zip codes must strictly be 5 numeric characters 0-9.\n"
            + "City must also be selected for country to populate.";

    public int editCustomerID = 0;

    public void editCustomer() {

        Customer editCustomer = (Customer) custTableView.getSelectionModel().getSelectedItem();
        System.out.println(editCustomer.getCustomer());

        editCustomerID = editCustomer.getCustomerID();
        custName.setText(editCustomer.getCustomer());
        custAddress.setText(editCustomer.getAddress());
        custAddress2.setText(editCustomer.getAddress2());
        editCustomerCity.setValue(editCustomer.getCity());
        editCustomerCountry.setText(editCustomer.getCountry());
        custZip.setText(Integer.toString(editCustomer.getZip()));
        custPhone.setText(editCustomer.getPhone());
    }

    public void deleteCustomer() {

        try {
            Customer deleteCustomer = (Customer) custTableView.getSelectionModel().getSelectedItem();
            int deleteCustomerID = deleteCustomer.getCustomerID();
            CustomerDAO.delCustomer(deleteCustomerID);
            custTableView.setItems(CustomerDAO.getAllCustomers());
        } catch (SQLException ex) {
            Logger.getLogger(ViewEditDeleteCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ViewEditDeleteCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void saveCustomer() throws ClassNotFoundException, SQLException, Exception {


        if (editCustomerID == 0) {

            Alert saveBeforeEditAlert = new Alert(Alert.AlertType.ERROR);
            saveBeforeEditAlert.setTitle("Invalid Button Press!");
            saveBeforeEditAlert.setHeaderText("No information to save.");
            saveBeforeEditAlert.setContentText("You must select a customer from the list, click edit to load it, and make any edits before saving.");
            saveBeforeEditAlert.showAndWait();

        } else {


            String name = custName.getText();
            String address = custAddress.getText();
            String address2 = custAddress2.getText();
            String city = (String) editCustomerCity.getSelectionModel().getSelectedItem();
            int zip = Integer.parseInt(custZip.getText());
            String country = editCustomerCountry.getText();
            String phone = custPhone.getText();
            String zipString = String.valueOf(zip);

            if (CustomerDAO.customerInfoValidates(name, address, zipString, phone)) {


                CustomerDAO.updateCustomer(editCustomerID, name, address, address2, city, zip, phone);
                custTableView.setItems(CustomerDAO.getAllCustomers());


            } else {


                Alert failedValidationAlert = new Alert(Alert.AlertType.ERROR);
                failedValidationAlert.setTitle(inputValidationFailTitle);
                failedValidationAlert.setHeaderText(inputValidationFailHeader);
                failedValidationAlert.setContentText(inputValidationFailContent);
                failedValidationAlert.showAndWait();

            }

        }


    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            System.out.println("invieweditdel init");
            editCustomerCity.setItems(allCities);
            custNameColumn.setCellValueFactory(new PropertyValueFactory<>("Customer"));
            custIDColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
            custTableView.setItems(CustomerDAO.getAllCustomers());

            editCustomerCity.setOnAction((event) -> {
                try {
                    editCustomerCountry.setText(Query.getCountryGivenCity((String) editCustomerCity.getSelectionModel().getSelectedItem()));
                } catch (SQLException ex) {
                    Logger.getLogger(ViewEditDeleteCustomerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });


        } catch (SQLException ex) {
            Logger.getLogger(ViewEditDeleteCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ViewEditDeleteCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
