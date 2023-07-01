package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mason
 */
public class Report {
    public String month;
    public String type;
    public String amount;

    public Report() {

    }

    public Report(String month, String type, String amount) {
        this.month = month;
        this.type = type;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

  
}
