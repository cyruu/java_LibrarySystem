package subclasses.calculateUserChoice;

import mainclass.MainClass;

public class CalculateUserChoice {
    public static int getEquivalentChoiceNumber(int menuChoice) {
        int equivalentChoiceNumber = 0;
        // if in guest mode
        // set exit to 9
        if (MainClass.getUserMode() == "guest") {
            // 1viewbooks 2searchbook 3login 4signup 5 exit
            switch (menuChoice) {
                case 5:
                    menuChoice = 9;
                    break;
                default:
                    break;
            }
        }
        // if user mode is admin
        // set addbook=7 removebook=8 exit=9
        if (MainClass.getUserMode() == "admin") {
            // 3addbook 4removebook 5changeprivacy 6logout 7. add new admin 8exit
            switch (menuChoice) {
                case 3:
                    menuChoice = 7;
                    break;
                case 4:
                    menuChoice = 8;
                    break;
                // new admin add
                case 7:
                    menuChoice = 12;
                    break;
                case 8:
                    menuChoice = 9;
                    break;
            }
        }

        // if user mode is member
        // set changepriv=5 logout=6 exit=9
        if (MainClass.getUserMode() == "member") {
            // 3borrowedbooks 4return books 5change privacy 6logout 7exit
            switch (menuChoice) {
                // borrowed
                case 3:
                    menuChoice = 10;
                    break;
                // return books
                case 4:
                    menuChoice = 11;
                    break;
                // privacy
                case 5:
                    menuChoice = 5;
                    break;
                // logout
                case 6:
                    menuChoice = 6;
                    break;
                // exit
                case 7:
                    menuChoice = 9;
                    break;
            }
        }
        // return converted number
        equivalentChoiceNumber = menuChoice;
        return equivalentChoiceNumber;
    }

    public static int noPrevMenu(int menuChoice) {
        switch (menuChoice) {
            case 1:
                menuChoice = 2;
                break;
            case 2:
                menuChoice = 3;
                break;
            case 3:
                menuChoice = 4;
                break;
        }
        return menuChoice;

    }

    public static int noNextMenu(int menuChoice) {
        switch (menuChoice) {
            case 1:
                menuChoice = 1;
                break;
            case 2:
                menuChoice = 3;
                break;
            case 3:
                menuChoice = 4;
                break;
        }
        return menuChoice;

    }
}
