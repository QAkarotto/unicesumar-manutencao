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
Testabilidade
5. Excesso de parâmetros

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
