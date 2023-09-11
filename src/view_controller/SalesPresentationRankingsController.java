
package view_controller;

import java.net.URL;
import java.sql.SQLException;
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
import model.ReportDAO;

public class SalesPresentationRankingsController implements Initializable {

    @FXML
    private TableView apptTableView;

    @FXML
    private TableColumn consultantColumn;

    @FXML
    private TableColumn numSalesPresentationsColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        try {
            consultantColumn.setCellValueFactory(new PropertyValueFactory<>("User"));
            numSalesPresentationsColumn.setCellValueFactory(new PropertyValueFactory<>("NumSalesPresentations"));
            apptTableView.setItems(ReportDAO.createSalesPresentationRankingsReport());
            apptTableView.getSortOrder().add(numSalesPresentationsColumn);
            numSalesPresentationsColumn.setSortType(TableColumn.SortType.DESCENDING);
        } catch (SQLException ex) {
            Logger.getLogger(SalesPresentationRankingsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SalesPresentationRankingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
