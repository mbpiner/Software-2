package model;

import com.mysql.cj.xdevapi.Table;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static model.Main.filePath;
import model.Query;
import view_controller.LoginController; //06/24/2023 put this here for globalusername/id

/**
 *
 * @author Mason
 */
public class ReportDAO {

    public static ObservableList<Report> reportList = FXCollections.observableArrayList();
    public static ObservableList<salesPresentationRankingsReport> salesPresentationRankingsReportList = FXCollections.observableArrayList();

    public static int[] appointmentArray = new int[5];
    public static int apptTypeIndex = 0;
    public static int monthIndex = 1;
    public static String typeNumStr;

    public static ObservableList<Report> createTypesByMonthReport() throws ClassNotFoundException, SQLException, Exception {

        String[] appointmentTypes = {"Follow-Up", "Initial Consultation", "Sales Presentation", "Product Support", "General Consultation"};
        String[] months = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};

        for (String month : months) {
            apptTypeIndex = 0;
            for (String appointmentType : appointmentTypes) {
                String apptTypesByMonthQuery = "SELECT count(type), contact from appointment where extract(month from time) =" + monthIndex + " and appointment.type = '" + appointmentType + "'";
                Query.makeQuery(apptTypesByMonthQuery);
                ResultSet rs = Query.getResult();
                rs.next();
                if (rs.getInt("count(type)") > 0) {
                    typeNumStr = String.valueOf(rs.getInt("count(type)"));
                    Report report = new Report(month, appointmentType, typeNumStr);
                    reportList.add(report);
                }
                apptTypeIndex++;
            }

            monthIndex++;

        }

        return reportList;
    }

   public static ObservableList<salesPresentationRankingsReport> createSalesPresentationRankingsReport() throws ClassNotFoundException, SQLException, Exception {

        salesPresentationRankingsReportList.clear();
        for (int i = 1; i < 5; i++) {
            String salesPresentationsQuery = "SELECT count(type), contact, userId from appointment where appointment.userId = " + i + " AND appointment.type = 'Sales Presentation'";

            Query.makeQuery(salesPresentationsQuery);
            ResultSet rs = Query.getResult();
            rs.next();
            if (rs.getInt("count(type)") > 0) {
                typeNumStr = String.valueOf(rs.getInt("count(type)"));
                salesPresentationRankingsReport report = new salesPresentationRankingsReport(rs.getString("contact"), typeNumStr);
                salesPresentationRankingsReportList.add(report);
            }
        }
        return salesPresentationRankingsReportList;
    }

}