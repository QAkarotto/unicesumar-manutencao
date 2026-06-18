# Atividade 3 – Debugging e Observabilidade (2º Bimestre)

Esta atividade foca na depuração de bugs em Java, na aplicação de programação por contrato para falhar rápido e na instrumentação de logs para dar visibilidade ao comportamento do sistema.

## Objetivo

Aplicar o algoritmo de depuração resolvendo 3 bugs reportados na aula passada, proteger a aplicação com programação por contrato (Fail Fast) usando os recursos do Java e implementar instrumentação de logging utilizando a biblioteca Log4j.

## Exemplo de Apoio

Para orientar a implementação, consulte o exemplo didático em `library-maintenance-lab`.

- Para executar os testes localmente, o Maven deve estar instalado e disponível no `PATH` da máquina.
- `library-maintenance-lab/pom.xml`: configuração Maven para executar testes unitários e logs com Log4j 2
- `library-maintenance-lab/src/test/java/LoanManagerTest.java`: exemplo de teste unitário para reprodução e validação de comportamento
- `library-maintenance-lab/src/main/java/ProgramacaoPorContratoExample.java`: exemplo de Programação por Contrato com `assert`
- `library-maintenance-lab/src/LoggingExample.java`: exemplo de logging com registros de INFO e ERROR
- `library-maintenance-lab/src/main/resources/log4j2.xml`: configuração de console usada para exibir os logs

## Etapa 1: Reprodução Automatizada

O passo mais importante da depuração empírica é conseguir reproduzir o erro isoladamente. A melhor forma de provar que o problema foi localizado é escrevendo um teste automatizado que falhe por causa do bug.

### Ação no Repositório

- Antes de rodar os testes, instale o Maven na sua máquina e verifique se o comando `mvn` funciona no terminal.
- Escolha 3 bugs que estejam na coluna `In Progress` do seu GitHub Projects, como uma falha no método de empréstimo ou no `getBookByISBN` da classe `LibraryServices`.
- Navegue até a pasta de testes do projeto, geralmente `src/test/java`.
- Escreva um teste de unidade utilizando o framework JUnit que simule a entrada exata que causa o erro, como buscar um livro inexistente ou realizar um empréstimo com usuário nulo.
- Execute o teste e tire um print provando que ele falhou, com a barra vermelha no JUnit.
- Use o cenário mais minimalista possível para simplificar a reprodução do problema.

## Etapa 2: Correção e Programação por Contrato

Agora que localizamos as causas raiz, precisamos consertar o código e blindá-lo contra falhas futuras implementando as pré-condições do contrato.

### Ação no Repositório

- Nos métodos onde os defeitos foram localizados, no diretório `src/main/java`, faça a correção da lógica do algoritmo.
- Aplique a Programação por Contrato. No início do método, adicione uma verificação de pré-condição utilizando o comando nativo `assert` do Java ou lançando uma exceção, como `IllegalArgumentException`, para que o sistema adote o comportamento `Fail Fast`.
- Execute novamente o teste do JUnit criado no Passo 1 e tire um print provando que ele agora passa com sucesso, com a barra verde.

## Etapa 3: Visibilidade em Produção

Bugs silenciosos são difíceis de rastrear. Vamos gerar um histórico adequado de execução para a equipe de monitoramento utilizando os níveis de logging.

### Ação no Repositório

- Na classe que foi consertada, instancie o logger do Log4j com `private static final Logger logger = LogManager.getLogger(SuaClasse.class);`.
- Registre um evento de negócio no caminho feliz da execução com nível `INFO`.
- Registre um erro grave com nível `ERROR` em um bloco `catch` ou cenário de falha, passando a `Exception` completa para o logger e não apenas o `.getMessage()`.
- É terminantemente proibido utilizar concatenação de strings nas mensagens de log por questões de performance. Utilize sempre placeholders `{}`.

## Etapa 4: Atualização do Histórico e Fechamento no Board

Os bugs foram corrigidos, os testes garantem que não há regressões e os logs dão visibilidade ao fluxo. Agora é hora de empacotar a versão.

### Ação no GitHub

- Faça o commit da solução utilizando estritamente o padrão Conventional Commits.
- Na mensagem ou no rodapé do commit, adicione `Closes #numero_da_issue` para atrelar a correção à tarefa.
- Envie o código para o repositório com `git push`.
- No GitHub Projects, arraste o card do bug da coluna `In Progress` para a coluna `Done`.

## Formato de Entrega

### Repositório da Entrega

1. A entrega deve ser feita em um repositório público no GitHub.
2. O repositório deve ser um fork do repositório da disciplina.
3. O project e as issues devem estar visíveis no repositório.

### Evidências

A entrega deve incluir evidências da execução do teste, dos logs, da issue e do commit.

- Relatório de execução do teste falhando
- Relatório de execução do teste passando após a correção
- Screenshots dos logs de caminho feliz e de erro exibidos no terminal durante a execução
- Issue finalizada no GitHub Projects
- Commit realizado com a correção

### Envio por E-mail

Após publicar o repositório no GitHub, enviar o link por e-mail para:

**joao.vsantos@unicesumar.edu.br**

O e-mail deve seguir este formato:

**Título do e-mail:**

```text
Atividade 3 Manutenção ESOFT5S 2BIM
```

**Corpo do e-mail:**

```text
Integrantes:

Nome completo de cada integrante da equipe
Usuário do GitHub de cada integrante

Repositório: https://github.com/seu-usuario/seu-fork
Project: https://github.com/seu-usuario/seu-fork/projects/XX
```

## Apresentação
Apresentar em sala ou em vídeo gravado explicando como foi realizada a implementação.

## Prazo de Entrega

**Data**: 21/05/2026  
**Horário**: até 19h

## Checklist de Validação

Antes de entregar, validar:

- [ ] 2 bugs reproduzidos com teste automatizado
- [ ] Correção aplicada no código principal
- [ ] Programação por contrato adicionada com `assert` ou exceção
- [ ] Logs `INFO` e `ERROR` implementados com Log4j
- [ ] Mensagens de log sem concatenação de strings
- [ ] Testes passando após a correção
- [ ] Card movido para `Done` no GitHub Projects
- [ ] Commit realizado no padrão Conventional Commits
- [ ] E-mail preparado com o link do repositório e project