import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LibrarySystemTest {
    private LibrarySystem librarySystem;

    @Before
    public void setUp() {
        // Inicializa o sistema, o que automaticamente carrega os dados e mapeia os comandos
        librarySystem = new LibrarySystem();
        LegacyDatabase.getLogs().clear();
    }

    @Test
    public void testLibrarySystemInitializationDoesNotCrash() {
        // Valida que os gerenciadores internos foram criados com sucesso
        assertNotNull(librarySystem.getBookManager());
        assertNotNull(librarySystem.getUserManager());
        assertNotNull(librarySystem.getLoanManager());
    }

    @Test
    public void testDatabaseSeedingOnConstructor() {
        // Valida se a carga inicial de sementes funcionou conforme o construtor planejava
        assertTrue(LegacyDatabase.getBooks().size() > 0);
        assertTrue(LegacyDatabase.getUsers().size() > 0);
    }
}
