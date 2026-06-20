# Library Maintenance Lab

`library-maintenance-lab` e um laboratorio em Java para estudo de manutencao de software em um sistema legado de biblioteca. O projeto reune codigo propositalmente simples e pontos de evolucao para apoiar analises, correcoes e pequenas melhorias incrementais.

## Sobre o projeto

O sistema inclui funcionalidades de cadastro de livros e usuarios, emprestimo e devolucao, geracao de relatorios e execucao por linha de comando. O objetivo e servir como base para estudo de manutencao de software, nao como uma aplicacao pronta para producao.

### Principais componentes

- `src/main/java/`: codigo-fonte principal do laboratorio
- `src/test/java/LoanManagerTest.java`: exemplo de teste unitario com JUnit 4
- `src/main/resources/log4j2.xml`: configuracao de logging com Log4j 2
- `pom.xml`: build Maven, testes e execucao do projeto

## Pre-requisitos

- Java 8 ou superior
- Maven 3.8+ recomendado

## Como executar

Executar a suite de testes:

```bash
mvn test
```

Executar a aplicacao principal:

```bash
mvn -q exec:java -Dexec.mainClass=Main
```

Executar com argumento de listagem, se aplicavel ao fluxo atual:

```bash
mvn -q exec:java -Dexec.mainClass=Main -Dexec.args="--list"
```

Executar o exemplo de logging:

```bash
mvn -q exec:java -Dexec.mainClass=LoggingExample
```

## Estrutura do repositorio

- `src/main/java/`: classes do sistema legado e utilitarios
- `src/main/resources/`: recursos da aplicacao
- `src/test/java/`: testes automatizados
- `README.md`: visao geral do projeto e instrucoes de execucao

## Observacao

O foco do laboratorio e permitir evolucao controlada do codigo, com mudancas pequenas e validacao frequente, preservando o comportamento existente sempre que possivel.

## Lista dos problemas encontrados:

Descrição do Projeto

Este projeto tem como objetivo a análise de um sistema legado de gerenciamento de biblioteca, fornecido pelo professor, com foco na identificação e correção de problemas estruturais e lógicos.

A proposta consiste em aplicar boas práticas de engenharia de software, incluindo:

Refatoração de código
Correção de bugs
Melhoria de legibilidade e manutenção
Aplicação de princípios como SRP (Single Responsibility Principle)
Objetivos
Identificar falhas no código original
Classificar os problemas encontrados
Corrigir erros de lógica e implementação
Melhorar a estrutura do código seguindo boas práticas
Tornar o sistema mais modular, legível e sustentável
Tecnologias Utilizadas
Java
Programação Orientada a Objetos (POO)
Estruturas de dados básicas
Manipulação de coleções (Map, List)
Problemas Identificados e Correções

1. Erro no cálculo de empréstimos

Classe: ReportGenerator
Problema:
O sistema contabilizava incorretamente o número de empréstimos, incluindo empréstimos ainda em aberto.

Tipo de erro:

Bug de cálculo

Correção aplicada:
Foi ajustada a lógica para considerar apenas empréstimos finalizados, garantindo maior precisão nos relatórios.

2. Código duplicado

Método: handleRegisterBook() — Classe LibrarySystem

Problema:
Existia uma verificação duplicada que já era realizada na classe BookManager.

Tipo de erro:

Código duplicado

Correção aplicada:
Remoção da redundância, delegando a responsabilidade corretamente para a camada adequada, evitando inconsistências e facilitando manutenção.

3. Lógica inconsistente na contagem de empréstimos

Método: countOpenLoansByBook() — Classe LegacyDatabase

Problema:
O método utilizava lógica incorreta, filtrando dados de forma inconsistente devido a problemas de nomeação e implementação.

Tipo de erro:

Nomeação inadequada
Lógica inconsistente

Correção aplicada:
Ajuste da lógica de filtragem para garantir que a contagem seja feita corretamente com base no identificador do livro.

4. Método excessivamente longo

Método: borrowBook()

Problema:
O método possuía múltiplas responsabilidades, dificultando leitura, testes e manutenção.

Tipo de erro:

Método longo (violação do princípio de responsabilidade única)

Correção aplicada:
Refatoração do método em funções menores, cada uma com uma responsabilidade específica, melhorando:

Legibilidade
Reutilização
Testabilidade 5. Excesso de parâmetros

Método: registerUser()

Problema:
O método possuía muitos parâmetros, tornando sua utilização confusa e propensa a erros.

Tipo de erro:

Long Parameter List

Correção aplicada:
Uso de um objeto (UserData) para encapsular os dados do usuário, reduzindo a complexidade e melhorando a organização.

Melhorias Aplicadas
Aplicação de boas práticas de POO
Redução de acoplamento
Aumento da coesão entre classes
Código mais limpo e legível
Melhor separação de responsabilidades
Resultados Obtidos

Após as correções, o sistema apresentou:

Maior confiabilidade nos dados
Melhor organização estrutural
Código mais fácil de manter e evoluir
Redução de redundâncias e erros lógicos

6. Classe com muitas responsabilidades

Classe: LibrarySystem

Problema:
A classe centralizava diversas funções como:

Interface (menu)
Entrada de dados
Regras de negócio
Logs

Tipo de erro:

God Class

Correção aplicada:
Separação das responsabilidades em diferentes classes, promovendo melhor organização e manutenção.

7. Falha de validação de dados

Método: registerBook() — Classe BookManager

Problema:
Permitida a criação de livros com título em branco, violando regras de negócio.

Tipo de erro:

Erro de validação

Correção aplicada:
Implementação de validação obrigatória para o título do livro.

8. Tratamento de erro inconsistente

Método: returnBook() — Classe LoanManager

Problema:
O método falhava ao lidar com empréstimos nulos, interrompendo a execução sem feedback adequado.

Tipo de erro:

Tratamento de erro inconsistente

Correção aplicada:
Padronização do tratamento de erros, garantindo mensagens adequadas e continuidade segura do sistema.

9. Responsabilidades misturadas

Método: startCli() — Classe LibrarySystem

Problema:
Mistura de:

Interface do usuário
Regras de execução
Tratamento de erros

Tipo de erro:

Baixa coesão / responsabilidades misturadas

Correção aplicada:
Separação das responsabilidades em camadas distintas, melhorando organização e clareza.

10. Código redundante

Método: validateUserData() — Classe UserManager

Problema:
Realizava validações já executadas no momento do cadastro do usuário.

Tipo de erro:

Código duplicado

Correção aplicada:
Centralização das validações em um único ponto, evitando redundância.

## Refatorações realizadas:

1. Empréstimos fechados - Corrigido a contagem de empréstimos fechados.

2. handleRegisterBook() - Retirada a parte duplicada do código.

3. countOpenLoansByBook() - Corrigido a lógica do método.

4. borrowBook() - O método foi dividido em métodos menores e mais específicos.

5. registerUser() - Foi criada a classe UserData para passar os parâmetros necessáriso para a execução do método.

## Videos de demonstração de cada parcipante consta na pasta /videos
