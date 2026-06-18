import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Exemplo de logging baseado no fluxo do laboratorio de biblioteca.
 */
public class LoggingExample {

    private static final Logger logger = LogManager.getLogger(LoggingExample.class);

    public static void main(String[] args) {
        new LoggingExample().demonstrarFluxoDeEmpréstimo();
    }

    public void demonstrarFluxoDeEmpréstimo() {
        logger.info("Iniciando exemplo de logging do laboratorio de biblioteca");

        processarEmpréstimo(3, 1);

        try {
            processarEmpréstimo(0, 2);
        } catch (IllegalStateException exception) {
            logger.error("Falha controlada ao processar emprestimo no exemplo", exception);
        }
    }

    private void processarEmpréstimo(int openCopies, int bookId) {
        logger.info("Processando emprestimo para o livro {} com {} copias abertas", bookId, openCopies);

        if (openCopies <= 0) {
            throw new IllegalStateException("Nao ha copias disponiveis para emprestimo.");
        }

        logger.info("Emprestimo do livro {} processado com sucesso", bookId);
    }
}