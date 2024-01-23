public static void main(String[] args) {
        Character continueAsking;
        do {
            int userChoice = displayMenus();
            switch (userChoice) {
                case 1:
                    tempAddBooks();
                    // addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    displayBooks();
                    break;
                case 4:
                    editBook();
                    break;

                default:
                    break;
            }
            System.out.println();
            System.out.println("Do you want to continue[y/n]: ");
            // ask only one character
            // use scan.next().charAt(0);
            continueAsking = scan.next().charAt(0);

        } while (!exited && continueAsking == 'y');

        // end of do while loop
        System.out.println("Exited.");
    }
}
