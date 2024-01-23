package subclasses;

import java.util.Scanner;

import subclasses.signupaccount.SignupAccount;
import subclasses.validation.InputNumberValidation;
import subclasses.validinputgetter.GetValidInput;

public class Signup {
    public static boolean signup() {
        boolean signupSuccess = false;
        int signupChoice;

        System.out.println("\t---------");
        System.out.println("\tSign Up");
        System.out.println("\t---------");
        System.out.println("\t1. Create new account\t\t2. Already a member\t\t3. Exit");
        System.out.println();
        System.out.println("Enter your choice: ");
        signupChoice = GetValidInput.getValidInput(3);
        // do {

        // signupChoice = scan.nextInt();
        // signUpValid = InputNumberValidation.isInputNumberValid(signupChoice, 3);
        // if (signUpValid) {
        // break;
        // } else {
        // System.out.println("Enter valid choice:");
        // }
        // } while (true);

        switch (signupChoice) {
            // create new account
            case 1:
                // return true if new account successfully created
                signupSuccess = SignupAccount.newMemberSignUp();
                if (signupSuccess) {
                    signupSuccess = true;

                }
                break;
            // already have account
            // direct to login menu
            case 2:
                signupSuccess = true;
                break;
            case 3:
                break;

        }
        return signupSuccess;
    }

}
