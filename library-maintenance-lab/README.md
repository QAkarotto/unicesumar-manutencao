# Laboratório de Manutenção de Software – Sistema Legado de Biblioteca

Este repositório simula um sistema Java legado que evoluiu ao longo do tempo com múltiplas mudanças incrementais, correções rápidas e decisões arquiteturais de curto prazo. O resultado é um código funcional, porém com alta complexidade de manutenção, ideal para práticas reais de manutenção de software.

## Contexto do Sistema

Com base na implementação atual, o sistema oferece:

- cadastro de livros ([BookManager.registerBook](src/BookManager.java#L10))
- cadastro de usuários ([UserManager.registerUser](src/UserManager.java#L5))
- empréstimo de livros ([LoanManager.borrowBook](src/LoanManager.java#L14))
- devolução de livros ([LoanManager.returnBook](src/LoanManager.java#L90))
- geração de relatórios ([ReportGenerator.generateSimpleReport](src/ReportGenerator.java#L9))
- operação via menu de linha de comando ([LibrarySystem.startCli](src/LibrarySystem.java#L23))

O projeto contém intencionalmente problemas de manutenibilidade e bugs sutis para apoiar atividades práticas de manutenção preventiva, corretiva e evolutiva.

## Organização das Atividades

As atividades foram separadas em documentos próprios para deixar objetivos, escopo e formato de entrega mais claros:

1. [ATIVIDADE_1.md](../ATIVIDADES/1BIM/ATIVIDADE_1.md) - Análise de Código e Manutenção Preventiva
2. [ATIVIDADE_2.md](../ATIVIDADES/1BIM/ATIVIDADE_2.md) - Manutenção Corretiva e Evolutiva

Data final de entrega: 16/04.

## Como Executar o Projeto

Compilar:

```bash
javac src/*.java
```

Executar modo interativo:

```bash
java -cp src Main
```

Executar listagem rápida:

```bash
java -cp src Main --list
```

Executar relatório rápido:

```bash
java -cp src Main --report
```

## Visão Geral de Problemas de Manutenibilidade

Problemas detalhados e guias de exploração foram movidos para os arquivos de atividade.

## Observação Final

O objetivo não é reescrever o sistema inteiro do zero.

Os estudantes devem melhorar o sistema incrementalmente, simulando manutenção de software no mundo real com pequenas mudanças seguras, validação contínua e evolução controlada.

## Teste

Teste de commit

## Teste 2 

Teste de Commit 2

# Problemas encontrados por - Kleber Junior

---

## 1. LibrarySystem.java - God Class

**Problema:** A classe `LibrarySystem` centraliza múltiplas responsabilidades como interface CLI, controle de fluxo, regras de negócio, debug e integração com outros serviços.

**Por que isso é um problema:**  
Viola o princípio de responsabilidade única (SRP), tornando a classe difícil de manter, testar e evoluir. Qualquer alteração pode impactar diversas partes do sistema.

---

## 2. LibrarySystem.java - startCli() - Mixed Responsibilities

**Problema:** O método `startCli()` mistura lógica de interface com o usuário, controle de fluxo e tratamento de erros.

**Por que isso é um problema:**  
Dificulta a leitura e manutenção, além de impedir reutilização de partes do código. Mudanças na interface podem afetar regras de negócio.

---

## 3. LibrarySystem.java - handleDebugArea() - Long Method / Deep Nesting

**Problema:** O método possui muitos níveis de `if/else` aninhados e múltiplas responsabilidades.

**Por que isso é um problema:**  
Código difícil de entender, manter e propenso a erros. Aumenta o risco de bugs ao modificar qualquer parte.

---

## 4. BookManager.java - registerBook(...) - Long Parameter List

**Problema:** O método possui muitos parâmetros primitivos.

**Por que isso é um problema:**  
Aumenta a chance de erros na chamada do método e dificulta manutenção e evolução do código.

---

## 5. BookManager.java - listBooksSimple() - Edge Case Bug

**Problema:** A linha que acessa `temp.get(0)` pode causar erro quando a lista está vazia.

**Por que isso é um problema:**  
Pode gerar exceção em tempo de execução (IndexOutOfBoundsException), quebrando o sistema.

---

## 6. BookManager.java - updateAvailableWithLegacyRule(...) - Magic Numbers

**Problema:** Uso de valores mágicos como `opCode == 1`, `opCode == 2`, `flag == 9`.

**Por que isso é um problema:**  
Dificulta o entendimento do código, pois não há significado explícito desses valores.

---

## 7. LoanManager.java - borrowBook(...) - Long Method

**Problema:** O método concentra validação, regras de negócio, persistência e notificação.

**Por que isso é um problema:**  
Dificulta manutenção, leitura e testes. Qualquer alteração pode introduzir novos erros.

---

## 8. LoanManager.java - borrowBook(...) - Deep Nesting

**Problema:** Uso excessivo de estruturas condicionais aninhadas.

**Por que isso é um problema:**  
Reduz legibilidade e aumenta complexidade, tornando o código mais propenso a falhas.

---

## 9. LoanManager.java - returnBook(...) - Inconsistent Error Handling

**Problema:** Quando o empréstimo não é encontrado, o método retorna silenciosamente ao invés de lançar erro.

**Por que isso é um problema:**  
Comportamento inconsistente pode esconder erros e dificultar debugging.

---

## 10. LegacyDatabase.java - countOpenLoansByBook(...) - Bug de Lógica

**Problema:** O método utiliza `userId` ao invés de `bookId` no filtro.

**Por que isso é um problema:**  
Retorna resultados incorretos, comprometendo a lógica de negócio do sistema.

---

## 11. LegacyDatabase.java - Global State / Hidden Dependencies

**Problema:** Uso extensivo de variáveis `static` compartilhadas globalmente.

**Por que isso é um problema:**  
Dificulta testes, causa dependência de estado global e pode gerar comportamentos inesperados.

---

## 12. DataUtil.java - Utility Class Overuse

**Problema:** Classe com muitos métodos estáticos e estado global (`scanner`, `retryCounter`).

**Por que isso é um problema:**  
Dificulta testes e reutilização, além de aumentar o acoplamento do sistema.

## Classificação dos Code Smells

- God Class → Baixa coesão / Alta responsabilidade
- Long Method → Baixa legibilidade / Complexidade alta
- Deep Nesting → Complexidade ciclomática elevada
- Long Parameter List → Baixa manutenibilidade
- Primitive Obsession → Falta de modelagem de domínio
- Magic Numbers → Baixa legibilidade
- Tight Coupling → Alto acoplamento
- Global State → Dependência oculta
- Inconsistent Error Handling → Falta de padronização
- Edge Case Bug → Falha de robustez

## Refatorações realizadas

- Aplicação de Early Return para reduzir aninhamento
- Extração de métodos (Extract Method)
- Substituição de Magic Numbers por constantes nomeadas
- Correção de bugs lógicos (countOpenLoansByBook)
- Tratamento de edge cases (lista vazia em listBooksSimple)
- Separação parcial de responsabilidades em métodos

## Evidência de preservação de comportamento

Antes das refatorações:
- Cadastro de livro funcionando corretamente
- Empréstimo de livro funcionando
- Devolução de livro funcionando

Depois das refatorações:
- Os mesmos fluxos foram executados
- As saídas no console permaneceram consistentes

Conclusão:
O comportamento externo do sistema foi preservado após as refatorações.