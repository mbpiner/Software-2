package model;

import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import view_controller.LoginController;


import static model.AppointmentDAO.correctApptCustName;

public class CustomerDAO {

    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    public static ObservableList<String> allCustomerNames = FXCollections.observableArrayList();
    

    public CustomerDAO() {

    }

    public static Customer getCustomer(int customerID) throws ClassNotFoundException, SQLException, Exception {

        String query = "SELECT customer.customerId, customer.customer, customer.addressId,"
                + "address.address,address.address2, city.city, address.postalCode, "
                + "country.country, address.phone "
                + "FROM customer inner join address on customer.addressId = address.addressId "
                + "inner join city on city.cityId = address.cityId "
                + "inner join country on city.countryId = country.countryId WHERE customerId=" + customerID;

        Query.makeQuery(query);
        ResultSet rs = Query.getResult();
        rs.next();
        Customer customer = new Customer(customerID, rs.getString("customer"), rs.getString("address"), rs.getString("address2"), rs.getInt("addressId"),
                rs.getString("city"), rs.getInt("postalCode"), rs.getString("country"), rs.getString("phone"));

        return customer;

    }

    public static int getIDGivenName(String Name, String customerAddressCityOrCountry) throws ClassNotFoundException, SQLException, Exception {
        String getIDQuery = "SELECT " + customerAddressCityOrCountry + "." + customerAddressCityOrCountry + "Id FROM " + customerAddressCityOrCountry + " WHERE " + customerAddressCityOrCountry + "." + customerAddressCityOrCountry + "='" + Name + "'";
        Query.makeQuery(getIDQuery);
        ResultSet ID_RS = Query.getResult();
        ID_RS.next();
        int ID = ID_RS.getInt(customerAddressCityOrCountry + "Id");
        return ID;
    }

    public static boolean addressExists(String address, String address2, int zip, String phone, int cityID) throws ClassNotFoundException, SQLException, Exception {
        String addressExistsQuery = "SELECT * FROM address inner join city on address.cityId = city.cityId"
                + " inner join country on city.countryId = country.countryId"
                + " WHERE address = '" + address + "' AND " + "address2 = '" + address2 + "'"
                + " AND postalCode = " + zip + " AND phone = '" + phone + "' AND address.cityId = " + cityID;

        Query.makeQuery(addressExistsQuery);
        ResultSet addressExists_RS = Query.getResult();
        return addressExists_RS.next();
    }

    public static boolean createAddress(String address, String address2, int zip, String phone, int cityID) throws ClassNotFoundException, SQLException, Exception {
        int userID = LoginController.globalCurrentUserID;
        String userName = LoginController.globalCurrentUserName;
        
        String addressInsertQuery = "INSERT into address VALUES (0,'" + address + "','"
                + address2 + "'," + cityID + "," + zip + ",'" + phone + "',CURRENT_DATE(), '" + userName + "',CURRENT_TIMESTAMP(),'" + userName + "')";

//        String addressInsertQuery = "INSERT into address(address, address2, cityId, postalCode, phone, dateCreated, createdBy, lastUpdate, lastUpdateBy) VALUES ('" + address + "','"
//                + address2 + "'," + cityID + "," + zip + ",'" + phone + "',CURRENT_DATE(), '" + userName + "',CURRENT_TIMESTAMP(),'" + userName + "')";

        if (!addressExists(address, address2, zip, phone, cityID)) {
            Query.makeQuery(addressInsertQuery);
            return true;
        } else {
            return false;
        }

    }

    public static boolean customerInfoValidates(String name, String address, String zip, String phone) throws ClassNotFoundException, SQLException, Exception {

        return address.matches("\\d+\\w+\\s\\w+\\s\\w+") && address.length() < 25 && name.matches("(?:(\\w+))(?: (\\w+))?$") && name.length() < 25 && zip.matches("^\\d{5}") && phone.matches("(?:\\d{3}-){2}\\d{4}");

    }

    public static Customer createCustomer(String customer, String address, String address2, String city, int zip, String country, String phone) throws ClassNotFoundException, SQLException, Exception {
        int userID = LoginController.globalCurrentUserID;
        String userName = LoginController.globalCurrentUserName;
        
        int cityID = getIDGivenName(city, "city");
        int customerID;
        int addressID;
        createAddress(address, address2, zip, phone, cityID);
        addressID = getIDGivenName(address, "address");
        String createCustomerQuery = "INSERT into customer VALUES (0,'" + customer + "',"
                + addressID + ", 1, CURRENT_DATE(),  '" + userName + "',CURRENT_TIMESTAMP(),'" + userName + "')";

// String createCustomerQuery = "INSERT into customer VALUES ('" + customer + "',"
//                + addressID + ", 1, CURRENT_DATE(),  '" + userName + "',CURRENT_TIMESTAMP(),'" + userName + "')";


        Query.makeQuery(createCustomerQuery);
        ResultSet rs = Query.getResult();
        rs.next();
        customerID = rs.getInt(1);
        System.out.println("custID" + customerID + "custIDend..\n");
        Customer newCustomer = getCustomer(customerID);
        return newCustomer;

    }
                                                        //(editCustomerID, name, address, address2, city, zip, phone);
    public static void updateCustomer(int customerID, String customer, String address, String address2, String city, int zip, String phone) throws ClassNotFoundException, SQLException, Exception {
        int cityID = getIDGivenName(city, "city");
        createAddress(address, address2, zip, phone, cityID);
        int addressID = getIDGivenName(address, "address");
        
        AppointmentDAO.correctApptCustName(customerID, customer);
       
        
        String customerUpdateQuery = "UPDATE customer SET customer.customer = '" + customer + "'"
                + ", customer.addressId = " + addressID
                + ", customer.lastUpdateBy = '"+LoginController.globalCurrentUserName+"'"
                + ", customer.lastUpdate = CURRENT_TIMESTAMP() WHERE customer.customerId =" + customerID + ";";
        
        
        
        String addressUpdateQuery = "UPDATE address SET address.address ='" + address + "'"
                + ",address.address2 = '" + address2 + "'"
                + ",address.cityId = " + cityID
                + ",address.postalCode = " + zip
                + ",address.phone = '" + phone + "'"
                + " WHERE address.addressId =" + addressID + ";";

        Query.makeQuery(customerUpdateQuery);
        Query.makeQuery(addressUpdateQuery);
        
    }

    public static void delCustomer(int customerID) {
        String customerDeleteQuery = "DELETE FROM customer WHERE customer.customerId =" + customerID + ";";
        Query.makeQuery(customerDeleteQuery);
    }

    public static ObservableList<Customer> getAllCustomers() throws ClassNotFoundException, SQLException, Exception {
        allCustomers.clear();
        String getAllCustomersQuery = "SELECT customer.customerId, customer.customer, customer.addressId,"
                + "address.address,address.address2, city.city, address.postalCode, "
                + "country.country, address.phone "
                + "FROM customer inner join address on customer.addressId = address.addressId "
                + "inner join city on city.cityId = address.cityId "
                + "inner join country on city.countryId = country.countryId";

        Query.makeQuery(getAllCustomersQuery);
        ResultSet rs = Query.getResult();

        while (rs.next()) {
            Customer customer = new Customer(rs.getInt("customerId"), rs.getString("customer"), rs.getString("address"), rs.getString("address2"),
                    rs.getInt("addressId"), rs.getString("city"), rs.getInt("postalCode"), rs.getString("country"), rs.getString("phone"));

            System.out.println("infunc");
            System.out.println(rs);
            System.out.println("infunc2");
            allCustomers.add(customer);
        }

        return allCustomers;

    }

    public static ObservableList<String> getAllCustomerNames() throws ClassNotFoundException, SQLException, Exception {
        allCustomerNames.clear();

        //Here we use a lambda to get ObservableList<Customer> allCustomers, a list of Customer Objects returned from getAllCustomers().
        //then we loop through each object in this list, using customer as the parameter in our Lambda function which represents each individual
        //customer object. Then we use the .add() function of ObservableList and pass customer.getCustomer() as a parameter, which returns a String, 
        //the customer's name. Doing this with a Lambda is very clean, neat, and easy to understand.
        getAllCustomers().forEach((customer) -> {
            allCustomerNames.add(customer.getCustomer());
        });

        return allCustomerNames;

    }

}
