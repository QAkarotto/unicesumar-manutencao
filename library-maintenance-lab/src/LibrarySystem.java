public class LibrarySystem {

    private BookManager bookManager = new BookManager();
    private boolean running = true;

    public void startCli() {
        DataUtil.printHeader("Library System");

        while (running) {
            showMenu();
            String option = DataUtil.readLine("Option: ");

            switch (option) {
                case "1":
                    registerBook();
                    break;
                case "2":
                    listBooks();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n1 - Register Book");
        System.out.println("2 - List Books");
        System.out.println("0 - Exit");
    }

    private void registerBook() {
        try {
            String title = DataUtil.readLine("Title: ");
            String author = DataUtil.readLine("Author: ");
            int year = DataUtil.askInt("Year: ", 2000);
            String category = DataUtil.ask("Category: ", "GENERAL");
            int total = DataUtil.askInt("Total copies: ", 1);
            int available = DataUtil.askInt("Available copies: ", total);

            int id = bookManager.registerBook(
                    title, author, year, category,
                    total, available, "X0", "NO-ISBN"
            );

            System.out.println("Book created: " + id);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listBooks() {
        bookManager.listBooksSimple();
    }
}