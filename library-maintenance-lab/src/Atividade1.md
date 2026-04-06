# Atividade 1 – Relatório de Manutenção Preventiva

## 1. Identificação e Classificação de Code Smells (10 Problemas)

| Arquivo | Classe/Método | Code Smell | Classificação (Categoria) | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `LegacyDatabase` | Campos `Map<String, Object>` | **Primitive Obsession** | Abusadores de OO | Uso de Mapas genéricos para emular objetos (livros e usuários), quebrando a tipagem forte (Type Safety). |
| `BookManager` | `registerBook(...)` | **Anemic Domain Model** | Abusadores de OO | Validação de negócio (ex: checar se título é branco) feita no Manager, enquanto os dados não têm inteligência. |
| `LibrarySystem` | Classe inteira | **God Class** | Inchadores (Bloaters) | Centraliza menu, orquestração, entrada de dados e testes na mesma classe. |
| `LibrarySystem` | `handleDebugArea()` | **Long Method / Deep Nesting** | Inchadores / Abusadores | Método excessivamente longo com cadeia de `if/else` em vários níveis, dificultando a leitura. |
| `BookManager` | `updateAvailableWith...` | **Feature Envy** | Acopladores (Couplers) | O Manager acessa os dados de cópias do livro, faz o cálculo matemático fora dele e injeta o valor de volta. |
| `LegacyDatabase` | Toda a classe | **Hidden Global State** | Obstrutores de Mudança | Variáveis estáticas públicas (`books`, `users`) criando estado global não encapsulado. |
| `BookManager` | Validações de entrada | **Duplicate Code** | Os Dispensáveis | Regras de validação (como `isBlank`) repetidas desnecessariamente ao longo de vários métodos. |
| `DataUtil` | `datePlusDaysApprox()` | **Primitive Obsession** | Abusadores de OO | Cálculo de data feito via concatenação de strings (`date + " +" + days`) ao invés de usar APIs de tempo. |
| `LegacyDatabase` | `unsafeUpdateBookField()`| **Encapsulation Violation** | Abusadores de OO | Atualizações dinâmicas feitas via String que ignoram qualquer regra de validação do domínio. |
| `BookManager` | `findBooksByCategory...` | **Mystery Guests / Magic Numbers** | Inchadores (Bloaters) | Uso de variáveis com nomes sem sentido (`x`, `y`, `z`) e limites numéricos fixos não documentados. |

## 2. Refatorações Aplicadas (5 Técnicas)

As refatorações foram aplicadas de forma incremental, garantindo que o comportamento do sistema no terminal permanecesse idêntico ao original.

1. **Extract Class (Extração de Classe):** Criação das classes de domínio `Book`, `User` e `Loan` para substituir a estrutura ineficiente de `Map<String, Object>`.
2. **Self-Validating Constructor (Construtor Auto-Validável):** Migração das lógicas de validação (ex: campos obrigatórios) dos Managers diretamente para os construtores das novas entidades (`Book` e `User`), implementando o padrão *Fail-Fast*.
3. **Move Method (Movimentação de Método):** O controle de estoque (`availableCopies`) foi retirado do `BookManager` e encapsulado dentro da classe `Book` através dos métodos `borrowCopy()` e `returnCopy()`.
4. **Replace Global Data with Dependency Injection:** Exclusão completa das variáveis estáticas globais do `LegacyDatabase`. Criação da classe `Library` (Repositório) instanciada na `LibrarySystem` e injetada via construtor nos Managers.
5. **Consolidate Conditional Expression / Replace with API:** Substituição da lógica arcaica de concatenação de datas no `DataUtil` pelo uso da API moderna `java.time.LocalDate`, mantendo um fallback (`try/catch`) para evitar quebras em entradas legadas fora do padrão.