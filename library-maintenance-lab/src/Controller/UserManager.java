package Controller;

import Model.User;
import Repository.Library;
import Util.DataUtil;

import java.util.List;

public class UserManager {

    // Injeção de Dependência da nossa central de dados
    private Library library;

    public UserManager(Library library) {
        this.library = library;
    }

    // --- FLUXO DE CADASTRO ---
    public void registerUser(String name, String email, String phone, String userType, String city, String document, String status) {
        try {
            // A responsabilidade de instanciar e guardar é da Library.
            // A responsabilidade de validar (nome/email vazios) está dentro do construtor de User.
            int id = library.addUser(name, email, phone, userType, city, document, status);

            System.out.println("Sucesso! Usuário '" + name + "' cadastrado com o ID: " + id);

        } catch (RuntimeException e) {
            // O Manager apenas captura o grito do construtor e exibe de forma educada no console
            System.out.println("Falha no cadastro de usuário: " + e.getMessage());
        }
    }

    // --- FLUXO DE EXIBIÇÃO ---
    public void listUsers() {
        List<User> users = library.getUsers();

        // Tratamento defensivo igual fizemos no BookManager
        if (users.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado no sistema.");
            return;
        }

        System.out.println("ID | NOME | EMAIL | TIPO | STATUS | DÍVIDA");
        for (User u : users) {
            // Dica de refatoração: Isso poderia virar um u.getSummaryLine() dentro da classe User no futuro
            System.out.println(u.getId() + " | " + u.getName() + " | " + u.getEmail() + " | " +
                    u.getUserType() + " | " + u.getStatus() + " | R$ " + u.getDebt());
        }
    }

    // --- INTERAÇÃO COM O CONSOLE (Opcional, mantendo a estrutura do legado) ---
    public void RegisterUserFromConsole() {
        System.out.println("\n--- NOVO USUÁRIO ---");

        // Assumindo que a classe DataUtil original do legado ainda existe para ler o teclado
        String name = DataUtil.readLine("Nome: ");
        String email = DataUtil.readLine("Email: ");
        String phone = DataUtil.readLine("Telefone: ");
        String type = DataUtil.ask("Tipo (Student/Teacher): ", "Student");
        String city = DataUtil.readLine("Cidade: ");
        String doc = DataUtil.readLine("Documento: ");

        // O status inicial por padrão é sempre "ACTIVE"
        registerUser(name, email, phone, type, city, doc, "ACTIVE");
    }
}