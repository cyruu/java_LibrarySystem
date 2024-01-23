package subclasses.options;

import mainclass.MainClass;
import subclasses.Login;
import subclasses.Signup;
import subclasses.book.borrow.BorrowBook;
import subclasses.book.editbook.EditBook;
import subclasses.book.removebook.RemoveBook;
import subclasses.signupaccount.SignupAccount;
import subclasses.validinputgetter.GetValidInput;

public class OptionAfterSelect {
    public static void optionAfterSelect() {
        // other options
        // admin options after selecting a book
        if ((MainClass.getUserMode()).equals("admin")) {
            System.out.println("\n\t\t\t1. Edit\t\t\t2. Remove\t\t\t3. Back");
            System.out.println();
            System.out.println("Enter your choice:");
            int choice = GetValidInput.getValidInput(3);
            switch (choice) {
                // edit
                case 1:
                    // System.out.println("edit book");
                    EditBook.editBook();
                    break;
                // remove
                case 2:

                    RemoveBook.removeBook();
                    break;
                case 3:
                    break;

            }
        } else {
            System.out.println("\n\t\t\t1. Borrow\t\t\t2. Back");
            System.out.println();
            System.out.println("Enter your choice:");
            int choice = GetValidInput.getValidInput(2);
            switch (choice) {
                // rent
                case 1:
                    // if member then rent
                    if ((MainClass.getUserMode()).equals("member")) {

                        BorrowBook.borrowBook();
                    } else if ((MainClass.getUserMode()).equals("guest")) {
                        boolean loginSuccess = false;
                        System.out.println("\n\t(Become a member)\n");
                        System.out.println("\t---------");
                        System.out.println("\tSign Up");
                        System.out.println("\t---------");
                        System.out.println("\t1. Create new account\t\t2. Already a member\t\t3. Exit");
                        System.out.println();
                        System.out.println("Enter your choice: ");
                        int signupChoice = GetValidInput.getValidInput(3);
                        boolean signupSuccess = false;
                        boolean directlyViewMenus = false;
                        switch (signupChoice) {
                            // new acc
                            case 1:
                                signupSuccess = SignupAccount.newMemberSignUp();
                                if (signupSuccess) {
                                    System.out.println("Account Created! ");
                                } else {
                                    System.out.println("Failed to create new account!!");
                                    break;
                                }
                                // no break
                            case 2:
                                // login
                                loginSuccess = Login.memberOnlyLogin();
                                if (loginSuccess) {

                                    BorrowBook.borrowBook();
                                }
                                break;
                            case 3:
                                break;

                        }
                    }
                    break;
                // back
                case 2:

                    break;

            }
        }
    }
}
