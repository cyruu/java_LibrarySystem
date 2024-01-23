
import java.sql.*;

import subclasses.dbconnect.GetJdbcConnection;

public class NewAccountValidation {
    public static boolean canThisAccountBeCreated(String username, String password) {
        boolean accountValid = false;
        // String userNameDb = "notindatabase";

        try {
            String userExistQuery = "Select count(*) from users where username='" + username + "'";
            Connection signUpCon = GetJdbcConnection.getConnection();
            Statement userExistSt = signUpCon.createStatement();
            ResultSet userExistRs = userExistSt.executeQuery(userExistQuery);
            userExistRs.next();
            int userExist = userExistRs.getInt(1);
            // user not in database then add to db (users) as a member
            if (userExist == 0) {
                String addUserQuery = "insert into users (username,password) values ('" + username + "','" + password
                        + "')";

                Statement addUserSt = signUpCon.createStatement();
                int row = addUserSt.executeUpdate(addUserQuery);
                // if row ==1 then inserted
                if (row == 1) {
                    System.out.println();
                }

            }
            // user already exists
            else {
                System.out.println();
                System.out.println("Failed! Username (" + username + ") already exists!");

            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        if (username.equals("notindatabase")) {
            if (password.equals(confirmPassword)) {
                accountValid = true;
            } else {
                System.out.println("Passwords dont match !");
            }
        } else {
            System.out.println("Username already exists !");
        }
        System.out.println();
        return accountValid;
    }
}
