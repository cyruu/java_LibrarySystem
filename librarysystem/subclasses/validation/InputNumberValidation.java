package subclasses.validation;

import mainclass.MainClass;

public class InputNumberValidation {
    // check input for display menus
    public static boolean isInputNumberValid(int menuChoice) {
        boolean inputValid = false;
        String userMode = MainClass.getUserMode();
        // guest mode check input number
        if (userMode.equals("guest")) {
            // availabe 1-5 choices
            // check if input belongs
            if (menuChoice >= 1 && menuChoice <= 5) {
                inputValid = true;

            }
        }
        // admin mode check input number
        if (userMode.equals("admin")) {
            // availabe 1-7 choices
            // check if input belongs
            if (menuChoice >= 1 && menuChoice <= 7) {
                inputValid = true;

            }
        }
        // guest mode check input number
        if (userMode.equals("member")) {
            // availabe 1-5 choices
            // check if input belongs
            if (menuChoice >= 1 && menuChoice <= 5) {
                inputValid = true;

            }
        }
        // return if input valid or not
        return inputValid;
    }

    // check input for options inside any menu
    public static boolean isInputNumberValid(int menuChoice, int lastChoiceNumber) {
        boolean inputValid = false;
        if (menuChoice >= 1 && menuChoice <= lastChoiceNumber) {
            inputValid = true;
        }
        return inputValid;
    }
}
