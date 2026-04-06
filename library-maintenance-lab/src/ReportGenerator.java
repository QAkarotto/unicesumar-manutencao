import Model.Loan;
import Repository.Library;
import Util.DataUtil;

import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {

    // Injetamos a nossa central de dados
    private Library library;

    public ReportGenerator(Library library) {
        this.library = library;
    }

    // 1. A Casca Feia (mas necessária) do relatório simples
    public String generateSimpleReport(String reportName, int mode, String manager, String helper, int yearFilter, String category) {
        // Usamos a Library limpa por baixo dos panos
        int totalBooks = library.getBooks().size();
        int totalUsers = library.getUsers().size();
        int totalLoans = library.getLoans().size();

        return "\n=== " + reportName + " ===\n" +
                "Total de Livros: " + totalBooks + "\n" +
                "Total de Usuários: " + totalUsers + "\n" +
                "Total de Empréstimos: " + totalLoans + "\n";
    }

    // Mantendo exatamente a mesma estrutura da imagem
    public void printSimpleReport() {
        String r = generateSimpleReport("Legacy Repository.Library", 1, "manager", "helper", 0, "ALL");
        System.out.println(r);
    }

    // 2. Contagem de Empréstimos (Refatorada para OOP)
    public Map<String, Integer> countLoansByUser() {
        Map<String, Integer> map = new HashMap<>();

        // Iteramos sobre os nossos objetos Loan tipados
        for (Loan loan : library.getLoans()) {
            String uid = String.valueOf(loan.getUserId());
            Integer c = map.get(uid);

            if (c == null) {
                c = 0;
            }
            c = c + 1;
            map.put(uid, c);
        }
        return map;
    }

    // 3. O Histograma
    public void printLoanHistogram() {
        Map<String, Integer> map = countLoansByUser();

        for (Map.Entry<String, Integer> e : map.entrySet()) {
            // Mantendo a chamada original ao utilitário do sistema legado
            String bar = DataUtil.repeat("#", e.getValue());
            System.out.println("Model.User " + e.getKey() + " -> " + bar);
        }
    }
}