package subclasses.validinputgetter;

import java.util.Scanner;

import subclasses.validation.InputNumberValidation;

public class GetValidInput {
    public static int getValidInput(int lastChoiceNumber) {
        Scanner scan = new Scanner(System.in);
        int inputChoice;
        boolean inputValid = false;
        do {

            inputChoice = scan.nextInt();
            inputValid = InputNumberValidation.isInputNumberValid(inputChoice, lastChoiceNumber);
            if (inputValid) {
                break;
            } else {
                System.out.println("Enter valid choice:");
            }
        } while (true);
        return inputChoice;
    }
}
