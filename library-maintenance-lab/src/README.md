# Atividade 2 – Manutenção Corretiva e Evolutiva

Esta atividade foca em **manutenção corretiva** (correção de falhas reais) e **manutenção evolutiva** (adição de nova funcionalidade), mantendo o comportamento externo do sistema legado.

## Correções realizadas em LoanManager.java

Na manutenção corretiva do arquivo `LoanManager.java`, foram corrigidos três bugs reais do sistema:

1. **Correção do comportamento de empréstimo não encontrado em `returnBook()`**  
   O sistema passou a lançar exceção quando o empréstimo informado não existe, evitando retorno silencioso e facilitando a identificação do erro.

2. **Correção do bug de empréstimo duplicado no canal SMS**  
   Foi removida a criação de um segundo empréstimo no fluxo de notificações por SMS, mantendo apenas um registro correto por operação.

3. **Correção do cálculo da dívida do usuário**  
   A lógica foi ajustada para que a multa aumente a dívida do usuário, em vez de diminuí-la.

Essas mudanças mantiveram o restante do fluxo de `borrowBook()` e `returnBook()` inalterado, garantindo que o comportamento externo do sistema permaneça consistente.

## Como reproduzir os bugs (antes e depois)

1. **Empréstimo não encontrado em `returnBook()`**  
   - Antes: chamar `returnBook()` com empréstimo inexistente resultava em retorno silencioso, sem aviso ao usuário.  
   - Depois: o método passa a lançar exceção clara, indicando que o empréstimo não foi encontrado.

2. **Empréstimo duplicado no canal SMS**  
   - Antes: ao usar o canal SMS, um segundo empréstimo era criado no mesmo fluxo, gerando duplicidade.  
   - Depois: apenas um único empréstimo é registrado para a mesma operação.

3. **Erro de cálculo na dívida do usuário**  
   - Antes: ao aplicar multa/juros, o sistema **subtraía** o valor da dívida.  
   - Depois: a lógica foi corrigida para **somar** o valor da multa à dívida do usuário.

## Nova funcionalidade implementada

- **Histórico de empréstimos por usuário**  
  Foi criado o método `LoanManager.listLoansByUser(String userId)` que retorna a lista de todos os empréstimos de um usuário específico, organizando a lógica de consulta de empréstimos em uma operação própria.

A nova funcionalidade:

- foi adicionada de forma **incremental**, sem reescrever o sistema;  
- mantém as operações existentes funcionando normalmente (cadastro, empréstimo, devolução, relatórios).

## Impactos e riscos conhecidos

- As correções de `LoanManager` não alteraram regras de negócio, apenas **tornaram o tratamento de erro mais claro**.  
- A nova funcionalidade adiciona comportamento adicional, mas **utiliza os componentes existentes** (BookManager, UserManager, LegacyDatabase) de forma consistente.  
- Riscos observados:
  - introdução de nova lógica de validação;  
  - necessidade de testes manuais adicionais para garantir que regras de limite não sejam quebradas.

## Validação do comportamento

Antes e depois das mudanças foram executados:

```bash
javac src/*.java
java -cp src Main
java -cp src Main --list
java -cp src Main --report
```

O sistema continua:

- compilando sem erros;  
- executando o menu interativo;  
- listando livros;  
- gerando relatórios com o mesmo comportamento anterior,  
enquanto os bugs foram corrigidos e a nova funcionalidade funciona conforme esperado.

## Observação final

O objetivo não foi reescrever o sistema, e sim simular **manutenção real** com:
- correções de bugs reais (`LoanManager`);  
- e evolução incremental com uma nova funcionalidade.
