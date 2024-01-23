package subclasses.book.searchbook;

import subclasses.book.viewbook.ChooseBook;
import subclasses.options.OptionAfterSelect;
import subclasses.validinputgetter.GetValidInput;

public class SearchBook {
    public static void searchBook() {
        ChooseBook.bookChoosed = false;
        int searchChoice;
        System.out.println();
        System.out.println("\t----------");
        System.out.println("\tSearch by");
        System.out.println("\t----------");
        System.out.println();
        System.out
                .println("\t1. ISBN\t\t2. Title\t\t3. Genre\n\t4. Author\t5. Published year\t6. Back");
        System.out.println();
        System.out.println("Enter your choice: ");
        searchChoice = GetValidInput.getValidInput(6);
        switch (searchChoice) {
            // search by isbn
            case 1:

                SearchByIsbn.searchByIsbn();

                break;
            // title
            case 2:
                SearchByTitle.searchByTitle();

                break;

            // genre
            case 3:
                SearchByGenre.searchByGenre();

                break;
            // author
            case 4:
                SearchByAuthor.searchByAuthor();
                // ChooseBook.chooseBook();

                break;
            // published year
            case 5:
                SearchByPublishedYear.searchByPublishedYear();

                break;
            // back
            case 6:
                break;
        }
        // rent option after only choosing book
        // if (ChooseBook.bookChoosed) {

        // OptionAfterSelect.optionAfterSelect();

        // }
    }
}
