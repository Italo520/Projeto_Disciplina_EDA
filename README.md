# Implementação e Análise do Problema do Ciclo Hamiltoniano

Este projeto contém a implementação de um algoritmo de *backtracking* para resolver o problema do Ciclo Hamiltoniano, um problema clássico de Teoria dos Grafos classificado como NP-Completo.

O trabalho foi desenvolvido como parte da disciplina de Estrutura de Dados e Algoritmos.

## Como Executar os Testes de Performance

O código principal (`CicloHamiltoniano.java`) já contém uma suíte de testes de performance que analisa o comportamento do algoritmo em diferentes cenários (caso fácil, pior caso, etc.).

Para compilar o projeto e executar esses testes, utilize o seguinte comando Maven na raiz do projeto:

```bash
mvn clean compile exec:java -Dexec.mainClass="com.ifpb.edu.CicloHamiltoniano"
```

Os resultados, incluindo tempo de execução e número de chamadas recursivas, serão impressos no console.
