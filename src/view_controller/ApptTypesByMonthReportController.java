
package view_controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ReportDAO;

public class ApptTypesByMonthReportController implements Initializable {

    @FXML
    private TableView numApptTypesByMonthTable;

    @FXML
    private TableColumn monthColumn;

    @FXML
    private TableColumn typeColumn;

    @FXML
    private TableColumn numTypesColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            monthColumn.setCellValueFactory(new PropertyValueFactory<>("Month"));
            numTypesColumn.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
            numApptTypesByMonthTable.setItems(ReportDAO.createTypesByMonthReport());

        } catch (Exception ex) {
            Logger.getLogger(ApptTypesByMonthReportController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

}
