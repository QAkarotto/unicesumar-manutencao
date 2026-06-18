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
