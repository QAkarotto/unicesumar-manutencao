public class Main {

    public static void main(String[] args) {
        // Inicializa o nosso novo sistema orientado a objetos
        LibrarySystem system = new LibrarySystem();

        System.out.println("Starting legacy library system...");
        // Como o LegacyDatabase não existe mais, adaptamos a mensagem
        System.out.println("Mode: Refactored OOP");

        // sample usage before interactive mode
        // This simulates old startup behavior
        system.runDemoScenario();

        // Tratamento dos argumentos exigidos na Atividade 2
        if (args != null && args.length > 0) {
            if ("--report".equals(args[0])) {
                String report = system.getReportGenerator().generateSimpleReport("Startup Report", 1, "main", "helper", 0, "");
                System.out.println(report);
                return;
            }
            if ("--list".equals(args[0])) {
                system.handleListBooks();
                system.handleListUsers();
                system.handleListLoans();
                return;
            }
        }

        // Se rodar sem argumentos, abre o Menu Interativo
        system.startCli();
    }
}
