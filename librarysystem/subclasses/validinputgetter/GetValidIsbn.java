package subclasses.validinputgetter;

import java.sql.*;
import java.util.Scanner;

import subclasses.dbconnect.GetJdbcConnection;

public class GetValidIsbn {
    public static int getValidIsbn() {
        Scanner scanner = new Scanner(System.in);
        int validIsbn = 0;
        try {
            Connection con = GetJdbcConnection.getConnection();
            Statement st = con.createStatement();
            do {

                // generate random number from 100 to 1000
                int randomValidIsbn = (int) Math.floor((Math.random() * 399) + 101);
                // query to check if random isbn is already registered
                String isbnExistQuery = "select count(*) from librarybooks where bookIsbn=" + randomValidIsbn;
                ResultSet isbnExistRs = st.executeQuery(isbnExistQuery);
                isbnExistRs.next();
                int isbnFound = isbnExistRs.getInt(1);
                // isbn not in databse, can be added
                if (isbnFound == 0) {
                    validIsbn = randomValidIsbn;
                    break;
                }

            } while (true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return validIsbn;
    }
}
