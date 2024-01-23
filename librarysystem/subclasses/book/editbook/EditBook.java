package subclasses.book.editbook;

import subclasses.book.searchbook.SearchByIsbn;
import subclasses.validinputgetter.GetValidInput;

public class EditBook {
    public static void editBook() {
        System.out.println();
        System.out.println("------------");
        System.out.println("Edit book");
        System.out.println("------------");
        System.out
                .println("\t1. Title\t\t2. Genre\t\t3. Author\n\t4. Published year\t5. Copies\t\t6. Back");
        System.out.println();
        System.out.println("Enter your choice: ");
        int editChoice = GetValidInput.getValidInput(6);
        switch (editChoice) {
            // edit title
            case 1:
                EditTitle.editTitle();
                break;
            // genre
            case 2:
                EditGenre.editGenre();
                break;
            // author
            case 3:
                EditAuthor.editAuthor();
                break;
            // published year
            case 4:
                EditPublishedYear.editYear();
                break;
            // copies
            case 5:
                EditCopies.editCopies();
                break;
            // back
            case 6:
                break;
        }
    }

}
