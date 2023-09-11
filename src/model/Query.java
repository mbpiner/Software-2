package model;

import java.sql.*;

public class Query {

    static String query;
    static Statement stmt;
    static ResultSet result;
    static int updateResult;

    public static void makeQuery(String q) {
        query = q;

        try {
            stmt = DB.conn.createStatement();
            if (query.toLowerCase().startsWith("select")) {
                System.out.println("select");
                result = stmt.executeQuery(query);
            } else {
                System.out.println("else. no select.");
                updateResult = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
                result = stmt.getGeneratedKeys();
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    public static ResultSet getResult() {
        return result;
    }

    public static String getCountryGivenCity(String city) throws SQLException {

        String countryQuery = "SELECT country.country FROM country inner join city on city.countryId = country.countryId WHERE city.city = '" + city + "'";
        makeQuery(countryQuery);
        ResultSet country = getResult();
        while (country.next()) {
            return country.getString("country");
        }
        return "";
    }

}
