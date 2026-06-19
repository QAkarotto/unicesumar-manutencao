# Exercícios de fixação do conteúdo da prova 2

1. Como o Princípio de Pareto (80/20) se aplica à distribuição empírica de defeitos em sistemas de software?
2. Qual deve ser a prioridade da equipe de manutenção e alocação de esforço ao identificar as "áreas quentes" de um sistema?
3. Por que tratar todos os pacotes de um sistema legado com o mesmo nível de cobertura de testes gera desperdício financeiro?
4. O que é a "Dívida Técnica Estratégica" e qual é a condição principal para que ela seja assumida de forma saudável?
5. Qual é a diferença fundamental entre uma dívida técnica assumida conscientemente pelo negócio e o desleixo técnico crônico de uma equipe?
6. Na metáfora da Dívida Técnica, o que representa na prática o "Pagamento do Principal"?
7. Na metáfora da Dívida Técnica, o que representam os "Juros Diários" pagos pela equipe de desenvolvimento?
8. Qual é a principal diferença de rastreabilidade entre o registro de dívida SATD-C e o SATD-I?
9. Por que o registro de dívidas técnicas através de Issues (SATD-I) é gerencialmente superior ao uso de tags nos comentários de código (SATD-C)?
10. Por que Michael Feathers afirma que "código sem testes é, por definição, um código ruim"?
11. Qual é o papel fundamental da "rede de segurança" criada por testes automatizados durante manutenções evolutivas em sistemas legados?
12. Por que arquiteturas fortemente acopladas (Big Ball of Mud) rejeitam e dificultam a criação de testes de unidade rápidos?
13. O que é o antipadrão "Cone de Sorvete" (Ice Cream Cone) no contexto de testes de um sistema legado?
14. O que é Refatoração estrutural de código e no que ela difere da reescrita total de um sistema?
15. Qual é o risco crítico de se refatorar um módulo imenso de código legado sem o amparo prévio de testes automatizados?
16. Qual é o objetivo central e inegociável do "Passo 1" do Algoritmo de Depuração empírica?
17. Por que é recomendado escrever um teste automatizado "que falhe intencionalmente" antes de iniciar a alteração de código para consertar um bug?
18. Como a transição de um teste do estado "Vermelho" (falha) para o "Verde" (sucesso) atesta a validade do patch de correção?
19. O que significa o procedimento de "Refinamento da Entrada" na investigação de um defeito complexo?
20. Como o ato de reduzir iterativamente um arquivo de entrada de 1000 linhas para apenas 3 linhas acelera a descoberta da causa raiz de um bug?
21. Como funciona, na prática, a técnica de Rubber Duck Debugging (Depuração com Pato de Borracha)?
22. Qual é o mecanismo cognitivo que torna a verbalização do código linha a linha eficaz para desmascarar falhas lógicas silenciosas?
23. O que a curva de "Custo da Mudança" (Cost of Change) demonstra a respeito da evolução de um software?
24. Por que consertar um erro de requisito de negócio em Produção é exponencialmente mais caro do que corrigir um erro de lógica na etapa de codificação?
25. Quais são os três conjuntos de informações obrigatórias que devem compor um relatório de bug de qualidade?
26. Como a omissão do campo "Comportamento Esperado" em um relatório de bug prejudica a etapa de validação do conserto pela engenharia?
27. Por que um relatório de bug contendo unicamente a frase descritiva "A tela travou" é inútil para a equipe de SRE iniciar a manutenção?
28. Sabendo que os programadores gastam quase 60% de suas jornadas apenas lendo código, qual é a principal vantagem financeira e operacional da adoção do Clean Code?
29. Qual é a regra de ouro estipulada pela engenharia moderna para a redação de "Comentários de Implementação" em métodos internos?
30. Por que os comentários internos devem focar estritamente no "porquê" das decisões técnicas e omitir a tradução literal de "o que" a sintaxe executa?
31. O que são "Comentários Zumbis" (ou Comentários Mentirosos) e como eles afetam a manutenibilidade do sistema a longo prazo?
32. Qual é a consequência grave de se imprimir variáveis com PII (Informações de Identificação Pessoal, como senhas e CPFs) diretamente em arquivos de log?
33. Qual deve ser o procedimento técnico obrigatório aplicado aos dados de clientes antes da sua injeção nas bibliotecas de geração de logs?
34. Qual é a utilidade operacional técnica da implementação massiva de registros de Logs em sistemas distribuídos que rodam em nuvem?
35. O que caracteriza a estratégia de descontinuação de sistemas conhecida como "Migração Big Bang"?
36. Por que a "Migração Big Bang" é considerada altamente arriscada e temida em softwares corporativos críticos?
37. Como funciona a operação arquitetural do padrão Strangler Fig (Figueira Estranguladora) em sistemas legados?
38. Como o padrão Strangler Fig garante a segurança das transações do cliente caso o novo microsserviço apresente falhas em produção?
39. O que caracteriza o "Código Morto" (Dead Code) dentro de um repositório institucional?
40. O que indica a métrica gerencial Truck Factor (Fator Caminhão) quando o seu valor é igual a 1?
41. Como a obrigatoriedade das Revisões de Código (Code Reviews) age para mitigar os gargalos de conhecimento expostos pelo Truck Factor?
42. O que caracteriza uma Manutenção de Software classificada na categoria "Adaptativa"?
43. O que são Magic Numbers (Números Mágicos) e qual é a refatoração indicada para removê-los do código?
44. Como o uso rigoroso de Guias de Estilo (padronização de margens e chaves) otimiza o fluxo de controle de versão (Git) da equipe?
45. O que é o antipadrão da "Superengenharia" (Overengineering) e por que a sua aplicação viola frontalmente as premissas do princípio YAGNI?
