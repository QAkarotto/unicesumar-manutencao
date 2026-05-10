# README 2 - Correções e melhorias complementares

Este documento apresenta a segunda etapa de manutenção do sistema, com foco em correções de comportamento, validações e implementação de funcionalidade adicional.

## Alterações realizadas

### 1. Correção no método `BookManager.listBooksSimple`

Foi corrigido o comportamento do método responsável pela listagem simples de livros quando a coleção está vazia.

Antes da correção, o sistema tentava acessar a posição `0` da lista mesmo quando não havia elementos, o que causava erro de execução.
A lógica foi ajustada para verificar se a lista está vazia com `isEmpty()`. Quando isso ocorre, o sistema apenas informa que não há livros disponíveis e encerra o método com segurança.

#### Resultado da alteração

- Evita falha por acesso a índice inexistente
- Melhora o tratamento de listas vazias
- Torna a execução mais segura e previsível

---

### 2. Correção do cálculo da dívida no `LoanManager`

Foi corrigido um bug na atualização da dívida do usuário durante o processo de devolução com multa.

Antes da correção, o valor da taxa era subtraído da dívida do usuário, o que gerava um comportamento incorreto no saldo devedor.
Após a correção, a dívida passa a ser atualizada da forma esperada, somando corretamente o valor da multa ao débito existente.

#### Resultado da alteração

- Corrige a regra de negócio relacionada à dívida do usuário
- Evita inconsistências financeiras no sistema
- Garante maior confiabilidade no controle de multas

---

### 3. Implementação de busca de empréstimos por usuário

Foi adicionada uma nova funcionalidade para consultar empréstimos com base no usuário informado.

Essa melhoria permite localizar com mais facilidade os registros de empréstimos vinculados a um usuário específico, facilitando consultas, validações e acompanhamento da situação dos livros emprestados.

#### Resultado da alteração

- Melhora a rastreabilidade dos empréstimos
- Facilita consultas no sistema
- Apoia rotinas de atendimento e conferência

---

### 4. Validação de título vazio ou nulo

Foi adicionada uma validação para impedir o cadastro ou processamento de registros com título inválido.

Agora, quando o campo `title` estiver vazio ou nulo, o sistema lança uma exceção informando que o título é inválido.
Essa validação evita a persistência de dados incompletos e melhora a integridade das informações armazenadas.

#### Resultado da alteração

- Impede registros com título em branco
- Melhora a qualidade dos dados
- Reduz problemas de validação em etapas posteriores

---

## Considerações finais

As alterações desta segunda etapa tiveram como objetivo aumentar a confiabilidade do sistema, corrigindo falhas de execução, ajustando regras de negócio e adicionando funcionalidades úteis para a operação.

De forma geral, as melhorias implementadas contribuíram para:

- maior estabilidade do sistema
- melhor validação dos dados
- redução de erros em tempo de execução
- melhor suporte às operações de consulta e controle
