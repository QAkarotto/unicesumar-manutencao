# Relatório de Manutenção Preventiva - Atividade 1

Este documento detalha as análises e refatorações realizadas no sistema legado de biblioteca como parte da Atividade 1.

## 1. Problemas de Manutenibilidade Identificados

Foram identificados os seguintes problemas (*code smells*) no sistema:

| Arquivo | Local | Code Smell | Descrição |
| --- | --- | --- | --- |
| `LibrarySystem.java` | Classe | **God Class** | Centraliza orquestração, UI, instanciação de dependências e lógica de debug. |
| `LoanManager.java` | `borrowBook` | **Long Method** | Método com muitas responsabilidades (validação, persistência, notificação, log). |
| `LoanManager.java` | `borrowBook` | **Deep Nesting** | Múltiplos níveis de `if` encadeados dificultando a leitura. |
| `LoanManager.java` | Atributo | **Tight Coupling** | Instanciação direta de `NotificationService` dificultando testes unitários. |
| `BookManager.java` | `listBooksSimple` | **Edge Case Bug** | Tentativa de acessar `temp.get(0)` em lista vazia causa crash. |
| `ReportGenerator.java` | `totalLoans` | **Magic Number** | Ajuste fixo + 1 sem explicação de negócio. |
| `ReportGenerator.java` | `closedLoans` | **Logic Bug** | Contador incrementa para todos os empréstimos, ignorando o status. |
| `LegacyDatabase.java` | `countOpenLoansByBook` | **Naming/Logic Inconsistency** | Nome sugere filtro por livro, mas implementa filtro por usuário. |
| `LoanManager.java` | `returnBook` | **Primitive Obsession** | Uso de `int forceFlag` para controlar lógica de multa em vez de polimorfismo ou enums. |
| `DataUtil.java` | `datePlusDaysApprox` | **Fragile Logic** | Cálculo de data via concatenação de strings em vez de API de data. |

## 2. Refatorações Realizadas

Foram aplicadas as seguintes refatorações para melhorar a manutenibilidade sem alterar o comportamento externo. Abaixo, detalhamos o estado anterior (problema) e a solução aplicada para cada caso:

### 2.1. Extract Method em `LoanManager.java` (Método `borrowBook`)

> **Antes:** O método `borrowBook` era um "Long Method" que concentrava múltiplas responsabilidades: validação de regras de negócio (usuário ativo, limite de dívida, cópias disponíveis), atualização de estoque, criação do registro de empréstimo e geração de logs de política.

> **Depois:** O método foi decomposto em métodos menores e mais coesos. Criamos os métodos privados `validateBorrow` (para isolar as regras de negócio), `updateBookStock` (para centralizar a lógica de incremento/decremento de cópias) e `logLoanPolicy` (para isolar a lógica de auditoria). Isso tornou o método principal muito mais limpo e focado apenas na orquestração do fluxo.

### 2.2. Introduce Guard Clauses em `LoanManager.java` (Método `borrowBook`)

> **Antes:** As validações iniciais do empréstimo utilizavam um "Deep Nesting" (múltiplos `if` encadeados, como `if (user != null) { if (book != null) { if ("ACTIVE".equals(...)) { ... } } }`). Isso empurrava o "caminho feliz" do código para a direita, dificultando a leitura e a manutenção.

> **Depois:** Substituímos os `if` encadeados por "Guard Clauses" (retornos antecipados) dentro do novo método `validateBorrow`. Agora, o código verifica as condições de falha primeiro e lança exceções imediatamente (ex: `if (user == null) throw new RuntimeException(...);`). O caminho feliz ficou alinhado à margem esquerda, reduzindo drasticamente a complexidade cognitiva.

### 2.3. Dependency Injection em `LoanManager.java`

> **Antes:** A classe `LoanManager` possuía um "Tight Coupling" (alto acoplamento) com a classe `NotificationService`, instanciando-a diretamente em seu atributo (`private NotificationService notificationService = new NotificationService();`). Isso tornava impossível testar o `LoanManager` isoladamente sem disparar notificações reais.

> **Depois:** Aplicamos a Injeção de Dependência adicionando construtores à classe `LoanManager`. Agora, a classe pode receber uma instância de `NotificationService` via construtor (`public LoanManager(NotificationService notificationService)`), permitindo que dependências falsas (*mocks*) sejam injetadas durante testes unitários.

### 2.4. Extract Method em `BookManager.java` (Método `registerBook`)

> **Antes:** O método `registerBook` misturava a lógica de persistência com várias regras de normalização e validação de dados (ex: tratar título em branco, definir ano padrão se negativo, definir categoria padrão).

> **Depois:** Extraímos essas regras de validação e normalização para métodos privados específicos, como `normalizeTitle`, `validateAuthor` e `normalizeYear`. Isso melhorou a legibilidade do método principal, que agora apenas chama os validadores e delega a persistência para o banco de dados.

### 2.5. Fix Edge Case em `BookManager.java` (Método `listBooksSimple`)

> **Antes:** O método tentava acessar o primeiro elemento de uma lista temporária (`temp.get(0)`) sem verificar se a lista estava vazia. Se não houvesse livros cadastrados, o sistema lançaria uma exceção `IndexOutOfBoundsException`, causando um crash.

> **Depois:** Adicionamos uma verificação simples (`if (temp.isEmpty())`) que faz um "early return" e imprime uma mensagem amigável ("No books found.") caso não existam livros, prevenindo a falha do sistema.

### 2.6. Consolidação de Lógica em `BookManager.java` (Método `updateAvailableWithLegacyRule`)

> **Antes:** O método possuía blocos `if/else` redundantes para calcular o novo número de cópias disponíveis com base em um `opCode`. A lógica de garantir que o valor não ficasse abaixo de zero ou acima do total era repetida em vários ramos.

> **Depois:** Simplificamos a estrutura calculando primeiro o valor pretendido (`next`) com base no `opCode` e, em seguida, aplicando as regras de limite (mínimo 0, máximo `total`) em um único ponto no final do método. Isso removeu a duplicação de código e tornou a regra de limite muito mais clara.

## 3. Evidência de Preservação de Comportamento

Após as refatorações, o sistema foi compilado e executado com os comandos de validação originais:

- `javac src/*.java` (Compilação OK)

- `java -cp src Main --list` (Saída idêntica à original, listando livros, usuários e o empréstimo do cenário anterior)

- `java -cp src Main --report` (Relatório gerado com sucesso, mantendo os bugs lógicos originais que serão corrigidos na Atividade 2)

