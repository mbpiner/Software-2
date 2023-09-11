package model;

public class salesPresentationRankingsReport {

    public String user;
    public String numSalesPresentations;

    public salesPresentationRankingsReport() {

    }

    public salesPresentationRankingsReport(String user, String numSalesPresentations) {
        this.numSalesPresentations = numSalesPresentations;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNumSalesPresentations() {
        return numSalesPresentations;
    }

    public void setNumSalesPresentations(String numSalesPresentations) {
        this.numSalesPresentations = numSalesPresentations;
    }

}
