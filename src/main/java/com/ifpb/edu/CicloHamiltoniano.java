import java.util.*;

public class CicloHamiltoniano {
    private int n;
    private boolean[][] adj;
    private int[] path;
    private boolean[] visited;
    private long tempoInicio;
    private long tempoFim;
    private long chamadas;

    public CicloHamiltoniano(int n) {
        this.n = n;
        this.adj = new boolean[n][n];
        this.path = new int[n];
        this.visited = new boolean[n];
        this.chamadas = 0;
    }

    public void adicionarAresta(int u, int v) {
        adj[u][v] = true;
        adj[v][u] = true;
    }

    public boolean encontrarCicloHamiltoniano() {
        path[0] = 0;
        visited[0] = true;
        chamadas = 0;
        tempoInicio = System.nanoTime();
        boolean resultado = hamiltonianoCycleUtil(1);
        tempoFim = System.nanoTime();
        return resultado;
    }

    private boolean hamiltonianoCycleUtil(int pos) {
        chamadas++;
        
        if (pos == n) {
            return adj[path[pos - 1]][path[0]];
        }

        for (int v = 1; v < n; v++) {
            if (adj[path[pos - 1]][v] && !visited[v]) {
                path[pos] = v;
                visited[v] = true;

                if (hamiltonianoCycleUtil(pos + 1)) {
                    return true;
                }

                visited[v] = false;
            }
        }

        return false;
    }

    public long getTempoNano() {
        return tempoFim - tempoInicio;
    }

    public long getTempoMili() {
        return (tempoFim - tempoInicio) / 1_000_000;
    }

    public double getTempoMicro() {
        return (tempoFim - tempoInicio) / 1000.0;
    }

    public double getTempoSeg() {
        return (tempoFim - tempoInicio) / 1_000_000_000.0;
    }

    public long getChamadas() {
        return chamadas;
    }

    public int[] getCaminho() {
        return path;
    }

    // PIOR CASO EXTREMO: Grafo bipartido completo
    // Força a exploração de TODAS as permutações
    public static CicloHamiltoniano criarPiorCasoExtremo(int n) {
        CicloHamiltoniano ciclo = new CicloHamiltoniano(n);
        
        int meio = n / 2;
        
        // Grupo 1: vértices 0 a meio-1
        // Grupo 2: vértices meio a n-1
        
        // Conecta TODO vértice do grupo 1 com TODO vértice do grupo 2
        for (int i = 0; i < meio; i++) {
            for (int j = meio; j < n; j++) {
                ciclo.adicionarAresta(i, j);
            }
        }
        
        // Sem arestas dentro dos grupos
        // Isso força o algoritmo a alternar entre grupos
        // e explorar todas as permutações possíveis
        
        return ciclo;
    }

    // Grafo com solução óbvia (caso fácil para comparação)
    public static CicloHamiltoniano criarGrafoFacil(int n) {
        CicloHamiltoniano ciclo = new CicloHamiltoniano(n);
        
        // Ciclo simples + muitas arestas (probabilidade 0.6)
        Random rand = new Random(42);
        
        for (int i = 0; i < n - 1; i++) {
            ciclo.adicionarAresta(i, i + 1);
        }
        ciclo.adicionarAresta(n - 1, 0);
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 2; j < n; j++) {
                if (rand.nextDouble() < 0.6 && !(i == 0 && j == n - 1)) {
                    ciclo.adicionarAresta(i, j);
                }
            }
        }
        
        return ciclo;
    }

    // Grafo esparso com muito backtracking
    public static CicloHamiltoniano criarGrafoEsparsoExtermo(int n) {
        CicloHamiltoniano ciclo = new CicloHamiltoniano(n);
        
        // Cria um padrão que força muitas exploração mas não tem ciclo
        // Apenas conecta vizinhos imediatos
        for (int i = 0; i < n; i++) {
            ciclo.adicionarAresta(i, (i + 1) % n);
        }
        
        // Remove apenas UMA aresta crítica para quebrar o ciclo
        ciclo.adj[n - 1][0] = false;
        ciclo.adj[0][n - 1] = false;
        
        return ciclo;
    }

    public static void main(String[] args) {
        System.out.println("===== TESTE 1: CASO FÁCIL (Grafo Altamente Conectado) =====\n");
        System.out.println("n  | Encontrado | Tempo (seg)  | Tempo (ms)   | Chamadas Recursivas");
        System.out.println("----------------------------------------------------------------------");

        for (int n = 10; n <= 18; n++) {
            CicloHamiltoniano ciclo = criarGrafoFacil(n);
            boolean encontrado = ciclo.encontrarCicloHamiltoniano();
            
            System.out.printf("%2d | %s | %12.6f | %12d | %18d%n",
                    n,
                    encontrado ? "sim " : "não ",
                    ciclo.getTempoSeg(),
                    ciclo.getTempoMili(),
                    ciclo.getChamadas());
        }

        System.out.println("\n===== TESTE 2: PIOR CASO EXTREMO (Grafo Bipartido - Força Backtracking) =====\n");
        System.out.println("n  | Encontrado | Tempo (seg)  | Tempo (ms)     | Chamadas Recursivas");
        System.out.println("----------------------------------------------------------------------");

        for (int n = 8; n <= 20; n++) {
            CicloHamiltoniano ciclo = criarPiorCasoExtremo(n);
            boolean encontrado = ciclo.encontrarCicloHamiltoniano();
            
            System.out.printf("%2d | %s | %12.6f | %14d | %18d%n",
                    n,
                    encontrado ? "sim " : "não ",
                    ciclo.getTempoSeg(),
                    ciclo.getTempoMili(),
                    ciclo.getChamadas());
            
            // Para n >= 18, mostra progresso e tempo estimado
            if (n >= 18) {
                double tempoSeg = ciclo.getTempoSeg();
                if (tempoSeg > 15) {
                    System.out.println(">>> TEMPO LIMITE ATINGIDO! Abortando testes maiores.\n");
                    break;
                }
            }
        }

        System.out.println("\n===== TESTE 3: GRAFO ESPARSO COM ARESTA CRÍTICA REMOVIDA =====\n");
        System.out.println("n  | Encontrado | Tempo (seg)  | Tempo (ms)     | Chamadas Recursivas");
        System.out.println("----------------------------------------------------------------------");

        for (int n = 8; n <= 20; n++) {
            CicloHamiltoniano ciclo = criarGrafoEsparsoExtermo(n);
            boolean encontrado = ciclo.encontrarCicloHamiltoniano();
            
            System.out.printf("%2d | %s | %12.6f | %14d | %18d%n",
                    n,
                    encontrado ? "sim " : "não ",
                    ciclo.getTempoSeg(),
                    ciclo.getTempoMili(),
                    ciclo.getChamadas());
            
            if (n >= 18) {
                double tempoSeg = ciclo.getTempoSeg();
                if (tempoSeg > 15) {
                    System.out.println(">>> TEMPO LIMITE ATINGIDO! Abortando testes maiores.\n");
                    break;
                }
            }
        }

        System.out.println("\n===== ANÁLISE DE CRESCIMENTO EXPONENCIAL =====\n");
        System.out.println("Observações:");
        System.out.println("1. TESTE 1 (fácil): Tempo ~constante, poucas chamadas recursivas");
        System.out.println("2. TESTE 2 (pior caso): Crescimento EXPONENCIAL em tempo e chamadas");
        System.out.println("3. TESTE 3 (esparso): Também mostra crescimento exponencial");
        System.out.println("\nA complexidade é O(n!) no pior caso, confirmando a teoria NP-Completo!");
    }
}
