package Controller;

import Model.Book;
import Repository.Library;
import Util.DataUtil;

import java.util.List;

public class BookManager {

    // Agora o Manager recebe a Library para poder trabalhar
    private Library library;

    public BookManager(Library library) {
        this.library = library;
    }

    // O Manager cuida APENAS da interação com a tela do usuário
    public void registerBookFromConsole() {
        System.out.println("--- NOVO LIVRO ---");
        String title = DataUtil.readLine("Title: ");
        String author = DataUtil.readLine("Author: ");
        int year = DataUtil.askInt("Year: ", 2000);
        String category = DataUtil.ask("Category: ", "GENERAL");
        int total = DataUtil.askInt("Total copies: ", 1);
        int avail = DataUtil.askInt("Available copies: ", total);
        String shelf = DataUtil.ask("Shelf: ", "X0");
        String isbn = DataUtil.ask("ISBN: ", "NO-ISBN");

        try {
            // Tenta adicionar na Library. Se o usuário digitou título vazio, o construtor do Book vai berrar aqui.
            int id = library.addBook(title, author, year, category, total, avail, shelf, isbn);
            System.out.println("Sucesso! Livro salvo com ID: " + id);

        } catch (IllegalArgumentException e) {
            // O Manager é o escudo protetor. Ele pega a exceção e mostra bonito na tela.
            System.out.println("Falha no cadastro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    // O Manager pede pra Library listar os livros
    public void listBooksSimple() {
        List<Book> books = library.getBooks();

        // O Bug resolvido da Atividade 2 continua aqui, protegendo a tela
        if (books.isEmpty()) {
            System.out.println("Nenhum livro cadastrado na biblioteca.");
            return;
        }

        System.out.println("ID | TITLE | AUTHOR | Y | CAT | AV");
        for (Book b : books) {
            System.out.println(b.getId() + " | " + b.getTitle() + " | " + b.getAuthor() + " | " + b.getYear() + " | " + b.getCategory() + " | " + b.getAvailableCopies());
        }
    }
}