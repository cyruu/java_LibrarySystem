package subclasses.signupaccount;

import java.sql.*;
import java.util.Scanner;

import subclasses.dbconnect.GetJdbcConnection;

public class SignupAccount {
    public static boolean newMemberSignUp() {
        boolean signupSuccess = false;
        boolean accountValid = false;
        String inputUsername;
        String inputPassword;
        char retry = 'n';
        String inputConfirmPassword;
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Clear input buffer [any character]:");
        do {
            scanner.nextLine();
            System.out.println();
            System.out.println("--------------------");
            System.out.println("Create new account ");
            System.out.println("--------------------");
            System.out.println("Enter username: ");
            inputUsername = scanner.nextLine();
            System.out.println("Enter password:");
            inputPassword = scanner.nextLine();
            System.out.println("Confirm password:");
            inputConfirmPassword = scanner.nextLine();
            // compare pass and confirm pass
            // if same pass then check username in databse
            if (inputPassword.equals((inputConfirmPassword)) && inputPassword != "" && inputConfirmPassword != ""
                    && inputUsername != "") {

                try {
                    String userExistQuery = "Select count(*) from users where username='" + inputUsername + "'";
                    Connection signUpCon = GetJdbcConnection.getConnection();
                    Statement userExistSt = signUpCon.createStatement();
                    ResultSet userExistRs = userExistSt.executeQuery(userExistQuery);
                    userExistRs.next();
                    int userExist = userExistRs.getInt(1);
                    // user not in database then add to db (users) as a member
                    if (userExist == 0) {
                        int row = 0;
                        String addUserQuery = "insert into users (username,password) values ('" + inputUsername + "','"
                                + inputPassword + "')";

                        Statement addUserSt = signUpCon.createStatement();
                        row = addUserSt.executeUpdate(addUserQuery);
                        // if row ==1 then inserted
                        if (row == 1) {
                            accountValid = true;
                            signupSuccess = true;
                            System.out.println();
                            System.out.println("Sign up successful! (" + inputUsername + ")");
                        } else {
                            System.out.println();
                            System.out.println("Failed!! Sign up unsuccessful!");

                        }
                    }
                    // user already exists
                    else {
                        System.out.println();
                        System.out.println("Failed! Username (" + inputUsername + ") already exists!");

                    }
                    signUpCon.close();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                if (inputPassword == "" || inputConfirmPassword == "") {

                    System.out.println();
                    System.out.println("Failed!! Passwords can't be empty!!");
                } else if (inputUsername == "") {

                    System.out.println();
                    System.out.println("Failed!! Username did cant'be empty!!");
                } else {
                    System.out.println();
                    System.out.println("Failed!! Passwords did not match!!");

                }

            }
            if (!accountValid) {
                System.out.println();
                System.out.println("Retry? [y/n]: ");
                retry = scanner.next().charAt(0);
            }
        } while (!accountValid && retry == 'y');
        return signupSuccess;
    }

    public static void addNewAdmin() {
        boolean accountValid = false;
        String inputUsername;
        String inputPassword;
        char retry = 'n';
        String inputConfirmPassword;
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Clear input buffer [any character]:");
        do {
            scanner.nextLine();
            System.out.println();
            System.out.println("------------------------");
            System.out.println("Create new admin account ");
            System.out.println("------------------------");
            System.out.println("Enter admin username: ");
            inputUsername = scanner.nextLine();
            System.out.println("Enter password:");
            inputPassword = scanner.nextLine();
            System.out.println("Confirm password:");
            inputConfirmPassword = scanner.nextLine();
            // compare pass and confirm pass
            // if same pass then check username in databse
            if (inputPassword.equals((inputConfirmPassword)) && inputPassword != "" && inputConfirmPassword != ""
                    && inputUsername != "") {

                try {
                    String userExistQuery = "Select count(*) from users where username='" + inputUsername + "'";
                    Connection signUpCon = GetJdbcConnection.getConnection();
                    Statement userExistSt = signUpCon.createStatement();
                    ResultSet userExistRs = userExistSt.executeQuery(userExistQuery);
                    userExistRs.next();
                    int userExist = userExistRs.getInt(1);
                    // user not in database then add to db (users) as a admin
                    if (userExist == 0) {
                        int row = 0;
                        String addadminQuery = "insert into users (username,password,isAdmin) values ('" + inputUsername
                                + "','"
                                + inputPassword + "',true)";

                        Statement addadminSt = signUpCon.createStatement();
                        row = addadminSt.executeUpdate(addadminQuery);
                        // if row ==1 then inserted
                        if (row == 1) {
                            accountValid = true;

                            System.out.println();
                            System.out.println("Success! New admin (" + inputUsername + ") added!");
                        } else {
                            System.out.println();
                            System.out.println("Failed!! Ne admin sign up unsuccessful!");

                        }
                    }
                    // user already exists
                    else {
                        System.out.println();
                        System.out.println("Failed! Username (" + inputUsername + ") already exists!");

                    }
                    signUpCon.close();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                if (inputPassword == "" || inputConfirmPassword == "") {

                    System.out.println();
                    System.out.println("Failed!! Passwords can't be empty!!");
                } else if (inputUsername == "") {

                    System.out.println();
                    System.out.println("Failed!! Username did cant'be empty!!");
                } else {
                    System.out.println();
                    System.out.println("Failed!! Passwords did not match!!");

                }

            }
            if (!accountValid) {
                System.out.println();
                System.out.println("Retry? [y/n]: ");
                retry = scanner.next().charAt(0);
            }
        } while (!accountValid && retry == 'y');

    }
}
