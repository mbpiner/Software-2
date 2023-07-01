package view_controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AppointmentDAO;
import model.Log;
import model.Query;

/**
 *
 * @author Mason
 */
public class LoginController implements Initializable {

    @FXML
    public TextField loginUsernameField;

    @FXML
    public TextField loginPasswordField;

    @FXML
    public Label loginUsernameLabel;

    @FXML
    public Label loginPasswordLabel;

    @FXML
    public Label loginTitleLabel;

    @FXML
    public Button loginSubmitButton;

    String failedLoginAlertTitle = "Login Failed!";
    String failedLoginAlertHeader = "Incorrect Username and/or Password!";
    String failedLoginAlertContent = "Please try again.";

    String SuccessfulLoginAlertTitle = "Login Successful!";
    String SuccessfulLoginAlertHeader = "Welcome to the Appointment Scheduler System.";
    String SuccessfulLoginAlertContent = "Appointments are billed for an hour in length and are stored in military time. Good luck!";

    public static int globalCurrentUserID;
    public static String globalCurrentUserName;
    String userName;
    String userPassWord;

    @FXML
    public void loginSubmitButtonPushed(ActionEvent actionEvent) throws ClassNotFoundException, SQLException, Exception {

        userName = loginUsernameField.getText();
        userPassWord = loginPasswordField.getText();
        try {

            Alert SuccessfulLoginAlert = new Alert(Alert.AlertType.INFORMATION);
            SuccessfulLoginAlert.setTitle(SuccessfulLoginAlertTitle);
            SuccessfulLoginAlert.setHeaderText(SuccessfulLoginAlertHeader);
            SuccessfulLoginAlert.setContentText(SuccessfulLoginAlertContent);

            String query = "SELECT * FROM user WHERE user.userName ='" + userName + "' AND user.password ='" + userPassWord + "'";

            Query.makeQuery(query);
            ResultSet rs = Query.getResult();
            rs.next();
            userName = rs.getString("userName");
            userPassWord = rs.getString("password");
            globalCurrentUserID = rs.getInt("userId");
            globalCurrentUserName = userName;
            Log.log(userName, true);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboardView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.show();
            SuccessfulLoginAlert.showAndWait();

            ObservableList allApptsWithin15Mins = AppointmentDAO.allApptsForMinsHoursOrDays("minute", 15);
            if (!allApptsWithin15Mins.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Appointment Reminder");
                alert.setHeaderText("Appointment within next 15 minutes");
                alert.setResizable(false);
                alert.setContentText("You have an appointment within the next 15 minutes! Select OK to display them in a table. Otherwise, select Cancel.");
                
                Optional<ButtonType> result = alert.showAndWait();
                ButtonType button = result.orElse(ButtonType.CANCEL);

                if (button == ButtonType.OK) {

                    FXMLLoader apptWarningLoader = new FXMLLoader(getClass().getResource("appt15MinWarningView.fxml"));
                    Parent root2 = (Parent) apptWarningLoader.load();
                    Stage stage2 = new Stage();
                    stage2.initModality(Modality.APPLICATION_MODAL);
                    stage2.setScene(new Scene(root2));
                    stage2.show();

                } else {
                    System.out.println("canceled");
                }

            }

        } catch (Exception e) {

            Alert failedLoginAlert = new Alert(Alert.AlertType.ERROR);
            failedLoginAlert.setTitle(failedLoginAlertTitle);
            failedLoginAlert.setHeaderText(failedLoginAlertHeader);
            failedLoginAlert.setContentText(failedLoginAlertContent);
            Log.log(userName, false);
            e.printStackTrace();
            failedLoginAlert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Locale currentLocale = Locale.getDefault();
        String language = currentLocale.getLanguage();
        String country = currentLocale.getCountry();

        String loginTitleText = "Scheduler Database Login";
        String loginUsernameText = "Username:";
        String loginPasswordText = "Password:";
        String loginButtonText = "Login";

            System.out.println("locale, " + currentLocale + ", country, " + country + ", language, " + language);
        
        //I had this set as "GB", as it was the only way I could get the system to change locale or language in ANY way.
        //Going to region and language, and changing the format to English (United Kingdom).
        //My system would not recognize any change otherwise, no matter what I did.
        if (country.equals("ES")) {

            failedLoginAlertTitle = "¡Error de inicio de sesion!";
            failedLoginAlertHeader = "¡Nombre de usuario y / o contraseña incorrectos!";
            failedLoginAlertContent = "Inténtalo de nuevo.";

            SuccessfulLoginAlertTitle = "Acceso Exitoso!";
            SuccessfulLoginAlertHeader = "Bienvenido al sistema del programador de citas.";
            SuccessfulLoginAlertContent = "Las citas se facturan durante una hora y se almacenan en tiempo militar. Buena suerte!";

            loginTitleText = "Planificador";
            loginUsernameText = "Usuario:";
            loginPasswordText = "Contraseña:  ";
            loginButtonText = "Iniciar sesión";

            loginUsernameLabel.setText(loginUsernameText);
            loginPasswordLabel.setText(loginPasswordText);
            loginTitleLabel.setText(loginTitleText);
            loginSubmitButton.setText(loginButtonText);
        }

    }

}
