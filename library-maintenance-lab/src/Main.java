public class Main {

    public static void main(String[] args) {

        LibrarySystem system = new LibrarySystem();

        System.out.println("Starting legacy library system...");
        System.out.println("Mode: " + LegacyDatabase.getSystemMode());

        system.runDemoScenario();

        if (handleArguments(system, args)) {
            return;
        }

        system.startCli();
    }

    private static boolean handleArguments(LibrarySystem system, String[] args) {
        if (args == null || args.length == 0) return false;

        switch (args[0]) {
            case "--report":
                System.out.println(system.getReportGenerator()
                        .generateSimpleReport("Startup Report", 1, "main", "helper", 0, ""));
                return true;

            case "--list":
                system.handleListBooks();
                system.handleListUsers();
                system.handleListLoans();
                return true;

            default:
                return false;
        }
    }
}