package subclasses.book.viewbook;

import subclasses.book.searchbook.SearchByIsbn;
import subclasses.validinputgetter.GetValidInput;

public class ChooseBook {
    public static boolean bookChoosed = false;

    public static void chooseBook() {
        ChooseBook.bookChoosed = false;
        // select book
        System.out.println();
        System.out.println("\t\t 1. Choose book\t\t\t2. Back");
        System.out.println("\nEnter your choice: ");
        int choice = GetValidInput.getValidInput(2);

        switch (choice) {
            // choose book
            case 1:
                SearchByIsbn.searchByIsbn();
                break;
            // back
            case 2:
                break;
        }
    }
}
