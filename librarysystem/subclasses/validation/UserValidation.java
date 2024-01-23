package subclasses.validation;

import java.sql.*;
import subclasses.dbconnect.GetJdbcConnection;

public class UserValidation {
    public static boolean isUserValid(String username, String password, String loginAs) {
        boolean userValid = false;
        try {

            Connection userValidatinCon = GetJdbcConnection.getConnection();
            Statement userValidationSt = userValidatinCon.createStatement();
            // if login as admin use admin users data from database
            if (loginAs.equals("admin")) {
                String memberExistQuery = "select count(*) from users where username='" + username
                        + "' and isAdmin=true";

                ResultSet memberExistRs = userValidationSt.executeQuery(memberExistQuery);
                memberExistRs.next();
                int memberExist = memberExistRs.getInt(1);
                // member found in database then check password
                if (memberExist == 1) {
                    // check pass of the username
                    String memPassQuery = "select password from users where username='" + username + "'";
                    ResultSet memPassRs = userValidationSt.executeQuery(memPassQuery);
                    memPassRs.next();
                    String memPass = memPassRs.getString(1);
                    // password match
                    if (memPass.equals(password)) {
                        userValid = true;
                        System.out.println();
                        System.out.println("Succssfully logged in as (" + username + ")");
                    }
                    // pass dont match with db
                    else {

                        System.out.println();
                        System.out.println("Incorrect Password!!");
                    }
                }
                // member not found
                else {
                    System.out.println();
                    System.out.println("Invalid Username!!");
                }
            }
            // if login as member use members users data from database
            if (loginAs.equals("member")) {

                String memberExistQuery = "select count(*) from users where username='" + username
                        + "' and isAdmin=false";

                ResultSet memberExistRs = userValidationSt.executeQuery(memberExistQuery);
                memberExistRs.next();
                int memberExist = memberExistRs.getInt(1);
                // member found in database then check password
                if (memberExist == 1) {
                    // check pass of the username
                    String memPassQuery = "select password from users where username='" + username + "'";
                    ResultSet memPassRs = userValidationSt.executeQuery(memPassQuery);
                    memPassRs.next();
                    String memPass = memPassRs.getString(1);
                    // password match
                    if (memPass.equals(password)) {
                        userValid = true;
                        System.out.println();
                        System.out.println("Succssfully logged in as (" + username + ")");
                    }
                    // pass dont match with db
                    else {

                        System.out.println();
                        System.out.println("Incorrect Password!!");
                    }
                }
                // member not found
                else {
                    System.out.println();
                    System.out.println("Invalid Username!!");
                }
            }
            userValidatinCon.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return userValid;
    }
}
